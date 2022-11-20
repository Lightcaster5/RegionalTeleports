package dev.jonahm.regionalteleports.managers.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class Config {

    private final File file;
    private FileConfiguration config;

    public Config(String name, boolean loadFromStream, Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        InputStream stream = plugin.getResource(name + ".yml");
        try {
            if (!file.exists()) {
                if (loadFromStream) {
                    if (stream != null) {
                        Files.copy(stream, file.toPath());
                    }
                } else {
                    file.createNewFile();
                }
            }
            load();
        } catch (IOException ignored) {
        }
    }

    private void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        load();
    }

    public FileConfiguration getFileConfiguration() {
        return config;
    }


}
