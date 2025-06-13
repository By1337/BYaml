package dev.by1337.yaml;

import dev.by1337.yaml.util.YamlReader;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@ApiStatus.Internal
class BukkitYamlReader implements YamlReader {

    @Override
    public YamlMap read(String data) {
        try {
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.loadFromString(data);

            return new YamlMap(toMap(configuration));
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static LinkedHashMap<String, Object> toMap(MemorySection memorySection) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (String key : memorySection.getKeys(false)) {
            Object value = memorySection.get(key);
            if (value instanceof MemorySection mem) {
                result.put(key, toMap(mem));
            } else if (value instanceof Collection<?> list) {
                List<Object> listToAdd = new ArrayList<>();
                for (Object o : list) {
                    if (o instanceof MemorySection mem) {
                        listToAdd.add(toMap(mem));
                    } else {
                        listToAdd.add(o);
                    }
                }
                result.put(key, listToAdd);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public String saveToString(Map<String, Object> map) {
        YamlConfiguration root = new YamlConfiguration();
        convertMapsToSections(map, root);
        return root.saveToString();
    }

    private static void convertMapsToSections(@NotNull Map<?, ?> input, @NotNull ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    static {
        YamlMap.setYamlReader(new BukkitYamlReader());
    }












}
