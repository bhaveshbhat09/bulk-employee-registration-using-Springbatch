package com.epfo.learn.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppStorage {

    private final Path base;

    public AppStorage(Environment env) {
        // Optional: add app.storage.dir=... to epfolearn.properties to change this.
        this.base = Paths.get(env.getProperty("app.storage.dir", "E:/project/epfo-learn/storage"));
    }

    public Path uploadFile(long batchId) {
        return base.resolve("uploads").resolve("batch" + batchId + ".txt");
    }

    public Path errorFile(long batchId) {
        return base.resolve("errors").resolve("batch" + batchId + "-errors.txt");
    }
}