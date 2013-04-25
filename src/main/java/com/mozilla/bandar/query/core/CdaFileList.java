package com.mozilla.bandar.query.core;

import java.io.OutputStream;

import org.dom4j.DocumentException;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.ICdaEnvironment;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.QueryException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.discovery.DiscoveryOptions;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.UnknownDataAccessException;
import pt.webdetails.cpf.session.ISessionUtils;

public class CdaFileList extends CdaBaseResult {
    public CdaFileList() {
        super(null);
    }

    @Override
    protected void writeCdaResult(CdaEngine engine, CdaSettings cdaSettings, OutputStream outputStream)
            throws DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException,
            UnknownDataAccessException, UnsupportedExporterException, ExporterException, QueryException {
        final DiscoveryOptions discoveryOptions = new DiscoveryOptions();
        discoveryOptions.setOutputType("json"); // FIXME: parameterize?
        ICdaEnvironment environment = CdaEngine.getEnvironment();
        ISessionUtils sessionUtils = environment.getSessionUtils();
        engine.getCdaList(outputStream, discoveryOptions, sessionUtils.getCurrentSession());
    }
}
