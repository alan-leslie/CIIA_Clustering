package com.alag.ci.cluster.test;

import com.alag.ci.blog.cluster.impl.ClusterImpl;
import com.alag.ci.blog.cluster.impl.TextKMeansClustererImpl;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextCluster;
import iweb2.clustering.utils.XMLFile;

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
public class TreeKMeansClusterTest {

    @Test
    public void testValidClustering() {
        try {
            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl-1318553114765/processed/", null);

//            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
            TextKMeansClustererImpl clusterer = new TextKMeansClustererImpl(5);
            ClusterImpl.CLUSTER_NO = 5;
            TextCluster rootCluster = new ClusterImpl(0, pt);
            rootCluster.hierCluster(clusterer);

            XMLFile.writeXML("KMeansTest.xml", rootCluster.asXML());
        } catch (Exception ex) {
            assert (false);
        }
    }
}
