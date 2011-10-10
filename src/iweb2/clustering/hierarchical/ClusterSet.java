package iweb2.clustering.hierarchical;


//import iweb2.ch4.model.Cluster;
//import iweb2.ch4.model.MultiDimensionalDataPoint;

import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set of clusters.
 */
public class ClusterSet {

    private Set<TextCluster> allClusters = new HashSet<TextCluster>();
    
    public TextCluster findClusterByElement(TextDataItem e) {
        TextCluster cluster = null;
        for(TextCluster c : allClusters) {
            // todo sort his out
//            if( c.contains(e) ) {
//                cluster = c;
//                break;
//            }
        }
        return cluster;
    }

    public List<TextCluster> getAllClusters() {
        return new ArrayList<TextCluster>(allClusters);
    }
    
    public boolean add(TextCluster c) {
        return allClusters.add(c);
    }
    
    public boolean remove(TextCluster c) {
        return allClusters.remove(c);
    }
    
    public int size() {
        return allClusters.size();
    }
    
//    public ClusterSet copy() {
//        ClusterSet clusterSet = new ClusterSet();
//        for(Cluster c : this.allClusters ) {
//            Cluster clusterCopy = c.copy();
//            clusterSet.add(clusterCopy);
//        }
//        return clusterSet;
//    }
}
