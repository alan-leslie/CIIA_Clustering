package com.alag.ci.cluster.rock;

import com.alag.ci.blog.dataset.impl.LocalData;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.DataSetCreator;
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
//        elements.add(new LocalData("Doc2", "water sun sand swim"));
//        elements.add(new LocalData("Doc3", "water sun swim read"));
//        elements.add(new LocalData("Doc4", "read sand"));
        elements.add(new LocalData("Doc5", "read sand"));        
        elements.add(new LocalData("Doc5", "sand"));

        DataSetCreator pt = new PageTextDataSetCreatorImpl("", elements);

        try {
            List<TextDataItem> theItems = pt.createLearningData();

            TextDataItem[] testData = (TextDataItem[]) theItems.toArray(new TextDataItem[theItems.size()]);

            int k = 1;
            double th = 0.2;
            ROCKAlgorithm rock = new ROCKAlgorithm(testData, k, th);
            Dendrogram dnd = rock.cluster();
            dnd.printAll();
        } catch (Exception ex) {
            Logger.getLogger(ROCKAlgoritmTest.class.getName()).log(Level.SEVERE, null, ex);
            assert(false);
        }
    }
}
