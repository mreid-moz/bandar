package com.mozilla.bandar.query.core;

import java.util.List;

public interface QueryProvider {
    public String getName();
    public List<String> getQueryNames();
    public LocalFileResult getQueryResult(String name);
}
