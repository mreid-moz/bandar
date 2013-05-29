package com.mozilla.bandar.api;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.mozilla.bandar.api.CToolsResponse.MetaData;

public class TestFixtures {
    CToolsResponse reference;

    @Before
    public void setUp() {
        List<CToolsResponse.MetaData> metadata = new ArrayList<CToolsResponse.MetaData>(3);
        metadata.add(new CToolsResponse.MetaData("foo", "String", 0));
        metadata.add(new CToolsResponse.MetaData("bar", "Integer", 1));
        metadata.add(new CToolsResponse.MetaData("baz", "Boolean", 2));

        Map<String,String> queryInfo = new HashMap<String,String>();
        queryInfo.put("totalRows", "2");

        List<List<Object>> resultset = new ArrayList<List<Object>>(2);
        List<Object> row1 = new ArrayList<Object>(metadata.size());
        List<Object> row2 = new ArrayList<Object>(metadata.size());
        row1.add("Hola");        row1.add(30); row1.add(true);
        row2.add("Where am I?"); row2.add(40); row2.add(true);
        resultset.add(row1);
        resultset.add(row2);

        reference = new CToolsResponse(metadata, queryInfo, resultset);
    }

    @Test
    public void cdaResponseSerialization() throws Exception {
        assertEquals(jsonFixture("fixtures/cdaresponse.json"), asJson(reference));
    }

    @Test
    public void cdaResponseDeserialization() throws Exception {
        CToolsResponse fixture = fromJson(jsonFixture("fixtures/cdaresponse.json"), CToolsResponse.class);
        assertTrue(reference.equals(fixture));
    }

    @Test
    public void equalsAndHashCode() throws Exception {
        CToolsResponse apple = fromJson(jsonFixture("fixtures/cdaresponse.json"), CToolsResponse.class);
        CToolsResponse orange = fromJson(jsonFixture("fixtures/cdaresponse.json"), CToolsResponse.class);

        assertEquals(apple, orange);
        assertEquals(apple, apple);
        assertEquals(apple.hashCode(), orange.hashCode());

        MetaData appleMetadata = apple.getMetadata().get(0);
        MetaData orangeMetadata = orange.getMetadata().get(0);
        assertEquals(appleMetadata, orangeMetadata);
        assertEquals(appleMetadata.hashCode(), orangeMetadata.hashCode());

        appleMetadata.setColType("Apple");
        orangeMetadata.setColType("Orange");
        assertFalse(appleMetadata.equals(orangeMetadata));
        assertNotSame(appleMetadata.hashCode(), orangeMetadata.hashCode());

        assertFalse(apple.equals(orange));
    }

    @Test
    public void testXmlSerialization() throws IOException, JAXBException {
        CToolsResponse apple = fromJson(jsonFixture("fixtures/cdaresponse.json"), CToolsResponse.class);

        boolean threw = false;

        try {
            // If all goes according to plan, this should throw an exception due to JAXB being unable to
            // handle the java.util.List instances in CToolsResponse
            JAXBContext badContext = JAXBContext.newInstance(CToolsResponse.class);
            fail();
        } catch (Exception e) {
            threw = true;
        }

        assertTrue(threw);


        // This should work, since CToolsResponseWrapper uses arrays instead of Lists.
        CToolsResponseWrapper orange = new CToolsResponseWrapper(apple);
        JAXBElement<CToolsResponseWrapper> xml = new JAXBElement<CToolsResponseWrapper>(new QName("CToolsResponse"), CToolsResponseWrapper.class, orange);
        System.out.println(xml.toString());
        JAXBContext context = JAXBContext.newInstance(CToolsResponseWrapper.class);
        Marshaller marshmallow = context.createMarshaller();
        marshmallow.marshal(xml, System.out);
        System.out.println("All done");
    }
}
