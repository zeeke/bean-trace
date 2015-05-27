package org.laborra.beantrace;

import org.laborra.beantrace.renderers.GraphRenderer;

public class TraceConfiguration {

    private GraphRenderer graphRenderer;
    private FieldExclusionStrategy fieldExclusionStrategy;

    public GraphRenderer getGraphRenderer() {
        return graphRenderer;
    }

    public void setGraphRenderer(GraphRenderer graphRenderer) {
        this.graphRenderer = graphRenderer;
    }

    public FieldExclusionStrategy getFieldExclusionStrategy() {
        return fieldExclusionStrategy;
    }

    public void setFieldExclusionStrategy(FieldExclusionStrategy fieldExclusionStrategy) {
        this.fieldExclusionStrategy = fieldExclusionStrategy;
    }
}
