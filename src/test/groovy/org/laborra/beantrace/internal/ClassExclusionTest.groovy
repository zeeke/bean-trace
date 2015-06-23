package org.laborra.beantrace.internal
import com.google.common.collect.ImmutableMap
import org.junit.Test
import org.laborra.beantrace.TraceConfiguration

import static org.laborra.beantrace.test.VertexAssert.assertThat

class ClassExclusionTest {

    private static VertexFactory makeSUT(Class ... types) {
        def configuration = new TraceConfiguration()
        types.each { configuration.setExcludedTypes(Arrays.asList(types)) }
        def factory = Container.make(configuration).vertexFactory
        return factory
    }

    @Test
    void basic_exclusion() {
        def subject = Arrays.asList(
                ImmutableMap.of(
                        "some key",
                        Collections.emptyList()
                ),
                Arrays.asList("element")
        )
        def sut = makeSUT(Map)

        def root = sut.create(subject)

        assertThat(root)
            .hasNReferences(2)
            .assertReferenceWithName("0")
            .hasNAttributes(0)
            .hasNReferences(0)

        assertThat(root)
            .assertReferenceWithName("1")
            .hasAttribute("0", "element")
    }

}
