package dev.jonahm.regionalteleports.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public class WorldGuardHook {

    @Getter
    private final boolean enabled;

    private final RegionContainer container;

    public WorldGuardHook() {
        enabled = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        if (enabled) {
            container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        } else {
            container = null;
        }
    }

    public boolean isLocationInAnyRegion(Location location, String worldName) {
        if (!enabled) return false;
        RegionManager regions = getRegionManager(worldName);
        if (regions == null) return false;
        for (ProtectedRegion protectedRegion : regions.getRegions().values()) {
            if (protectedRegion.contains(BukkitAdapter.asBlockVector(location))) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocationInRegion(Location location, List<String> regionNames, String worldName) {
        if (!enabled) return false;
        RegionManager regions = getRegionManager(worldName);
        if (regions == null) return false;
        for (Map.Entry<String, ProtectedRegion> entry : regions.getRegions().entrySet()) {
            if (regionNames.contains(entry.getKey())) {
                if (entry.getValue().contains(BukkitAdapter.asBlockVector(location))) {
                    return true;
                }
            }
        }
        return false;
    }

    private RegionManager getRegionManager(String worldName) {
        if (!enabled) return null;
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;
        return container.get(BukkitAdapter.adapt(world));
    }

}
