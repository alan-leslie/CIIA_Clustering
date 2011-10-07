package com.alag.ci.blog.search.impl.rss;

import com.alag.ci.blog.search.impl.BlogQueryParameterImpl;

public class RSSFeedBlogQueryParameterImpl extends BlogQueryParameterImpl {
    
    public enum RSSProviderURL {
        FEEDSTER("http://feedster.com/search.php","offset","limit","sort","type",QueryType.SEARCH),
        BLOGDIGGER("http://www.blogdigger.com/rss.jsp","si","pp","sortby","",QueryType.SEARCH),
        MSN("http://search.msn.com/results.aspx","first","count","","format",QueryType.SEARCH),
        BLOGLINE("http://www.bloglines.com/search","","","","",QueryType.SEARCH),
        TECHNORATI_SEARCH("http://api.technorati.com/search","","","","",QueryType.SEARCH),
        TECHNORATI_TAG_SEARCH("http://api.technorati.com/tag","","","","",QueryType.TAG),
        ICE_ROCKET("http://blogs.icerocket.com/search","","","","",QueryType.SEARCH), 
        PLAZOO("http://www.plazoo.com/en/default.asp","","","","",QueryType.SEARCH);   
        
        private String url = null;
        private String firstKey = null;
        private String numKey = null;
        private String sortKey = null;
        private String formatKey = null;
        private QueryType queryType = null;
        
        RSSProviderURL(String url,String firstKey, String numKey,
                String sortKey, String formatKey, QueryType queryType) {
            this.url = url;
            this.firstKey = firstKey;
            this.numKey = numKey;
            this.sortKey = sortKey;
            this.formatKey = formatKey;
            this.queryType = queryType;
        }
        
        public String getUrl() {
            return this.url;
        }
        
        public String getNumKey() {
            return this.numKey;
        }
        
        public String getSortKey() {
            return this.sortKey;
        }
        public String getFormatKey() {
            return this.formatKey;
        }
        public String getFirstKey() {
            return this.firstKey;
        }

        public QueryType getQueryType() {
            return queryType;
        }       
        
    }
    
    private RSSProviderURL rssProviderUrl = null;
    
    public RSSFeedBlogQueryParameterImpl(RSSProviderURL rssProviderUrl) {
        super(rssProviderUrl.getQueryType(),rssProviderUrl.getUrl());
        this.rssProviderUrl = rssProviderUrl;
    }
    
    public RSSProviderURL getRSSProviderURL() {
        return this.rssProviderUrl;
    }
}