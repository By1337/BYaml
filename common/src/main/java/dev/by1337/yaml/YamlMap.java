package dev.by1337.yaml;

import dev.by1337.yaml.codec.CodecFinder;
import dev.by1337.yaml.codec.YamlCodec;
import dev.by1337.yaml.util.YamlReader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class YamlMap {
    @ApiStatus.Internal
    private static YamlReader YAML_READER;

    private final LinkedHashMap<String, Object> map;

    public YamlMap() {
        map = new LinkedHashMap<>();
    }

    public YamlMap(LinkedHashMap<String, Object> map) {
        this.map = map;
    }

    public static YamlMap loadFromString(String yaml) {
        return YAML_READER.read(yaml);
    }

    public static YamlMap load(File file) {
        try {
            return load(new StringReader(Files.readString(file.toPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static YamlMap load(Reader reader) {
        final BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();

        try (input) {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            return loadFromString(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static YamlMap load(InputStream inputStream) {
        return load(new InputStreamReader(inputStream));
    }

    public YamlValue get() {
        return YamlValue.wrapUnsafe(this);
    }

    @NotNull
    public  YamlValue get(@NotNull String path, @Nullable Object def) {
       return YamlValue.wrapUnsafe(getRaw(path, def));
    }

    @NotNull
    public YamlValue get(@NotNull String path) {
        return YamlValue.wrapUnsafe(getRaw(path));
    }

    @Nullable
    @Contract("_, !null -> !null")
    public Object getRaw(@NotNull String path, @Nullable Object def) {
        var v =  getRaw(path);
        return v == null ? def : v;
    }

    @Nullable
    public Object getRaw(@NotNull String path) {
        String[] path0 = path.split("\\.");

        Object last = null;
        for (String s : path0) {
            if (last == null) {
                Object o = map.get(s);
                if (o == null) return null;
                last = o;
            } else if (last instanceof Map<?, ?> sub) {
                Object o = sub.get(s);
                if (o == null) return null;
                last = o;
            } else {
                throw new IllegalStateException(
                        "Expected Map<String, Object> for path traversal, but found primitive: " +
                                last.getClass().getName() + " at segment '" + s + "'"
                );
            }
        }
        return last;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T> Object serialize(@Nullable T obj) {
        if (obj == null) return null;
        if (obj instanceof YamlMap) return ((YamlMap) obj).map;
        Object o = YamlValue.unpack(obj);

        YamlCodec<T> codec = (YamlCodec<T>) CodecFinder.INSTANCE.getCodec(o.getClass());
        if (codec == null) return o;
        return codec.encode((T) o).unpack();
    }

    public <T> void set(@NotNull String path, @Nullable T o, YamlCodec<T> codec) {
        if (o == null) setRaw(path, null);
        else set(path, codec.encode(o));
    }


    public void set(@NotNull String path, @Nullable Object o) {
        setRaw(path, serialize(o));
    }

    @SuppressWarnings("unchecked")
    public void setRaw(@NotNull String path, @Nullable Object obj) {
        String[] pathParts = path.split("\\.");
        Map<String, Object> currentMap = map;

        for (int i = 0; i < pathParts.length; i++) {
            String key = pathParts[i];

            if (i == pathParts.length - 1) {
                if (obj == null) {
                    currentMap.remove(key);
                } else {
                    currentMap.put(key, obj);
                }
            } else {
                Object value = currentMap.get(key);
                if (value instanceof Map) {
                    currentMap = (Map<String, Object>) value;
                } else if (value == null) {
                    if (obj == null) return;
                    Map<String, Object> newMap = new LinkedHashMap<>();
                    currentMap.put(key, newMap);
                    currentMap = newMap;
                } else {
                    String currentPath = String.join(".", Arrays.copyOf(pathParts, i + 1));
                    throw new IllegalStateException("Cannot traverse into '" + currentPath + "': expected Map but found primitive " + value.getClass().getSimpleName());
                }
            }
        }
    }

    public boolean has(String key) {
        return getRaw(key) != null;
    }

    public LinkedHashMap<String, Object> getRaw() {
        return map;
    }

    public String saveToString() {
        return YAML_READER.saveToString(map);
    }

    @Override
    public String toString() {
        return "YamlMap{" +
                "map=" + map +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YamlMap yamlMap = (YamlMap) o;
        return Objects.equals(map, yamlMap.map);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map);
    }

    @ApiStatus.Internal
    public static void setYamlReader(@NotNull YamlReader instance) {
        Objects.requireNonNull(instance);
        if (YamlMap.YAML_READER != null) throw new UnsupportedOperationException("Yaml reader already set");
        YamlMap.YAML_READER = instance;
    }

    public static YamlReader getYamlReader() {
        return YAML_READER;
    }

    static {
        try {
            Class.forName("dev.by1337.yaml.BukkitYamlReader", true, YamlReader.class.getClassLoader());
        } catch (ClassNotFoundException ignored) {
        }
        //Objects.requireNonNull(YAML_READER, "Yaml reader not set");
    }
}
