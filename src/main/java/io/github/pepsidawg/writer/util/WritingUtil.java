package io.github.pepsidawg.writer.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WritingUtil {


    public static int[][] getBlockArray(TextContext context) throws Exception {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        Rectangle2D bounds;
        LineMetrics metrics;
        int width, height;

        img.getGraphics().setFont(context.getFont());

        bounds = context.getFont().getStringBounds(context.getMessage(), img.getGraphics().getFontMetrics().getFontRenderContext());
        metrics = context.getFont().getLineMetrics(context.getMessage(), img.getGraphics().getFontMetrics().getFontRenderContext());
        width = (int)Math.ceil(bounds.getWidth());
        height = (int)metrics.getHeight();
        img = new BufferedImage(width, height*2, BufferedImage.TYPE_INT_ARGB);

        Bukkit.broadcastMessage(width + "X" + height);
        Bukkit.broadcastMessage("Ascent: " + metrics.getAscent() + " Descent: " + metrics.getDescent() + " Height: " + metrics.getHeight());

        Graphics2D g2d = img.createGraphics();
        g2d.setFont(context.getFont());
        g2d.setColor(Color.BLACK);
        g2d.drawString(context.getMessage(), 0, 0);
        g2d.dispose();

        int[][] writing = new int[height][width];
        for(int h = 0; h < height; h++)
            for(int w = 0; w < width; w++)
                writing[h][w] = img.getRGB(w, h);

        return trim(writing);
    }

    private static int[][] trim(int[][] input) {
        List<Integer> empty = new ArrayList<>();

        for(int h = 0; h < input.length; h++) {
            boolean emptyRow = true;
            for(int w = 0; w < input[0].length; w++) {
                if(input[h][w] != 0) {
                    emptyRow = false;
                }
            }
            if(emptyRow)
                empty.add(h);
        }

        int[][] result = new int[input.length - empty.size()][input[0].length];
        int h2 = 0, w2 = 0;
        for(int h = 0; h < input.length; h++) {
            if(empty.contains(h))
                continue;

            for(int w = 0; w < input[0].length; w++) {
                result[h2][w2] = input[h][w];
                w2++;
            }
            w2 = 0;
            h2++;
        }
        return result;
    }

    public static void print(TextContext context, EditSession edit, Location start) throws Exception {
       int[][] messageArray = getBlockArray(context);

        edit.enableQueue();
        for(int h = 0; h < messageArray.length; h++) {
            for (int w = 0; w < messageArray[0].length; w++) {
                Location newloc = start.clone().add(w, -(h), 0);
                Vector vec = new Vector(newloc.getX(), newloc.getY(), newloc.getZ());

                if (messageArray[h][w] != 0) {
                    if(!(context.isReplacementBlock(edit.getBlock(vec)))) {
                        edit.setBlock(vec, new BaseBlock(context.getReplacement(), context.getDamageValue()));
                    }
                } else {
                    if(edit.getBlock(vec).getId() != 0 && context.shouldReplaceWithAir())
                        edit.setBlock(vec, new BaseBlock(0));
                }
            }
        }
        edit.flushQueue();
    }
}
