package com.alag.ci.blog.cluster.impl;

import java.util.*;

import com.alag.ci.blog.search.RetrievedBlogEntry;
import com.alag.ci.cluster.*;
import com.alag.ci.textanalysis.*;
import com.alag.ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;

public class ClusterImpl implements TextCluster {
    private TagMagnitudeVector center = null;
    private List<TextDataItem> items = null;
    private int clusterId;
    
    public ClusterImpl(int clusterId) {
        this.clusterId = clusterId;
        this.items = new ArrayList<TextDataItem>();
    }
 
    public void computeCenter() {
        if (this.items.size() == 0) {
            return;
        }
        List<TagMagnitudeVector> tmList = new ArrayList<TagMagnitudeVector>();
        for (TextDataItem item: items) {
            tmList.add(item.getTagMagnitudeVector());
        }
        List<TagMagnitude> emptyList = Collections.emptyList();
        TagMagnitudeVector empty = new TagMagnitudeVectorImpl(emptyList);
        this.center = empty.add(tmList);
    }
    
    public int getClusterId() {
        return this.clusterId;
    }
    
    public void addDataItem(TextDataItem item) {
        items.add(item);
    }

    public TagMagnitudeVector getCenter() {
        return center;
    }

    public List<TextDataItem> getItems() {
        return items;
    }

    public void setCenter(TagMagnitudeVector center) {
        this.center = center;
    }
    
    public void clearItems() {
        this.items.clear();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        sb.append("Id=" + this.clusterId);
    //    sb.append("\n" + this.center);
        for (TextDataItem item: items) {
            if (item != null) {
                RetrievedBlogEntry blog = (RetrievedBlogEntry) item.getData();
                sb.append("\nTitle=" + blog.getTitle());
                sb.append("\nExcerpt=" + blog.getExcerpt());
            }
        }
        return sb.toString();
    }
}
