package com.alag.ci.blog.search;

import java.util.List;

public interface BlogQueryResult {
    public Integer getQueryCount();
    public List<RetrievedBlogEntry> getRelevantBlogs();
}
