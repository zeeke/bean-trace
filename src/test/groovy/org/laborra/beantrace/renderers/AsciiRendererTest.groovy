package org.laborra.beantrace.renderers

import org.junit.Test
import org.laborra.beantrace.model.Edge
import org.laborra.beantrace.model.Vertex

class AsciiRendererTest {

    StringWriter writer = new StringWriter()
    AsciiRenderer sut = new AsciiRenderer(writer)

    @Test
    void "single node"() {
        Vertex node = new Vertex(StringWriter, "42")

        sut.render(node)

        assert writer.toString() == "StringWriter@42"
    }

    @Test
    void "two node and link"() {
        Vertex child = new Vertex(StringWriter, "55")
        Vertex root = new Vertex(StringWriter, "42")
        root.references << new Edge("fieldName", child)

        sut.render(root)

        assert writer.toString() == """\
                StringWriter@42
                   `-- fieldName : StringWriter@55""".stripIndent()
    }

    @Test
    void "one root with two leaves"() {
        Vertex root = new Vertex(StringWriter, "root")
        root.references << new Edge("fieldName", new Vertex(StringWriter, "leaf1"))
        root.references << new Edge("fieldName", new Vertex(StringWriter, "leaf1"))

        when:
        sut.render(root)

        then:
        assert writer.toString() == """\
                StringWriter@root
                   |-- fieldName : StringWriter@leaf1
                   |-- fieldName : StringWriter@leaf1""".stripIndent()
    }
}
