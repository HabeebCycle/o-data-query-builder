import io.github.habeebcycle.querybuilder.QueryBuilder;
import io.github.habeebcycle.querybuilder.keyword.FilterExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.habeebcycle.querybuilder.keyword.FilterExpression.EQ;
import static io.github.habeebcycle.querybuilder.keyword.FilterPhrases.*;
import static io.github.habeebcycle.querybuilder.utils.Constant.AND;
import static io.github.habeebcycle.querybuilder.utils.Constant.OR;

class QueryBuilderApplicationTests {

    @Test
    void simpleQueryTest() {
        String expected = "?$orderby=MyProp2&$top=5&$skip=5&$count=true&$expand=MyProps&$select=My Properties,mmmm&$filter=Property eq 'MyValue'";
        String query = new QueryBuilder()
                .count()
                .top(5)
                .skip(5)
                .expand("MyProps")
                .orderBy("MyProp2")
                .filter(f -> f.filterExpression("Property", EQ, "MyValue"))
                .select("My Properties, mmmm")
                .toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void filterQueryTest() {
        String expected = "?$filter=Property1 eq 'Value1'";
        String query = new QueryBuilder()
                .filter(f -> f.filterExpression("Property1", EQ, "Value1"))
                .toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void filterPhraseTest() {
        String expected = "?$filter=contains(Property1,'Value1') and startswith(Property1,'Value1') and endswith(Property1,'Value1') and indexof(Property1,'Value1') eq 1 and length(Property1) eq 19 and substring(Property1, 1, 2) eq 'ab'";
        String query = new QueryBuilder()
                .filter(f ->
                        f.filterPhrase(contains("Property1","Value1"))
                                .filterPhrase(startsWith("Property1","Value1"))
                                .filterPhrase(endsWith("Property1","Value1"))
                                .filterPhrase(indexOf("Property1","Value1", EQ, 1))
                                .filterPhrase(length("Property1", EQ, 19))
                                .filterPhrase(substring("Property1", 1, 2, EQ, "ab"))
                ).toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void filterExpressionTest() {
        String expectedAnd = "?$filter=Property1 eq 'Value1' and Property2 eq 'Value1'";
        String queryAnd = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", EQ, "Value1")
                                .filterExpression("Property2", EQ, "Value1"),
                        AND)
                .toQuery();

        String expectedOr = "?$filter=Property1 eq 'Value1' or Property2 eq 'Value1'";
        String queryOr = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", EQ, "Value1")
                                .filterExpression("Property2", EQ, "Value1"),
                        OR)
                .toQuery();

        Assertions.assertEquals(expectedAnd, queryAnd);
        Assertions.assertEquals(expectedOr, queryOr);
    }

    @Test
    void complexQueryTest () {
        String expected = "?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' or Property4 eq 'Value4')";

        String query = new QueryBuilder()
                .filter(f -> f
                        .filterExpression("Property1", EQ, "Value1")
                        .filterExpression("Property2", EQ, "Value2")
                        .or(f1 -> f1
                                .filterExpression("Property3", EQ, "Value3")
                                .filterExpression("Property4", EQ, "Value4")
                        )
                )
                .toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void combineFilters () {
        String expected = "?$filter=Property1 eq 'value1' and Property1 eq 'value2'";
        String[] values = {"value1", "value2"};
        List<String> values1 = List.of(values);
        String query = new QueryBuilder()
                .filter(f -> f.filterExpressions("Property1", FilterExpression.EQ, List.of(values))).toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void combinePharses() {
        String expected = "?$filter=startswith(Property1,'Value1') and contains(Property1,'Value1') and length(Property1) eq 19";
        List<String> phrases = List.of("startswith(Property1,'Value1')", "contains(Property1,'Value1')", "length(Property1) eq 19");
        String query = new QueryBuilder()
                .filter(f -> f.filterPhrases(phrases)).toQuery();

        Assertions.assertEquals(expected, query);
    }
}
