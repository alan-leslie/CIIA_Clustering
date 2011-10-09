package com.alag.ci.cluster;

import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.List;
import java.util.Set;

public interface TextCluster {
    public void clearItems();
    public TagMagnitudeVector getCenter();
    public void computeCenter();
    public int getClusterId() ;
    public void setClusterId(int newId);
    public String getSource() ;    
    public String getTitle() ;
    public void addDataItem(TextDataItem item);
    public List<TextDataItem> getDataItems();
    public void hierCluster(Clusterer theClusterer);
    public List<TextCluster> getSubClusters();
    public Set<TextDataItem> getElements();
    public TextCluster copy();
    public String getElementsAsString();
}
