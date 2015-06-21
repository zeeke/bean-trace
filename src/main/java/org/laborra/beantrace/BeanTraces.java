package org.laborra.beantrace;

import org.laborra.beantrace.model.Vertex;
import org.laborra.beantrace.renderers.GraphRenderer;

/**
 * <p>Bean Trace enables printing  object relationships in a graphic clear way.
 * Following examples show the various usage of this library.</p>
 *
 * <h2>Basic printing</h2>
 * <pre class="code"><code class="java">
 * List&lt;String&gt; subject = new LinkedList&lt;&gt;();
 * subject.add("one");
 * subject.add("two");
 * BeanTraces.printBeanTrace(subject);
 * </code></pre>
 * Output:
 * <pre>
 * LinkedList
 *    |-- 1 : two
 *    `-- 0 : one
 * </pre>
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
     * @see org.laborra.beantrace.renderers.GraphRenderers
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
