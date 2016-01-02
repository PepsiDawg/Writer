package io.github.pepsidawg.writer.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.TabCompletion;
import io.github.pepsidawg.writer.util.WritingManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TextSettings {

    @Command(
            aliases = {"font"},
            desc = "Sets the font to be written in",
            usage = "<font>",
            min = 1, max = 1
    )
    public static void font(CommandContext args, CommandSender sender) throws Exception {
        WritingManager manager = WritingManager.getInstance();
        Player player = playerCheck(sender);

        if(manager.isFont(args.getString(0))) {
            manager.getWriting(player).setFont(manager.getFontPath(args.getString(0)));
            player.sendMessage(ChatColor.DARK_AQUA + "Font set to " + ChatColor.GREEN + args.getString(0));
        }
    }

    @Command(
            aliases = {"size"},
            desc = "Sets the font size!",
            usage = "<size>",
            min = 1, max = 1
    )
    @TabCompletion(
            token = "size",
            tabOptions = {"12","14","16","18","20","24","32","48"}
    )
    public static void size(CommandContext args, CommandSender sender) throws Exception {
        WritingManager manager = WritingManager.getInstance();
        Player player = playerCheck(sender);

        manager.getWriting(player).setSize(args.getInteger(0));
        player.sendMessage(ChatColor.DARK_AQUA + "Font size set to " + ChatColor.GREEN + args.getString(0));
    }


    @Command(
            aliases = {"bold"},
            desc = "Sets if the text should be bold",
            usage = "<true|false>",
            min = 1, max = 1
    )
    @TabCompletion(
            token = "bold",
            tabOptions = {"true", "false"}
    )
    public static void bold(CommandContext context, CommandSender sender) throws Exception {
        WritingManager manager = WritingManager.getInstance();
        Player player = playerCheck(sender);

        manager.getWriting(player).setBold(getBoolean(context.getString(0)));
        player.sendMessage(ChatColor.DARK_AQUA + "Bold set to " + ChatColor.GREEN + context.getString(0));
    }


    @Command(
            aliases = {"italics"},
            desc = "Sets if the text should be italicized",
            usage = "<true|false>",
            min = 1, max = 1
    )
    @TabCompletion(
            token = "italics",
            tabOptions = {"true", "false"}
    )
    public static void italics(CommandContext context, CommandSender sender) throws Exception {
        WritingManager manager = WritingManager.getInstance();
        Player player = playerCheck(sender);

        manager.getWriting(player).setItalics(getBoolean(context.getString(0)));
        player.sendMessage(ChatColor.DARK_AQUA + "Italics set to " + ChatColor.GREEN + context.getString(0));
    }


    @Command(
            aliases = {"air"},
            desc = "Sets if air should replace blocks",
            usage = "<true|false>",
            min = 1, max = 1
    )
    @TabCompletion(
            token = "air",
            tabOptions = {"true", "false"}
    )
    public static void air(CommandContext context, CommandSender sender) throws Exception {
        WritingManager manager = WritingManager.getInstance();
        Player player = playerCheck(sender);

        manager.getWriting(player).setAir(getBoolean(context.getString(0)));
        player.sendMessage(ChatColor.DARK_AQUA + "Italics set to " + ChatColor.GREEN + context.getString(0));
    }


    @Command(
            aliases = {"material"},
            desc = "Sets the material for the message to be written in",
            usage = "<material:data>",
            min = 1, max = 1
    )
    public static void material(CommandContext context, CommandSender sender) throws Exception {
        WritingManager manager = WritingManager.getInstance();
        Player player = playerCheck(sender);
        String[] materialData = context.getString(0).split(":");
        Material mat;
        short data = 0;



        try {
            mat = Material.valueOf(materialData[0]);
        } catch (Exception e) {
            throw new Exception("Invalid material " + materialData[0]);
        }

        if(materialData.length == 2) {
            data = Short.parseShort(materialData[1]);
        }

        manager.getWriting(player).setReplacement(mat, data);
        player.sendMessage(ChatColor.DARK_AQUA + "Material set to " + ChatColor.GREEN + context.getString(0));
    }


    private static boolean getBoolean(String value) throws Exception {
        if(value.equalsIgnoreCase("true"))
            return true;
        else if(value.equalsIgnoreCase("false"))
            return false;
        else
            throw new Exception("Expected boolean; Received String");
    }

    private static Player playerCheck(CommandSender sender) throws Exception {
        if(!(sender instanceof Player))
            throw new Exception("Only players may use this command!");
        return (Player) sender;
    }
}
