package com.alag.ci.blog.search.impl.technorati;

import org.xml.sax.SAXException;

import com.alag.ci.blog.search.XmlToken;
import com.alag.ci.blog.search.impl.BlogSearchResponseHandlerImpl;
import com.alag.ci.blog.search.impl.RetrievedBlogEntryImpl;

public class TechnoratiResponseHandler extends BlogSearchResponseHandlerImpl {
    /**
     * <document> <result> <query>[query string]</query> <querycount>[number of
     * matches]</querycount> <querytime>[duration of query]</querytime>
     * <rankingstart>[value of start parameter]</rankingstart> </result> <item>
     * <weblog> <name>[name of blog containing match]</name> <url>[blog URL]</url>
     * <rssurl>[blog RSS URL]</rssurl> <atomurl>[blog Atom URL]</atomurl>
     * <inboundblogs>[inbound blogs]</inboundblogs> <inboundlinks>[inbound
     * links]</inboundlinks> <lastupdate>[date blog last updated]</lastupdate>
     * </weblog> <title>[title of entry]</title> <excerpt>[blurb from entry
     * with search term highlighted]</excerpt> <created>[date entry was
     * created]</created> <permalink>[URL of blog entry]</permalink> </item>
     * ... </document> </tapi>
     * 
     * <document> <result> <query>[tag]</query> <postsmatched>[number of posts
     * that match the tag]</postsmatched> <blogsmatched>[number of blogs that
     * match the tag]</blogsmatched> <start>[value of the start parameter]</start>
     * <limit>[value of limit parameter]</limit> <querytime>[duration of query]</querytime>
     * </result> <item> .... </item> </document>
     */

    public enum TechnoratiXmlToken implements XmlToken {
        COUNT("querycount"),POSTSMATCHED("postsmatched"),
        WEBLOG("weblog"), NAME("name"), LASTUPDATE(
                "lastupdate"), URL("url"), TITLE("title"), EXCERPT("excerpt"), ITEM(
                "item"), CREATED("created"), FIRSTNAME("firstname"),LASTNAME("lastname");

        private String tag = null;

        TechnoratiXmlToken(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }

    }
    
    private String firstName = null;
 
    protected XmlToken [] getXMLTokens() {
        return TechnoratiXmlToken.values();
    }
 
   protected boolean isBlogEntryToken(XmlToken t) {
        return (TechnoratiXmlToken.ITEM.compareTo(
                (TechnoratiXmlToken)t) == 0);
    }
    
    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = this.getCharString() +  new String(buf, offset, len);
        this.setCharString(s);
        System.out.println("Token=" + this.getWhichToken() + " -->" + s);
        TechnoratiXmlToken token = (TechnoratiXmlToken) this.getWhichToken();
        RetrievedBlogEntryImpl item = getItem();
        if (token != null) {
            switch (token) {
                case POSTSMATCHED: {
                    this.getResult().setQueryCount(new Integer(s));
                    break;
                }
                case COUNT: {
                    this.getResult().setQueryCount(new Integer(s));
                    break;
                }
                case URL: {
                    item.setUrl(s);
                    break;
                }
                case NAME: {
                    item.setName(s);
                    break;
                }
                case LASTUPDATE: {
                    item.setLastUpdateTime(getParsedDate(s));
                    break;
                }
                case CREATED: {
                    item.setCreationTime(getParsedDate(s));
                    break;
                }
                case TITLE: {
                    item.setTitle(s);
                    break;
                }
                case EXCERPT: {
                    item.setExcerpt(s);
                    break;
                }
                case FIRSTNAME: {
                    this.firstName = s;
                    break;
                }
                case LASTNAME: {
                    item.setAuthor(this.firstName + " " + s);
                    this.firstName = null;
                }
            }
        }

    }
}
