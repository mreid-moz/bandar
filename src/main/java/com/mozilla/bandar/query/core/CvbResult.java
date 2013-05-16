package com.mozilla.bandar.query.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cpf.http.CommonParameterProvider;
import pt.webdetails.cpf.http.ICommonParameterProvider;
import pt.webdetails.cpk.CpkCoreService;

public class CvbResult implements StreamingOutput {
    Logger logger = LoggerFactory.getLogger(CvbResult.class);

    private CpkCoreService service;
    private String kettleFile;
    private String outputType;
    private MultivaluedMap<String, String> queryParams;

    public CvbResult(CpkCoreService service, String kettleFile, String outputType, MultivaluedMap<String, String> queryParams) {
        this.service = service;
        this.kettleFile = kettleFile;
        this.outputType = outputType;
        this.queryParams = queryParams;
    }

    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {
        Map<String, ICommonParameterProvider> providers = new HashMap<String, ICommonParameterProvider>();
        ICommonParameterProvider pathProvider = new CommonParameterProvider();
        final ICommonParameterProvider requestProvider = new CommonParameterProvider();
        pathProvider.put("path", kettleFile);//kjb or ktr
        pathProvider.put("outputstream", output);
        pathProvider.put("httpresponse", null);
        requestProvider.put("request", "unnecessary value?");

        QueryParamHelper.handle(queryParams, new QueryParamHelper.Handler() {
            @Override
            public void handleSingle(String key, String value) {
                logger.debug("Setting {} to '{}'", key, value);
                requestProvider.put("param" + key, value);
            }

            @Override
            public void handleMulti(String key, List<String> value) {
                requestProvider.put("param" + key, value.toArray());
            }

            @Override
            public void handleEmpty(String key) {
                requestProvider.put("param" + key, null);
            }
        });

        providers.put("path", pathProvider);
        providers.put("request", requestProvider);
        try {
            service.createContent(providers);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }
}
