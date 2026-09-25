#!/usr/bin/env python3
"""
Creates a minimal demo dataset using colour-patch images so training
can run immediately to prove the pipeline works end-to-end.

Once you have the real Kaggle dataset, replace data/Cotton Disease/
with the real images and re-run train_model.py for production accuracy.

Real dataset: https://www.kaggle.com/datasets/janmejaybhoi/cotton-disease-dataset
"""
import os
import random
import numpy as np
from pathlib import Path

try:
    from PIL import Image, ImageDraw, ImageFilter
except ImportError:
    os.system("pip install pillow -q")
    from PIL import Image, ImageDraw, ImageFilter

CLASSES = [
    "diseased cotton leaf",
    "diseased cotton plant",
    "fresh cotton leaf",
    "fresh cotton plant",
]

# Realistic colour palettes for each class
PALETTES = {
    "diseased cotton leaf":  [(80, 60, 20), (120, 90, 30), (60, 100, 40), (140, 110, 50)],
    "diseased cotton plant": [(50, 40, 10), (90, 70, 20), (40, 80, 30), (110, 90, 25)],
    "fresh cotton leaf":     [(34, 139, 34), (50, 180, 50), (20, 160, 60), (60, 200, 80)],
    "fresh cotton plant":    [(28, 120, 28), (45, 165, 45), (15, 150, 55), (55, 190, 75)],
}

def make_image(cls_name: str, size: int = 224) -> Image.Image:
    """Generate a synthetic leaf-like image with class-specific colours."""
    palette = PALETTES[cls_name]
    base_color = random.choice(palette)
    img = Image.new("RGB", (size, size), base_color)
    draw = ImageDraw.Draw(img)

    # Add texture patches to simulate leaf structure
    for _ in range(random.randint(8, 20)):
        x1 = random.randint(0, size - 40)
        y1 = random.randint(0, size - 40)
        x2 = x1 + random.randint(10, 60)
        y2 = y1 + random.randint(10, 60)
        variation = tuple(
            max(0, min(255, c + random.randint(-30, 30))) for c in base_color
        )
        draw.ellipse([x1, y1, x2, y2], fill=variation)

    # Diseased classes get brown spots
    if "diseased" in cls_name:
        for _ in range(random.randint(5, 15)):
            x = random.randint(0, size - 20)
            y = random.randint(0, size - 20)
            r = random.randint(5, 20)
            brown = (random.randint(80, 140), random.randint(40, 80), random.randint(10, 40))
            draw.ellipse([x-r, y-r, x+r, y+r], fill=brown)

    # Add slight blur to simulate camera
    img = img.filter(ImageFilter.GaussianBlur(radius=random.uniform(0.5, 1.5)))

    # Random brightness
    arr = np.array(img, dtype=np.float32)
    arr *= random.uniform(0.7, 1.3)
    arr = np.clip(arr, 0, 255).astype(np.uint8)
    return Image.fromarray(arr)


def create_dataset(
    n_train: int = 200,
    n_val:   int = 40,
    n_test:  int = 40,
):
    base = Path("data/Cotton Disease")
    total = 0
    for split, n in [("train", n_train), ("val", n_val), ("test", n_test)]:
        for cls in CLASSES:
            d = base / split / cls
            d.mkdir(parents=True, exist_ok=True)
            existing = len(list(d.glob("*.jpg")))
            to_create = max(0, n - existing)
            for i in range(to_create):
                img = make_image(cls)
                img.save(str(d / f"{cls.replace(' ','_')}_{existing+i:04d}.jpg"), quality=90)
            created = len(list(d.glob("*.jpg")))
            print(f"  {split}/{cls}: {created} images")
            total += created

    print(f"\n✅ Demo dataset ready: {total} images")
    print("   data/Cotton Disease/train|val|test/<class>/")
    print("\n⚠️  These are synthetic colour-patch images.")
    print("   Replace with the real Kaggle dataset for production accuracy.")
    print("   Real dataset: https://www.kaggle.com/datasets/janmejaybhoi/cotton-disease-dataset")
    print("\nRun training: python train_model.py")


if __name__ == "__main__":
    print("Creating synthetic demo dataset...")
    create_dataset(n_train=200, n_val=40, n_test=40)
