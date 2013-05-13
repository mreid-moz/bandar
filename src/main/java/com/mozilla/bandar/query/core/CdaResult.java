package com.mozilla.bandar.query.core;

import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import org.dom4j.DocumentException;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.QueryException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.UnknownDataAccessException;

public class CdaResult extends CdaBaseResult {
    private String outputType;
    private MultivaluedMap<String, String> queryParams;

    public CdaResult(String cdaFile, String outputType, MultivaluedMap<String, String> queryParams) {
        super(cdaFile);
        this.outputType = outputType;
        this.queryParams = queryParams;
    }

    @Override
    protected void writeCdaResult(CdaEngine engine, CdaSettings cdaSettings, OutputStream outputStream)
            throws DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException,
            UnknownDataAccessException, UnsupportedExporterException, ExporterException, QueryException {
        /*
        // TODO: write out response.
        try {
            coreService.getCdaFile(outputStream, path, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         */

        final QueryOptions queryOptions = new QueryOptions();
        queryOptions.setDataAccessId("2");

        if (queryParams != null) {
            for (Entry<String, List<String>> param : queryParams.entrySet()) {
                String key = param.getKey();
                List<String> value = param.getValue();
                if (value != null) {
                    if (value.size() == 1) {
                        queryOptions.addParameter(key, value.get(0));
                    } else if (value.size() > 1) {
                        queryOptions.addParameter(key, value);
                    } else {
                        // Empty list
                        queryOptions.addParameter(key, null);
                    }
                }
            }
        }

        queryOptions.setOutputType(outputType);
        engine.doQuery(outputStream, cdaSettings, queryOptions);
    }
}
