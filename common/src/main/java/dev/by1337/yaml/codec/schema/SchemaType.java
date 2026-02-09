package dev.by1337.yaml.codec.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.UUID;

public class SchemaType {
    private final UUID randName;
    private JsonObject object;

    public SchemaType(JsonObject object) {
        this.object = object;
        randName = UUID.randomUUID();
    }

    public SchemaType() {
        randName = UUID.randomUUID();
        object = new JsonObject();
    }

    public SchemaType(UUID randName, JsonObject object) {
        this.randName = randName;
        this.object = object;
    }

    @ApiStatus.Internal
    public void setObject(JsonObject object) {
        this.object = object;
    }

    JsonObject getObject() {
        return object;
    }

    JsonObject asRef(){
        return JsonSchemaTypeBuilder.newRef(randName.toString()).getObject();
    }

    public SchemaType asRefSchema(){
        return JsonSchemaTypeBuilder.newRef(randName.toString());
    }

    public JsonSchemaTypeBuilder asBuilder() {
        return new JsonSchemaTypeBuilder(deepCopy(object));
    }

    // SchemaType -> List<SchemaType>
    @Contract(value = " -> new", pure = true)
    public SchemaType listOf() {
        return SchemaTypes.ARRAY.asBuilder().items(this).build();
    }

    // SchemaType -> Map<String, SchemaType>
    @Contract(value = " -> new", pure = true)
    public SchemaType asMap(){
        return SchemaTypes.OBJECT.asBuilder().patternProperties(".*", this).build();
    }

    @Contract(value = "_ -> new", pure = true)
    public SchemaType or(SchemaType... other){
        return JsonSchemaTypeBuilder.create().anyOf(other).addAnyOf(this).build();
    }


    public JsonObject buildJson() {
        return new JsonDeduplicator().deduplicate(deepCopy(object));
    }


    @SuppressWarnings("unchecked")
    static <T extends JsonElement> T deepCopy(T val) {
        if (val instanceof JsonObject obj) {
            JsonObject copy = new JsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                copy.add(entry.getKey(), deepCopy(entry.getValue()));
            }
            return (T) copy;
        } else if (val instanceof JsonArray arr) {
            JsonArray copy = new JsonArray();
            for (JsonElement element : arr) {
                copy.add(deepCopy(element));
            }
            return (T) copy;
        }
        return val;
    }

    public UUID getRandName() {
        return randName;
    }
}
