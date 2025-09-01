package dev.by1337.yaml.codec;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@Deprecated
interface LegacyYamlCodec<T> {
    default YamlCodec<List<T>> listOf() {
        throw new UnsupportedOperationException();
    }
}
