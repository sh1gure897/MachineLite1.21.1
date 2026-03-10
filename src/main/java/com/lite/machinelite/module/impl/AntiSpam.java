package com.lite.machinelite.module.impl;

import com.lite.machinelite.event.Event;
import com.lite.machinelite.event.impl.ChatInputEvent;
import com.lite.machinelite.module.Module;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class AntiSpam extends Module {
    public AntiSpam(String name, int keyCode) {
        super(name, keyCode);
    }

    private String getStringFromOrderedText(OrderedText text) {
        StringBuilder sb = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        });
        return sb.toString();
    }

    private String stripBypass(String text) {
        // Strip out existing counters (e.g. " [x2]" or " §8[x2]")
        return text.replaceAll("(?i)(\\s*\u00A78)?\\s*\\[x\\d+\\]$", "").trim();
    }

    private boolean isSpam(String oldText, String newText) {
        if (oldText.equals(newText))
            return true;

        // Extracting common prefixes to intelligently detect random suffix bypasses
        int minLen = Math.min(oldText.length(), newText.length());
        int commonPrefixLen = 0;
        while (commonPrefixLen < minLen && oldText.charAt(commonPrefixLen) == newText.charAt(commonPrefixLen)) {
            commonPrefixLen++;
        }

        if (commonPrefixLen >= 10) {
            String divergentOld = oldText.substring(commonPrefixLen);
            String divergentNew = newText.substring(commonPrefixLen);

            // Bypass suffixes are usually short and don't contain spaces (a single
            // continuous random word)
            if (divergentOld.length() <= 12 && divergentNew.length() <= 12) {
                if (!divergentOld.contains(" ") && !divergentNew.contains(" ")) {
                    // Safe assumptions for bypasses: It contains digits, or the common prefix is
                    // extremely long
                    if (commonPrefixLen >= 20 || (containsDigit(divergentOld) && containsDigit(divergentNew))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean containsDigit(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c))
                return true;
        }
        return false;
    }

    @Override
    public void onEvent(Event event) {
        if (!isEnabled())
            return;

        if (event instanceof ChatInputEvent chatEvent) {
            try {
                List<ChatHudLine.Visible> chatLines = chatEvent.getChatLines();

                if (!chatLines.isEmpty()) {
                    String newRaw = chatEvent.getTextComponent().getString();
                    String cleanNew = stripBypass(newRaw);

                    if (cleanNew.isEmpty())
                        return;

                    int spamCounter = 1;

                    // Match up to the last 50 lines to avoid excessive iteration
                    int searchLimit = Math.max(0, chatLines.size() - 50);

                    for (int i = chatLines.size() - 1; i >= searchLimit; --i) {
                        String oldRaw = getStringFromOrderedText(chatLines.get(i).content());
                        String cleanOld = stripBypass(oldRaw);

                        if (isSpam(cleanOld, cleanNew)) {
                            // Find out previous counter
                            int oldCounter = 1;
                            if (oldRaw.matches(".* \\[x\\d+\\]$") || oldRaw.matches(".* \u00A78\\[x\\d+\\]$")) {
                                int bracketIndex = oldRaw.lastIndexOf("[x");
                                if (bracketIndex != -1) {
                                    String countStr = oldRaw.substring(bracketIndex + 2, oldRaw.length() - 1);
                                    if (isInteger(countStr)) {
                                        oldCounter = Integer.parseInt(countStr);
                                    }
                                }
                            }
                            spamCounter += oldCounter;
                            chatLines.remove(i);
                            break;
                        }
                    }

                    if (spamCounter > 1) {
                        chatEvent.setCancelled(true);
                        // Modify the incoming event text to correctly append the counter and prevent
                        // double processing
                        chatEvent.setTextComponent(Text.literal(newRaw + " \u00A78[x" + spamCounter + "]"));
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
