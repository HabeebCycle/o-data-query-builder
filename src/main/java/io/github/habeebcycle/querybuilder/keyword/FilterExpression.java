package io.github.habeebcycle.querybuilder.keyword;

public enum FilterExpression {
    EQ("eq"),
    NE("ne"),
    GT("gt"),
    LT("lt"),
    GE("ge"),
    LE("le");

    private final String expression;

    FilterExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}
