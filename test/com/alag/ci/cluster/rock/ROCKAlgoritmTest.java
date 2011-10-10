package com.alag.ci.cluster.rock;

import iweb2.clustering.rock.ROCKAlgorithm;
import iweb2.clustering.rock.LinkMatrix;
import iweb2.clustering.hierarchical.Dendrogram;
import com.alag.ci.blog.dataset.impl.LocalData;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author al
 */
public class ROCKAlgoritmTest {

    @Test
    public void testBasicDatItems() {
        //Define data
        List<RetrievedDataEntry> elements = new ArrayList<RetrievedDataEntry>();
        elements.add(new LocalData("Doc1", "book"));
        elements.add(new LocalData("Doc2", "water sun sand swim"));
        elements.add(new LocalData("Doc3", "water sun swim read"));
        elements.add(new LocalData("Doc4", "read sand"));

        DataSetCreator pt = new PageTextDataSetCreatorImpl("", elements);

        try {
            List<TextDataItem> theItems = pt.createLearningData();

            TextDataItem[] testData = (TextDataItem[]) theItems.toArray(new TextDataItem[theItems.size()]);

            int k = 1;
            double th = 0.2;
            ROCKAlgorithm rock = new ROCKAlgorithm(testData, k, th);
            LinkMatrix linkMatrix = rock.getLinkMatrix();
            linkMatrix.printSimilarityMatrix();
            linkMatrix.printPointNeighborMatrix();
            linkMatrix.printPointLinkMatrix();

            int links = linkMatrix.getLinks(theItems.get(0), theItems.get(1));
            assert (links == 0);
            links = linkMatrix.getLinks(theItems.get(0), theItems.get(2));
            assert (links == 0);
            links = linkMatrix.getLinks(theItems.get(0), theItems.get(3));
            assert (links == 0);
            links = linkMatrix.getLinks(theItems.get(1), theItems.get(2));
            assert (links == 3);
            links = linkMatrix.getLinks(theItems.get(1), theItems.get(3));
            assert (links == 3);
            links = linkMatrix.getLinks(theItems.get(2), theItems.get(3));
            assert (links == 3);

            Dendrogram dnd = rock.cluster();

            List<Integer> theLevels = dnd.getAllLevels();
            assert(theLevels.size() == 3);
            List<TextCluster> theClusters = dnd.getClustersForLevel(1);
            assert(theClusters.size() == elements.size());
            theClusters = dnd.getClustersForLevel(2);
            assert(theClusters.size() == 3);
            theClusters = dnd.getClustersForLevel(3);
            assert(theClusters.size() == 2);

            dnd.printAll();
        } catch (Exception ex) {
            Logger.getLogger(ROCKAlgoritmTest.class.getName()).log(Level.SEVERE, null, ex);
            assert (false);
        }
    }
}
