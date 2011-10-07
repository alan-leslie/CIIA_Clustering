package com.alag.ci.blog.search;

@SuppressWarnings("serial")
public class BlogSearcherException extends Exception {
    public BlogSearcherException(String message, Throwable cause) {
        super(message, cause);
    }
    public BlogSearcherException(String message) {
        super(message);
    }
}
