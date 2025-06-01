package dev.by1337.yaml;

import dev.by1337.yaml.codec.CodecFinder;
import dev.by1337.yaml.codec.YamlCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class YamlValue {
    public static final YamlValue EMPTY = new YamlValue(null);

    private final @Nullable Object value;

    private YamlValue(@Nullable Object value) {
        if (value instanceof YamlValue) throw new IllegalStateException(value + " уже YamlValue");
        this.value = value;
    }

    public static YamlValue wrap(@Nullable final Object value) {
        if (value == null) return EMPTY;
        if (value instanceof YamlMap map) return new YamlValue(map.getRaw());
        return new YamlValue(unpack(value));
    }

    static YamlValue wrapUnsafe(@Nullable final Object value) {
        if (value == null) return EMPTY;
        if (value instanceof YamlMap map) return new YamlValue(map.getRaw());
        return new YamlValue(value);
    }

    public <T> T decode(YamlCodec<T> codec) {
        return codec.decode(this);
    }

    public <T> T decode(YamlCodec<T> codec, T def) {
        if (isNull()) return def;
        return codec.decode(this);
    }

    public <T> T getAs(Class<T> type) {
        var codec = CodecFinder.INSTANCE.getCodec(type);
        if (codec == null) throw new IllegalStateException("No codec found for type: " + type.getName());
        return codec.decode(this);
    }

    @SuppressWarnings("unchecked")
    public YamlMap getAsYamlMap() {
        reqMap();
        if (value instanceof LinkedHashMap<?, ?>)
            return new YamlMap((LinkedHashMap<String, Object>) value);
        Map<String, Object> map = (Map<String, Object>) value;
        return new YamlMap(new LinkedHashMap<>(map));
    }

    public @Nullable Object getValue() {
        return value;
    }

    public Stream<YamlValue> stream(Stream<YamlValue> def) {
        if (!isCollection()) return def;
        return stream();
    }

    public Stream<YamlValue> stream() {
        reqCollection();
        return ((Collection<?>) value).stream().map(YamlValue::wrapUnsafe);
    }

    public Stream<Map.Entry<YamlValue, YamlValue>> streamMap(Stream<Map.Entry<YamlValue, YamlValue>> def) {
        if (!isMap()) return def;
        return streamMap();
    }

    @SuppressWarnings("unchecked")
    public Stream<Map.Entry<YamlValue, YamlValue>> streamMap() {
        reqMap();
        return ((Map<Object, Object>) value).entrySet().stream().map(e -> new MapEntry<>(wrapUnsafe(e.getKey()), wrapUnsafe(e.getValue())));
    }

    public void reqCollection() {
        if (!isCollection()) {
            throw new IllegalStateException("Expected a List, but found " + describeType() + ".");
        }
    }

    public void reqMap() {
        if (!isMap()) {
            throw new IllegalStateException("Expected a Map, but found " + describeType() + ".");
        }
    }

    public void reqNonNull() {
        Objects.requireNonNull(value);
    }

    public <T> List<T> getAsList(Class<T> type, List<T> def) {
        if (!isCollection()) return def;
        return getAsList(type);
    }

    public <T> List<T> getAsList(Class<T> type) {
        var codec = CodecFinder.INSTANCE.getCodec(type);
        if (codec == null) throw new IllegalStateException("No codec found for type: " + type.getName());
        return getAsList(codec);
    }

    public <T> List<T> getAsList(YamlCodec<T> codec, List<T> def) {
        if (!isCollection()) return def;
        return getAsList(codec);
    }

    public <T> List<T> getAsList(YamlCodec<T> codec) {
        reqCollection();
        return ((Collection<?>) value).stream().map(v -> codec.decode(wrapUnsafe(v))).toList();
    }

    public <T> List<T> getAsList(Function<YamlValue, T> mapper, List<T> def) {
        if (isNull()) return def;
        return getAsList(mapper);
    }

    public <T> List<T> getAsList(Function<YamlValue, T> mapper) {
        return stream().map(mapper).toList();
    }

    public List<String> getAsStringList() {
        return getAsList(YamlCodec.STRING);
    }

    public <K, V> Map<K, V> getAsMap(Class<K> keyType, Class<V> valueType, Map<K, V> def) {
        if (isNull()) return def;
        return getAsMap(keyType, valueType);
    }

    public <K, V> Map<K, V> getAsMap(Class<K> keyType, Class<V> valueType) {
        var keyCodec = CodecFinder.INSTANCE.getCodec(keyType);
        if (keyCodec == null) throw new IllegalStateException("No codec found for type: " + keyType.getName());
        var valueCodec = CodecFinder.INSTANCE.getCodec(valueType);
        if (valueCodec == null) throw new IllegalStateException("No codec found for type: " + valueType.getName());
        return getAsMap(keyCodec, valueCodec);
    }

    public <K, V> Map<K, V> getAsMap(YamlCodec<K> keyCodec, YamlCodec<V> valueCodec, Map<K, V> def) {
        if (isNull()) return def;
        return getAsMap(keyCodec, valueCodec);
    }

    public <K, V> Map<K, V> getAsMap(YamlCodec<K> keyCodec, YamlCodec<V> valueCodec) {
        return YamlCodec.mapOf(keyCodec, valueCodec).decode(this);
    }

    public <K, V> Map<K, V> getAsMap(Class<K> keyType, Function<YamlValue, V> valueDecoder, Map<K, V> def) {
        if (isNull()) return def;
        return getAsMap(keyType, valueDecoder);
    }

    public <K, V> Map<K, V> getAsMap(Class<K> keyType, Function<YamlValue, V> valueDecoder) {
        var keyCodec = CodecFinder.INSTANCE.getCodec(keyType);
        if (keyCodec == null) throw new IllegalStateException("No codec found for type: " + keyType.getName());
        return getAsMap(keyCodec, valueDecoder);
    }

    public <K, V> Map<K, V> getAsMap(YamlCodec<K> keyCodec, Function<YamlValue, V> valueDecoder, Map<K, V> def) {
        if (isNull()) return def;
        return getAsMap(keyCodec, valueDecoder);
    }

    public <K, V> Map<K, V> getAsMap(YamlCodec<K> keyCodec, Function<YamlValue, V> valueDecoder) {
        return getAsMap(keyCodec::decode, valueDecoder);
    }

    public <K, V> Map<K, V> getAsMap(Function<YamlValue, K> keyDecoder, Function<YamlValue, V> valueDecoder, Map<K, V> def) {
        if (isNull()) return def;
        return getAsMap(keyDecoder, valueDecoder);
    }

    public <K, V> Map<K, V> getAsMap(Function<YamlValue, K> keyDecoder, Function<YamlValue, V> valueDecoder) {
        Map<K, V> result = new LinkedHashMap<>();
        streamMap().forEach(entry -> result.put(keyDecoder.apply(entry.getValue()), valueDecoder.apply(entry.getValue())));
        return result;
    }


    public Integer getAsInt() {
        reqNonNull();
        return YamlCodec.INT.decode(this);
    }

    public Integer getAsInt(int def) {
        if (isNull()) return def;
        return YamlCodec.INT.decode(this);
    }

    public Byte getAsByte() {
        reqNonNull();
        return YamlCodec.BYTE.decode(this);
    }

    public Byte getAsByte(byte def) {
        if (isNull()) return def;
        return YamlCodec.BYTE.decode(this);
    }

    public Double getAsDouble() {
        reqNonNull();
        return YamlCodec.DOUBLE.decode(this);
    }

    public Double getAsDouble(double def) {
        if (isNull()) return def;
        return YamlCodec.DOUBLE.decode(this);
    }

    public Long getAsLong() {
        reqNonNull();
        return YamlCodec.LONG.decode(this);
    }

    public Long getAsLong(long def) {
        if (isNull()) return def;
        return YamlCodec.LONG.decode(this);
    }

    public Float getAsFloat() {
        reqNonNull();
        return YamlCodec.FLOAT.decode(this);
    }

    public Float getAsFloat(float def) {
        if (isNull()) return def;
        return YamlCodec.FLOAT.decode(this);
    }


    public Short getAsShort() {
        reqNonNull();
        return YamlCodec.SHORT.decode(this);
    }

    public Short getAsShort(short def) {
        if (isNull()) return def;
        return YamlCodec.SHORT.decode(this);
    }

    public Boolean getAsBoolean() {
        reqNonNull();
        return YamlCodec.BOOL.decode(this);
    }

    public Boolean getAsBoolean(boolean def) {
        if (isNull()) return def;
        return YamlCodec.BOOL.decode(this);
    }

    public String getAsString() {
        reqNonNull();
        return YamlCodec.STRING.decode(this);
    }

    public String getAsString(String def) {
        if (isNull()) return def;
        return YamlCodec.STRING.decode(this);
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

    public Object unpack() {
        return unpack(value);
    }

    @Contract("null -> null")
    public static Object unpack(@Nullable Object val) {
        Object o = val;
        if (o instanceof YamlMap map) return map.getRaw();
        if (o instanceof YamlValue) {
            o = ((YamlValue) o).unpack();
        }
        if (o instanceof Map<?, ?> m) {
            return unpackMap(m);
        }
        if (o instanceof Collection<?> list) {
            return unpackCollection(list);
        }
        return o;
    }

    public static Map<?, ?> unpackMap(Map<?, ?> map) {
        Map<Object, Object> result = new LinkedHashMap<>();
        for (Object o : map.keySet()) {
            result.put(unpack(o), unpack(map.get(o)));
        }
        return result;
    }

    public static Collection<?> unpackCollection(Collection<?> list) {
        List<Object> result = new ArrayList<>(list.size());
        for (Object o : list) {
            result.add(unpack(o));
        }
        return result;
    }

    private String describeType() {
        if (value == null) return "null";
        if (value instanceof Map) return "Map";
        if (value instanceof Collection) return "List";
        return value.getClass().getSimpleName();
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

