package com.mozilla.bandar.query.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryList {
    private final Map<String,List<String>> queries;

    public QueryList(List<QueryProvider> providers) {

        Map<String,List<String>> tempQueries = new HashMap<String,List<String>>();
        for (QueryProvider provider : providers) {
            tempQueries.put(provider.getName(), provider.getQueryNames());
        }

        this.queries = Collections.unmodifiableMap(tempQueries);
    }

    public Map<String,List<String>> getQueries() {
        return queries;
    }
}
