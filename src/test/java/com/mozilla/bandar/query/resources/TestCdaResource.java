package com.mozilla.bandar.query.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.QueryException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.discovery.DiscoveryOptions;
import pt.webdetails.cda.exporter.CsvExporter;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;
import pt.webdetails.cda.settings.UnknownDataAccessException;

import com.mozilla.bandar.query.core.CdaResult;
import com.mozilla.bandar.query.core.LocalFileProvider;

public class TestCdaResource {
    String cdaName = "sample";
    String cdaPath = "/home/mark/mozilla/github/bandar/src/test/resources/cda";
    Map<String,String> cdaPathMap = new HashMap<String,String>(1);
    String cdaFile = "sample-kettle";
    Logger logger = LoggerFactory.getLogger(TestCdaResource.class);
    LocalFileProvider provider;

    @Before
    public void setUp() {
        cdaPathMap.put(cdaName, cdaPath);
    }

    @Test
    public void test() throws WebApplicationException, IOException {
        CdaResource cdaResource = new CdaResource(cdaPathMap);
        List<String> types = Arrays.asList("json", "xml", "html", "csv");
        for (String type : types)
        {
            CdaResult result;
            if ("json".equals(type))
                result = cdaResource.getJson(cdaName, cdaFile);
            else
                result = cdaResource.getByType(cdaName, cdaFile, type);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            result.write(output);

            String nice = output.toString("UTF-8");
            System.err.println("Found " + nice.length() + " characters for type " + type + ":");
            System.err.println(nice);

        }
    }

//    @Test
    public void test2() throws URISyntaxException, DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException, UnknownDataAccessException, QueryException, UnsupportedExporterException, ExporterException {

        // Define an outputStream
        OutputStream out = System.out;

        logger.info("Building CDA settings from sample file");

        final SettingsManager settingsManager = SettingsManager.getInstance();
        File settingsFile = new File(cdaPath + "/sample-sql.cda");
        final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
        logger.debug("Doing query on Cda - Initializing CdaEngine");
        final CdaEngine engine = CdaEngine.getInstance();

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setDataAccessId("1");
        queryOptions.addParameter("orderDate", "2003-04-01");
        // queryOptions.addParameter("status","In Process");

        logger.info("Doing csv export");
        queryOptions.setOutputType("csv");
        queryOptions.addSetting(CsvExporter.CSV_SEPARATOR_SETTING, ",");
        engine.doQuery(out, cdaSettings, queryOptions);

        logger.info("Doing xml export");
        queryOptions.setOutputType("xml");
        engine.doQuery(out, cdaSettings, queryOptions);

        logger.info("Doing json export");
        queryOptions.setOutputType("json");
        engine.doQuery(out, cdaSettings, queryOptions);

        logger.info("Doing xsl export");
        queryOptions.setOutputType("xls");
        engine.doQuery(out, cdaSettings, queryOptions);
    }

//    @Test
    public void test3() throws DocumentException, UnsupportedConnectionException, UnsupportedDataAccessException, UnknownDataAccessException, QueryException, UnsupportedExporterException, ExporterException {

        // Define an outputStream
        OutputStream out = System.out;

        logger.info("Building CDA settings from sample file");

        final SettingsManager settingsManager = SettingsManager.getInstance();
        File settingsFile = new File(cdaPath + "/sample-kettle.cda");
        final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
        logger.debug("Doing query on Cda - Initializing CdaEngine");
        final CdaEngine engine = CdaEngine.getInstance();

        final QueryOptions queryOptions = new QueryOptions();
        queryOptions.setDataAccessId("2");
        queryOptions.setOutputType("json");
        queryOptions.addParameter("myRadius", "10");
        queryOptions.addParameter("ZipCode", "90210");

        final DiscoveryOptions discoveryOptions = new DiscoveryOptions();
        discoveryOptions.setOutputType("json");
        discoveryOptions.setDataAccessId("2");

        engine.listParameters(out, cdaSettings, discoveryOptions);
        engine.listQueries(out, cdaSettings, discoveryOptions);

        logger.info("Doing query");
        engine.doQuery(out, cdaSettings, queryOptions);
    }
}
