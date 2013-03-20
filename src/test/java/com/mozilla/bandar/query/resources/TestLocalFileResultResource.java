package com.mozilla.bandar.query.resources;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mozilla.bandar.Constants;
import com.mozilla.bandar.query.core.LocalFileProvider;
import com.mozilla.bandar.util.TestUtils;

public class TestLocalFileResultResource {
    LocalFileProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new LocalFileProvider(Constants.VALID_FILE_PROVIDER_PATH);
        TestUtils.setUnreadables();
    }

    @Test
    public void test() {
        Response ok = Response.ok().build();
        Response notFound = Response.status(Status.NOT_FOUND).build();
        Response forbidden = Response.status(Status.FORBIDDEN).build();

        LocalFileResultResource list = new LocalFileResultResource(provider);
        Response barResponse = list.getQueryResult("bar.txt");
        assertEquals(ok.getStatus(), barResponse.getStatus());

        Response bogusResponse = list.getQueryResult("bogus.txt");
        assertEquals(notFound.getStatus(), bogusResponse.getStatus());

        // TODO: refactor so that we can mock an unreadable file.
        Response unreadableResponse = list.getQueryResult("unreadable.txt");
        assertEquals(forbidden.getStatus(), unreadableResponse.getStatus());
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.unsetUnreadables();
    }
}
