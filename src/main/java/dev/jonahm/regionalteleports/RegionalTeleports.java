package dev.jonahm.regionalteleports;

import dev.jonahm.regionalteleports.hooks.WorldGuardHook;
import dev.jonahm.regionalteleports.managers.CommandManager;
import dev.jonahm.regionalteleports.managers.DataManager;
import dev.jonahm.regionalteleports.managers.RandomLocationManager;
import dev.jonahm.regionalteleports.managers.configs.ConfigManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class RegionalTeleports extends JavaPlugin {

    @Getter
    private static RegionalTeleports instance;

    @Getter
    private static ConfigManager configManager;

    @Getter
    private static WorldGuardHook worldGuardHook;
    @Getter
    private static RandomLocationManager randomLocationManager;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.createConfig("config", true);

        new DataManager();

        worldGuardHook = new WorldGuardHook();
        randomLocationManager = new RandomLocationManager();

        new CommandManager();
    }

}
