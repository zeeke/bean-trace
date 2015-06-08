package org.laborra.beantrace.renderers

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.laborra.beantrace.BeanTraces

class D3JSRendererTest {

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder()

    private D3JSRenderer sut

    @Before
    void setupSUT() {
        sut = new D3JSRenderer(
                File.createTempFile("test", ".html").newPrintWriter(),
                new InputStreamReader(D3JSRenderer.getResourceAsStream('/org/laborra/beantrace/d3/index.html'))
        )
    }

    @Test
    void basic_tree() {
        sut.render(RendererTestUtils.basicTree())
    }

    @Test
    void complex_tree() {
        sut.render(RendererTestUtils.tree(4, 3))
    }

    @Test
    void class_loader() {
        BeanTraces.printBeanTrace(D3JSRendererTest.classLoader, sut)
    }
}
