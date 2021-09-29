package io.github.habeebcycle.querybuilder;

import io.github.habeebcycle.querybuilder.keyword.FilterExpression;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class ODataQueryBuilderApplication {
    public static void main(String[] args) {
        /*String stringDate = "18/07/2019 04:20:54 AM";
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
        var dt = LocalDateTime.parse(stringDate, formatter);
        //LocalDateTime dt = LocalDateTime.of(2020, 8, 5, 21, 12, 0);
        System.out.println(dt);*/
        final String ZULU_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String strDate = "03/02/2007 05:36:54 pm";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
        LocalDateTime ldt = LocalDateTime.parse(strDate, format);
        System.out.println(ldt);
        Object i = 0;
        System.out.println(Double.valueOf(String.valueOf(i)));

        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern(ZULU_TIME_FORMAT);
        System.out.println(ldt.format(newFormat));
        System.out.println(LocalDateTime.now().format(newFormat)); //2021-08-07T20:10:52.358Z
        System.out.println(LocalDateTime.parse("2021-08-07T20:10:52.358Z", newFormat)); //2021-08-07T20:10:52.358

        Instant fd = Instant.now();
        String sfd = fd.toString();
        System.out.println(sfd);
        Instant ins = Instant.parse("2020-11-04T22:38:14Z");
        System.out.println(ins);
        Timestamp tsp = Timestamp.from(ins);
        System.out.println(Timestamp.from(Instant.parse("2021-02-22T07:41:28Z")));

        //System.out.println(myTest(x -> x.filterPhrase("phrase1").filterPhrase("phrase2")));
        //System.out.println(QueryKeyword.FILTER.getKeyword());

        String query = new QueryBuilder()
                .count()
                .top(5)
                .skip(5)
                .expand("MyProps")
                .orderBy("MyProp2")
                .filter(f -> f.filterExpression("Property", FilterExpression.EQ, "MyValue"))
                .select("My Properties")
                .toQuery();

        String query2 = new QueryBuilder()
                .filter(f -> f.filterExpression("Property1", FilterExpression.EQ, "Value1"))
                .toQuery();

        String query3 = new QueryBuilder()
                .filter(f ->
                    f.filterPhrase("contains(Property1,'Value1')")
                        .filterPhrase("startswith(Property1,'Value1')")
                        .filterPhrase("endswith(Property1,'Value1')")
                        .filterPhrase("indexof(Property1,'Value1') eq 1")
                        .filterPhrase("length(Property1) eq 19")
                        .filterPhrase("substring(Property1, 1, 2) eq 'ab'")
                ).toQuery();

        List<String> phrases = List.of("startswith(Property1,'Value1')", "contains(Property1,'Value1')", "length(Property1) eq 19");
        String query3a = new QueryBuilder()
                .filter(f -> f.filterPhrases(phrases)).toQuery();

        String query3b = new QueryBuilder()
                .filter(f -> f.filterExpressions("Property1", FilterExpression.EQ, new String[]{"value1", "value2"})).toQuery();

        String query4 = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", FilterExpression.EQ, "Value1")
                                .filterExpression("Property2", FilterExpression.EQ, "Value1"),
                        "and")
                .toQuery();

        String query5 = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", FilterExpression.EQ, "Value1")
                                .filterExpression("Property2", FilterExpression.EQ, "Value1"),
                        "or")
                .toQuery();

        String query6 = new QueryBuilder()
                .filter(f -> f
                    .filterExpression("Property1", FilterExpression.EQ, "Value1")
                    .filterExpression("Property2", FilterExpression.EQ, "Value2")
                    .and(f1 -> f1  //can be - 'and'
                        .filterExpression("Property3", FilterExpression.EQ, "Value3")
                        .filterExpression("Property4", FilterExpression.EQ, "Value4")
                    ),
                        "and"   //can be - 'or'
                )
                .toQuery();

        String query7 = new QueryBuilder()
                .filter(f -> f
                        .filterExpression("Property1", FilterExpression.EQ, "Value1")
                        .filterExpression("Property2", FilterExpression.EQ, "Value2")
                        .or(f1 -> f1  //can be - 'or'
                                .filterExpression("Property3", FilterExpression.EQ, "Value3")
                                .filterExpression("Property4", FilterExpression.EQ, "Value4")
                        )
                )
                .toQuery();

        System.out.println(query3a);

    }

    public static String myTest(Function<FilterBuilder, FilterBuilder> filter) {
        return filter.apply(new FilterBuilder()).toQuery("h");
    }

}
