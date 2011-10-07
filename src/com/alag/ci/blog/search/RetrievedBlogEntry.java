package com.alag.ci.blog.search;

import java.util.Date;

public interface RetrievedBlogEntry {
    public String getName();
    public String getUrl();
    public String getTitle();
    public String getExcerpt();
    public String getAuthor();
    public Date getLastUpdateTime();
    public Date getCreationTime();
}
