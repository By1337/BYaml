package dev.by1337.yaml;


import dev.by1337.yaml.codec.YamlCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class YamlMapTest {


    @Test
    public void test1() {
        YamlMap yamlMap = new YamlMap();
        yamlMap.set("subMap", new YamlMap());
        yamlMap.get("subMap").decode(YamlCodec.YAML_MAP).set("test", "string");
        Assertions.assertEquals("string", yamlMap.get("subMap.test").decode(YamlCodec.STRING));
    }

    @Test
    public void test2() {
        YamlMap yamlMap = new YamlMap();
        yamlMap.set("subMap", new YamlMap());
        yamlMap.get("subMap").decode(YamlCodec.STRING_TO_STRING).put("test", "string");
        Assertions.assertNull(yamlMap.get("subMap.test").getValue());
    }

    @Test
    public void test3() {
        YamlMap yamlMap = new YamlMap();
        var list = List.of(new YamlMap(), new YamlMap(), new YamlMap());
        yamlMap.set("subMaps", list);
        for (YamlMap map : yamlMap.get("subMaps").decode(YamlCodec.YAML_MAP_LIST)) {
            map.set("test", "string");
        }
        for (YamlMap map : list) {
            Assertions.assertEquals("string", map.get("test").decode(YamlCodec.STRING));
        }
    }
    @Test
    public void test4() {
        YamlMap yamlMap = new YamlMap();
        Map<String, YamlMap> innerMap = Map.of(
                "a", new YamlMap(),
                "b", new YamlMap()
        );

        yamlMap.set("subMap", innerMap);

        Map<String, YamlMap> decoded = yamlMap.get("subMap").decode(YamlCodec.STRING_TO_YAML_MAP);

        decoded.get("a").set("test", "string");

        Assertions.assertEquals("string", yamlMap.get("subMap.a.test").decode(YamlCodec.STRING));
    }

    @Test
    public void test23() {
        Assertions.assertEquals(54, YamlValue.EMPTY.getAsInt(54));
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
}