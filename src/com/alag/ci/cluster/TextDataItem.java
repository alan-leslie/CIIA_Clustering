package com.alag.ci.cluster;

import com.alag.ci.textanalysis.TagMagnitudeVector;

public interface TextDataItem {
    public Object getData();
    public TagMagnitudeVector getTagMagnitudeVector() ;
    public Integer getClusterId();
    public void setClusterId(Integer clusterId); 
    public void setCiRelated(boolean ciRelated);
}
