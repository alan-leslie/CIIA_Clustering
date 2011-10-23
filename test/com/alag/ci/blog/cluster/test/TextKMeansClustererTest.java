/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.blog.cluster.test;

import iweb2.clustering.utils.XMLFile;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.blog.dataset.impl.LocalData;
import com.alag.ci.blog.search.RetrievedDataEntry;
import java.util.ArrayList;
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
        List<RetrievedDataEntry> elements = new ArrayList<RetrievedDataEntry>();
        elements.add(new LocalData("Doc1", "book"));
        elements.add(new LocalData("Doc2", "water sun sand swim"));
        elements.add(new LocalData("Doc3", "water sun swim read"));
        elements.add(new LocalData("Doc4", "read sand"));

        DataSetCreator pt = new PageTextDataSetCreatorImpl("", elements);

        TextKMeansClustererImpl clusterer = new TextKMeansClustererImpl(2);
        ClusterImpl.CLUSTER_NO = 2;
        TextCluster rootCluster = new ClusterImpl(0, pt);
        rootCluster.hierCluster(clusterer);

        XMLFile.writeXML("KMeansTestBasic.xml", rootCluster.asXML());
        List<TextCluster> theClusters = rootCluster.getSubClusters(); // not really needed
        
        // todo - add assertions
    }
}