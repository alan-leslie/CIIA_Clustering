package com.alag.ci.cluster;

import com.alag.ci.textanalysis.TagMagnitudeVector;

public interface TextCluster {
    public void clearItems();
    public TagMagnitudeVector getCenter();
    public void computeCenter();
    public int getClusterId() ;
    public void addDataItem(TextDataItem item);
}
