package com.mozilla.bandar.query.resources;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cda.CdaCoreService;

import com.mozilla.bandar.query.core.CdaResult;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;

@Path("/cda")
public class CdaResource {
    Logger logger = LoggerFactory.getLogger(CdaResource.class);
    private final CdaCoreService coreService;
    private Map<String,String> paths;

    public CdaResource(Map<String,String> paths) {
        this.paths = paths;
        this.coreService = new CdaCoreService();
    }

    @GET
    @Path("/{name}/{cdafile}.json")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getJson(@PathParam("name") String name, @PathParam("cdafile") String cdaFile) {
        logger.info("getJson");
        return getByType(name, cdaFile, "json");
    }

    @GET
    @Path("/{name}/{cdafile}.xml")
    @Timed
    @Produces(MediaType.APPLICATION_XML)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getXml(@PathParam("name") String name, @PathParam("cdafile") String cdaFile) {
        logger.info("getXml");
        return getByType(name, cdaFile, "xml");
    }

    @GET
    @Path("/{name}/{cdafile}.{outType}")
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public CdaResult getByType(@PathParam("name") String name, @PathParam("cdafile") String cdaFile, @PathParam("outType") String outType) {
        logger.info("getByType");
        return new CdaResult(coreService, this.paths.get(name), cdaFile, outType);
    }
}
