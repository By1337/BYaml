package dev.by1337.yaml.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;
@ApiStatus.Internal
public class LazyLoad<T> implements Supplier<T>{
    private final Supplier<T> supplier;
    private T value;

    public LazyLoad(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return value == null ? value = supplier.get() : value;
    }
}
