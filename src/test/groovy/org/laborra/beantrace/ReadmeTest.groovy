package org.laborra.beantrace

import org.junit.Before
import org.junit.Test
import org.laborra.beantrace.renderers.GraphRenderers

import static org.hamcrest.Matchers.containsString
import static org.junit.Assert.assertThat

class ReadmeTest {

    StringWriter stringWriter

    @Before
    void setupWriter() {
        stringWriter = new StringWriter()
    }

    @Test
    void "simple ascii rendered object trace"() {
        B b = new B("A string data")
        A a = new A(b);

        BeanTraces.printBeanTrace(
                a,
                GraphRenderers.newAsciiRenderer(stringWriter)
        )

        assert stringWriter.toString() == """\
            A
               `-- b : B""".stripIndent()
    }

    @Test
    void "three objects"() {
        def a1b = new B("Here")
        def a2b = new B("is an")
        def b = new B("example")
        def c = new C()
        c.a1 = new A(a1b)
        c.a2 = new A(a2b)
        c.b = b

        BeanTraces.printBeanTrace(
                c,
                GraphRenderers.newAsciiRenderer(stringWriter)
        )

        String result = stringWriter.toString()
        assertThat(result, containsString("C"))
        assertThat(result, containsString("-- a1 : A"))
        assertThat(result, containsString("`-- b : B"))
        assertThat(result, containsString("-- a2 : A"))
        assertThat(result, containsString("`-- b : B"))
    }

    public static class A {

        private B b;

        public A(B b) {
            this.b = b;
        }
    }

    public static class B {
        private String data;

        public B(String data) {
            this.data = data;
        }
    }

    public static class C {

        private A a1;
        private A a2;
        private B b;

    }
}
