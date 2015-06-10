package org.laborra.beantrace.internal;

import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.lang.reflect.Field;
import java.util.*;

public class DefaultVertexFactory implements VertexFactory {

    private final Map<Object, Vertex> visitedMap = new HashMap<>();
    private final FieldExclusionStrategy fieldExclusionStrategy;

    public DefaultVertexFactory(FieldExclusionStrategy fieldExclusionStrategy) {
        this.fieldExclusionStrategy = fieldExclusionStrategy;
    }

    public Vertex create(Object subject) {
        if (visitedMap.containsKey(subject)) {
            return visitedMap.get(subject);
        }

        final Set<Edge> references = new HashSet<>();
        final Set<Attribute> attributes = new HashSet<>();
        final Vertex ret = new Vertex(
                subject.getClass(),
                System.identityHashCode(subject) + "",
                references,
                attributes
        );

        visitedMap.put(subject, ret);

        if (subject.getClass().isArray()) {
            populateArray(ret, subject);
            return ret;
        }


        final Field[] fields = subject.getClass().getDeclaredFields();
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

            addField(ret, field.getName(), value);
        }

        return ret;
    }

    private void addField(Vertex vertex, String fieldName, Object value) {

        if (value == null) {
            return;
        }

        final Class<?> type = value.getClass();

        // Going to extract a strategy ...
        if (type.isPrimitive() || String.class.equals(type) || Integer.class.equals(type)) {
            vertex.getAttributes().add(new Attribute<>(
                    fieldName,
                    value
            ));
            return;
        }

        vertex.getReferences().add(new Edge(
                fieldName,
                create(value)
        ));
    }

    private void populateArray(Vertex vertex, Object subject) {

        Object[] arraySubject = (Object[]) subject;

        int i = 0;
        for (Object item : arraySubject) {
            addField(vertex, i + "", item);
            i++;
        }
    }
}
