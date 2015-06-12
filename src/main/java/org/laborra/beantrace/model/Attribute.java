package org.laborra.beantrace.model;

/**
 * Represents an attribute of an object.
 */
public class Attribute<T> {
    private String name;
    private T value;

    public Attribute(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
