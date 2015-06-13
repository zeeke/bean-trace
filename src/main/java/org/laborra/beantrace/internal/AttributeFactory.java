package org.laborra.beantrace.internal;

import com.google.common.base.Optional;
import org.laborra.beantrace.model.Attribute;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
     * {@link AttributeFactory}. The first not absent
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
