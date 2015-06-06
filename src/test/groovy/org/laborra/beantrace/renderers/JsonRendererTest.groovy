package org.laborra.beantrace.renderers

import org.junit.Before
import org.junit.Test
import org.laborra.beantrace.model.Attribute
import org.laborra.beantrace.model.Vertex

import static org.junit.Assert.assertEquals


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
                '{id: "single", type: "java.lang.Object", attributes: [prop1: "value1"]}' +
                '], links: []}')
    }

    @Test
    void multiple_nodes() {
        def vertex = RendererTestUtils.basicTree()

        sut.render(vertex)

        assertEquals(writer.toString(), '{nodes: [' +
                '{id: "root", type: "java.lang.Object", attributes: []}, ' +
                '{id: "leaf1", type: "java.lang.Object", attributes: []}, ' +
                '{id: "leaf2", type: "java.lang.Object", attributes: []}' +
                '], links: [{source: 0, target: 1}, {source: 0, target: 2}]}')
    }

//    static Vertex makeVertex(String id, Map<String, String> attributes = [:], Map<String, String>) {
//
//        return new Vertex(
//                Object.class,
//                id,
//                attributes.collect { k, v -> new Attribute<Object>(k, v) }
//        )
//    }
}
