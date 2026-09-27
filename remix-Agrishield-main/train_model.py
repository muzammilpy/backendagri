#!/usr/bin/env python3
"""
AgriShield Crop-Disease Model Training Pipeline v3.0
=====================================================
Trains a MobileNetV2-based classifier over 25 disease/healthy classes
merged from two Kaggle datasets:

  • vipoooool/new-plant-diseases-dataset  → data/PlantVillage/
  • janmejaybhoi/cotton-disease-dataset   → data/Cotton Disease/

Usage:
    # 1. Download datasets first:
    python download_dataset.py

    # 2. Train the model (task 1.1 only prepares data — training is task 1.2):
    python train_model.py

Requirements: tensorflow>=2.11, scikit-learn, numpy, matplotlib, seaborn
"""

import os
import sys
import json
import shutil
import time
import logging
from pathlib import Path

import numpy as np

# Silence TF C++ noise before the TF import
os.environ.setdefault("TF_CPP_MIN_LOG_LEVEL", "2")
os.environ.setdefault("TF_ENABLE_ONEDNN_OPTS", "0")

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

import tensorflow as tf
from sklearn.model_selection import train_test_split

# ---------------------------------------------------------------------------
# Logging
# ---------------------------------------------------------------------------
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s  %(levelname)-7s %(message)s",
    datefmt="%H:%M:%S",
)
log = logging.getLogger("agrishield.train")

# ---------------------------------------------------------------------------
# Hyper-parameters
# ---------------------------------------------------------------------------
IMG_SIZE: int = 224
BATCH_SIZE: int = 32
EPOCHS_HEAD: int = 10
EPOCHS_FINE: int = 20
LR_HEAD: float = 1e-3
LR_FINE: float = 5e-6

TRAIN_RATIO: float = 0.70
VAL_RATIO: float = 0.15
# Test ratio is implicitly 1 - TRAIN_RATIO - VAL_RATIO = 0.15
RANDOM_STATE: int = 42

# ---------------------------------------------------------------------------
# Filesystem roots
# ---------------------------------------------------------------------------
PLANTVILLAGE_ROOT = Path("data/PlantVillage")
COTTON_ROOT = Path("data/Cotton Disease")
MODEL_DIR = Path("models")
SPLIT_DIR = Path("data/_split")  # ephemeral symlink/copy tree for training

# ---------------------------------------------------------------------------
# 25-class mapping
# ---------------------------------------------------------------------------
# PlantVillage class folder names → canonical label
# Cotton Disease class folder names → canonical label
#
# PlantVillage uses the pattern  Crop___Condition  (three underscores).
# Cotton Disease uses plain English names.

PLANTVILLAGE_CLASSES: dict[str, str] = {
    # Corn / Maize (3 classes)
    "Corn_(maize)___Common_rust_": "Corn_(maize)___Common_rust",
    "Corn_(maize)___Northern_Leaf_Blight": "Corn_(maize)___Northern_Leaf_Blight",
    "Corn_(maize)___healthy": "Corn_(maize)___healthy",
    # Grape (2 classes)
    "Grape___Black_rot": "Grape___Black_rot",
    "Grape___healthy": "Grape___healthy",
    # Potato (3 classes)
    "Potato___Early_blight": "Potato___Early_blight",
    "Potato___Late_blight": "Potato___Late_blight",
    "Potato___healthy": "Potato___healthy",
    # Rice (3 classes)
    "Rice___Brown_Spot": "Rice___Brown_Spot",
    "Rice___Leaf_Blast": "Rice___Leaf_Blast",
    "Rice___healthy": "Rice___healthy",
    # Tomato (5 classes)
    "Tomato___Bacterial_spot": "Tomato___Bacterial_spot",
    "Tomato___Early_blight": "Tomato___Early_blight",
    "Tomato___Late_blight": "Tomato___Late_blight",
    "Tomato___Leaf_Mold": "Tomato___Leaf_Mold",
    "Tomato___healthy": "Tomato___healthy",
    # Wheat (3 classes)
    "Wheat___Brown_rust": "Wheat___Brown_rust",
    "Wheat___Yellow_rust": "Wheat___Yellow_rust",
    "Wheat___healthy": "Wheat___healthy",
}  # 21 PlantVillage classes

