package com.alag.ci.cluster;

import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.Map;

public interface TextDataItem {
    public Map<String, String> getAttributeMap();
    public RetrievedDataEntry getData();
    public TagMagnitudeVector getTagMagnitudeVector() ;    
    public String [] getTags(int i) ;
    public Integer getClusterId();
    public void setClusterId(Integer clusterId); 
    public void setCiRelated(boolean ciRelated);
}
