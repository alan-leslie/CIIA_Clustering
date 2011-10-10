package com.alag.ci.blog.dataset.impl;

import com.alag.ci.blog.search.RetrievedDataEntry;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author al
 */
public class LocalData implements RetrievedDataEntry {
    private String dirName = "DIR";
    private String fileName = "";
    private String theText = "";
    private String theURL = "URL";
    private String theTitle = "";

    public LocalData(String fileName, 
            String theText) {
        this.theText = theText;
        this.fileName = fileName;
        this.theTitle = fileName;
        this.theURL = fileName;
    }
    
    @Override
    public String getText(){
        return theText;
    }
    
    @Override
    public String getTitle(){
        return theTitle;
    }
    
    @Override
    public String getUrl(){
        return theURL;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocalData other = (LocalData) obj;
        if ((this.theURL == null) ? (other.theURL != null) : !this.theURL.equals(other.theURL)) {
            return false;
        }
 
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.theURL != null ? this.theURL.hashCode() : 0);
        return hash;
    } 

    @Override
    public String getName() {
        return getTitle();
    }

    @Override
    public String getExcerpt() {
        return getText();
    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public Date getLastUpdateTime() {
        Date retVal = new Date();
        return retVal;
    }

    @Override
    public Date getCreationTime() {
        Date retVal = new Date();
        return retVal;
    }
}
    
