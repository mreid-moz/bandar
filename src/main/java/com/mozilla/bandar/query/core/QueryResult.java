package com.mozilla.bandar.query.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import com.google.common.io.Files;

public class QueryResult implements StreamingOutput {
    private final File sourceFile;
    
    public QueryResult(String path, String filename) {
        this.sourceFile = new File(path + File.separator + filename);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException,
            WebApplicationException {
        Files.copy(sourceFile, outputStream);
    }
    
    public boolean canRead() {
        return sourceFile.canRead();
    }
    
    public boolean exists() {
        return sourceFile.exists();
    }
}
