package org.laborra.beantrace

import org.laborra.beantrace.renderers.GraphRenderers
import org.mockito.Mockito

class ExampleDocs {

    private File outputDirectory

    static void main(String ... args) {
        def docs = new ExampleDocs(outputDirectory: new File(args[0]))

        docs.mockitoAscii()
        docs.mockitoGraphviz()
        docs.beanTraceConfig();
    }

    private File createOutputFile(String fileName) {
        return new File(outputDirectory, fileName)
    }

    private mockitoAscii() {
        def subject = Mockito.mock(Runnable );
        File outputFile = createOutputFile('mockito_ascii.txt')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newAscii(outputFile));
    }

    private mockitoGraphviz() {
        def subject = Mockito.mock(Runnable);
        File outputFile = createOutputFile('mockito_graphviz.dot')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));
    }

    private beanTraceConfig() {
        def subject = TraceConfiguration.makeDefault()
        File outputFile = createOutputFile('bean_trace_graphviz.dot')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));

        outputFile = createOutputFile('bean_trace_ascii.txt')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newAscii(outputFile));
    }
}

