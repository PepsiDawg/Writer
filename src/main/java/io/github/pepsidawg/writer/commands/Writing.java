package io.github.pepsidawg.writer.commands;

import com.sk89q.minecraft.util.commands.*;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import io.github.pepsidawg.writer.Writer;
import io.github.pepsidawg.writer.commands.completions.TextSettingsCompletions;
import io.github.pepsidawg.writer.util.TextContext;
import io.github.pepsidawg.writer.util.WritingManager;
import io.github.pepsidawg.writer.util.WritingUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Writing {
    static WritingManager writingManager = WritingManager.getInstance();

    public static class Writings {
        @Command(
                aliases = {"writer", "writing"},
                desc = "Base writing command"
        )
        @CommandPermissions("writings.edit")
        @NestedCommand(Writing.class)
        @TabCompletion(token = "writer")
        @NestedTabCompletion(Writing.class)
        public static void write(CommandContext args, CommandSender sender) { }
    }

    @Command(
            aliases = {"set"},
            desc = "Set text attributes"
    )
    @NestedCommand(TextSettings.class)
    @TabCompletion(token = "set")
    @NestedTabCompletion({TextSettingsCompletions.class, TextSettings.class})
    public static void set(CommandContext args, CommandSender sender) { }


    @Command(
            aliases = {"print", "write"},
            desc = "Writes out your message in blocks!",
            usage = "<message>",
            min = 1
    )
    @TabCompletion(token = "print")
    public static void print(CommandContext args, CommandSender sender) throws CommandPermissionsException, Exception {
        if(!(sender instanceof Player))
            throw new CommandPermissionsException();

        Player player = (Player) sender;
        TextContext context = writingManager.getWriting(player);
        WorldEditPlugin worldEdit = Writer.getInstance().getWorldEdit();
        EditSession edit = worldEdit.createEditSession(player);

        WritingUtil.print(context.setMessage(args.getString(0, args.argsLength() - 1)), edit, player.getLocation().clone());
        worldEdit.remember(player, edit);
        player.sendMessage(ChatColor.GREEN + String.valueOf(edit.size()) + ChatColor.DARK_AQUA + " blocks changed");
    }

    @Command(
            aliases = {"reset"},
            desc = "Resets the writing settings",
            min = 0, max = 0
    )
    @TabCompletion(token = "reset")
    public static void reset(CommandContext args, CommandSender sender) throws CommandPermissionsException {
        if(!(sender instanceof Player))
            throw new CommandPermissionsException();
        writingManager.addWriting(((Player) sender));
        sender.sendMessage(ChatColor.DARK_AQUA + "Your text settings have been reset!");
    }

    @Command(
            aliases = {"reload"},
            desc = "reloads the fonts",
            max = 0
    )
    @CommandPermissions("writer.reload")
    @TabCompletion(token = "reload")
    public static void reload(CommandContext context, CommandSender sender) throws Exception {
        try {
            int loaded = WritingManager.getInstance().loadFontPaths();
            sender.sendMessage(ChatColor.GREEN + String.valueOf(loaded) + ChatColor.DARK_AQUA + " fonts loaded!");
        } catch (Exception e) {
            throw new Exception("Could not load fonts!");
        }
    }
}
