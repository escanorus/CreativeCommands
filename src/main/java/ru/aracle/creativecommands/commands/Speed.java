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

public class Speed implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            assert player != null;
            if (args.length > 0) {
                if (args[0].equals("toggle")) {
                    String scroll = CreativeCommands.speed.string("Speed-scrolling." + player.getName());
                    if (scroll == null) {
                        CreativeCommands.speed.configuration().set("Speed-scrolling." + player.getName(), "true");
                        CreativeCommands.speed.save();
                        CreativeCommands.speed.reload();
                        player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Speed.scroll-on")));
                        return false;
                    }
                    if (scroll.equals("true")) {
                        CreativeCommands.speed.configuration().set("Speed-scrolling." + player.getName(), "false");
                        CreativeCommands.speed.save();
                        CreativeCommands.speed.reload();
                        player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Speed.scroll-off")));
                        return false;
                    }
                    if (scroll.equals("false")) {
                        CreativeCommands.speed.configuration().set("Speed-scrolling." + player.getName(), "true");
                        CreativeCommands.speed.save();
                        CreativeCommands.speed.reload();
                        player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Speed.scroll-on")));
                        return false;
                    }
                }
                if (args[0].equals("1")) { speed(1, player.isFlying(), player); return false; }
                if (args[0].equals("2")) { speed(2, player.isFlying(), player); return false; }
                if (args[0].equals("3")) { speed(3, player.isFlying(), player); return false; }
                if (args[0].equals("4")) { speed(4, player.isFlying(), player); return false; }
                if (args[0].equals("5")) { speed(5, player.isFlying(), player); return false; }
                if (args[0].equals("6")) { speed(6, player.isFlying(), player); return false; }
                if (args[0].equals("7")) { speed(7, player.isFlying(), player); return false; }
                if (args[0].equals("8")) { speed(8, player.isFlying(), player); return false; }
                if (args[0].equals("9")) { speed(9, player.isFlying(), player); return false; }
                if (args[0].equals("10")) { speed(10, player.isFlying(), player); return false; }
            } else {
                sender.sendActionBar(Components.translate("Messages.Speed.usage"));
            }
        }
        return false;
    }

    public static void speed(int speed, boolean flying, Player player) {
        if (flying) {
            CreativeCommands.maps.speed().put(player, new Integer[] {speed, CreativeCommands.maps.speed().get(player)[1]});
            player.setFlySpeed((float) speed /10);
        } else {
            CreativeCommands.maps.speed().put(player, new Integer[] {CreativeCommands.maps.speed().get(player)[0], speed});
            player.setWalkSpeed((float) speed /10);
        }
        Speed.information(player.isFlying(), player);
    }

    public static void information(boolean flying, Player player) {
        int speed;
        if (flying) {
            speed = CreativeCommands.maps.speed().get(player)[0];
            player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Speed.fly-speed").replace("%speed%", String.valueOf(speed))));
        } else {
            speed = CreativeCommands.maps.speed().get(player)[1];
            player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Speed.walk-speed").replace("%speed%", String.valueOf(speed))));
        }
    }

    public static int math(int speed, int now, int past) {
        now++;
        past++;
        if (past == 9 && now == 1) {
            if (speed < 10) speed++;
            return speed;
        }
        if (past == 1 && now == 9) {
            if (speed > 1) speed--;
            return speed;
        }
        if (now - past >= 1) {
            if (speed < 10) speed++;
            return speed;
        }
        if (now - past <= -1) {
            if (speed > 1) speed--;
        }
        return speed;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.add("toggle");
            tabs.add("1");
            tabs.add("2");
            tabs.add("3");
            tabs.add("4");
            tabs.add("5");
            tabs.add("6");
            tabs.add("7");
            tabs.add("8");
            tabs.add("9");
            tabs.add("10");
        }
        return tabs;
    }
}
