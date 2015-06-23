package org.laborra.beantrace.renderers;

import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * Print the object graph to {@link java.lang.Appendable} specified by the {@link Config},
 * using ASCII characters.
 */
public class AsciiRenderer implements GraphRenderer {

    private Config config;

    private final Set<Vertex> visited = new HashSet<>();

    /**
     * Initialize the renderer using the default configuration.
     */
    public AsciiRenderer() {
        this(new Config());
    }

    public AsciiRenderer(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    @Override
    public void render(Vertex subject) {
        renderLinks(subject);
    }

    private void renderLinks(Vertex subject) {
        try {
            final Writer output = config.getOutput();
            renderNode(output, subject, 0, "");
            output.flush();
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }

    private void renderNode(Writer output, Vertex vertex, int depth, String prefix) throws IOException {

        indent(depth, output);

        output.append(prefix);
        output.append(vertex.getClazz().getSimpleName());

        if (config.isPrintId()) {
            output.append("@").append(vertex.getId());
        }

        if (visited.contains(vertex)) {
            return;
        }

        visited.add(vertex);

        final Set<Attribute> attributes = vertex.getAttributes();
        final Set<Edge> references = vertex.getReferences();

        int k = 1;
        for (Attribute attribute : attributes) {
            output.append("\n");
            indent(depth + 1, output);
            output.append(k == references.size() + attributes.size() ? "`-- " : "|-- ")
                    .append(attribute.getName())
                    .append(" : ")
                    .append(attribute.getValue().toString());

            k++;
        }

        for (Edge reference : references) {
            output.append("\n");
            renderNode(
                    config.getOutput(), reference.getTo(),
                    depth + 1,
                    (k == references.size() + attributes.size() ? "`-- " : "|-- ") + reference.getName() + " : "
            );
            k++;
        }

        output.flush();
    }

    private void indent(int depth, Appendable appendable) throws IOException {
        for (int i=0; i<depth; i++) {
            appendable.append("   ");
        }
    }

    /**
     * Appendable configuration
     */
    public static class Config {

        /**
         * Where the renderer should write the ASCII graph. Default to {@link System#out}.
         */
        private Writer writer = new PrintWriter(System.out);

        /**
         * Whether or not to print the object identifier.
         */
        private boolean printId = false;

        public boolean isPrintId() {
            return printId;
        }

        public void setPrintId(boolean printId) {
            this.printId = printId;
        }

        public Writer getOutput() {
            return writer;
        }

        public void setOutput(Writer writer) {
            this.writer = writer;
        }
    }
}
