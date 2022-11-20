package dev.jonahm.regionalteleports.managers;

import dev.jonahm.regionalteleports.RegionalTeleports;
import dev.jonahm.regionalteleports.data.TeleportRegion;
import dev.jonahm.regionalteleports.hooks.WorldGuardHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomLocationManager {

    private final Map<String, List<Location>> locationCache = new HashMap<>();

    public RandomLocationManager() {
        init();
    }

    private void init() {
        for (TeleportRegion teleportRegion : DataManager.teleportRegions) {
            refillLocationCache(teleportRegion.permission());
        }
    }

    public void refillLocationCache(String regionPermission) {
        TeleportRegion region = DataManager.teleportRegions.stream().filter(r -> r.permission().equals(regionPermission)).findFirst().orElse(null);
        if (region == null) return;
        List<Location> locations = locationCache.get(regionPermission);
        if (locations == null) locations = new ArrayList<>();

        int x, z;
        Location location = null;
        World world = Bukkit.getWorld(region.regionBounds().worldName());
        boolean safe = false;

        while (!safe) {
            x = getRandom(region.regionBounds().minX(), region.regionBounds().maxX());
            z = getRandom(region.regionBounds().minZ(), region.regionBounds().maxZ());
            if (world == null) return;
            location = world.getHighestBlockAt(x, z).getLocation();
            if (location.getBlock().getType().isSolid() && worldGuardCompliant(location, world.getName())) {
                safe = true;
            }
        }

        locations.add(location.add(0, 1, 0));
        locationCache.put(regionPermission, locations);
        if (locations.size() < 10) refillLocationCache(regionPermission);
    }

    public Location getRandomLocation(Player player) {
        for (Map.Entry<String, List<Location>> entry : locationCache.entrySet()) {
            if (player.hasPermission(entry.getKey())) {
                Location location = entry.getValue().get(0);
                List<Location> locations = entry.getValue();
                locations.remove(0);
                locationCache.put(entry.getKey(), locations);
                refillLocationCache(entry.getKey());
                return location;
            }
        }
        return null;
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private boolean worldGuardCompliant(Location location, String worldName) {
        if (!DataManager.worldGuardEnabled) return true;
        WorldGuardHook worldGuardHook = RegionalTeleports.getWorldGuardHook();
        if (worldGuardHook == null || !worldGuardHook.isEnabled()) return true;
        if (DataManager.worldGuardBlacklistAll) return !worldGuardHook.isLocationInAnyRegion(location, worldName);
        else return !worldGuardHook.isLocationInRegion(location, DataManager.worldGuardBlacklistedRegions, worldName);
    }

}
