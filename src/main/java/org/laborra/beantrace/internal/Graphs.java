package org.laborra.beantrace.internal;

import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

        visitor.visit(vertex);

        final Set<Edge> references = vertex.getReferences();
        for (Edge reference : references) {
            traverse(reference.getTo(), visitor);
        }
    }
}
