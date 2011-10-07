package com.alag.ci.cluster;

import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.List;

public interface TextCluster {
    public void clearItems();
    public TagMagnitudeVector getCenter();
    public void computeCenter();
    public int getClusterId() ;
    public String getSource() ;    
    public String getTitle() ;
    public void addDataItem(TextDataItem item);
    public List<TextDataItem> getDataItems();
    public void hierCluster(Clusterer theClusterer);
    public List<TextCluster> getSubClusters();   
}
