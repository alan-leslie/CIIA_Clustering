/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.blog.dataset.impl;

import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.textanalysis.TagMagnitude;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author al
 */
public class TestDataItem implements TextDataItem {
    private CrawlerPage thePage = null;
    private TagMagnitudeVector tagMagnitudeVector = null;
    private Integer clusterId;
    private boolean ciRelated = false;
    
    TestDataItem(CrawlerPage newPage,
            TagMagnitudeVector tagMagnitudeVector) {
        this.thePage = newPage;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }

    public TestDataItem(String string, String[] string0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
  
    public boolean isCiRelated() {
        return false;
    }

    @Override
    public void setCiRelated(boolean ciRelated) {
    }

    @Override
    public RetrievedDataEntry getData() {
        return this.thePage;
    }
    
    CrawlerPage getPage() {
        return thePage;
    }

    @Override
    public TagMagnitudeVector getTagMagnitudeVector() {
        return tagMagnitudeVector;
    }
    
    public double distance(TagMagnitudeVector other) {
        return this.getTagMagnitudeVector().dotProduct(other);
    }

    @Override
    public Integer getClusterId() {
        return clusterId;
    }

    @Override
    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public Map<String, String> getAttributeMap() {
        Map<String, String> theAttributes = new HashMap<String, String>();
        theAttributes.put("Title", thePage.getTitle());
        theAttributes.put("URL", thePage.getUrl());
        theAttributes.put("Text", thePage.getText());
        return theAttributes;
    }

    @Override
    public String[] getTags(int noOfTags) {
        List<TagMagnitude> tagMagnitudes = tagMagnitudeVector.getTagMagnitudes();
        Collections.sort(tagMagnitudes);
      
        int tagArraySize = noOfTags;
        if(noOfTags < 1){
            tagArraySize = tagMagnitudes.size();
        }

        String retVal[] = new String[tagArraySize];
        
        int i = 0;
        for(TagMagnitude theTM: tagMagnitudes){
            retVal[i] = theTM.getTag().getStemmedText();
            ++i;
        }
        
        // anything left over is set to null
        
        return retVal;
    }
}