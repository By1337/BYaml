package dev.by1337.yaml.codec.k2v;

import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.YamlCodec;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class LookupCodec<V> implements Key2ValueCodec<V> {

    private final Map<String, V> k2v = new HashMap<>();
    private final Map<V, String> v2k = new IdentityHashMap<>();
    private SchemaType schemaType = SchemaTypes.ANY;

    public LookupCodec(Map<String, V> map) {
        map.forEach(this::put);
        schemaType = JsonSchemaTypeBuilder.create().enumOf(k2v.keySet()).build();
    }

    public LookupCodec(Collection<V> collection, Function<V, String> mapper) {
        collection.forEach(v -> put(mapper.apply(v), v));
        schemaType = JsonSchemaTypeBuilder.create().enumOf(k2v.keySet()).build();
    }

    public LookupCodec(Iterator<V> iterator, Function<V, String> mapper) {
        iterator.forEachRemaining(v -> put(mapper.apply(v), v));
        schemaType = JsonSchemaTypeBuilder.create().enumOf(k2v.keySet()).build();
    }

    public LookupCodec(V[] array, Function<V, String> mapper) {
        for (V v : array) {
            put(mapper.apply(v), v);
        }
        schemaType = JsonSchemaTypeBuilder.create().enumOf(k2v.keySet()).build();
    }

    @Override
    public Map<String, V> asMap() {
        return k2v;
    }

    public WildcardLookupCodec<V> wildcard(){
        return new WildcardLookupCodec<>(k2v);
    }

    public static <T extends Enum<T>> LookupCodec<T> fromEnum(T[] enums) {
        return new LookupCodec<>(enums, Enum::name);
    }

    private void put(String name, V v) {
        k2v.put(name.toLowerCase(), v);
        v2k.put(v, name.toLowerCase());
    }

    @Override
    public DataResult<V> decode(YamlValue value) {
        return YamlCodec.STRING.decode(value).flatMap(s -> {
            var v = k2v.get(s.toLowerCase());
            if (v == null) return DataResult.error("Unknown key: " + s);
            return DataResult.success(v);
        });
    }

    @Override
    public YamlValue encode(V value) {
        String key = v2k.get(value);
        return YamlValue.wrap(Objects.requireNonNullElseGet(key, () -> "Unknown value: " + value));
    }

    @Override
    public @NotNull SchemaType schema() {
        return schemaType;
    }

}
