package com.mozilla.bandar.query.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;

public class TestLocalFileProvider {
    final String providerBasePath = "src/test/resources/test_file_query/test_parent/test_root";
    LocalFileProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new LocalFileProvider(providerBasePath);
    }

    @Test
    public void test() {
        assertEquals("file", provider.getName());
        List<String> queryNames = provider.getQueryNames();
        assertNotNull(queryNames);
        assertEquals(2, queryNames.size());
        assertTrue(queryNames.contains("bar.txt"));
        assertTrue(queryNames.contains("quux.txt"));

        // Normal case: file exists, all is well.
        requestGoodFile("bar.txt", "I am bar\n");

        // Another normal case: file exists in a subdirectory, all is well.
        requestGoodFile("test_child/baz.txt", "I am baz\n");

        // Attempt to reach a file outside of the chroot:
        // File exists, but we shouldn't be able to get it:
        requestBadFile("../foo.txt", true);

        // Attempt to reach a totally bogus file:
        requestBadFile("my_bogus_filename.tar.gz", false);
    }

    private void requestGoodFile(String filename, String contents) {
        LocalFileResult fileResult = provider.getQueryResult(filename);
        assertTrue(fileResult.exists());
        assertTrue(fileResult.canRead());
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(20);
            fileResult.write(bytes);
            String fileContent = bytes.toString("UTF-8");
            assertEquals(contents, fileContent);
        } catch (WebApplicationException e) {
            fail("getting query result threw a WebApplicationException");
            e.printStackTrace();
        } catch (IOException e) {
            fail("getting query result threw an IOException");
            e.printStackTrace();
        }
    }

    // Ensure that the given file will cause an IO Exception to be thrown.
    private void requestBadFile(String badFileName, boolean actuallyExists) {
        if (actuallyExists) {
            assertTrue(new File(providerBasePath + File.separator + badFileName).exists());
        } else {
            assertFalse(new File(providerBasePath + File.separator + badFileName).exists());
        }
        LocalFileResult fileResult;
        fileResult = provider.getQueryResult(badFileName);
        assertFalse(fileResult.exists());
        assertFalse(fileResult.canRead());
        String fileContent = null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(20);
            fileResult.write(bytes);
            fileContent = bytes.toString("UTF-8");
            fail("should have thrown an exception accessing a file outside chroot");
        } catch (WebApplicationException e) {
            fail("getting query result threw a WebApplicationException");
            e.printStackTrace();
        } catch (IOException e) {
            // This is the code path we wanted.

        }
        assertNull(fileContent);
    }

}
