package com.alag.ci.blog.search.impl;

import java.io.StringWriter;
import java.util.List;

import com.alag.ci.blog.search.BlogQueryResult;
import com.alag.ci.blog.search.RetrievedBlogEntry;

public class BlogQueryResultImpl implements BlogQueryResult {
    private List<RetrievedBlogEntry> results = null;
    private Integer queryCount = null;
  
    public List<RetrievedBlogEntry> getRelevantBlogs() {
        return this.results;
    }

    public void setResults(List<RetrievedBlogEntry> results) {
        this.results = results;
    }

    public Integer getQueryCount() {
        return this.queryCount;
    }
    
    public void setQueryCount(Integer queryCount) {
        this.queryCount = queryCount;
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        int index = 1;
        sw.append("Total=" + this.queryCount);
        for(RetrievedBlogEntry rbe: results) {
            sw.append("\n"+ (index++) + rbe);
        }
        return sw.toString();
    }
  
}
