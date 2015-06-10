package org.laborra.beantrace;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Exclusion logic to be used while scanning the object.
 */
public interface FieldExclusionStrategy {

    boolean mustExclude(Object subject, Field field);

    public static final FieldExclusionStrategy STATIC_FIELDS = new FieldExclusionStrategy() {
        @Override
        public boolean mustExclude(Object subject, Field field) {
            return java.lang.reflect.Modifier.isStatic(field.getModifiers());
        }
    };

    public static final FieldExclusionStrategy SYSTEM_PACKAGES = new PackageExclusion(Arrays.asList(
            "groovy.lang",
            "org.codehaus.groovy"
    ));

    /**
     * Default exclusion strategy used by {@link org.laborra.beantrace.BeanTraces}
     */
    public static final FieldExclusionStrategy DEFAULT_STRATEGY = new Composite(
            STATIC_FIELDS, SYSTEM_PACKAGES
    );

    /**
     * With this class it is possible to mix multiple strategy in an AND fashion.
     */
    public static class Composite implements FieldExclusionStrategy {

        private final List<FieldExclusionStrategy> delegates;

        public Composite(FieldExclusionStrategy ... delegates) {
            this.delegates = Lists.newArrayList(delegates);
        }

        @Override
        public boolean mustExclude(Object subject, Field field) {
            for (FieldExclusionStrategy delegate : delegates) {
                if (delegate.mustExclude(subject, field)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Exclude a field based on its package. If the given field type package starts with
     * one of the specified strings, then the field is excluded.
     */
    public static class PackageExclusion implements FieldExclusionStrategy {

        private final List<String> excludedPackages;

        public PackageExclusion(List<String> excludedPackages) {
            this.excludedPackages = excludedPackages;
        }

        @Override
        public boolean mustExclude(Object subject, Field field) {
            final String typeName = field.getType().getName();

            for (String excludedPackage : excludedPackages) {
                if (typeName.startsWith(excludedPackage)) {
                    return true;
                }
            }

            return false;
        }
    }



}
