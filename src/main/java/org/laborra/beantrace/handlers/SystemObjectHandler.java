package org.laborra.beantrace.handlers;

import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Vertex;

import java.util.HashMap;
import java.util.Map;

/**
 * This handler manage system object like System.out, in order to stop the scanning
 * and collect more useful information.
 */
public class SystemObjectHandler implements VertexHandler {

    private final Map<Integer, String> hashCodeToName;

    public SystemObjectHandler(Map<Integer, String> hashCodeToName) {
        this.hashCodeToName = hashCodeToName;
    }

    public static SystemObjectHandler makeDefault() {
        final Map<Integer, String> map = new HashMap<>();
        map.put(System.out.hashCode(), "System OUT");
        map.put(System.err.hashCode(), "System ERR");
        return new SystemObjectHandler(map);
    }

    @Override
    public boolean canHandle(Object subject) {
        return hashCodeToName.containsKey(subject.hashCode());
    }

    @Override
    public void handle(Vertex vertex, Object subject) {
        vertex.getAttributes().add(new Attribute<>(
                "System",
                hashCodeToName.get(subject.hashCode())
        ));
    }
}
