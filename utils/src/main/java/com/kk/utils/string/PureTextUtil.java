package com.kk.utils.string;


import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PureTextUtil {

    /**
     * 过滤 文本中的 script
     * @param content
     * @return
     */
    public static String filterScript(String content) {
        if (content == null) {
            return null;
        }
        String reg = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        Pattern p_script = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(content);
        content = m_script.replaceAll(""); // 过滤script标签

        reg = "<[\\sa-zA-z]*?script[^>]*?>";
        p_script = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        m_script = p_script.matcher(content);
        content = m_script.replaceAll(""); // 过滤script标签

        content = content.replace("<","");
        content = content.replace(">","");
        return content;
    }


    public static String removeHtml(String value){
        if (StringUtils.isEmpty(value)){
            return "";
        }
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(value);
        value=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(value);
        value=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(value);
        value = m_html.replaceAll("");//
        return value;
    }
}