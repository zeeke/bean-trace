package org.laborra.beantrace.renderers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.internal.Graphs;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class JsonRenderer implements GraphRenderer {
    private final Writer writer;

    private static final Escaper ESCAPER = Escapers.builder()
                .addEscape('"', "\\\"")
                .build();

    public JsonRenderer(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void render(Vertex subject) {

        final Collection<Vertex> vertices = Graphs.collectAllVertices(subject);
        final Map<Vertex, Integer> vertexToIndexMap = Graphs.mapVerticesToIndex(vertices);

        final Iterable<String> verticesAsJson = Iterables.transform(vertices, new Function<Vertex, String>() {
            @Override
            public String apply(Vertex input) {
                return vertexAsJson(input);
            }
        });

        final Map<Edge, Vertex> edgeMap = Graphs.mapEdgeToStartingVertex(subject);

        final Iterable<String> edgesAsJson = Iterables.transform(edgeMap.keySet(), new Function<Edge, String>() {
            @Override
            public String apply(Edge input) {
                return edgeAsJson(edgeMap, vertexToIndexMap, input);
            }
        });

        try {
            writer.write(
                    curly(
                            jsonArrayField("nodes", verticesAsJson),
                            jsonArrayField("links", edgesAsJson)
                    )
            );
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }

    private static String vertexAsJson(Vertex subject) {
        final List<String> fields = new LinkedList<>();
        for (Attribute attribute : subject.getAttributes()) {
            fields.add(jsonField(attribute.getName(), Objects.toString(attribute.getValue())));
        }

        return curly(
                jsonField("id", subject.getId()),
                jsonField("type", subject.getClazz().getName()),
                jsonUnquotedField("attributes", curly(fields))
        );
    }

    private static String edgeAsJson(Map<Edge, Vertex> edgeMap, Map<Vertex, Integer> vertexToIndexMap, Edge edge) {
        Integer fromIndex = vertexToIndexMap.get(edgeMap.get(edge));
        Integer toIndex = vertexToIndexMap.get(edge.getTo());

        return curly(jsonUnquotedField("source", fromIndex) + ", " + jsonUnquotedField("target", toIndex));
    }

    private static String jsonField(String propertyName, String propertyValue) {
        return propertyName + ": \"" + ESCAPER.escape(propertyValue) + "\"";
    }

    private static String jsonArrayField(String propertyName, Iterable<String> values) {
        return propertyName + ": [" + Joiner.on(", ").join(values) + "]";
    }

    private static String curly(Iterable<String> sources) {
        ArrayList<String> strings = Lists.newArrayList(sources);
        return curly(strings.toArray(new String[strings.size()]));
    }

    private static String curly(String ... sources) {
        return "{" + Joiner.on(", ").join(sources) + "}";
    }

    private static String jsonUnquotedField(String propertyName, Object value) {
        return propertyName + ": " + value;
    }
}
