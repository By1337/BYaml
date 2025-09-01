package dev.by1337.yaml.codec.k2v;

import dev.by1337.yaml.codec.YamlCodec;

import java.util.List;
import java.util.Map;

public interface Key2ValueCodec<T> extends YamlCodec<T> {
    Map<String, T> asMap();

    default WildcardLookupCodec<T> wildcard() {
        return new WildcardLookupCodec<>(asMap());
    }
}
