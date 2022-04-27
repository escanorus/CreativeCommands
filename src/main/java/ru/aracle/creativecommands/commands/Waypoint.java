package ru.aracle.creativecommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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

public class Waypoint implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            assert player != null;
            if (args.length > 0) {
                String first = args[0];
                if (first.equals("clear")) {
                    CreativeCommands.waypoint.configuration().set(player.getName() + ".waypoints", null);
                    CreativeCommands.waypoint.configuration().set(player.getName() + ".waypoints-count", 0);
                    CreativeCommands.waypoint.save();
                    CreativeCommands.waypoint.reload();
                    player.sendActionBar(Components.translate("Messages.Waypoint.cleared"));
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,2);
                    return false;
                }
                if (first.equals("toggle")) {
                    String key = CreativeCommands.waypoint.string(player.getName() + ".using-f");
                    if (key == null) {
                        CreativeCommands.waypoint.configuration().set(player.getName() + ".using-f", "true");
                        CreativeCommands.waypoint.configuration().set(player.getName() + ".waypoints-count", 0);
                        CreativeCommands.waypoint.save();
                        CreativeCommands.waypoint.reload();
                        player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Waypoint.using-f-on")));
                        return false;
                    }
                    if (key.equals("true")) {
                        CreativeCommands.waypoint.configuration().set(player.getName() + ".using-f", "false");
                        CreativeCommands.waypoint.save();
                        CreativeCommands.waypoint.reload();
                        player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Waypoint.using-f-off")));
                        return false;
                    }
                    if (key.equals("false")) {
                        CreativeCommands.waypoint.configuration().set(player.getName() + ".using-f", "true");
                        CreativeCommands.waypoint.save();
                        CreativeCommands.waypoint.reload();
                        player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Waypoint.using-f-on")));
                        return false;
                    }
                }
                if (first.equals("last")) {
                    int number = CreativeCommands.waypoint.integer(player.getName()+".waypoints-count");
                    teleport(number, player);
                    return false;
                }
                if (first.equals("set")) {
                    int count = CreativeCommands.waypoint.integer(player.getName()+".waypoints-count");
                    if (CreativeCommands.waypoint.strings(player.getName()+".waypoints") == null) {
                        CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints", new String[]{"1|"+player.getLocation()});
                    } else {
                        List<String> waypoints = CreativeCommands.waypoint.strings(player.getName()+".waypoints");
                        if (count < 10) {
                            waypoints.add((count+1)+"|"+player.getLocation());
                        } else {
                            player.sendActionBar(Components.translate("Messages.Waypoint.max"));
                            return false;
                        }
                        CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints", waypoints);
                    }
                    CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints-count", count+1);
                    CreativeCommands.waypoint.save();
                    CreativeCommands.waypoint.reload();
                    player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Waypoint.saved").replace("%count%", String.valueOf(count+1))));
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1,2);
                    return false;
                }
                if (first.equals("1")) { teleport(1, player); return false; }
                if (first.equals("2")) { teleport(2, player); return false; }
                if (first.equals("3")) { teleport(3, player); return false; }
                if (first.equals("4")) { teleport(4, player); return false; }
                if (first.equals("5")) { teleport(5, player); return false; }
                if (first.equals("6")) { teleport(6, player); return false; }
                if (first.equals("7")) { teleport(7, player); return false; }
                if (first.equals("8")) { teleport(8, player); return false; }
                if (first.equals("9")) { teleport(9, player); return false; }
                if (first.equals("10")) { teleport(10, player); return false; }
            } else {
                player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Waypoint.usage")));
            }
        }
        return false;
    }

    public static void teleport(int number, Player player) {
        String waypoint = null;
        for (String string : CreativeCommands.waypoint.strings(player.getName() + ".waypoints")) {
            if (string.split("\\|")[0].equals(String.valueOf(number))) {
                waypoint = string;
            }
        }
        if (waypoint != null) {
            String world = waypoint.split("name=")[1].split("},x")[0];
            String x = waypoint.split("x=")[1].split(",y")[0];
            String y = waypoint.split("y=")[1].split(",z")[0];
            String z = waypoint.split("z=")[1].split(",p")[0];
            String pith = waypoint.split("pitch=")[1].split(",yaw")[0];
            String yaw = waypoint.split("yaw=")[1].replace("}","");
            Location location = new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z), Float.parseFloat(yaw), Float.parseFloat(pith));
            player.teleport(location);
            player.sendActionBar(Components.translate("Messages.Waypoint.teleport"));
            player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1,1);
        } else {
            player.sendActionBar(Components.translate("Messages.Waypoint.not-exist"));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.add("toggle");
            tabs.add("clear");
            tabs.add("last");
            tabs.add("set");
            int number = CreativeCommands.waypoint.integer(sender.getName()+".waypoints-count");
            if (number != 0) {
                int i = 0;
                while (i < number) {
                    i++;
                    tabs.add(String.valueOf(i));
                }
            }
        }
        return tabs;
    }
}
