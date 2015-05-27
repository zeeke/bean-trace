package org.laborra.beantrace;

public interface FieldExclusionStrategy {

    boolean mustExclude(Object subject, Class<?> type);

    public static class DefaultExclusion implements FieldExclusionStrategy {

        @Override
        public boolean mustExclude(Object subject, Class<?> type) {
            if (type.getName().startsWith("groovy.lang")) {
                return true;
            }

            if (type.getName().startsWith("org.codehaus.groovy")) {
                return true;
            }

            return false;
        }
    }

}