COTTON_CLASSES: dict[str, str] = {
    # Cotton Disease dataset folder names → canonical label
    "Bacterial Blight": "Cotton___Bacterial_Blight",
    "Curl Virus": "Cotton___Curl_Virus",
    "Fusarium Wilt": "Cotton___Fusarium_Wilt",
    "Healthy": "Cotton___healthy",
    # Some releases use lowercase / alternate spellings — include aliases:
    "bacterial blight": "Cotton___Bacterial_Blight",
    "curl virus": "Cotton___Curl_Virus",
    "fusarium wilt": "Cotton___Fusarium_Wilt",
    "healthy": "Cotton___healthy",
    # Alternate names used by some Kaggle versions
    "diseased cotton leaf": "Cotton___Bacterial_Blight",
    "diseased cotton plant": "Cotton___Fusarium_Wilt",
    "fresh cotton leaf": "Cotton___healthy",
    "fresh cotton plant": "Cotton___healthy",
}  # maps to 4 unique Cotton classes

# Combined canonical labels (deterministic sort = stable class indices)
# The union of PlantVillage (21) + Cotton (4) classes yields 25 unique labels.
# Cotton aliases (e.g., "diseased cotton leaf") intentionally map to the same
# canonical labels as the primary names, so the set is deduplicated correctly.
ALL_LABELS: list[str] = sorted(
    set(PLANTVILLAGE_CLASSES.values()) | set(COTTON_CLASSES.values())
)

