package org.laborra.beantrace

import com.google.common.collect.ImmutableMap
import org.laborra.beantrace.internal.Container
import org.laborra.beantrace.renderers.GraphRenderers
import org.mockito.Mockito

class ExampleDocs {

    private File outputDirectory

    static void main(String ... args) {
        def docs = new ExampleDocs(outputDirectory: new File(args[0]))

        docs.mockitoAscii()
        docs.mockitoGraphviz()
        docs.beanTraceConfig()
        docs.simpleList()
        docs.maxDepth()
        docs.typeBasedExclusion()
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
        def subject = Container.make(BeanTraces.newDefaultConfiguration())
        File outputFile = createOutputFile('bean_trace_graphviz.dot')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));

        outputFile = createOutputFile('bean_trace_ascii.txt')
        BeanTraces.printBeanTrace(subject, GraphRenderers.newAscii(outputFile));
    }

    void simpleList() {
        println()
        List<String> subject = new LinkedList<>();
        subject.add("one");
        subject.add("two");
        BeanTraces.printBeanTrace(subject);
    }

    void maxDepth() {
        println()
        List<Object> subject = Arrays.asList(Arrays.asList(Arrays.asList()));
        TraceConfiguration config = BeanTraces.newDefaultConfiguration();
        config.setMaxDepth(2);
        BeanTraces.printBeanTrace(subject, config);
    }

    void typeBasedExclusion() {
        println()

        List<Object> subject = Arrays.asList(
                ImmutableMap.of("some key", Collections.emptyList()),
                Arrays.asList("element")
        )
        TraceConfiguration config = BeanTraces.newDefaultConfiguration();
        config.setExcludedTypes(Arrays.asList(Map.class));
        BeanTraces.printBeanTrace(subject, config);

    }
}

