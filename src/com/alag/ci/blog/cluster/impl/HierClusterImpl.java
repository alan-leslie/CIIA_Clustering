package com.alag.ci.blog.cluster.impl;

import com.alag.ci.blog.dataset.impl.CrawlerPage;
import java.io.StringWriter;

import com.alag.ci.blog.search.RetrievedDataEntry;
import com.alag.ci.cluster.TextCluster;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.cluster.hiercluster.HierCluster;
import java.util.ArrayList;
import java.util.List;

public class HierClusterImpl extends ClusterImpl implements HierCluster {

    private HierCluster child1 = null;
    private HierCluster child2 = null;
    private double similarity;

    public HierClusterImpl(int clusterId, HierCluster child1,
            HierCluster child2, double similarity, TextDataItem dataItem) {
        super(clusterId);
        this.child1 = child1;
        this.child2 = child2;
        this.similarity = similarity;
        if (dataItem != null) {
            this.addDataItem(dataItem);
        }
    }

    @Override
    public HierCluster getChild1() {
        return child1;
    }

    @Override
    public HierCluster getChild2() {
        return child2;
    }

    @Override
    public double getSimilarity() {
        return similarity;
    }

    @Override
    public double computeSimilarity(HierCluster o) {
        return this.getCenter().dotProduct(o.getCenter());
    }

    @Override
    public String toString() {
        StringWriter sb = new StringWriter();
        String blogDetails = getBlogDetails();
        if (blogDetails != null) {
            sb.append("Id=" + this.getClusterId() + " " + blogDetails);
        } else {
            sb.append("Id=" + this.getClusterId() + " similarity="
                    + this.similarity);
        }
        if (this.getChild1() != null) {
            sb.append(" C1=" + this.getChild1().getClusterId());
        }
        if (this.getChild2() != null) {
            sb.append(" C2=" + this.getChild2().getClusterId());
        }
        return sb.toString();
    }

    private String getBlogDetails() {
        String retVal = null;
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            TextDataItem textDataItem = this.getItems().get(0);
            if (textDataItem != null) {
                try {
                    RetrievedDataEntry blog = (RetrievedDataEntry) textDataItem.getData();
                    return blog.getTitle();
                } catch (ClassCastException exc) {
                }
                try {
                    CrawlerPage thePage = (CrawlerPage) textDataItem.getData();
                    return thePage.getTitle();
                } catch (ClassCastException exc) {
                }
            }
        }
        return null;
    }
    
    @Override
    public String getSource() {
        String retVal = null;
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            TextDataItem textDataItem = this.getItems().get(0);
            if (textDataItem != null) {
                try {
                    RetrievedDataEntry blog = (RetrievedDataEntry) textDataItem.getData();
                    return blog.getUrl();
                } catch (ClassCastException exc) {
                }
                try {
                    CrawlerPage thePage = (CrawlerPage) textDataItem.getData();
                    return thePage.getUrl();
                } catch (ClassCastException exc) {
                }
            }
        }
        return null;
    }
    
        @Override
    public String getTitle() {
        String retVal = null;
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            TextDataItem textDataItem = this.getItems().get(0);
            if (textDataItem != null) {
                try {
                    RetrievedDataEntry blog = (RetrievedDataEntry) textDataItem.getData();
                    return blog.getTitle();
                } catch (ClassCastException exc) {
                }
                try {
                    CrawlerPage thePage = (CrawlerPage) textDataItem.getData();
                    return thePage.getTitle();
                } catch (ClassCastException exc) {
                }
            }
        }
        return null;
    }
 
    @Override
    public List<TextCluster> getSubClusters() {
        List<TextCluster> theChildren = new ArrayList<TextCluster>();
        TextCluster firstChild = getChild1();
        if(firstChild != null){
            theChildren.add(firstChild);
        }
        
        TextCluster secondChild = getChild2();
        if(secondChild != null){
            theChildren.add(secondChild);
        }

        return theChildren;
    }
    
    @Override
    public List<HierCluster> getChildren(){
        List<HierCluster> theChildren = new ArrayList<HierCluster>();

        HierCluster firstChild = getChild1();
        if(firstChild != null){
            theChildren.add(firstChild);
        }
        
        HierCluster secondChild = getChild2();
        if(secondChild != null){
            theChildren.add(secondChild);
        }

        return theChildren;
    }
    
    @Override
    public String asXML() {
        StringBuilder theBuilder = new StringBuilder();
        theBuilder.append("<cluster>\n");
        theBuilder.append("<name>");
        List<TextCluster> theSubClusters = getSubClusters();
        if(theSubClusters != null &&
                theSubClusters.size() > 1){
            theBuilder.append(Integer.toString(getClusterId())); 
        } else {
            theBuilder.append(getElementsAsString());
        }
        theBuilder.append("</name>\n");
        if (theSubClusters != null) {
            for (TextCluster subCluster : theSubClusters) {
                theBuilder.append(subCluster.asXML());
            }
        }
        theBuilder.append("</cluster>\n");
        String theXML = theBuilder.toString();
        return theXML;
    }
}
