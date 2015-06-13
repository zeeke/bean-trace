package org.laborra.beantrace.internal;

import com.google.common.base.Optional;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.lang.reflect.Field;

/**
 * Populated the references and the attributes of the vertex based on the
 * passed field of the object.
 */
public class VertexFieldPopulator {

    private final VertexFactory vertexFactory;

    public VertexFieldPopulator(VertexFactory vertexFactory) {
        this.vertexFactory = vertexFactory;
    }

    public void addField(Vertex vertex, Object subject, Field field, Object value) {
        if (value == null) {
            return;
        }

        final Optional<Attribute> attributeOptional = AttributeFactory.DEFAULT_FACTORY.make(subject, field, value);

        if (attributeOptional.isPresent()) {
            vertex.getAttributes().add(attributeOptional.get());
            return;
        }

        vertex.getReferences().add(new Edge(
                field.getName(), vertexFactory.create(value)
        ));
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
