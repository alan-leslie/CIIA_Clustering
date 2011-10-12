/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.cluster.test;

import clustering.ui.TreeView;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextDataItem;
import iweb2.clustering.hierarchical.Dendrogram;
import iweb2.clustering.rock.ROCKAlgorithm;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author al
 */
public class TreeROCKClusterTest {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
                try {
                    List<TextDataItem> theItems = pt.createLearningData();
                    TextDataItem[] testData = (TextDataItem[]) theItems.toArray(new TextDataItem[theItems.size()]);

                    int k = 1;
                    double th = 0.2;
                    ROCKAlgorithm rock = new ROCKAlgorithm(testData, k, th);
                    Dendrogram dnd = rock.cluster();
                    dnd.printAll();

                    TreeView.createAndShowGUI(dnd);
                } catch (Exception ex) {
                    Logger.getLogger(TreeROCKClusterTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
