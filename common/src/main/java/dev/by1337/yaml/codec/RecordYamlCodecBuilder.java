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

    private static <T> T decodeField(Map<String, YamlValue> map, YamlField<?, T> field, StringBuilder error) {
        if (map.containsKey(field.name)) {
            DataResult<T> t = field.codec.decode(map.get(field.name));
            if (t.hasError()) {
                error.append("Errors in '").append(field.name).append("':\n  - ").append(t.error().replace("\n", "\n    ")).append("\n");
            }
            return t.orDefault(field.defaultValue);
        }
        return field.defaultValue;
    }

    public static <T, F0> YamlCodec<T> mapOf(
            Function1<F0, T> creator,
            YamlField<T, F0> f0
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    );
                });
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
            Function2<F0, F1, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    );
                });
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
            Function3<F0, F1, F2, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    );
                });
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
            Function4<F0, F1, F2, F3, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function5<F0, F1, F2, F3, F4, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function6<F0, F1, F2, F3, F4, F5, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function7<F0, F1, F2, F3, F4, F5, F6, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function8<F0, F1, F2, F3, F4, F5, F6, F7, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function9<F0, F1, F2, F3, F4, F5, F6, F7, F8, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function10<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8,
            YamlField<T, F9> f9
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function11<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> creator,
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
            YamlField<T, F10> f10
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function12<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, T> creator,
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
            YamlField<T, F11> f11
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function13<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, T> creator,
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
            YamlField<T, F12> f12
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function14<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, T> creator,
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
            YamlField<T, F13> f13
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function15<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, T> creator,
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
            YamlField<T, F14> f14
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    F14 p14 = decodeField(map, f14, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function16<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, T> creator,
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
            YamlField<T, F15> f15
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    F14 p14 = decodeField(map, f14, error);
                    F15 p15 = decodeField(map, f15, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function17<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, T> creator,
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
            YamlField<T, F16> f16
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    F14 p14 = decodeField(map, f14, error);
                    F15 p15 = decodeField(map, f15, error);
                    F16 p16 = decodeField(map, f16, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function18<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, T> creator,
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
            YamlField<T, F17> f17
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    F14 p14 = decodeField(map, f14, error);
                    F15 p15 = decodeField(map, f15, error);
                    F16 p16 = decodeField(map, f16, error);
                    F17 p17 = decodeField(map, f17, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function19<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, T> creator,
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
            YamlField<T, F18> f18
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    F14 p14 = decodeField(map, f14, error);
                    F15 p15 = decodeField(map, f15, error);
                    F16 p16 = decodeField(map, f16, error);
                    F17 p17 = decodeField(map, f17, error);
                    F18 p18 = decodeField(map, f18, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Function20<F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, T> creator,
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
            YamlField<T, F19> f19
    ) {
        return new MapYamlCodec<T>() {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.asMap(YamlCodec.STRING, YamlCodec.identity()).flatMap(map -> {
                    StringBuilder error = new StringBuilder();
                    F0 p0 = decodeField(map, f0, error);
                    F1 p1 = decodeField(map, f1, error);
                    F2 p2 = decodeField(map, f2, error);
                    F3 p3 = decodeField(map, f3, error);
                    F4 p4 = decodeField(map, f4, error);
                    F5 p5 = decodeField(map, f5, error);
                    F6 p6 = decodeField(map, f6, error);
                    F7 p7 = decodeField(map, f7, error);
                    F8 p8 = decodeField(map, f8, error);
                    F9 p9 = decodeField(map, f9, error);
                    F10 p10 = decodeField(map, f10, error);
                    F11 p11 = decodeField(map, f11, error);
                    F12 p12 = decodeField(map, f12, error);
                    F13 p13 = decodeField(map, f13, error);
                    F14 p14 = decodeField(map, f14, error);
                    F15 p15 = decodeField(map, f15, error);
                    F16 p16 = decodeField(map, f16, error);
                    F17 p17 = decodeField(map, f17, error);
                    F18 p18 = decodeField(map, f18, error);
                    F19 p19 = decodeField(map, f19, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString());
                });
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
            Map<String, Object> map = new LinkedHashMap<>();
            for (YamlField field : getFields()) {
                Object o = field.getter.apply(value);
                if (o == null) {
                    o = field.defaultValue;
                }
                if (o != null) {
                    map.put(field.name, field.codec.encode(o).getValue());
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
        private void buildSchemaType() {
            JsonSchemaTypeBuilder builder = new JsonSchemaTypeBuilder();
            builder.type(SchemaTypes.Type.OBJECT);
            List<String> required = new ArrayList<>();
            for (YamlField field : getFields()) {
                builder.properties(field.name, field.codec.schema());
                if (field.defaultValue == null) {
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
