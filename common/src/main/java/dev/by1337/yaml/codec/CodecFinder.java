package dev.by1337.yaml.codec;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ApiStatus.Internal
public class CodecFinder {

    public static CodecFinder INSTANCE;

    private final Map<Class<?>, YamlCodec<?>> codecs = new HashMap<>();
    private final List<Function<Class<?>, @Nullable YamlCodec<?>>> codecFactories = new ArrayList<>();

    private CodecFinder() {
    }

    public <T> void registerCodec(final Class<T> clazz, final YamlCodec<T> codec) {
        codecs.put(clazz, codec);
    }

    public void registerCodecFactory(final Function<Class<?>, @Nullable YamlCodec<?>> factory) {
        codecFactories.add(0, factory);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> YamlCodec<T> getCodec(final Class<T> clazz) {
        var v = codecs.get(clazz);
        if (v == null) {
            for (Function<Class<?>, YamlCodec<?>> codecFactory : codecFactories) {
                v = codecFactory.apply(clazz);
                if (v != null) {
                    codecs.put(clazz, v);
                    break;
                }
            }
        }
        return (YamlCodec<T>) v;
    }


    static {
        INSTANCE = new CodecFinder();
        INSTANCE.registerCodecFactory(new Function<>() {
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public YamlCodec<?> apply(Class<?> type) {
                if (type.isEnum()) {
                    return YamlCodec.enumOf((Class<Enum>) type);
                }
                return null;
            }
        });
    }
}
