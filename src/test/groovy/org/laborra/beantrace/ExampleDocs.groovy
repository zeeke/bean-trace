package org.laborra.beantrace

import org.laborra.beantrace.internal.VertexHandler
import org.laborra.beantrace.model.Attribute
import org.laborra.beantrace.model.Vertex
import org.laborra.beantrace.renderers.AsciiRenderer
import org.laborra.beantrace.renderers.GraphRenderers
import org.mockito.Mockito

import java.util.concurrent.Callable

class ExampleDocs {

    private File outputDirectory

    static void main(String ... args) {
        def documentation = new ExampleDocs(outputDirectory: new File(args[0]))

        documentation.mockitoAscii()
        documentation.mockitoGraphviz()
    }

    private static final List<TraceConfiguration> makeConfigurations(File outputDir) {
        TraceConfiguration conf1 = makeBasicConfiguration()
        conf1.setGraphRenderer(new AsciiRenderer(outputDir.createNewFile()))
        return [
                conf1,

        ]
    }

    private static TraceConfiguration makeBasicConfiguration() {
        def conf1 = TraceConfiguration.makeDefault()
        conf1.vertexHandlers.add(0, new URLTypeHandler())
        conf1
    }

    private File createOutputFile(String fileName) {
        return new File(outputDirectory, fileName)
    }

    private static final List<Object> makeSubjects() {
        return [
                BeanTraces.class.getClassLoader(),
                Mockito.mock(Callable),
        ]
    }

    private mockitoAscii() {
        def subject = Mockito.mock(Runnable);
        File outputFile = createOutputFile('mockito_ascii.txt')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newAscii(outputFile));
    }

    private mockitoGraphviz() {
        def subject = Mockito.mock(Runnable);
        File outputFile = createOutputFile('mockito_graphviz.dot')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));
    }
}

class URLTypeHandler extends VertexHandler.TypeBasedHandler<URL> {

    protected URLTypeHandler() {
        super(URL)
    }

    @Override
    protected void typedHandle(Vertex vertex, URL subject) {
        vertex.attributes << new Attribute("url", subject.toString())
    }
}