# ---------------------------------------------------------------------------
# Per-class metadata used in model_metadata.json
# ---------------------------------------------------------------------------
CLASS_METADATA: dict[str, dict] = {
    # ---------- Cotton ----------
    "Cotton___Bacterial_Blight": {
        "display_name": "Cotton Bacterial Blight",
        "crop": "Cotton",
        "severity": "HIGH",
        "immediate_action": "Remove infected leaves. Apply Copper Oxychloride 50 WP @ 2.5g/L.",
        "likely_causes": ["Xanthomonas citri subsp. malvacearum"],
        "chemical": "Copper Oxychloride 50 WP @ 2.5g/L, PHI: 7 days",
        "biological": "Pseudomonas fluorescens 2g/L foliar spray",
        "ipm_cultural": "Crop rotation, certified seed, balanced NPK",
        "marathi": "कापसावरील जीवाणूजन्य करपा",
        "hindi": "कपास का जीवाणु झुलसा",
    },
    "Cotton___Curl_Virus": {
        "display_name": "Cotton Leaf Curl Virus",
        "crop": "Cotton",
        "severity": "CRITICAL",
        "immediate_action": "Uproot and destroy infected plants. Control whitefly vectors immediately.",
        "likely_causes": ["Cotton Leaf Curl Virus (CLCuV)", "Bemisia tabaci whitefly"],
        "chemical": "Imidacloprid 17.8 SL @ 0.3ml/L, PHI: 14 days",
        "biological": "Yellow sticky traps 20/acre; Verticillium lecanii 5g/L",
        "ipm_cultural": "Avoid ratoon cotton; plant resistant varieties; rogue infected plants",
        "marathi": "कापसावरील पानगुंडाळणी विषाणू",
        "hindi": "कपास की पत्ती मरोड़ विषाणु",
    },
    "Cotton___Fusarium_Wilt": {
        "display_name": "Cotton Fusarium Wilt",
        "crop": "Cotton",
        "severity": "CRITICAL",
        "immediate_action": "Uproot wilted plants. Drench soil with Carbendazim 1g/L.",
        "likely_causes": ["Fusarium oxysporum f.sp. vasinfectum"],
        "chemical": "Carbendazim 50 WP @ 1g/L soil drench, PHI: 7 days",
        "biological": "Trichoderma viride 5g/L seed treatment; T. harzianum soil application",
        "ipm_cultural": "Use wilt-resistant varieties; deep ploughing; crop rotation with legumes",
        "marathi": "कापसावरील मर रोग",
        "hindi": "कपास का उकठा रोग",
    },
    "Cotton___healthy": {
        "display_name": "Healthy Cotton",
        "crop": "Cotton",
        "severity": "LOW",
        "immediate_action": "Plant healthy. Scout every 7 days. Check pheromone trap ETL.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive NSKE 5% spray as per schedule; Trichogramma 50000/acre",
        "ipm_cultural": "Continue NPK fertilizer schedule; maintain field hygiene",
        "marathi": "निरोगी कापूस",
        "hindi": "स्वस्थ कपास",
    },
    # ---------- Corn ----------
    "Corn_(maize)___Common_rust": {
        "display_name": "Corn Common Rust",
        "crop": "Corn (Maize)",
        "severity": "MODERATE",
        "immediate_action": "Apply Propiconazole 25 EC @ 1ml/L at first sign.",
        "likely_causes": ["Puccinia sorghi"],
        "chemical": "Propiconazole 25 EC @ 1ml/L, PHI: 14 days",
        "biological": "No established biocontrol; focus on resistant hybrids",
        "ipm_cultural": "Plant resistant varieties; early sowing; balanced fertilization",
        "marathi": "मक्यावरील तांबेरा रोग",
        "hindi": "मक्के का साधारण रतुआ",
    },
    "Corn_(maize)___Northern_Leaf_Blight": {
        "display_name": "Corn Northern Leaf Blight",
        "crop": "Corn (Maize)",
        "severity": "HIGH",
        "immediate_action": "Apply Mancozeb 75 WP @ 2g/L. Remove severely infected leaves.",
        "likely_causes": ["Setosphaeria turcica (Exserohilum turcicum)"],
        "chemical": "Mancozeb 75 WP @ 2g/L or Azoxystrobin 23 SC @ 1ml/L, PHI: 7 days",
        "biological": "Bacillus subtilis-based bioagents; Trichoderma spray",
        "ipm_cultural": "Crop rotation; remove crop debris; use resistant hybrids",
        "marathi": "मक्यावरील उत्तरी पानावरील करपा",
        "hindi": "मक्के का उत्तरी पर्णदाह",
    },
    "Corn_(maize)___healthy": {
        "display_name": "Healthy Corn",
        "crop": "Corn (Maize)",
        "severity": "LOW",
        "immediate_action": "No action required. Continue regular scouting.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive Trichoderma application at sowing",
        "ipm_cultural": "Maintain proper spacing; balanced NPK",
        "marathi": "निरोगी मका",
        "hindi": "स्वस्थ मक्का",
    },
    # ---------- Grape ----------
    "Grape___Black_rot": {
        "display_name": "Grape Black Rot",
        "crop": "Grape",
        "severity": "HIGH",
        "immediate_action": "Remove and destroy infected berries and leaves. Apply Mancozeb.",
        "likely_causes": ["Guignardia bidwellii"],
        "chemical": "Mancozeb 75 WP @ 2.5g/L or Captan 50 WP @ 2g/L, PHI: 7 days",
        "biological": "Bacillus subtilis foliar spray; remove mummified berries",
        "ipm_cultural": "Prune for canopy airflow; remove infected debris; avoid overhead irrigation",
        "marathi": "द्राक्षांवरील काळी कुज",
        "hindi": "अंगूर का काला सड़न",
    },
    "Grape___healthy": {
        "display_name": "Healthy Grape",
        "crop": "Grape",
        "severity": "LOW",
        "immediate_action": "No action required. Monitor weekly.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive Bordeaux mixture spray",
        "ipm_cultural": "Maintain canopy management; regular irrigation schedule",
        "marathi": "निरोगी द्राक्षे",
        "hindi": "स्वस्थ अंगूर",
    },
    # ---------- Potato ----------
    "Potato___Early_blight": {
        "display_name": "Potato Early Blight",
        "crop": "Potato",
        "severity": "MODERATE",
        "immediate_action": "Apply Mancozeb 75 WP @ 2g/L. Remove lower infected leaves.",
        "likely_causes": ["Alternaria solani"],
        "chemical": "Mancozeb 75 WP @ 2g/L or Chlorothalonil 75 WP @ 2g/L, PHI: 5 days",
        "biological": "Trichoderma harzianum soil drench; Bacillus subtilis foliar",
        "ipm_cultural": "Avoid overhead irrigation; crop rotation; balanced nutrition",
        "marathi": "बटाट्यावरील अगोदरचा करपा",
        "hindi": "आलू का अगेती झुलसा",
    },
    "Potato___Late_blight": {
        "display_name": "Potato Late Blight",
        "crop": "Potato",
        "severity": "CRITICAL",
        "immediate_action": "Apply Metalaxyl + Mancozeb @ 2.5g/L immediately. Destroy infected haulm.",
        "likely_causes": ["Phytophthora infestans"],
        "chemical": "Metalaxyl 8% + Mancozeb 64% WP @ 2.5g/L, PHI: 7 days",
        "biological": "No effective biocontrol; focus on preventive fungicides",
        "ipm_cultural": "Plant certified disease-free seed; ridge planting; destroy infected material",
        "marathi": "बटाट्यावरील उशिरा येणारा करपा",
        "hindi": "आलू का पिछेती झुलसा",
    },
    "Potato___healthy": {
        "display_name": "Healthy Potato",
        "crop": "Potato",
        "severity": "LOW",
        "immediate_action": "No action required. Scout every 7 days.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive Trichoderma seed treatment",
        "ipm_cultural": "Use certified seed tubers; maintain soil health",
        "marathi": "निरोगी बटाटा",
        "hindi": "स्वस्थ आलू",
    },
    # ---------- Rice ----------
    "Rice___Brown_Spot": {
        "display_name": "Rice Brown Spot",
        "crop": "Rice",
        "severity": "MODERATE",
        "immediate_action": "Apply Propiconazole 25 EC @ 1ml/L. Check potassium nutrition.",
        "likely_causes": ["Cochliobolus miyabeanus (Helminthosporium oryzae)"],
        "chemical": "Propiconazole 25 EC @ 1ml/L or Tricyclazole 75 WP @ 0.6g/L, PHI: 14 days",
        "biological": "Pseudomonas fluorescens 2g/L foliar spray",
        "ipm_cultural": "Balanced NPK; avoid water stress; use resistant varieties",
        "marathi": "भाताचा तपकिरी ठिपका",
        "hindi": "धान का भूरा धब्बा",
    },
    "Rice___Leaf_Blast": {
        "display_name": "Rice Leaf Blast",
        "crop": "Rice",
        "severity": "HIGH",
        "immediate_action": "Apply Tricyclazole 75 WP @ 0.6g/L immediately. Reduce N fertilizer.",
        "likely_causes": ["Magnaporthe oryzae"],
        "chemical": "Tricyclazole 75 WP @ 0.6g/L or Carbendazim 50 WP @ 1g/L, PHI: 10 days",
        "biological": "Pseudomonas fluorescens seed + foliar treatment",
        "ipm_cultural": "Avoid excessive nitrogen; plant resistant varieties; proper spacing",
        "marathi": "भाताचा खळ रोग",
        "hindi": "धान का ब्लास्ट रोग",
    },
    "Rice___healthy": {
        "display_name": "Healthy Rice",
        "crop": "Rice",
        "severity": "LOW",
        "immediate_action": "No action required. Monitor field regularly.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive Pseudomonas fluorescens seed treatment",
        "ipm_cultural": "Maintain water level; balanced NPK",
        "marathi": "निरोगी भात",
        "hindi": "स्वस्थ धान",
    },
    # ---------- Tomato ----------
    "Tomato___Bacterial_spot": {
        "display_name": "Tomato Bacterial Spot",
        "crop": "Tomato",
        "severity": "HIGH",
        "immediate_action": "Apply Copper Oxychloride 50 WP @ 2.5g/L. Remove infected plant parts.",
        "likely_causes": ["Xanthomonas vesicatoria"],
        "chemical": "Copper Oxychloride 50 WP @ 2.5g/L, PHI: 7 days",
        "biological": "Bacillus subtilis foliar spray; Pseudomonas fluorescens 2g/L",
        "ipm_cultural": "Certified seed; crop rotation; avoid overhead irrigation",
        "marathi": "टोमॅटोवरील जीवाणूजन्य ठिपका",
        "hindi": "टमाटर का जीवाणु धब्बा",
    },
    "Tomato___Early_blight": {
        "display_name": "Tomato Early Blight",
        "crop": "Tomato",
        "severity": "MODERATE",
        "immediate_action": "Apply Mancozeb 75 WP @ 2g/L. Remove lower leaves.",
        "likely_causes": ["Alternaria solani"],
        "chemical": "Mancozeb 75 WP @ 2g/L or Chlorothalonil 75 WP @ 2g/L, PHI: 5 days",
        "biological": "Trichoderma harzianum soil application",
        "ipm_cultural": "Proper staking; mulching; crop rotation",
        "marathi": "टोमॅटोवरील अगोदरचा करपा",
        "hindi": "टमाटर का अगेती झुलसा",
    },
    "Tomato___Late_blight": {
        "display_name": "Tomato Late Blight",
        "crop": "Tomato",
        "severity": "CRITICAL",
        "immediate_action": "Apply Metalaxyl + Mancozeb @ 2.5g/L immediately. Destroy infected plants.",
        "likely_causes": ["Phytophthora infestans"],
        "chemical": "Metalaxyl 8% + Mancozeb 64% WP @ 2.5g/L, PHI: 7 days",
        "biological": "No effective biocontrol; preventive fungicide schedule essential",
        "ipm_cultural": "Avoid overhead irrigation; plant resistant varieties; remove infected debris",
        "marathi": "टोमॅटोवरील उशिरा येणारा करपा",
        "hindi": "टमाटर का पिछेती झुलसा",
    },
    "Tomato___Leaf_Mold": {
        "display_name": "Tomato Leaf Mold",
        "crop": "Tomato",
        "severity": "MODERATE",
        "immediate_action": "Improve ventilation. Apply Mancozeb 75 WP @ 2g/L.",
        "likely_causes": ["Passalora fulva (Fulvia fulva)"],
        "chemical": "Mancozeb 75 WP @ 2g/L or Propiconazole 25 EC @ 1ml/L, PHI: 7 days",
        "biological": "Bacillus subtilis foliar spray; reduce humidity",
        "ipm_cultural": "Prune for airflow; avoid high humidity; resistant varieties",
        "marathi": "टोमॅटोवरील पानावरील बुरशी",
        "hindi": "टमाटर का पत्ती फफूंद",
    },
    "Tomato___healthy": {
        "display_name": "Healthy Tomato",
        "crop": "Tomato",
        "severity": "LOW",
        "immediate_action": "No action required. Scout every 7 days.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive Trichoderma soil application",
        "ipm_cultural": "Regular irrigation; balanced NPK; proper spacing",
        "marathi": "निरोगी टोमॅटो",
        "hindi": "स्वस्थ टमाटर",
    },
    # ---------- Wheat ----------
    "Wheat___Brown_rust": {
        "display_name": "Wheat Brown Rust",
        "crop": "Wheat",
        "severity": "HIGH",
        "immediate_action": "Apply Propiconazole 25 EC @ 1ml/L at flag-leaf stage.",
        "likely_causes": ["Puccinia triticina"],
        "chemical": "Propiconazole 25 EC @ 1ml/L or Tebuconazole 25.9 EC @ 1ml/L, PHI: 14 days",
        "biological": "No established biocontrol; resistant varieties are primary defence",
        "ipm_cultural": "Early sowing; balanced N application; plant resistant varieties",
        "marathi": "गव्हावरील तपकिरी तांबेरा",
        "hindi": "गेहूं का भूरा रतुआ",
    },
    "Wheat___Yellow_rust": {
        "display_name": "Wheat Yellow Rust",
        "crop": "Wheat",
        "severity": "HIGH",
        "immediate_action": "Apply Propiconazole 25 EC @ 1ml/L. Monitor spread closely.",
        "likely_causes": ["Puccinia striiformis f.sp. tritici"],
        "chemical": "Propiconazole 25 EC @ 1ml/L or Tebuconazole 25.9 EC @ 1ml/L, PHI: 14 days",
        "biological": "No established biocontrol; use resistant/tolerant varieties",
        "ipm_cultural": "Avoid late sowing; balanced NPK; early detection scouting",
        "marathi": "गव्हावरील पिवळा तांबेरा",
        "hindi": "गेहूं का पीला रतुआ",
    },
    "Wheat___healthy": {
        "display_name": "Healthy Wheat",
        "crop": "Wheat",
        "severity": "LOW",
        "immediate_action": "No action required. Scout weekly.",
        "likely_causes": [],
        "chemical": "No intervention needed",
        "biological": "Preventive Trichoderma seed treatment",
        "ipm_cultural": "Optimal seeding rate; balanced fertilization",
        "marathi": "निरोगी गहू",
        "hindi": "स्वस्थ गेहूं",
    },
}


