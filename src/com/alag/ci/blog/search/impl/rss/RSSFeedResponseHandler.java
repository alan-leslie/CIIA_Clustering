package com.alag.ci.blog.search.impl.rss;

import org.xml.sax.SAXException;

import com.alag.ci.blog.search.XmlToken;
import com.alag.ci.blog.search.impl.BlogSearchResponseHandlerImpl;
import com.alag.ci.blog.search.impl.RetrievedBlogEntryImpl;

public class RSSFeedResponseHandler extends BlogSearchResponseHandlerImpl {

    public enum RSSFeedXmlToken implements XmlToken {
        ITEM("item"), TITLE("title"), AUTHOR("author"), LINK("link"), DESCRIPTION(
                "description"), PUBDATE("pubDate");

        private String tag = null;

        RSSFeedXmlToken(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }

    protected XmlToken[] getXMLTokens() {
        return RSSFeedXmlToken.values();
    }

    protected boolean isBlogEntryToken(XmlToken t) {
     //   System.out.println("Token = " + t.getTag());
        return (RSSFeedXmlToken.ITEM.compareTo((RSSFeedXmlToken) t) == 0);
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = this.getCharString() + new String(buf, offset, len);
        this.setCharString(s);
      //  System.out.println("Token=" + s);
        RSSFeedXmlToken token = (RSSFeedXmlToken) this.getWhichToken();
        RetrievedBlogEntryImpl item = getItem();
        if (token != null) {
            switch (token) {
            case LINK: {
                if (item != null) {
                    item.setUrl(s);
                }
                break;
            }
            case TITLE: {
                if (item != null) {
                    item.setTitle(s);
                    item.setName(s);
                }
                break;
            }
            case AUTHOR: {
                if (item != null) {
                    item.setAuthor(s);
                }
                break;
            }
            case DESCRIPTION: {
                if (item != null) {
                    item.setExcerpt(s);
                }
                break;
            }
            case PUBDATE: {
                if (item != null) {
                   // item.setCreationTime(getParsedDate(s));
                    item.setLastUpdateTime(getParsedDate(s));
                }
                break;
            }
            }
        }
    }
}