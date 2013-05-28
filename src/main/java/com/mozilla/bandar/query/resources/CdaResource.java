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
    public static final String DATA_ID_PATTERN = "{dataid : (/[0-9]+)?}";
    Logger logger = LoggerFactory.getLogger(CdaResource.class);

    public CdaResource() {
    }

    @GET
    @Path("/{cdafile}.json" + DATA_ID_PATTERN)
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getJson(@PathParam("cdafile") String cdaFile, @PathParam("dataid") String dataId, @Context UriInfo ui) {
//        logger.info("getJson");
        return getByType(cdaFile, dataId, "json", ui);
    }

    @GET
    @Path("/{cdafile}.xml" + DATA_ID_PATTERN)
    @Timed
    @Produces(MediaType.APPLICATION_XML)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getXml(@PathParam("cdafile") String cdaFile, @PathParam("dataid") String dataId, @Context UriInfo ui) {
//        logger.info("getXml");
        return getByType(cdaFile, dataId, "xml", ui);
    }

    @GET
    @Path("/{cdafile}.{outType}" + DATA_ID_PATTERN)
    @Timed
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getByType(@PathParam("cdafile") String cdaFile, @PathParam("dataid") String dataId, @PathParam("outType") String outType, @Context UriInfo ui) {
        CdaResult result = null;

        try {
            result = new CdaResult(cdaFile, cleanDataId(dataId), outType, ui == null ? null : ui.getQueryParameters());
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

    // Remove the leading slash if present.
    private String cleanDataId(String dataId) {
        String cleanDataId = dataId;
        if (dataId != null && dataId.indexOf("/") == 0) {
            cleanDataId = dataId.substring(1);
        }

        return cleanDataId;
    }

    @GET
    @Path("/{cdafile}" + DATA_ID_PATTERN)
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getDefault(@PathParam("cdafile") String cdaFile, @PathParam("dataid") String dataId, @Context UriInfo ui) {
        return getJson(cdaFile, dataId, ui);
    }

    @GET
    @Path("/{cdafile}/parameters")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public StreamingOutput getParameters(@PathParam("cdafile") String cdaFile) {
        // TODO: /{cdafile}/{dataid}/parameters
        //       since different data ids can have different parameters
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
        return getDefault(name, null, null);
    }
}
