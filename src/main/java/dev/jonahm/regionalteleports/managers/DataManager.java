package dev.jonahm.regionalteleports.managers;

import dev.jonahm.regionalteleports.RegionalTeleports;
import dev.jonahm.regionalteleports.data.RegionBound;
import dev.jonahm.regionalteleports.data.TeleportRegion;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public static String wildCommandPermission, wildCommandTeleportedMessage, wildCommandNoPermissionMessage;

    public final static List<TeleportRegion> teleportRegions = new ArrayList<>();

    public static boolean worldGuardEnabled, worldGuardBlacklistAll;

    public final static List<String> worldGuardBlacklistedRegions = new ArrayList<>();

    public DataManager() {
        init();
    }

    private void init() {
        FileConfiguration config = RegionalTeleports.getConfigManager().getConfig("config").getFileConfiguration();
        wildCommandPermission = config.getString("wild-command.permission");
        wildCommandTeleportedMessage = config.getString("wild-command.messages.teleported");
        wildCommandNoPermissionMessage = config.getString("wild-command.messages.no-permission");

        for (String regionEntry : config.getConfigurationSection("teleport-regions").getKeys(false)) {
            String path = "teleport-regions." + regionEntry + ".", boundsPath = path + "bounds.";
            String permission = config.getString(path + "permission");
            RegionBound bound = new RegionBound(config.getString(boundsPath + "world"), config.getInt(boundsPath + "min.x"),
                    config.getInt(boundsPath + "min.z"), config.getInt(boundsPath + "max.x"), config.getInt(boundsPath + "max.z"));
            teleportRegions.add(new TeleportRegion(regionEntry, permission, bound));
        }

        worldGuardEnabled = config.getBoolean("worldguard.enabled");
        worldGuardBlacklistAll = config.getBoolean("worldguard.blacklist-all");
        worldGuardBlacklistedRegions.addAll(config.getStringList("worldguard.blacklisted-regions"));
    }

}
