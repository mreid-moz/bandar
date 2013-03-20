package com.mozilla.bandar.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import com.mozilla.bandar.Constants;
import com.mozilla.bandar.query.core.LocalFileResult;


public class TestUtils {

    public static boolean resultContains(LocalFileResult fileResult, String contents) {
        return resultContains(fileResult, contents, "UTF-8");
    }

    public static boolean resultContains(LocalFileResult fileResult, String contents, String encoding) {
        if (fileResult == null || contents == null)
            return false;

        boolean matches = false;
        try {
            String fileContent = getResultContents(fileResult, encoding);
            matches = contents.equals(fileContent);
        } catch (WebApplicationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }

    public static String getResultContents(LocalFileResult fileResult)
            throws IOException, WebApplicationException {
        return getResultContents(fileResult, "UTF-8");
    }

    public static String getResultContents(LocalFileResult fileResult, String encoding)
            throws IOException, WebApplicationException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        fileResult.write(bytes);
        String fileContent = bytes.toString(encoding);

        return fileContent;
    }

    public static void setUnreadables() {
        File unreadableFile = new File(Constants.VALID_FILE_PROVIDER_PATH + File.separator + "unreadable.txt");
        unreadableFile.setReadable(false);
    }

    public static void unsetUnreadables() {
        File unreadableFile = new File(Constants.VALID_FILE_PROVIDER_PATH + File.separator + "unreadable.txt");
        unreadableFile.setReadable(true);
    }
}
