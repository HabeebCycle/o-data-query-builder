package com.habeebcycle.framework.querybuilder;

import com.habeebcycle.framework.querybuilder.fragment.QueryFragment;
import com.habeebcycle.framework.querybuilder.keyword.FilterExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.habeebcycle.framework.querybuilder.keyword.QueryKeyword.FILTER;
import static com.habeebcycle.framework.querybuilder.utils.Constant.AND;
import static com.habeebcycle.framework.querybuilder.utils.Constant.OR;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class FilterBuilder {

    private final List<QueryFragment> queryFragment = new ArrayList<>();

    public FilterBuilder() {}

    public FilterBuilder filterExpression (String field, FilterExpression expression, Object value) {
        String fragment = format("%s %s %s", field, expression.getExpression(), computeFragmentValue(value));
        this.queryFragment.add(new QueryFragment(FILTER, fragment));
        return this;
    }

    public FilterBuilder filterPhrase (String phrase) {
        this.queryFragment.add(new QueryFragment(FILTER, phrase));
        return this;
    }

    public FilterBuilder and (Function<FilterBuilder, FilterBuilder> filter) {
        this.queryFragment.add(new QueryFragment(FILTER,
                format("(%s)", filter.apply(new FilterBuilder()).toQuery(AND))));
        return this;
    }

    public FilterBuilder or (Function<FilterBuilder, FilterBuilder> filter) {
        this.queryFragment.add(new QueryFragment(FILTER,
                format("(%s)", filter.apply(new FilterBuilder()).toQuery(OR))));
        return this;
    }

    public String toQuery (String operator) {
        if (this.queryFragment.isEmpty())
            return EMPTY;

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
