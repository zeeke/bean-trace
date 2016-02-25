package org.laborra.beantrace.handlers;

import org.laborra.beantrace.VertexFieldAdder;
import org.laborra.beantrace.internal.ReflectUtils;
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
    void handle(Vertex vertex, Object subject, VertexFieldAdder vertexFieldAdder);

    class Composite implements VertexHandler {

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
        public void handle(Vertex vertex, Object subject, VertexFieldAdder vertexFieldAdder) {
            for (VertexHandler vertexHandler : delegates) {
                if (vertexHandler.canHandle(subject)) {
                    vertexHandler.handle(vertex, subject, vertexFieldAdder);
                    return;
                }
            }
        }
    }

    class MapVertexHandler extends TypeBasedHandler<Map<Object, Object>> {

        public MapVertexHandler() {
            super(Map.class);
        }

        @Override
        public void typedHandle(Vertex vertex, Map<Object, Object> subject, VertexFieldAdder vertexFieldAdder) {
            int i = -1;
            for (Map.Entry<Object, Object> entry : subject.entrySet()) {
                i++;
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                final boolean primitiveKey = ReflectUtils.isPrimitive(key.getClass());

                if (primitiveKey) {
                    vertexFieldAdder.addField(vertex, key.toString(), value);
                }

                if (!primitiveKey) {
                    final Vertex entryVertex = new Vertex(Map.Entry.class, System.identityHashCode(entry) + "");
                    vertexFieldAdder.addField(entryVertex, "key", key);
                    vertexFieldAdder.addField(entryVertex, "value", value);
                    vertex.getReferences().add(new Edge("entry_" + i, entryVertex));
                }
            }
        }
    }

    class ArrayHandler implements VertexHandler {

        public boolean canHandle(Object subject) {
            return subject.getClass().isArray();
        }

        public void handle(Vertex vertex, Object subject, VertexFieldAdder vertexFieldAdder) {
            int arrayLength = Array.getLength(subject);
            for (int i = 0; i < arrayLength; i++) {
                Object item = Array.get(subject, i);
                vertexFieldAdder.addField(vertex, i + "", item);
            }
        }
    }

    class ClassTypeHandler extends TypeBasedHandler<Class> {

        public ClassTypeHandler() {
            super(Class.class);
        }

        @Override
        protected void typedHandle(Vertex vertex, Class subject, VertexFieldAdder vertexFieldAdder) {
            vertexFieldAdder.addField(vertex, "name", subject.getName());
        }
    }

}
