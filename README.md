# Create Crushing Recipe Fix

A small, unofficial compatibility/fix mod for **Create 0.5.1i** on **Minecraft 1.18.2 Forge**.

It fixes duplicate Crushing recipes reported in Create issue #5559 without modifying the Create JAR, without Mixins, and without adding gameplay content.

CurseForge: https://www.curseforge.com/minecraft/mc-mods/create-crushing-recipe-fix

Upstream issue: https://github.com/Creators-of-Create/Create/issues/5559

## What it fixes

Create 0.5.1i contains redundant direct-item Crushing recipes for several stone types while equivalent tag-based `_recycling` recipes already cover the same base blocks.

This patch disables the redundant direct-item recipe IDs and leaves Create's broader tag-based recipes intact.

Affected recipes:

- Asurine
- Crimsite
- Ochrum
- Veridium
- Tuff
- Diorite

The intended Crushing outputs and processing behavior are unchanged. The duplicate recipe entries are simply removed.

## Requirements

- Minecraft 1.18.2
- Forge 40.2.4 or newer in the 1.18.2 branch
- Create 0.5.1i

The mod metadata intentionally targets **Create 0.5.1i exactly** because that is the version this fix was verified against.

## Installation

### Single-player

Put `create-crushing-recipe-fix-1.0.1.jar` in the instance's `mods` folder.

### Dedicated server

Put the JAR in the server's `mods` folder.

Clients joining a patched dedicated server do **not** need this patch installed locally. It is also safe for clients to have it installed.

## Technical details

This is a resource-only Forge mod using `lowcodefml`. It contains no Java classes and no Mixins.

Each redundant Create recipe path is overridden with a Forge `forge:false` load condition. Forge therefore skips the redundant direct-item recipe while Create's existing `_recycling` recipe remains available.

No Create files are edited in place.

## Tested

Runtime behavior was validated on the original 1.0.0 build with:

- Minecraft 1.18.2
- Forge 40.3.0
- Create 0.5.1i
- JEI 10.6.1.1023

Test coverage included:

- Single-player / integrated server
- Dedicated server with the patch installed on both server and client
- Dedicated server with the patch installed only on the server
- Actual Crushing Wheels processing of Asurine after the fix
- JEI showing one Crushing recipe instead of the duplicate pair

Version 1.0.1 keeps the same recipe-fix payload and changes only release/version metadata. The final 1.0.1 JAR was package-validated after rebuilding.

Detailed validation notes are available in [docs/VALIDATION.md](docs/VALIDATION.md).

## Issues and requests

Please report bugs and compatibility requests through this repository's GitHub Issues page rather than CurseForge comments. Use the provided Bug Report or Feature Request template so logs and reproduction details stay together.

## Removing the mod

The mod does not add blocks, items, entities, or save data. Removing it does not damage a world; the original duplicate Create recipes simply return.

## Building

The release JAR is just the contents of `src/main/resources` packaged as a JAR.

Run:

```text
python build.py
```

The output is written to `dist/create-crushing-recipe-fix-1.0.1.jar`.

## License

This project is licensed under the MIT License. See `LICENSE`.

## Credits

Create is developed by simibubi and the Creators-of-Create contributors.

This project is independent, unofficial, and is not affiliated with or endorsed by Creators-of-Create.
