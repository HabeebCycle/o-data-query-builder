package com.habeebcycle.framework.querybuilder;

import com.habeebcycle.framework.querybuilder.keyword.QueryKeyword;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.habeebcycle.framework.querybuilder.keyword.FilterExpression.EQUALS;

public class ODataQueryBuilderApplication {
    public static void main(String[] args) {
        /*String stringDate = "18/07/2019 04:20:54 AM";
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
        var dt = LocalDateTime.parse(stringDate, formatter);
        //LocalDateTime dt = LocalDateTime.of(2020, 8, 5, 21, 12, 0);
        System.out.println(dt);*/
        final String ZULU_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String strDate = "03/02/2007 05:36 pm";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
        LocalDateTime ldt = LocalDateTime.parse(strDate, format);
        System.out.println(ldt);

        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern(ZULU_TIME_FORMAT);
        System.out.println(ldt.format(newFormat));

        //System.out.println(myTest(x -> x.filterPhrase("phrase1").filterPhrase("phrase2")));
        //System.out.println(QueryKeyword.FILTER.getKeyword());

        String query = new QueryBuilder()
                .count()
                .top(5)
                .skip(5)
                .expand("MyProps")
                .orderBy("MyProp2")
                .filter(f -> f.filterExpression("Property", EQUALS, "MyValue"))
                .select("My Properties,mmmm")
                .toQuery();

        String query2 = new QueryBuilder()
                .filter(f -> f.filterExpression("Property1", EQUALS, "Value1"))
                .toQuery();

        String query3 = new QueryBuilder()
                .filter(f ->
                    f.filterPhrase("contains(Property1,'Value1')")
                        .filterPhrase("startswith(Property1,'Value1')")
                        .filterPhrase("endswith(Property1,'Value1')")
                        .filterPhrase("indexOf(Property1,'Value1') eq 1")
                        .filterPhrase("length(Property1) eq 19")
                        .filterPhrase("substring(Property1, 1, 2) eq 'ab'")
                ).toQuery();

        String query4 = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", EQUALS, "Value1")
                                .filterExpression("Property2", EQUALS, "Value1"),
                        "and")
                .toQuery();

        String query5 = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", EQUALS, "Value1")
                                .filterExpression("Property2", EQUALS, "Value1"),
                        "or")
                .toQuery();

        String query6 = new QueryBuilder()
                .filter(f -> f
                    .filterExpression("Property1", EQUALS, "Value1")
                    .filterExpression("Property2", EQUALS, "Value2")
                    .and(f1 -> f1  //can be - 'and'
                        .filterExpression("Property3", EQUALS, "Value3")
                        .filterExpression("Property4", EQUALS, "Value4")
                    ),
                        "and"   //can be - 'or'
                )
                .toQuery();

        String query7 = new QueryBuilder()
                .filter(f -> f
                        .filterExpression("Property1", EQUALS, "Value1")
                        .filterExpression("Property2", EQUALS, "Value2")
                        .or(f1 -> f1  //can be - 'or'
                                .filterExpression("Property3", EQUALS, "Value3")
                                .filterExpression("Property4", EQUALS, "Value4")
                        )
                )
                .toQuery();

        System.out.println(query5);

    }

    public static String myTest(Function<FilterBuilder, FilterBuilder> filter) {
        return filter.apply(new FilterBuilder()).toQuery("h");
    }

}
