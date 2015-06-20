package org.laborra.beantrace.handlers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

/**
 * Collection of {@link org.laborra.beantrace.handlers.VertexHandler} related objects and utility
 * methods.
 */
public class StandardJavaHandlers {

    public static VertexHandler WRITER_HANDLER = new TypedStopVertexHandler<Writer>(Writer.class);
    public static VertexHandler READER_HANDLER = new TypedStopVertexHandler<Reader>(Reader.class);
    public static VertexHandler OUTPUT_STREAM_HANDLER = new TypedStopVertexHandler<OutputStream>(OutputStream.class);
    public static VertexHandler INPUT_STREAM_HANDLER = new TypedStopVertexHandler<InputStream>(InputStream.class);

    public static VertexHandler JAVA_IO_HANDLER = new VertexHandler.Composite(Arrays.asList(
            WRITER_HANDLER,
            READER_HANDLER,
            INPUT_STREAM_HANDLER,
            OUTPUT_STREAM_HANDLER
    ));

}
