package org.laborra.beantrace;

import org.laborra.beantrace.model.Vertex;

public interface VertexFieldAdder {
    void addField(Vertex vertex, String fieldName, Object item);
}
