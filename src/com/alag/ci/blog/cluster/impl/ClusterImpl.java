package com.alag.ci.blog.cluster.impl;

import java.util.*;

import com.alag.ci.blog.search.RetrievedBlogEntry;
import com.alag.ci.blog.dataset.impl.CrawlerPage;
import com.alag.ci.cluster.*;
import com.alag.ci.textanalysis.*;
import com.alag.ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterImpl implements TextCluster {
    private static int CLUSTER_NO = 3;
    private static int idCounter = 0;
    private TagMagnitudeVector center = null;
    private List<TextDataItem> items = null;
    private List<TextCluster> subClusters = null;
    private int clusterId;

    public ClusterImpl(int clusterId) {
        this.clusterId = idCounter++;
        this.items = new ArrayList<TextDataItem>();
    }
    
    public ClusterImpl(int clusterId,
            TextDataItem theItem) {
        this.clusterId = idCounter++;
        this.items = new ArrayList<TextDataItem>();
        items.add(theItem);
    }
    
    public ClusterImpl(int clusterId,
            DataSetCreator pt) {
        this.clusterId = clusterId;
        try {
            this.items = pt.createLearningData();
        } catch (Exception ex) {
            this.items = new ArrayList<TextDataItem>();
            Logger.getLogger(ClusterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void computeCenter() {
        if (this.items.isEmpty()) {
            return;
        }
        List<TagMagnitudeVector> tmList = new ArrayList<TagMagnitudeVector>();
        for (TextDataItem item : items) {
            tmList.add(item.getTagMagnitudeVector());
        }
        List<TagMagnitude> emptyList = Collections.emptyList();
        TagMagnitudeVector empty = new TagMagnitudeVectorImpl(emptyList);
        this.center = empty.add(tmList);
    }

    @Override
    public int getClusterId() {
        return this.clusterId;
    }

    @Override
    public void addDataItem(TextDataItem item) {
        items.add(item);
    }

    @Override
    public TagMagnitudeVector getCenter() {
        return center;
    }

    public List<TextDataItem> getItems() {
        return items;
    }

    public void setCenter(TagMagnitudeVector center) {
        this.center = center;
    }

    @Override
    public void clearItems() {
        this.items.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id=").append(this.clusterId);
        sb.append("No Of Items=").append(Integer.toString(this.items.size()));
        //    sb.append("\n" + this.center);
        for (TextDataItem item : items) {
            if (item != null) {
                boolean typeFound = false;
                try {
                    RetrievedBlogEntry blog = (RetrievedBlogEntry) item.getData();
                    sb.append("\nTitle=").append(blog.getTitle());
                    sb.append("\nExcerpt=").append(blog.getExcerpt());
                    typeFound = true;
                } catch (ClassCastException exc) {
                }

                if (!typeFound) {
                    try {
                        CrawlerPage thePage = (CrawlerPage) item.getData();
                        sb.append("\nTitle=").append(thePage.getTitle());
                        sb.append("\nURL=").append(thePage.getURL());
                        typeFound = true;
                    } catch (ClassCastException exc) {
                    }
                }

            }
        }

        if (subClusters != null) {
            for (TextCluster cluster : subClusters) {
                sb.append(cluster.toString());
            }
        }

        return sb.toString();
    }

    @Override
    public List<TextDataItem> getDataItems() {
        return items;
    }

    @Override
    public String getSource() {
        String retVal = null;
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            TextDataItem textDataItem = this.getItems().get(0);
            if (textDataItem != null) {
                try {
                    RetrievedBlogEntry blog = (RetrievedBlogEntry) textDataItem.getData();
                    return blog.getUrl();
                } catch (ClassCastException exc) {
                }
                try {
                    CrawlerPage thePage = (CrawlerPage) textDataItem.getData();
                    return thePage.getURL();
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
                    RetrievedBlogEntry blog = (RetrievedBlogEntry) textDataItem.getData();
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
    public void hierCluster(Clusterer theClusterer) {
        if (theClusterer != null) {
            if (this.items != null) {
                theClusterer.setDataSet(this.items);

                if (this.items.size() > CLUSTER_NO) {
                    subClusters = theClusterer.cluster();
                    
                    for(TextCluster theSubCluster: subClusters){
                        theSubCluster.hierCluster(theClusterer);
                    }
                    
                    int clustSize = subClusters.size();
                    int dummy = 0;
                                                        
                    // todo for each of the sub clusters 
                    // cluster again
                } else {
                    subClusters = new ArrayList<TextCluster>();
                    for(TextDataItem theItem: items){
                        // todo - get the correct id
                        TextCluster theSubCluster = new ClusterImpl(1, theItem);
                        boolean add = subClusters.add(theSubCluster);
                    }
                }
            }
        }
    }

    @Override
    public List<TextCluster> getSubClusters() {
        return subClusters;
    }
}
