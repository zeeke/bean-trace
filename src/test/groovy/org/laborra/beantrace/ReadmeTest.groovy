package org.laborra.beantrace
import org.junit.Test
import org.laborra.beantrace.renderers.GraphRenderers

class ReadmeTest {

    @Test
    void "simple ascii rendered object trace"() {
        B b = new B("A string data")
        A a = new A(b);

        final StringWriter stringWriter = new StringWriter();
        BeanTraces.printBeanTrace(
                a,
                GraphRenderers.newAsciiRenderer(stringWriter)
        )

        assert stringWriter.toString() == """\
            A@${System.identityHashCode(a)}
               `-- b : B@${System.identityHashCode(b)}""".stripIndent()
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
}
