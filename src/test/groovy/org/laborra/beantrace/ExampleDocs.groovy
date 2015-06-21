package org.laborra.beantrace

import org.laborra.beantrace.renderers.GraphRenderers
import org.mockito.Mockito

class ExampleDocs {

    private File outputDirectory

    static void main(String ... args) {
        def docs = new ExampleDocs(outputDirectory: new File(args[0]))

        docs.simpleList()
        docs.mockitoAscii()
        docs.mockitoGraphviz()
        docs.beanTraceConfig();
    }

    private File createOutputFile(String fileName) {
        return new File(outputDirectory, fileName)
    }

    void mockitoAscii() {
        def subject = Mockito.mock(Runnable );
        File outputFile = createOutputFile('mockito_ascii.txt')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newAscii(outputFile));
    }

    void mockitoGraphviz() {
        def subject = Mockito.mock(Runnable);
        File outputFile = createOutputFile('mockito_graphviz.dot')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));
    }

    void beanTraceConfig() {
        def subject = TraceConfiguration.makeDefault()
        File outputFile = createOutputFile('bean_trace_graphviz.dot')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));

        outputFile = createOutputFile('bean_trace_ascii.txt')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newAscii(outputFile));
    }

    void simpleList() {
        List<String> subject = new LinkedList<>();
        subject.add("one");
        subject.add("two");
        BeanTraces.printBeanTrace(subject);
    }
}

