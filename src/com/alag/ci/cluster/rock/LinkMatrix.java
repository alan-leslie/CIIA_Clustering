package com.alag.ci.cluster.rock;

import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import iweb2.similarity.SimilarityMeasure;

import java.util.Arrays;
import java.util.Set;

/**
 * Calculates number of links between data points.
 */
public class LinkMatrix {

    private double th;
    double[][] pointSimilarityMatrix;
    int[][] pointNeighborMatrix;
    int[][] pointLinkMatrix;
    private ObjectToIndexMapping<TextDataItem> objToIndexMapping;
    private int TOP_N_TERMS = 20;
    
    
    public LinkMatrix(TextDataItem[] points, SimilarityMeasure pointSim, double th) {
        double[][] similarityMatrix = calculatePointSimilarities(points, pointSim);
        init(points, similarityMatrix, th);
    }
    
//    public LinkMatrix(TextDataItem[] points, double[][] similarityMatrix, double th) {
//        init(points, similarityMatrix, th);
//    }

    private void init(TextDataItem[] points, double[][] similarityMatrix, double th) {
    
        this.th = th;
        
        objToIndexMapping = new ObjectToIndexMapping<TextDataItem>();
        
        // Create TextDataItem <-> Index mapping.
        for(TextDataItem point : points) {
            objToIndexMapping.getIndex(point);
        }
        
        pointSimilarityMatrix = similarityMatrix;
        
        // Identify neighbors: a[i][j] == 1 if (i,j) are neighbors and 0 otherwise.
        int n = points.length;
        
        pointNeighborMatrix = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                if( pointSimilarityMatrix[i][j] >= th ) {
                    pointNeighborMatrix[i][j] = 1;
                }
                else {
                    pointNeighborMatrix[i][j] = 0;
                }
                pointNeighborMatrix[j][i] = pointNeighborMatrix[i][j];
            }
            pointNeighborMatrix[i][i] = 1;
        }
        
        // Calculate number of links between points
        pointLinkMatrix = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = i; j < n; j++) {
                pointLinkMatrix[i][j] = 
                    nLinksBetweenPoints(pointNeighborMatrix, i, j);
                pointLinkMatrix[j][i] = pointLinkMatrix[i][j]; 
            }
        }
        
    }
    
    
    public int getLinks(TextDataItem p1, TextDataItem p2) {
        int i = objToIndexMapping.getIndex(p1);
        int j = objToIndexMapping.getIndex(p2);
        return pointLinkMatrix[i][j];
    }
    
    /**
     * Calculates number of links between two clusters. Number of links between 
     * two clusters is the sum of links between all point pairs( p1, p2) where   
     * p1 belongs to the first cluster and p2 belongs to the other cluster.
     * 
     * @param clusterX
     * @param clusterY
     * 
     * @return link count between two clusters.
     */

    public int getLinks(TextCluster clusterX, TextCluster clusterY) {
        Set<TextDataItem> itemsX = clusterX.getElements();
        Set<TextDataItem> itemsY = clusterY.getElements();
        
        int linkSum = 0;
        
        for(TextDataItem x : itemsX) {
            for(TextDataItem y : itemsY) {
                linkSum += getLinks(x, y);
            }
        }
        return linkSum;
    }
    
    private int nLinksBetweenPoints(int[][] neighbors, int indexX, int indexY) {
        int nLinks = 0;
        for(int i = 0, n = neighbors.length; i < n; i++) {
            nLinks += neighbors[indexX][i] * neighbors[i][indexY];
        }
        return nLinks;
    }
    
    /*
     * Calculates similarity matrix for all points.
     */
    private double[][] calculatePointSimilarities(
            TextDataItem[] points, SimilarityMeasure pointSim) {
        
        int n = points.length;
        double[][] simMatrix = new double[n][n];
        for(int i = 0; i < n; i++) {
            TextDataItem itemX = points[i];
            String[] attributesX = itemX.getTags(TOP_N_TERMS);
            for(int j = i + 1; j < n; j++) {
                TextDataItem itemY = points[j];
                String[] attributesY = itemY.getTags(TOP_N_TERMS);
                simMatrix[i][j] = pointSim.similarity(
                        attributesX, attributesY);
                simMatrix[j][i] = simMatrix[i][j];
            }
            simMatrix[i][i] = 1.0;
        }

        return simMatrix;
    }
 
    public void printSimilarityMatrix() {
      System.out.println("Point Similarity matrix:");
      for(int i = 0; i < pointSimilarityMatrix.length; i++) {
          System.out.println(Arrays.toString(pointSimilarityMatrix[i]));
      }
    }

    public void printPointNeighborMatrix() {
        System.out.println("Point Neighbor matrix (th=" + String.valueOf(th) + "):");
        for(int i = 0; i < pointNeighborMatrix.length; i++) {
            System.out.println(Arrays.toString(pointNeighborMatrix[i]));
        }
     }

    public void printPointLinkMatrix() {
        System.out.println("Point Link matrix (th=" + String.valueOf(th) + "):");
        for(int i = 0; i < pointLinkMatrix.length; i++) {
            System.out.println(Arrays.toString(pointLinkMatrix[i]));
        }
     }  
}
