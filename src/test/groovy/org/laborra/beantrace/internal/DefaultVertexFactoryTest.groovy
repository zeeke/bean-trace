package org.laborra.beantrace.internal
import org.junit.Before
import org.junit.Test
import org.laborra.beantrace.FieldExclusionStrategy

class DefaultVertexFactoryTest {

    DefaultVertexFactory sut;

    @Before
    void setupSUT() {
        sut = new DefaultVertexFactory(new FieldExclusionStrategy.DefaultExclusion())
    }

    @Test
    void primitive_data_array() {
        def subject = new ObjectArray()
        subject.data = [ 10, 20 ] as Object[];

        def vertex = sut.create(subject);

        vertex.attributes.with {
            assert find { it.name == "0" }.value == 10
            assert find { it.name == "1" }.value == 10
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
            assert find { it.name == "0" }.to.attributes.first().value == 'v1'
            assert find { it.name == "0" }.to.attributes.first().value == 'v2'
        }
    }

}

class ObjectArray {
    Object[] data
}

class StringPropBean {
    String data
}
