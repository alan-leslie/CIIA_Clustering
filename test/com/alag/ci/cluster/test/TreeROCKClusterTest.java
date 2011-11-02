/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.cluster.test;

import iweb2.clustering.rock.LinkMatrix;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextDataItem;
import iweb2.clustering.hierarchical.Dendrogram;
import iweb2.clustering.rock.ROCKAlgorithm;
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
public class TreeROCKClusterTest {

    @Test
    public void testValidClustering() {
        try {
//            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl-1318553114765/processed/", null);
//            DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/test/processed/", null);

            List<TextDataItem> beList = pt.createLearningData();
            TextDataItem[] testData = beList.toArray(new TextDataItem[beList.size()]);

            int k = 1;
            double th = 0.1;
            LinkMatrix.TOP_N_TERMS = 50;
            ROCKAlgorithm rock = new ROCKAlgorithm(testData, k, th);
            Dendrogram dnd = rock.cluster();
            dnd.printAll();

            XMLFile.writeXML("ROCKTest.xml", dnd.asXML());
        } catch (Exception ex) {
            assert (false);
        }
    }
}
