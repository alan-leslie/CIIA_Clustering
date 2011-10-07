package com.alag.ci.blog.search.impl.technorati;

import com.alag.ci.blog.search.impl.BlogQueryParameterImpl;

public class TechnoratiTagBlogQueryParameterImpl extends
        BlogQueryParameterImpl {
    private static final String   TECHNORATI_TAG_API_URL =
        "http://api.technorati.com/tag";
    
    public TechnoratiTagBlogQueryParameterImpl() {
        super(QueryType.TAG,TECHNORATI_TAG_API_URL);
    }
}

