#!/usr/bin/env python3
"""
AgriShield ml_engine.py
=======================
Standalone Python TFLite inference engine.
Use this to test your model from the command line before deploying to Android.

Usage:
    python ml_engine.py --image path/to/leaf.jpg
    python ml_engine.py --image path/to/leaf.jpg --model models/agrishield_cotton.tflite
"""

import sys, json, argparse, time
import numpy as np
from pathlib import Path

try:
    import tensorflow as tf
    from PIL import Image
except ImportError as e:
    print(f"Missing: {e}\nRun: pip install tensorflow pillow")
    sys.exit(1)

MODEL_PATH    = Path("models/agrishield_cotton.tflite")
LABELS_PATH   = Path("models/labels.txt")
METADATA_PATH = Path("models/model_metadata.json")
IMG_SIZE      = 224


class CottonDiseaseClassifier:
    """
    Wraps the TFLite interpreter.
    Input : RGB image (any size) -> resized to 224x224, normalised 0-1
    Output: list of (class_name, probability, metadata_dict) sorted by probability desc
    """

    def __init__(self, model_path=MODEL_PATH, labels_path=LABELS_PATH,
                 metadata_path=METADATA_PATH):
        if not Path(model_path).exists():
            raise FileNotFoundError(
                f"Model not found: {model_path}\n"
                "Run: python train_model.py  to generate it first."
            )

        self.interpreter = tf.lite.Interpreter(model_path=str(model_path))
        self.interpreter.allocate_tensors()

        self.input_details  = self.interpreter.get_input_details()
        self.output_details = self.interpreter.get_output_details()

        # Validate input shape
        expected = self.input_details[0]["shape"]   # [1, 224, 224, 3]
        self.img_size = int(expected[1])

        # Load labels
        self.labels = Path(labels_path).read_text().strip().splitlines()

        # Load rich IPM metadata
        self.metadata = {}
        if Path(metadata_path).exists():
            with open(metadata_path, encoding="utf-8") as f:
                raw = json.load(f)
            self.metadata = raw.get("classes", {})

        print(f"Model loaded | classes: {len(self.labels)} | img_size: {self.img_size}")

    # ------------------------------------------------------------------
    def preprocess(self, image_path: str) -> np.ndarray:
        """Load, resize, normalise image -> float32 [1, H, W, 3]"""
        img = Image.open(image_path).convert("RGB")
        img = img.resize((self.img_size, self.img_size), Image.LANCZOS)
        arr = np.array(img, dtype=np.float32) / 255.0
        return np.expand_dims(arr, axis=0)

    def preprocess_bytes(self, image_bytes: bytes) -> np.ndarray:
        """Same but from raw bytes (for use in a web server endpoint)."""
        import io
        img = Image.open(io.BytesIO(image_bytes)).convert("RGB")
        img = img.resize((self.img_size, self.img_size), Image.LANCZOS)
        arr = np.array(img, dtype=np.float32) / 255.0
        return np.expand_dims(arr, axis=0)

    # ------------------------------------------------------------------
    def predict(self, image_path: str) -> list[dict]:
        """
        Run inference on one image file.

        Returns a list of dicts sorted by confidence (highest first):
        [
          {
            "label":      "diseased cotton leaf",
            "confidence": 94.3,          <- real softmax probability * 100
            "rank":       1,
            "display_name": "Diseased Cotton Leaf",
            "severity":   "HIGH",
            "immediate_action": "...",
            "chemical":   "...",
            "biological": "...",
            "marathi":    "...",
          },
          ...
        ]
        """
        inp = self.preprocess(image_path)
        return self._run(inp)

    def predict_bytes(self, image_bytes: bytes) -> list[dict]:
        inp = self.preprocess_bytes(image_bytes)
        return self._run(inp)

    def _run(self, inp: np.ndarray) -> list[dict]:
        t0 = time.perf_counter()

        self.interpreter.set_tensor(self.input_details[0]["index"], inp)
        self.interpreter.invoke()
        probs = self.interpreter.get_tensor(
            self.output_details[0]["index"]
        )[0]                                  # shape: (num_classes,)

        elapsed_ms = (time.perf_counter() - t0) * 1000

        results = []
        for idx, prob in enumerate(probs):
            label = self.labels[idx] if idx < len(self.labels) else f"class_{idx}"
            meta  = self.metadata.get(str(idx), {})
            results.append({
                "label":           label,
                "confidence":      round(float(prob) * 100, 2),  # REAL softmax, not hardcoded
                "rank":            0,   # filled below
                "display_name":    meta.get("display_name", label),
                "severity":        meta.get("severity", "MODERATE"),
                "immediate_action":meta.get("immediate_action", "Consult local KVK."),
                "likely_causes":   meta.get("likely_causes", []),
                "chemical":        meta.get("chemical", ""),
                "biological":      meta.get("biological", ""),
                "marathi":         meta.get("marathi", label),
                "hindi":           meta.get("hindi", label),
                "inference_ms":    round(elapsed_ms, 1),
            })

        results.sort(key=lambda x: x["confidence"], reverse=True)
        for i, r in enumerate(results):
            r["rank"] = i + 1

        return results

    # ------------------------------------------------------------------
    def diagnose(self, image_path: str, farm_lat: float = None,
                 farm_lon: float = None, lang: str = "en") -> dict:
        """
        Full diagnosis combining ML prediction + weather risk context.
        lang: "en" | "mr" | "hi"
        """
        predictions = self.predict(image_path)
        top         = predictions[0]

        # Localise display name
        if lang == "mr":
            top["localised_name"] = top.get("marathi", top["display_name"])
        elif lang == "hi":
            top["localised_name"] = top.get("hindi", top["display_name"])
        else:
            top["localised_name"] = top["display_name"]

        # Confidence interpretation
        conf = top["confidence"]
        if conf >= 85:
            reliability = "HIGH — Strong visual match"
        elif conf >= 65:
            reliability = "MODERATE — Confirm with physical inspection"
        else:
            reliability = "LOW — Seek expert validation"

        return {
            "top_prediction":   top,
            "all_predictions":  predictions,
            "confidence_band":  reliability,
            "farm_lat":         farm_lat,
            "farm_lon":         farm_lon,
            "language":         lang,
            "model_source":     "AgriShield TFLite v1.0 (MobileNetV2 / Cotton)",
        }


