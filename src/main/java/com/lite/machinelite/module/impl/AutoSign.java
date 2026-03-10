package com.lite.machinelite.module.impl;

import com.lite.machinelite.MachineLite;
import com.lite.machinelite.module.Module;
import net.minecraft.text.Text;

public class AutoSign extends Module {
    private Text[] signText;

    public AutoSign(String name, int keyCode) {
        super(name, keyCode);
    }

    @Override
    public void onDisabled() {
        this.signText = null;
    }

    public Text[] getSignText() {
        return this.signText;
    }

    public void setSignText(Text[] signText) {
        this.signText = signText;
    }

    public void setSignTexts(Text[] signText) {
        if (this.isEnabled() && this.signText == null) {
            this.signText = signText;

            if (MachineLite.getModuleManager().isEnabled(Debug.class)) {
                MachineLite.WriteChat("\2477SignTextData:");
                MachineLite.WriteChat(this.signText[0].getString());
                MachineLite.WriteChat(this.signText[1].getString());
                MachineLite.WriteChat(this.signText[2].getString());
                MachineLite.WriteChat(this.signText[3].getString());
            }
        }
    }
}

