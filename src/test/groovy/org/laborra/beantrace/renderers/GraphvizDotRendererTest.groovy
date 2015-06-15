package org.laborra.beantrace.renderers
import org.junit.Before
import org.junit.Test
import org.laborra.beantrace.model.Vertex

class GraphvizDotRendererTest {

    StringWriter writer
    GraphvizDotRenderer sut

    @Before
    void setupSUT() {
        writer = new StringWriter()
        sut = new GraphvizDotRenderer(writer)
    }

    @Test
    void "basic_tree"() {
        Vertex root = RendererTestUtils.basicTree()

        sut.render(root)

        def result = writer.toString()

        assert result.contains("root [")
        assert result.contains("leaf1 [")
        assert result.contains("leaf2 [")
        assert result.contains("root -> leaf1 [label=\"field1\"]")
        assert result.contains("root -> leaf2 [label=\"field2\"]")
    }

}
