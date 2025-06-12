package dev.by1337.yaml.codec;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@ApiStatus.NonExtendable
public interface DataResult<T> {

    @Nullable T result();

    @Nullable String error();

    default boolean hasResult() {
        return result() != null;
    }

    default boolean hasError() {
        return error() != null;
    }

    default T orDefault(final T defaultValue) {
        var res = result();
        return res == null ? defaultValue : res;
    }

    default T getOrThrow() {
        if (hasResult()) {
            return result();
        }
        String msg = error();
        throw new IllegalStateException(msg == null ? "No result" : msg);
    }

    static <T> DataResult<T> accept(ThrowingSupplier<T> supplier, Function<Throwable, DataResult<T>> ifFailed, @Nullable String error) {
        try {
            if (error == null) return DataResult.success(supplier.get());
            return DataResult.error(error).partial(supplier.get());
        } catch (Throwable t) {
            return ifFailed.apply(t);
        }
    }

    static <T> DataResult<T> accept(ThrowingSupplier<T> supplier, Function<Throwable, DataResult<T>> ifFailed) {
        try {
            return DataResult.success(supplier.get());
        } catch (Throwable t) {
            return ifFailed.apply(t);
        }
    }

    default <R> DataResult<R> partial(R result) {
        if (hasResult()) throw new IllegalStateException("DataResult already has a result");
        String error = error();
        return new DataResult<R>() {
            @Override
            public @Nullable R result() {
                return result;
            }

            @Override
            public @Nullable String error() {
                return error;
            }
            @Override
            public String toString() {
                return "DataResult{ result: '" + result() + "', error: '" + error() + "' }";
            }
        };
    }

    static <T> DataResult<T> success(@Nullable final T result) {
        return new DataResult<T>() {
            @Override
            public @Nullable T result() {
                return result;
            }

            @Override
            public @Nullable String error() {
                return null;
            }

            @Override
            public String toString() {
                return "DataResult{ result: '" + result() + "', error: '" + error() + "' }";
            }
        };
    }

    static <T> DataResult<T> error(Throwable t) {
        return error(t.getMessage(), t);
    }
    static <T> DataResult<T> error(String message, Object... args) {
        return error(MessageFormatter.apply(message, args));
    }
    static <T> DataResult<T> error(String message, Throwable t, Object... args) {
        final String error = MessageFormatter.apply(message, args) + "\n" + MessageFormatter.throwableToString(t);
        return error(error);
    }

    static <T> DataResult<T> error(String message) {
        return new DataResult<T>() {
            @Override
            public @Nullable T result() {
                return null;
            }

            @Override
            public String error() {
                return message;
            }

            @Override
            public String toString() {
                return "DataResult{ result: 'null', error: '" + error() + "' }";
            }
        };
    }

    default <R> DataResult<R> flatMap(ThrowingFunction<? super T, DataResult<R>> mapper) {
        if (result() == null) {
            String msg = error();
            return error(null, (msg == null ? "Failed to map null! mapper: " + mapper.getClass() : msg));
        }
        try {
            return mapper.apply(result());
        } catch (Throwable e) {
            return error("Failed to map data result!", e);
        }
    }

    default <R> DataResult<R> mapValue(ThrowingFunction<? super T, ? extends R> mapper) {
        if (result() == null) {
            String msg = error();
            return error(null, (msg == null ? "Failed to map null! mapper: " + mapper.getClass() : msg));
        }
        try {
            return success(mapper.apply(result()));
        } catch (Throwable e) {
            return error("Failed to map value!", e);
        }
    }



    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    interface ThrowingFunction<T, R> {
        R apply(T t) throws Throwable;
    }
}
