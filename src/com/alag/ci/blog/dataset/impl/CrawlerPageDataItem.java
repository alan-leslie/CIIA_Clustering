/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.blog.dataset.impl;

import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author al
 */
public class CrawlerPageDataItem implements TextDataItem {
    private CrawlerPage thePage = null;
    private TagMagnitudeVector tagMagnitudeVector = null;
    private Integer clusterId;
    private boolean ciRelated = false;
    
    CrawlerPageDataItem(CrawlerPage newPage,
            TagMagnitudeVector tagMagnitudeVector) {
        this.thePage = newPage;
        this.tagMagnitudeVector = tagMagnitudeVector;
    }
  
    public boolean isCiRelated() {
        return false;
    }

    @Override
    public void setCiRelated(boolean ciRelated) {
    }

    @Override
    public Object getData() {
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
        theAttributes.put("URL", thePage.getURL());
        theAttributes.put("Text", thePage.getText());
        return theAttributes;
    }
}

