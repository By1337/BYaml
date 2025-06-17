package dev.by1337.yaml.codec.schema;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SchemaHolder {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static void addSchema(File rootDir, String name, String filePattern, SchemaType schema, String title) {
        File schemasFolder = new File(rootDir, ".vscode/schemas");
        File schemaFile = new File(schemasFolder, name);
        if (schemaFile.exists()) return;
        schemasFolder.mkdirs();
        File vscode = new File(rootDir, ".vscode");
        vscode.mkdirs();
        File settingsFile = new File(vscode, "settings.json");
        JsonObject settings;
        if (settingsFile.exists()) {
            try (var in = new InputStreamReader(new FileInputStream(settingsFile))) {
                settings = GSON.fromJson(in, JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            settings = new JsonObject();
        }
        final Map<String, String> schemasMap = new HashMap<>();
        if (settings.get("yaml.schemas") instanceof JsonObject schemas) {
            for (Map.Entry<String, JsonElement> entry : schemas.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) {
                    schemasMap.put(entry.getValue().getAsString(), entry.getKey());
                }
            }
        }
        schemasMap.put(filePattern, getRelativePathWithDot(rootDir, schemaFile));
        JsonObject schemas = new JsonObject();
        schemasMap.forEach((key, value) -> schemas.addProperty(value, key));
        settings.add("yaml.schemas", schemas);

        try {
            Files.writeString(schemaFile.toPath(), GSON.toJson(schema.asBuilder().schema().title(title).build().buildJson()));
            Files.writeString(settingsFile.toPath(), GSON.toJson(settings));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRelativePathWithDot(File base, File target) {
        return "./" + base.toPath().relativize(target.toPath()).toString().replace('\\', '/');
    }
}
