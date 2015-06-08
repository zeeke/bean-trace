package org.laborra.beantrace.renderers

import org.junit.Before
import org.junit.Test
import org.laborra.beantrace.model.Attribute
import org.laborra.beantrace.model.Vertex

import static org.hamcrest.Matchers.containsString
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat

class JsonRendererTest {

    StringWriter writer
    GraphRenderer sut

    @Before
    void setupSUT() {
        writer = new StringWriter()
        sut = new JsonRenderer(writer)
    }

    @Test
    void single_node() {
        def vertex = new Vertex(Object, "single")
        vertex.attributes << new Attribute("prop1", "value1")

        sut.render(vertex)

        assertEquals(writer.toString(), '{nodes: [' +
                '{id: "single", type: "java.lang.Object", attributes: {prop1: "value1"}}' +
                '], links: []}')
    }

    @Test
    void multiple_nodes() {
        def vertex = RendererTestUtils.basicTree()

        sut.render(vertex)

        String result = writer.toString()
        assertThat(result, containsString('{id: "root", type: "java.lang.Object", attributes: {}}'))
        assertThat(result, containsString('{id: "leaf1", type: "java.lang.Object", attributes: {}}'))
        assertThat(result, containsString('{id: "leaf2", type: "java.lang.Object", attributes: {}}'))
        assertThat(result, containsString('[{source: 0, target: 1}, {source: 0, target: 2}]'))
    }
}
