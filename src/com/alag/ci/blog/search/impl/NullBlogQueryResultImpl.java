package com.alag.ci.blog.search.impl;

import java.util.Collections;

public class NullBlogQueryResultImpl extends BlogQueryResultImpl {
    @SuppressWarnings("unchecked")
    public NullBlogQueryResultImpl() {
        super();
        this.setResults(Collections.EMPTY_LIST);
    }
}
