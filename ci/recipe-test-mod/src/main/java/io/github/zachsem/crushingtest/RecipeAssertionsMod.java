package io.github.zachsem.crushingtest;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(RecipeAssertionsMod.MODID)
public class RecipeAssertionsMod {
    public static final String MODID = "create_crushing_recipe_ci_test";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String[] STONES = {
            "asurine",
            "crimsite",
            "diorite",
            "ochrum",
            "tuff",
            "veridium"
    };

    public RecipeAssertionsMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
    }

    private void onServerStarted(ServerStartedEvent event) {
        boolean expectPatch = Boolean.parseBoolean(
                System.getProperty("createCrushingRecipeFix.expected", "true")
        );

        RecipeManager recipes = event.getServer().getRecipeManager();
        List<String> failures = new ArrayList<>();

        for (String stone : STONES) {
            ResourceLocation direct = new ResourceLocation("create", "crushing/" + stone);
            ResourceLocation recycling = new ResourceLocation("create", "crushing/" + stone + "_recycling");

            boolean directLoaded = recipes.byKey(direct).isPresent();
            boolean recyclingLoaded = recipes.byKey(recycling).isPresent();

            if (expectPatch && directLoaded) {
                failures.add("redundant recipe still loaded: " + direct);
            }

            if (!expectPatch && !directLoaded) {
                failures.add("control recipe unexpectedly missing: " + direct);
            }

            if (!recyclingLoaded) {
                failures.add("intended recycling recipe missing: " + recycling);
            }
        }

        if (!failures.isEmpty()) {
            failures.forEach(failure -> LOGGER.error("[Create Crushing Recipe CI] {}", failure));
            throw new IllegalStateException(
                    "Create Crushing Recipe CI assertions failed (" + failures.size() + " failure(s))"
            );
        }

        LOGGER.info(
                "[Create Crushing Recipe CI] PASS: expectedPatch={}, all six recipe pairs matched expectations",
                expectPatch
        );
    }
}
