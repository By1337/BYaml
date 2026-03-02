package dev.by1337.yaml.codec;

import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;

import java.util.List;
import java.util.function.Function;

@Deprecated
interface LegacyYamlCodec<T> {
    default YamlCodec<List<T>> listOf() {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated {@link YamlCodec#listOf()}
     */
    @Deprecated
    default YamlCodec<List<T>> listOrSingle() {
        return listOf();
    }

    @Deprecated
    default SchemaType schema() {
        return SchemaTypes.ANY;
    }

    @Deprecated
    default YamlCodec<T> schema(Function<SchemaType, SchemaType> mutator) {
        return (YamlCodec<T>) this;
    }

    @Deprecated
    default YamlCodec<T> schema(SchemaType schemaType) {
        return (YamlCodec<T>) this;
    }

}
