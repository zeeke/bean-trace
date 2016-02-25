package org.laborra.beantrace.internal;

import org.laborra.beantrace.model.Vertex;

public interface VertexFactory {

    Vertex create(Object subject);

}
