package dev.by1337.yaml.codec;

import dev.by1337.yaml.YamlMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class InlineYamlCodecBuilderTest {

    public static class Vec3i {
        public static final YamlCodec<Vec3i> MAP_CODEC = RecordYamlCodecBuilder.mapOf(
                Vec3i::new,
                YamlCodec.INT.fieldOf("x", v -> v.x),
                YamlCodec.INT.fieldOf("y", v -> v.y),
                YamlCodec.INT.fieldOf("z", v -> v.z)
        );
        public static final YamlCodec<Vec3i> INLINE_CODEC = InlineYamlCodecBuilder.inline(
                " ", "<x> <y> <z>",
                Vec3i::new,
                YamlCodec.INT.withGetter(v -> v.x),
                YamlCodec.INT.withGetter(v -> v.y),
                YamlCodec.INT.withGetter(v -> v.z)
        );
        public static final YamlCodec<Vec3i> CODEC = YamlCodec.dispatchByShape(INLINE_CODEC, MAP_CODEC);
        private final int x;
        private final int y;
        private final int z;

        public Vec3i(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vec3i vec3i = (Vec3i) o;
            return x == vec3i.x && y == vec3i.y && z == vec3i.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    @Test
    public void test() {
        Vec3i vec3i = new Vec3i(1, 2, 3);
        YamlMap yamlMap = new YamlMap();
        yamlMap.set("full", vec3i, Vec3i.MAP_CODEC);
        yamlMap.set("inline", vec3i, Vec3i.INLINE_CODEC);
        yamlMap.set("idk", vec3i, Vec3i.CODEC);
        Assertions.assertEquals(vec3i, yamlMap.get("full").decode(Vec3i.CODEC).result());
        Assertions.assertEquals(vec3i, yamlMap.get("inline").decode(Vec3i.CODEC).result());
        Assertions.assertEquals(vec3i, yamlMap.get("idk").decode(Vec3i.CODEC).result());
    }
}