package org.laborra.beantrace.internal;

import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.util.*;

/**
 * Utility class to interact with vertex graphs.
 *
 */
public class Graphs {

    /**
     * Collect all vertices in the given graph.
     *
     * @param root A vertex contained in the graph
     * @return A collection with all vertices
     */
    public static Collection<Vertex> collectAllVertices(Vertex root) {
        final CollectorVisitor collectorVisitor = new CollectorVisitor();
        traverse(root, collectorVisitor);
        return collectorVisitor.getResult();
    }

    /**
     * Traverse the graph where the root is contained with the given visitor.
     *
     * @param root The starting vertex
     * @param visitor The visitor to apply to all vertices
     */
    public static void traverse(Vertex root, VertexVisitor visitor) {
        traverse(root, visitor, new HashSet<Vertex>());
    }

    private static void traverse(Vertex vertex, VertexVisitor visitor, Set<Vertex> visited) {
        if (visited.contains(vertex)) {
            return;
        }
        visited.add(vertex);

        visitor.visit(vertex);

        final Set<Edge> references = vertex.getReferences();
        for (Edge reference : references) {
            traverse(reference.getTo(), visitor, visited);
        }
    }

    public static Map<Edge, Vertex> mapEdgeToStartingVertex(Vertex subject) {
        final Map<Edge, Vertex> edgeMap = new HashMap<>();
        traverse(subject, new VertexVisitor() {
            @Override
            public void visit(Vertex vertex) {
                for (Edge edge : vertex.getReferences()) {
                    edgeMap.put(edge, vertex);
                }
            }
        });
        return edgeMap;
    }

    public static Map<Vertex, Integer> mapVerticesToIndex(Collection<Vertex> vertices) {
        final Map<Vertex, Integer> vertexToIndexMap = new HashMap<>();
        int i=0;
        for (Vertex vertex : vertices) {
            vertexToIndexMap.put(vertex, i);
            i++;
        }
        return vertexToIndexMap;
    }
}
