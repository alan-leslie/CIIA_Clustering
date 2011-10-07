package com.alag.ci.blog.cluster.impl;

import java.util.*;

import com.alag.ci.blog.dataset.impl.BlogDataSetCreatorImpl;
import com.alag.ci.cluster.*;

public class TextKMeansClustererImpl implements Clusterer {
    private List<TextDataItem> textDataSet = null;
    private List<TextCluster> clusters = null;
    private int numClusters ;
    
    public TextKMeansClustererImpl(List<TextDataItem> textDataSet,
            int numClusters) {
        this.textDataSet = textDataSet;
        this.numClusters = numClusters;
    }
    
    public List<TextCluster> cluster() {
        if (this.textDataSet.size() == 0) {
            return Collections.emptyList();
        }
        this.intitializeClusters();
        boolean change = true;
        int count = 0;
        while ((count ++ < 100) && (change)) {
            clearClusterItems();
            change = reassignClusters();
            computeClusterCenters();
        }
        return this.clusters;
    }
    
    private void intitializeClusters() {
        this.clusters = new ArrayList<TextCluster>();
        Map<Integer,Integer> usedIndexes = new HashMap<Integer,Integer>();
        for (int i = 0; i < this.numClusters; i++ ) {
            ClusterImpl cluster = new ClusterImpl(i);
            cluster.setCenter(getDataItemAtRandom(usedIndexes).getTagMagnitudeVector());
            this.clusters.add(cluster);
        }
    }
    
    private TextDataItem getDataItemAtRandom(Map<Integer,Integer> usedIndexes) {
        boolean found = false;
        while (!found) {
            int index = (int)Math.floor(Math.random()*this.textDataSet.size());
            if (!usedIndexes.containsKey(index)) {
                System.out.println(index);
                usedIndexes.put(index, index);
                return this.textDataSet.get(index);
            }
        }
        return null;
    }
    
    private boolean reassignClusters() {
        int numChanges = 0;
        for (TextDataItem item: this.textDataSet) {
            TextCluster newCluster = getClosestCluster(item);
            if ((item.getClusterId() == null ) ||
            (item.getClusterId().intValue() != 
                newCluster.getClusterId())) {
                System.out.println("From " + item.getClusterId() + 
                        " to " + newCluster.getClusterId());
                numChanges ++;
                item.setClusterId(newCluster.getClusterId());
            }
            newCluster.addDataItem(item);
        }
        System.out.println("Changes=" + numChanges);
        return (numChanges > 0);
    }
    
    private void computeClusterCenters() {
        for (TextCluster cluster: this.clusters) {
            cluster.computeCenter();
        }
    }
    
    private void clearClusterItems(){
        for (TextCluster cluster: this.clusters) {
            cluster.clearItems();
        }
    }
    
    private TextCluster getClosestCluster(TextDataItem item) {
        TextCluster closestCluster = null;
        Double hightSimilarity = null;
        for (TextCluster cluster: this.clusters) {
            double similarity =
                cluster.getCenter().dotProduct(item.getTagMagnitudeVector());
            if ((hightSimilarity == null) ||
                    (hightSimilarity.doubleValue() < similarity)) {
                hightSimilarity = similarity;
                closestCluster = cluster;
            }
        }
        return closestCluster;
    }

    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TextCluster cluster: clusters) {
            sb.append("\n\n");
            sb.append(cluster.toString());
        }
        return sb.toString();
    }
    
    public static final void main(String [] args) throws Exception {
        DataSetCreator bc = new BlogDataSetCreatorImpl();
        List<TextDataItem> blogData = bc.createLearningData();
        TextKMeansClustererImpl clusterer = new TextKMeansClustererImpl(
                blogData,4);
        clusterer.cluster();
        System.out.println(clusterer);
    }
}
