package com.alag.ci.cluster.rock;

import iweb2.ch4.clustering.hierarchical.Dendrogram;
import iweb2.ch4.model.Cluster;
import iweb2.ch4.model.MultiDimensionalDataPoint;
import iweb2.similarity.JaccardCoefficient;
import iweb2.similarity.SimilarityMeasure;

import java.util.ArrayList;
import java.util.List;

public class ROCKAlgorithm {

    private MultiDimensionalDataPoint[] points;
    private int k;
    private double th;
    
    private SimilarityMeasure similarityMeasure;
    
    private LinkMatrix linkMatrix;
    
    /**
     * 
     * @param k desired number of clusters.
     * @param th threshold value to identify neighbors among points.
     */
    public ROCKAlgorithm(MultiDimensionalDataPoint[] points, int k, double th) {
        this.points = points;
        this.k = k;
        this.th = th;
        this.similarityMeasure = new JaccardCoefficient();
        //this.similarityMeasure = new CosineSimilarity();
        this.linkMatrix = new LinkMatrix(points, similarityMeasure, th);
    }
    
    
    public Dendrogram cluster() {
        
        //  Create a new cluster out of every point.
        List<Cluster> initialClusters = new ArrayList<Cluster>();
        for(int i = 0, n = points.length; i < n; i++) {
            Cluster cluster = new Cluster(points[i]);
            initialClusters.add(cluster);
        }
        double g = Double.POSITIVE_INFINITY;        
        Dendrogram dnd = new Dendrogram("Goodness");
        dnd.addLevel(String.valueOf(g), initialClusters);
        
        MergeGoodnessMeasure goodnessMeasure = new MergeGoodnessMeasure(th);
        
        ROCKClusters allClusters = new ROCKClusters(initialClusters, 
                linkMatrix,
                goodnessMeasure);

        int nClusters = allClusters.size();
        while( nClusters > k ) {
            int nClustersBeforeMerge = nClusters;
            g = allClusters.mergeBestCandidates();
            nClusters = allClusters.size();
            if( nClusters == nClustersBeforeMerge ) {
                // there are no linked clusters to merge
                break; 
            }
            dnd.addLevel(String.valueOf(g), allClusters.getAllClusters());
        }

        System.out.println("Number of clusters: "+allClusters.getAllClusters().size());
        return dnd;
    }
    
    public static void main(String[] args) {
        //Define data
        MultiDimensionalDataPoint[] elements = new MultiDimensionalDataPoint[4];
        elements[0] = new MultiDimensionalDataPoint("Doc1", new String[] {"book"});
        elements[1] = new MultiDimensionalDataPoint("Doc2", new String[] {"water", "sun", "sand", "swim"});
        elements[2] = new MultiDimensionalDataPoint("Doc3", new String[] {"water", "sun", "swim", "read"});
        elements[3] = new MultiDimensionalDataPoint("Doc4", new String[] {"read", "sand"});
        
        int k = 1;
        double th = 0.2;
        ROCKAlgorithm rock = new ROCKAlgorithm(elements, k, th);
        Dendrogram dnd = rock.cluster();
        dnd.printAll();
    }


    public int getK() {
        return k;
    }


    public double getTh() {
        return th;
    }


    public SimilarityMeasure getSimilarityMeasure() {
        return similarityMeasure;
    }


    public LinkMatrix getLinkMatrix() {
        return linkMatrix;
    }
    
}
