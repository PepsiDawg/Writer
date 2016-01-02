package io.github.pepsidawg.writer.commands.completions;

import com.sk89q.minecraft.util.commands.TabCompletion;
import com.sk89q.minecraft.util.commands.TabContext;
import io.github.pepsidawg.writer.util.WritingManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextSettingsCompletions {

    @TabCompletion(
            token = "font",
            useTabOptions = false
    )
    public static List<String> fontCompletion(TabContext context, CommandSender sender, Command command) {
        String token = context.getArg(context.getArgs().length - 1);
        List<String> result = new ArrayList<>();

        for(String option : WritingManager.getInstance().getFontNames())
            if(option.startsWith(token))
                result.add(option);

        //default option
        if("sans-serif".startsWith(token))
            result.add("sans-serif");

        return result;
    }


    @TabCompletion(
            token = "material",
            useTabOptions = false
    )
    public static List<String> materialCompletion(TabContext context, CommandSender sender, Command command) {
        String token = context.getArg(context.getArgs().length - 1);

        return Arrays.asList(Material.values()).stream()
                                               .filter(m -> m.name().toLowerCase().startsWith(token))
                                               .map(mat -> mat.name())
                                               .collect(Collectors.toList());
    }
}
