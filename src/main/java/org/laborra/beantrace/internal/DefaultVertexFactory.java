package org.laborra.beantrace.internal;

import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        final Field[] fields = subject.getClass().getDeclaredFields();
        for (Field field : fields) {
            final Class<?> type = field.getType();
            if (field.isSynthetic()) {
                continue;
            }

            if (fieldExclusionStrategy.mustExclude(subject, type)) {
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

            if (type.isPrimitive() || String.class.equals(type)) {
                attributes.add(new Attribute<>(
                        field.getName(),
                        value
                ));
                continue;
            }

            if (value != null) {
                references.add(new Edge(
                        field.getName(),
                        create(value)
                ));
            }
        }

        return ret;
    }
}
