package com.mozilla.bandar.query.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cda.CdaCoreService;
import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.QueryException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.exporter.Exporter;
import pt.webdetails.cda.exporter.ExporterEngine;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;
import pt.webdetails.cda.settings.UnknownDataAccessException;
import pt.webdetails.cpf.utils.Utils;

public class CdaResult implements StreamingOutput {
    Logger logger = LoggerFactory.getLogger(CdaResult.class);
    private final CdaCoreService coreService;
    private String path;
    private String cdaFile;
    private String outputType;

    public CdaResult(CdaCoreService coreService, String path, String cdaFile, String outputType) {
        this.coreService = coreService;
        this.path = path;
        this.cdaFile = cdaFile;
        this.outputType = outputType;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException,
                                                        WebApplicationException {
        /*
        // TODO: write out response.
        try {
            coreService.getCdaFile(outputStream, path, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         */

        try {
            final CdaEngine engine = CdaEngine.getInstance();
            final QueryOptions queryOptions = new QueryOptions();

            queryOptions.setDataAccessId("2");

            final CdaSettings cdaSettings = SettingsManager.getInstance().parseSettingsFile(path + "/" + cdaFile + ".cda");


            // Handle the query itself and its output format...
            queryOptions.setOutputType(outputType);

            Exporter exporter = ExporterEngine.getInstance().getExporter(queryOptions.getOutputType(), queryOptions.getExtraSettings());

            String attachmentName = exporter.getAttachmentName();
            String mimeType = (attachmentName == null) ? null : Utils.getMimeType(attachmentName);
            if(StringUtils.isEmpty(mimeType)){
                mimeType = exporter.getMimeType();
            }

            // Finally, pass the query to the engine
            engine.doQuery(outputStream, cdaSettings, queryOptions);
        } catch (UnknownDataAccessException e) {
            e.printStackTrace();
        } catch (QueryException e) {
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
