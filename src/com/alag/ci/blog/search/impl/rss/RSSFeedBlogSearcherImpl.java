package com.alag.ci.blog.search.impl.rss;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alag.ci.blog.search.BlogQueryParameter;
import com.alag.ci.blog.search.BlogSearcherException;
import com.alag.ci.blog.search.BlogQueryParameter.QueryParameter;
import com.alag.ci.blog.search.impl.BlogSearcherImpl;
import com.alag.ci.blog.search.impl.rss.RSSFeedBlogQueryParameterImpl.RSSProviderURL;

/**
 * Generic class for rss
 * MSN: http://search.msn.com/results.aspx?q=collective+intelligence&format=rss&first=1&count=3
 * Blogdigger: http://www.blogdigger.com/rss.jsp?sortby=date&q=collective&si=1&pp=2
 * Feedster: http://feedster.com/search.php?q=collective+intelligence&sort=date&type=rss&offset=0&limit=2&language=en
 * 
 * @author Satnam Alag
 *
 */
public class RSSFeedBlogSearcherImpl extends BlogSearcherImpl { 
    public RSSFeedBlogSearcherImpl() throws BlogSearcherException {
    }

   // http://www.blogdigger.com/rss.jsp?sortby=date&q=collective&si=1&pp=2
    protected HttpMethod  getMethod(BlogQueryParameter param) {
        RSSFeedBlogQueryParameterImpl rssParam = (RSSFeedBlogQueryParameterImpl)param;
        RSSProviderURL rssProvider = rssParam.getRSSProviderURL();
        String url = param.getMethodUrl() + "?q=" +
            urlEncode(param.getParameter(QueryParameter.QUERY))+
            "&" + rssProvider.getFirstKey() + "=" + param.getParameter(QueryParameter.START_INDEX)+
            "&" + rssProvider.getNumKey() +  "=" + param.getParameter(QueryParameter.LIMIT);
        if ("".compareTo(rssProvider.getSortKey())  != 0) {
            String sortBy = "date";
            if (param.getParameter(QueryParameter.SORTBY) != null ) {
                sortBy = param.getParameter(QueryParameter.SORTBY);
            }
            url += "&" + rssProvider.getSortKey() +  "=" + sortBy;
        }
        if ("".compareTo(rssProvider.getFormatKey())  != 0) {
            url += "&" + rssProvider.getFormatKey() + "=rss" ;
        }
        
        System.out.println(url);
        GetMethod method = new GetMethod(url);
        return method;
    }

}