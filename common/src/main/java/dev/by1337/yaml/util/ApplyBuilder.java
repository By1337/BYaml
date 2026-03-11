package dev.by1337.yaml.util;

import dev.by1337.yaml.codec.DataResult;

import java.util.function.Function;

public class ApplyBuilder {

    public static class Ap2<F0, F1> {
        final DataResult<F0> f0;
        final DataResult<F1> f1;

        public Ap2(DataResult<F0> f0, DataResult<F1> f1) {
            this.f0 = f0;
            this.f1 = f1;
        }

        public <T> DataResult<T> map(Function2<F0, F1, T> mapper) {
            try {
                return flatMap(mapper.map(DataResult::success));
            } catch (Exception e) {
                return DataResult.error(e);
            }
        }

        public <T> DataResult<T> flatMap(Function2<F0, F1, DataResult<T>> mapper) {
            F0 f0 = this.f0.result();
            if (f0 == null) return this.f0.widen();
            F1 f1 = this.f1.result();
            if (f1 == null) return this.f1.widen();
            return mapper.apply(f0, f1)
                    .withError(this.f0.error())
                    .withError(this.f1.error());
        }

        public <R> Ap3<F0, F1, R> and(DataResult<R> r){
            return new Ap3<>(f0, f1, r);
        }
    }
    public static class Ap3<F0, F1,F2> {
        final DataResult<F0> f0;
        final DataResult<F1> f1;
        final DataResult<F2> f2;

        public Ap3(DataResult<F0> f0, DataResult<F1> f1, DataResult<F2> f2) {
            this.f0 = f0;
            this.f1 = f1;
            this.f2 = f2;
        }

        public <T> DataResult<T> map(Function3<F0, F1, F2, T> mapper) {
            try {
                return flatMap(mapper.map(DataResult::success));
            } catch (Exception e) {
                return DataResult.error(e);
            }
        }

        public <T> DataResult<T> flatMap(Function3<F0, F1, F2, DataResult<T>> mapper) {
            F0 f0 = this.f0.result();
            if (f0 == null) return this.f0.widen();
            F1 f1 = this.f1.result();
            if (f1 == null) return this.f1.widen();
            F2 f2 = this.f2.result();
            if (f2 == null) return this.f2.widen();
            return mapper.apply(f0, f1, f2)
                    .withError(this.f0.error())
                    .withError(this.f1.error())
                    .withError(this.f2.error());
        }
    }

    public interface Function1<F0, T> {
        T apply(F0 f0);
    }

    public interface Function2<F0, F1, T> {
        T apply(F0 f0, F1 f1);

        default <R> Function2<F0, F1, R> map(Function<T, R> map) {
            return (f0, f1) -> map.apply(apply(f0, f1));
        }
    }

    public interface Function3<F0, F1, F2, T> {
        T apply(F0 f0, F1 f1, F2 f2);
        default <R> Function3<F0, F1, F2, R> map(Function<T, R> map) {
            return (f0, f1, f2) -> map.apply(apply(f0, f1, f2));
        }
    }

}
