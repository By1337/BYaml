package dev.by1337.yaml.codec;

import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.schema.JsonSchemaTypeBuilder;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordYamlCodecBuilder {

    private static <T> T decodeField(Map<String, YamlValue> map, YamlField<?, T> field) {
        if (map.containsKey(field.name)) {
            return field.codec.decode(map.get(field.name));
        }
        return field.defaultValue;
    }

    public static <T, F0> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            Function1<F0, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                return creator.apply(p0);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            Function2<F0, F1, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                return creator.apply(p0, p1);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            Function3<F0, F1, F2, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                return creator.apply(p0, p1, p2);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            Function4<F0, F1, F2, F3, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                return creator.apply(p0, p1, p2, p3);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            Function5<F0, F1, F2, F3, F4, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                return creator.apply(p0, p1, p2, p3, p4);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            Function6<F0, F1, F2, F3, F4, F5, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                return creator.apply(p0, p1, p2, p3, p4, p5);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            Function7<F0, F1, F2, F3, F4, F5, F6, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            Function8<F0, F1, F2, F3, F4, F5, F6, F7, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            Function9<F0, F1, F2, F3, F4, F5, F6, F7, F8, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            Function10<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            Function11<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            Function12<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            Function13<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            Function14<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            YamlField<T, F14> f14,
            Function15<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                F14 p14 = decodeField(map, f14);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            YamlField<T, F14> f14,
            YamlField<T, F15> f15,
            Function16<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                F14 p14 = decodeField(map, f14);
                F15 p15 = decodeField(map, f15);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            YamlField<T, F14> f14,
            YamlField<T, F15> f15,
            YamlField<T, F16> f16,
            Function17<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                F14 p14 = decodeField(map, f14);
                F15 p15 = decodeField(map, f15);
                F16 p16 = decodeField(map, f16);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            YamlField<T, F14> f14,
            YamlField<T, F15> f15,
            YamlField<T, F16> f16,
            YamlField<T, F17> f17,
            Function18<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                F14 p14 = decodeField(map, f14);
                F15 p15 = decodeField(map, f15);
                F16 p16 = decodeField(map, f16);
                F17 p17 = decodeField(map, f17);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            YamlField<T, F14> f14,
            YamlField<T, F15> f15,
            YamlField<T, F16> f16,
            YamlField<T, F17> f17,
            YamlField<T, F18> f18,
            Function19<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                F14 p14 = decodeField(map, f14);
                F15 p15 = decodeField(map, f15);
                F16 p16 = decodeField(map, f16);
                F17 p17 = decodeField(map, f17);
                F18 p18 = decodeField(map, f18);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19> YamlCodec<T> mapOf(
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9,
            YamlField<T, F10> f10,
            YamlField<T, F11> f11,
            YamlField<T, F12> f12,
            YamlField<T, F13> f13,
            YamlField<T, F14> f14,
            YamlField<T, F15> f15,
            YamlField<T, F16> f16,
            YamlField<T, F17> f17,
            YamlField<T, F18> f18,
            YamlField<T, F19> f19,
            Function20<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, T> creator
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public T decode(YamlValue value) {
                Map<String, YamlValue> map = value.getAsMap(YamlCodec.STRING, YamlCodec.identity());
                F0 p0 = decodeField(map, f0);
                F1 p1 = decodeField(map, f1);
                F2 p2 = decodeField(map, f2);
                F3 p3 = decodeField(map, f3);
                F4 p4 = decodeField(map, f4);
                F5 p5 = decodeField(map, f5);
                F6 p6 = decodeField(map, f6);
                F7 p7 = decodeField(map, f7);
                F8 p8 = decodeField(map, f8);
                F9 p9 = decodeField(map, f9);
                F10 p10 = decodeField(map, f10);
                F11 p11 = decodeField(map, f11);
                F12 p12 = decodeField(map, f12);
                F13 p13 = decodeField(map, f13);
                F14 p14 = decodeField(map, f14);
                F15 p15 = decodeField(map, f15);
                F16 p16 = decodeField(map, f16);
                F17 p17 = decodeField(map, f17);
                F18 p18 = decodeField(map, f18);
                F19 p19 = decodeField(map, f19);
                return creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19);
            }

            @SuppressWarnings({"rawtypes"})
            private final YamlField[] fields = new YamlField[]{f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19};

            @Override
            @SuppressWarnings({"rawtypes"})
            protected YamlField[] getFields() {
                return fields;
            }
        };
    }

    private static abstract class MapYamlCodec<T> implements YamlCodec<T> {

        private SchemaType schemaType;

        @SuppressWarnings({"rawtypes"})
        protected abstract YamlField[] getFields();

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public YamlValue encode(T value) {
            Map<String, YamlValue> map = new LinkedHashMap<>();
            for (YamlField field : getFields()) {
                Object o = field.getter.apply(value);
                if (o == null) {
                    o = field.defaultValue;
                }
                if (o != null) {
                    map.put(field.name, field.codec.encode(o));
                }
            }
            return YamlValue.wrap(map);
        }

        @Override
        public @NotNull SchemaType schema() {
            if (schemaType != null) return schemaType;
            buildSchemaType();
            return schemaType;
        }

        @SuppressWarnings({"rawtypes"})
        private void buildSchemaType(){
            JsonSchemaTypeBuilder builder = new JsonSchemaTypeBuilder();
            builder.type(SchemaTypes.Type.OBJECT);
            List<String> required = new ArrayList<>();
            for (YamlField field : getFields()) {
                builder.properties(field.name, field.codec.schema());
                if (field.defaultValue == null){
                    required.add(field.name);
                }
            }
            builder.additionalProperties(false);
            if (!required.isEmpty()) builder.required(required);
            schemaType = builder.build();
        }
    }


    public interface Function1<F0, T> {
        T apply(F0 f0);
    }

    public interface Function2<F0, F1, T> {
        T apply(F0 f0, F1 f1);
    }

    public interface Function3<F0, F1, F2, T> {
        T apply(F0 f0, F1 f1, F2 f2);
    }

    public interface Function4<F0, F1, F2, F3, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3);
    }

    public interface Function5<F0, F1, F2, F3, F4, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4);
    }

    public interface Function6<F0, F1, F2, F3, F4, F5, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5);
    }

    public interface Function7<F0, F1, F2, F3, F4, F5, F6, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6);
    }

    public interface Function8<F0, F1, F2, F3, F4, F5, F6, F7, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7);
    }

    public interface Function9<F0, F1, F2, F3, F4, F5, F6, F7, F8, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8);
    }

    public interface Function10<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9);
    }

    public interface Function11<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10);
    }

    public interface Function12<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11);
    }

    public interface Function13<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12);
    }

    public interface Function14<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13);
    }

    public interface Function15<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13, F14 f14);
    }

    public interface Function16<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13, F14 f14, F15 f15);
    }

    public interface Function17<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13, F14 f14, F15 f15, F16 f16);
    }

    public interface Function18<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13, F14 f14, F15 f15, F16 f16, F17 f17);
    }

    public interface Function19<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13, F14 f14, F15 f15, F16 f16, F17 f17, F18 f18);
    }

    public interface Function20<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, T> {
        T apply(F0 f0, F1 f1, F2 f2, F3 f3, F4 f4, F5 f5, F6 f6, F7 f7, F8 f8, F9 f9, F10 f10, F11 f11, F12 f12, F13 f13, F14 f14, F15 f15, F16 f16, F17 f17, F18 f18, F19 f19);
    }
}
