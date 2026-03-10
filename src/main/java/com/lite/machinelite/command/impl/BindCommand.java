package com.lite.machinelite.command.impl;

import com.lite.machinelite.MachineLite;
import com.lite.machinelite.module.Module;
import com.lite.machinelite.command.Command;
import net.minecraft.client.util.InputUtil;

import java.io.FileNotFoundException;

public class BindCommand extends Command {
    public BindCommand(String[] names, String description) {
        super(names, description);
    }

    public void fire(String[] args) {
        if (args != null && args.length > 0) {
            Module module = MachineLite.getModuleManager().getModuleByString(args[0]);

                if (module != null) {
                    if (args.length == 1) {
                        MachineLite.WriteChat(String.format("\2477The current key for \247b%s \2477is \2479%s.", module.getName(), InputUtil.fromKeyCode(module.getKeyCode(), -1).getLocalizedText().getString()));
                    } else {
                        try {
                            int key = InputUtil.fromTranslationKey("key.keyboard." + args[1].toLowerCase()).getCode();
                            module.setKeyCode(key);
                            MachineLite.WriteChat("\2477" + module.getName() + " bind to \247f" + args[1].toUpperCase() + "\2477.");
                        } catch (Exception e) {
                            MachineLite.WriteChat("\247cInvalid key!");
                        }
                    }
                } else {
                    MachineLite.WriteChat("\247cModule was not found");
                }
        } else {
            MachineLite.WriteChat("\2477.bind <Module> <Key>");
        }
    }
}
