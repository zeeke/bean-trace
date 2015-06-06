package org.laborra.beantrace.internal;

import org.laborra.beantrace.model.Vertex;

import java.util.Collection;
import java.util.LinkedList;

public class CollectorVisitor implements VertexVisitor {

    private final Collection<Vertex> result = new LinkedList<>();

    @Override
    public void visit(Vertex vertex) {
        result.add(vertex);
    }

    public Collection<Vertex> getResult() {
        return result;
    }
}
