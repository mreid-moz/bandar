package com.mozilla.bandar.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CToolsResponse {
    public static class MetaData {
        @JsonProperty
        String colName;

        @JsonProperty
        String colType;

        @JsonProperty
        int colIndex;
        public int getColIndex() {
            return colIndex;
        }
        public void setColIndex(int colIndex) {
            this.colIndex = colIndex;
        }
        public String getColType() {
            return colType;
        }
        public void setColType(String colType) {
            this.colType = colType;
        }
        public String getColName() {
            return colName;
        }
        public void setColName(String colName) {
            this.colName = colName;
        }

        private MetaData() { }

        public MetaData(String colName, String colType, int colIndex) {
            this();
            this.colIndex = colIndex;
            this.colType = colType;
            this.colName = colName;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + colIndex;
            result = prime * result
                    + ((colName == null) ? 0 : colName.hashCode());
            result = prime * result
                    + ((colType == null) ? 0 : colType.hashCode());
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
            MetaData other = (MetaData) obj;
            if (colIndex != other.colIndex)
                return false;
            if (colName == null) {
                if (other.colName != null)
                    return false;
            } else if (!colName.equals(other.colName))
                return false;
            if (colType == null) {
                if (other.colType != null)
                    return false;
            } else if (!colType.equals(other.colType))
                return false;
            return true;
        }

    }

    @JsonProperty
    private List<MetaData> metadata;

    @JsonProperty
    private Map<String,String> queryInfo;

    @JsonProperty
    private List<List<Object>> resultset;

    private CToolsResponse() { }

    public CToolsResponse(List<MetaData> metadata, Map<String, String> queryInfo, List<List<Object>> resultset) {
        this();
        this.metadata = metadata;
        this.queryInfo = queryInfo;
        this.resultset = resultset;
    }

    public List<MetaData> getMetadata() { return metadata; }
    public void setMetadata(List<MetaData> metadata) {
        this.metadata = metadata;
    }
    public Map<String, String> getQueryInfo() { return queryInfo; }
    public void setQueryInfo(Map<String, String> queryInfo) {
        this.queryInfo = queryInfo;
    }
    public List<List<Object>> getResultset() { return resultset; }
    public void setResultset(List<List<Object>> resultset) {
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
        CToolsResponse other = (CToolsResponse) obj;
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
        if (metadata != null && metadata.size() > 0) {
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