package io.github.habeebcycle.querybuilder;

import io.github.habeebcycle.querybuilder.fragment.QueryFragment;
import io.github.habeebcycle.querybuilder.keyword.QueryKeyword;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.habeebcycle.querybuilder.keyword.QueryKeyword.*;
import static io.github.habeebcycle.querybuilder.utils.Constant.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class QueryBuilder {

    private final List<QueryFragment> queryFragment = new ArrayList<>();

    public QueryBuilder() {}

    public QueryBuilder orderBy(String fields) {
        this.clear(ORDER_BY);
        this.queryFragment.add(new QueryFragment(ORDER_BY,
                format("%s%s", ORDER_BY.getKeyword(), fields)));
        return this;
    }

    public QueryBuilder top(int top) {
        this.clear(TOP);
        this.queryFragment.add(new QueryFragment(TOP,
                format("%s%s", TOP.getKeyword(), top)));
        return this;
    }

    public QueryBuilder skip(int skip) {
        this.clear(SKIP);
        this.queryFragment.add(new QueryFragment(SKIP,
                format("%s%s", SKIP.getKeyword(), skip)));
        return this;
    }

    public QueryBuilder count() {
        this.clear(COUNT);
        this.queryFragment.add(new QueryFragment(COUNT,
                format("%s%s", COUNT.getKeyword(), true)));
        return this;
    }

    public QueryBuilder expand(String fields) {
        this.clear(EXPAND);
        this.queryFragment.add(new QueryFragment(EXPAND,
                format("%s%s", EXPAND.getKeyword(), fields)));
        return this;
    }

    public QueryBuilder select(String fields) {
        return select(fields.split(COM));
    }

    public QueryBuilder select(String ...fields) {
        return select(asList(fields));
    }

    public QueryBuilder select(List<String> fields) {
        this.clear(SELECT);
        this.queryFragment.add(new QueryFragment(SELECT,
                format("%s%s", SELECT.getKeyword(), trimJoin(fields))));
        return this;
    }

    public QueryBuilder filter(Function<FilterBuilder, FilterBuilder> filter) {
        return this.filter(filter, AND);
    }

    public QueryBuilder filter(Function<FilterBuilder, FilterBuilder> filter, String operator) {
        this.clear(FILTER);
        this.queryFragment.add(new QueryFragment(FILTER,
                filter.apply(new FilterBuilder()).toQuery(operator)));
        return this;
    }

    public String toQuery() {
        if(this.queryFragment.isEmpty())
            return EMPTY;

        List<QueryFragment> sortedFragments = this.queryFragment
                .stream()
                .sorted(Comparator.comparing(QueryFragment::getKeywordOrder))
                .collect(Collectors.toList());
        List<QueryFragment> unSortedFragment = sortedFragments.stream()
                .filter(f -> !f.getQueryKeyword().equals(FILTER))
                .collect(Collectors.toList());
        List<QueryFragment> filterFragment = sortedFragments.stream()
                .filter(f -> f.getQueryKeyword().equals(FILTER))
                .collect(Collectors.toList());

        String query = format("?%s",
                sortedFragments.stream()
                        .filter(f -> !f.getQueryKeyword().equals(FILTER))
                        .map(QueryFragment::getValue)
                        .collect(Collectors.joining(AMP)));

        if(filterFragment.isEmpty())
            return query;

        if(!unSortedFragment.isEmpty())
            query = query.concat(AMP);

        return query.concat(parseFilters(filterFragment, AND).trim());
    }

    private String parseFilters(List<QueryFragment> fragments, String operator) {
        if(fragments == null || fragments.isEmpty())
            return EMPTY;

        return FILTER.getKeyword().concat(
                fragments.stream().map(QueryFragment::getValue)
                .collect(Collectors.joining(format(" %s ", operator))));
    }

    private void clear(QueryKeyword queryKeyword) {
        this.queryFragment
                .removeIf(f -> f.getQueryKeyword().equals(queryKeyword));
    }

    private String trimJoin(List<String> arr) {
        return arr.stream().map(String::trim).collect(Collectors.joining(COM));
    }
}
