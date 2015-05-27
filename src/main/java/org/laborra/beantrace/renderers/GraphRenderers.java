package org.laborra.beantrace.renderers;

public class GraphRenderers {
    public static AsciiRenderer newAsciiRenderer(Appendable appendable) {
        return new AsciiRenderer(appendable);
    }
}
