package net.landania.commons;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;


public class JsonConfig<T> {

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    private final Path defaultPath;
    private final Class<? extends T> configClass;
    private T config;

    public JsonConfig(@NotNull Path defaultPath, @NotNull Class<? extends T> defaultClass) {
        this.defaultPath = defaultPath;
        this.configClass = defaultClass;
    }

    public T access() {
        if (!isLoaded()) {
            throw new IllegalStateException("Configuration is not loaded!");
        }
        return config;
    }

    public void load(@Nullable T defaultConfig, @NotNull Path path) {
        if (isLoaded()) {
            // Why not just reload?
            throw new IllegalStateException("Configuration is already loaded! If you want to reload it, use #reload() instead.");
        }
        File file = path.toFile();
        if (!file.exists()) {
            if (defaultConfig == null) {

                throw new IllegalStateException("Default configuration is null and the file doesn't exist!");
            }
            this.config = defaultConfig;
            save(path);
            return;
        }
        try {
            FileReader fileReader = new FileReader(file);
            JsonReader reader = new JsonReader(fileReader);
            this.config = GSON.fromJson(reader, configClass);
            reader.close(); // Close the reader to prevent memory leaks
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(@Nullable T defaultConfig) {
        load(defaultConfig, defaultPath);
    }

    public void save(@NotNull Path path) {
        if (!isLoaded()) {
            throw new IllegalStateException("Configuration is not loaded!");
        }
        try {
            File file = path.toFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            String json = GSON.toJson(config, TypeToken.get(configClass).getType());
            // clear file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        save(defaultPath);
    }

    public void reload(@NotNull Path path) {
        if (!isLoaded()) {
            throw new IllegalStateException("Configuration is not loaded!");
        }
        load(config, path);
    }

    public void reload() {
        reload(defaultPath);
    }

    public boolean isLoaded() {
        return config != null;
    }

    @NotNull
    public Path getDefaultPath() {
        return defaultPath;
    }

}
