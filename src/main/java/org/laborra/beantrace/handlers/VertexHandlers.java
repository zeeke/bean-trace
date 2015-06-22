package org.laborra.beantrace.handlers;

import org.laborra.beantrace.internal.VertexFieldPopulator;

import java.util.Arrays;
import java.util.List;

public class VertexHandlers {

    /**
     * Builds the default {@link VertexHandler}
     *
     * @param vertexFieldPopulator The utility object to use when scanning objects
     */
    public static List<VertexHandler> makeDefault(VertexFieldPopulator vertexFieldPopulator) {
        return Arrays.asList(
                SystemObjectHandler.makeDefault(),
                new VertexHandler.ArrayHandler(vertexFieldPopulator),
                new CollectionHandler(vertexFieldPopulator),
                new VertexHandler.MapVertexHandler(vertexFieldPopulator),
                new VertexHandler.ClassTypeHandler(vertexFieldPopulator),
                new URLTypeHandler(),

                StandardJavaHandlers.JAVA_IO_HANDLER
        );
    }
}
