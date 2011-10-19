package iweb2.clustering.rock;

import clustering.ui.TreeView;
import iweb2.clustering.hierarchical.Dendrogram;
import com.alag.ci.blog.cluster.impl.ClusterImpl;
import com.alag.ci.blog.dataset.impl.LocalData;
import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.DataSetCreator;
import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import iweb2.clustering.utils.XMLFile;
import iweb2.similarity.JaccardCoefficient;
import iweb2.similarity.SimilarityMeasure;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ROCKAlgorithm {

    private TextDataItem[] points;
    private int k;
    private double th;
    private SimilarityMeasure similarityMeasure;
    private LinkMatrix linkMatrix;

    /**
     * 
     * @param k desired number of clusters.
     * @param th threshold value to identify neighbors among points.
     */
    public ROCKAlgorithm(TextDataItem[] points, int k, double th) {
        this.points = points;
        this.k = k;
        this.th = th;
        this.similarityMeasure = new JaccardCoefficient();
        //this.similarityMeasure = new CosineSimilarity();
        this.linkMatrix = new LinkMatrix(points, similarityMeasure, th);
    }

    public Dendrogram cluster() {
        //  Create a new cluster out of every point.
        List<TextCluster> initialClusters = new ArrayList<TextCluster>();
        for (int i = 0, n = points.length; i < n; i++) {
            TextCluster cluster = new ClusterImpl(0, points[i]);
            initialClusters.add(cluster);
        }
        double g = Double.POSITIVE_INFINITY;
        Dendrogram dnd = new Dendrogram("Goodness");
        dnd.addLevel(String.valueOf(g), initialClusters);

        MergeGoodnessMeasure goodnessMeasure = new MergeGoodnessMeasure(th);

        ROCKClusters allClusters = new ROCKClusters(initialClusters,
                linkMatrix,
                goodnessMeasure);

        int nClusters = allClusters.size();
        while (nClusters > k) {
            int nClustersBeforeMerge = nClusters;
            g = allClusters.mergeBestCandidates();
            nClusters = allClusters.size();
            if (nClusters == nClustersBeforeMerge) {
                // there are no linked clusters to merge
                break;
            }
            dnd.addLevel(String.valueOf(g), allClusters.getAllClusters());
        }

        System.out.println("Number of clusters: " + allClusters.getAllClusters().size());
        
        return dnd;
    }

    public int getK() {
        return k;
    }

    public double getTh() {
        return th;
    }

    public SimilarityMeasure getSimilarityMeasure() {
        return similarityMeasure;
    }

    public LinkMatrix getLinkMatrix() {
        return linkMatrix;
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

            int k = 1;
            double th = 0.2;
            ROCKAlgorithm rock = new ROCKAlgorithm(testData, k, th);
            Dendrogram dnd = rock.cluster();
            dnd.printAll();
            XMLFile.writeXML("ROCKTest.xml", dnd.asXML());

            TreeView.createAndShowGUI(dnd);
        } catch (Exception ex) {
            Logger.getLogger(ROCKAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            assert (false);
        }
    }
}
