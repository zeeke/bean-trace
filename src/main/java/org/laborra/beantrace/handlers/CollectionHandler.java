package org.laborra.beantrace.handlers;

import org.laborra.beantrace.VertexFieldAdder;
import org.laborra.beantrace.model.Vertex;

import java.util.Collection;

class CollectionHandler extends TypeBasedHandler<Collection<Object>> {

    private final VertexFieldAdder vertexFieldAdder;

    public CollectionHandler(VertexFieldAdder vertexFieldAdder) {
        super(Collection.class);
        this.vertexFieldAdder = vertexFieldAdder;
    }

    @Override
    protected void typedHandle(Vertex vertex, Collection<Object> subject) {
        int i = 0;
        for (Object item : subject) {
            vertexFieldAdder.addField(vertex, i + "", item);
            i++;
        }
    }
}
