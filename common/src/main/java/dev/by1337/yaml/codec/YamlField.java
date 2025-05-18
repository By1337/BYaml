package dev.by1337.yaml.codec;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class YamlField<T, F> {
    protected final YamlCodec<F> codec;
    protected Function<T, F> getter;
    protected final String name;
    protected F defaultValue;
    protected final BiConsumer<T, F> setter;

    public YamlField(YamlCodec<F> codec, String name, Function<T, F> getter, BiConsumer<T, F> setter) {
        this.codec = codec;
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public YamlField(YamlCodec<F> codec, String name, Function<T, F> getter) {
        this.codec = codec;
        this.name = name;
        this.getter = getter;
        setter = null;
    }

    public YamlField(YamlCodec<F> codec, String name, Function<T, F> getter, F defaultValue) {
        this.codec = codec;
        this.name = name;
        this.getter = getter;
        this.defaultValue = defaultValue;
        setter = null;
    }

    public YamlField(YamlCodec<F> codec, String name, Function<T, F> getter, BiConsumer<T, F> setter, F defaultValue) {
        this.codec = codec;
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.defaultValue = defaultValue;
    }

    public YamlField<T, F> getterOf(Function<T, F> getter) {
        this.getter = getter;
        return this;
    }

    public YamlField<T, F> defaultValue(F defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public YamlCodec<F> codec() {
        return codec;
    }

    public Function<T, F> getter() {
        return getter;
    }

    public String name() {
        return name;
    }

    public F defaultValue() {
        return defaultValue;
    }

    public BiConsumer<T, F> setter() {
        return setter;
    }
}
