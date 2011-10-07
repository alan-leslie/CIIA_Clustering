package com.alag.ci.blog.search.impl.bloglines;


import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alag.ci.blog.search.BlogQueryParameter;
import com.alag.ci.blog.search.BlogSearchResponseHandler;
import com.alag.ci.blog.search.BlogSearcherException;
import com.alag.ci.blog.search.BlogQueryParameter.QueryParameter;
import com.alag.ci.blog.search.impl.BlogSearcherImpl;

/**
 * http://www.bloglines.com/search?format=publicapi&apiuser=xxx&apikey=xxx&q=collective+intelligence
 * @author Satnam Alag
 *
 */
public class BlogLinesBlogSearcherImpl extends BlogSearcherImpl {
    public BlogLinesBlogSearcherImpl() throws BlogSearcherException {
    }

    protected BlogSearchResponseHandler getBlogSearchResponseHandler() { 
        return new BlogLinesResponseHandler();
    }

    protected HttpMethod  getMethod(BlogQueryParameter param) {
        String url = param.getMethodUrl() + "?" +
        //"format=rss&" +
        "format=publicapi&" +
        "apiuser=" + param.getParameter(QueryParameter.APIUSER)+
        "&apikey=" + param.getParameter(QueryParameter.KEY)+
        "&q=" + urlEncode(param.getParameter(QueryParameter.QUERY));
        
        System.out.println(url);
        GetMethod method = new GetMethod(url);
        return method;
    }
}
