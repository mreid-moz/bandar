package com.mozilla.bandar.query.core;

import java.io.OutputStream;

import org.dom4j.DocumentException;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.QueryException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.discovery.DiscoveryOptions;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.UnknownDataAccessException;

public class CdaParameterList extends CdaBaseResult {

    public CdaParameterList(String cdaFile) {
        super(cdaFile);
    }

    @Override
    protected void writeCdaResult(CdaEngine engine, CdaSettings cdaSettings, OutputStream outputStream)
            throws DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException,
            UnknownDataAccessException, UnsupportedExporterException, ExporterException, QueryException {
        final DiscoveryOptions discoveryOptions = new DiscoveryOptions();
        discoveryOptions.setOutputType("json"); // FIXME: parameterize?
        discoveryOptions.setDataAccessId("2");

        engine.listParameters(outputStream, cdaSettings, discoveryOptions);
    }
}
