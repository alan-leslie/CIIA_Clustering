package com.alag.ci.blog.search.impl.bloglines;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.alag.ci.blog.search.XmlToken;
import com.alag.ci.blog.search.impl.BlogSearchResponseHandlerImpl;
import com.alag.ci.blog.search.impl.RetrievedBlogEntryImpl;


public class BlogLinesResponseHandler extends BlogSearchResponseHandlerImpl {
    /**
     * <publicapi>
  <link type="rss" title="Bloglines Search: bloglines+freedback" href="http://www.bloglines.com/search?q=bloglines+freedback&s=f&pop=l&news=m&format=rss"/>
  <resultset set="main" qtype="article" estimate="115" found="71">
    <result id="0" siteid="521144" itemid="455" inline="0" date="Fri, 20 Oct 2006 20:46:00 GMT" citations="0">
      <site nsubs="26">
        <name>A Drivel Runs Through It</name>
        <url>http://www.patiastephens.com</url>
        <feedurl>http://www.patiastephens.com/atom.xml</feedurl>
      </site>
      <title><B>Bloglines</B>, you are breaking my heart</title>
      <author>Patia</author>
      <abstract>
        Here then, is my <B>freedback</B> for the <B>Bloglines</B> folks:
      </abstract>
      <url>
        http://www.patiastephens.com/2006/10/bloglines-you-are-breaking-my-heart.html
      </url>
    </result>
    <result id="1" siteid="1732365" itemid="584" inline="0" date="Tue, 17 Oct 2006 14:16:00 GMT" citations="0">
      <site nsubs="16">
        <name>The Social Customer Manifesto - recent comments</name>
        <url>http://www.socialcustomer.com/</url>
        <feedurl>http://www.socialcustomer.com/comments.rdf</feedurl>
      </site>
      <title>
        Craig comments on ""We&apos;re Listening""
      </title>
      <abstract>
        <B>Bloglines</B> support this and actually listen.  I&apos;ve had them visit my site and read my <B>freedback</B>.  :) I&apos;d suggest perhaps rather than companies...
      </abstract>
      <url>
        http://www.socialcustomer.com/2006/10/were_listening.html
      </url>
    </result>
  </resultset>
</publicapi>
     *
     */
    public enum BlogLinesXmlToken implements XmlToken {
         NAME("name"), URL("url"), TITLE("title"), ABSTRACT("abstract"), 
         RESULT("result"), CREATED("date"),AUTHOR("author"),FOUND("found");

        private String tag = null;

        BlogLinesXmlToken(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }

    protected XmlToken [] getXMLTokens() {
        return BlogLinesXmlToken.values();
    }
    
    protected boolean isBlogEntryToken(XmlToken t) {
        return (BlogLinesXmlToken.RESULT.compareTo(
                (BlogLinesXmlToken)t) == 0);
    }
    
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        super.startElement(namespaceURI, localName, qName, atts);
   
        String dateValue = atts.getValue(BlogLinesXmlToken.CREATED.getTag());
        if (dateValue != null) {
            RetrievedBlogEntryImpl item = getItem();
            item.setLastUpdateTime(getParsedDate(dateValue));
        }
        String numFound = atts.getValue(BlogLinesXmlToken.FOUND.getTag());
        if (numFound != null) {
           this.getResult().setQueryCount(new Integer(numFound));
        }
        this.setCharString("");

    }
    
    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = this.getCharString() +  new String(buf, offset, len);
        this.setCharString(s);
      // System.out.println("Token=" + s);
        BlogLinesXmlToken token = (BlogLinesXmlToken) this.getWhichToken();
        RetrievedBlogEntryImpl item = getItem();
        if ((token != null) && (item != null) ){
            switch (token) {
            case URL: {
                item.setUrl(s);
                break;
            }
            case NAME: {
                item.setName(s);
                break;
            }
            case TITLE: {
                item.setTitle(s);
                break;
            }
            case AUTHOR: {
                item.setAuthor(s);
                break;
            }
            case ABSTRACT: {
                item.setExcerpt(s);
                break;
            }
        }
        }
    }
}
    
