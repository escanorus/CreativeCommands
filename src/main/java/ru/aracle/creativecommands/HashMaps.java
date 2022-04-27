package ru.aracle.creativecommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class HashMaps {

    HashMap<Player, Location> locations = new HashMap<>();
    HashMap<Player, Player> teleport = new HashMap<>();
    HashMap<Player, Integer[]> speed = new HashMap<>();

    public HashMap<Player, Location> locations() {
        return locations;
    }

    public HashMap<Player, Player> teleport() {
        return teleport;
    }

    public HashMap<Player, Integer[]> speed() {
        return speed;
    }

}