# ---------------------------------------------------------------------------
# Guard: check that both datasets exist
# ---------------------------------------------------------------------------

def check_datasets() -> None:
    """Verify both required dataset directories are present.

    Checks for ``data/PlantVillage/train/`` and
    ``data/Cotton Disease/train/``.  If either is absent the function prints
    a human-readable error message and calls ``sys.exit(1)``; it never raises
    an unhandled exception.

    Requirements: 1.2, 1.3, 7.6
    """
    pv_train = PLANTVILLAGE_ROOT / "train"
    co_train = COTTON_ROOT / "train"

    missing: list[str] = []
    if not pv_train.exists():
        missing.append(str(pv_train))
    if not co_train.exists():
        missing.append(str(co_train))

    if missing:
        for m in missing:
            print(f"ERROR: Training data not found at {m}. Run: python download_dataset.py")
        sys.exit(1)


# ---------------------------------------------------------------------------
# Dataset preparation — merge into unified class tree with stratified split
# ---------------------------------------------------------------------------

def _collect_image_paths(
    source_dir: Path,
    folder_to_label: dict[str, str],
) -> tuple[list[str], list[str]]:
    """Walk *source_dir* and collect (image_path, canonical_label) pairs.

    Only folders whose name appears in *folder_to_label* are included.

    Args:
        source_dir: Root directory containing per-class subdirectories.
        folder_to_label: Maps folder name → canonical class label string.

    Returns:
        Tuple of (image_paths, labels) parallel lists.
    """
    paths: list[str] = []
    labels: list[str] = []
    extensions = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"}

    for cls_dir in sorted(source_dir.iterdir()):
        if not cls_dir.is_dir():
            continue
        label = folder_to_label.get(cls_dir.name)
        if label is None:
            # Try case-insensitive fallback
            for k, v in folder_to_label.items():
                if k.lower() == cls_dir.name.lower():
                    label = v
                    break
        if label is None:
            log.debug("Skipping unrecognised folder: %s", cls_dir)
            continue
        for img in cls_dir.iterdir():
            if img.suffix in extensions:
                paths.append(str(img))
                labels.append(label)

    return paths, labels


