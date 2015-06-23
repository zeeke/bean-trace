package org.laborra.beantrace.internal
import org.junit.Test
import org.laborra.beantrace.TraceConfiguration
import org.laborra.beantrace.model.Vertex

import static org.laborra.beantrace.internal.VertexAssert.assertThat

class MaxDepthVertexFactoryDecoratorTest {

    @Test
    void high_depth() {
        VertexFactory factory = makeSUT(10)
        SelfRef subject = makeLinkedGraph()

        def root = factory.create(subject)

        assertThat(root).hasNReferences(1)
        assertThat(root.references[0].to).hasNReferences(1)
        def last = root.references[0].to.references[0].to
        assertThat(last).hasAttribute("data", "something")
    }

    @Test
    void cut_on_max_depth_reached() {
        def factory = makeSUT(2)
        SelfRef subject = makeLinkedGraph()

        def root = factory.create(subject)


        assertThat(root).hasNReferences(1)
        assertThat(root.references[0].to)
                .hasNReferences(0)
                .hasNAttributes(1)
                .hasAttribute('...', '...')
    }

    @Test
    void cut_only_max_depth() {
        def factory = makeSUT(3)
        def subject = new ListRef(
                children:  [
                        makeLinkedGraph(),
                        new SelfRef(data: "something")
                ]
        )

        Vertex root = factory.create(subject)

//        GraphRenderers.newAscii().render(root)
        assertThat(root).hasNReferences(1)

        Vertex secondLevel = root.references[0].to
        assertThat(secondLevel)
            .hasNReferences(2)

        def truncatedBranch = secondLevel.references[0].to
        assertThat(truncatedBranch)
            .hasNReferences(0)
            .hasAttribute("...", "...")
    }

    private static SelfRef makeLinkedGraph() {
        def subject = new SelfRef(
                field: new SelfRef(
                        field: new SelfRef(
                                data: "something"
                        )
                )
        )
        subject
    }

    private static VertexFactory makeSUT(int maxDepth) {
        def configuration = new TraceConfiguration()
        configuration.setMaxDepth(maxDepth)
        def factory = Container.make(configuration).vertexFactory
        factory
    }

}

class SelfRef {
    SelfRef field
    String data
}

class ListRef extends SelfRef {
    List<SelfRef> children
}
