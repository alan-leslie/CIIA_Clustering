package com.alag.ci.cluster.test;

import com.alag.ci.cluster.TextCluster;
import com.alag.ci.blog.search.RetrievedDataEntry;
import java.util.Map;
import weka.clusterers.ClusterEvaluation;
import com.alag.ci.blog.weka.impl.WEKABlogDataSetClusterer;
import iweb2.clustering.utils.XMLFile;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author al
 */
public class TreeWEKA_EMClusterTest {

    @Test
    public void testValidClustering() {
        try {
//            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
            WEKABlogDataSetClusterer bds = new WEKABlogDataSetClusterer("/home/al/lasers/crawl_small/processed/", null);
            ClusterEvaluation theEval = bds.cluster();
            double[] assignments = theEval.getClusterAssignments();

            Map<Integer, List<RetrievedDataEntry>> assignMap =
                    bds.associateInstancesWithClusters(assignments);

//            TextCluster rootCluster = null;
            for (int clusterId = 0; clusterId < assignMap.size(); clusterId++) {
                List<RetrievedDataEntry> entries = assignMap.get(clusterId);
                System.out.println(clusterId);
                for (RetrievedDataEntry blogEntry : entries) {
                    System.out.println(blogEntry.getUrl());
                }
            }

//            XMLFile.writeXML("HierTest.xml", rootCluster.asXML());
        } catch (Exception ex) {
            assert (false);
        }
    }

    public static void main(String[] args) throws Exception {
    }
}
