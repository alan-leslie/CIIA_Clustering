package com.alag.ci.blog.dataset.impl;

import com.alag.ci.blog.search.RetrievedBlogEntry;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.HashMap;
import java.util.Map;

public class BlogAnalysisDataItem implements TextDataItem {
    private RetrievedBlogEntry blogEntry = null;
    private TagMagnitudeVector tagMagnitudeVector = null;
    private Integer clusterId;
    private boolean ciRelated = false;
    
    public BlogAnalysisDataItem(RetrievedBlogEntry blogEntry,
            TagMagnitudeVector tagMagnitudeVector) {
        this.blogEntry = blogEntry;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }
  
    public boolean isCiRelated() {
        return ciRelated;
    }

    public void setCiRelated(boolean ciRelated) {
        this.ciRelated = ciRelated;
    }

    public Object getData() {
        return this.getBlogEntry();
    }
    
    public RetrievedBlogEntry getBlogEntry() {
        return blogEntry;
    }

    public TagMagnitudeVector getTagMagnitudeVector() {
        return tagMagnitudeVector;
    }
    
    public double distance(TagMagnitudeVector other) {
        return this.getTagMagnitudeVector().dotProduct(other);
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public Map<String, String> getAttributeMap() {
        Map<String, String> theAttributes = new HashMap<String, String>();
        theAttributes.put("Title", blogEntry.getTitle());
        theAttributes.put("URL", blogEntry.getUrl());
        theAttributes.put("Excerpt", blogEntry.getExcerpt());
        return theAttributes;
    }

}
