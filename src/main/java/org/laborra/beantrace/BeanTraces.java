package org.laborra.beantrace;

import org.laborra.beantrace.model.Vertex;
import org.laborra.beantrace.renderers.GraphRenderer;

/**
 * Main library entry point.
 */
public class BeanTraces {

    /**
     * Print an object trace with the default configuration.
     *
     * @see #newDefaultConfiguration()
     *
     * @param subject The object to print
     */
    public static void printBeanTrace(Object subject) {
        printBeanTrace(subject, newDefaultConfiguration());
    }

    /**
     * Prints an object trace and dumps it to the given renderer.
     *
     * @param subject The object to print
     * @param graphRenderer The renderer to use
     */
    public static void printBeanTrace(Object subject, GraphRenderer graphRenderer) {
        final TraceConfiguration traceConfiguration = newDefaultConfiguration();
        traceConfiguration.setGraphRenderer(graphRenderer);
        printBeanTrace(subject, traceConfiguration);
    }

    /**
     * Prints an object trace with the given configuration.
     *
     * @param subject The object to print
     * @param traceConfiguration The algorithm configuration
     */
    public static void printBeanTrace(Object subject, TraceConfiguration traceConfiguration) {
        final Vertex vertex = traceConfiguration.getVertexFactory().create(subject);
        traceConfiguration.getGraphRenderer().render(vertex);
    }

    /**
     * Create a new configuration object with default values.
     *
     * @return the configuration.
     */
    public static TraceConfiguration newDefaultConfiguration() {
        return TraceConfiguration.makeDefault();
    }

}
