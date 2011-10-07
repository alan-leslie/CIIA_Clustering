package com.alag.ci.blog.search.impl.technorati;

import com.alag.ci.blog.search.impl.BlogQueryParameterImpl;

public class TechnoratiSearchBlogQueryParameterImpl extends
        BlogQueryParameterImpl {
    private static final String TECHNORATI_SEARCH_API_URL =
        "http://api.technorati.com/search";    
    
    public TechnoratiSearchBlogQueryParameterImpl() {
        super(QueryType.SEARCH,TECHNORATI_SEARCH_API_URL);
    }
}
