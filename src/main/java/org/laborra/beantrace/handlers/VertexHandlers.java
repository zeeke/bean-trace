package org.laborra.beantrace.handlers;

import org.laborra.beantrace.VertexFieldAdder;

import java.util.Arrays;
import java.util.List;

public class VertexHandlers {

    /**
     * Builds the default {@link VertexHandler}
     *
     * @param vertexFieldAdder The utility object to use when scanning objects
     */
    public static List<VertexHandler> makeDefault(VertexFieldAdder vertexFieldAdder) {
        return Arrays.asList(
                SystemObjectHandler.makeDefault(),
                new VertexHandler.ArrayHandler(vertexFieldAdder),
                new CollectionHandler(vertexFieldAdder),
                new VertexHandler.MapVertexHandler(vertexFieldAdder),
                new VertexHandler.ClassTypeHandler(vertexFieldAdder),
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
