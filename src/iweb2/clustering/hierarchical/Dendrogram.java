package iweb2.clustering.hierarchical;

import com.alag.ci.cluster.TextCluster;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dendrogram {

    /*
     * Clusters by level.
     */
    private Map<Integer, ClusterSet> entryMap;
    private Map<Integer, String> levelLabels;
    private Integer nextLevel;
    private String levelLabelName;
    
    public Dendrogram(String levelLabelName) {
        entryMap = new LinkedHashMap<Integer, ClusterSet>();
        levelLabels = new LinkedHashMap<Integer, String>();
        nextLevel = 1;
        this.levelLabelName = levelLabelName; 
    }
    
    public int addLevel(String label, TextCluster cluster) {
        List<TextCluster> values = new ArrayList<TextCluster>();
        values.add(cluster);
        return addLevel(label, values);
    }
    
    /**
     * Creates a new dendrogram level using copies of provided clusters. 
     */
    public int addLevel(String label, Collection<TextCluster> clusters) {
        
        ClusterSet clusterSet = new ClusterSet();
        
        for(TextCluster c : clusters) {
            // copy cluster before adding - over time cluster elements may change
            // but for dendrogram we want to keep current state.
            clusterSet.add(c.copy());
        }
        
        int level = nextLevel;
        
        entryMap.put(level, clusterSet);
        levelLabels.put(level, label);
        
        nextLevel++;
        return level;
    }
    
    /**
     * Replaces clusters in the specified level. If level doesn't exist it will 
     * be created.
     *  
     * @param level dendrogram level.
     * @param label level description.
     * @param clusters clusters for the level.
     * @return 
     */
    public void setLevel(int level, String label, Collection<TextCluster> clusters) {
        
        ClusterSet clusterSet = new ClusterSet();
        
        for(TextCluster c : clusters) {
            clusterSet.add(c.copy());
        }
        
        System.out.println("Setting cluster level: "+level);
        
        entryMap.put(level, clusterSet);
        levelLabels.put(level, label);
       
        if( level >= nextLevel ) {
            nextLevel = level + 1;
        }
    }
    
    public void printAll() {
        for(Map.Entry<Integer, ClusterSet> e : entryMap.entrySet()) {
            Integer level = e.getKey();
            print(level);
        }
    }
    
    public void print(int level) {
        String label = levelLabels.get(level);
        ClusterSet clusters = entryMap.get(level);
        System.out.println("Clusters for: level=" + level + ", " + 
                levelLabelName + "=" + label);
        for(TextCluster c : clusters.getAllClusters()) {
        	if (c.getElements().size() > 1) {
        	System.out.println("____________________________________________________________\n");
            System.out.println(c.getClusterId());                       
            System.out.println(c.getElementsAsString());
        	System.out.println("____________________________________________________________\n\n");
        	}
        }
    }
    
    public int getTopLevel() {
        return nextLevel - 1;
    }
    
    public List<Integer> getAllLevels() {
        return new ArrayList<Integer>( entryMap.keySet() );
    }
    
    public String getLabelForLevel(int level) {
        return levelLabels.get(level);
    }
    
    public List<TextCluster> getClustersForLevel(int level) {
        ClusterSet cs = entryMap.get(level);
        return cs.getAllClusters();
    }
    
}