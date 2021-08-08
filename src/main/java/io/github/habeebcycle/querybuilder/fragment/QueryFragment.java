package io.github.habeebcycle.querybuilder.fragment;

import io.github.habeebcycle.querybuilder.keyword.QueryKeyword;

public class QueryFragment {

    private final QueryKeyword queryKeyword;
    private final String value;

    public QueryFragment(final QueryKeyword queryKeyword, final String value) {
        this.queryKeyword = queryKeyword;
        this.value = value;
    }

    public QueryKeyword getQueryKeyword() {
        return queryKeyword;
    }

    public String getKeywordText() {
        return queryKeyword.getKeyword();
    }

    public int getKeywordOrder() {
        return queryKeyword.getOrder();
    }

    public String getValue() {
        return value;
    }
}
