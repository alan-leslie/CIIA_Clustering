/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.blog.dataset.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author al
 */
public class CrawlerPage {
    private String dirName;
    private String fileName;
    private String theText = "";
    private String theURL = "";
    private String theTitle = "";
    private static final String propDir = "properties/";
    private static final String txtDir = "txt/";
    private static final String propExt = ".properties";
    private static final String txtExt = ".txt";
    
    public CrawlerPage(String dirName, 
            String fileName) throws IOException {
        this.dirName = dirName;
        this.fileName = fileName;
        
        try{
            StringBuilder theTextBuilder = new StringBuilder();
            String fullTxtFileName = this.dirName + txtDir + this.fileName + txtExt;
            BufferedReader in = new BufferedReader(new FileReader(fullTxtFileName));
            String str;
            while ((str = in.readLine()) != null) {
                theTextBuilder.append(str);
            }
            in.close();
            theText = theTextBuilder.toString();            
        } catch(IOException exc) {
            throw exc;           
        } finally {
            // clean up and close files
        }
        
        try{
            String fullPropFileName = this.dirName + propDir + this.fileName + propExt;
            BufferedReader in = new BufferedReader(new FileReader(fullPropFileName));
            String str;
            while ((str = in.readLine()) != null) {
                if(str.indexOf("title:") == 0){
                    theTitle = str.substring(6);
                }
                               
                if(str.indexOf("url:") == 0){
                    theURL = str.substring(4);
                }
            }
            in.close();          
        } catch(IOException exc) {
            throw exc;           
        } finally {
            // clean up and close files
        }        
    }
    
    public String getText(){
        return theText;
    }
    
    public String getTitle(){
        return theTitle;
    }
    
    public String getURL(){
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
        final CrawlerPage other = (CrawlerPage) obj;
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
    
    public static void main(String [] args) throws Exception {
        CrawlerPage thePage = new CrawlerPage("/home/al/lasers/crawl-1317050427563/processed/3/", "1");

        System.out.println(thePage.getTitle());      
        System.out.println(thePage.getURL());  
    }
}