# ── CLI test harness ───────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="AgriShield Cotton Classifier")
    parser.add_argument("--image",  required=True, help="Path to leaf image")
    parser.add_argument("--model",  default=str(MODEL_PATH))
    parser.add_argument("--labels", default=str(LABELS_PATH))
    parser.add_argument("--meta",   default=str(METADATA_PATH))
    parser.add_argument("--lang",   default="en", choices=["en","mr","hi","gu"])
    parser.add_argument("--top",    type=int, default=4,
                        help="Show top-N predictions")
    args = parser.parse_args()

    clf = CottonDiseaseClassifier(args.model, args.labels, args.meta)
    result = clf.diagnose(args.image, lang=args.lang)
    top = result["top_prediction"]

    RESET = "\033[0m"; BOLD = "\033[1m"
    GREEN = "\033[92m"; YELLOW = "\033[93m"; RED = "\033[91m"
    CYAN  = "\033[96m"
    sev_color = {"LOW": GREEN, "MODERATE": YELLOW,
                 "HIGH": RED, "CRITICAL": RED}.get(top["severity"], RESET)

    print(f"\n{'='*60}")
    print(f"  {BOLD}AgriShield Cotton Disease Classifier{RESET}")
    print(f"{'='*60}")
    print(f"  Image         : {args.image}")
    print(f"  Inference time: {top['inference_ms']} ms")
    print(f"\n  {BOLD}TOP DIAGNOSIS:{RESET}")
    print(f"  Class         : {BOLD}{top['localised_name']}{RESET}")
    print(f"  Confidence    : {BOLD}{top['confidence']}%{RESET}  ({result['confidence_band']})")
    print(f"  Severity      : {sev_color}{BOLD}{top['severity']}{RESET}")
    print(f"\n  {BOLD}Immediate Action:{RESET}")
    print(f"  {top['immediate_action']}")
    print(f"\n  {BOLD}Biological Control:{RESET}")
    print(f"  {top['biological']}")
    print(f"\n  {BOLD}Chemical Control:{RESET}")
    print(f"  {top['chemical']}")

    print(f"\n  {CYAN}All class probabilities:{RESET}")
    for r in result["all_predictions"][:args.top]:
        bar_len = int(r["confidence"] / 2)
        bar = "█" * bar_len + "░" * (50 - bar_len)
        print(f"  {r['rank']}. {r['label']:35s} {bar} {r['confidence']:5.1f}%")
    print(f"{'='*60}\n")


if __name__ == "__main__":
    main()
