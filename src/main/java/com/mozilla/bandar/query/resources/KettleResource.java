package com.mozilla.bandar.query.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.api.CToolsResponse;
import com.mozilla.bandar.api.CToolsResponseWrapper;
import com.mozilla.bandar.kettle.KettleResult;
import com.mozilla.bandar.query.core.QueryProvider;
import com.yammer.metrics.annotation.Timed;

/**
 * @author mark
 *
 */

@Path("/kettle")
public class KettleResource implements QueryProvider {
    Logger logger = LoggerFactory.getLogger(KettleResource.class);
    private final String environmentPath;

    public KettleResource(String path) throws IOException {
        // Initialize Kettle Environment.  Without it, you get errors like this:
        // Unable to load class for step/plugin with id [GetFileNames].
        // Check if the plugin is available in the plugins subdirectory
        // of the Kettle distribution.
        try {
            logger.debug("Initializing Kettle environment");
            KettleEnvironment.init();
            logger.debug("Finished Initializing Kettle environment");
        } catch (KettleException e) {
            logger.error("Failed to initialize Kettle environment", e);
            throw new WebApplicationException(e);
        }

        environmentPath = new File(path).getCanonicalPath();
        logger.debug("Kettle path: '{}'", environmentPath);
    }

    @GET
    @Path("/{etlfile}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public CToolsResponse getDefaultKettleResult(@PathParam("etlfile") String etlFile, @Context UriInfo ui) throws IOException {
        KettleResult kettleResult = new KettleResult(environmentPath, etlFile, getParams(ui));
        return kettleResult.getResponse();
    }

    // Safely get the query parameters from the specified UriInfo (if possible)
    private MultivaluedMap<String, String> getParams(UriInfo ui) {
        return ui == null ? null : ui.getQueryParameters();
    }

    @GET
    @Path("/{etlfile}.json")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public CToolsResponse getKettleResult(@PathParam("etlfile") String etlFile, @Context UriInfo ui) throws IOException {
        KettleResult kettleResult = new KettleResult(environmentPath, etlFile, getParams(ui));
        return kettleResult.getResponse();
    }

    @GET
    @Path("/{etlfile}.xml")
    @Timed
    @Produces(MediaType.APPLICATION_XML)
    public CToolsResponseWrapper getKettleResultXml(@PathParam("etlfile") String etlFile, @Context UriInfo ui) throws IOException {
        KettleResult kettleResult = new KettleResult(environmentPath, etlFile, getParams(ui));
        // Built-in XML marshaling does not know how to handle java.util.List's :(
        return new CToolsResponseWrapper(kettleResult.getResponse());
    }

    @Override
    public String getName() {
        return "kettle";
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<String> getQueryNames() {
        // TODO: Filter out files starting with underscore (to support subtransformations
        //       without cluttering up the list)
        List<String> tempQueries = new ArrayList<String>();
        File dir = new File(environmentPath);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.canRead()) {
                    String fileName = file.getName();
                    String[] allowedExtensions = new String[]{
                            KettleResult.EtlType.TRANSFORM.getExtension(),
                            KettleResult.EtlType.JOB.getExtension()
                    };
                    for (String extension : allowedExtensions) {
                        if (fileName.endsWith(extension)) {
                            tempQueries.add(fileName.substring(0, fileName.length() - extension.length()));
                            break;
                        }
                    }
                }
            }
        }

        return Collections.unmodifiableList(tempQueries);
    }

    @Override
    public StreamingOutput getQueryResult(String name) {
        return new KettleResult(environmentPath, name, null);
    }
}
