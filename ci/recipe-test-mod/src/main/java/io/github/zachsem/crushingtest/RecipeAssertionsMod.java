package io.github.zachsem.crushingtest;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod(RecipeAssertionsMod.MODID)
public class RecipeAssertionsMod {
    public static final String MODID = "create_crushing_recipe_ci_test";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Path EXPECTED_FILE = Path.of("ci-recipe-test-expected.txt");
    private static final Path RESULT_FILE = Path.of("ci-recipe-test-result.txt");

    private static final String[] STONES = {
            "asurine",
            "crimsite",
            "diorite",
            "ochrum",
            "tuff",
            "veridium"
    };

    public RecipeAssertionsMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void onServerStarting(ServerStartingEvent event) {
        boolean expectPatch = readExpectedPatch();
        RecipeManager recipes = event.getServer().getRecipeManager();
        List<String> failures = new ArrayList<>();
        List<String> observations = new ArrayList<>();

        for (String stone : STONES) {
            ResourceLocation direct = new ResourceLocation("create", "crushing/" + stone);
            ResourceLocation recycling = new ResourceLocation("create", "crushing/" + stone + "_recycling");

            boolean directLoaded = recipes.byKey(direct).isPresent();
            boolean recyclingLoaded = recipes.byKey(recycling).isPresent();

            observations.add(
                    stone + ": direct=" + directLoaded + ", recycling=" + recyclingLoaded
            );

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

            String result = "FAIL expectedPatch=" + expectPatch + System.lineSeparator()
                    + String.join(System.lineSeparator(), observations) + System.lineSeparator()
                    + String.join(System.lineSeparator(), failures) + System.lineSeparator();
            writeResult(result);

            throw new IllegalStateException(
                    "Create Crushing Recipe CI assertions failed (" + failures.size() + " failure(s))"
            );
        }

        String result = "PASS expectedPatch=" + expectPatch + System.lineSeparator()
                + String.join(System.lineSeparator(), observations) + System.lineSeparator();
        writeResult(result);

        LOGGER.info(
                "[Create Crushing Recipe CI] PASS: expectedPatch={}, all six recipe pairs matched expectations",
                expectPatch
        );
    }

    private static boolean readExpectedPatch() {
        try {
            String value = Files.readString(EXPECTED_FILE).trim();
            if (!value.equals("true") && !value.equals("false")) {
                throw new IllegalStateException("Invalid expected-patch value: " + value);
            }
            return Boolean.parseBoolean(value);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read Create Crushing Recipe CI expectation file: "
                            + EXPECTED_FILE.toAbsolutePath(),
                    e
            );
        }
    }

    private static void writeResult(String result) {
        try {
            Files.writeString(RESULT_FILE, result);
            LOGGER.info("[Create Crushing Recipe CI] Wrote result to {}", RESULT_FILE.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Could not write Create Crushing Recipe CI result file", e);
        }
    }
}
