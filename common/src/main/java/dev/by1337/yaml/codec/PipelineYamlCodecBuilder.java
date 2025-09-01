package dev.by1337.yaml.codec;

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
            if (field.name == null) {
                builder.properties(field.codec.schema().asBuilder());
            } else {
                builder.properties(field.name, field.codec.schema());
            }
        }
        builder.additionalProperties(false);
        return new YamlCodec<T>() {
            final SchemaType schemaType = builder.build();

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.YAML_VALUE).flatMap(map -> {
                    T v = creator.get();
                    StringBuilder error = new StringBuilder();
                    for (@NotNull YamlField field : fields) {
                        DataResult<?> result = field.decode(map);
                        if (result.hasError()) {
                            error.append("Errors in '").append(field.name).append("':\n  - ").append(result.error().replace("\n", "\n    ")).append("\n");
                        }
                        if (result.hasResult()) {
                            field.setter.accept(v, result.result());
                        } else if (field.defaultValue != null) {
                            field.setter.accept(v, field.defaultValue);
                        }
                    }
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                        return DataResult.error(error.toString()).partial(v);
                    }
                    return DataResult.success(v);
                });
            }

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public YamlValue encode(T value) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (YamlField field : fields) {
                    Object o = field.getter.apply(value);
                    if (o != null) {
                        map.put(field.name, field.codec.encode(o).getValue());
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
