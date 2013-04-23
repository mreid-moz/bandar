package com.mozilla.bandar.query.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.discovery.DiscoveryOptions;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;
import pt.webdetails.cda.settings.UnknownDataAccessException;

public class CdaParams implements StreamingOutput {
    Logger logger = LoggerFactory.getLogger(CdaParams.class);
    private String cdaFile;

    public CdaParams(String cdaFile) {
        this.cdaFile = cdaFile;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException,
                                                        WebApplicationException {
        try {

            final CdaEngine engine = CdaEngine.getInstance();
//            final QueryOptions queryOptions = new QueryOptions();
//            queryOptions.setOutputType("json"); // FIXME: parameterize?
//            queryOptions.setDataAccessId("2");

//            IRepositoryAccess repAccess = CdaEngine.getEnvironment().getRepositoryAccess();
            final CdaSettings cdaSettings = SettingsManager.getInstance().parseSettingsFile(cdaFile + ".cda");

            // Handle the query itself and its output format...
            final DiscoveryOptions discoveryOptions = new DiscoveryOptions();
            discoveryOptions.setOutputType("json"); // FIXME: parameterize?
            discoveryOptions.setDataAccessId("2");


            engine.listParameters(outputStream, cdaSettings, discoveryOptions);
        } catch (UnknownDataAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedExporterException e) {
            e.printStackTrace();
        } catch (ExporterException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (UnsupportedConnectionException e) {
            e.printStackTrace();
        } catch (UnsupportedDataAccessException e) {
            e.printStackTrace();
        }
    }
}
