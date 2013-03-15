package com.mozilla.bandar.query.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryList {
    private final List<String> queries;
    
    public QueryList(String path) {
        List<String> tempQueries = new ArrayList<String>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.canRead()) {
                    tempQueries.add(file.getName());
                }
            }
        }
        
        this.queries = Collections.unmodifiableList(tempQueries);
    }
    
    public List<String> getQueries() {
        return queries;
    }
}
