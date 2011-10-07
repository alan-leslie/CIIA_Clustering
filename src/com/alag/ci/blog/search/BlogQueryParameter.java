package com.alag.ci.blog.search;

import java.util.Collection;

public interface BlogQueryParameter {
    //Types of query supported
    public enum QueryType {SEARCH,TAG};
    //The parameters supported by the API
    public enum QueryParameter {KEY, APIUSER,START_INDEX, LIMIT, 
        QUERY, TAG,SORTBY,LANGUAGE};

    public String getParameter(QueryParameter param);  
    public void setParameter(QueryParameter param, String value);
    public Collection<QueryParameter> getAllParameters();
    public QueryType getQueryType();
    public String getMethodUrl();
}
