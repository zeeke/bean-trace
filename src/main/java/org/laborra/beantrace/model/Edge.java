package org.laborra.beantrace.model;

/**
 * A link from a vertex to another.
 *
 * @see org.laborra.beantrace.model.Vertex
 */
public class Edge {
    private String name;
    private Vertex to;

    public Edge(String name, Vertex to) {
        this.name = name;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public Vertex getTo() {
        return to;
    }
}
