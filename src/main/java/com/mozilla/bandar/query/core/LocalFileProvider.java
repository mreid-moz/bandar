package com.mozilla.bandar.query.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalFileProvider implements QueryProvider {
    final String name = "file";
    final String path;

    @Override
    public String getName() {
        return name;
    }

    public LocalFileProvider(String path) {
        this.path = path;
    }

    @Override
    public List<String> getQueryNames() {
        List<String> tempQueries = new ArrayList<String>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.canRead()) {
                    tempQueries.add(file.getName());
                }
            }
        }

        return Collections.unmodifiableList(tempQueries);
    }

    @Override
    public LocalFileResult getQueryResult(String name) {
        return new LocalFileResult(this.path, name);
    }
}
