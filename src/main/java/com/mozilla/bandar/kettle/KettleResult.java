package com.mozilla.bandar.kettle;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.StepInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.api.CToolsResponse;

public class KettleResult implements StreamingOutput {
    private static final String OUTPUT_STEP_NAME = "OUTPUT";
    Logger logger = LoggerFactory.getLogger(KettleResult.class);
    public enum EtlType {
        TRANSFORM(".ktr"),
        JOB(".kjb");

        private String extension;

        EtlType(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }

    EtlType type;
    String etlFileName;
    MultivaluedMap<String, String> parameters;

    public KettleResult(String path, String etlFile, MultivaluedMap<String, String> parameters) {
        String fileBase = path + File.separatorChar + etlFile;

        this.type = EtlType.TRANSFORM;
        this.etlFileName = fileBase + EtlType.TRANSFORM.getExtension();
        logger.debug("Checking '{}'", this.etlFileName);
        if (!new File(etlFileName).exists()) {
            this.type = EtlType.JOB;
            this.etlFileName = fileBase + EtlType.JOB.getExtension();
            logger.debug("Checking '{}'", this.etlFileName);
            if (!new File(etlFileName).exists()) {
                throw new WebApplicationException(Status.NOT_FOUND);
            }
        }

        this.parameters = parameters;
    }

    @Override
    public void write(OutputStream output) throws IOException {
        CToolsResponse response = getResponse();

        String responseJson = asJson(response);
        output.write(responseJson.getBytes());
    }

    public CToolsResponse getResponse() throws IOException {
        try {
            TransMeta transformationMeta = new TransMeta(etlFileName);

            if (parameters != null && parameters.size() > 0) {
                for (String paramName : parameters.keySet()) {
                    List<String> list = parameters.get(paramName);
                    // TODO: if there are multiple parameters incoming, join them?
                    if (list != null && list.size() > 0) {
                        try {
                            transformationMeta.setParameterValue(paramName, list.get(0));
                        } catch (UnknownParamException e) {
                            logger.warn("Attempt to set unknown parameter '{}' in ETL '{}'", paramName, this.etlFileName);
                        }
                        if (list.size() > 1) {
                            logger.info("Found {} values for {}, only using the first one.", list.size(), paramName);
                        }
                    }
                }
            }

            // TODO: do we need this?
            //            transformation.copyParametersFrom(transformation.getTransMeta());

            // TODO: wat?
            transformationMeta.setVariable("pentahoUsername", "Bandar");

            // TODO: roles?
            //            String[] authorities = userSession.getAuthorities();
            //            if (authorities != null && authorities.length > 0) {
            //                transformation.getTransMeta().setVariable("pentahoRoles", StringUtils.join(authorities, ","));
            //            }

            Trans transformation = new Trans(transformationMeta);

            // TODO: do we need this?
            //            transformation.copyVariablesFrom(transformation.getTransMeta());

            transformation.activateParameters();

            transformation.prepareExecution(null);

            StepInterface step = transformation.findRunThread(OUTPUT_STEP_NAME);
            transformation.startThreads();

            // TODO: write out a preamble / header

            final List<CToolsResponse.MetaData> meta = new ArrayList<CToolsResponse.MetaData>();
            final List<List<Object>> resultSet = new ArrayList<List<Object>>();

            if (step != null) {
                step.addRowListener(new RowAdapter() {
                    @Override
                    public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row) throws KettleStepException {
                        if (meta.size() == 0) {
                            String[] fieldNames = rowMeta.getFieldNames();
                            for (int i = 0; i < fieldNames.length; i++) {
                                String fieldType = rowMeta.getValueMeta(i).getTypeDesc();
                                meta.add(new CToolsResponse.MetaData(fieldNames[i], fieldType, i));
                            }
                            //                            fieldCount = fieldNames.length;
                        }
                        // TODO: write out one row
                        List<Object> rowOut = new ArrayList<Object>(meta.size());
                        for (int i = 0; i < meta.size(); i++) {
                            rowOut.add(row[i]);
                        }
                        resultSet.add(rowOut);
                    }
                });
            }
            //            setMimeType(transformation.getVariable("mimeType"), transformation.getParameterValue("mimeType"));

            transformation.waitUntilFinished();

            Result result = transformation.getResult();

            // Output the overall result if we didn't find a specific output step above.
            if (step == null) {
                List<RowMetaAndData> rows = result.getRows();
                if (rows != null) {
                    for (RowMetaAndData row : rows) {
                        if (meta.size() == 0) {
                            RowMetaInterface rowMeta = row.getRowMeta();
                            String[] fieldNames = rowMeta.getFieldNames();
                            for (int i = 0; i < fieldNames.length; i++) {
                                String fieldType = rowMeta.getValueMeta(i).getTypeDesc();
                                meta.add(new CToolsResponse.MetaData(fieldNames[i], fieldType, i));
                            }
                        }
                        // TODO: write out one row
                        List<Object> rowOut = new ArrayList<Object>(meta.size());
                        Object[] data = row.getData();
                        for (int i = 0; i < meta.size(); i++) {
                            rowOut.add(data[i]);
                        }
                        resultSet.add(rowOut);
                    }
                }
            }

            Map<String,String> queryInfo = new HashMap<String,String>();
            queryInfo.put("totalRows", String.valueOf(resultSet.size()));

            CToolsResponse response = new CToolsResponse(meta, queryInfo, resultSet);
            return response;
        } catch (KettleXMLException e) {
            relayKettleError(e, "Kettle XML Error");
        } catch (KettleException e) {
            relayKettleError(e, "Kettle Exception");
        }
        return null;
    }

    private void relayKettleError(Exception e, String label) {
        logger.error("{} processing '{}'", label, etlFileName);
        logger.error(label, e);
        throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
}


