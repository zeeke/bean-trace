package org.laborra.beantrace

import org.laborra.beantrace.renderers.GraphRenderers
import org.mockito.Mockito

class ExampleDocs {

    private File outputDirectory

    static void main(String ... args) {
        def documentation = new ExampleDocs(outputDirectory: new File(args[0]))

        documentation.mockitoAscii()
        documentation.mockitoGraphviz()
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
}

