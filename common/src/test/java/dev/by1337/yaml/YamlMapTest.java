package dev.by1337.yaml;


import dev.by1337.yaml.codec.DataResult;
import dev.by1337.yaml.codec.PipelineYamlCodecBuilder;
import dev.by1337.yaml.codec.RecordYamlCodecBuilder;
import dev.by1337.yaml.codec.YamlCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlMapTest {


    @Test
    public void test1() {
        YamlMap yamlMap = new YamlMap();
        yamlMap.set("subMap", new YamlMap());
        yamlMap.get("subMap").decode(YamlCodec.YAML_MAP).getOrThrow().set("test", "string");
        Assertions.assertEquals("string", yamlMap.get("subMap.test").decode(YamlCodec.STRING).getOrThrow());
    }

    @Test
    public void test2() {
        YamlMap yamlMap = new YamlMap();
        yamlMap.set("subMap", new YamlMap());
        yamlMap.get("subMap").decode(YamlCodec.STRING_TO_STRING).getOrThrow().put("test", "string");
        Assertions.assertNull(yamlMap.get("subMap.test").getValue());
    }

    @Test
    public void test3() {
        YamlMap yamlMap = new YamlMap();
        var list = List.of(new YamlMap(), new YamlMap(), new YamlMap());
        yamlMap.set("subMaps", list);
        for (YamlMap map : yamlMap.get("subMaps").decode(YamlCodec.YAML_MAP_LIST).getOrThrow()) {
            map.set("test", "string");
        }
        for (YamlMap map : list) {
            Assertions.assertEquals("string", map.get("test").decode(YamlCodec.STRING).getOrThrow());
        }
    }

    @Test
    public void test4() {
        YamlMap yamlMap = new YamlMap();
        Map<String, Map<String, Object>> innerMap = Map.of(
                "a", new LinkedHashMap<>(),
                "b", new LinkedHashMap<>()
        );

        yamlMap.set("subMap", innerMap);

        Map<String, YamlMap> decoded = yamlMap.get("subMap").decode(YamlCodec.STRING_TO_YAML_MAP).getOrThrow();

        decoded.get("a").set("test", "string");

        Assertions.assertEquals("string", yamlMap.get("subMap.a.test").decode(YamlCodec.STRING).getOrThrow());
    }

    @Test
    public void test23() {
        Assertions.assertEquals(54, YamlValue.EMPTY.decode(YamlCodec.INT, 54).getOrThrow());
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

        Assertions.assertEquals(map, result.getOrThrow());
    }

    @Test
    public void testEnum() {
        DataResult<TestEnum> result = YamlValue.wrap("idk").decode(YamlCodec.enumOf(TestEnum.class));
        Assertions.assertTrue(result.hasError());
    }

    @Test
    public void testBadInts() {
        DataResult<List<Integer>> result = YamlCodec.INT.listOf().decode(YamlValue.wrap(List.of("10", "15", "asd", "20", "ewq")));

        Assertions.assertTrue(result.hasError());
        Assertions.assertEquals(List.of(10, 15, 20), result.result());
    }

/*    @Test
    public void test45() {

        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        map.put("first", Map.of("key_1", List.of("1", "qwe", "precol")));
        map.put("second", Map.of("key_2", List.of("1", "qwe")));

        root.put("map", map);

        var result = YamlValue.wrap(root).decode(CodecTest.RECORD_CODEC);

        System.out.println(result);
        System.out.println();
        System.out.println(result.error());
    }*/

    private static class CodecTest {
        private static final YamlCodec<Map<String, Map<String, List<Integer>>>> MAP_CODEC = YamlCodec.mapOf(YamlCodec.STRING, YamlCodec.mapOf(YamlCodec.STRING, YamlCodec.INT.listOf()));
        private static final YamlCodec<CodecTest> RECORD_CODEC = RecordYamlCodecBuilder.mapOf(
                CodecTest::new,
                MAP_CODEC.fieldOf("map", v -> v.map)
        );
        private static final YamlCodec<CodecTest> CODEC = PipelineYamlCodecBuilder.of(CodecTest::new)
                .field(MAP_CODEC, "map", v -> v.map, (v, m) -> v.map = m)
                .build();
        private Map<String, Map<String, List<Integer>>> map;

        public CodecTest(Map<String, Map<String, List<Integer>>> map) {
            this.map = map;
        }

        public CodecTest() {
        }
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
    }
}