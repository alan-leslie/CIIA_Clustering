/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.blog.dataset.impl;

import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.textanalysis.TagMagnitude;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import com.alag.ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author al
 */
public class TestDataItem implements TextDataItem {
    private TagMagnitudeVector tagMagnitudeVector = null;
    private String theDoc = null;
    private Integer clusterId;
    private boolean ciRelated = false;
    private RetrievedDataEntry theEntry = null;
    
    TestDataItem(CrawlerPage newPage,
            TagMagnitudeVector tagMagnitudeVector) {
        this.theEntry = newPage;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }

    public TestDataItem(String theDocName, String[] string0) {
        this.theDoc = theDocName;
        List<TagMagnitude> tagMagnitudes = null;
        this.tagMagnitudeVector = new TagMagnitudeVectorImpl(tagMagnitudes);
    }
  
    public boolean isCiRelated() {
        return false;
    }

    @Override
    public void setCiRelated(boolean ciRelated) {
    }

    @Override
    public RetrievedDataEntry getData() {
        return theEntry;
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
        theAttributes.put("Title", theEntry.getTitle());
        theAttributes.put("URL", theEntry.getUrl());
        theAttributes.put("Text", theEntry.getText());
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
                
        for(int i = 0; i < tagArraySize && i < tagMagnitudes.size(); ++i){
            retVal[i] = tagMagnitudes.get(i).getTag().getStemmedText();
        }
              
        // anything left over is set to null
        
        return retVal;
    }
}