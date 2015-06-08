package org.laborra.beantrace.renderers;

/**
 * Basic class to create graph renderers. They can be used to draw different king
 * of bean traces.
 *
 * @see org.laborra.beantrace.BeanTraces#printBeanTrace(Object, GraphRenderer)
 * @see org.laborra.beantrace.TraceConfiguration#setGraphRenderer(GraphRenderer)
 */
public class GraphRenderers {
    public static AsciiRenderer newAsciiRenderer(Appendable appendable) {
        return new AsciiRenderer(appendable);
    }
}
