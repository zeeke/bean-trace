package org.laborra.beantrace.internal
import org.junit.Test
import org.laborra.beantrace.FieldExclusionStrategy
import org.laborra.beantrace.model.Vertex
import org.mockito.Mockito

import java.lang.reflect.Field

import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

class FieldHandlerTest {

    @Test
    void inherited_fields() {
        def fieldExclusionStrategy = mock(FieldExclusionStrategy)
        def populator = mock(VertexFieldAdder)
        def sut = new FieldHandler(fieldExclusionStrategy, populator)

        sut.handle(new Vertex(B, "id"), new B())

        verify(populator, atLeast(2)).addField(Mockito.<Vertex>any(), any(), Mockito.<Field>any(), any())
    }


    static class A {
        int parentField
    }

    static class B extends A{
        int childField
    }

}


