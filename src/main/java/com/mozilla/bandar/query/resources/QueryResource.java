package com.mozilla.bandar.query.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.mozilla.bandar.query.core.QueryList;
import com.mozilla.bandar.query.core.QueryProvider;
import com.yammer.metrics.annotation.Timed;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {
    private final List<QueryProvider> providers;

    public QueryResource(QueryProvider... providers) {
        List<QueryProvider> tempProviders = new ArrayList<QueryProvider>();
        for (QueryProvider provider : providers) {
            tempProviders.add(provider);
        }
        this.providers = Collections.unmodifiableList(tempProviders);
    }

    @GET
    @Timed
    public Map<String,List<String>> getAvailableQueries(@QueryParam("filetype") Optional<String> filetype) {
        return new QueryList(providers).getQueries();
    }
}
