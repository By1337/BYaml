package dev.by1337.yaml;

import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.YamlCodec;
import dev.by1337.yaml.codec.YamlHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public final class YamlValue implements YamlHolder {
    public static final YamlValue EMPTY = new YamlValue(null);

    private final @Nullable Object value;

    private YamlValue(@Nullable Object value) {
        if (value instanceof YamlValue) throw new IllegalStateException(value + " уже YamlValue");
        this.value = value;
    }

    public static YamlValue wrap(@Nullable final Object value) {
        if (value == null) return EMPTY;
        if (value instanceof YamlHolder holder) return new YamlValue(holder.getRaw());
        return new YamlValue(value);
    }

    public <T> DataResult<T> decode(YamlCodec<T> codec) {
        return codec.decode(this);
    }

    public <T> DataResult<T> decode(YamlCodec<T> codec, T def) {
        if (isNull()) return DataResult.success(def);
        return codec.decode(this);
    }

    @SuppressWarnings("unchecked")
    public DataResult<YamlMap> asYamlMap() {
        if (value instanceof LinkedHashMap<?, ?>)
            return DataResult.success(new YamlMap((LinkedHashMap<String, Object>) value));
        if (value instanceof Map<?, ?> map)
            return DataResult.success(new YamlMap(new LinkedHashMap<>((Map<String, Object>) map)));
        return DataResult.error("Expected a Map, but found " + describeType() + ".");
    }

    public @Nullable Object getValue() {
        return value;
    }

    public DataResult<Collection<?>> asCollection() {
        if (value instanceof Collection<?>) {
            return DataResult.success((Collection<?>) value);
        }
        return DataResult.error("Expected a Collection, but found " + describeType() + ".");
    }

    public Stream<YamlValue> stream(Stream<YamlValue> def) {
        return stream().orDefault(def);
    }

    public DataResult<Stream<YamlValue>> stream() {
        return asCollection().flatMap(c -> DataResult.success(c.stream().map(YamlValue::wrap)));
    }

    public Stream<Map.Entry<YamlValue, YamlValue>> streamMap(Stream<Map.Entry<YamlValue, YamlValue>> def) {
        return streamMap().orDefault(def);
    }

    public DataResult<Map<Object, Object>> asMap() {
        if (value instanceof Map<?, ?> map) {
            return DataResult.success((Map<Object, Object>) map);
        }
        return DataResult.error("Expected a Map, but found " + describeType() + ".");
    }


    public DataResult<Stream<Map.Entry<YamlValue, YamlValue>>> streamMap() {
        return asMap().flatMap(m -> DataResult.success(m.entrySet().stream().map(e -> new MapEntry<>(wrap(e.getKey()), wrap(e.getValue())))));
    }

    public <T> List<T> asList(YamlCodec<T> codec, List<T> def) {
        return asList(codec).orDefault(def);
    }

    public <T> DataResult<List<T>> asList(YamlCodec<T> codec) {
        return stream().flatMap(s -> {
            StringBuilder error = new StringBuilder();
            var res = s.map(v -> {
                DataResult<T> result = codec.decode(v);
                if (result.hasError()) {
                    error.append(result.error()).append("\n");
                }
                return result.result();
            }).filter(Objects::nonNull).toList();
            if (error.isEmpty()) {
                return DataResult.success(res);
            } else {
                error.setLength(error.length() - 1);
                if (res.isEmpty()) return DataResult.error(error.toString());
                return DataResult.error(error.toString()).partial(res);
            }
        });
    }

    public <T> List<T> asList(Function<YamlValue, T> mapper, List<T> def) {
        return asList(mapper).orDefault(def);
    }

    public <T> DataResult<List<T>> asList(Function<YamlValue, T> mapper) {
        return stream().flatMap(s -> DataResult.success(s.map(mapper).toList()));
    }

    public DataResult<List<String>> getAsStringList() {
        return asList(YamlCodec.STRING);
    }

    public <K, V> Map<K, V> asMap(YamlCodec<K> keyCodec, YamlCodec<V> valueCodec, Map<K, V> def) {
        return asMap(keyCodec, valueCodec).orDefault(def);
    }

    public <K, V> DataResult<Map<K, V>> asMap(YamlCodec<K> keyCodec, YamlCodec<V> valueCodec) {
        return YamlCodec.mapOf(keyCodec, valueCodec).decode(this);
    }


    public <K, V> Map<K, V> asMap(YamlCodec<K> keyCodec, Function<YamlValue, V> valueDecoder, Map<K, V> def) {
        return asMap(keyCodec, valueDecoder).orDefault(def);
    }

    public <K, V> DataResult<Map<K, V>> asMap(YamlCodec<K> keyCodec, Function<YamlValue, V> valueDecoder) {
        return asMap(v -> keyCodec.decode(v).getOrThrow(), valueDecoder);
    }

    public <K, V> Map<K, V> asMap(Function<YamlValue, K> keyDecoder, Function<YamlValue, V> valueDecoder, Map<K, V> def) {
        return asMap(keyDecoder, valueDecoder).orDefault(def);
    }

    public <K, V> DataResult<Map<K, V>> asMap(Function<YamlValue, K> keyDecoder, Function<YamlValue, V> valueDecoder) {
        return streamMap().flatMap(s -> {
            Map<K, V> result = new LinkedHashMap<>();
            StringBuilder error = new StringBuilder();
            s.forEach(entry -> {
                DataResult<K> key = DataResult.accept(() -> keyDecoder.apply(entry.getKey()), DataResult::error);
                DataResult<V> value = DataResult.accept(() -> valueDecoder.apply(entry.getValue()), DataResult::error);
                K k = key.result();
                V v = value.result();
                if (key.hasError()){
                    error.append("Invalid key: ").append(key.error()).append("\n");
                }
                if (value.hasError()){
                    error.append("Invalid value: ").append(value.error()).append("\n");
                }
                if (k != null && v != null) {
                    result.put(k, v);
                }
            });
            if (error.isEmpty()) {
                return DataResult.success(result);
            } else {
                error.setLength(error.length() - 1);
                if (result.isEmpty()) return DataResult.error(error.toString());
                return DataResult.error(error.toString()).partial(result);
            }
        });
    }

    public boolean isMap() {
        return value instanceof Map<?, ?>;
    }

    public boolean isCollection() {
        return value instanceof Collection<?>;
    }

    public boolean isPrimitive() {
        return !isMap() && !isCollection();
    }

    public boolean isNull() {
        return value == null;
    }

    private String describeType() {
        if (value == null) return "null";
        if (value instanceof Map) return "Map";
        if (value instanceof Collection) return "List";
        return value.getClass().getSimpleName();
    }

    @Override
    public Object getRaw() {
        return value;
    }

    private static class MapEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        public MapEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }

}

