package org.laborra.beantrace.internal;

import org.laborra.beantrace.handlers.VertexHandler;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.util.*;

public class DefaultVertexFactory implements VertexFactory {

    private final Map<Object, Vertex> visitedMap = new IdentityHashMap<>();
    private final VertexHandler vertexHandler;

    public DefaultVertexFactory(VertexHandler vertexHandler) {
        this.vertexHandler = vertexHandler;
    }

    public Vertex create(Object subject) {
        if (visitedMap.containsKey(subject)) {
            return visitedMap.get(subject);
        }

        final Set<Edge> references = new HashSet<>();
        final Set<Attribute> attributes = new HashSet<>();
        final Vertex ret = new Vertex(
                subject.getClass(),
                System.identityHashCode(subject) + "",
                references,
                attributes
        );

        visitedMap.put(subject, ret);

        vertexHandler.handle(ret, subject);

        return ret;
    }

}
