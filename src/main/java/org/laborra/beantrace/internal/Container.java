package org.laborra.beantrace.internal;

import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.TraceConfiguration;
import org.laborra.beantrace.handlers.VertexHandler;
import org.laborra.beantrace.handlers.VertexHandlers;
import org.laborra.beantrace.renderers.GraphRenderer;

import java.util.LinkedList;
import java.util.List;

public class Container {

    private GraphRenderer graphRenderer;
    private List<VertexHandler> vertexHandlers;
    private FieldExclusionStrategy fieldExclusionStrategy;
    private VertexFactory vertexFactory;
    private VertexFieldPopulator vertexFieldPopulator;

    public static Container make(TraceConfiguration configuration) {
        final Container ret = new Container();
        ret.graphRenderer = configuration.getGraphRenderer();
        ret.fieldExclusionStrategy  = FieldExclusionStrategy.DEFAULT_STRATEGY;
        ret.vertexHandlers = new LinkedList<>();

        ret.vertexFactory  = new MaxDepthVertexFactoryDecorator(
                configuration.getMaxDepth(),
                new DefaultVertexFactory(new VertexHandler.Composite(ret.vertexHandlers))
        );

        ret.vertexFieldPopulator = new VertexFieldPopulator(ret.vertexFactory);

        ret.vertexHandlers.addAll(configuration.getCustomHandlers());
        ret.vertexHandlers.addAll(VertexHandlers.makeDefault(ret.vertexFieldPopulator));
        ret.vertexHandlers.add(new FieldHandler(
                ret.fieldExclusionStrategy,
                ret.vertexFieldPopulator
        ));

        return ret;
    }

    public VertexFactory getVertexFactory() {
        return vertexFactory;
    }

    public GraphRenderer getGraphRenderer() {
        return graphRenderer;
    }
}
