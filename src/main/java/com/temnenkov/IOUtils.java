package com.temnenkov;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class IOUtils {
    private IOUtils() {
    }

    public static String readFullyAsString(InputStream inputStream, String encoding)
            throws IOException {

        if (inputStream == null){
            return null;
        }

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toString(encoding);
        }
    }
}
