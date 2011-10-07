package com.alag.ci.blog.search.impl.bloglines;
import com.alag.ci.blog.search.impl.BlogQueryParameterImpl;

public class BlogLineSearchBlogQueryParameterImpl extends
        BlogQueryParameterImpl {
    private static final String   BLOGLINE_SEARCH_API_URL =
        "http://www.bloglines.com/search";  
    
    public BlogLineSearchBlogQueryParameterImpl() {
        super(QueryType.SEARCH,BLOGLINE_SEARCH_API_URL);
    }
}

