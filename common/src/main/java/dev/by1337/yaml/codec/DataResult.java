package dev.by1337.yaml.codec;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;


/**
 * A container for a value and/or an error message.
 *
 * <p>{@code DataResult} is typically used to represent the outcome of data processing operations
 * such as parsing or encoding. It may contain a successful value, an error message, or both
 * (as a "partial" result).
 *
 * <p>Use {@link #success(Object)} and {@link #error(String)} to create instances.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * DataResult<Integer> result = DataResult.success(42);
 * if (result.hasResult()) {
 *     System.out.println("Got value: " + result.result());
 * } else {
 *     System.err.println("Failed: " + result.error());
 * }
 * }</pre>
 *
 * @param <T> the type of the value
 */
@ApiStatus.NonExtendable
public interface DataResult<T> {

    /**
     * @return the value of this result, or {@code null} if not available
     */
    @Nullable T result();

    /**
     * @return the error message if present, or {@code null} if the operation succeeded
     */
    @Nullable String error();

    /**
     * Returns whether this result contains a non-null value.
     *
     * @return true if the result is present, false otherwise
     */
    default boolean hasResult() {
        return result() != null;
    }

    /**
     * Returns whether this result contains an error message.
     *
     * @return true if an error is present, false otherwise
     */
    default boolean hasError() {
        return error() != null;
    }

    /**
     * @param defaultValue Результат по умолчанию
     * @return {@link DataResult#result()} если он не {@code null} иначе {@code defaultValue}
     */
    default T orDefault(final T defaultValue) {
        var res = result();
        return res == null ? defaultValue : res;
    }


    /**
     * Returns the result value if present, otherwise throws an {@link IllegalStateException}.
     *
     * <p>This method is useful in cases where the absence of a result is considered a fatal error,
     * and you want to fail fast if decoding or processing has failed.
     *
     * @return the non-null result
     * @throws IllegalStateException if no result is present; the exception message will include the error if available
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * int value = DataResult.success(42).getOrThrow(); // returns 42
     *
     * DataResult<String> failed = DataResult.error("Invalid format");
     * failed.getOrThrow(); // throws IllegalStateException("Invalid format")
     * }</pre>
     */
    default @NotNull T getOrThrow() {
        T v = result();
        if (v != null) {
            return v;
        }
        String msg = error();
        throw new IllegalStateException(msg == null ? "No result" : msg);
    }

    /**
     * Creates a {@link DataResult} from a potentially exception-throwing supplier, with support for partial success and custom error handling.
     * <p>
     * If the supplier executes successfully and {@code error} is {@code null}, a full success result is returned.
     * If the supplier executes successfully but {@code error} is non-null, a partial result is returned with the provided error message.
     * If the supplier throws an exception, the exception is passed to {@code ifFailed} to produce a failed {@code DataResult}.
     *
     * @param supplier the supplier that produces the result, may throw an exception
     * @param ifFailed a function that transforms any thrown exception into a {@code DataResult} failure
     * @param error an optional error message; if not null and the supplier succeeds, the result is marked partial
     * @param <T> the type of result value
     * @return a {@code DataResult} representing the success, partial, or failure result
     */
    static <T> DataResult<T> accept(ThrowingSupplier<T> supplier, Function<Throwable, DataResult<T>> ifFailed, @Nullable String error) {
        try {
            if (error == null) return DataResult.success(supplier.get());
            return DataResult.error(error).partial(supplier.get());
        } catch (Throwable t) {
            return ifFailed.apply(t);
        }
    }

    /**
     * Executes the given {@link ThrowingSupplier}, wrapping the result in a {@link DataResult}.
     * <p>
     * If the supplier completes successfully, a successful {@code DataResult} is returned.
     * If the supplier throws an exception, the exception is passed to {@code ifFailed} to produce a failed {@code DataResult}.
     *
     * <p><b>Example usage:</b>
     * <pre>{@code
     * return DataResult.accept(
     *     () -> Integer.parseInt(input),
     *     throwable -> DataResult.error("Invalid number format", throwable)
     * );
     * }</pre>
     *
     * @param supplier a supplier that may throw an exception
     * @param ifFailed function to create a failed {@code DataResult} from a thrown exception
     * @param <T> the type of result value
     * @return a successful or failed {@code DataResult} depending on the supplier outcome
     */

    static <T> DataResult<T> accept(ThrowingSupplier<T> supplier, Function<Throwable, DataResult<T>> ifFailed) {
        try {
            return DataResult.success(supplier.get());
        } catch (Throwable t) {
            return ifFailed.apply(t);
        }
    }

    /**
     * Returns a {@code partial} result with the given value if this result currently has no value.
     * Otherwise returns this instance unchanged.
     *
     * <p>This is useful when enriching an error-only {@code DataResult} with a best-effort or default value
     * without overriding an already successful result.
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * DataResult<String> result = existingResult.partialIfHasNoResult("default value");
     * }</pre>
     *
     * @param result the value to use if this result has none
     * @return a new partial result or the current instance
     */
    default DataResult<T> partialIfHasNoResult(T result) {
        if (result() == null) return partial(result);
        return this;
    }

    /**
     * Creates a new {@code DataResult} with the given partial result and the current error message.
     * Must be called only if this result has no value.
     *
     * <p>This is useful to attach a fallback or default value to an error result.
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * DataResult<String> result = DataResult.<String>error("Something failed").partial("fallback");
     * }</pre>
     *
     * @param result the fallback value to associate
     * @param <R> the result type
     * @return a new {@code DataResult} containing the partial value and the error
     * @throws IllegalStateException if this result already has a value
     */
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

