package com.simplequalityoflife;

import com.simplequalityoflife.client.AutoWalkHandler;
import com.simplequalityoflife.client.ClientNetworking;
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
        // Empfängt die Server-Config und macht sie zur wirksamen Config auf entfernten Servern.
        ClientNetworking.register();
        Simplequalityoflife.setConfigOverride(ClientNetworking::getSyncedConfig);

        AutoWalkHandler.register();
        // HoeHarvestHandler wird bereits im gemeinsamen Initializer (Simplequalityoflife) registriert.
        // Eine zweite Registrierung hier würde den UseBlockCallback im Singleplayer doppelt anmelden.

        crawlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.simplequalityoflife.crawl", // Translation Key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                QOL_CATEGORY
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