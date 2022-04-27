package ru.aracle.creativecommands.commands;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.aracle.creativecommands.CreativeCommands;
import ru.aracle.creativecommands.operations.Components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Teleport implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length > 0) {
                String first = args[0];
                ArrayList<String> players= new ArrayList<>();
                for (Player online : online()) {
                    players.add(online.getName());
                }
                if (players.contains(first)) {
                    assert player != null;
                    teleport(player, Objects.requireNonNull(Bukkit.getServer().getPlayer(first)));
                } else if (!first.equals("accept") && !first.equals("deny")) {
                    sender.sendActionBar(Components.translate("Messages.Teleport.not-online"));
                }
                if (first.equals("accept")) {
                    accept(player);
                }
                if (first.equals("deny")) {
                    deny(player);
                }
            } else {
                sender.sendActionBar(Components.translate("Messages.Teleport.usage"));
            }
        }
        return false;
    }

    public static Collection<? extends Player> online() {
        return Bukkit.getServer().getOnlinePlayers();
    }

    public static void teleport(Player sender, Player target) {
        if (sender != target) {
            if (!sender.isOp()) {
                String query = CreativeCommands.settings.string("Messages.Teleport.query");
                String acceptButton = CreativeCommands.settings.string("Messages.Teleport.accept_button");
                String space = CreativeCommands.settings.string("Messages.Teleport.space_between");
                String denyButton = CreativeCommands.settings.string("Messages.Teleport.deny_button");
                String accept = CreativeCommands.settings.string("Messages.Teleport.accept_button");
                String deny = CreativeCommands.settings.string("Messages.Teleport.deny_button");
                target.sendMessage(Components.LegacyComponent(" "));
                target.sendMessage(Components.LegacyComponent(query.replace("%player%", target.getName())));
                target.sendMessage(Components.LegacyComponent(acceptButton)
                        .hoverEvent(HoverEvent.showText(Components.LegacyComponent(accept)))
                        .clickEvent(ClickEvent.runCommand("/teleport accept"))
                        .append(Components.LegacyComponent(space))
                        .append(Components.LegacyComponent(denyButton)
                                .hoverEvent(HoverEvent.showText(Components.LegacyComponent(deny)))
                                .clickEvent(ClickEvent.runCommand("/teleport deny"))));
                target.sendMessage(Components.LegacyComponent(" "));
                CreativeCommands.maps.teleport().put(target, sender);
            } else {
                sender.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Teleport.accepted-target").replace("%player%", target.getName())));
                sender.teleport(target.getLocation());
            }
        } else {
            sender.sendActionBar(Components.translate("Messages.Teleport.self-teleport"));
        }
    }

    public static void accept(Player sender) {
        Player target = CreativeCommands.maps.teleport().get(sender);
        if (target != null) {
            if (target.isOnline()) {
                target.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Teleport.accepted-target").replace("%player%", sender.getName())));
                target.teleport(sender.getLocation());
            }
            sender.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Teleport.accepted-sender").replace("%player%", target.getName())));
            CreativeCommands.maps.teleport().remove(sender);
        } else {
            sender.sendActionBar(Components.translate("Messages.Teleport.no-queries"));
        }
    }

    public static void deny(Player sender) {

        Player target = CreativeCommands.maps.teleport().get(sender);
        if (target != null) {
            target.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Teleport.denied-target").replace("%player%", sender.getName())));
            sender.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Teleport.denied-sender").replace("%player%", target.getName())));
            CreativeCommands.maps.teleport().remove(sender);
        }
        else {
            sender.sendActionBar(Components.translate("Messages.Teleport.no-queries"));
        }
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.add("accept");
            tabs.add("deny");
            for (Player online : online()) {
                tabs.add(online.getName());
            }
        }
        return tabs;
    }
}
