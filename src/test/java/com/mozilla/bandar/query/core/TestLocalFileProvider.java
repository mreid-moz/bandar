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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mozilla.bandar.Constants;
import com.mozilla.bandar.util.TestUtils;

public class TestLocalFileProvider {
    final String providerBasePath = Constants.VALID_FILE_PROVIDER_PATH;
    LocalFileProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new LocalFileProvider(providerBasePath);
        TestUtils.setUnreadables();
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

    @Test
    public void test_generate_io_exception() {
        File parent = Mockito.mock(File.class);
        String error = "DON'T PANIC: This is an expected error during testing.";
        try {
            Mockito.when(parent.getCanonicalPath()).thenThrow(new IOException(error));
        } catch (IOException e) {
            fail("Mock should not have thrown an exception");
        }

        LocalFileResult pitcher = new LocalFileResult(parent, new File("garbage"));
        assertFalse(pitcher.exists());
        assertFalse(pitcher.canRead());
    }

    private void requestGoodFile(String filename, String contents) {
        LocalFileResult fileResult = provider.getQueryResult(filename);
        assertTrue(fileResult.exists());
        assertTrue(fileResult.canRead());
        assertTrue(TestUtils.resultContains(fileResult, contents));
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

    @After
    public void tearDown() throws Exception {
        TestUtils.unsetUnreadables();
    }
}
