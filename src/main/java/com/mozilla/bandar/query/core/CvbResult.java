package com.mozilla.bandar.query.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;

import pt.webdetails.cpf.http.CommonParameterProvider;
import pt.webdetails.cpf.http.ICommonParameterProvider;
import pt.webdetails.cpk.CpkCoreService;

public class CvbResult implements StreamingOutput {
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
        ICommonParameterProvider requestProvider = new CommonParameterProvider();
        pathProvider.put("path", kettleFile);//kjb or ktr
        pathProvider.put("outputstream", output);
        pathProvider.put("httpresponse", null);
        requestProvider.put("request","unnecessary value?");
        // TODO: this should come from CvbResource (or somewhere).
        String uriKey = "URI";
        if (queryParams != null && queryParams.containsKey(uriKey)) {
            requestProvider.put("param" + uriKey, queryParams.getFirst(uriKey));
        }

        providers.put("path", pathProvider);
        providers.put("request", requestProvider);
        try {
            service.createContent(providers);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }
}
