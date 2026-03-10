package com.lite.machinelite;

import com.lite.machinelite.command.Command;
import com.lite.machinelite.command.CommandManager;
import com.lite.machinelite.config.Profile;
import com.lite.machinelite.event.EventManager;
import com.lite.machinelite.module.ModuleManager;
import com.lite.machinelite.utilities.IMC;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Arrays;

public class MachineLite implements ClientModInitializer, IMC {
    public final static String CLIENT_ID = "lite";
    public final static String CLIENT_NAME = "MachineLite";
    public final static String CLIENT_VERSION = "v1.6";
    private boolean helpNotifier;

    private static EventManager eventManager;
    private static ModuleManager moduleManager;
    private static CommandManager commandManager;
    private static Profile profile;

    @Override
    public void onInitializeClient() {
        System.out.println("Initializing MachineLite Fabric port!");

        try {
            profile = new Profile();
            (eventManager = new EventManager()).initialize();
            (moduleManager = new ModuleManager()).initialize();
            (commandManager = new CommandManager()).initialize();
            profile.loadFile();
        } catch (Exception e) {
            System.err.println("Failed to initialize MachineLite modules/events!");
            e.printStackTrace();
        }

        helpNotifier = true;

        // Register event handlers that replace Forge's EntityJoinWorldEvent
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (helpNotifier && client.player != null) {
                Command command = commandManager.getCommand("help");
                if (command != null) {
                    command.fire(null);
                }
                helpNotifier = false;
            }
        });

        // Register tick event for keybinds
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Note: In 1.21.1 we usually use GLFW for keybinds.
            // Temporary note: We need to adapt the keybinding system later.
        });

        // Command handling
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.startsWith(".")) {
                String[] args = message.substring(1).trim().split(" ");
                if (args.length > 0) {
                    Command command = commandManager.getCommand(args[0]);
                    if (command != null) {
                        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
                        try {
                            command.fire(commandArgs);
                        } catch (Exception e) {
                            MachineLite.WriteChat("Error executing command.");
                            e.printStackTrace();
                        }
                    } else {
                        MachineLite.WriteChat("\u00A7cUnknown command. Type .help for a list of commands.");
                    }
                }
                return false; // prevent sending to server
            }
            return true; // allow logic to continue
        });
    }

    public static void WriteChat(Object message) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("\u00A77[\u00A75Machine \u00A7fLite\u00A77] \u00A7f" + message.toString()), false);
        }
    }

    public static Profile getProfile() {
        return profile;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }
}
