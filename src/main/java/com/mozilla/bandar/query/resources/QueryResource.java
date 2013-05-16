package com.mozilla.bandar.query.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mozilla.bandar.query.core.QueryList;
import com.mozilla.bandar.query.core.QueryProvider;
import com.yammer.metrics.annotation.Timed;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {
    private final List<QueryProvider> providers;

    public QueryResource(QueryProvider... providers) {
        this.providers = new ArrayList<QueryProvider>();
        for (QueryProvider provider : providers) {
            this.providers.add(provider);
        }
    }

    public void addProvider(QueryProvider provider) {
        this.providers.add(provider);
    }

    @GET
    @Timed
    public Map<String,List<String>> getAvailableQueries() {
        return new QueryList(providers).getQueries();
    }
}
