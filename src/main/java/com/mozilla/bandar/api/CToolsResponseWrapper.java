package com.mozilla.bandar.api;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mozilla.bandar.api.CToolsResponse.MetaData;

@XmlRootElement(name="CToolsResponse")
public class CToolsResponseWrapper {


//    @XmlElementWrapper
    @JsonProperty
    private MetaData[] metadata;

//    @XmlElementWrapper
    @JsonProperty
    private Map<String,String> queryInfo;

//    @XmlElementWrapper
    @JsonProperty
    private Object[][] resultset;

    private CToolsResponseWrapper() { }

    public CToolsResponseWrapper(MetaData[] metadata, Map<String, String> queryInfo, Object[][] resultset) {
        this();
        this.metadata = metadata;
        this.queryInfo = queryInfo;
        this.resultset = resultset;
    }

    public CToolsResponseWrapper(CToolsResponse response) {
        this.metadata = response.getMetadata().toArray(new MetaData[]{});
        this.queryInfo = response.getQueryInfo();
        List<List<Object>> resultsetIn = response.getResultset();
        if (resultsetIn != null && resultsetIn.size() > 0) {
            List<Object> firstRow = resultsetIn.get(0);
            this.resultset = new Object[resultsetIn.size()][firstRow.size()];
            for (int row = 0; row < resultsetIn.size(); row++) {
                List<Object> rowN = resultsetIn.get(row);
                for (int col = 0; col < rowN.size(); col++) {
                    this.resultset[row][col] = rowN.get(col);
                }
            }
        }
    }

//    @XmlElementRef
    public MetaData[] getMetadata() { return metadata; }
    public void setMetadata(MetaData[] metadata) {
        this.metadata = metadata;
    }
//    @XmlElementRef
    public Map<String, String> getQueryInfo() { return queryInfo; }
    public void setQueryInfo(Map<String, String> queryInfo) {
        this.queryInfo = queryInfo;
    }
//    @XmlElementRef
    public Object[][] getResultset() { return resultset; }
    public void setResultset(Object[][] resultset) {
        this.resultset = resultset;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result
                + ((queryInfo == null) ? 0 : queryInfo.hashCode());
        result = prime * result
                + ((resultset == null) ? 0 : resultset.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CToolsResponseWrapper other = (CToolsResponseWrapper) obj;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        if (queryInfo == null) {
            if (other.queryInfo != null)
                return false;
        } else if (!queryInfo.equals(other.queryInfo))
            return false;
        if (resultset == null) {
            if (other.resultset != null)
                return false;
        } else if (!resultset.equals(other.resultset))
            return false;
        return true;
    }

    public int getFieldIndex(String fieldName) {
        // If we have metadata, figure out what column we're looking for.  Otherwise, guess.
        int fieldIndex = -1;
        if (metadata != null && metadata.length > 0) {
            for (MetaData field : metadata) {
                if (fieldName.equals(field.getColName())) {
                    fieldIndex = field.getColIndex();
                    break;
                }
            }
        }
        return fieldIndex;
    }

}