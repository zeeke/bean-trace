package org.laborra.beantrace.handlers

import org.junit.Test
import org.laborra.beantrace.internal.VertexAssert
import org.laborra.beantrace.model.Vertex

class SystemObjectHandlerTest {

    @Test
    void handle_specified_object() {
        def a = "just an object"
        def map = [:]
        map.put(a.hashCode(), "a string")
        def sut = new SystemObjectHandler(map)

        assert sut.canHandle(a)
        def vertex = new Vertex(String, "id")

        sut.handle(vertex, a)
        VertexAssert.assertThat(vertex)
            .hasAttribute("System", "a string")
            .hasNReferences(0)
            .hasNAttributes(1)
    }


}
