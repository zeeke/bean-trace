package org.laborra.beantrace.handlers;

import java.util.Arrays;
import java.util.List;

public class VertexHandlers {

    /**
     * Builds the default {@link VertexHandler}
     */
    public static List<VertexHandler> makeDefault() {
        return Arrays.asList(
                SystemObjectHandler.makeDefault(),
                new VertexHandler.ArrayHandler(),
                new CollectionHandler(),
                new VertexHandler.MapVertexHandler(),
                new VertexHandler.ClassTypeHandler(),
                new URLTypeHandler(),

                StandardJavaHandlers.JAVA_IO_HANDLER
        );
    }

    /**
     * Creates an exclusion vertex handler based on the object type.
     *
     * @param clazz The type to exclude
     */
    public static VertexHandler makeExclusionFor(Class<?> clazz) {
        return new TypedStopVertexHandler<>(clazz);
    }
}
