package org.laborra.beantrace.internal
import org.assertj.core.api.AbstractAssert
import org.junit.Before
import org.junit.Test
import org.laborra.beantrace.TraceConfiguration
import org.laborra.beantrace.model.Vertex

class DefaultVertexFactoryTest {

    VertexFactory sut;

    @Before
    void setupSUT() {
        sut = Container.make(new TraceConfiguration()).getVertexFactory()
    }

    @Test
    void primitive_data_array() {
        def subject = new ObjectArray()
        subject.data = [ 10, 20 ] as Object[];

        def vertex = sut.create(subject).references.first().to;

        vertex.attributes.with {
            assert it.find { it.name == "0" }.value == 10
            assert it.find { it.name == "1" }.value == 20
        }
    }

    @Test
    void reference_array() {
        def subject = new ObjectArray()
        subject.data = [
                new StringPropBean(data: 'v1'),
                new StringPropBean(data: 'v2'),
        ]

        def vertex = sut.create(subject);

        def arrayVertex = vertex.references.first().to
        assert arrayVertex.references
        arrayVertex.references.with {
            assert size() == 2
            assert it.find { it.name == "0" }.to.attributes.first().value == 'v1'
            assert it.find { it.name == "1" }.to.attributes.first().value == 'v2'
        }
    }

    @Test
    void array_with_null_values() {
        def vertex = sut.create([0, null, 2] as Integer[])

        vertex.attributes.with {
            assert it.size() == 2
            assert it.find { it.name == "0" }.value == 0
            assert it.find { it.name == "2" }.value == 2
        }
    }

    @Test
    void strings_field_must_become_an_attribute() {
        def vertex = sut.create(new StringPropBean(data: 'sample'))

        assert vertex.references.size() == 0
        assert vertex.attributes.last().value == 'sample'
    }

    @Test
    void hash_maps_primitive_to_primitive() {
        def subject = new HashMap<String, String>()
        subject.put('K1', 'V1')
        subject.put('K2', 'V2')
        def vertex = sut.create(subject)

        VertexAssert.assertThat(vertex)
                .hasNAttributes(2)
                .hasAttribute('K1', 'V1')
                .hasAttribute('K2', 'V2')
                .hasNReferences(0)
    }

    @Test
    void hash_map_object_to_object() {
        def subject = new LinkedHashMap<Object, Object>()
        def k1 = new Object()
        def k2 = new Object()
        def v1 = new Object()
        def v2 = new Object()

        subject.put(k1, v1)
        subject.put(k2, v2)
        def vertex = sut.create(subject)

        VertexAssert.assertThat(vertex)
                .hasNAttributes(0)
                .hasNReferences(2)

        def entry_1 = vertex.references.find { it.name == 'entry_0' }.to
        VertexAssert.assertThat(entry_1)
            .hasReference("key", k1)
            .hasReference("value", v1)

        def entry_2 = vertex.references.find { it.name == 'entry_1' }.to
        VertexAssert.assertThat(entry_2)
                .hasReference("key", k2)
                .hasReference("value", v2)
    }

    @Test
    void hash_map_primitive_to_object() {

        def subject = new HashMap<String, Object>()
        def v1 = new Object()
        def v2 = new Object()
        subject.put('K1', v1)
        subject.put('K2', v2)
        def vertex = sut.create(subject)

        VertexAssert.assertThat(vertex)
                .hasNAttributes(0)
                .hasReference('K1', v1)
                .hasReference('K2', v2)
                .hasNReferences(2)
    }

    @Test
    void hash_map_object_to_primitive() {
        def subject = new LinkedHashMap<Object, String>()
        def k1 = new Object()
        def k2 = new Object()

        subject.put(k1, "v1")
        subject.put(k2, "v2")
        def vertex = sut.create(subject)

        VertexAssert.assertThat(vertex)
                .hasNAttributes(0)
                .hasNReferences(2)

        def entry_1 = vertex.references.find { it.name == 'entry_0' }.to
        VertexAssert.assertThat(entry_1)
                .hasReference("key", k1)
                .hasAttribute("value", "v1")

        def entry_2 = vertex.references.find { it.name == 'entry_1' }.to
        VertexAssert.assertThat(entry_2)
                .hasReference("key", k2)
                .hasAttribute("value", "v2")
    }
}

class ObjectArray {
    Object[] data
}

class StringPropBean {
    String data
}

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
}
