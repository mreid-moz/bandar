package com.mozilla.bandar.query.resources;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mozilla.bandar.query.core.LocalFileProvider;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;

@Path("/query/file")
@Produces(MediaType.APPLICATION_JSON)
public class LocalFileResource {
    private final LocalFileProvider provider;

    public LocalFileResource(LocalFileProvider provider) {
        this.provider = provider;
    }

    @GET
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public Response getQueryResult() {
        return Response.ok(provider.getQueryNames()).build();
    }
}
