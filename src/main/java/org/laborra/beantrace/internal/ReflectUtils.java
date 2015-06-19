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
            (Class) Integer.class, Long.class, String.class, Boolean.class
    );

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