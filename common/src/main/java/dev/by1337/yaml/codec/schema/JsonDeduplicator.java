package dev.by1337.yaml.codec.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
class JsonDeduplicator {

    private int refCounter = 0;

    public JsonObject deduplicate(JsonObject object) {
        if (!object.has("properties")) return object;
        JsonObject definitions = getOrCreateJsonObject("definitions", object);
        merge(extractEnums(object, new JsonEnumHolder()).enums, definitions);
        var map = deduplicate0(object.getAsJsonObject("properties"), object);
        if (map.isEmpty()) return object;

        merge(map, definitions);

        // merge(deduplicate0(definitions, definitions), definitions);
        object.add("definitions", definitions);
        return object;
    }
    private JsonObject getOrCreateJsonObject(String name, JsonObject jsonObject) {
        if (jsonObject.get(name) instanceof JsonObject o) {
            return o;
        } else {
            JsonObject obj = new JsonObject();
            jsonObject.add(name, obj);
            return obj;
        }
    }

    private void merge(Map<String, JsonElement> map, JsonObject to) {
        for (String s : map.keySet()) {
            to.add(s, map.get(s));
        }
    }


    private Map<String, JsonElement> deduplicate0(JsonElement element, JsonObject root) {
        Map<String, JsonElement> result = new HashMap<>();
        if (element instanceof JsonObject o) {
            for (Map.Entry<String, JsonElement> entry : o.entrySet()) {

                if (entry.getValue() instanceof JsonObject obj) {
                    if (!canBeReplaced(obj)) continue;

                    String name = "dub_" + (refCounter + 1);
                    JsonObject replaceTo = new JsonObject();
                    replaceTo.addProperty("$ref", "#/definitions/" + name);

                    if (replace(root, obj, replaceTo)) {
                        result.put(name, obj);
                        entry.setValue(replaceTo);
                        refCounter++;
                    }
                }

            }
        }
        return result;
    }

    private JsonEnumHolder extractEnums(JsonElement e, JsonEnumHolder holder) {
        if (e instanceof JsonObject o) {
            if (o.has("enum") && o.size() == 1) {
                JsonObject enum_ = new JsonObject();
                enum_.add("enum", o.remove("enum"));
                enum_.addProperty("type", "string");
                String name = holder.putEnum(enum_);
                o.addProperty("$ref", "#/definitions/" + name);
            } else {
                for (Map.Entry<String, JsonElement> entry : o.entrySet()) {
                    extractEnums(entry.getValue(), holder);
                }
            }
        } else if (e instanceof JsonArray arr) {
            for (JsonElement element : arr) {
                extractEnums(element, holder);
            }
        }
        return holder;
    }
    private class JsonEnumHolder {
        private Map<String, JsonElement> enums = new HashMap<>();

        public String putEnum(JsonObject obj){
            for (Map.Entry<String, JsonElement> entry : enums.entrySet()) {
                if (entry.getValue().equals(obj)){
                    return entry.getKey();
                }
            }
            var name = "dub_" + ++refCounter;
            enums.put(name, obj);
            return name;
        }
    }

    private boolean replace(JsonObject in, JsonElement who, JsonElement replaceTo) {
        boolean state = false;
        for (Map.Entry<String, JsonElement> entry : in.entrySet()) {
            var value = entry.getValue();
            if (value != who && value.equals(who)) {
                entry.setValue(replaceTo);
                state = true;
            } else if (value instanceof JsonObject obj) {
                if (replace(obj, who, replaceTo)) {
                    state = true;
                }
            } else if (value instanceof JsonArray array) {
                for (JsonElement element : array) {
                    if (element instanceof JsonObject o) {
                        if (replace(o, who, replaceTo)) {
                            state = true;
                        }
                    }
                }
            }
        }
        return state;
    }

    private static boolean canBeReplaced(JsonObject obj) {
        if (obj.has("$ref")) return false;
        if (obj.get("type") instanceof JsonPrimitive p) {
            if (p.getAsString().equals("object")) return true;
            if (p.getAsString().equals("array")) return true;
            return false;
        }
        return true;
    }
}
