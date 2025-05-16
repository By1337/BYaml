package dev.by1337.yaml.util;

import dev.by1337.yaml.YamlMap;

import java.util.Map;

public interface YamlReader {
     YamlMap read(String data);
     String saveToString(Map<String, Object> map);
}
