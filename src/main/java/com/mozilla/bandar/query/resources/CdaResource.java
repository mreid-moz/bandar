package com.mozilla.bandar.query.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.query.core.CdaFileList;
import com.mozilla.bandar.query.core.CdaParameterList;
import com.mozilla.bandar.query.core.CdaQueryList;
import com.mozilla.bandar.query.core.CdaResult;
import com.mozilla.bandar.query.core.QueryProvider;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;

@Path("/cda")
public class CdaResource implements QueryProvider {
    Logger logger = LoggerFactory.getLogger(CdaResource.class);

    public CdaResource() {
    }

    @GET
    @Path("/{cdafile}.json")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getJson(@PathParam("cdafile") String cdaFile, @Context UriInfo ui) {
//        logger.info("getJson");
        return getByType(cdaFile, "json", ui);
    }

    @GET
    @Path("/{cdafile}.xml")
    @Timed
    @Produces(MediaType.APPLICATION_XML)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getXml(@PathParam("cdafile") String cdaFile, @Context UriInfo ui) {
//        logger.info("getXml");
        return getByType(cdaFile, "xml", ui);
    }

    @GET
    @Path("/{cdafile}.{outType}")
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getByType(@PathParam("cdafile") String cdaFile, @PathParam("outType") String outType, @Context UriInfo ui) {
        CdaResult result = null;
//        logger.info("getByType");
        try {
            result = new CdaResult(cdaFile, outType, ui == null ? null : ui.getQueryParameters());
        } catch (RuntimeException e) {
            List<String> cdaFiles = getQueryNames();
            if (!cdaFiles.contains(cdaFile)) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else {
                throw new WebApplicationException(e);
            }
        }

        return result;
    }

    @GET
    @Path("/{cdafile}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getDefault(@PathParam("cdafile") String cdaFile, @Context UriInfo ui) {
        return getJson(cdaFile, ui);
    }

    @GET
    @Path("/{cdafile}/parameters")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getParameters(@PathParam("cdafile") String cdaFile) {
        // TODO: /{cdafile}/parameters.{outType}?
        return new CdaParameterList(cdaFile);
    }
    @GET
    @Path("/{cdafile}/queries")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public StreamingOutput getQueries(@PathParam("cdafile") String cdaFile) {
        return new CdaQueryList(cdaFile);
    }

    // TODO: do we need these?
    //  - wrap/unwrap
    //  - getCdaFile
    //  - writeCdaFile
    //  - clearCache / cacheMonitor / cacheController / manageCache (Task?)
    //  - editFile
    //  - previewQuery
    //  - getCssResource
    //  - getJsResource
    //  - listDataAccessTypes

    @GET
//    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getCdaFiles() {
        CdaFileList list = new CdaFileList();
        return list.get();
    }

    // TODO: split provider out from resource?
    @Override
    public String getName() {
        return "cda";
    }

    @Override
    public List<String> getQueryNames() {
        return getCdaFiles();
    }

    @Override
    public StreamingOutput getQueryResult(String name) {
        return getDefault(name, null);
    }
}
