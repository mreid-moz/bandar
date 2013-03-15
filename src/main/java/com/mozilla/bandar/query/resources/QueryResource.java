package com.mozilla.bandar.query.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.mozilla.bandar.query.core.QueryList;
import com.yammer.metrics.annotation.Timed;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {
    private final String basePath;
    
    public QueryResource(String basePath) {
        this.basePath = basePath;
    }
    
    @GET
    @Timed
    public QueryList getAvailableQueries(@QueryParam("filetype") Optional<String> filetype) {
        return new QueryList(basePath);
    }
}
