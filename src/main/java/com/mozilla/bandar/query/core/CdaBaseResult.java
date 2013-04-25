package com.mozilla.bandar.query.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.dom4j.DocumentException;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.connections.UnsupportedConnectionException;
import pt.webdetails.cda.dataaccess.QueryException;
import pt.webdetails.cda.dataaccess.UnsupportedDataAccessException;
import pt.webdetails.cda.exporter.ExporterException;
import pt.webdetails.cda.exporter.UnsupportedExporterException;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;
import pt.webdetails.cda.settings.UnknownDataAccessException;

public abstract class CdaBaseResult implements StreamingOutput {
    protected String cdaFile;

    public CdaBaseResult(String cdaFile) {
        this.cdaFile = cdaFile;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        boolean causedException = true;
        Throwable exception = null;

        try {
            final CdaEngine engine = CdaEngine.getInstance();
            CdaSettings cdaSettings = null;
            if (cdaFile != null)
                cdaSettings = SettingsManager.getInstance().parseSettingsFile(cdaFile + ".cda");
            writeCdaResult(engine, cdaSettings, outputStream);
            causedException = false;
        } catch (UnknownDataAccessException e) {
            exception = e;
        } catch (UnsupportedExporterException e) {
            exception = e;
        } catch (ExporterException e) {
            exception = e;
        } catch (DocumentException e) {
            exception = e;
        } catch (UnsupportedConnectionException e) {
            exception = e;
        } catch (UnsupportedDataAccessException e) {
            exception = e;
        } catch (QueryException e) {
            exception = e;
        }

        if (causedException) {
            throw new WebApplicationException(exception);
        }
    }

    protected abstract void writeCdaResult(CdaEngine engine, CdaSettings cdaSettings, OutputStream outputStream)
            throws DocumentException, UnsupportedConnectionException,
            UnsupportedDataAccessException, UnknownDataAccessException,
            UnsupportedExporterException, ExporterException, QueryException;
}
