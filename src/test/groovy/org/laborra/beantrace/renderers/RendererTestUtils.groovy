package org.laborra.beantrace.renderers

import org.laborra.beantrace.model.Edge
import org.laborra.beantrace.model.Vertex

class RendererTestUtils {

    static Vertex basicTree() {
        Vertex root = new Vertex(Object, "root")
        root.references << new Edge("field1", new Vertex(Object, "leaf1"))
        root.references << new Edge("field2", new Vertex(Object, "leaf2"))
        return root
    }
}
