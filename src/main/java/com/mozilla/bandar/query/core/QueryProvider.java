package com.mozilla.bandar.query.core;

import java.util.List;

import javax.ws.rs.core.StreamingOutput;

public interface QueryProvider {
    public String getName();
    public List<String> getQueryNames();
    public StreamingOutput getQueryResult(String name);
}
