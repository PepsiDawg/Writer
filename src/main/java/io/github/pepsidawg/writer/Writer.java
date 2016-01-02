package io.github.pepsidawg.writer;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.bukkit.util.TabCompletionManager;
import com.sk89q.bukkit.util.TabCompletionRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import io.github.pepsidawg.writer.commands.Writing;
import io.github.pepsidawg.writer.util.WritingManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Writer extends JavaPlugin {
    private static Writer self;
    private CommandsManager<CommandSender> commands;
    private TabCompletionManager tabCompletionManager;
    private static WorldEditPlugin worldEdit;

    @Override
    public void onEnable() {
        self = this;
        setupCommands();
        setupTabCompleters();
        setupDirectiories();

        try {
            int loaded = WritingManager.getInstance().loadFontPaths();
            getLogger().info(loaded + " fonts loaded!");
        } catch (Exception e) {
            getLogger().severe("Could not load fonts!");
        }
    }

    public static Writer getInstance() {
        return self;
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };

        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);

        cmdRegister.register(Writing.Writings.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String[] _args = debug(args);

        try {
            this.commands.execute(cmd.getName(), _args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + e.getCause().getMessage());
                if (debug) {
                    e.getCause().printStackTrace();
                    debug = false;
                }
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            if (debug) {
                e.getCause().printStackTrace();
                debug = false;
            }
        }

        return true;
    }

    private boolean debug = false;
    private String[] debug(String[] args) {
        List<String> argList = new LinkedList<>(Arrays.asList(args));
        String[] res;

        if(argList.contains("--debug")) {
            debug = true;
            argList.remove("--debug");
            res = new String[argList.size()];
            return argList.toArray(res);
        }
        return args;
    }

    private void setupTabCompleters() {
        tabCompletionManager = new TabCompletionManager();
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        TabCompletionRegistration registration = new TabCompletionRegistration(tabCompletionManager, cmdRegister, this);

        registration.registerTabCompletions(Writing.Writings.class);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabCompletionManager.execute(sender, command, alias, args);
    }

    private void setupDirectiories() {
        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        List<String> directories = Arrays.asList(
                getDataFolder().getAbsolutePath() + "/Fonts"
        );

        for(String dir : directories) {
            File file = new File(dir);
            if(!file.exists())
                file.mkdirs();
        }
    }

    public WorldEditPlugin getWorldEdit() throws Exception {
        if(worldEdit == null) {
            Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");

            if(plugin instanceof WorldEditPlugin)
                worldEdit = (WorldEditPlugin) plugin;
            else throw new Exception("WorldEdit is not loaded!");
        }
        return worldEdit;
    }
}
