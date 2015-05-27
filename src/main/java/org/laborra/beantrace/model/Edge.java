package org.laborra.beantrace.model;

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
