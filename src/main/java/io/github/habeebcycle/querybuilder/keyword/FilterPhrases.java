package io.github.habeebcycle.querybuilder.keyword;

import static java.lang.String.format;

/**
 * Filter Phrases
 */
public class FilterPhrases {

    public static String concat(String param1, String param2) {
        return format("concat(%s, %s)", param1, param2);
    }

    public static String contains(String param, String value) {
        return format("contains(%s,'%s')", param, value);
    }

    public static String startsWith(String param, String value) {
        return format("startswith(%s,'%s')", param, value);
    }

    public static String endsWith(String param, String value) {
        return format("endswith(%s,'%s')", param, value);
    }

    public static String indexOf(String param, String value, FilterExpression expression, int result) {
        return format("indexof(%s,'%s') %s %s", param, value, expression.getExpression(), result);
    }

    public static String length(String param, FilterExpression expression, int result) {
        return format("length(%s) %s %s", param, expression.getExpression(), result);
    }

    public static String substring(String param, int args1, FilterExpression expression, String result) {
        return format("substring(%s, %s) %s %s", param, args1, expression.getExpression(), result);
    }

    public static String substring(String param, int args1, int args2, FilterExpression expression, String result) {
        return format("substring(%s, %s, %s) %s '%s'", param, args1, args2, expression.getExpression(), result);
    }

    /**
     * substringof - Available in OData V 3.0
     * @param searchString String to search
     * @param searchInString String or property to search the string above
     * @return substringof('Alfreds',CompanyName)
     */
    public static String substringOf(String searchString, String searchInString) {
        return format("substringof('%s',%s)", searchString, searchInString);
    }

    public static String toLower(String param, FilterExpression expression, String result) {
        return format("tolower(%s) %s '%s'", param, expression.getExpression(), result);
    }

    public static String toUpper(String param, FilterExpression expression, String result) {
        return format("toupper(%s) %s '%s'", param, expression.getExpression(), result);
    }

    public static String trim(String param, FilterExpression expression, String result) {
        return format("trim(%s) %s %s", param, expression.getExpression(), result);
    }

    public static String ceiling(String param, FilterExpression expression, int result) {
        return format("ceiling(%s) %s %s", param, expression.getExpression(), result);
    }

    public static String floor(String param, FilterExpression expression, int result) {
        return format("floor(%s) %s %s", param, expression.getExpression(), result);
    }

    public static String round(String param, FilterExpression expression, int result) {
        return format("round(%s) %s %s", param, expression.getExpression(), result);
    }
}
