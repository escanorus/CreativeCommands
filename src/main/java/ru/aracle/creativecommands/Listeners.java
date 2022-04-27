package ru.aracle.creativecommands;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import ru.aracle.creativecommands.commands.Speed;
import ru.aracle.creativecommands.operations.Components;

import java.util.HashMap;
import java.util.List;

public class Listeners implements Listener {
    @EventHandler
    public void back(PlayerTeleportEvent event) {
        HashMap<Player, Location> locations = CreativeCommands.maps.locations();
        locations.put(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void waypoint(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        String key = CreativeCommands.waypoint.string(player.getName()+".using-f");
        if (key == null) {
            CreativeCommands.waypoint.configuration().set(player.getName()+".using-f", "true");
            CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints-count", 0);
            CreativeCommands.waypoint.save();
            CreativeCommands.waypoint.reload();
        }
        else if (key.equals("true")) {
            if (player.isSneaking()) {
                event.setCancelled(true);
                int count = CreativeCommands.waypoint.integer(player.getName()+".waypoints-count");
                if (CreativeCommands.waypoint.strings(player.getName()+".waypoints") == null) {
                    CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints", new String[]{"1|"+player.getLocation()});
                } else {
                    List<String> waypoints = CreativeCommands.waypoint.strings(player.getName()+".waypoints");
                    if (count < 10) {
                        waypoints.add((count+1)+"|"+player.getLocation());
                    } else {
                        player.sendActionBar(Components.translate("Messages.Waypoint.max"));
                        return;
                    }
                    CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints", waypoints);
                }
                CreativeCommands.waypoint.configuration().set(player.getName()+".waypoints-count", count+1);
                CreativeCommands.waypoint.save();
                CreativeCommands.waypoint.reload();
                player.sendActionBar(Components.LegacyComponent(CreativeCommands.settings.string("Messages.Waypoint.saved").replace("%count%", String.valueOf(count+1))));
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1,2);
            }
        }
    }

    @EventHandler
    public void sneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            Player player = event.getPlayer();
            String scroll = CreativeCommands.speed.string("Speed-scrolling."+player.getName());
            if (scroll != null && scroll.equals("true")) {
                player.sendActionBar(Components.translate("Messages.Speed.usage-scroll"));
            }
        }
    }

    @EventHandler
    public void speed(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        String scroll = CreativeCommands.speed.string("Speed-scrolling."+player.getName());
        if (!CreativeCommands.maps.speed().containsKey(player)) {
            CreativeCommands.maps.speed.put(player, new Integer[]{1,1});
        }
        if (scroll == null) {
            CreativeCommands.speed.configuration().set("Speed-scrolling."+player.getName(), "true");
            CreativeCommands.speed.save();
            CreativeCommands.speed.reload();
        }
        else if (scroll.equals("true")) {
            if (player.isSneaking()) {
                if (player.isFlying()) {
                    int speed = Speed.math(CreativeCommands.maps.speed().get(player)[0], event.getNewSlot(), event.getPreviousSlot());
                    CreativeCommands.maps.speed().put(player, new Integer[] {speed, CreativeCommands.maps.speed().get(player)[1]});
                    player.setFlySpeed((float) speed /10);
                } else {
                    int speed = Speed.math(CreativeCommands.maps.speed().get(player)[1], event.getNewSlot(), event.getPreviousSlot());
                    CreativeCommands.maps.speed().put(player, new Integer[] {CreativeCommands.maps.speed().get(player)[0], speed});
                    player.setWalkSpeed((float) speed /10);
                }
                Speed.information(player.isFlying(), player);
            }
        }
    }
}
