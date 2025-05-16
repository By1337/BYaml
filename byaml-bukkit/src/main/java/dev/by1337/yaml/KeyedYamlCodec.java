package dev.by1337.yaml;

import dev.by1337.yaml.codec.YamlCodec;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyedYamlCodec<T extends Keyed> implements YamlCodec<T> {

    private final Map<String, T> map = new HashMap<>();
    private final SchemaType schemaType;

    public KeyedYamlCodec(final T[] keys) {
        Set<String> elements = new HashSet<>();
        for (T key : keys) {
            map.put(key.getKey().asString(), key);
            map.put(key.getKey().getKey(), key);
            elements.add(key.getKey().getKey().toLowerCase());
            if (key instanceof Enum<?> enumKey) {
                map.put(enumKey.name().toLowerCase(), key);
            }
        }
        schemaType = JsonSchemaTypeBuilder.create().enumOf(elements).build();
    }

    public KeyedYamlCodec(final Iterable<T> iterable) {
        Set<String> elements = new HashSet<>();
        for (T key : iterable) {
            map.put(key.getKey().asString(), key);
            map.put(key.getKey().getKey(), key);
            elements.add(key.getKey().getKey().toLowerCase());
            if (key instanceof Enum<?> enumKey) {
                map.put(enumKey.name().toLowerCase(), key);
            }
        }
        schemaType = JsonSchemaTypeBuilder.create().enumOf(elements).build();
    }

    @Override
    public T decode(YamlValue value) {
        String data = STRING.decode(value);
        return map.get(data.toLowerCase());
    }

    @Override
    public YamlValue encode(T value) {
        return YamlValue.wrap(value.getKey().asString());
    }

    @Override
    public @NotNull SchemaType schema() {
        return schemaType;
    }
}
