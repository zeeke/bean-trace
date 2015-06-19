package org.laborra.beantrace.handlers;

import org.laborra.beantrace.model.Vertex;

/**
 * This class stops the propagation of the scanning  on the given object type.
 */
class TypedStopVertexHandler<T> extends TypeBasedHandler<T> {

    TypedStopVertexHandler(Class<?> handledType) {
        super(handledType);
    }

    @Override
    protected void typedHandle(Vertex vertex, T subject) {
        // Intentionally left blank
    }
}