def prepare_data(data_root: Path | None = None) -> tuple[Path, Path, Path]:
    """Merge PlantVillage and Cotton Disease datasets and produce a 70/15/15 stratified split.

    This function:

    1. Collects image paths from ``data/PlantVillage/train/`` (and
       ``data/PlantVillage/valid/`` if present) for the 21 specified
       PlantVillage classes.
    2. Collects image paths from ``data/Cotton Disease/train/`` for the 4
       Cotton classes.
    3. Merges both collections into a flat list and applies a stratified
       70/15/15 train / val / test split using
       :func:`sklearn.model_selection.train_test_split` with
       ``random_state=42`` for reproducibility.
    4. Creates a temporary directory tree under ``data/_split/`` with
       ``train/``, ``val/``, and ``test/`` sub-trees where each image is
       represented as a *symlink* (falling back to a copy on platforms
       without symlink support).

    Args:
        data_root: Optional override for the workspace root.  When ``None``
            the current working directory is used.

    Returns:
        Tuple ``(train_dir, val_dir, test_dir)`` as :class:`pathlib.Path`
        objects pointing to the created split directories.

    Raises:
        SystemExit: If either required dataset directory is absent (delegates
            to :func:`check_datasets`).
    """
    if data_root is not None:
        pv_root = data_root / "PlantVillage"
        co_root = data_root / "Cotton Disease"
    else:
        pv_root = PLANTVILLAGE_ROOT
        co_root = COTTON_ROOT

    # ---- collect PlantVillage images ----
    pv_train_dir = pv_root / "train"
    pv_valid_dir = pv_root / "valid"  # some releases ship "valid" not "val"

    all_paths: list[str] = []
    all_labels: list[str] = []

    for split_dir in [pv_train_dir, pv_valid_dir]:
        if split_dir.exists():
            p, l = _collect_image_paths(split_dir, PLANTVILLAGE_CLASSES)
            all_paths.extend(p)
            all_labels.extend(l)
            log.info("PlantVillage %s: %d images collected", split_dir.name, len(p))

    # ---- collect Cotton Disease images ----
    co_train_dir = co_root / "train"
    p, l = _collect_image_paths(co_train_dir, COTTON_CLASSES)
    all_paths.extend(p)
    all_labels.extend(l)
    log.info("Cotton Disease train: %d images collected", len(p))

    # Also pick up a separate test split from Cotton Disease if present
    co_test_dir = co_root / "test"
    if co_test_dir.exists():
        p, l = _collect_image_paths(co_test_dir, COTTON_CLASSES)
        all_paths.extend(p)
        all_labels.extend(l)
        log.info("Cotton Disease test: %d images collected", len(p))

    total = len(all_paths)
    if total == 0:
        print("ERROR: No images found in dataset directories. Run: python download_dataset.py")
        sys.exit(1)

    log.info("Total images before split: %d across %d classes", total, len(set(all_labels)))

    # ---- stratified split: 70 % train, 15 % val, 15 % test ----
    # Step 1: split into train (70%) and temp (30%)
    x_train, x_temp, y_train, y_temp = train_test_split(
        all_paths,
        all_labels,
        test_size=(1.0 - TRAIN_RATIO),
        stratify=all_labels,
        random_state=RANDOM_STATE,
    )
    # Step 2: split temp into val (50%) and test (50%) → each 15% of total
    x_val, x_test, y_val, y_test = train_test_split(
        x_temp,
        y_temp,
        test_size=0.5,
        stratify=y_temp,
        random_state=RANDOM_STATE,
    )

    log.info(
        "Split: train=%d  val=%d  test=%d",
        len(x_train), len(x_val), len(x_test),
    )

    # ---- build split directory tree ----
    split_root = SPLIT_DIR
    if split_root.exists():
        shutil.rmtree(split_root)
    split_root.mkdir(parents=True)

    train_dir = split_root / "train"
    val_dir = split_root / "val"
    test_dir = split_root / "test"

    _populate_split_dir(train_dir, x_train, y_train)
    _populate_split_dir(val_dir, x_val, y_val)
    _populate_split_dir(test_dir, x_test, y_test)

    log.info("Split tree created at: %s", split_root)
    return train_dir, val_dir, test_dir


