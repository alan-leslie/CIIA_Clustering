package com.alag.ci.cluster.rock;

import com.alag.ci.blog.cluster.impl.ClusterImpl;
import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import iweb2.similarity.JaccardCoefficient;
import iweb2.similarity.SimilarityMeasure;

import java.util.ArrayList;
import java.util.List;

public class ROCKAlgorithm {

    private TextDataItem[] points;
    private int k;
    private double th;
    
    private SimilarityMeasure similarityMeasure;
    
    private LinkMatrix linkMatrix;
    
    /**
     * 
     * @param k desired number of clusters.
     * @param th threshold value to identify neighbors among points.
     */
    public ROCKAlgorithm(TextDataItem[] points, int k, double th) {
        this.points = points;
        this.k = k;
        this.th = th;
        this.similarityMeasure = new JaccardCoefficient();
        //this.similarityMeasure = new CosineSimilarity();
        this.linkMatrix = new LinkMatrix(points, similarityMeasure, th);
    }
    
    
    public Dendrogram cluster() {   
        //  Create a new cluster out of every point.
        List<TextCluster> initialClusters = new ArrayList<TextCluster>();
        for(int i = 0, n = points.length; i < n; i++) {
            TextCluster cluster = new ClusterImpl(0, points[i]);
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
