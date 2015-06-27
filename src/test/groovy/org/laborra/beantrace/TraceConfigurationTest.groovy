package org.laborra.beantrace

import org.junit.Test

class TraceConfigurationTest {

    @Test
    void basic_builder() {
        def actual = TraceConfiguration.builder()
                .withMaxDepth(2)
                .withExclusion(Map.class)
                .build()

        assert actual.maxDepth == 2
        assert actual.customHandlers == []
        assert actual.excludedTypes == [ Map ]
    }
}
