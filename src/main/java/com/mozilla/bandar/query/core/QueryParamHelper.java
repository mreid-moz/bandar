package com.mozilla.bandar.query.core;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

public class QueryParamHelper {

    public interface Handler {
        public void handleSingle(String key, String value);
        public void handleMulti(String key, List<String> value);
        public void handleEmpty(String key);
    }

    public static void handle(MultivaluedMap<String, String> queryParams, Handler handler) {
        if (queryParams != null) {
            for (Entry<String, List<String>> param : queryParams.entrySet()) {
                String key = param.getKey();
                List<String> value = param.getValue();
                if (value != null) {
                    if (value.size() == 1) {
                        handler.handleSingle(key, value.get(0));
                    } else if (value.size() > 1) {
                        handler.handleMulti(key, value);
                    } else {
                        // Empty list
                        handler.handleEmpty(key);
                    }
                }
            }
        }
    }
}