    /**
     * Creates a successful {@code DataResult} with the given value and no error.
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * DataResult<String> result = DataResult.success("ok");
     * }</pre>
     *
     * @param result the value to return
     * @param <T> the result type
     * @return a successful {@code DataResult}
     */
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

    /**
     * Creates an error {@code DataResult} from the given {@link Throwable}.
     *
     * @param t the exception
     * @param <T> the result type
     * @return an error {@code DataResult} with the exception message
     */
    static <T> DataResult<T> error(@NotNull Throwable t) {
        return error(t.getMessage(), t);
    }

    /**
     * Creates an error {@code DataResult} with a formatted error message.
     *
     * @param message the format string
     * @param args arguments for the format
     * @param <T> the result type
     * @return an error {@code DataResult}
     */
    static <T> DataResult<T> error(@NotNull String message, Object... args) {
        return error(MessageFormatter.apply(message, args));
    }

    /**
     * Creates an error {@code DataResult} with a formatted message and a full stack trace.
     *
     * <p>The final message is composed of the formatted message followed by the throwable's stack trace.
     *
     * @param message the error format string
     * @param t the throwable to include
     * @param args arguments for the format
     * @param <T> the result type
     * @return an error {@code DataResult}
     */
    static <T> DataResult<T> error(@NotNull String message, @NotNull Throwable t, Object... args) {
        final String error = MessageFormatter.apply(message, args) + "\n" + MessageFormatter.throwableToString(t);
        return error(error);
    }

    /**
     * Creates an error {@code DataResult} with the given message.
     *
     * @param message the error message
     * @param <T> the result type
     * @return an error {@code DataResult}
     */
    static <T> DataResult<T> error(@Nullable String message) {
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

    /**
     * Applies the given mapping function to the successful result, returning a new {@link DataResult}.
     * <p>
     * This method is similar to {@link java.util.Optional#flatMap}, and is useful for chaining operations that themselves return {@code DataResult}.
     * <p>
     * If this result is an error or null, the error is propagated and the mapper is not applied.
     *
     * <p><b>Example usage:</b>
     * <pre>{@code
     * return STRING.decode(value).flatMap(str ->
     *     parseToInt(str).flatMap(num ->
     *         DataResult.success(num * 2)
     *     )
     * );
     * }</pre>
     *
     * @param mapper the mapping function to apply to the result value
     * @param <R> the type of the result of the mapping function
     * @return a new {@code DataResult} representing either the mapped value or a propagated error
     */

    default <R> DataResult<R> flatMap(ThrowingFunction<? super T, DataResult<R>> mapper) {
        if (result() == null) {
            String msg = error();
            return error((msg == null ? "Failed to map null! mapper: " + mapper.getClass() : msg));
        }
        try {
            return mapper.apply(result());
        } catch (Throwable e) {
            return error("Failed to map data result!", e);
        }
    }

    /**
     * Applies the given mapping function to the successful result value and wraps the result in a new {@link DataResult}.
     * <p>
     * Unlike {@link #flatMap}, the mapping function is not expected to return a {@code DataResult} — only a plain value.
     * This is useful when you want to transform the contained value without introducing another level of wrapping.
     * <p>
     * If the current result is an error or {@code null}, the error is propagated and the mapper is not applied.
     * <p>
     * If the mapper throws an exception, the error is caught and wrapped in a failed {@code DataResult}.
     *
     * <p><b>Example usage:</b>
     * <pre>{@code
     * return STRING.decode(value)
     *     .mapValue(str -> str.toUpperCase()); // wraps uppercased string in a new DataResult
     * }</pre>
     *
     * @param mapper the function to apply to the result value
     * @param <R> the type of the result after mapping
     * @return a new {@code DataResult} containing the mapped value, or an error if mapping fails
     */
    default <R> DataResult<R> mapValue(ThrowingFunction<? super T, ? extends R> mapper) {
        if (result() == null) {
            String msg = error();
            return error((msg == null ? "Failed to map null! mapper: " + mapper.getClass() : msg));
        }
        try {
            return success(mapper.apply(result()));
        } catch (Throwable e) {
            return error("Failed to map value!", e);
        }
    }



    /**
     * A functional interface similar to {@link java.util.function.Supplier}, but allows throwing checked exceptions.
     *
     * <p>This is useful in functional APIs where operations may fail and throw exceptions, and you want to
     * propagate or wrap those exceptions in a controlled way (e.g., via {@code DataResult} or similar wrappers).
     *
     * <p><b>Example usage:</b>
     * <pre>{@code
     * DataResult<String> result = DataResult.accept(() -> {
     *     if (someConditionFails()) throw new IOException("Something went wrong");
     *     return "Success!";
     * }, throwable -> DataResult.error("Failure", throwable));
     * }</pre>
     *
     * @param <T> the type of the result supplied
     */
    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }


    /**
     * A functional interface similar to {@link java.util.function.Function}, but allows throwing checked exceptions.
     *
     * <p>This is especially useful in mapping methods (e.g., {@code map}, {@code flatMap}) where the function may
     * throw exceptions during transformation and you want to handle that gracefully.
     *
     * <p><b>Example usage:</b>
     * <pre>{@code
     * DataResult<Integer> parsed = stringResult.flatMap((ThrowingFunction<String, DataResult<Integer>>) s -> {
     *     return DataResult.success(Integer.parseInt(s));
     * });
     * }</pre>
     *
     * @param <T> the input type
     * @param <R> the result type
     */
    @FunctionalInterface
    interface ThrowingFunction<T, R> {
        R apply(T t) throws Throwable;
    }

}
