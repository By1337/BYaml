package dev.by1337.yaml.codec;

import com.google.common.base.Joiner;
import dev.by1337.yaml.YamlMap;
import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface YamlCodec<T> {
    YamlCodec<YamlValue> YAML_VALUE = of(DataResult::success, Function.identity(), SchemaTypes.ANY);
    YamlCodec<Object> OBJECT = of(v -> DataResult.success(v.getValue()), YamlValue::wrap, SchemaTypes.OBJECT);
    YamlCodec<Integer> INT = new PrimitiveMapper<>(Integer.class, o -> Integer.parseInt(o.toString()), SchemaTypes.INT);
    YamlCodec<Byte> BYTE = new PrimitiveMapper<>(Byte.class, o -> Byte.parseByte(o.toString()), SchemaTypes.INT);
    YamlCodec<Double> DOUBLE = new PrimitiveMapper<>(Double.class, o -> Double.parseDouble(o.toString()), SchemaTypes.NUMBER);
    YamlCodec<Float> FLOAT = new PrimitiveMapper<>(Float.class, o -> Float.parseFloat(o.toString()), SchemaTypes.NUMBER);
    YamlCodec<Long> LONG = new PrimitiveMapper<>(Long.class, o -> Long.parseLong(o.toString()), SchemaTypes.NUMBER);
    YamlCodec<Short> SHORT = new PrimitiveMapper<>(Short.class, o -> Short.parseShort(o.toString()), SchemaTypes.INT);
    YamlCodec<Boolean> BOOL = new PrimitiveMapper<>(Boolean.class, o -> Boolean.parseBoolean(o.toString()), SchemaTypes.BOOL);
    YamlCodec<String> STRING = new PrimitiveMapper<>(String.class, Object::toString, SchemaTypes.STRING);
    YamlCodec<Map<String, Object>> STRING_TO_OBJECT = mapOf(STRING, OBJECT);
    YamlCodec<YamlMap> YAML_MAP = of(YamlValue::asYamlMap, YamlMap::get, SchemaTypes.OBJECT);
    YamlCodec<List<YamlMap>> YAML_MAP_LIST = YAML_MAP.listOf();
    YamlCodec<List<String>> STRING_LIST = STRING.listOf();
    YamlCodec<List<Integer>> INT_LIST = INT.listOf();
    YamlCodec<Map<String, String>> STRING_TO_STRING = mapOf(STRING, STRING);
    YamlCodec<Map<String, Integer>> STRING_TO_INT = mapOf(STRING, INT);
    YamlCodec<Map<String, YamlMap>> STRING_TO_YAML_MAP = mapOf(STRING, YAML_MAP);
    YamlCodec<String> MULTI_LINE_STRING = of(v -> {
        if (v.isCollection()) return v.decode(STRING_LIST).mapValue(l -> Joiner.on("\n").join(l));
        return v.decode(STRING);
    }, YamlValue::wrap, SchemaTypes.STRINGS);
    YamlCodec<List<String>> STRINGS = STRING.listOrSingle();

    DataResult<T> decode(YamlValue value);

    default DataResult<T> decode(Object value) {
       return decode(YamlValue.wrap(value));
    }

    YamlValue encode(T value);

    @NotNull
    SchemaType schema();

    default <R> YamlField<R, T> fieldOf(String name, Function<R, T> getter) {
        return new YamlField<>(this, name, getter);
    }

    default <R> YamlField<R, T> fieldOf(String name, Function<R, T> getter, T def) {
        return new YamlField<>(this, name, getter, def);
    }

    default <R> YamlField<R, T> fieldOf(String name, Function<R, T> getter, BiConsumer<R, T> setter) {
        return new YamlField<>(this, name, getter, setter);
    }

    default <R> YamlField<R, T> fieldOf(String name, Function<R, T> getter, BiConsumer<R, T> setter, T def) {
        return new YamlField<>(this, name, getter, setter, def);
    }

    default YamlCodec<T> schema(Function<SchemaType, SchemaType> mutator) {
        final YamlCodec<T> subCodec = this;
        final SchemaType schemaType = mutator.apply(schema());
        return new YamlCodec<>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return subCodec.decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return subCodec.encode(value);
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }

    default YamlCodec<T> schema(SchemaType schemaType) {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return subCodec.decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return subCodec.encode(value);
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }

    default <E> YamlCodec<E> map(Function<T, E> map, Function<E, T> demap) {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<E>() {
            @Override
            public DataResult<E> decode(YamlValue value) {
                return subCodec.decode(value).flatMap(v -> DataResult.accept(() -> map.apply(v), DataResult::error));
            }

            @Override
            public YamlValue encode(E value) {
                return subCodec.encode(demap.apply(value));
            }

            @Override
            public @NotNull SchemaType schema() {
                return subCodec.schema();
            }
        };
    }

    default <E> YamlCodec<E> flatMap(Function<T, DataResult<E>> map, Function<E, T> demap) {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<E>() {
            @Override
            public DataResult<E> decode(YamlValue value) {
                return subCodec.decode(value).flatMap(map::apply);
            }

            @Override
            public YamlValue encode(E value) {
                return subCodec.encode(demap.apply(value));
            }

            @Override
            public @NotNull SchemaType schema() {
                return subCodec.schema();
            }
        };
    }

    default YamlCodec<List<T>> listOrSingle() {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<>() {
            final YamlCodec<List<T>> listCodec = subCodec.listOf();
            final SchemaType type = SchemaTypes.oneOf(subCodec.schema(), subCodec.schema().listOf());

            @Override
            public DataResult<List<T>> decode(YamlValue value) {
                if (value.isCollection()) return listCodec.decode(value);
                return subCodec.decode(value).flatMap(l -> DataResult.success(List.of(l)));
            }

            @Override
            public YamlValue encode(List<T> value) {
                if (value.size() == 1) return subCodec.encode(value.get(0));
                return listCodec.encode(value);
            }

            @Override
            public @NotNull SchemaType schema() {
                return type;
            }
        };
    }

    default YamlCodec<List<T>> listOf() {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<>() {
            final SchemaType type = subCodec.schema().listOf();

            @Override
            public DataResult<List<T>> decode(YamlValue value) {
                return value.asList(subCodec);
            }

            @Override
            public YamlValue encode(List<T> value) {
                return YamlValue.wrap(value.stream().map(subCodec::encode).collect(Collectors.toList()));
            }

            @Override
            public @NotNull SchemaType schema() {
                return type;
            }
        };
    }

    static <K, V> YamlCodec<Map<K, V>> mapOf(final YamlCodec<K> keyCodec, final YamlCodec<V> valueCodec) {
        return new YamlCodec<>() {
            final SchemaType schemaType = SchemaTypes.OBJECT.asBuilder().patternProperties(".*", valueCodec.schema()).build();

            @Override
            public DataResult<Map<K, V>> decode(YamlValue in) {
                return in.streamMap().flatMap(s -> {
                    Map<K, V> map = new LinkedHashMap<>();
                    StringBuilder error = new StringBuilder();
                    s.forEach(entry -> {
                        DataResult<K> key = keyCodec.decode(entry.getKey());
                        DataResult<V> value = valueCodec.decode(entry.getValue());
                        K k = key.result();
                        V v = value.result();
                        if (key.hasError() || value.hasError()) {
                            error.append("Errors in '").append(entry.getKey().getRaw()).append("':\n");
                        }
                        if (key.hasError()) {
                            error.append("  - ").append(key.error().replace("\n", "\n    ")).append("\n");
                        }
                        if (value.hasError()) {
                            error.append("  - ").append(value.error().replace("\n", "\n    ")).append("\n");
                        }
                        if (k != null && v != null) {
                            map.put(k, v);
                        }
                    });
                    if (error.isEmpty()) {
                        return DataResult.success(map);
                    } else {
                        error.setLength(error.length() - 1);
                        if (map.isEmpty()) return DataResult.error(error.toString());
                        return DataResult.error(error.toString()).partial(map);
                    }
                });
            }

            @Override
            public YamlValue encode(Map<K, V> value) {
                return YamlValue.wrap(value.entrySet().stream().collect(Collectors.toMap(
                                e -> keyCodec.encode(e.getKey()).getValue(),
                                e -> valueCodec.encode(e.getValue()).getValue(),
                                (v1, v2) -> v1,
                                LinkedHashMap::new
                        ))
                );
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }

    static <T> YamlCodec<T> of(final Function<YamlValue, DataResult<T>> decoder, final Function<T, YamlValue> encoder, SchemaType type) {
        return new YamlCodec<>() {

            @Override
            public DataResult<T> decode(YamlValue value) {
                return decoder.apply(value);
            }

            @Override
            public YamlValue encode(T value) {
                return encoder.apply(value);
            }

            @Override
            public @NotNull SchemaType schema() {
                return type;
            }
        };
    }

    static <T extends Enum<T>> YamlCodec<T> enumOf(Class<T> type) {
        final Map<String, T> values = new HashMap<>();
        for (T enumConstant : type.getEnumConstants()) {
            values.put(enumConstant.name().toLowerCase(), enumConstant);
        }
        SchemaType schemaType = JsonSchemaTypeBuilder.create().enumOf(values.keySet()).build();
        return new YamlCodec<>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return STRING.decode(value).flatMap(s -> {
                    T val = values.get(s);
                    if (val == null)
                        return DataResult.error("Unknown value {} for {}", new Object[]{s, type.getSimpleName()});
                    return DataResult.success(val);
                });
            }

            @Override
            public YamlValue encode(T value) {
                return YamlValue.wrap(value.name().toLowerCase());
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }

    class PrimitiveMapper<T> implements YamlCodec<T> {
        private final Class<T> type;
        private final Function<Object, T> decoder;
        private final SchemaType schemaType;

        public PrimitiveMapper(Class<T> type, Function<Object, T> decoder, SchemaType schemaType) {
            this.type = type;
            this.decoder = decoder;
            this.schemaType = schemaType;
        }

        @Override
        public DataResult<T> decode(YamlValue value) {
            Object o = value.getValue();
            if (type.isAssignableFrom(o.getClass())) {
                return DataResult.success(type.cast(o));
            }
            return DataResult.accept(() -> decoder.apply(o), t -> DataResult.error(t.getMessage()));
        }

        @Override
        public YamlValue encode(T value) {
            return YamlValue.wrap(value);
        }

        @Override
        public @NotNull SchemaType schema() {
            return schemaType;
        }
    }

    static YamlCodec<YamlValue> identity() {
        return YAML_VALUE;
    }
}
