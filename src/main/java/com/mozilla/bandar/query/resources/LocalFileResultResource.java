package com.mozilla.bandar.query.resources;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.mozilla.bandar.query.core.LocalFileProvider;
import com.mozilla.bandar.query.core.LocalFileResult;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;

@Path("/query/file/{name}")
@Produces(MediaType.TEXT_PLAIN)
public class LocalFileResultResource {
    private final LocalFileProvider provider;

    public LocalFileResultResource(LocalFileProvider provider) {
        this.provider = provider;
    }

    @GET
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public Response getQueryResult(@PathParam("name") String name) {
        LocalFileResult result = provider.getQueryResult(name);
        if (result.exists()) {
            if (result.canRead()) {
                return Response.ok(result).build();
            } else {
                return Response.status(Status.FORBIDDEN).build();
            }
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
