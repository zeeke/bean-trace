package org.laborra.beantrace;

/**
 * Thrown when an exception in the Object Trace library is raised.
 *
 * @author Andrea Panattoni
 */
public class BeanTraceException extends RuntimeException {
    public BeanTraceException(Throwable cause) {
        super(cause);
    }
}
