package dev.by1337.yaml.codec.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public  class JsonSchemaTypeBuilder {
    private final JsonObject jsonObject;

    public JsonSchemaTypeBuilder() {
        jsonObject = new JsonObject();
    }

    public JsonSchemaTypeBuilder(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static JsonSchemaTypeBuilder create() {
        return new JsonSchemaTypeBuilder();
    }

    public static SchemaType newRef(String name){
        return new JsonSchemaTypeBuilder().ref("#/definitions/" + name).build();
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder remove(String key) {
        jsonObject.remove(key);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder definitions(SchemaType schemaType) {
        JsonObject definitions = getOrCreateJsonObject("definitions");
        var v = SchemaType.deepCopy(schemaType.getObject());
        var def = v.get("definitions");
        if (def != null){
            def.getAsJsonObject().entrySet()
                    .forEach(e-> definitions.add(e.getKey(), e.getValue()));
        }
        v.remove("definitions");
        String key = schemaType.getRandName().toString();
        if (!definitions.has(key)){
            definitions.add(schemaType.getRandName().toString(), v);
        }
        return this;
    }

    @Contract("_, _ -> this")
    public JsonSchemaTypeBuilder definitions(String definition, SchemaType schemaType) {
        JsonObject definitions = getOrCreateJsonObject("definitions");
        definitions.add(definition, schemaType.getObject());
        return this;
    }


    @Contract("_ -> this")
    public JsonSchemaTypeBuilder examples(String example) {
        JsonArray examples = getOrCreateJsonArray("examples");
        examples.add(example);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder description(String description) {
        jsonObject.addProperty("description", description);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder type(SchemaTypes.Type type) {
        jsonObject.addProperty("type", type.getId());
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder types(SchemaTypes.Type... types) {
        JsonArray jsonArray = new JsonArray();
        for (SchemaTypes.Type type : types) {
            jsonArray.add(type.getId());
        }
        jsonObject.add("type", jsonArray);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder properties(Map<String, SchemaType> properties) {
        JsonObject obj = getOrCreateJsonObject("properties");
        properties.forEach((key, value) -> {
            definitions(value);
            obj.add(key, value.asRef());
        });
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder additionalProperties(boolean b) {
        jsonObject.addProperty("additionalProperties", b);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder items(SchemaType type) {
        definitions(type);
        jsonObject.add("items", type.asRef());
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder patternProperties(Map<String, SchemaType> properties) {
        JsonObject obj = getOrCreateJsonObject("patternProperties");
        properties.forEach((key, value) -> {
            definitions(value);
            obj.add(key, value.asRef());
        });
        return this;
    }

    @Contract("_,_ -> this")
    public JsonSchemaTypeBuilder patternProperties(String key, SchemaType type) {
        JsonObject obj = getOrCreateJsonObject("patternProperties");
        definitions(type);
        obj.add(key, type.asRef());
        return this;
    }

    @Contract("_,_ -> this")
    public JsonSchemaTypeBuilder properties(String key, SchemaType type) {
        JsonObject obj = getOrCreateJsonObject("properties");
        definitions(type);
        obj.add(key, type.asRef());
        return this;
    }
    @Contract("_ -> this")
    public JsonSchemaTypeBuilder properties(JsonSchemaTypeBuilder other) {
        {
            JsonObject obj = getOrCreateJsonObject("properties");
            JsonObject obj2 = other.getOrCreateJsonObject("properties");
            obj2.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
        }

        {
            JsonObject obj = getOrCreateJsonObject("definitions");
            JsonObject obj2 = other.getOrCreateJsonObject("definitions");
            obj2.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder merge(JsonSchemaTypeBuilder other) {
        {
            JsonObject obj = getOrCreateJsonObject("properties");
            JsonObject obj2 = other.getOrCreateJsonObject("properties");
            obj2.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
        }
        {
            JsonObject obj = getOrCreateJsonObject("definitions");
            JsonObject obj2 = other.getOrCreateJsonObject("definitions");
            obj2.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
        }
        {
            JsonObject obj = getOrCreateJsonObject("patternProperties");
            JsonObject obj2 = other.getOrCreateJsonObject("patternProperties");
            obj2.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
        }
        {
            JsonObject obj = getOrCreateJsonObject("additionalProperties");
            JsonObject obj2 = other.getOrCreateJsonObject("additionalProperties");
            obj2.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder oneOf(SchemaType... types) {
        JsonArray array = new JsonArray();
        for (SchemaType type : types) {
            definitions(type);
            array.add(type.asRef());
        }
        jsonObject.add("oneOf", array);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder addOneOf(SchemaType... types) {
        JsonArray array;
        JsonElement element = jsonObject.get("oneOf");
        if (element instanceof JsonArray arr) {
            array = arr;
        } else {
            array = new JsonArray();
            jsonObject.add("oneOf", array);
        }
        for (SchemaType type : types) {
            definitions(type);
            array.add(type.asRef());
        }
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder anyOf(SchemaType... types) {
        JsonArray array = new JsonArray();
        for (SchemaType type : types) {
            definitions(type);
            array.add(type.asRef());
        }
        jsonObject.add("anyOf", array);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder addAnyOf(SchemaType... types) {
        JsonArray array;
        JsonElement element = jsonObject.get("anyOf");
        if (element instanceof JsonArray arr) {
            array = arr;
        } else {
            array = new JsonArray();
            jsonObject.add("anyOf", array);
        }
        for (SchemaType type : types) {
            definitions(type);
            array.add(type.asRef());
        }
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder ref(String ref) {
        jsonObject.addProperty("$ref", ref);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder enumOf(String... elements) {
        JsonArray array = new JsonArray();
        for (String element : elements) {
            array.add(element);
        }
        jsonObject.add("enum", array);
        return this;
    }


    @SafeVarargs
    @Contract("_ -> this")
    public final  <T extends Enum<T>> JsonSchemaTypeBuilder enumOf(T... arr) {
        JsonArray array = new JsonArray();
        for (T element : arr) {
            array.add(element.name().toLowerCase());
        }
        jsonObject.add("enum", array);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder enumOf(Collection<String> elements) {
        JsonArray array = new JsonArray();
        for (String element : elements) {
            array.add(element);
        }
        jsonObject.add("enum", array);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder required(String... elements) {
        JsonArray array = new JsonArray();
        for (String element : elements) {
            array.add(element);
        }
        jsonObject.add("required", array);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder required(Collection<String> elements) {
        JsonArray array = new JsonArray();
        for (String element : elements) {
            array.add(element);
        }
        jsonObject.add("required", array);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder multipleOf(double value) {
        jsonObject.addProperty("multipleOf", value);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder maximum(double value) {
        jsonObject.addProperty("maximum", value);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder exclusiveMaximum(double value) {
        jsonObject.addProperty("exclusiveMaximum", value);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder minimum(double value) {
        jsonObject.addProperty("minimum", value);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder exclusiveMinimum(double value) {
        jsonObject.addProperty("exclusiveMinimum", value);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder pattern(String value) {
        jsonObject.addProperty("pattern", value);
        return this;
    }


    @Contract("_ -> this")
    public JsonSchemaTypeBuilder title(String value) {
        jsonObject.addProperty("title", value);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder schema() {
        return schema("http://json-schema.org/draft-07/schema#");
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder schema(String s) {
        jsonObject.addProperty("$schema", s);
        return this;
    }


    private JsonObject getOrCreateJsonObject(String name) {
        if (jsonObject.get(name) instanceof JsonObject o) {
            return o;
        } else {
            JsonObject obj = new JsonObject();
            jsonObject.add(name, obj);
            return obj;
        }
    }
    private JsonArray getOrCreateJsonArray(String name) {
        if (jsonObject.get(name) instanceof JsonArray o) {
            return o;
        } else {
            JsonArray obj = new JsonArray();
            jsonObject.add(name, obj);
            return obj;
        }
    }

    public SchemaType build() {
        return new SchemaType(jsonObject);
    }
    public SchemaType build(UUID key) {
        return new SchemaType(key,buildJsonObject(key));
    }

    private JsonObject buildJsonObject(UUID key) {
        JsonObject schema = SchemaType.deepCopy(jsonObject);

        JsonObject out = new JsonObject();

        out.addProperty("$ref", "#/definitions/" + key);

        JsonObject definitions = getOrCreateJsonObject("definitions", out);

        if (schema.has("definitions")) {
            JsonObject innerDefs = schema.getAsJsonObject("definitions");
            schema.remove("definitions");

            for (var e : innerDefs.entrySet()) {
                definitions.add(e.getKey(), e.getValue());
            }
        }
        definitions.add(key.toString(), schema);

        return out;
    }
    private JsonObject getOrCreateJsonObject(String name, JsonObject in) {
        if (in.get(name) instanceof JsonObject o) {
            return o;
        } else {
            JsonObject obj = new JsonObject();
            in.add(name, obj);
            return obj;
        }
    }
}
