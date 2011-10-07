package com.alag.ci.blog.search.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.alag.ci.blog.search.BlogQueryParameter;

public abstract class BlogQueryParameterImpl implements BlogQueryParameter {
    private Map<QueryParameter,String> params = null;
    private QueryType queryType = null;
    private String methodUrl = null;
    
    public BlogQueryParameterImpl(QueryType queryType, String methodUrl) {
        this.queryType = queryType;
        this.methodUrl = methodUrl;
        this.params = new HashMap<QueryParameter,String>();
    }

    public String getMethodUrl() {
        return methodUrl;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public Collection<QueryParameter> getAllParameters() {
        return this.params.keySet();
    }
    
    public String getParameter(QueryParameter param) {
        return this.params.get(param);
    }

    public void setParameter(QueryParameter param, String value) {
       this.params.put(param, value);
    }
}
