package dev.by1337.yaml.util;

import dev.by1337.yaml.codec.YamlCodec;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class ArrayCodecUtil {

    public  static  <A, T> YamlCodec<A> createCodec(
            YamlCodec<T> baseCodec,
            IntFunction<A> factory,
            Class<T> componentType
    ) {
        return baseCodec.listOf().map(list -> {
            A array = factory.apply(list.size());

            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i));
            }

            return array;

        }, array -> {
            int size = Array.getLength(array);

            List<T> list = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                list.add(componentType.cast(Array.get(array, i)));
            }

            return list;
        });
    }
}
