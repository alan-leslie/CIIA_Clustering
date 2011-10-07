package com.alag.ci.cluster;

import java.util.List;

public interface Clusterer {
    public void setDataSet(List<TextDataItem> textDataSet);
    public List<TextCluster> cluster();
}
