package io.github.habeebcycle.querybuilder;

import io.github.habeebcycle.querybuilder.fragment.QueryFragment;
import io.github.habeebcycle.querybuilder.keyword.FilterExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.habeebcycle.querybuilder.keyword.QueryKeyword.FILTER;
import static io.github.habeebcycle.querybuilder.utils.Constant.AND;
import static io.github.habeebcycle.querybuilder.utils.Constant.OR;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class FilterBuilder {

    private final List<QueryFragment> queryFragment = new ArrayList<>();

    public FilterBuilder() {}

    public FilterBuilder filterExpression (String field, FilterExpression expression, Object value) {
        return filterExpressions(field, expression, List.of(value));
    }

    public FilterBuilder filterExpressions (String field, FilterExpression expression, Object... value) {
        return filterExpressions(field, expression, List.of(value));
    }

    public FilterBuilder filterExpressions (String field, FilterExpression expression, List<Object> values) {
        String fragment = null;
        for(Object o : values) {
            fragment = format("%s %s %s", field, expression.getExpression(), computeFragmentValue(o));
            this.queryFragment.add(new QueryFragment(FILTER, fragment));
        }
        return this;
    }

    public FilterBuilder filterPhrase (String phrase) {
        return filterPhrases(List.of(phrase));
    }

    public FilterBuilder filterPhrases (String... phrases) {
        return filterPhrases(List.of(phrases));
    }

    public FilterBuilder filterPhrases (List<String> phrases) {
        for(String phrase : phrases) {
            this.queryFragment.add(new QueryFragment(FILTER, phrase));
        }
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