def _populate_split_dir(
    split_dir: Path,
    image_paths: list[str],
    labels: list[str],
) -> None:
    """Create class subdirectories under *split_dir* and link/copy images.

    Each canonical label becomes a sub-directory.  Images are symlinked when
    the operating system supports it; otherwise a hard copy is made.

    Args:
        split_dir: Destination split directory (``train/``, ``val/``, or
            ``test/``).
        image_paths: Absolute or relative path strings for each image.
        labels: Canonical label string for the corresponding image.
    """
    split_dir.mkdir(parents=True, exist_ok=True)
    _use_symlinks: bool | None = None  # lazy-detect per OS

    for img_path_str, label in zip(image_paths, labels):
        src = Path(img_path_str).resolve()
        dest_dir = split_dir / label
        dest_dir.mkdir(parents=True, exist_ok=True)
        dest = dest_dir / src.name

        if dest.exists():
            # Avoid collisions from duplicate filenames across classes/splits
            dest = dest_dir / f"{src.stem}_{abs(hash(str(src)))}{src.suffix}"

        if _use_symlinks is None:
            # Detect once
            try:
                test_link = dest_dir / "__test_link__"
                test_link.symlink_to(src)
                test_link.unlink()
                _use_symlinks = True
            except (OSError, NotImplementedError):
                _use_symlinks = False

        if _use_symlinks:
            try:
                dest.symlink_to(src)
            except OSError:
                shutil.copy2(str(src), str(dest))
        else:
            shutil.copy2(str(src), str(dest))


