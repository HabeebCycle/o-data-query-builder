package com.habeebcycle.framework.querybuilder.keyword;

public enum FilterExpression {
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    GREATER_THAN("gt"),
    LESS_THAN("le"),
    GREATER_THAN_EQUAL("ge"),
    LESS_THAN_EQUAL("le");

    private final String expression;

    FilterExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
