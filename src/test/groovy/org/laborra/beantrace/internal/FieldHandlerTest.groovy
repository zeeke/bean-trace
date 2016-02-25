package org.laborra.beantrace.internal

import org.junit.Test
import org.laborra.beantrace.FieldExclusionStrategy
import org.laborra.beantrace.model.Vertex
import org.mockito.Mockito

import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

class FieldHandlerTest {

    @Test
    void inherited_fields() {
        def fieldExclusionStrategy = mock(FieldExclusionStrategy)
        def vertexFieldAdder = mock(DefaultVertexFieldAdder)
        def sut = new FieldHandler(fieldExclusionStrategy)

        sut.handle(new Vertex(B, "id"), new B(), vertexFieldAdder)

        verify(vertexFieldAdder, atLeast(2)).addField(Mockito.<Vertex>any(), anyString(), anyString())
    }


    static class A {
        int parentField
    }

    static class B extends A{
        int childField
    }

}


