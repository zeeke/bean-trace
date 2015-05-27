package org.laborra.beantrace.renderers;

import org.laborra.beantrace.BeanTraceException;
import org.laborra.beantrace.model.Edge;
import org.laborra.beantrace.model.Vertex;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AsciiRenderer implements GraphRenderer {
    private final Appendable appendable;

    private final Set<Vertex> visited = new HashSet<>();

    public AsciiRenderer(Appendable appendable) {
        this.appendable = appendable;
    }

    @Override
    public void render(Vertex subject) {
        renderLinks(subject);
    }

    private void renderLinks(Vertex subject) {
        try {
            renderNode(subject, 0, "");
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }

    private void renderNode(Vertex vertex, int depth, String prefix) throws IOException {
        for (int i=0; i<depth; i++) {
            appendable.append("   ");
        }

        appendable.append(prefix);
        appendable.append(vertex.getClazz().getSimpleName()).append("@").append(vertex.getId());

        if (!visited.contains(vertex)) {
            visited.add(vertex);
            final Set<Edge> references = vertex.getReferences();
            int k = 1;
            for (Edge reference : references) {
                appendable.append("\n");
                renderNode(
                        reference.getTo(),
                        depth + 1,
                        (k == references.size() ? "`-- " : "|-- ") + reference.getName() + " : "
                );
            }
        }
    }
}
