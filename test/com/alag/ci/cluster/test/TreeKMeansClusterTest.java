/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.cluster.test;

import clustering.ui.TreeView;
import com.alag.ci.blog.cluster.impl.ClusterImpl;
import com.alag.ci.blog.cluster.impl.TextKMeansClustererImpl;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextCluster;

/**
 *
 * @author al
 */
public class TreeKMeansClusterTest {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                DataSetCreator pt = new PageTextDataSetCreatorImpl("/home/al/lasers/crawl_small/processed/", null);
                TextKMeansClustererImpl clusterer = new TextKMeansClustererImpl(3);
                TextCluster rootCluster = new ClusterImpl(0, pt);
                rootCluster.hierCluster(clusterer);

                TreeView.createAndShowGUI(rootCluster);
            }
        });
    }
}
