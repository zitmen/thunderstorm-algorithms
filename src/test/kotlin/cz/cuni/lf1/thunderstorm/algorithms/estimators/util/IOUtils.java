package cz.cuni.lf1.thunderstorm.algorithms.estimators.util;

import java.io.*;
import java.nio.charset.Charset;

public class IOUtils {

    public static void closeQuietly(Reader input) {
        closeQuietly((Closeable)input);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
        return lineIterator(input, Charsets.toCharset(encoding));
    }

    public static LineIterator lineIterator(InputStream input, Charset encoding) throws IOException {
        return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
    }
}
