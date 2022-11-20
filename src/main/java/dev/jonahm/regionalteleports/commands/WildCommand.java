package dev.jonahm.regionalteleports.commands;

import dev.jonahm.regionalteleports.RegionalTeleports;
import dev.jonahm.regionalteleports.managers.DataManager;
import dev.jonahm.regionalteleports.utils.CC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WildCommand extends Command {

    public WildCommand() {
        super("wild", "Wild command", "/wild", List.of("wild"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission(DataManager.wildCommandPermission)) {
                Location location = RegionalTeleports.getRandomLocationManager().getRandomLocation(player);
                if (location != null) {
                    player.teleport(location.add(0.5, 0, 0.5));
                    player.sendMessage(CC.color(DataManager.wildCommandTeleportedMessage.replace("{x}", String.valueOf(location.getBlockX()))
                            .replace("{y}", String.valueOf(location.getBlockY())).replace("{z}", String.valueOf(location.getBlockZ()))));
                }
            } else {
                player.sendMessage(CC.color(DataManager.wildCommandNoPermissionMessage));
            }
        }
        return true;
    }

}
