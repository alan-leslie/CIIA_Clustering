package com.alag.ci.blog.dataset.impl;

import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.textanalysis.TagMagnitude;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogAnalysisDataItem implements TextDataItem {
    private RetrievedDataEntry blogEntry = null;
    private TagMagnitudeVector tagMagnitudeVector = null;
    private Integer clusterId;
    private boolean ciRelated = false;
    
    public BlogAnalysisDataItem(RetrievedDataEntry blogEntry,
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

    public RetrievedDataEntry getData() {
        return this.getBlogEntry();
    }
    
    public RetrievedDataEntry getBlogEntry() {
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

    @Override
    public String[] getTags(int noOfItems) {
       List<TagMagnitude> tagMagnitudes = tagMagnitudeVector.getTagMagnitudes();
        String retVal[] = new String[tagMagnitudes.size()];
        int i = 0;
        for(TagMagnitude theTM: tagMagnitudes){
            retVal[i] = theTM.getTag().getStemmedText();
            ++i;
        }
        
        return retVal;
    }
}
