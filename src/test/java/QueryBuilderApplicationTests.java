import com.habeebcycle.framework.querybuilder.QueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.habeebcycle.framework.querybuilder.keyword.FilterExpression.*;
import static com.habeebcycle.framework.querybuilder.keyword.FilterPhrases.*;
import static com.habeebcycle.framework.querybuilder.utils.Constant.AND;
import static com.habeebcycle.framework.querybuilder.utils.Constant.OR;

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
                .filter(f -> f.filterExpression("Property", EQUALS, "MyValue"))
                .select("My Properties,mmmm")
                .toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void filterQueryTest() {
        String expected = "?$filter=Property1 eq 'Value1'";
        String query = new QueryBuilder()
                .filter(f -> f.filterExpression("Property1", EQUALS, "Value1"))
                .toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void filterPhraseTest() {
        String expected = "?$filter=contains(Property1,'Value1') and startswith(Property1,'Value1') and endswith(Property1,'Value1') and indexOf(Property1,'Value1') eq 1 and length(Property1) eq 19 and substring(Property1, 1, 2) eq 'ab'";
        String query = new QueryBuilder()
                .filter(f ->
                        f.filterPhrase(contains("Property1","Value1"))
                                .filterPhrase(startsWith("Property1","Value1"))
                                .filterPhrase(endsWith("Property1","Value1"))
                                .filterPhrase(indexOf("Property1","Value1", EQUALS, 1))
                                .filterPhrase(length("Property1", EQUALS, 19))
                                .filterPhrase(substring("Property1", 1, 2, EQUALS, "ab"))
                ).toQuery();

        Assertions.assertEquals(expected, query);
    }

    @Test
    void filterExpressionTest() {
        String expectedAnd = "?$filter=Property1 eq 'Value1' and Property2 eq 'Value1'";
        String queryAnd = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", EQUALS, "Value1")
                                .filterExpression("Property2", EQUALS, "Value1"),
                        AND)
                .toQuery();

        String expectedOr = "?$filter=Property1 eq 'Value1' or Property2 eq 'Value1'";
        String queryOr = new QueryBuilder()
                .filter(f -> f
                                .filterExpression("Property1", EQUALS, "Value1")
                                .filterExpression("Property2", EQUALS, "Value1"),
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
                        .filterExpression("Property1", EQUALS, "Value1")
                        .filterExpression("Property2", EQUALS, "Value2")
                        .or(f1 -> f1
                                .filterExpression("Property3", EQUALS, "Value3")
                                .filterExpression("Property4", EQUALS, "Value4")
                        )
                )
                .toQuery();

        Assertions.assertEquals(expected, query);
    }
}
