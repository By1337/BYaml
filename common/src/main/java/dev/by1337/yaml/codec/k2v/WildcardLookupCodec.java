package dev.by1337.yaml.codec.k2v;

import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.YamlCodec;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import dev.by1337.yaml.util.Wildcard;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class WildcardLookupCodec<V> implements YamlCodec<List<V>> {

    private final Map<String, V> k2v = new HashMap<>();
    private final Map<V, String> v2k = new IdentityHashMap<>();
    private SchemaType schemaType = SchemaTypes.ANY;

    public WildcardLookupCodec(Map<String, V> map) {
        map.forEach(this::put);
        createSchemaType();
    }

    public WildcardLookupCodec(Collection<V> collection, Function<V, String> mapper) {
        collection.forEach(v -> put(mapper.apply(v), v));
        createSchemaType();
    }

    public WildcardLookupCodec(Iterator<V> iterator, Function<V, String> mapper) {
        iterator.forEachRemaining(v -> put(mapper.apply(v), v));
        createSchemaType();
    }

    public WildcardLookupCodec(V[] array, Function<V, String> mapper) {
        for (V v : array) {
            put(mapper.apply(v), v);
        }
    }

    private void createSchemaType() {
        var enumOf = JsonSchemaTypeBuilder.create().enumOf(k2v.keySet()).build();
        schemaType = SchemaTypes.anyOf(
                enumOf,
                enumOf.listOf(),
                SchemaTypes.STRING,
                SchemaTypes.STRING.listOf()
        );
    }

    public static <T extends Enum<T>> WildcardLookupCodec<T> fromEnum(T[] enums) {
        return new WildcardLookupCodec<>(enums, Enum::name);
    }

    private void put(String name, V v) {
        k2v.put(name.toLowerCase(), v);
        v2k.put(v, name.toLowerCase());
    }

    public DataResult<List<V>> decode$(YamlValue value) {
        return decode(value);
    }

    @Override
    public DataResult<List<V>> decode(YamlValue value) {
        if (value.isCollection()) {
            return STRING_LIST.decode(value).flatMap(l -> {
                List<V> result = new ArrayList<>();
                StringBuilder error = new StringBuilder();
                l.forEach(s -> {
                    if (!accept(s.toLowerCase(), result::add)) {
                        error.append("Unknown key: ").append(s).append("\n");
                    }
                });
                if (error.isEmpty()) {
                    return DataResult.success(result);
                }
                error.setLength(error.length() - 1);
                return DataResult.error(error.toString()).partial(result);
            });
        } else {
            return STRING.decode(value).flatMap(s -> {
                var v = get(s.toLowerCase());
                if (v.isEmpty()) return DataResult.error("Unknown key: " + s);
                return DataResult.success(v);
            });
        }
    }

    private List<V> get(String key) {
        List<V> list = new ArrayList<>();
        accept(key, list::add);
        return list;
    }

    private boolean accept(String key, Consumer<V> consumer) {
        V value = k2v.get(key);
        if (value != null) {
            consumer.accept(value);
            return true;
        }
        AtomicBoolean hasValue = new AtomicBoolean(false);
        k2v.forEach((k, v) -> {
            if (Wildcard.matches(k, key)) {
                consumer.accept(v);
                hasValue.set(true);
            }
        });
        return hasValue.get();
    }

    @Override
    public YamlValue encode(List<V> list) {
        return encode$(list);
    }

    private YamlValue encode$(Collection<V> list) {
        List<String> result = new ArrayList<>();
        for (V value : list) {
            String key = v2k.get(value);
            if (key != null) {
                result.add(key);
            }
        }
        return YamlValue.wrap(result);
    }

    @Override
    public @NotNull SchemaType schema() {
        return schemaType;
    }

    public <R extends Collection<V>> YamlCodec<R> as(Function<List<V>, R> mapper) {
        return new YamlCodec<R>() {
            @Override
            public DataResult<R> decode(YamlValue value) {
                return decode$(value).flatMap(list -> DataResult.success(mapper.apply(list)));
            }

            @Override
            public YamlValue encode(R value) {
                return encode$(value);
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }

    public YamlCodec<Set<V>> asSet() {
        return as(HashSet::new);
    }

    public YamlCodec<V[]> asArray() {
        return new YamlCodec<V[]>() {
            @Override
            public DataResult<V[]> decode(YamlValue value) {
                return WildcardLookupCodec.this.decode(value).flatMap(l -> DataResult.success((V[]) l.toArray()));
            }

            @Override
            public YamlValue encode(V[] value) {
                return encode$(Arrays.asList(value));
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }
}
