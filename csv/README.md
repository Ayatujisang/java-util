#### java读取csv可以直接使用普通io操作，csv文件中间使用逗号隔开即可，编码使用gbk


### 使用 javacsv.jar


```
依赖：  
 <dependency>
    <groupId>net.sourceforge.javacsv</groupId>
    <artifactId>javacsv</artifactId>
    <version>2.0</version>
</dependency>
```

### DEMO
```
package com.kk.csv;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @auther zhihui.kzh
 */
public class CsvTest {

    public static void main(String[] args) throws Exception {
//        test_read();

        test_write();
    }

    private static void test_write() throws Exception {
        CsvWriter wr = new CsvWriter("/data/info.csv", ',', Charset.forName("GBK"));
        {
            String[] contents = {"名称", "Id", "性别"};
            wr.writeRecord(contents);
        }
        {
            String[] contents = {"Lily", "90", "女生"};
            wr.writeRecord(contents);
        }
        wr.close();
    }

    private static void test_read() throws IOException {
        //生成CsvReader对象，以，为分隔符，GBK编码方式
        CsvReader r = new CsvReader("/data/dat.csv", ',', Charset.forName("GBK"));

        //读取表头
        r.readHeaders();
        System.out.println(Arrays.toString(r.getHeaders()));

        //逐条读取记录，直至读完
        while (r.readRecord()) {
            //读取一条记录
            System.out.println(r.getRawRecord()); // 每行原始输入
            //按列名读取这条记录的值
            System.out.print(r.get("ID") + ",");
            System.out.println(r.get("属性"));
        }
        r.close();
    }
}

```
