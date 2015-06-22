package org.laborra.beantrace.renderers;

import org.laborra.beantrace.BeanTraceException;

import java.io.*;

/**
 * Basic class to create graph renderers. They can be used to draw different king
 * of bean traces.
 *
 * @see org.laborra.beantrace.BeanTraces#printBeanTrace(Object, org.laborra.beantrace.renderers.GraphRenderer)
 * @see org.laborra.beantrace.TraceConfiguration#setGraphRenderer(org.laborra.beantrace.renderers.GraphRenderer)
 */
public class GraphRenderers {

    /**
     * Creates a new {@link GraphvizDotRenderer} that writes the contents to the given
     * appendable.
     */
    public static GraphvizDotRenderer newGraphviz(Writer output) {
        return new GraphvizDotRenderer(output);
    }

    /**
     * Shortcut to create a file graphviz renderer.
     *
     * @see #newGraphviz(Writer)
     */
    public static GraphvizDotRenderer newGraphviz(File outputFile) {
        try {
            return newGraphviz(new BufferedWriter(new FileWriter(outputFile), 1));
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }

    /**
     * Creates a new {@link AsciiRenderer} that prints the graphs to the
     * standard output.
     */
    public static AsciiRenderer newAscii() {
        return newAscii(new PrintWriter(System.out));
    }

    /**
     * Creates a new {@link AsciiRenderer} that prints
     * the graph to the given appendable.
     *
     * @param output Where to write the graph
     * @return the create renderer
     */
    public static AsciiRenderer newAscii(Writer output) {
        final AsciiRenderer ret = new AsciiRenderer();
        ret.getConfig().setOutput(output);
        return ret;
    }

    /**
     * Shortcut to create an ascii renderer that writes to a file.
     *
     * @see #newAscii(Writer)
     */
    public static AsciiRenderer newAscii(File outputFile) {
        try {
            return newAscii(new BufferedWriter(new FileWriter(outputFile), 1));
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }
}
