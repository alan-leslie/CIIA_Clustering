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

public class MSTSingleLinkAlgorithm {

    private TextDataItem[] elements;
    private double[][] a;
    private double[][] m;
    private ClusterSet allClusters;

    public MSTSingleLinkAlgorithm(TextDataItem[] elements, double[][] adjacencyMatrix) {
        this.elements = elements;
        this.a = adjacencyMatrix;
        this.allClusters = new ClusterSet();
    }

    public Dendrogram cluster() {

        m = (new MST()).buildMST(a);

        Dendrogram dnd = new Dendrogram("Distance");
        double d = 0.0;

        // initially load all elements as individual clusters
        for (TextDataItem e : elements) {
            TextCluster c = new ClusterImpl(0, e);
            allClusters.add(c);
        }

        int lastDndLevel = dnd.addLevel(String.valueOf(d), allClusters.getAllClusters());

        double previousD = d;

        while (allClusters.size() > 1) {
            d = mergeTwoClosestClusters();
            if (previousD == d) {
                dnd.setLevel(lastDndLevel, String.valueOf(d), allClusters.getAllClusters());
            } else {
                lastDndLevel = dnd.addLevel(String.valueOf(d), allClusters.getAllClusters());
            }
            previousD = d;
        }

        return dnd;
    }

    private double mergeTwoClosestClusters() {
        int minI = -1;
        int minJ = -1;
        double minWeight = Double.POSITIVE_INFINITY;

        for (int i = 0, n = m.length; i < n; i++) {
            for (int j = 0, k = m.length; j < k; j++) {
                if (m[i][j] >= 0 && minWeight > m[i][j]) {
                    minI = i;
                    minJ = j;
                    minWeight = m[i][j];
                }
            }
        }


        double d = Double.NaN;
        if (minI > -1) {
            TextDataItem e1 = elements[minI];
            TextCluster c1 = allClusters.findClusterByElement(e1);
            TextDataItem e2 = elements[minJ];
            TextCluster c2 = allClusters.findClusterByElement(e2);

            List<TextDataItem> theItems = c1.getDataItems();
            theItems.addAll(c2.getDataItems());

            allClusters.remove(c1);
            allClusters.remove(c2);
            d = minWeight;

            String mergedTitle = c1.getTitle() + "-" + c2.getTitle();
            List<RetrievedDataEntry> theData = new ArrayList<RetrievedDataEntry>();

            for (TextDataItem theItem : theItems) {
                theData.add(theItem.getData());
            }

            DataSetCreator theCreator = new PageTextDataSetCreatorImpl("", theData);
            TextCluster mergedCluster = new ClusterImpl(0, theCreator);

            allClusters.add(mergedCluster);
            m[minI][minJ] = -1; // remove link. Using -1 because 0 is a valid distance.
            m[minJ][minI] = -1; // remove link. Using -1 because 0 is a valid distance.
        }

        return d;
    }

    public static void main(String[] args) {
        //Define data
//        TextDataItem[] elements = new TextDataItem[5];
//        elements[0] = new TextDataItem("A", new double[] {});
//        elements[1] = new TextDataItem("B", new double[] {});
//        elements[2] = new TextDataItem("C", new double[] {});
//        elements[3] = new TextDataItem("D", new double[] {});
//        elements[4] = new TextDataItem("E", new double[] {});
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

            MSTSingleLinkAlgorithm ca = new MSTSingleLinkAlgorithm(testData, a);
            Dendrogram dnd = ca.cluster();
            dnd.printAll();
        } catch (Exception ex) {
            Logger.getLogger(MSTSingleLinkAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            assert (false);
        }
    }
}
