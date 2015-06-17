package org.laborra.beantrace.handlers;

import org.laborra.beantrace.model.Attribute;
import org.laborra.beantrace.model.Vertex;

import java.net.URL;

class URLTypeHandler extends TypeBasedHandler<URL> {

    URLTypeHandler() {
        super(URL.class);
    }

    @Override
    protected void typedHandle(Vertex vertex, URL subject) {
        vertex.getAttributes().add(new Attribute<>("url", subject.toString()));
    }
}
