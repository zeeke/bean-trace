package org.laborra.beantrace.renderers;

/**
 * Basic class to create graph renderers. They can be used to draw different king
 * of bean traces.
 *
 * @see org.laborra.beantrace.BeanTraces#printBeanTrace(Object, GraphRenderer)
 * @see org.laborra.beantrace.TraceConfiguration#setGraphRenderer(GraphRenderer)
 */
public class GraphRenderers {

    /**
     * Creates a new {@link org.laborra.beantrace.renderers.AsciiRenderer} that prints
     * the graph to the given appendable.
     *
     * @param appendable Where to write the graph
     * @return the create renderer
     */
    public static AsciiRenderer newAsciiRenderer(Appendable appendable) {
        final AsciiRenderer ret = new AsciiRenderer();
        ret.getConfig().setAppendable(appendable);
        return ret;
    }
}
