package org.laborra.beantrace.internal;

import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.VertexFieldAdder;
import org.laborra.beantrace.handlers.VertexHandler;
import org.laborra.beantrace.model.Vertex;

import java.lang.reflect.Field;
import java.util.List;

public class FieldHandler implements VertexHandler {

    private final FieldExclusionStrategy fieldExclusionStrategy;

    public FieldHandler(FieldExclusionStrategy fieldExclusionStrategy) {
        this.fieldExclusionStrategy = fieldExclusionStrategy;
    }

    public boolean canHandle(Object subject) {
        return true;
    }

    public void handle(Vertex vertex, Object subject, VertexFieldAdder vertexFieldAdder) {
        final List<Field> fields = ReflectUtils.getFieldFromClassHierarchy(subject.getClass());
        for (Field field : fields) {
            if (field.isSynthetic()) {
                continue;
            }

            if (fieldExclusionStrategy.mustExclude(subject, field)) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            final Object value;
            try {
                value = field.get(subject);
            } catch (IllegalAccessException e) {
                throw new BeanTraceException(e);
            }

            vertexFieldAdder.addField(vertex, field.getName(), value);
        }
    }
}
