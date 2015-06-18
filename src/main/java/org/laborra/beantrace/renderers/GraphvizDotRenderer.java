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
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class GraphvizDotRenderer implements GraphRenderer {

    private final Writer writer;

    public GraphvizDotRenderer(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void render(Vertex subject) {
        final Collection<Vertex> vertices = Graphs.collectAllVertices(subject);
        final Map<Edge, Vertex> edgeMap = Graphs.mapEdgeToStartingVertex(subject);

        try (Writer output = writer) {
            output.append(getHeader())
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
                        .append(" [shape=none margin=0 ")
                        .append("label=<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">")
                        .append("<TR><TD BGCOLOR=\"lightgrey\" COLSPAN=\"2\">")
                        .append(input.getClazz().getSimpleName())
                        .append("</TD></TR>");

                final Set<Attribute> attributes = input.getAttributes();
                for (Attribute attribute : attributes) {
                    sb
                            .append("<TR><TD>")
                            .append(attribute.getName())
                            .append("</TD><TD>")
                            .append(attribute.getValue())
                            .append("</TD></TR>");
                }

                sb.append("</TABLE>>];");
                return sb.toString();
            }
        });

        return Joiner.on("\n").join(chunks);
    }

    public String getFooter() {
        return "\n}\n";
    }
}
