package dev.by1337.yaml;

import dev.by1337.yaml.codec.DataResult;
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
    private final String className;

    public KeyedYamlCodec(final T[] keys, String className) {
        this.className = className;
        Set<String> elements = new HashSet<>();
        for (T key : keys) {
            try {
                map.put(key.getKey().asString(), key);
                map.put(key.getKey().getKey(), key);
                elements.add(key.getKey().getKey().toLowerCase());
                if (key instanceof Enum<?> enumKey) {
                    map.put(enumKey.name().toLowerCase(), key);
                }
            } catch (Throwable ignored) {
            }
        }
        schemaType = JsonSchemaTypeBuilder.create().enumOf(elements).build();
    }

    public KeyedYamlCodec(final Iterable<T> iterable, String className) {
        this.className = className;
        Set<String> elements = new HashSet<>();
        for (T key : iterable) {
            try {
                map.put(key.getKey().asString(), key);
                map.put(key.getKey().getKey(), key);
                elements.add(key.getKey().getKey().toLowerCase());
                if (key instanceof Enum<?> enumKey) {
                    map.put(enumKey.name().toLowerCase(), key);
                }
            } catch (Throwable ignored) {
            }
        }
        schemaType = JsonSchemaTypeBuilder.create().enumOf(elements).build();
    }

    @Override
    public DataResult<T> decode(YamlValue value) {
        return STRING.decode(value).flatMap(s -> {
            var v = map.get(s.toLowerCase());
            if (v == null) return DataResult.error("Unknown key: " + s);
            return DataResult.success(v);
        });
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
