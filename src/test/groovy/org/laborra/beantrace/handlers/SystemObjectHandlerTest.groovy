package org.laborra.beantrace.handlers

import org.junit.Test
import org.laborra.beantrace.model.Vertex
import org.laborra.beantrace.test.VertexAssert

class SystemObjectHandlerTest {

    @Test
    void handle_specified_object() {
        def a = "just an object"
        def map = [:]
        map.put(a.hashCode(), "a string")
        def sut = new SystemObjectHandler(map)

        assert sut.canHandle(a)
        def vertex = new Vertex(String, "id")

        sut.handle(vertex, a, null)
        VertexAssert.assertThat(vertex)
            .hasAttribute("System", "a string")
            .hasNReferences(0)
            .hasNAttributes(1)
    }


}
