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

import pt.webdetails.cpk.CpkCoreService;
import pt.webdetails.cpk.ICpkEnvironment;
import pt.webdetails.cpk.elements.IElement;

import com.mozilla.bandar.cvb.DummyCpkEnvironment;
import com.mozilla.bandar.query.core.CvbResult;
import com.mozilla.bandar.query.core.QueryProvider;
import com.yammer.metrics.annotation.Timed;

@Path("/cvb")
public class CvbResource implements QueryProvider {
    Logger logger = LoggerFactory.getLogger(CvbResource.class);
    private final String environmentPath;
    private CpkCoreService service;

    public CvbResource(String path) throws IOException {
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
        logger.debug("CVB path: '{}'", environmentPath);
        ICpkEnvironment environment = new DummyCpkEnvironment(environmentPath);
        service = new CpkCoreService(environment);
    }

    @GET
    @Path("/{cvbfile}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public StreamingOutput getKettleResult(@PathParam("cvbfile") String cvbFile, @Context UriInfo ui) {
        return new CvbResult(service, "/" + cvbFile, "json", ui == null ? null : ui.getQueryParameters());
    }

    // TODO: split provider out from resource?
    @Override
    public String getName() {
        return "cvb";
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<String> getQueryNames() {
        IElement[] elements = service.getElements();
        List<String> names = new ArrayList<String>();
        if (elements != null) {
            for (IElement element : elements) {
                names.add(element.getId());
            }
        }
        return names;
    }

    @Override
    public StreamingOutput getQueryResult(String name) {
        return getKettleResult(name, null);
    }
}
