package io.github.pepsidawg.writer.util;

import com.sk89q.worldedit.blocks.BaseBlock;
import org.bukkit.Material;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TextContext {
    private String message = "";
    private int replace = 1;
    private short data = 0;
    private String fontPath = "sans_serif";
    private int size = 12;
    private boolean bold = false;
    private boolean italics = false;
    private boolean air = false;

    public String getMessage() {
        return message;
    }

    public TextContext setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getReplacement() {
        return replace;
    }

    public TextContext setReplacement(Material replace, short data) throws Exception {
        if(replace.isBlock()) {
            this.replace = replace.getId();
            this.data = data;
        } else
            throw new Exception("Only blocks can be used as a replacement material!");
        return this;
    }

    public boolean isItalics() {
        return italics;
    }

    public TextContext setItalics(boolean italics) {
        this.italics = italics;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public TextContext setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public int getSize() {
        return size;
    }

    public TextContext setSize(int size) {
        this.size = size;
        return this;
    }

    public void setDamageValue(short damage) {
        data = damage;
    }

    public short getDamageValue() {
        return data;
    }

    public Font getFont() throws Exception {
        if(fontPath.equalsIgnoreCase("sans_serif")) //Default option
            return new Font(Font.SANS_SERIF, getOptions(), getSize());
        else {
            try {
                Font f = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(f);
                return  f.deriveFont(getOptions(), getSize());
            } catch (IOException | FontFormatException e) {
                throw new Exception("Error loading font, does it exist?");
            }
        }
    }

    public TextContext setFont(String fontPath) {
        this.fontPath = fontPath;
        return this;
    }

    public boolean shouldReplaceWithAir() {
        return air;
    }

    public TextContext setAir(boolean air) {
        this.air = air;
        return this;
    }

    private int getOptions() {
        if(!isBold() && !isItalics())
            return Font.PLAIN;
        else if(isBold() && !isItalics())
            return Font.BOLD;
        else if(!isBold() && isItalics())
            return Font.ITALIC;
        else
            return Font.BOLD | Font.ITALIC;
    }

    public boolean isReplacementBlock(BaseBlock block) {
        return block.getId() == getReplacement() && block.getData() == getDamageValue();
    }
}
