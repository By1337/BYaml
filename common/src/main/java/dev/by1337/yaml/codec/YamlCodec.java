package dev.by1337.yaml.codec;

import com.google.common.base.Joiner;
import dev.by1337.yaml.YamlMap;
import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.k2v.Key2ValueCodec;
import dev.by1337.yaml.codec.k2v.LookupCodec;
import dev.by1337.yaml.codec.k2v.WildcardLookupCodec;
import dev.by1337.yaml.codec.list.ListCodec;
import dev.by1337.yaml.util.LazyLoad;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface YamlCodec<T> extends LegacyYamlCodec<T> {
    YamlCodec<YamlValue> YAML_VALUE = of(DataResult::success, Function.identity());
    YamlCodec<Object> OBJECT = of(v -> DataResult.success(v.getValue()), YamlValue::wrap);
    YamlCodec<Integer> INT = new PrimitiveMapper<>(Integer.class, o -> Integer.parseInt(o.toString()));
    YamlCodec<Byte> BYTE = new PrimitiveMapper<>(Byte.class, o -> Byte.parseByte(o.toString()));
    YamlCodec<Double> DOUBLE = new PrimitiveMapper<>(Double.class, o -> Double.parseDouble(o.toString()));
    YamlCodec<Float> FLOAT = new PrimitiveMapper<>(Float.class, o -> Float.parseFloat(o.toString()));
    YamlCodec<Long> LONG = new PrimitiveMapper<>(Long.class, o -> Long.parseLong(o.toString()));
    YamlCodec<Short> SHORT = new PrimitiveMapper<>(Short.class, o -> Short.parseShort(o.toString()));
    YamlCodec<Boolean> BOOL = new PrimitiveMapper<>(Boolean.class, o -> Boolean.parseBoolean(o.toString()));
    YamlCodec<String> STRING = new PrimitiveMapper<>(String.class, Object::toString);
    YamlCodec<Map<String, Object>> STRING_TO_OBJECT = mapOf(STRING, OBJECT);
    YamlCodec<YamlMap> YAML_MAP = of(YamlValue::asYamlMap, YamlMap::get);
    YamlCodec<List<YamlMap>> YAML_MAP_LIST = YAML_MAP.listOf();
    YamlCodec<List<String>> STRING_LIST = STRING.listOf();
    YamlCodec<List<Integer>> INT_LIST = INT.listOf();
    YamlCodec<Map<String, String>> STRING_TO_STRING = mapOf(STRING, STRING);
    YamlCodec<Map<String, Integer>> STRING_TO_INT = mapOf(STRING, INT);
    YamlCodec<Map<String, YamlMap>> STRING_TO_YAML_MAP = mapOf(STRING, YAML_MAP);
    YamlCodec<String> MULTI_LINE_STRING = of(v -> {
        if (v.isCollection()) return v.decode(STRING_LIST).mapValue(l -> Joiner.on("\n").join(l));
        return v.decode(STRING);
    }, YamlValue::wrap);
    YamlCodec<List<String>> STRINGS = STRING.listOf();

    DataResult<T> decode(YamlValue value);

    default DataResult<T> decode(Object value) {
        return decode(YamlValue.wrap(value));
    }

    YamlValue encode(T value);

    default <R> YamlField<R, T> withGetter(Function<R, T> getter) {
        return new YamlField<>(this, null, getter);
    }

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

    default <E extends T> YamlCodec<E> map(Function<T, E> map) {
        final YamlCodec<T> subCodec = this;
        return new YamlCodec<E>() {
            @Override
            public DataResult<E> decode(YamlValue value) {
                return subCodec.decode(value).flatMap(v -> DataResult.accept(() -> map.apply(v), DataResult::error));
            }

            @Override
            public YamlValue encode(E value) {
                return subCodec.encode(value);
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
        };
    }

    @Override
    default ListCodec<T> listOf() {
        return new ListCodec<>(this);
    }

    default YamlCodec<T> whenPrimitive(YamlCodec<T> primitive) {
        return dispatchByShape(primitive, this, this);
    }

    default YamlCodec<T> whenMap(YamlCodec<T> map) {
        return dispatchByShape(this, map, this);
    }

    default YamlCodec<T> preDecode(Function<YamlValue, YamlValue> fixer) {
        var subCodec = this;
        return new YamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return subCodec.decode(fixer.apply(value));
            }

            @Override
            public YamlValue encode(T value) {
                return subCodec.encode(value);
            }
        };
    }
    default YamlCodec<T> postDecode(Consumer<T> c){
        var subCodec = this;
        return new YamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return subCodec.decode(value).map(v -> {
                    c.accept(v);
                    return v;
                });
            }

            @Override
            public YamlValue encode(T value) {
                return subCodec.encode(value);
            }
        };
    }

    @SuppressWarnings("unchecked")
    default <R> YamlCodec<R> widen() {
        return (YamlCodec<R>) this;
    }

    static <T> YamlCodec<T> lazyLoad(Supplier<YamlCodec<T>> getter) {
        LazyLoad<YamlCodec<T>> get = new LazyLoad<>(getter);
        return new YamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return get.get().decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return get.get().encode(value);
            }
        };
    }

    static <T> YamlCodec<T> dispatchByShape(YamlCodec<T> primitive, YamlCodec<T> map) {
        return dispatchByShape(primitive, map, primitive);
    }

    static <T> YamlCodec<T> dispatchByShape(YamlCodec<T> primitive, YamlCodec<T> map, YamlCodec<T> encoder) {
        return new YamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.isMap() ? map.decode(value) : primitive.decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return encoder.encode(value);
            }
        };
    }

    static <T> YamlCodec<T> recursive(Function<YamlCodec<T>, YamlCodec<T>> wrapped) {
        return new YamlCodec<T>() {
            final LazyLoad<YamlCodec<T>> wrappedCodec = new LazyLoad<>(() -> wrapped.apply(this));

            @Override
            public DataResult<T> decode(YamlValue value) {
                return wrappedCodec.get().decode(value);
            }

            @Override
            public YamlValue encode(T value) {
                return wrappedCodec.get().encode(value);
            }
        };
    }

    static <T> YamlCodec<List<T>> wildcard(Map<String, T> map) {
        return new WildcardLookupCodec<>(map);
    }

    static <T> YamlCodec<T> lookup(Map<String, T> map) {
        return new LookupCodec<>(map);
    }

    static <K, V> YamlCodec<Map<K, V>> mapOf(final YamlCodec<K> keyCodec, final YamlCodec<V> valueCodec) {
        return new YamlCodec<>() {

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
        };
    }

    static <T> YamlCodec<T> of(final Function<YamlValue, DataResult<T>> decoder, final Function<T, YamlValue> encoder) {
        return new YamlCodec<>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return decoder.apply(value);
            }

            @Override
            public YamlValue encode(T value) {
                return encoder.apply(value);
            }
        };
    }

    @Deprecated
    static <T extends Enum<T>> YamlCodec<T> enumOf(Class<T> type) {
        return fromEnum(type);
    }

    static <T extends Enum<T>> Key2ValueCodec<T> fromEnum(Class<T> type) {
        final Map<String, T> values = new HashMap<>();
        for (T enumConstant : type.getEnumConstants()) {
            values.put(enumConstant.name().toLowerCase(), enumConstant);
        }
        return new Key2ValueCodec<>() {
            @Override
            public Map<String, T> asMap() {
                return values;
            }

            @Override
            public DataResult<T> decode(YamlValue value) {
                return STRING.decode(value).flatMap(s -> {
                    T val = values.get(s.toLowerCase(Locale.ROOT));
                    if (val == null)
                        return DataResult.error("Unknown value {} for {}", new Object[]{s, type.getSimpleName()});
                    return DataResult.success(val);
                });
            }

            @Override
            public YamlValue encode(T value) {
                return YamlValue.wrap(value.name().toLowerCase());
            }
        };
    }

    class PrimitiveMapper<T> implements YamlCodec<T> {
        private final Class<T> type;
        private final Function<Object, T> decoder;

        public PrimitiveMapper(Class<T> type, Function<Object, T> decoder) {
            this.type = type;
            this.decoder = decoder;
        }

        @Override
        public DataResult<T> decode(YamlValue value) {
            if (value.isNull()) return DataResult.error("value is null");
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
    }

    static YamlCodec<YamlValue> identity() {
        return YAML_VALUE;
    }
}
