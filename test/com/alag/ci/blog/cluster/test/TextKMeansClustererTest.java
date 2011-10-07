/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.blog.cluster.test;

import java.util.List;
import com.alag.ci.cluster.TextCluster;
import com.alag.ci.blog.cluster.impl.ClusterImpl;
import com.alag.ci.blog.cluster.impl.TextKMeansClustererImpl;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
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
public class TextKMeansClustererTest {

    public TextKMeansClustererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class TextKMeansClustererImpl.
     */
    @Test
    public void testValidClustering() {
        DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
        TextKMeansClustererImpl clusterer = new TextKMeansClustererImpl(3);
        TextCluster rootCluster = new ClusterImpl(0, pt);
        rootCluster.hierCluster(clusterer);
        List<TextCluster> theClusters = rootCluster.getSubClusters(); // not really needed
        
        int i = 0;
        for(TextCluster theCluster: theClusters){
            System.out.println("Cluster no:" + Integer.toString(i));          
            System.out.println(theCluster.toString());
            ++i;
        }
    }
}