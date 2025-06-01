package dev.by1337.yaml.codec.schema;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

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
        JsonObject schemas;
        if (settings.get("yaml.schemas") instanceof JsonObject o) {
            schemas = o;
        } else {
            schemas = new JsonObject();
            settings.add("yaml.schemas", schemas);
        }
        schemas.addProperty(getRelativePathWithDot(rootDir, schemaFile), filePattern);

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
