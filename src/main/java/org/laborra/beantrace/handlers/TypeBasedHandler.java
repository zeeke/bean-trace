package org.laborra.beantrace.handlers;

import org.laborra.beantrace.model.Vertex;

abstract class TypeBasedHandler<T> implements VertexHandler {

    private final Class<?> handledType;

    protected TypeBasedHandler(Class<?> handledType) {
        this.handledType = handledType;
    }

    @Override
    public boolean canHandle(Object subject) {
        return handledType.isAssignableFrom(subject.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Vertex vertex, Object subject) {
        typedHandle(vertex, (T) subject);
    }

    protected abstract void typedHandle(Vertex vertex, T subject);
}
