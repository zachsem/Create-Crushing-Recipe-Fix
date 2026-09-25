# Validation

Runtime validation was performed for Create Crushing Recipe Fix 1.0.0. Version 1.0.1 uses the same recipe-fix payload with release/version metadata updated.

## Test environment

- Minecraft 1.18.2
- Forge 40.3.0
- Create 0.5.1i
- JEI 10.6.1.1023
- Java 17

## Original bug reproduction

Without the patch, Create exposes duplicate Crushing recipes for affected stone inputs. Asurine was used as the primary in-game control case and appeared as two Crushing recipes in JEI.

## Patched behavior

With the patch installed:

- Asurine showed one Crushing recipe instead of the duplicate pair.
- Crushing Wheels successfully processed Asurine and produced the intended Create output.
- The patch loaded successfully in single-player.
- A dedicated server started successfully with the patch installed.
- A client with the patch installed connected successfully.
- A client without the patch installed also connected successfully to the patched dedicated server and received the server recipe set.

## Installation behavior established by testing

- Single-player: install the patch locally because the integrated server loads the recipes.
- Dedicated server: install the patch on the server.
- Dedicated-server clients: the patch is not required locally, though having it installed is also supported.

## Scope

The patch disables only the redundant direct-item recipe IDs and preserves Create's tag-based recycling recipes.

No blocks, items, entities, or persistent world data are added by this project.


## 1.0.1 release artifact

The final 1.0.1 JAR was package-validated after rebuilding:

- Internal mod version is 1.0.1.
- License metadata is MIT.
- Pack metadata identifies version 1.0.1.
- All six intended recipe overrides are present.
- Each override retains the same `forge:false` condition used by the runtime-tested build.

No gameplay or recipe behavior was changed between the runtime-tested 1.0.0 build and 1.0.1.
