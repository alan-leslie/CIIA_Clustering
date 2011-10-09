
package com.alag.ci.cluster.rock;

import com.alag.ci.blog.dataset.impl.TestDataItem;
import com.alag.ci.cluster.TextDataItem;
import org.junit.Test;

/**
 *
 * @author al
 */
public class ROCKAlgoritmTest {
    @Test
   public void testBasicDatItems() {
        //Define data
        TextDataItem[] elements = new TextDataItem[4];
        elements[0] = new TestDataItem("Doc1", new String[] {"book"});
        elements[1] = new TestDataItem("Doc2", new String[] {"water", "sun", "sand", "swim"});
        elements[2] = new TestDataItem("Doc3", new String[] {"water", "sun", "swim", "read"});
        elements[3] = new TestDataItem("Doc4", new String[] {"read", "sand"});
        
        int k = 1;
        double th = 0.2;
        ROCKAlgorithm rock = new ROCKAlgorithm(elements, k, th);
        Dendrogram dnd = rock.cluster();
        dnd.printAll();
    }    
}
