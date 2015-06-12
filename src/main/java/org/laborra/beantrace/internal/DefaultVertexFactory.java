package org.laborra.beantrace.internal;

import com.google.common.base.Optional;
import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.lang.reflect.Array;
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

            addField(ret, subject, field, value);
        }

        return ret;
    }

    private void addField(Vertex vertex, Object subject, Field field, Object value) {
        if (value == null) {
            return;
        }

        final Optional<Attribute> attributeOptional = AttributeFactory.DEFAULT_FACTORY.make(subject, field, value);

        if (attributeOptional.isPresent()) {
            vertex.getAttributes().add(attributeOptional.get());
            return;
        }

        vertex.getReferences().add(new Edge(
                field.getName(), create(value)
        ));
    }

    private void populateArray(Vertex vertex, Object subject) {

        int arrayLength = Array.getLength(subject);
        for (int i = 0; i < arrayLength; i++) {
            Object item = Array.get(subject, i);

            if (item == null) {
                continue;
            }

            final String fieldName = i + "";
            if (ReflectUtils.isPrimitive(item.getClass())) {
                vertex.getAttributes().add(new Attribute<>(fieldName, item));
                continue;
            }

            vertex.getReferences().add(new Edge(
                    fieldName, create(item)
            ));
        }
    }

    /**
     * Optionally creates a field. If the return value is not present, then the engine
     * will try to build an {@link org.laborra.beantrace.model.Edge}.
     */
    public interface AttributeFactory {

        Optional<Attribute> make(Object object, Field field, Object value);

        public static final AttributeFactory PRIMITIVE_TYPES_FACTORY = new AttributeFactory() {

            @Override
            public Optional<Attribute> make(Object object, Field field, Object value) {
                final Class<?> type = field.getType();
                if (!ReflectUtils.isPrimitive(type)) {
                    return Optional.absent();
                }
                @SuppressWarnings("unchecked")
                final Optional<Attribute> ret = Optional.of(new Attribute(field.getName(), value));

                return ret;
            }
        };

        public static final AttributeFactory DEFAULT_FACTORY = new Composite(Arrays.asList(
                PRIMITIVE_TYPES_FACTORY
        ));

        /**
         * This implementation delegates to multiple
         * {@link org.laborra.beantrace.internal.DefaultVertexFactory.AttributeFactory}. The first not absent
         * returned value will is the total return value.
         */
        public static class Composite implements AttributeFactory {

            private final List<AttributeFactory> delegates;

            public Composite(List<AttributeFactory> delegates) {
                this.delegates = delegates;
            }

            @Override
            public Optional<Attribute> make(Object object, Field field, Object value) {
                for (AttributeFactory delegate : delegates) {
                    final Optional<Attribute> make = delegate.make(object, field, value);
                    if (make.isPresent()) {
                        return make;
                    }
                }

                return Optional.absent();
            }
        }
    }
}
