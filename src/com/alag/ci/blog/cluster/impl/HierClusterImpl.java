package com.alag.ci.blog.cluster.impl;

import java.io.StringWriter;

import com.alag.ci.blog.search.RetrievedBlogEntry;
import com.alag.ci.cluster.TextDataItem;
import com.alag.ci.cluster.hiercluster.HierCluster;

public class HierClusterImpl extends ClusterImpl implements HierCluster {
    private HierCluster child1 = null;
    private HierCluster child2 = null;
    private double similarity;
    
    public HierClusterImpl(int clusterId,HierCluster child1,
            HierCluster child2, double similarity, TextDataItem dataItem) {
        super(clusterId);
        this.child1 = child1;
        this.child2 = child2;
        this.similarity = similarity;
        if (dataItem != null) {
            this.addDataItem(dataItem);
        }
    }

    public HierCluster getChild1() {
        return child1;
    }

    public HierCluster getChild2() {
        return child2;
    }

    public double getSimilarity() {
        return similarity;
    }
    
    public double computeSimilarity(HierCluster o) {
        return this.getCenter().dotProduct(o.getCenter());
    }
    
    public String toString() {
        StringWriter sb = new StringWriter();
        String blogDetails = getBlogDetails();
        if (blogDetails != null) {
            sb.append("Id=" + this.getClusterId() + " " + blogDetails);
        } else {
            sb.append("Id=" + this.getClusterId() + " similarity="+ 
                this.similarity );
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
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            TextDataItem textDataItem = this.getItems().get(0);
            if (textDataItem != null) {
                RetrievedBlogEntry blog = (RetrievedBlogEntry) textDataItem.getData();
                return blog.getTitle();
            }
        }
        return null;
    }
    
}
