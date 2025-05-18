package dev.by1337.yaml;


import dev.by1337.yaml.codec.BuilderYamlCodecBuilder;
import dev.by1337.yaml.codec.RecordYamlCodecBuilder;
import dev.by1337.yaml.codec.YamlCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class YamlMapTest {


    @Test
    public void test23() {
        Assertions.assertEquals(54, YamlValue.EMPTY.getAsInt(54));
    }

    @Test
    public void yamlMapGet() {
        {
            YamlMap yamlMap = new YamlMap();
            var newMap = new YamlMap();
            yamlMap.set("subMap", newMap);
            var decoded = yamlMap.get("subMap").decode(YamlCodec.YAML_MAP);
            Assertions.assertSame(newMap.getRaw(), decoded.getRaw());
            decoded.set("test", "string");
            Assertions.assertEquals("string", yamlMap.get("subMap.test").decode(YamlCodec.STRING));
        }
        {
            YamlMap yamlMap = new YamlMap();
            List<YamlMap> list = new ArrayList<>();
            list.add(new YamlMap());
            list.add(new YamlMap());
            list.add(new YamlMap());


            yamlMap.set("maps", list);


            List<YamlMap> result = yamlMap.get("maps").decode(YamlCodec.YAML_MAP.listOf());

            Assertions.assertSame(result.get(0).getRaw(), list.get(0).getRaw());
            Assertions.assertSame(result.get(1).getRaw(), list.get(1).getRaw());
            Assertions.assertSame(result.get(2).getRaw(), list.get(2).getRaw());

            result.get(0).set("test", "string");
            result.get(1).set("test", "string");
            result.get(2).set("test", "string");

            Assertions.assertEquals("string", list.get(0).get("test").decode(YamlCodec.STRING));
            Assertions.assertEquals("string", list.get(1).get("test").decode(YamlCodec.STRING));
            Assertions.assertEquals("string", list.get(2).get("test").decode(YamlCodec.STRING));
        }


    }

    @Test
    public void test24() {
        Assertions.assertSame(YamlValue.wrap(null), YamlValue.EMPTY);

    }

    @Test
    public void test22() {

        var codec = YamlCodec.mapOf(YamlCodec.STRING, YamlCodec.INT.listOf());

        Map<String, List<Integer>> map = new HashMap<>();
        map.put("1", List.of(1));

        var result = YamlValue.wrap(map).decode(codec);

        Assertions.assertEquals(map, result);
    }


    public void preview() {
        YamlMap yaml = new YamlMap();

        var codec = YamlCodec.mapOf(YamlCodec.STRING, YamlCodec.INT.listOf());

        Map<String, List<Integer>> result = yaml.get("data").decode(codec);
    }


    public void testGetSet() {
        YamlMap map = new YamlMap(new LinkedHashMap<>());

        map.set("test.test", 2);
        map.set("test.test2", 3);

        Assertions.assertEquals(2, map.get("test.test").getValue());
        Assertions.assertEquals(3, map.get("test.test2").getValue());
    }



/*    @Test
    public void tezt() {
        Vec3i orig = new Vec3i(1, 2, 3, null);

        YamlMap map = Vec3i.CODEC.encode(orig).getAsYamlMap();
        System.out.println(map);
        map.set("x", null);
        Vec3i decoded = map.get().decode(Vec3i.CODEC);
        Assertions.assertEquals(orig, decoded);

    }

    @Test
    public void tezt2() {
        Vec3d orig = new Vec3d(1, 2, 3, "data");

        YamlMap map = Vec3d.CODEC.encode(orig).getAsYamlMap();
        System.out.println(map);

        Vec3d decoded = map.get().decode(Vec3d.CODEC);
        Assertions.assertEquals(orig, decoded);

    }*/

    public static class Vec3d {
        public static final YamlCodec<Vec3d> CODEC = BuilderYamlCodecBuilder.builderOf(
                Vec3d::new,
                YamlCodec.DOUBLE.fieldOf("x", Vec3d::getX, Vec3d::setX),
                YamlCodec.DOUBLE.fieldOf("y", Vec3d::getY, Vec3d::setY),
                YamlCodec.DOUBLE.fieldOf("z", Vec3d::getZ, Vec3d::setZ),
                YamlCodec.STRING.fieldOf("data", Vec3d::getData, Vec3d::setData)
        );
        public double x;
        public double y;
        public double z;
        public String data;


        public Vec3d() {
        }

        public Vec3d(double x, double y, double z, String data) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.data = data;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vec3d vec3d = (Vec3d) o;
            return Double.compare(x, vec3d.x) == 0 && Double.compare(y, vec3d.y) == 0 && Double.compare(z, vec3d.z) == 0 && Objects.equals(data, vec3d.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, data);
        }
    }

    public static class Vec3i {
        private static final YamlCodec<Vec3i> CODEC = RecordYamlCodecBuilder.mapOf(
                YamlCodec.INT.fieldOf("x", Vec3i::getX),
                YamlCodec.INT.fieldOf("y", Vec3i::getY),
                YamlCodec.INT.fieldOf("z", Vec3i::getZ),
                YamlCodec.OBJECT.fieldOf("null", Vec3i::getNullable),
                Vec3i::new
        );

        private final Integer x;
        private final Integer y;
        private final Integer z;
        private final Object nullable;

        public Vec3i(Integer x, Integer y, Integer z, Object nullable) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.nullable = nullable;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public Object getNullable() {
            return nullable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vec3i vec3I = (Vec3i) o;
            return x == vec3I.x && y == vec3I.y && z == vec3I.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

}