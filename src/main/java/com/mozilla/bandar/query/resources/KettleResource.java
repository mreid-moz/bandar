package com.mozilla.bandar.query.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.api.CToolsResponse;
import com.mozilla.bandar.kettle.KettleResult;
import com.mozilla.bandar.query.core.QueryProvider;
import com.yammer.metrics.annotation.Timed;

/**
 * TODO:
 *  - Make sure we can run a "listFiles" on HDFS
 *  - Track down the extra columns that appear in results (generate-rows.ktr)
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
    @Path("/{etlfile}.json")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public CToolsResponse getKettleResult(@PathParam("etlfile") String etlFile, @Context UriInfo ui) throws IOException {
        KettleResult kettleResult = new KettleResult(environmentPath, etlFile, ui == null ? null : ui.getQueryParameters());
        return kettleResult.getResponse();
    }

    // TODO: split provider out from resource?
    @Override
    public String getName() {
        return "kettle";
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<String> getQueryNames() {
        List<String> names = new ArrayList<String>();
        // TODO: Same thing as LocalFile, but remove the .ktr or .kjb extension
        //       (and filter out files starting with underscore).
        return names;
    }

    @Override
    public StreamingOutput getQueryResult(String name) {
        return new KettleResult(environmentPath, name, null);
    }
}
