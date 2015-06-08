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

    static Vertex tree(int nLevel, int nChild) {
        Vertex root = new Vertex(Object, "root")
        populate(root, nLevel, nChild)
        return root
    }

    private static void populate(Vertex vertex, int nLevel, int nChild) {
        if (nLevel == 0) {
            return
        }

        (1..nChild).each { i ->
            def child = new Vertex(Object, "node_$nLevel:$i")
            populate(child, nLevel - 1, nChild)
            vertex.references << new Edge("field_$i", child)
        }
    }
}
