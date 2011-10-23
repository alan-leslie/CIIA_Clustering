package com.alag.ci.blog.cluster.impl;

import com.alag.ci.blog.dataset.impl.PageTextDataSetCreatorImpl;
import com.alag.ci.blog.search.RetrievedDataEntry;
import java.util.*;

import com.alag.ci.cluster.*;
import com.alag.ci.textanalysis.*;
import com.alag.ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterImpl implements TextCluster {

    public static int CLUSTER_NO = 3;
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
        this.clusterId = idCounter++;
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
        List<TextDataItem> retVal = null;

        if (items != null) {
            retVal = new ArrayList<TextDataItem>();
            retVal.addAll(items);
        }

        return retVal;
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
                Map<String, String> theAttributes = item.getAttributeMap();

                String theTitle = theAttributes.get("Title");
                String theExcerpt = theAttributes.get("Excerpt");
                String theURL = theAttributes.get("URL");
                String theText = theAttributes.get("Text");

                if (theTitle != null) {
                    sb.append("\nTitle=").append(theTitle);
                }

                if (theExcerpt != null) {
                    sb.append("\nExcerpt").append(theExcerpt);
                }

                if (theURL != null) {
                    sb.append("\nURL=").append(theURL);
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
        return getItems();
    }

    @Override
    public String getSource() {
        String retVal = null;
        if ((this.getItems() != null) && (this.getItems().size() > 0)) {
            TextDataItem textDataItem = this.getItems().get(0);
            if (textDataItem != null) {
                Map<String, String> theAttributes = textDataItem.getAttributeMap();

                String theURL = theAttributes.get("URL");

                if (theURL != null) {
                    retVal = theURL;
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
                Map<String, String> theAttributes = textDataItem.getAttributeMap();

                String theTitle = theAttributes.get("Title");

                if (theTitle != null) {
                    retVal = theTitle;
                }
            }
        }

        return null;
    }

    @Override
    public void hierCluster(Clusterer theClusterer) {
        if (theClusterer != null) {
            List<RetrievedDataEntry> theData = new ArrayList<RetrievedDataEntry>();
            for (TextDataItem theItem : items) {
                theData.add(theItem.getData());
            }
            DataSetCreator subPageText = new PageTextDataSetCreatorImpl("", theData);
            try {
                List<TextDataItem> spList = subPageText.createLearningData();
            } catch (Exception ex) {
                Logger.getLogger(ClusterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (this.items != null) {
                if (this.items.size() > CLUSTER_NO) {
                    theClusterer.setDataSet(this.items);
                    subClusters = theClusterer.cluster();

                    for (TextCluster theSubCluster : subClusters) {
                        theSubCluster.hierCluster(theClusterer);
                    }
                } else {
                    if (this.items.size() > 1) {
                        subClusters = new ArrayList<TextCluster>();
                        for (TextDataItem theItem : items) {
                            TextCluster theSubCluster = new ClusterImpl(0, theItem);
                            boolean add = subClusters.add(theSubCluster);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<TextCluster> getSubClusters() {
        List<TextCluster> theSubClusters = null;

        if (subClusters != null) {
            theSubClusters = new ArrayList<TextCluster>();
            theSubClusters.addAll(subClusters);
        }

        return theSubClusters;
    }

    @Override
    public Set<TextDataItem> getElements() {
        Set<TextDataItem> retVal = new HashSet<TextDataItem>();

        if (items != null) {
            retVal.addAll(items);
        }

        return retVal;
    }

    @Override
    public void setClusterId(int newId) {
        clusterId = newId;
    }

    @Override
    public TextCluster copy() {
        ClusterImpl copy = new ClusterImpl(clusterId);
        copy.setCenter(getCenter());
        copy.setItems(getItems());
        copy.setSubClusters(getSubClusters());

        return copy;
    }

    @Override
    public String getElementsAsString() {
        StringBuffer buf = new StringBuffer();
        for (TextDataItem e : items) {
            if (buf.length() > 0) {
                buf.append(",\n");
            }
            buf.append(e.getData().getTitle());
        }

        return "{" + buf.toString() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClusterImpl other = (ClusterImpl) obj;
        if (items == null) {
            if (other.items != null) {
                return false;
            }
        } else {
            if (!items.equals(other.items)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.clusterId;
        return hash;
    }

    private void setItems(List<TextDataItem> items) {
        this.items = items;
    }

    private void setSubClusters(List<TextCluster> subClusters) {
        this.subClusters = subClusters;
    }

    // should check if cluster exists already
    // should check that all of the parents data items 
    // are included (precon)
    @Override
    public void addSubCluster(TextCluster cluster) {
        if (subClusters == null) {
            subClusters = new ArrayList<TextCluster>();
        }

        subClusters.add(cluster);
    }

    @Override
    public String asXML() {
        StringBuilder theBuilder = new StringBuilder();
        theBuilder.append("<cluster>\n");
        theBuilder.append("<name>");
        if(getElements().size() > 1){
            theBuilder.append(Integer.toString(getClusterId())); 
        } else {
            theBuilder.append(getElementsAsString());
        }
        theBuilder.append("</name>\n");
        if (subClusters != null) {
            for (TextCluster subCluster : subClusters) {
                theBuilder.append(subCluster.asXML());
            }
        }
        theBuilder.append("</cluster>\n");
        String theXML = theBuilder.toString();
        return theXML;
    }
}
