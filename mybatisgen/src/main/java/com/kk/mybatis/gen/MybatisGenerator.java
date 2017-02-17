package com.kk.mybatis.gen;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * mapper中 sql.id 需要去掉第一个逗号
 */
public class MybatisGenerator {
    public static final String SPTJ = "#";

    public static String basePath = "/Users/kongzhihui/workspace/idea/mybatisgen/";

    public static final String packageName = "com.kk.dao";
    public static final String packageModelName = "com.kk.model";

    public static void main(String[] args) throws IOException {
        File sqlPath = new File(basePath + "tpl/mybatis.sql");
        String sql = FileUtils.readFileToString(sqlPath);

        String tableName = parseTableName(sql); // table_name
        String modelName = field_solve(tableName); // tableName

        List<String> fieldsName = parseFieldsName(sql);
        System.out.println("fieldName=" + fieldsName);

        List<String> fieldsType = parseFieldsType(sql);
        System.out.println("fieldsType=" + fieldsType);

        if (fieldsName.size() != fieldsType.size()) {
            throw new RuntimeException("fieldName.length != fieldType.length:" + fieldsName.size() + "," + fieldsType.size());
        }


        String ModelName = Character.toUpperCase(modelName.charAt(0))
                + modelName.substring(1);// 首字母大写   TableName
        List<Bean> fieds = new ArrayList<Bean>();
        for (int i = 0; i < fieldsName.size(); i++) {
            Bean bean = new Bean(fieldsName.get(i), field_solve(fieldsName.get(i)), fieldsType.get(i));
            fieds.add(bean);
            System.out.println(bean);
        }
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("packageName", packageName);
        root.put("packageModelName", packageModelName);
        root.put("ModelName", ModelName);
        root.put("modelName", modelName);
        root.put("fieds", fieds);
        root.put("tableName", tableName);
        root.put("SPTJ", SPTJ);

        String res = FreeMarkerUtil.writeWithTemplate("template.ftl", root);
//        String res = FreeMarkerUtil.writeWithTemplate("template_map.ftl", root);
        System.out.println(res);
        FileUtils.write(new File(basePath + "data/freemark/result/mapping.xml"), res);

        res = FreeMarkerUtil.writeWithTemplate("dao.ftl", root);
        System.out.println(res);
        FileUtils.write(new File(basePath + "data/freemark/result/dao.java"), res);

        res = FreeMarkerUtil.writeWithTemplate("model.ftl", root);
        System.out.println(res);
        FileUtils.write(new File(basePath + "data/freemark/result/model.java"), res);
    }


    static Map<String, String> fieldTypeMap = new HashMap<String, String>();

    static {
        fieldTypeMap.put("bigint", "Long");
        fieldTypeMap.put("int", "Integer");
        fieldTypeMap.put("smallint", "Integer");
        fieldTypeMap.put("varchar", "String");
        fieldTypeMap.put("char", "String");
        fieldTypeMap.put("date", "Date");
        fieldTypeMap.put("datetime", "Date");
        fieldTypeMap.put("timestamp", "Date");
        fieldTypeMap.put("double", "Double");
        fieldTypeMap.put("float", "Double");
        fieldTypeMap.put("tinyint", "Boolean");
        fieldTypeMap.put("decimal", "BigDecimal");
        fieldTypeMap.put("text", "String");
        fieldTypeMap.put("mediumtext", "String");
    }

    private static List<String> parseFieldsName(String sql) {
        String[] sqls = StringUtils.split(sql, "\n");
        List<String> result = new ArrayList<String>();

        for (String cont : sqls) {
            if (containVar(cont)) {
                Pattern pattern = Pattern.compile("`([a-zA-Z_0-9]+)`");
                Matcher matcher = pattern.matcher(cont);
                if (matcher.find()) {
                    result.add(matcher.group(1));
                }
            }
        }
        return result;
    }

    private static List<String> parseFieldsType(String sql) {
        String[] sqls = StringUtils.split(sql, "\n");
        List<String> result = new ArrayList<String>();

        for (String cont : sqls) {
            if (containVar(cont)) {
                if (cont.contains(" date ") || cont.contains(" datetime ")
                        || cont.contains(" timestamp ")) {
                    result.add("Date");
                    continue;
                }
                if (cont.contains(" mediumtext ") || cont.contains(" text ")) {
                    result.add("String");
                    continue;
                }
                Pattern pattern = Pattern.compile("([a-z]+)\\(");
                Matcher matcher = pattern.matcher(cont);
                if (matcher.find()) {
                    String mtype = matcher.group(1);
                    if (fieldTypeMap.containsKey(mtype)) {
                        result.add(fieldTypeMap.get(mtype));
                    } else {
                        throw new RuntimeException("找不到对应的类型：" + cont + ".." + mtype);
                    }
                }
            }
        }
        return result;
    }

    private static boolean containVar(String cont) {
        if (cont.contains("KEY")) {
            return false;
        }
        for (String key : fieldTypeMap.keySet()) {
            if (cont.contains(key) || cont.contains(key.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private static String parseTableName(String sql) {
        String[] sqls = StringUtils.split(sql, "\n");
        for (String cont : sqls) {
            if (cont.contains("table") || cont.contains("TABLE")) {
                Pattern pattern = Pattern.compile("`([a-zA-Z_0-9]+)`");
                Matcher matcher = pattern.matcher(cont);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        }
        throw new RuntimeException("找不到tableName");
    }

    private static String field_solve(String string) {
        StringBuilder sb = new StringBuilder();
        boolean change = false;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch == '_') {
                change = true;
                continue;
            }
            if (change) {
                sb.append(Character.toUpperCase(ch));
                change = false;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
