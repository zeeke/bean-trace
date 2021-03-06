package org.laborra.beantrace;

import org.laborra.beantrace.internal.Container;
import org.laborra.beantrace.model.Vertex;
import org.laborra.beantrace.renderers.GraphRenderer;

/**
 * <p>Bean Trace enables printing  object relationships in a graphic clear way.
 * Following examples show the various usage of this library.</p>
 *
 * <h2>Basic printing</h2>
 * <pre>
 *
 * List&lt;String&gt; subject = new LinkedList&lt;&gt;();
 * subject.add("one");
 * subject.add("two");
 * BeanTraces.printBeanTrace(subject);
 *
 * </pre>
 * Output:
 * <pre>
 * LinkedList
 *    |-- 1 : two
 *    `-- 0 : one
 * </pre>
 *
 * <h2>Limiting max depth</h2>
 * <pre>
 *
 * List&lt;Object&gt; subject = Arrays.asList(Arrays.asList(Arrays.asList()));
 * TraceConfiguration config = BeanTraces.newDefaultConfiguration();
 * config.setMaxDepth(2);
 * BeanTraces.printBeanTrace(subject, config);
 *
 * </pre>
 * Output:
 * <pre>
 * ArrayList
 *   `-- 0 : ArrayList
 *      `-- ... : ...
 * </pre>
 *
 * <h2>Type based exclusion</h2>
 * <p>You can avoid to scan some objects based on their types</p>
 *
 * <pre>
 * List&lt;Object&gt; subject = Arrays.asList(
 *      ImmutableMap.of("some key", Collections.emptyList()),
 *      Arrays.asList("element")
 * );
 * TraceConfiguration config = BeanTraces.newDefaultConfiguration();
 * config.setExcludedTypes(Arrays.asList(Map.class));
 * BeanTraces.printBeanTrace(subject, config);
 *
 * </pre>
 * Output:
 * <pre>
 * ArrayList
 *    |-- 0 : SingletonImmutableBiMap
 *    `-- 1 : ArrayList
 *       `-- 0 : element
 * </pre>
 *
 * <h2>Use builder for configuring traces</h2>
 * <pre>
 *
 * List&lt;Object&gt; subject = Arrays.asList(Arrays.asList(Arrays.asList()));
 * TraceConfiguration config = BeanTraces.builder()
 *      .withMaxDepth(2)
 *      .withExclusion(Map.class)
 *      .build();
 * BeanTraces.printBeanTrace(subject, config);
 *
 * </pre>
 *
 *
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
        final Container container = Container.make(traceConfiguration);
        final Vertex vertex = container.getVertexFactory().create(subject);
        container.getGraphRenderer().render(vertex);
    }

    /**
     * Create a new configuration object with default values.
     *
     * @return the configuration.
     */
    public static TraceConfiguration newDefaultConfiguration() {
        return new TraceConfiguration();
    }

}
