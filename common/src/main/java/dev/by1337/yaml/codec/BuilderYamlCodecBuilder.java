package dev.by1337.yaml.codec;

import dev.by1337.yaml.YamlMap;
import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BuilderYamlCodecBuilder {


    @SafeVarargs
    public static <T, E> YamlCodec<T> builderOf(
            @NotNull Supplier<T> creator,
            @NotNull YamlField<T, ?>... fields
    ) {
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
                    } else if (field.defaultValue != null){
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
