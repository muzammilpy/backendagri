#!/usr/bin/env python3
"""
AgriShield Dataset Downloader
==============================
Downloads the Kaggle Cotton Disease dataset without needing kaggle.json.
Uses opendatasets — asks for your Kaggle username + API key once.

Steps:
  1. Go to https://www.kaggle.com/settings/account  (Account -> API -> Create New Token)
  2. This downloads kaggle.json — open it, note username and key
  3. Run: python download_dataset.py
  4. Enter your Kaggle username and API key when prompted

OR: Manual download
  1. Go to: https://www.kaggle.com/datasets/janmejaybhoi/cotton-disease-dataset
  2. Click Download -> Download (zipped)
  3. Extract so the folder looks like:
       data/Cotton Disease/train/diseased cotton leaf/   <- images here
       data/Cotton Disease/train/fresh cotton leaf/
       data/Cotton Disease/train/diseased cotton plant/
       data/Cotton Disease/train/fresh cotton plant/
       data/Cotton Disease/test/...
  4. Run: python train_model.py
"""

import os
import sys
import zipfile
from pathlib import Path

def try_opendatasets():
    try:
        import opendatasets as od
    except ImportError:
        print("Installing opendatasets...")
        os.system(f"{sys.executable} -m pip install opendatasets -q")
        import opendatasets as od

    print("\nDownloading Cotton Disease dataset via opendatasets...")
    print("You will be prompted for your Kaggle username and API key.")
    print("Get your key from: https://www.kaggle.com/settings (API section -> Create New Token)\n")
    od.download(
        "https://www.kaggle.com/datasets/janmejaybhoi/cotton-disease-dataset",
        data_dir="data"
    )
    # opendatasets downloads to data/cotton-disease-dataset/
    # rename to match expected structure
    src = Path("data/cotton-disease-dataset")
    dst = Path("data/Cotton Disease")
    if src.exists() and not dst.exists():
        src.rename(dst)
        print(f"Renamed {src} -> {dst}")


def try_kaggle_api():
    """Try using kaggle CLI if credentials are available."""
    import subprocess
    result = subprocess.run(
        ["kaggle", "datasets", "download", "-d",
         "janmejaybhoi/cotton-disease-dataset", "-p", "data", "--unzip"],
        capture_output=True, text=True
    )
    if result.returncode == 0:
        print("Downloaded via kaggle CLI")
        return True
    return False


def verify_structure():
    """Check the dataset structure is correct."""
    base = Path("data/Cotton Disease")
    expected_splits = ["train", "test"]
    expected_classes = [
        "diseased cotton leaf",
        "diseased cotton plant",
        "fresh cotton leaf",
        "fresh cotton plant",
    ]

    if not base.exists():
        print(f"\n❌ Folder not found: {base}")
        print("Please download manually from:")
        print("https://www.kaggle.com/datasets/janmejaybhoi/cotton-disease-dataset")
        return False

    total_images = 0
    for split in expected_splits:
        split_dir = base / split
        if not split_dir.exists():
            print(f"  ⚠️  Missing split: {split}")
            continue
        for cls in expected_classes:
            cls_dir = split_dir / cls
            if cls_dir.exists():
                n = len(list(cls_dir.glob("*.jpg")) +
                        list(cls_dir.glob("*.jpeg")) +
                        list(cls_dir.glob("*.png")))
                total_images += n
                print(f"  ✓ {split}/{cls}: {n} images")
            else:
                print(f"  ⚠️  Missing: {split}/{cls}")

    print(f"\nTotal images found: {total_images}")
    if total_images > 0:
        print("✅ Dataset ready! Run: python train_model.py")
        return True
    return False


if __name__ == "__main__":
    print("=" * 55)
    print("  AgriShield Cotton Dataset Downloader")
    print("=" * 55)

    # First verify if already downloaded
    if verify_structure():
        sys.exit(0)

    print("\nDataset not found locally. Attempting download...")

    # Try kaggle CLI
    if try_kaggle_api():
        verify_structure()
        sys.exit(0)

    # Try opendatasets (prompts for credentials)
    try_opendatasets()
    verify_structure()
