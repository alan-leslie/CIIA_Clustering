package com.alag.ci.cluster.hiercluster;

import com.alag.ci.cluster.TextCluster;

public interface HierCluster extends TextCluster {
    public HierCluster getChild1() ;
    public HierCluster getChild2();
    public double getSimilarity() ;
    public double computeSimilarity(HierCluster o);
}
