package dev.by1337.yaml;

import dev.by1337.yaml.codec.CodecFinder;
import dev.by1337.yaml.codec.YamlCodec;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BukkitYamlCodecsTest {

    @Test
    public void run() {

        try {
            Class<?> cl = Class.forName("dev.by1337.yaml.BukkitYamlCodecs", false, BukkitYamlCodecsTest.class.getClassLoader());
            for (Field field : cl.getFields()) {
                if (field.getType() != YamlCodec.class || !Modifier.isStatic(field.getModifiers())) continue;

                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType pt) {
                    Type actualType = pt.getActualTypeArguments()[0];
                    if (actualType instanceof Class<?> clazz) {
                        System.out.println(
                                String.format(
                                        "CodecFinder.INSTANCE.registerCodec(%s.class, %s);",
                                        clazz.getCanonicalName(),
                                        field.getName()
                                )
                        );

                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }


}