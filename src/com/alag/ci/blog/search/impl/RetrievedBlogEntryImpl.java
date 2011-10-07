package com.alag.ci.blog.search.impl;

import java.io.StringWriter;
import java.util.Date;

import com.alag.ci.blog.search.RetrievedBlogEntry;

public class RetrievedBlogEntryImpl implements RetrievedBlogEntry {
    private String name = null;
    private String url = null;
    private Date lastUpdateTime = null;
    private Date creationTime = null;
    private String title = null;
    private String excerpt = null;
    private String author = null;

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        sw.append(" Name: " + this.getName());
        sw.append("\nUrl: " + this.getUrl());
        sw.append("\nTitle: " + this.getTitle());
        sw.append("\nExcerpt: " + this.getExcerpt());
        if (this.getLastUpdateTime() != null) {
            sw.append("\nLastUpdateTime: " + this.getLastUpdateTime());
        }
      //  sw.append("\nCreationTime: " + this.getCreationTime());
        sw.append("\nAuthor: " + this.getAuthor());
        return sw.toString();
    }
}
