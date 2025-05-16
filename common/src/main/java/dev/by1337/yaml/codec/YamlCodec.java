package dev.by1337.yaml.codec;

import com.google.common.base.Enums;
import com.google.common.base.Joiner;
import dev.by1337.yaml.YamlMap;
import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface YamlCodec<T> {
    YamlCodec<YamlValue> YAML_VALUE = of(Function.identity(), Function.identity(), SchemaTypes.ANY);
    YamlCodec<Object> OBJECT = of(YamlValue::getValue, YamlValue::wrap, SchemaTypes.OBJECT);
    YamlCodec<Integer> INT = new PrimitiveMapper<>(Integer.class, o -> Integer.parseInt(o.toString()), SchemaTypes.INT);
    YamlCodec<Byte> BYTE = new PrimitiveMapper<>(Byte.class, o -> Byte.parseByte(o.toString()), SchemaTypes.INT);
    YamlCodec<Double> DOUBLE = new PrimitiveMapper<>(Double.class, o -> Double.parseDouble(o.toString()), SchemaTypes.NUMBER);
    YamlCodec<Float> FLOAT = new PrimitiveMapper<>(Float.class, o -> Float.parseFloat(o.toString()), SchemaTypes.NUMBER);
    YamlCodec<Long> LONG = new PrimitiveMapper<>(Long.class, o -> Long.parseLong(o.toString()), SchemaTypes.NUMBER);
    YamlCodec<Short> SHORT = new PrimitiveMapper<>(Short.class, o -> Short.parseShort(o.toString()), SchemaTypes.INT);
    YamlCodec<Boolean> BOOL = new PrimitiveMapper<>(Boolean.class, o -> Boolean.parseBoolean(o.toString()), SchemaTypes.BOOL);
    YamlCodec<String> STRING = new PrimitiveMapper<>(String.class, Object::toString, SchemaTypes.STRING);
    YamlCodec<Map<String, Object>> STRING_TO_OBJECT_MAP = mapOf(STRING, OBJECT);
    YamlCodec<YamlMap> YAML_MAP = of(YamlValue::getAsYamlMap, YamlValue::wrap, SchemaTypes.OBJECT);
    YamlCodec<List<YamlMap>> YAML_MAP_LIST = YAML_MAP.listOf();
    YamlCodec<List<String>> STRING_LIST = STRING.listOf();
    YamlCodec<List<Integer>> INT_LIST = INT.listOf();
    YamlCodec<Map<String, String>> STRING_TO_STRING_MAP = mapOf(STRING, STRING);
    YamlCodec<Map<String, Integer>> STRING_TO_INT_MAP = mapOf(STRING, INT);
    YamlCodec<Map<String, YamlMap>> STRING_TO_YAML_MAP_MAP = mapOf(STRING, YAML_MAP);
    YamlCodec<String> MULTI_LINE_STRING = of(v -> {
        if (v.isCollection()) return Joiner.on("\n").join(v.decode(STRING_LIST));
        return v.decode(STRING);
    }, YamlValue::wrap, SchemaTypes.STRINGS);
    YamlCodec<List<String>> STRINGS = STRING.listOrSingle();

    T decode(YamlValue value);

    YamlValue encode(T value);

    @NotNull
    SchemaType getSchemaType();

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

    default YamlCodec<T> schema(Function<SchemaType, SchemaType> mutator){
        final YamlCodec<T> subCodec = this;
        final SchemaType schemaType = mutator.apply(getSchemaType());
        return new YamlCodec<>() {
            @Override
            public T decode(YamlValue value) {
                return subCodec.decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return subCodec.encode(value);
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return schemaType;
            }
        };
    }

   default YamlCodec<T> schema(SchemaType schemaType){
       final YamlCodec<T> subCodec = this;
        return new YamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                return subCodec.decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return subCodec.encode(value);
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return schemaType;
            }
        };
    }

    default <E> YamlCodec<E> map(Function<T, E> map, Function<E, T> demap) {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<E>() {
            @Override
            public E decode(YamlValue value) {
                return map.apply(subCodec.decode(value));
            }

            @Override
            public YamlValue encode(E value) {
                return subCodec.encode(demap.apply(value));
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return subCodec.getSchemaType();
            }
        };
    }

    default YamlCodec<List<T>> listOrSingle() {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<>() {
            final YamlCodec<List<T>> listCodec = subCodec.listOf();
            final SchemaType type = SchemaTypes.oneOf(subCodec.getSchemaType(), subCodec.getSchemaType().listOf());
            @Override
            public List<T> decode(YamlValue value) {
                if (value.isCollection()) return listCodec.decode(value);
                var list = new ArrayList<T>();
                list.add(subCodec.decode(value));
                return list;
            }

            @Override
            public YamlValue encode(List<T> value) {
                if (value.size() == 1) return subCodec.encode(value.get(0));
                return listCodec.encode(value);
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return type;
            }
        };
    }

    default YamlCodec<List<T>> listOf() {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<>() {
            final SchemaType type = subCodec.getSchemaType().listOf();
            @Override
            public List<T> decode(YamlValue value) {
                return value.stream().map(subCodec::decode).collect(Collectors.toList());
            }

            @Override
            public YamlValue encode(List<T> value) {
                return YamlValue.wrap(value.stream().map(subCodec::encode).collect(Collectors.toList()));
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return type;
            }
        };
    }

    static <K, V> YamlCodec<Map<K, V>> mapOf(final YamlCodec<K> keyCodec, final YamlCodec<V> valueCodec) {
        return new YamlCodec<>() {
            final SchemaType schemaType = SchemaTypes.OBJECT.asBuilder().patternProperties(".*", valueCodec.getSchemaType()).build();
            @Override
            public Map<K, V> decode(YamlValue value) {
                return value.streamMap().collect(Collectors.toMap(
                        e -> keyCodec.decode(e.getKey()),
                        e -> valueCodec.decode(e.getValue()),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
            }

            @Override
            public YamlValue encode(Map<K, V> value) {
                return YamlValue.wrap(value.entrySet().stream().collect(Collectors.toMap(
                                e -> keyCodec.encode(e.getKey()),
                                e -> valueCodec.encode(e.getValue()),
                                (v1, v2) -> v1,
                                LinkedHashMap::new
                        ))
                );
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return schemaType;
            }
        };
    }

    static <T> YamlCodec<T> of(final Function<YamlValue, T> decoder, final Function<T, YamlValue> encoder, SchemaType type) {
        return new YamlCodec<T>() {

            @Override
            public T decode(YamlValue value) {
                return decoder.apply(value);
            }

            @Override
            public YamlValue encode(T value) {
                return encoder.apply(value);
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
                return type;
            }
        };
    }

    static <T extends Enum<T>> YamlCodec<T> enumOf(Class<T> type) {
        List<String> values = new ArrayList<>();
        for (T enumConstant : type.getEnumConstants()) {
            values.add(enumConstant.name().toLowerCase());
        }
        SchemaType schemaType = JsonSchemaTypeBuilder.create().enumOf(values).build();
        return new YamlCodec<>() {
            @Override
            public T decode(YamlValue value) {
                String data = STRING.decode(value);
                try {
                    return Enum.valueOf(type, data);
                } catch (IllegalArgumentException ignored) {
                    return Enum.valueOf(type, data.toUpperCase());
                }
            }

            @Override
            public YamlValue encode(T value) {
                return YamlValue.wrap(value.name());
            }

            @Override
            public @NotNull SchemaType getSchemaType() {
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
        public T decode(YamlValue value) {
            Object o = value.getValue();
            if (type.isAssignableFrom(o.getClass())) {
                return type.cast(o);
            }
            return decoder.apply(o);
        }

        @Override
        public YamlValue encode(T value) {
            return YamlValue.wrap(value);
        }

        @Override
        public @NotNull SchemaType getSchemaType() {
            return schemaType;
        }
    }

    static YamlCodec<YamlValue> identity() {
        return YAML_VALUE;
    }
}
