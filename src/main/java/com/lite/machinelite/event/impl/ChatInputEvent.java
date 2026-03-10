package com.lite.machinelite.event.impl;

import com.lite.machinelite.event.Event;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;

import java.util.List;

public class ChatInputEvent extends Event {
    private Text textComponent;
    private List<ChatHudLine.Visible> chatLines;
    private boolean modified = false;

    public ChatInputEvent(Text textComponent, List<ChatHudLine.Visible> chatLines) {
        this.textComponent = textComponent;
        this.chatLines = chatLines;
    }

    public Text getTextComponent() {
        return textComponent;
    }

    public void setTextComponent(Text textComponent) {
        this.textComponent = textComponent;
        this.modified = true;
    }

    public boolean isModified() {
        return modified;
    }

    public List<ChatHudLine.Visible> getChatLines() {
        return chatLines;
    }
}
