package cn.daenx.myauth.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yanp
 * @Description: 使用正则表达式，实现文本变量替换
 * @Date: 2021/10/15 18:34
 * @Version: 1.0.0
 */
@Slf4j
public class TemplateParseUtil {
    private final static Pattern pattern = Pattern.compile("((?<=\\{)([a-zA-Z\\d]+)(?=\\}))");
    public static String regParse(String template, Map<String, Object> map) {
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            String key = matcher.group();
            template = template.replaceAll("\\$\\{" + key + "\\}", map.get(key) + "");
        }
        return template;
    }
}