# ---------------------------------------------------------------------------
# Build tf.data.Dataset pipelines with augmentation
# ---------------------------------------------------------------------------

def build_datasets(
    train_dir: Path,
    val_dir: Path,
    test_dir: Path,
) -> tuple[
    "tf.data.Dataset",
    "tf.data.Dataset",
    "tf.data.Dataset",
    list[str],
]:
    """Create train, validation, and test ``tf.data.Dataset`` pipelines.

    Uses ``tf.keras.utils.image_dataset_from_directory`` with image size
    224×224 and batch size 32.  Training data is augmented; validation and
    test data are only normalised.

    Augmentation pipeline applied to training data only:
      - RandomFlip (horizontal and vertical)
      - RandomRotation(0.3)
      - RandomZoom(0.25)
      - RandomBrightness(0.3)
      - RandomContrast(0.3)
      - RandomTranslation(0.15, 0.15)

    Args:
        train_dir: Path to the ``train/`` split directory.
        val_dir: Path to the ``val/`` split directory.
        test_dir: Path to the ``test/`` split directory.

    Returns:
        Tuple ``(train_ds, val_ds, test_ds, class_names)`` where each
        dataset yields ``(images, one_hot_labels)`` batches and
        ``class_names`` is the alphabetically sorted list of class labels.
    """
    load_kwargs = dict(
        image_size=(IMG_SIZE, IMG_SIZE),
        batch_size=BATCH_SIZE,
        label_mode="categorical",
        seed=RANDOM_STATE,
    )

    train_ds: tf.data.Dataset = tf.keras.utils.image_dataset_from_directory(
        str(train_dir), shuffle=True, **load_kwargs
    )
    val_ds: tf.data.Dataset = tf.keras.utils.image_dataset_from_directory(
        str(val_dir), shuffle=False, **load_kwargs
    )
    test_ds: tf.data.Dataset = tf.keras.utils.image_dataset_from_directory(
        str(test_dir), shuffle=False, **load_kwargs
    )

    class_names: list[str] = train_ds.class_names
    log.info(
        "Classes (%d): %s", len(class_names), ", ".join(class_names[:5]) + " ..."
    )
    log.info(
        "Batches — train: %d  val: %d  test: %d",
        len(train_ds), len(val_ds), len(test_ds),
    )

    # ---- augmentation pipeline (training only) ----
    augmentation = tf.keras.Sequential(
        [
            tf.keras.layers.RandomFlip("horizontal_and_vertical"),
            tf.keras.layers.RandomRotation(0.3),
            tf.keras.layers.RandomZoom(0.25),
            tf.keras.layers.RandomBrightness(0.3),
            tf.keras.layers.RandomContrast(0.3),
            tf.keras.layers.RandomTranslation(0.15, 0.15),
        ],
        name="data_augmentation",
    )
    normalise = tf.keras.layers.Rescaling(1.0 / 255)

    AUTOTUNE = tf.data.AUTOTUNE

    def preprocess_train(images, labels):
        return normalise(augmentation(images, training=True)), labels

    def preprocess_eval(images, labels):
        return normalise(images), labels

    train_ds = (
        train_ds.map(preprocess_train, num_parallel_calls=AUTOTUNE)
        .prefetch(AUTOTUNE)
    )
    val_ds = (
        val_ds.map(preprocess_eval, num_parallel_calls=AUTOTUNE)
        .prefetch(AUTOTUNE)
    )
    test_ds = (
        test_ds.map(preprocess_eval, num_parallel_calls=AUTOTUNE)
        .prefetch(AUTOTUNE)
    )

    return train_ds, val_ds, test_ds, class_names


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> None:
    """Entry point for the training pipeline.

    Task 1.1 scope: dataset guard + data preparation only.
    Model training and export are handled in task 1.2.
    """
    log.info("AgriShield Training Pipeline v3.0 — data preparation")
    log.info("TF %s | GPU: %s", tf.__version__, bool(tf.config.list_physical_devices("GPU")))

    # Guard: abort early with a clear message if datasets are missing
    check_datasets()

    # Merge datasets and produce stratified 70/15/15 split on disk
    train_dir, val_dir, test_dir = prepare_data()

    # Verify the split directories can be loaded into tf.data pipelines
    train_ds, val_ds, test_ds, class_names = build_datasets(train_dir, val_dir, test_dir)

    log.info("Data preparation complete.")
    log.info("  train_dir : %s", train_dir)
    log.info("  val_dir   : %s", val_dir)
    log.info("  test_dir  : %s", test_dir)
    log.info("  Classes   : %d", len(class_names))
    log.info(
        "  Batches   : train=%d  val=%d  test=%d",
        len(train_ds), len(val_ds), len(test_ds),
    )
    log.info("Run task 1.2 to train the MobileNetV2 model and export TFLite assets.")


if __name__ == "__main__":
    main()
