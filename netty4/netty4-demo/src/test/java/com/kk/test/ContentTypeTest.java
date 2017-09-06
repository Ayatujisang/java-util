package com.kk.test;

import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

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
        File file = new File("~/test/s.py");
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        System.out.println(mimeTypesMap.getContentType(file.getPath())); // application/octet-stream
    }
}
