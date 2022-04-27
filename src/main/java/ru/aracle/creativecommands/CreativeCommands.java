package ru.aracle.creativecommands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.aracle.creativecommands.commands.*;
import ru.aracle.inception.configuration.Configurations;

import java.util.Objects;

public final class CreativeCommands extends JavaPlugin {

    public static CreativeCommands instance;
    public static Configurations settings;
    public static Configurations speed;
    public static Configurations waypoint;
    public static HashMaps maps;

    @Override
    public void onEnable() {
        instance = this;
        settings = new Configurations(instance, "settings.yml");
        speed = new Configurations(instance, "speed.yml");
        waypoint = new Configurations(instance, "waypoint.yml");
        settings.create();
        speed.create();
        waypoint.create();
        maps = new HashMaps();
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
        Objects.requireNonNull(getCommand("back")).setExecutor(new Back());
        Objects.requireNonNull(getCommand("tp")).setExecutor(new Teleport());
        Objects.requireNonNull(getCommand("speed")).setExecutor(new Speed());
        Objects.requireNonNull(getCommand("waypoint")).setExecutor(new Waypoint());
        Objects.requireNonNull(getCommand("creativecommands")).setExecutor(new Commands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
