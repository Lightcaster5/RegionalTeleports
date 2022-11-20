package dev.jonahm.regionalteleports.managers.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class ConfigManager {

    private final Plugin plugin;

    private final Map<String, Config> configMap = new HashMap<>();

    private final Map<String, String> messageMap = new HashMap<>();
    private final Map<String, List<String>> multiMessageMap = new HashMap<>();

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
    }

    public void createConfig(String name, boolean loadFromStream) {
        if (!configMap.containsKey(name)) {
            configMap.put(name, new Config(name, loadFromStream, plugin));
        }
        if (name.equalsIgnoreCase("messages")) {
            loadMessages();
        }
    }

    public Config getConfig(String name) {
        return configMap.get(name);
    }

    private void loadMessages() {
        if (!configMap.containsKey("messages")) return;
        FileConfiguration messages = getConfig("messages").getFileConfiguration();
        for (String path : messages.getKeys(false)) {
            if (messages.isString(path))
                messageMap.put(path, messages.getString(path));
            else if (messages.isList(path))
                multiMessageMap.put(path, messages.getStringList(path));
        }
    }

    public String getMessage(String path) {
        return messageMap.get(path);
    }

    public List<String> getMultiMessage(String path) {
        return multiMessageMap.get(path);
    }

}

