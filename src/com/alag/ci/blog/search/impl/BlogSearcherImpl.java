package com.alag.ci.blog.search.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alag.ci.blog.search.BlogQueryParameter;
import com.alag.ci.blog.search.BlogQueryResult;
import com.alag.ci.blog.search.BlogSearchResponseHandler;
import com.alag.ci.blog.search.BlogSearcher;
import com.alag.ci.blog.search.BlogSearcherException;
import com.alag.ci.blog.search.BlogQueryParameter.QueryParameter;
import com.alag.ci.blog.search.impl.rss.RSSFeedResponseHandler;

public abstract class BlogSearcherImpl implements BlogSearcher {
    private static final String JAXP_PROPERTY_NAME = 
        "javax.xml.parsers.SAXParserFactory";
    private static final String APACHE_XERCES_SAX_PARSER =
        "org.apache.xerces.jaxp.SAXParserFactoryImpl";

    private SAXParser parser = null;
    private Map<QueryParameter, String> paramStringMap = null;

    protected BlogSearcherImpl() throws BlogSearcherException {
        createSAXParser();
        initializeParamStringMap();
    }
    
    private void createSAXParser() throws BlogSearcherException {
        if (System.getProperty(JAXP_PROPERTY_NAME) == null) {
            System.setProperty(JAXP_PROPERTY_NAME, APACHE_XERCES_SAX_PARSER);
        }
        // First make an instance of a parser factory,
        // then use that to create a parser object.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            this.parser = factory.newSAXParser();
        } catch(ParserConfigurationException e) {
            throw new BlogSearcherException("SAX parser not found",e);
        } catch(SAXException se) {
            throw new BlogSearcherException("SAX exception",se);
        }
    }

    protected void initializeParamStringMap() {
        paramStringMap = new HashMap<QueryParameter, String>();
    }

    protected Map<QueryParameter, String> getParamStringMap() {
        return paramStringMap;
    }
    
    protected SAXParser getSAXParser() {
        return this.parser;
    }

    public BlogQueryResult getRelevantBlogs(BlogQueryParameter param)
            throws BlogSearcherException {
        BlogQueryResult result = new NullBlogQueryResultImpl();
        HttpClient client = new HttpClient();
        HttpMethod method = getMethod(param);
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                InputStream is = method.getResponseBodyAsStream();
                result = parseContent(is);
                is.close();
            }
        } catch (HttpException he) {
            throw new BlogSearcherException("HTTP exception ", he);
        } catch (IOException ioe) {
            throw new BlogSearcherException(
                    "IOException while getting response body", ioe);
        } finally {
            method.releaseConnection();
        }
        return result;
    }

    protected BlogSearchResponseHandler getBlogSearchResponseHandler() {
        return new RSSFeedResponseHandler();
    }

    protected abstract HttpMethod getMethod(BlogQueryParameter param);

    private BlogQueryResult parseContent(InputStream is)
            throws BlogSearcherException {
        try {
            BlogSearchResponseHandler h = getBlogSearchResponseHandler();
            this.getSAXParser().parse(is, (DefaultHandler) h);
            return h.getBlogQueryResult();
        } catch (SAXException se) {
            throw new BlogSearcherException("Error parsing Response XML", se);
        } catch (IOException ioe) {
            throw new BlogSearcherException("IOException while parsing XML",
                    ioe);
        }
    }

    /**
     * UTF-8 is the encoding scheme
     * 
     * @param s
     * @return
     * @throws BlogSearcherException
     */
    public static String urlEncode(String s) {
        String result = s;
        try {
            result = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //should never be thrown
            System.out.println("Unsupported encoding exception thrown");
        }
        return result;
    }

}
