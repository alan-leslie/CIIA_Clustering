package com.alag.ci.cluster;

import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.Map;

public interface TextDataItem {
    public Map<String, String> getAttributeMap();
    public Object getData();
    public TagMagnitudeVector getTagMagnitudeVector() ;
    public Integer getClusterId();
    public void setClusterId(Integer clusterId); 
    public void setCiRelated(boolean ciRelated);
}
