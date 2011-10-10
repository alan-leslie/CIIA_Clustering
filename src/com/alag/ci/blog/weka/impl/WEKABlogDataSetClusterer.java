package com.alag.ci.blog.weka.impl;

import java.util.*;

import weka.clusterers.*;
import weka.core.*;

import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.textanalysis.*;
import com.alag.ci.textanalysis.Tag;

public class WEKABlogDataSetClusterer extends PageTextDataSetCreatorImpl {
    private List<TextDataItem> blogEntries = null; 
    
    public WEKABlogDataSetClusterer(String dataDir,
            List<RetrievedDataEntry> theData) {
        super(dataDir, theData);
    }    
    public Instances createLearningDataSet() throws Exception {
        this.blogEntries = createLearningData();
        FastVector allAttributes = createAttributes();  
        Instances trainingDataSet = new Instances("blogClustering",
                allAttributes, blogEntries.size());
        int numAttributes = allAttributes.size();
        Collection<Tag> allTags = this.getAllTags();
        for (TextDataItem dataItem : blogEntries) {
            Instance instance = createNewInstance(numAttributes,trainingDataSet,
                     allTags, dataItem );  
            trainingDataSet.add(instance);
        }
        return trainingDataSet;
    }
    
    private FastVector createAttributes() {
        Collection<Tag> allTags = this.getAllTags();
        FastVector allAttributes = new FastVector(allTags.size());
        for (Tag tag : allTags) {
            Attribute tagAttribute = new Attribute(tag.getDisplayText());  
            allAttributes.addElement(tagAttribute);
        }
        return allAttributes;
    }
    
    private Instance createNewInstance(int numAttributes,
            Instances trainingDataSet, Collection<Tag> allTags,
            TextDataItem dataItem) {
        Instance instance = new Instance(numAttributes);
        instance.setDataset(trainingDataSet);
        int index = 0;
        TagMagnitudeVector tmv = dataItem.getTagMagnitudeVector();
        Map<Tag, TagMagnitude> tmvMap = tmv.getTagMagnitudeMap();
        for (Tag tag : allTags) {
            TagMagnitude tm = tmvMap.get(tag);
            if (tm != null) {
                instance.setValue(index++, tm.getMagnitude());  
            } else {
                instance.setValue(index++, 0.);
            }
        }
        return instance;
    }


    public void cluster() throws Exception {
        Instances instances = createLearningDataSet();
        Clusterer clusterer = getClusterer(instances);
        evaluateCluster(clusterer, instances);
    }
    

    private Clusterer getClusterer(Instances instances) throws Exception {
        EM em = new EM();
        em.setNumClusters(-1);
        em.setMaxIterations(100);
        em.buildClusterer(instances);
        return em;
    }

    private ClusterEvaluation evaluateCluster(Clusterer clusterer,
            Instances instances) throws Exception {
        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(clusterer);
        eval.evaluateClusterer(instances);
        String evalString = eval.clusterResultsToString();
        System.out.println(evalString);
        
        int numClusters = eval.getNumClusters();
        double[] assignments = eval.getClusterAssignments();
        System.out.println("NumClusters=" + numClusters);

        Map<Integer, List<RetrievedDataEntry>> assignMap =
                associateInstancesWithClusters(assignments);
        printClusterEntries(assignMap);
        return eval;
    }
    
    private Map<Integer, List<RetrievedDataEntry>> associateInstancesWithClusters(double[] assignments) {
        int index = 0;
        Map<Integer, List<RetrievedDataEntry>> assignMap = new HashMap<Integer, List<RetrievedDataEntry>>();
        for (double assignment : assignments) {
            TextDataItem dataItem = this.blogEntries.get(index++);
            List<RetrievedDataEntry> entries = assignMap.get((int) assignment);
            if (entries == null) {
                entries = new ArrayList<RetrievedDataEntry>();
                assignMap.put((int) assignment, entries);
            }
            entries.add((RetrievedDataEntry) dataItem.getData());
        }
        return assignMap;
    }
    
    private void printClusterEntries(Map<Integer, List<RetrievedDataEntry>> assignMap) {
        for (int clusterId = 0; clusterId < assignMap.size(); clusterId++) {
            List<RetrievedDataEntry> entries = assignMap.get(clusterId);
            System.out.println(clusterId);
            for (RetrievedDataEntry blogEntry : entries) {
                System.out.println(blogEntry.getExcerpt());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        WEKABlogDataSetClusterer bds = new WEKABlogDataSetClusterer("/home/al/lasers/crawl_small/processed/", null);
        bds.cluster();
    }
}
