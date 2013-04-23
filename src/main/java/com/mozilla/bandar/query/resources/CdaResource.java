package com.mozilla.bandar.query.resources;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public CdaResult getJson(@PathParam("cdafile") String cdaFile) {
        logger.info("getJson");
        return getByType(cdaFile, "json");
    }

    @GET
    @Path("/{cdafile}.xml")
    @Timed
    @Produces(MediaType.APPLICATION_XML)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getXml(@PathParam("cdafile") String cdaFile) {
        logger.info("getXml");
        return getByType(cdaFile, "xml");
    }

    @GET
    @Path("/{cdafile}.{outType}")
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getByType(@PathParam("cdafile") String cdaFile, @PathParam("outType") String outType) {
        logger.info("getByType");
        return new CdaResult(cdaFile, outType);
    }

}
