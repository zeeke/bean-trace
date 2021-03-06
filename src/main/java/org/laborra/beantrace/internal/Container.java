package org.laborra.beantrace.internal;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.TraceConfiguration;
import org.laborra.beantrace.VertexFieldAdder;
import org.laborra.beantrace.handlers.VertexHandler;
import org.laborra.beantrace.handlers.VertexHandlers;
import org.laborra.beantrace.model.Vertex;
import org.laborra.beantrace.renderers.GraphRenderer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Container {

    private GraphRenderer graphRenderer;
    private List<VertexHandler> vertexHandlers;
    private FieldExclusionStrategy fieldExclusionStrategy;
    private VertexFactory vertexFactory;
    private SettableVertexFieldAdder vertexFieldAdder;

    public static Container make(TraceConfiguration configuration) {
        final Container ret = new Container();
        ret.graphRenderer = configuration.getGraphRenderer();
        ret.fieldExclusionStrategy  = FieldExclusionStrategy.DEFAULT_STRATEGY;
        ret.vertexHandlers = new LinkedList<>();

        ret.vertexFieldAdder = new SettableVertexFieldAdder();
        ret.vertexFactory  = new MaxDepthVertexFactoryDecorator(
                configuration.getMaxDepth(),
                new DefaultVertexFactory(
                        new VertexHandler.Composite(ret.vertexHandlers),
                        ret.vertexFieldAdder
                )
        );

        ret.vertexFieldAdder.setDelegate(new DefaultVertexFieldAdder(ret.vertexFactory));

        ret.vertexHandlers.addAll(configuration.getCustomHandlers());
        ret.vertexHandlers.add(makeTypeExclusion(configuration.getExcludedTypes()));
        ret.vertexHandlers.addAll(VertexHandlers.makeDefault());
        ret.vertexHandlers.add(new FieldHandler(ret.fieldExclusionStrategy));

        return ret;
    }

    private static VertexHandler makeTypeExclusion(Collection<Class> excludedTypes) {
        final List<VertexHandler> delegates = Lists.newArrayList(Iterables.transform(excludedTypes, new Function<Class, VertexHandler>() {
            @Override
            public VertexHandler apply(Class input) {
                return VertexHandlers.makeExclusionFor(input);
            }
        }));

        return new VertexHandler.Composite(delegates);
    }

    public VertexFactory getVertexFactory() {
        return vertexFactory;
    }

    public GraphRenderer getGraphRenderer() {
        return graphRenderer;
    }

    /**
     * Tricky implementation to make circular dependency injection
     */
    static class SettableVertexFieldAdder implements VertexFieldAdder {

        private VertexFieldAdder delegate;

        public void setDelegate(VertexFieldAdder delegate) {
            this.delegate = delegate;
        }

        @Override
        public void addField(Vertex vertex, String fieldName, Object item) {
            delegate.addField(vertex, fieldName, item);
        }
    }
}
