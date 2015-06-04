package org.laborra.beantrace.internal;

import org.laborra.beantrace.model.Vertex;

public interface VertexVisitor<T> {

    void visit(Vertex vertex);

}
