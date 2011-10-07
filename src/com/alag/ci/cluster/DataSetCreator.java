package com.alag.ci.cluster;

import java.util.List;

public interface DataSetCreator {
    public List<TextDataItem> createLearningData() throws Exception ;
}
