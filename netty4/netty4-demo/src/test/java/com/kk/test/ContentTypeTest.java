package com.kk.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;

/**
 * 根据文件名称，获取在http中显示的 contentType信息， 如:text/html等。
 *
 * 需要将 mime.types 放到 resources/META-INF/mime.types位置。
 *
 * @auther zhihui.kzh
 * @create 6/9/1710:18
 */
public class ContentTypeTest {

    @Test
    public void test() {
        File file = new File("~/test/s.html");
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        System.out.println(mimeTypesMap.getContentType(file.getPath())); // application/octet-stream
    }

    @Test
    public void testContentLength() throws IOException {
        File file = new File("/data/test/pom.xml");

        byte[] bytes = FileUtils.readFileToByteArray(file);
        System.out.println(bytes.length);

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long fileLength = raf.length();
        System.out.println(fileLength); //  等同于 bytes.length
    }
}
