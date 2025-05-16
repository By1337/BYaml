package dev.by1337.yaml.codec.schema;

import java.util.Collection;

public class SchemaTypes {

    public static final SchemaType BOOL = builder().type(Type.BOOLEAN).build();
    public static final SchemaType INT = builder().type(Type.INTEGER).build();
    public static final SchemaType NULL = builder().type(Type.NULL).build();
    public static final SchemaType NUMBER = builder().type(Type.NUMBER).build();
    public static final SchemaType OBJECT = builder().type(Type.OBJECT).build();
    public static final SchemaType STRING = builder().type(Type.STRING).build();

    public static final SchemaType ARRAY = builder().type(Type.ARRAY).build();
    public static final SchemaType STRING_OR_NUMBER = oneOf(Type.NUMBER, Type.STRING);
    public static final SchemaType STRINGS = oneOf(STRING, STRING.listOf());
    public static final SchemaType ANY = builder().build();


    public static SchemaType oneOf(final Type... types) {
        return builder().types(types).build();
    }

    public static SchemaType oneOf(final SchemaType... schemas) {
        return builder().oneOf(schemas).build();
    }

    public static SchemaType enumOf(Collection<String> elements) {
        return builder().enumOf(elements).build();
    }

    public static SchemaType array(SchemaType type){
        return builder().type(Type.ARRAY).items(type).build();
    }

    public static JsonSchemaTypeBuilder builder(){
        return JsonSchemaTypeBuilder.create();
    }


    public static enum Type {
        ARRAY("array"),
        BOOLEAN("boolean"),
        INTEGER("integer"),
        NULL("null"),
        NUMBER("number"),
        OBJECT("object"),
        STRING("string");
        private final String id;

        Type(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
