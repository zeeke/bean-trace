package org.laborra.beantrace.internal;

import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;


/**
 * Populates the references and the attributes of the vertex based on the
 * passed field of the object.
 */
public class VertexFieldAdder {

    private final VertexFactory vertexFactory;

    public VertexFieldAdder(VertexFactory vertexFactory) {
        this.vertexFactory = vertexFactory;
    }

    public void addField(Vertex vertex, String fieldName, Object item) {
        if (item == null) {
            return;
        }

        if (ReflectUtils.isPrimitive(item.getClass())) {
            vertex.getAttributes().add(new Attribute<>(fieldName, item));
            return;
        }

        vertex.getReferences().add(new Edge(
                fieldName, vertexFactory.create(item)
        ));
    }
}
