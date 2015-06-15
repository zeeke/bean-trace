package org.laborra.beantrace.renderers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.internal.Graphs;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class GraphvizDotRenderer implements GraphRenderer {

    private final Appendable appendable;

    public GraphvizDotRenderer(Appendable appendable) {
        this.appendable = appendable;
    }

    @Override
    public void render(Vertex subject) {
        final Collection<Vertex> vertices = Graphs.collectAllVertices(subject);
        final Map<Edge, Vertex> edgeMap = Graphs.mapEdgeToStartingVertex(subject);

        try {
            appendable.append(getHeader())
                    .append(formatEdges(edgeMap))
                    .append(formatVertices(vertices))
                    .append(getFooter());
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }

    }

    private static String getHeader() {
        return "digraph bean_trace {\n";
    }

    private String formatEdges(final Map<Edge, Vertex> edgeMap) {
        final Iterable<String> chunks = Iterables.transform(edgeMap.entrySet(), new Function<Map.Entry<Edge, Vertex>, String>() {
            @Override
            public String apply(Map.Entry<Edge, Vertex> input) {
                return input.getValue().getId() + " -> " + input.getKey().getTo().getId() + " [label=\"" + input.getKey().getName() + "\"];";
            }
        });
        return Joiner.on("\n").join(chunks);
    }

    private String formatVertices(Collection<Vertex> vertices) {
        final Iterable<String> chunks = Iterables.transform(vertices, new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                final StringBuilder sb = new StringBuilder(input.getId())
                        .append(" [label=<strong>")
                        .append(input.getClazz())
                        .append("</strong><dl>");

                final Set<Attribute> attributes = input.getAttributes();
                for (Attribute attribute : attributes) {
                    sb
                            .append("<dt>")
                            .append(attribute.getName())
                            .append("</dt><dd>")
                            .append(attribute.getValue())
                            .append("</dd>");
                }

                sb.append("</dl>]");
                return sb.toString();
            }
        });

        return Joiner.on("\n").join(chunks);
    }

    public String getFooter() {
        return "\n}";
    }
}
