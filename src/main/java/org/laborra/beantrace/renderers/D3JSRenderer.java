package org.laborra.beantrace.renderers;

import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.model.Vertex;

import java.io.*;

public class D3JSRenderer implements GraphRenderer {

    private final Writer destinationWrite;
    private final Reader templateReader;

    public D3JSRenderer(Writer destinationWrite, Reader templateReader) {
        this.destinationWrite = destinationWrite;
        this.templateReader = templateReader;
    }

    @Override
    public void render(Vertex subject) {
        final StringWriter stringWriter = new StringWriter();
        new JsonRenderer(stringWriter).render(subject);

        try (
                final BufferedReader bufferedReader = new BufferedReader(templateReader);
                final PrintWriter printWriter = new PrintWriter(destinationWrite)
                ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String toWriteLine = line.replace("###JSON_DATA_TOKEN###", stringWriter.toString());
                printWriter.println(toWriteLine);
            }
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }


    }

}
