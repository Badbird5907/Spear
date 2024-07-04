package dev.badbird.spear.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateStringParser {
    private static final Pattern PATTERN = Pattern.compile("\\{([^}]+)}");

    public static List<String> parse(String template) {
        Matcher matcher = PATTERN.matcher(template);
        return matcher.results().map(m -> m.group(1)).toList();
    }

    public static String mapFields(Map<String, Object> map, String template) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    public static void main(String[] args) {
        System.out.println(parse("/abc/{def}/ghi/{jkl}"));
    }
}
