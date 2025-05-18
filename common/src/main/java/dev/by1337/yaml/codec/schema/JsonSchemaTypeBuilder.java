package dev.by1337.yaml.codec.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Map;

public class JsonSchemaTypeBuilder {
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
        properties.forEach((key, value) -> obj.add(key, value.getObject()));
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder additionalProperties(boolean b) {
        jsonObject.addProperty("additionalProperties", b);
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder items(SchemaType type) {
        jsonObject.add("items", type.getObject());
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder patternProperties(Map<String, SchemaType> properties) {
        JsonObject obj = getOrCreateJsonObject("patternProperties");
        properties.forEach((key, value) -> obj.add(key, value.getObject()));
        return this;
    }

    @Contract("_,_ -> this")
    public JsonSchemaTypeBuilder patternProperties(String key, SchemaType type) {
        JsonObject obj = getOrCreateJsonObject("patternProperties");
        obj.add(key, type.getObject());
        return this;
    }

    @Contract("_,_ -> this")
    public JsonSchemaTypeBuilder properties(String key, SchemaType type) {
        JsonObject obj = getOrCreateJsonObject("properties");
        obj.add(key, type.getObject());
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder oneOf(SchemaType... types) {
        JsonArray array = new JsonArray();
        for (SchemaType type : types) {
            array.add(type.getObject());
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
            array.add(type.getObject());
        }
        return this;
    }

    @Contract("_ -> this")
    public JsonSchemaTypeBuilder anyOf(SchemaType... types) {
        JsonArray array = new JsonArray();
        for (SchemaType type : types) {
            array.add(type.getObject());
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
            array.add(type.getObject());
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

    public SchemaType build() {
        return new SchemaType(jsonObject);
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }
}
