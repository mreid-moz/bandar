package com.mozilla.bandar.query.core;

import java.io.OutputStream;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Logger logger = LoggerFactory.getLogger(CdaFileList.class);
    public CdaFileList() {
        super(null);
    }

    @Override
    protected void writeCdaResult(CdaEngine engine, CdaSettings cdaSettings, OutputStream outputStream)
            throws DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException,
            UnknownDataAccessException, UnsupportedExporterException, ExporterException, QueryException {

        ICdaEnvironment environment = CdaEngine.getEnvironment();
//        IRepositoryAccess repo = environment.getRepositoryAccess();
//        String fileExtensions = "cda";
//        String dir = "file:///home/mark/mozilla/github/bandar/src/test/resources/cda/repo";
//        IRepositoryFile repositoryFile = repo.getRepositoryFile(dir, FileAccess.READ);
//        if (repositoryFile.isDirectory()) {
//            for (IRepositoryFile file : repositoryFile.listFiles()) {
//                logger.info("FOUND A CDA FILE: " + file.getFileName());
//            }
//        } else {
//            logger.warn("Base Repo is not a directory :(");
//        }


        final DiscoveryOptions discoveryOptions = new DiscoveryOptions();
        discoveryOptions.setOutputType("json"); // FIXME: parameterize?
        ISessionUtils sessionUtils = environment.getSessionUtils();
        engine.getCdaList(outputStream, discoveryOptions, sessionUtils.getCurrentSession());
    }
}
