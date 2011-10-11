package iweb2.clustering.hierarchical;

import com.alag.ci.blog.cluster.impl.ClusterImpl;
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

/** A hierarchical agglomerative clustering algorithm based on single link */
public class SingleLinkAlgorithm {

    private TextDataItem[] elements;
    private double[][] a;

    // Hierarchical Agglomerative Algorithm
    public SingleLinkAlgorithm(TextDataItem[] elements, double[][] adjacencyMatrix) {
        this.elements = elements;
        this.a = adjacencyMatrix;
    }

    public Dendrogram cluster() {
        Dendrogram dnd = new Dendrogram("Distance");
        double d = 0;

        // initially load all elements as individual clusters
        List<TextCluster> initialClusters = new ArrayList<TextCluster>();
        for (TextDataItem e : elements) {
            TextCluster c = new ClusterImpl(0, e);
            initialClusters.add(c);
        }

        dnd.addLevel(String.valueOf(d), initialClusters);

        d = 1.0;

        int k = initialClusters.size();

        while (k > 1) {
            int oldK = k;
            List<TextCluster> clusters = buildClusters(d);
            k = clusters.size();
            if (oldK != k) {
                dnd.addLevel(String.valueOf(d), clusters);
            }

            d = d + 1;
        }
        return dnd;
    }

    // Implements Single Link Technique
    private List<TextCluster> buildClusters(double distanceThreshold) {
        boolean[] usedElementFlags = new boolean[elements.length];
        List<TextCluster> clusters = new ArrayList<TextCluster>();
        for (int i = 0, n = a.length; i < n; i++) {
            List<TextDataItem> clusterPoints = new ArrayList<TextDataItem>();
            for (int j = i, k = a.length; j < k; j++) {
                if (a[i][j] <= distanceThreshold && usedElementFlags[j] == false) {
                    clusterPoints.add(elements[j]);
                    usedElementFlags[j] = true;
                }
            }
            if (clusterPoints.size() > 0) {
                List<RetrievedDataEntry> theData = new ArrayList<RetrievedDataEntry>();
                for(TextDataItem theItem: clusterPoints){
                    RetrievedDataEntry data = theItem.getData();
                    theData.add(data);
                }
                DataSetCreator pt = new PageTextDataSetCreatorImpl("", theData);
                TextCluster c = new ClusterImpl(0, pt);
                clusters.add(c);
            }
        }
        return clusters;
    }

    public static void main(String[] args) {
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
//        TextDataItem[] elements = new TextDataItem[5];
//        elements[0] = new TextDataItem("A", new double[] {});
//        elements[1] = new TextDataItem("B", new double[] {});
//        elements[2] = new TextDataItem("C", new double[] {});
//        elements[3] = new TextDataItem("D", new double[] {});
//        elements[4] = new TextDataItem("E", new double[] {});

            double[][] a = new double[][]{
                {0, 1, 2, 2, 3},
                {1, 0, 2, 4, 3},
                {2, 2, 0, 1, 5},
                {2, 4, 1, 0, 3},
                {3, 3, 5, 3, 0}
            };

            SingleLinkAlgorithm ca = new SingleLinkAlgorithm(testData, a);
            Dendrogram dnd = ca.cluster();
            dnd.printAll();
            //dnd.print(3);
        } catch (Exception ex) {
            Logger.getLogger(SingleLinkAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            assert (false);
        }
    }
}
