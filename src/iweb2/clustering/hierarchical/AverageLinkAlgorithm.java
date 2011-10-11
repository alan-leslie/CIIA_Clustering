package iweb2.clustering.hierarchical;

import com.alag.ci.blog.cluster.impl.ClusterImpl;
import com.alag.ci.blog.dataset.impl.LocalData;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import iweb2.clustering.utils.ObjectToIndexMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A hierarchical agglomerative clustering algorithm based on the average link */
public class AverageLinkAlgorithm {

    private TextDataItem[] elements;
    private double[][] a;
    private ClusterSet allClusters;

    public AverageLinkAlgorithm(TextDataItem[] elements, double[][] adjacencyMatrix) {
        this.elements = elements;
        this.a = adjacencyMatrix;
        this.allClusters = new ClusterSet();
    }

    public Dendrogram cluster() {

        Dendrogram dnd = new Dendrogram("Distance");
        double d = 0.0;

        // initially load all elements as individual clusters
        for (TextDataItem e : elements) {
            TextCluster c = new ClusterImpl(0, e);
            allClusters.add(c);
        }

        dnd.addLevel(String.valueOf(d), allClusters.getAllClusters());

        d = 1.0;

        while (allClusters.size() > 1) {
            int K = allClusters.size();
            mergeClusters(d);
            // it is possible that there were no clusters to merge for current d.
            if (K > allClusters.size()) {
                dnd.addLevel(String.valueOf(d), allClusters.getAllClusters());
                K = allClusters.size();
            }

            d = d + 0.5;
        }
        return dnd;
    }

    private void mergeClusters(double distanceThreshold) {
        int nClusters = allClusters.size();

        ObjectToIndexMapping<TextCluster> idxMapping =
                new ObjectToIndexMapping<TextCluster>();

        double[][] clusterDistances = new double[nClusters][nClusters];

        for (int i = 0, n = a.length; i < n; i++) {
            for (int j = i + 1, k = a.length; j < k; j++) {
                double d = a[i][j];
                if (d > 0) {
                    TextDataItem e1 = elements[i];
                    TextDataItem e2 = elements[j];
                    TextCluster c1 = allClusters.findClusterByElement(e1);
                    TextCluster c2 = allClusters.findClusterByElement(e2);
                    if (!c1.equals(c2)) {
                        int ci = idxMapping.getIndex(c1);
                        int cj = idxMapping.getIndex(c2);
                        clusterDistances[ci][cj] += d;
                        clusterDistances[cj][ci] += d;
                    }
                }
            }
        }

        boolean[] merged = new boolean[clusterDistances.length];
        for (int i = 0, n = clusterDistances.length; i < n; i++) {
            for (int j = i + 1, k = clusterDistances.length; j < k; j++) {
                TextCluster ci = idxMapping.getObject(i);
                TextCluster cj = idxMapping.getObject(j);
                int ni = ci.getElements().size();
                int nj = cj.getElements().size();
                clusterDistances[i][j] = clusterDistances[i][j] / (ni * nj);
                clusterDistances[j][i] = clusterDistances[i][j];
                // merge clusters if distance is below the threshold
                if (merged[i] == false && merged[j] == false) {
                    if (clusterDistances[i][j] <= distanceThreshold) {
                        List<TextDataItem> theItems = ci.getDataItems();
                        theItems.addAll(cj.getDataItems());

                        allClusters.remove(ci);
                        allClusters.remove(cj);

                        String mergedTitle = ci.getTitle() + "-" + cj.getTitle();
                        List<RetrievedDataEntry> theData = new ArrayList<RetrievedDataEntry>();

                        for (TextDataItem theItem : theItems) {
                            theData.add(theItem.getData());
                        }

                        DataSetCreator theCreator = new PageTextDataSetCreatorImpl("", theData);
                        TextCluster mergedCluster = new ClusterImpl(0, theCreator);

                        allClusters.add(mergedCluster);
                        merged[i] = true;
                        merged[j] = true;
                    }
                }
            }
        }
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

            AverageLinkAlgorithm ca = new AverageLinkAlgorithm(testData, a);
            Dendrogram dnd = ca.cluster();
            dnd.printAll();
        } catch (Exception ex) {
            Logger.getLogger(AverageLinkAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            assert (false);
        }
    }
}
