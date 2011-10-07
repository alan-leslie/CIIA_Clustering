package com.alag.ci.blog.cluster.impl;

import java.io.StringWriter;
import java.util.*;

import com.alag.ci.blog.dataset.impl.BlogDataSetCreatorImpl;
import com.alag.ci.cluster.*;
import com.alag.ci.cluster.hiercluster.HierCluster;

public class HierarchialClusteringImpl implements Clusterer {
    private List<TextDataItem> textDataSet = null;
    private Map<Integer,HierCluster> availableClusters = null;
    private HierCluster rootCluster ;
    private Map<HierDistance,HierDistance> allDistance = null;
    private int idCount = 0;
    
    public HierarchialClusteringImpl(List<TextDataItem> textDataSet) {
        this.textDataSet = textDataSet;
        this.availableClusters = new HashMap<Integer,HierCluster>();
        this.allDistance = new HashMap<HierDistance,HierDistance>();
    }
    
    public List<TextCluster> cluster() {
        createInitialClusters();
        while (allDistance.size() > 0) {
            addNextCluster();
        }
        List<TextCluster> clusters =  new ArrayList<TextCluster>();
        clusters.add(this.rootCluster);
        return clusters;
    }
  
    private void createInitialClusters() {
        createInitialSingleItemClusters();
        computeInitialDistances();
    }
    
    private void createInitialSingleItemClusters() {
        for (TextDataItem dataItem: this.textDataSet) {
            HierClusterImpl cluster = new HierClusterImpl(this.idCount ++,
                    null, null, 1.,dataItem);
            cluster.setCenter(dataItem.getTagMagnitudeVector());
            this.availableClusters.put(cluster.getClusterId(),cluster);
        }
    }
    
    private void computeInitialDistances() {
        for (HierCluster cluster: this.availableClusters.values()) {
            for (HierCluster otherCluster: this.availableClusters.values()) {
                if (cluster.getClusterId() != otherCluster.getClusterId()) {
                    HierDistance hd = new HierDistance(cluster,otherCluster);
                    if (!this.allDistance.containsKey(hd)) {
                        double similarity = cluster.computeSimilarity(otherCluster);
                        hd.setSimilarity(similarity);
                        this.allDistance.put(hd, hd);
                    }
                }
            }
        }
    }
    
    private void addNextCluster() {
        List<HierDistance> sortDist = new ArrayList<HierDistance>();
        sortDist.addAll(this.allDistance.keySet());
        Collections.sort(sortDist);
        HierDistance hd = sortDist.get(0);
        this.allDistance.remove(hd);
        
        HierCluster cluster = createNewCluster(hd);
        pruneDistances(hd.getC1(), hd.getC2(), sortDist);
        addNewClusterDistances(cluster);
        if (this.allDistance.size() == 0) {
            this.rootCluster = cluster;
        }
    }
    
    private HierCluster createNewCluster(HierDistance hd) {
        HierClusterImpl cluster = new HierClusterImpl(this.idCount ++,
                hd.getC1(), hd.getC2(), hd.getSimilarity(),null);
        cluster.setCenter(hd.getC1().getCenter().add(hd.getC2().getCenter()));
        this.availableClusters.put(cluster.getClusterId(),cluster);
        this.availableClusters.remove(hd.getC1().getClusterId());
        this.availableClusters.remove(hd.getC2().getClusterId());
        return cluster;
    }
    
    private void pruneDistances(HierCluster c1, HierCluster c2, List<HierDistance> sortDist) {
        for (HierDistance hierDistance: sortDist) {
            if ((hierDistance.containsCluster(c1)) ||
                (hierDistance.containsCluster(c2))) {
                this.allDistance.remove(hierDistance);               
            }
        }
    }
    
    private void addNewClusterDistances(HierCluster cluster) {
        for (HierCluster hc: this.availableClusters.values()) {
            if (hc.getClusterId() != cluster.getClusterId()) {
                HierDistance hierDistance = new HierDistance(cluster,hc);
                double similarity =
                    cluster.getCenter().dotProduct(hc.getCenter());
                hierDistance.setSimilarity(similarity);
                this.allDistance.put(hierDistance, hierDistance);
            }
        }
    }
 
    public String toString() {
        StringWriter sb = new StringWriter();
        sb.append("Num of clusters = " + this.idCount + "\n");
        sb.append(printClusterDetails(this.rootCluster,""));
        return sb.toString();
    }
    
    private String printClusterDetails(HierCluster cluster, String append) {
        StringWriter sb = new StringWriter();
        if (cluster != null) {
            sb.append(cluster.toString());
            String tab = "\t" + append;
            if (cluster.getChild1() != null) {
                sb.append("\n" + tab + "C1=" + printClusterDetails(cluster.getChild1(),tab));
            }
            if (cluster.getChild2() != null) {
                sb.append("\n" + tab + "C2=" +printClusterDetails(cluster.getChild2(),tab));
            }
        }
        return sb.toString();
    }
    
    public static final void main(String [] args) throws Exception {
        DataSetCreator bc = new BlogDataSetCreatorImpl();
        List<TextDataItem> blogData = bc.createLearningData();
        Clusterer clusterer = new HierarchialClusteringImpl(
                blogData);
        clusterer.cluster();
        System.out.println(clusterer);
    }
}
