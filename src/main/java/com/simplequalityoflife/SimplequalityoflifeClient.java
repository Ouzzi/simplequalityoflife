package com.simplequalityoflife;

import com.simplequalityoflife.client.AutoWalkHandler;
import com.simplequalityoflife.event.HoeHarvestHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class SimplequalityoflifeClient implements ClientModInitializer {

    private static KeyBinding crawlKey;
    private static final KeyBinding.Category QOL_CATEGORY = KeyBinding.Category.create(Identifier.of("simplequalityoflife", "main"));

    @Override
    public void onInitializeClient() {
        AutoWalkHandler.register();
        HoeHarvestHandler.register();

        crawlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.simplequalityoflife.crawl", // Translation Key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                QOL_CATEGORY // Hier das Kategorie-Objekt übergeben
        ));

        // Event Loop für Tastendruck
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (crawlKey.wasPressed()) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("crawl");
                }
            }
        });
    }
}