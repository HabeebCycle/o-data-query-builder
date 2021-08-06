package com.habeebcycle.framework.querybuilder.keyword;

public enum QueryKeyword {
    ORDER_BY("$orderby=", 1),
    TOP("$top=", 2),
    SKIP("$skip=", 3),
    COUNT("$count=", 4),
    EXPAND("$expand=", 5),
    FILTER("$filter=", 6),
    SELECT("$select=", 7);

    private final String keyword;
    private final int order;

    QueryKeyword(final String keyword, final int order) {
        this.keyword = keyword;
        this.order = order;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getOrder() {
        return order;
    }
}
