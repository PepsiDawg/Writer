package io.github.pepsidawg.writer.util;

import io.github.pepsidawg.writer.Writer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WritingManager {
    static WritingManager self;
    Map<String, String> fonts;
    Map<UUID, TextContext> writings;

    private WritingManager() {
        writings = new HashMap<>();
    }

    public static WritingManager getInstance() {
        if(self == null)
            self = new WritingManager();
        return self;
    }

    public TextContext getWriting(Player player) {
        if(!hasWriting(player))
            addWriting(player);

        return writings.get(player.getUniqueId());
    }

    public boolean hasWriting(Player player) {
        return writings.containsKey(player.getUniqueId());
    }

    public void addWriting(Player player) {
        writings.put(player.getUniqueId(), new TextContext());
    }

    public String getFontPath(String fontName) {
        return fonts.get(fontName);
    }

    public Set<String> getFontNames() {
        return fonts.keySet();
    }

    public boolean isFont(String fontName) {
        return fonts.containsKey(fontName);
    }

    public int loadFontPaths() throws Exception {
        String basePath = Writer.getInstance().getDataFolder().getAbsolutePath() + "/Fonts/";
        fonts = new HashMap<>();
        File fonts = new File(basePath);

        for(File font : fonts.listFiles())
            this.fonts.put(font.getName(), font.getAbsolutePath());

        this.fonts.put("sans_serif", "sans_serif");
        return fonts.listFiles().length;
    }
}
