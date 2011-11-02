package com.alag.ci.cluster.test;

import com.alag.ci.blog.cluster.impl.HierarchialClusteringImpl;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.cluster.hiercluster.HierCluster;
import iweb2.clustering.utils.XMLFile;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeHierClusterTest {

    @Test
    public void testValidClustering() {
        try {
//            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl-1318553114765/processed/", null);

            List<TextDataItem> beList = pt.createLearningData();
            HierarchialClusteringImpl clusterer = new HierarchialClusteringImpl(beList);
            clusterer.cluster();
            System.out.println(clusterer);

            HierCluster rootCluster = clusterer.getRoot();
            XMLFile.writeXML("HierTest.xml", rootCluster.asXML());
        } catch (Exception ex) {
            assert (false);
        }
    }
}
