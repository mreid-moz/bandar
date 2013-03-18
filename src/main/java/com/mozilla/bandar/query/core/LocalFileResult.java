package com.mozilla.bandar.query.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class LocalFileResult implements StreamingOutput {
    private final File sourceFile;
    private final boolean isValidChild;

    Logger logger = LoggerFactory.getLogger(LocalFileResult.class);

    public LocalFileResult(String path, String filename) {
        this.sourceFile = new File(path + File.separator + filename);

        // Make sure we're chrooted.
        // Example: http://localhost:8080/query/file/..%2Ftest
        // This gives you "path/../test"
        boolean insideChroot = false;
        try {
            String canonicalPath = new File(path).getCanonicalPath();
            String canonicalChild = this.sourceFile.getCanonicalPath();
            if (canonicalChild.startsWith(canonicalPath)) {
                insideChroot = true;
            } else {
                logger.warn("Attempted access outside chroot: {}", filename);
            }
        } catch (IOException e) {
            logger.error("Error checking chroot for requested file", e);
        }
        this.isValidChild = insideChroot;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException,
            WebApplicationException {
        // Just in case users don't check first
        if (isValidChild) {
            Files.copy(sourceFile, outputStream);
        } else {
            throw new IOException("Attempted access outside chroot");
        }
    }

    public boolean canRead() {
        return isValidChild && sourceFile.canRead();
    }

    public boolean exists() {
        return isValidChild && sourceFile.exists();
    }
}
