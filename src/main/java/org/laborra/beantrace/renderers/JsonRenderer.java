package org.laborra.beantrace.renderers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.internal.Graphs;
import org.laborra.beantrace.internal.VertexVisitor;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class JsonRenderer implements GraphRenderer {
    private final Writer writer;

    public JsonRenderer(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void render(Vertex subject) {

        final Collection<Vertex> vertices = Graphs.collectAllVertices(subject);
        final HashMap<Vertex, Integer> vertexToIndexMap = new HashMap<>();
        final HashMap<Edge, Vertex> edgeMap = new HashMap<>();

        int i=0;
        for (Vertex vertex : vertices) {
            vertexToIndexMap.put(vertex, i);
            i++;
        }

        Graphs.traverse(subject, new VertexVisitor() {
            @Override
            public void visit(Vertex vertex) {
                for (Edge edge : vertex.getReferences()) {
                    edgeMap.put(edge, vertex);
                }
            }
        });

        final Iterable<String> verticesAsJson = Iterables.transform(vertices, new Function<Vertex, String>() {
            @Nullable
            @Override
            public String apply(Vertex input) {
                return vertexAsJson(input);
            }
        });

        final Iterable<String> edgesAsJson = Iterables.transform(edgeMap.keySet(), new Function<Edge, String>() {
            @Nullable
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
                jsonArrayField("attributes", fields)
        );
    }

    private static String edgeAsJson(HashMap<Edge, Vertex> edgeMap, HashMap<Vertex, Integer> vertexToIndexMap, Edge edge) {
        Integer fromIndex = vertexToIndexMap.get(edgeMap.get(edge));
        Integer toIndex = vertexToIndexMap.get(edge.getTo());

        return curly(jsonIntField("source", fromIndex) + ", " + jsonIntField("target", toIndex));
    }

    private static String jsonField(String propertyName, String propertyValue) {
        return propertyName + ": \"" + propertyValue + "\"";
    }

    private static String jsonArrayField(String propertyName, Iterable<String> values) {
        return propertyName + ": [" + Joiner.on(", ").join(values) + "]";
    }

    private static String curly(String ... sources) {
        return "{" + Joiner.on(", ").join(sources) + "}";
    }

    private static String jsonIntField(String propertyName, Integer value) {
        return propertyName + ": " + value;
    }
}
