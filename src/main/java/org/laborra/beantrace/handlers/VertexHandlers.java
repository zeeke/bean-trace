package org.laborra.beantrace.handlers;

import org.laborra.beantrace.FieldExclusionStrategy;
import org.laborra.beantrace.TraceConfiguration;
import org.laborra.beantrace.internal.FieldHandler;
import org.laborra.beantrace.internal.VertexFieldPopulator;

import java.util.Arrays;
import java.util.List;

public class VertexHandlers {

    /**
     * Builds the default {@link VertexHandler}
     *
     * @param config The configuration to use to build the handlers
     */
    public static List<VertexHandler> makeDefault(TraceConfiguration config) {
        final VertexFieldPopulator vertexFieldPopulator = config.getVertexFieldPopulator();
        final FieldExclusionStrategy fieldExclusionStrategy = config.getFieldExclusionStrategy();

        return Arrays.asList(
                new VertexHandler.ArrayHandler(vertexFieldPopulator),
                new VertexHandler.MapVertexHandler(vertexFieldPopulator),
                new VertexHandler.ClassTypeHandler(vertexFieldPopulator),
                new URLTypeHandler(),
                new FieldHandler(
                        fieldExclusionStrategy,
                        vertexFieldPopulator
                )
        );
    }
}
