package com.mozilla.bandar.query.resources;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.query.core.CdaParams;
import com.mozilla.bandar.query.core.CdaResult;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;

@Path("/cda")
public class CdaResource {
    Logger logger = LoggerFactory.getLogger(CdaResource.class);

    public CdaResource() {
    }

    @GET
    @Path("/{cdafile}.json")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getJson(@PathParam("cdafile") String cdaFile, @Context UriInfo ui) {
        logger.info("getJson");
        return getByType(cdaFile, "json", ui);
    }

    @GET
    @Path("/{cdafile}.xml")
    @Timed
    @Produces(MediaType.APPLICATION_XML)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getXml(@PathParam("cdafile") String cdaFile, @Context UriInfo ui) {
        logger.info("getXml");
        return getByType(cdaFile, "xml", ui);
    }

    @GET
    @Path("/{cdafile}.{outType}")
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getByType(@PathParam("cdafile") String cdaFile, @PathParam("outType") String outType, @Context UriInfo ui) {
        logger.info("getByType");
        return new CdaResult(cdaFile, outType, ui.getQueryParameters());
    }

    @GET
    @Path("/{cdafile}/parameters")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaParams getParameters(@PathParam("cdafile") String cdaFile) {
        // TODO: /{cdafile}/parameters.{outType}?
        return new CdaParams(cdaFile);
    }

}
