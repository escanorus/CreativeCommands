package ru.aracle.creativecommands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.aracle.creativecommands.CreativeCommands;
import ru.aracle.creativecommands.operations.Components;

import java.util.ArrayList;
import java.util.List;

public class Commands implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                if (args.length > 0) {
                    String first = args[0];
                    if (first.equals("reload")) {
                        CreativeCommands.settings.reload();
                        sender.sendActionBar(Components.translate("Messages.Reload"));
                    }
                }
            }
        } else {
            if (args.length > 0) {
                String first = args[0];
                if (first.equals("reload")) {
                    CreativeCommands.settings.reload();
                    sender.sendActionBar(Components.translate("Messages.Reload"));
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.add("reload");
        }
        return tabs;
    }
}
