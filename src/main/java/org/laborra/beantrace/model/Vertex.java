package org.laborra.beantrace.model;

import java.util.HashSet;
import java.util.Set;

public class Vertex {
    private Class<?> clazz;
    private String id;
    private Set<Edge> references = new HashSet<>();
    private Set<Attribute> attributes = new HashSet<>();

    public Vertex(Class<?> clazz, String id) {
        this(clazz, id, new HashSet<Edge>(), new HashSet<Attribute>());
    }

    public Vertex(Class<?> clazz, String id, Set<Edge> references, Set<Attribute> attributes) {
        this.id = id;
        this.clazz = clazz;
        this.references = references;
        this.attributes = attributes;
    }

    public Set<Edge> getReferences() {
        return references;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getId() {
        return id;
    }
}
