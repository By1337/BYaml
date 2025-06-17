package dev.by1337.yaml.codec;

import dev.by1337.yaml.YamlValue;
import dev.by1337.yaml.codec.schema.SchemaType;
import dev.by1337.yaml.codec.schema.SchemaTypes;
import org.jetbrains.annotations.NotNull;

public class InlineYamlCodecBuilder {

    private static <T> T doDecode(Object obj, YamlField<?, T> field, StringBuilder error) {
        DataResult<T> t = field.codec.decode(obj);
        if (t.hasError()) {
            error.append(t.error().replace("\n", "\n    ")).append("\n");
        }
        return t.orDefault(field.defaultValue);
    }

    public static <T, F0, F1> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function2<F0, F1, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 2) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function3<F0, F1, F2, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 3) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function4<F0, F1, F2, F3, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 4) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function5<F0, F1, F2, F3, F4, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 5) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function6<F0, F1, F2, F3, F4, F5, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 6) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function7<F0, F1, F2, F3, F4, F5, F6, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 7) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function8<F0, F1, F2, F3, F4, F5, F6, F7, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 8) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8> YamlCodec<T> inline(
            String regex,
            String expectedExample,
            Function9<F0, F1, F2, F3, F4, F5, F6, F7, F8, T> creator,
            YamlField<T, F0> f0,
            YamlField<T, F1> f1,
            YamlField<T, F2> f2,
            YamlField<T, F3> f3,
            YamlField<T, F4> f4,
            YamlField<T, F5> f5,
            YamlField<T, F6> f6,
            YamlField<T, F7> f7,
            YamlField<T, F8> f8) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 9) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F9> f9) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 10) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F10> f10) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 11) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F11> f11) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 12) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F12> f12) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 13) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F13> f13) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 14) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F14> f14) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 15) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    F14 p14 = doDecode(args[14], f14, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F15> f15) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 16) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    F14 p14 = doDecode(args[14], f14, error);
                    F15 p15 = doDecode(args[15], f15, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F16> f16) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 17) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    F14 p14 = doDecode(args[14], f14, error);
                    F15 p15 = doDecode(args[15], f15, error);
                    F16 p16 = doDecode(args[16], f16, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F17> f17) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 18) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    F14 p14 = doDecode(args[14], f14, error);
                    F15 p15 = doDecode(args[15], f15, error);
                    F16 p16 = doDecode(args[16], f16, error);
                    F17 p17 = doDecode(args[17], f17, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F18> f18) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 19) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    F14 p14 = doDecode(args[14], f14, error);
                    F15 p15 = doDecode(args[15], f15, error);
                    F16 p16 = doDecode(args[16], f16, error);
                    F17 p17 = doDecode(args[17], f17, error);
                    F18 p18 = doDecode(args[18], f18, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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

    public static <T, F0, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19> YamlCodec<T> inline(
            String regex,
            String expectedExample,
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
            YamlField<T, F19> f19) {
        return new InlineCodec<>(regex, expectedExample) {
            @Override
            public DataResult<T> decode(YamlValue value) {
                return value.decode(YamlCodec.STRING).flatMap(s -> {
                    String[] args = s.split(regex);
                    if (args.length != 20) {
                        return DataResult.error("Expected ‘{}’, but got ‘{}’.", expectedExample, s);
                    }
                    StringBuilder error = new StringBuilder();
                    F0 p0 = doDecode(args[0], f0, error);
                    F1 p1 = doDecode(args[1], f1, error);
                    F2 p2 = doDecode(args[2], f2, error);
                    F3 p3 = doDecode(args[3], f3, error);
                    F4 p4 = doDecode(args[4], f4, error);
                    F5 p5 = doDecode(args[5], f5, error);
                    F6 p6 = doDecode(args[6], f6, error);
                    F7 p7 = doDecode(args[7], f7, error);
                    F8 p8 = doDecode(args[8], f8, error);
                    F9 p9 = doDecode(args[9], f9, error);
                    F10 p10 = doDecode(args[10], f10, error);
                    F11 p11 = doDecode(args[11], f11, error);
                    F12 p12 = doDecode(args[12], f12, error);
                    F13 p13 = doDecode(args[13], f13, error);
                    F14 p14 = doDecode(args[14], f14, error);
                    F15 p15 = doDecode(args[15], f15, error);
                    F16 p16 = doDecode(args[16], f16, error);
                    F17 p17 = doDecode(args[17], f17, error);
                    F18 p18 = doDecode(args[18], f18, error);
                    F19 p19 = doDecode(args[19], f19, error);
                    if (!error.isEmpty()) {
                        error.setLength(error.length() - 1);
                    }
                    return DataResult.accept(
                            () -> creator.apply(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19),
                            t -> DataResult.error(error.toString(), t),
                            error.isEmpty() ? null : error.toString()
                    ).mapErrorIfHas(err -> err + "\n" + "Expected ‘" + expectedExample + "’, but got ‘" + s + "’.");
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


    private static abstract class InlineCodec<T> implements YamlCodec<T> {
        private final String regex;
        private final SchemaType schemaType;

        private InlineCodec(String regex, String expectedExample) {
            this.regex = regex;
            schemaType = SchemaTypes.STRING.asBuilder().examples(expectedExample).build();
        }

        @SuppressWarnings({"rawtypes"})
        protected abstract YamlField[] getFields();

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public YamlValue encode(T value) {
            StringBuilder sb = new StringBuilder();
            for (YamlField field : getFields()) {
                sb.append(field.getter.apply(value)).append(regex);
            }
            sb.setLength(sb.length() - regex.length());
            return YamlValue.wrap(sb.toString());
        }

        @Override
        public @NotNull SchemaType schema() {
            return schemaType;
        }
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
