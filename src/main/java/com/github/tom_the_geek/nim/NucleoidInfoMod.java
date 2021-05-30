package com.github.tom_the_geek.nim;

import com.github.tom_the_geek.nac.NucleoidApiClient;
import com.github.tom_the_geek.nac.response.NucleoidGame;
import com.github.tom_the_geek.nac.response.NucleoidServerStatus;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NucleoidInfoMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "nucleoid-info-mod";
    public static final String MOD_NAME = "Nucleoid Info Mod";
    public static final Identifier ICONS_TEXTURE = new Identifier(MOD_ID, "textures/icons.png");

    private static NucleoidApiClient apiClient;
    private static long lastRefresh = -1;
    private static NucleoidServerStatus latestStatus;

    @Override
    public void onInitialize() {
        apiClient = NucleoidApiClient.builder().build();
        ClientLifecycleEvents.CLIENT_STOPPING.register(__ -> apiClient.close());
    }

    public static void refresh() {
        if (System.currentTimeMillis() - lastRefresh < 1000) return; // rate-limit ourselves to avoid spamming the API.

        apiClient.getServerStatus("play").handle((status, throwable) -> {
            if (throwable != null) {
                LOGGER.error("Failed to fetch Nucleoid status", throwable);
            }

            if (status != null) {
                latestStatus = status;
            }

            return null;
        });
        lastRefresh = System.currentTimeMillis();
    }

    public static List<Text> getTooltip() {
        if (latestStatus == null) {
            return Collections.emptyList();
        }

        return latestStatus.games.stream().map(NucleoidInfoMod::gameToText).collect(Collectors.toList());
    }

    private static Text gameToText(NucleoidGame game) {
        Text name = new LiteralText(game.name).formatted(Formatting.BLUE, Formatting.BOLD);
        Text playerCount = new LiteralText(String.valueOf(game.playerCount)).formatted(Formatting.GOLD);
        return new TranslatableText("text.nucleoid.game", name, playerCount);
    }
}
