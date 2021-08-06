package com.habeebcycle.framework.querybuilder;

import com.habeebcycle.framework.querybuilder.fragment.QueryFragment;
import com.habeebcycle.framework.querybuilder.keyword.QueryKeyword;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class FilterBuilder {

    private final List<QueryFragment> queryFragment = new ArrayList<>();

    public FilterBuilder() {}

    public FilterBuilder filterExpression (String field, String operator, Object value) {
        String fragment = format("%s %s %s", field, operator, computeFragmentValue(value));
        this.queryFragment.add(new QueryFragment(QueryKeyword.FILTER, fragment));
        return this;
    }

    public FilterBuilder filterPhrase (String phrase) {
        this.queryFragment.add(new QueryFragment(QueryKeyword.FILTER, phrase));
        return this;
    }

    public FilterBuilder and (Function<FilterBuilder, FilterBuilder> filter) {
        this.queryFragment.add(new QueryFragment(QueryKeyword.FILTER,
                format("(%s)", filter.apply(new FilterBuilder()).toQuery("and"))));
        return this;
    }

    public FilterBuilder or (Function<FilterBuilder, FilterBuilder> filter) {
        this.queryFragment.add(new QueryFragment(QueryKeyword.FILTER,
                format("(%s)", filter.apply(new FilterBuilder()).toQuery("or"))));
        return this;
    }

    public String toQuery (String operator) {
        if (this.queryFragment.isEmpty())
            return "";

        return this.queryFragment
                .stream()
                .map(QueryFragment::getValue)
                .collect(Collectors.joining(format(" %s ", operator)));
    }

    private String computeFragmentValue(Object value) {
        if (value instanceof String) {
            return format("'%s'", value);
        }

        return value.toString();
    }
}
