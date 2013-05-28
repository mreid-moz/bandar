package com.mozilla.bandar.query.core;

import java.io.OutputStream;
import java.util.List;

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
    private static final String DEFAULT_DATA_ID = "2";
    private String outputType;
    private MultivaluedMap<String, String> queryParams;
    private String dataId;

    public CdaResult(String cdaFile, String outputType, MultivaluedMap<String, String> queryParams) {
        this(cdaFile, DEFAULT_DATA_ID, outputType, queryParams);
    }

    public CdaResult(String cdaFile, String dataId, String outputType, MultivaluedMap<String, String> queryParams) {
        super(cdaFile);
        this.outputType = outputType;
        this.queryParams = queryParams;
        if (dataId != null && dataId.length() > 0) {
            this.dataId = dataId;
        } else {
            this.dataId = DEFAULT_DATA_ID;
        }
    }

    @Override
    protected void writeCdaResult(CdaEngine engine, CdaSettings cdaSettings, OutputStream outputStream)
            throws DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException,
            UnknownDataAccessException, UnsupportedExporterException, ExporterException, QueryException {

        final QueryOptions queryOptions = new QueryOptions();

        // TODO: if there is only one DataAccessId in the CDA file, just use it.
        queryOptions.setDataAccessId(dataId);

        QueryParamHelper.handle(queryParams, new QueryParamHelper.Handler() {
            @Override
            public void handleSingle(String key, String value) {
                queryOptions.addParameter(key, value);
            }

            @Override
            public void handleMulti(String key, List<String> value) {
                queryOptions.addParameter(key, value);
            }

            @Override
            public void handleEmpty(String key) {
                queryOptions.addParameter(key, null);
            }
        });

        queryOptions.setOutputType(outputType);
        engine.doQuery(outputStream, cdaSettings, queryOptions);
    }
}
