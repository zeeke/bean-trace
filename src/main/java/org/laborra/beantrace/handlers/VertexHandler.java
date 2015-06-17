package org.laborra.beantrace.handlers;

import org.laborra.beantrace.internal.ReflectUtils;
import org.laborra.beantrace.internal.VertexFieldPopulator;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * Manages the setup of a vertex based on the passed subject.
 */
public interface VertexHandler {

    /**
     * Specifies if this handler can manage the subject.
     */
    boolean canHandle(Object subject);

    /**
     * Handle the passed vertex based on the subject.
     */
    void handle(Vertex vertex, Object subject);

    public static abstract class TypeBasedHandler<T> implements VertexHandler {

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

    public static class Composite implements VertexHandler {

        private final List<VertexHandler> delegates;

        public Composite(List<VertexHandler> delegates) {
            this.delegates = delegates;
        }

        @Override
        public boolean canHandle(Object subject) {
            for (VertexHandler vertexHandler : delegates) {
                if (vertexHandler.canHandle(subject)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void handle(Vertex vertex, Object subject) {
            for (VertexHandler vertexHandler : delegates) {
                if (vertexHandler.canHandle(subject)) {
                    vertexHandler.handle(vertex, subject);
                    return;
                }
            }
        }
    }

    public static class MapVertexHandler extends TypeBasedHandler<Map<Object, Object>> {

        private final VertexFieldPopulator vertexFieldPopulator;

        public MapVertexHandler(VertexFieldPopulator vertexFieldPopulator) {
            super(Map.class);
            this.vertexFieldPopulator = vertexFieldPopulator;
        }

        @Override
        public void typedHandle(Vertex vertex, Map<Object, Object> subject) {
            int i = -1;
            for (Map.Entry<Object, Object> entry : subject.entrySet()) {
                i++;
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                final boolean primitiveKey = ReflectUtils.isPrimitive(key.getClass());

                if (primitiveKey) {
                    vertexFieldPopulator.addField(vertex, key.toString(), value);
                }

                if (!primitiveKey) {
                    final Vertex entryVertex = new Vertex(Map.Entry.class, System.identityHashCode(entry) + "");
                    vertexFieldPopulator.addField(entryVertex, "key", key);
                    vertexFieldPopulator.addField(entryVertex, "value", value);
                    vertex.getReferences().add(new Edge("entry_" + i, entryVertex));
                }
            }
        }
    }

    public static class ArrayHandler implements VertexHandler {

        private final VertexFieldPopulator vertexFieldPopulator;

        public ArrayHandler(VertexFieldPopulator vertexFieldPopulator) {
            this.vertexFieldPopulator = vertexFieldPopulator;
        }

        public boolean canHandle(Object subject) {
            return subject.getClass().isArray();
        }

        public void handle(Vertex vertex, Object subject) {
            int arrayLength = Array.getLength(subject);
            for (int i = 0; i < arrayLength; i++) {
                Object item = Array.get(subject, i);
                vertexFieldPopulator.addField(vertex, i + "", item);
            }
        }
    }

    public static class ClassTypeHandler extends TypeBasedHandler<Class> {

        private final VertexFieldPopulator vertexFieldPopulator;

        public ClassTypeHandler(VertexFieldPopulator vertexFieldPopulator) {
            super(Class.class);
            this.vertexFieldPopulator = vertexFieldPopulator;
        }

        @Override
        protected void typedHandle(Vertex vertex, Class subject) {
            vertexFieldPopulator.addField(vertex, "name", subject.getName());
        }
    }

}
