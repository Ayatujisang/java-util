package com.kk.mybatis.gen;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * freemarker 模板工具
 *
 * @author Ying-er
 * @version 1.0
 * @time 2010-2-6下午04:07:27
 */
public class FreeMarkerUtil {

    private static final String templateEncoding = "utf-8";

    /**
     * 返回模板内容
     *
     * @param templateName 模板文件名称
     * @param root         数据模型根对象 如果模板中没有值在map中，则抛出异常
     */
    public static String writeWithTemplate(String templateName, Map<?, ?> root) {
        try {
            /**
             * 创建Configuration对象
             */
            Configuration config = new Configuration();
            /**
             * 指定模板路径  !!!!!!!
             */
            File file = new File("/Users/kongzhihui/workspace/idea/mybatisgen/tpl");
            /**
             * 设置要解析的模板所在的目录，并加载模板文件
             */
            config.setDirectoryForTemplateLoading(file);
            /**
             * 设置包装器，并将对象包装为数据模型
             */
            config.setObjectWrapper(new DefaultObjectWrapper());

            /**
             * 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
             */
            Template template = config.getTemplate(templateName,
                    templateEncoding);
            /**
             * 合并数据模型与模板
             */
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Writer out = new OutputStreamWriter(bout);
            template.process(root, out);
            out.flush();
            out.close();

            return bout.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
