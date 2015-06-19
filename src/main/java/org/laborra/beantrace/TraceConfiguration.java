package org.laborra.beantrace;

import org.laborra.beantrace.handlers.VertexHandler;
import org.laborra.beantrace.handlers.VertexHandlers;
import org.laborra.beantrace.internal.*;
import org.laborra.beantrace.renderers.GraphRenderer;
import org.laborra.beantrace.renderers.GraphRenderers;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class TraceConfiguration {

    private GraphRenderer graphRenderer;
    private List<VertexHandler> vertexHandlers;
    private FieldExclusionStrategy fieldExclusionStrategy;
    private VertexFactory vertexFactory;
    private VertexFieldPopulator vertexFieldPopulator;

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

    public List<VertexHandler> getVertexHandlers() {
        return vertexHandlers;
    }

    public void setVertexHandlers(List<VertexHandler> vertexHandlers) {
        this.vertexHandlers = vertexHandlers;
    }

    public VertexFactory getVertexFactory() {
        return vertexFactory;
    }

    public void setVertexFactory(VertexFactory vertexFactory) {
        this.vertexFactory = vertexFactory;
    }

    public void setVertexFieldPopulator(VertexFieldPopulator vertexFieldPopulator) {
        this.vertexFieldPopulator = vertexFieldPopulator;
    }

    public VertexFieldPopulator getVertexFieldPopulator() {
        return vertexFieldPopulator;
    }

    public static TraceConfiguration makeDefault() {
        final TraceConfiguration ret = new TraceConfiguration();
        ret.graphRenderer = GraphRenderers.newAscii(new PrintWriter(System.out));

        ret.fieldExclusionStrategy  = FieldExclusionStrategy.DEFAULT_STRATEGY;
        ret.vertexHandlers = new LinkedList<>();

        ret.vertexFactory  = new DefaultVertexFactory(new VertexHandler.Composite(ret.vertexHandlers));
        ret.vertexFieldPopulator = new VertexFieldPopulator(ret.vertexFactory);

        ret.vertexHandlers.addAll(VertexHandlers.makeDefault(ret));

        return ret;
    }
}