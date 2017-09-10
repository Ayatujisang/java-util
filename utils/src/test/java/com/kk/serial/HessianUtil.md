package com.kk.serial;

import java.io.*;

/**
 * 子类和父类同名字段会被覆盖
 *
 * @auther zhihui.kzh
 * @create 9/9/1716:21
 */
public class HessianUtil {


    public static byte[] serialize(Serializable obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static void serialize(Serializable obj, OutputStream bout) throws IOException {
        if (bout == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }

        Hessian2Output out = new Hessian2Output(bout);
        try {
            out.writeObject(obj);
            out.flush();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }


    public static Object deserialize(byte[] objectData) throws IOException {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }

        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        return deserialize(bais);
    }

    public static Object deserialize(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        Hessian2Input in = null;
        try {
            // stream closed in the finally
            in = new Hessian2Input(inputStream);
            return in.readObject();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}
