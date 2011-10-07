package com.alag.ci.blog.search;

public interface BlogSearcher {
    public BlogQueryResult getRelevantBlogs(BlogQueryParameter param)
            throws BlogSearcherException;
}
