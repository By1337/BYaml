package dev.by1337.yaml.codec;

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
import java.util.function.Supplier;

public class PipelineYamlCodecBuilder<T> {
    private final Supplier<T> creator;
    private final List<YamlField<T, ?>> fields = new ArrayList<>();

    public PipelineYamlCodecBuilder(Supplier<T> creator) {
        this.creator = creator;
    }

    public static <T> PipelineYamlCodecBuilder<T> of(Supplier<T> creator) {
        return new PipelineYamlCodecBuilder<T>(creator);
    }

    public <R> PipelineYamlCodecBuilder<T> field(YamlCodec<R> codec, String name, Function<T, R> getter, BiConsumer<T, R> setter) {
        return field(codec, name, getter, setter, null);
    }

    public <R> PipelineYamlCodecBuilder<T> field(YamlCodec<R> codec, String name, Function<T, R> getter, BiConsumer<T, R> setter, R def) {
        fields.add(new YamlField<>(codec, name, getter, setter, def));
        return this;
    }

    public <R> PipelineYamlCodecBuilder<T> listOf(YamlCodec<R> codec, String name, Function<T, List<R>> getter, BiConsumer<T, List<R>> setter) {
        return listOf(codec, name, getter, setter, null);
    }

    public <R> PipelineYamlCodecBuilder<T> listOf(YamlCodec<R> codec, String name, Function<T, List<R>> getter, BiConsumer<T, List<R>> setter, List<R> def) {
        fields.add(new YamlField<>(codec.listOrSingle(), name, getter, setter, def));
        return this;
    }

    public <R, K> PipelineYamlCodecBuilder<T> mapOf(YamlCodec<K> keyCodec, YamlCodec<R> valueCodec, String name, Function<T, Map<K, R>> getter, BiConsumer<T, Map<K, R>> setter) {
        return mapOf(keyCodec, valueCodec, name, getter, setter, null);
    }

    public <R, K> PipelineYamlCodecBuilder<T> mapOf(YamlCodec<K> keyCodec, YamlCodec<R> valueCodec, String name, Function<T, Map<K, R>> getter, BiConsumer<T, Map<K, R>> setter, Map<K, R> def) {
        fields.add(new YamlField<>(YamlCodec.mapOf(keyCodec, valueCodec), name, getter, setter, def));
        return this;
    }

    public <R extends Enum<R>> PipelineYamlCodecBuilder<T> enumOf(Class<R> type, String name, Function<T, R> getter, BiConsumer<T, R> setter) {
        return enumOf(type, name, getter, setter, null);
    }

    public <R extends Enum<R>> PipelineYamlCodecBuilder<T> enumOf(Class<R> type, String name, Function<T, R> getter, BiConsumer<T, R> setter, R def) {
        fields.add(new YamlField<>(YamlCodec.enumOf(type), name, getter, setter, def));
        return this;
    }

    public PipelineYamlCodecBuilder<T> integer(String name, Function<T, Integer> getter, BiConsumer<T, Integer> setter) {
        return integer(name, getter, setter, null);
    }

    public PipelineYamlCodecBuilder<T> integer(String name, Function<T, Integer> getter, BiConsumer<T, Integer> setter, Integer def) {
        fields.add(new YamlField<>(YamlCodec.INT, name, getter, setter, def));
        return this;
    }

    public PipelineYamlCodecBuilder<T> string(String name, Function<T, String> getter, BiConsumer<T, String> setter) {
        return string(name, getter, setter, null);
    }

    public PipelineYamlCodecBuilder<T> string(String name, Function<T, String> getter, BiConsumer<T, String> setter, String def) {
        fields.add(new YamlField<>(YamlCodec.STRING, name, getter, setter, def));
        return this;
    }

    public PipelineYamlCodecBuilder<T> bool(String name, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        return bool(name, getter, setter, null);
    }

    public PipelineYamlCodecBuilder<T> bool(String name, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, Boolean def) {
        fields.add(new YamlField<>(YamlCodec.BOOL, name, getter, setter, def));
        return this;
    }

    public PipelineYamlCodecBuilder<T> strings(String name, Function<T, List<String>> getter, BiConsumer<T, List<String>> setter) {
        return strings(name, getter, setter, null);
    }

    public PipelineYamlCodecBuilder<T> strings(String name, Function<T, List<String>> getter, BiConsumer<T, List<String>> setter, List<String> def) {
        fields.add(new YamlField<>(YamlCodec.STRINGS, name, getter, setter, def));
        return this;
    }

    public PipelineYamlCodecBuilder<T> multiLine(String name, Function<T, String> getter, BiConsumer<T, String> setter) {
        return multiLine(name, getter, setter, null);
    }

    public PipelineYamlCodecBuilder<T> multiLine(String name, Function<T, String> getter, BiConsumer<T, String> setter, String def) {
        fields.add(new YamlField<>(YamlCodec.MULTI_LINE_STRING, name, getter, setter, def));
        return this;
    }

    public YamlCodec<T> build() {
        JsonSchemaTypeBuilder builder = new JsonSchemaTypeBuilder();
        builder.type(SchemaTypes.Type.OBJECT);
        for (YamlField<T, ?> field : fields) {
            if (field.setter == null) {
                throw new NullPointerException("Setter for field " + field.name + " is null");
            }
            builder.properties(field.name, field.codec.schema());
        }
        builder.additionalProperties(false);
        return new YamlCodec<T>() {
            final SchemaType schemaType = builder.build();

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public T decode(YamlValue value) {
                YamlMap map = value.getAsYamlMap();
                T v = creator.get();
                for (@NotNull YamlField field : fields) {
                    var val = map.get(field.name);
                    if (!val.isNull()) {
                        field.setter.accept(v, field.codec.decode(val));
                    } else if (field.defaultValue != null) {
                        field.setter.accept(v, field.defaultValue);
                    }
                }
                return v;
            }

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public YamlValue encode(T value) {
                Map<String, YamlValue> map = new LinkedHashMap<>();
                for (YamlField field : fields) {
                    Object o = field.getter.apply(value);
                    if (o == null) {
                        o = field.defaultValue;
                    }
                    if (o != null) {
                        map.put(field.name, field.codec.encode(o));
                    }
                }
                return YamlValue.wrap(map);
            }

            @Override
            public @NotNull SchemaType schema() {
                return schemaType;
            }
        };
    }

}
