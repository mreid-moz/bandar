package com.mozilla.bandar.query.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HDFSProvider implements QueryProvider {
    final String name = "hdfs";
    final String path;

    @Override
    public String getName() {
        return name;
    }

    public HDFSProvider(String path) {
        this.path = path;
    }

    @Override
    public List<String> getQueryNames() {
        List<String> tempQueries = new ArrayList<String>();

        tempQueries.add("browsing not supported... you need to specify the path to a file");

        return Collections.unmodifiableList(tempQueries);
    }

    @Override
    public LocalFileResult getQueryResult(String name) {
        // FIXME: get the file from hdfs.
        return new LocalFileResult(this.path, name);
    }
}
