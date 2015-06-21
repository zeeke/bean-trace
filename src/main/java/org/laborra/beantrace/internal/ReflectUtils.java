package org.laborra.beantrace.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Utility class with reflection related utility methods.
 */
public class ReflectUtils {

    private static final Set<Class> PRIMITIVE_TYPES = Sets.newHashSet(
            (Class) Integer.class, Long.class, String.class, Boolean.class, Byte.class,
            Character.class, Float.class, Double.class, Short.class
    );

    /**
     * Returns true if the given type is intended to be "primitive" by the library
     *
     * @param type The class to check
     * @return True if the give type is primitive, false otherwise
     */
    public static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() || PRIMITIVE_TYPES.contains(type);
    }

    public static List<Field> getFieldFromClassHierarchy(Class<?> clazz) {
        if (Object.class.equals(clazz)) {
            return Collections.emptyList();
        }
        final ArrayList<Field> fields = Lists.newArrayList(clazz.getDeclaredFields());
        fields.addAll(getFieldFromClassHierarchy(clazz.getSuperclass()));
        return fields;
    }
}
