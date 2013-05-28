package com.mozilla.bandar.query.resources;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mozilla.bandar.Constants;
import com.mozilla.bandar.query.core.LocalFileProvider;
import com.mozilla.bandar.util.TestUtils;
import com.yammer.dropwizard.testing.JsonHelpers;

public class TestLocalFileResource {
    LocalFileProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new LocalFileProvider(Constants.VALID_FILE_PROVIDER_PATH);
        TestUtils.setUnreadables();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testList() throws IOException {
        Response ok = Response.ok().build();

        LocalFileResource list = new LocalFileResource(provider);
        Response listResponse = list.getQueryResult();
        assertEquals(ok.getStatus(), listResponse.getStatus());

        // Make sure the returned data looks right.
        Object entity = listResponse.getEntity();
        String rep = asJson(entity);
        List<String> fromJson = JsonHelpers.fromJson(rep, List.class);
        Collections.sort(fromJson);
        assertEquals(2, fromJson.size());
        assertEquals("bar.txt", fromJson.get(0));
        assertEquals("quux.txt", fromJson.get(1));

        boolean threw = false;
        try {
            List<String> originalList = (List<String>)entity;
            Collections.sort(originalList);
            fail("should have thrown when sorting");
        } catch (UnsupportedOperationException e) {
            // Should be an unmodifiable collection
            threw = true;
        }

        assertTrue(threw);
    }

    @Test
    public void testContent() {
        Response ok = Response.ok().build();
        Response notFound = Response.status(Status.NOT_FOUND).build();
        Response forbidden = Response.status(Status.FORBIDDEN).build();

        LocalFileResource list = new LocalFileResource(provider);
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
