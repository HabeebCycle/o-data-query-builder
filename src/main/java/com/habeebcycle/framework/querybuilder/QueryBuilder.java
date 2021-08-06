package com.habeebcycle.framework.querybuilder;

import com.habeebcycle.framework.querybuilder.fragment.QueryFragment;
import com.habeebcycle.framework.querybuilder.keyword.QueryKeyword;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class QueryBuilder {

    private final List<QueryFragment> queryFragment = new ArrayList<>();

    public QueryBuilder() {}

    public QueryBuilder orderBy(String fields) {
        this.clear(QueryKeyword.ORDER_BY);
        this.queryFragment.add(new QueryFragment(QueryKeyword.ORDER_BY,
                format("%s%s", QueryKeyword.ORDER_BY.getKeyword(), fields)));
        return this;
    }

    public QueryBuilder top(int top) {
        this.clear(QueryKeyword.TOP);
        this.queryFragment.add(new QueryFragment(QueryKeyword.TOP,
                format("%s%s", QueryKeyword.TOP.getKeyword(), top)));
        return this;
    }

    public QueryBuilder skip(int skip) {
        this.clear(QueryKeyword.SKIP);
        this.queryFragment.add(new QueryFragment(QueryKeyword.SKIP,
                format("%s%s", QueryKeyword.SKIP.getKeyword(), skip)));
        return this;
    }

    public QueryBuilder count() {
        this.clear(QueryKeyword.COUNT);
        this.queryFragment.add(new QueryFragment(QueryKeyword.COUNT,
                format("%s%s", QueryKeyword.COUNT.getKeyword(), true)));
        return this;
    }

    public QueryBuilder expand(String fields) {
        this.clear(QueryKeyword.EXPAND);
        this.queryFragment.add(new QueryFragment(QueryKeyword.EXPAND,
                format("%s%s", QueryKeyword.EXPAND.getKeyword(), fields)));
        return this;
    }

    public QueryBuilder select(String fields) {
        this.clear(QueryKeyword.SELECT);
        this.queryFragment.add(new QueryFragment(QueryKeyword.SELECT,
                format("%s%s", QueryKeyword.SELECT.getKeyword(), fields)));
        return this;
    }

    public QueryBuilder filter(Function<FilterBuilder, FilterBuilder> filter) {
        return this.filter(filter, "and");
    }

    public QueryBuilder filter(Function<FilterBuilder, FilterBuilder> filter, String operator) {
        this.clear(QueryKeyword.FILTER);
        this.queryFragment.add(new QueryFragment(QueryKeyword.FILTER,
                filter.apply(new FilterBuilder()).toQuery(operator)));
        return this;
    }

    public QueryBuilder clear(QueryKeyword queryKeyword) {
        this.queryFragment
                .removeIf(f -> f.getQueryKeyword().equals(queryKeyword));
        return this;
    }

    public String toQuery() {
        //this.queryFragment.forEach(f -> System.out.println(f.getValue()));
        if(this.queryFragment.isEmpty())
            return "";

        List<QueryFragment> sortedFragments = this.queryFragment
                .stream()
                .sorted(Comparator.comparing(QueryFragment::getKeywordOrder))
                .collect(Collectors.toList());
        List<QueryFragment> unSortedFragment = sortedFragments.stream()
                .filter(f -> !f.getQueryKeyword().equals(QueryKeyword.FILTER))
                .collect(Collectors.toList());
        List<QueryFragment> filterFragment = sortedFragments.stream()
                .filter(f -> f.getQueryKeyword().equals(QueryKeyword.FILTER))
                .collect(Collectors.toList());

        String query = format("?%s",
                sortedFragments.stream()
                        .filter(f -> !f.getQueryKeyword().equals(QueryKeyword.FILTER))
                        .map(QueryFragment::getValue)
                        .collect(Collectors.joining("&")));

        if(filterFragment.isEmpty())
            return query;

        if(!unSortedFragment.isEmpty())
            query = query.concat("&");

        return query.concat(parseFilters(filterFragment, "and").trim());
    }

    private String parseFilters(List<QueryFragment> fragments, String operator) {
        if(fragments == null || fragments.isEmpty())
            return "";

        return QueryKeyword.FILTER.getKeyword().concat(
                fragments.stream().map(QueryFragment::getValue)
                .collect(Collectors.joining(format(" %s ", operator))));
    }
}
