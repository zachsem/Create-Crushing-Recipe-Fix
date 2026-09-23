from pathlib import Path
import zipfile

ROOT = Path(__file__).resolve().parent
SOURCE = ROOT / "src" / "main" / "resources"
DIST = ROOT / "dist"
OUT = DIST / "create-crushing-recipe-fix-1.0.0.jar"

DIST.mkdir(exist_ok=True)

with zipfile.ZipFile(OUT, "w", compression=zipfile.ZIP_DEFLATED) as jar:
    for path in sorted(SOURCE.rglob("*")):
        if path.is_file():
            jar.write(path, path.relative_to(SOURCE).as_posix())

print(f"Built: {OUT}")
