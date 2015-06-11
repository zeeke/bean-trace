package org.laborra.beantrace.internal;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Utility class with reflection related utility methods.
 */
public class ReflectUtils {

    private static final Set<Class> PRIMITIVE_TYPES = Sets.newHashSet(
            (Class) Integer.class, Long.class, String.class, Boolean.class
    );

    public static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() || PRIMITIVE_TYPES.contains(type);
    }
}
