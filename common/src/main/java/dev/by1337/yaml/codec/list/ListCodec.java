package dev.by1337.yaml.codec.list;

import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.YamlCodec;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListCodec<T> implements YamlCodec<List<T>> {
    private final YamlCodec<T> subCodec;


    public ListCodec(YamlCodec<T> subCodec) {
        this.subCodec = subCodec;
    }

    public <R extends Collection<T>> YamlCodec<R> as(Function<List<T>, R> mapper) {
        return new YamlCodec<R>() {
            @Override
            public DataResult<R> decode(YamlValue value) {
                return decode$0(value, mapper);
            }

            @Override
            public YamlValue encode(R value) {
                return encode$0(value);
            }
        };
    }

    public YamlCodec<Set<T>> asSet() {
        return as(HashSet::new);
    }

    public YamlCodec<T[]> asArray() {
        return new YamlCodec<T[]>() {
            @Override
            public DataResult<T[]> decode(YamlValue value) {
                return ListCodec.this.decode(value).flatMap(l -> DataResult.success((T[]) l.toArray()));
            }

            @Override
            public YamlValue encode(T[] value) {
               // if (value.length == 1) return subCodec.encode(value[0]);
                return encode$0(Arrays.asList(value));
            }
        };
    }

    public YamlCodec<T[]> asArray(Class<T> componentType) {
        return new YamlCodec<T[]>() {
            @Override
            public DataResult<T[]> decode(YamlValue value) {
                return ListCodec.this.decode(value).flatMap(l -> DataResult.success(l.toArray((T[])Array.newInstance(componentType, 0))));
            }

            @Override
            public YamlValue encode(T[] value) {
                //    if (value.length == 1) return subCodec.encode(value[0]);
                return encode$0(Arrays.asList(value));
            }
        };
    }

    @Override
    public DataResult<List<T>> decode(YamlValue value) {
        if (!value.isCollection())
            return subCodec.decode(value).flatMap(l -> DataResult.success(List.of(l)));
        return value.asList(subCodec);
    }

    private <R extends Collection<T>> DataResult<R> decode$0(YamlValue value, Function<List<T>, R> mapper) {
        if (!value.isCollection())
            return subCodec.decode(value).flatMap(l -> DataResult.success(mapper.apply(List.of(l))));
        return value.asList(subCodec).flatMap(list -> DataResult.success(mapper.apply(list)));
    }

    @Override
    public YamlValue encode(List<T> value) {
        return encode$0(value);
    }

    private YamlValue encode$0(Collection<T> value) {
       // if (value.size() == 1) return subCodec.encode(value.iterator().next());
        return YamlValue.wrap(value.stream().map(v -> subCodec.encode(v).getRaw()).collect(Collectors.toList()));
    }
}
