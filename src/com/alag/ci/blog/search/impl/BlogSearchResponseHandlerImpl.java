package com.alag.ci.blog.search.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alag.ci.blog.search.BlogQueryResult;
import com.alag.ci.blog.search.BlogSearchResponseHandler;
import com.alag.ci.blog.search.RetrievedBlogEntry;
import com.alag.ci.blog.search.XmlToken;

public abstract class BlogSearchResponseHandlerImpl extends DefaultHandler 
    implements BlogSearchResponseHandler {
    private BlogQueryResultImpl result = null;
    private List<RetrievedBlogEntry> entries = null;
    private RetrievedBlogEntryImpl item = null;
    private XmlToken whichToken = null;
    private Map<String, XmlToken> tagMap = null;
    private String charString = null;
    private DateFormat dateFormat = null;
    private DateFormat timeZoneDateFormat = null;
    private DateFormat timeZoneDayDateFormat = null;
    private DateFormat timeZoneYearDateFormat = null;

    public BlogSearchResponseHandlerImpl() {
        this.result = new BlogQueryResultImpl();
        this.entries = new ArrayList<RetrievedBlogEntry>();
        this.result.setResults(this.entries);
        this.tagMap = new HashMap<String, XmlToken>();
        for (XmlToken t : getXMLTokens()) {
            this.tagMap.put(t.getTag(), t);
        }
        //2007-01-10 11:25:56 Technorati - Tag search
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //"Mon, 01 Jan 2007 04:20:38 GMT" Bloglines
        this.timeZoneDayDateFormat = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss zzz");        
       //2007-01-10 19:21:49 GMT Technorati - Query search
        this.timeZoneYearDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        //"24 Dec 06 01:44:00 UTC" -- MSN, Feedster, Blogdigger
        this.timeZoneDateFormat = new SimpleDateFormat("dd MMM yy HH:mm:ss zzz");
    }
    
    protected abstract XmlToken [] getXMLTokens();

    public BlogQueryResult getBlogQueryResult() {
        return this.result;
    }

    protected XmlToken getWhichToken() {
        return whichToken;
    }

    protected RetrievedBlogEntryImpl getItem() {
        return item;
    }

    protected BlogQueryResultImpl getResult() {
        return result;
    }

    protected String getCharString() {
        return charString;
    }

    protected void setCharString(String charString) {
        this.charString = charString;
    }

    /**
     * Receive notification of the start of an element.
     * 
     * @param namespaceURI -
     *            The Namespace URI, or the empty string if the element has no
     *            Namespace URI or if Namespace processing is not being
     *            performed.
     * @param localName -
     *            The local name (without prefix), or the empty string if
     *            Namespace processing is not being performed.
     * @param qName -
     *            The qualified name (with prefix), or the empty string if
     *            qualified names are not available.
     * @param atts -
     *            The attributes attached to the element. If there are no
     *            attributes, it shall be an empty Attributes object.
     * @throws SAXException -
     *             Any SAX exception, possibly wrapping another exception.
     */
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        XmlToken t = this.tagMap.get(qName);
        if (t != null) {
            this.whichToken = t;
            if (isBlogEntryToken(t)) {
                this.item = new RetrievedBlogEntryImpl();
                this.entries.add(this.item);
            }
        }
        charString = "";
    }

    public void endElement(String namespaceURI, String sName, String qName ) 
        throws SAXException {
        this.whichToken = null;
    }

    protected abstract boolean isBlogEntryToken(XmlToken t) ;
    
    public abstract void characters(char buf[], int offset, int len) throws SAXException ;
    
    protected Date getParsedDate(String s) {
        System.out.println("Raw string: " + s);
        Date dateParsed = null;
        try {
            char lastChar = s.charAt(s.length() - 1);
            if (Character.isLetter(lastChar)) {
                char firstChar = s.charAt(0);
                if (Character.isLetter(firstChar)) {
                    dateParsed = this.timeZoneDayDateFormat.parse(s);
                } else {
                    if (s.indexOf("-") > 0) {
                        dateParsed = this.timeZoneYearDateFormat.parse(s);
                    } else {
                        dateParsed = this.timeZoneDateFormat.parse(s);
                    }
                }              
            } else {
                dateParsed = this.dateFormat.parse(s);
            }
            System.out.println(" 1. " + dateParsed.toString() + " was "+ s);
        } catch (ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + s + "\"");
        }
        return dateParsed;
    }
}
