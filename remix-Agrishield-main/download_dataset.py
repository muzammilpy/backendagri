#!/usr/bin/env python3
"""
AgriShield Dataset Downloader
==============================
Prints Kaggle CLI instructions for downloading both datasets required to
train the AgriShield 25-class crop-disease model.

Datasets:
  1. vipoooool/new-plant-diseases-dataset  → data/PlantVillage/
  2. janmejaybhoi/cotton-disease-dataset   → data/Cotton Disease/

Prerequisites:
  - Kaggle account: https://www.kaggle.com
  - API token (kaggle.json) placed at ~/.kaggle/kaggle.json
    OR export KAGGLE_USERNAME and KAGGLE_KEY environment variables.
  - kaggle CLI: pip install kaggle

Manual download alternative:
  1. https://www.kaggle.com/datasets/vipoooool/new-plant-diseases-dataset
     → extract so the layout is:
         data/PlantVillage/train/<ClassName>/  ← JPEG images
         data/PlantVillage/valid/<ClassName>/
  2. https://www.kaggle.com/datasets/janmejaybhoi/cotton-disease-dataset
     → extract so the layout is:
         data/Cotton Disease/train/<ClassName>/  ← JPEG images
         data/Cotton Disease/test/<ClassName>/
"""

import os
import sys
from pathlib import Path


# ---------------------------------------------------------------------------
# Expected dataset paths
# ---------------------------------------------------------------------------
PLANTVILLAGE_TRAIN = Path("data/PlantVillage/train")
COTTON_TRAIN = Path("data/Cotton Disease/train")


def _check_dir(p: Path) -> bool:
    """Return True when directory exists and contains at least one image."""
    if not p.exists():
        return False
    images = list(p.rglob("*.jpg")) + list(p.rglob("*.jpeg")) + list(p.rglob("*.png"))
    return len(images) > 0


def print_download_instructions() -> None:
    """Print step-by-step Kaggle CLI instructions for both datasets."""
    print()
    print("=" * 65)
    print("  AgriShield — Dataset Download Instructions")
    print("=" * 65)
    print()
    print("Step 1 — Install the Kaggle CLI (if not already installed):")
    print()
    print("    pip install kaggle")
    print()
    print("Step 2 — Place your Kaggle API token:")
    print()
    print("    Copy kaggle.json from https://www.kaggle.com/settings")
    print("    (Account → API → Create New Token) to ~/.kaggle/kaggle.json")
    print("    Then set permissions:")
    print("        chmod 600 ~/.kaggle/kaggle.json   # Linux / macOS")
    print()
    print("Step 3 — Download Dataset 1 (PlantVillage, ~2 GB):")
    print()
    print(
        "    kaggle datasets download -d vipoooool/new-plant-diseases-dataset"
        " -p data/PlantVillage --unzip"
    )
    print()
    print(
        "    After extraction the layout should be:\n"
        "        data/PlantVillage/train/<ClassName>/\n"
        "        data/PlantVillage/valid/<ClassName>/"
    )
    print()
    print("Step 4 — Download Dataset 2 (Cotton Disease, ~300 MB):")
    print()
    print(
        "    kaggle datasets download -d janmejaybhoi/cotton-disease-dataset"
        " -p data --unzip"
    )
    print()
    print(
        "    After extraction the layout should be:\n"
        "        data/Cotton Disease/train/<ClassName>/\n"
        "        data/Cotton Disease/test/<ClassName>/"
    )
    print()
    print("Step 5 — Run training:")
    print()
    print("    python train_model.py")
    print()
    print("=" * 65)
    print()


def verify_structure() -> bool:
    """Check whether both datasets are present and non-empty.

    Returns:
        True if both data/PlantVillage/train/ and
        data/Cotton Disease/train/ exist and contain images; False otherwise.
    """
    pv_ok = _check_dir(PLANTVILLAGE_TRAIN)
    co_ok = _check_dir(COTTON_TRAIN)

    print()
    print("Dataset status:")
    print(f"  [{'OK' if pv_ok else 'MISSING'}] {PLANTVILLAGE_TRAIN}")
    print(f"  [{'OK' if co_ok else 'MISSING'}] {COTTON_TRAIN}")
    print()

    return pv_ok and co_ok


if __name__ == "__main__":
    if verify_structure():
        print("Both datasets are present. Run: python train_model.py")
        sys.exit(0)

    print_download_instructions()
    sys.exit(1)
