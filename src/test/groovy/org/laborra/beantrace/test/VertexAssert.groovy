package org.laborra.beantrace.test

import org.assertj.core.api.AbstractAssert
import org.laborra.beantrace.model.Vertex

class VertexAssert extends AbstractAssert<VertexAssert, Vertex> {

    protected VertexAssert(Vertex actual, Class<?> selfType) {
        super(actual, selfType)
    }

    public static VertexAssert assertThat(Vertex vertex) {
        return new VertexAssert(vertex, VertexAssert.class);
    }

    public VertexAssert hasAttribute(String name, Object value) {
        def attribute = actual.attributes.find { it.name == name }

        if (attribute == null) {
            failWithMessage("Vertex does not have attribute $name")
        }

        assert attribute.value == value
        return this;
    }

    public VertexAssert hasReference(String name, Object value) {
        def reference = actual.references.find { it.name == name }

        if (reference == null) {
            failWithMessage("Vertex does not have attribute $name")
        }

        assert reference.to.id == System.identityHashCode(value) + ""
        return this;
    }

    public VertexAssert hasNAttributes(int n) {
        assert actual.attributes.size() == n
        return this
    }

    public VertexAssert hasNReferences(int n) {
        assert actual.references.size() == n
        return this
    }

    public VertexAssert assertReferenceWithName(String name) {
        return assertThat(actual.references.find { it.name == name }.to)
    }
}
