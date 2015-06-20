package org.laborra.beantrace.handlers;

import org.laborra.beantrace.internal.VertexFieldPopulator;
import org.laborra.beantrace.model.Vertex;

import java.util.Collection;

class CollectionHandler extends TypeBasedHandler<Collection<Object>> {

    private final VertexFieldPopulator vertexFieldPopulator;

    public CollectionHandler(VertexFieldPopulator vertexFieldPopulator) {
        super(Collection.class);
        this.vertexFieldPopulator = vertexFieldPopulator;
    }

    @Override
    protected void typedHandle(Vertex vertex, Collection<Object> subject) {
        int i = 0;
        for (Object item : subject) {
            vertexFieldPopulator.addField(vertex, i + "", item);
            i++;
        }
    }
}
