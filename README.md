# odata-query-builder
OData Query Builder in Java for Java Applications

Inspired by [Jared Mahan](https://github.com/jaredmahan/odata-query-builder) An eloquently fluent OData query builder for NPM

[![Build Status](https://travis-ci.org/jaredmahan/angular-searchFilter.svg?branch=master)](https://travis-ci.org/jaredmahan/odata-query-builder)
[![js-standard-style](https://img.shields.io/badge/code%20style-standard-brightgreen.svg)](http://standardjs.com/)

## Installation
#### Java 1.8 and later is required

Use Maven
```xml
<dependencies>
  <dependency>
    <groupId>io.github.habeebcycle</groupId>
    <artifactId>o-data-query-builder</artifactId>
    <version>0.0.2</version>
  </dependency>
  ...
</dependencies>
```
or Gradle
```groovy
implementation group: 'io.github.habeebcycle', name: 'o-data-query-builder', version: '0.0.2'
```
or
```groovy
implementation 'io.github.habeebcycle:o-data-query-builder:0.0.2'
```

### Usage
```jshelllanguage
String query = new QueryBuilder()
        .count()
        .top(5)
        .skip(5)
        .expand("MyProps")
        .orderBy("MyProp2")
        .filter(f -> f.filterExpression("Property", EQ, "MyValue"))
        .select("Field1,Field2")
        .toQuery();
```

Outputs:
`?$orderby=MyProp2&$top=5&$skip=5&$count=true&$expand=MyProps&$select=Field1,Field2&$filter=Property eq 'MyValue'`

# Filtering

## Filter Expressions
Filter expressions utilize [logical operators](http://docs.oasis-open.org/odata/odata/v4.01/cs01/part2-url-conventions/odata-v4.01-cs01-part2-url-conventions.html#sec_LogicalOperatorExamples) to filter data on a specific property.

Operator Options, changed in version 0.0.2 and above:
- Equal: `eq` - `FilterExpression.EQ`
- Not Equal: `ne` - `FilterExpression.NE`
- Greater Than: `gt` - `FilterExpression.GT`
- Greater Than or Equal: `ge` - `FilterExpression.GE`
- Less Than: `lt` - `FilterExpression.LT`
- Less Than or Equal: `le` - `FilterExpression.LE`

Operator Options, version 0.0.1:
- Equal: `eq` - `FilterExpression.EQUALS`
- Not Equal: `ne` - `FilterExpression.NOT_EQUALS`
- Greater Than: `gt` - `FilterExpression.GREATER_THAN`
- Greater Than or Equal: `ge` - `FilterExpression.GREATER_THAN_EQUAL`
- Less Than: `lt` - `FilterExpression.LESS_THAN`
- Less Than or Equal: `le` - `FilterExpression.LESS_THAN_EQUAL`

```jshelllanguage
String query = new QueryBuilder()
        .filter(f -> f.filterExpression("Property1", EQ, "Value1"))
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1'`

You can also combine filter expressions in version 0.0.2 and above:

```jshelllanguage
String[] values = {"value1", "value2"}; // or you can pass a list:  List.of("value1", "value2")
String query = new QueryBuilder()
    .filter(f -> f.filterExpressions("Property1", FilterExpression.EQ, values)).toQuery();
```
Outputs: `?$filter=Property1 eq 'value1' and Property1 eq 'value2'`

## Filter Phrases
Filter phrases are to be used with [canonical functions](http://docs.oasis-open.org/odata/odata/v4.01/cs01/part2-url-conventions/odata-v4.01-cs01-part2-url-conventions.html#sec_CanonicalFunctions). Filter Phrasing exposes the filter as a string which allows you to inject any of the various filtering mechanisms available in `OData v4`.

Below are a few examples:

```jshelllanguage
String query = new QueryBuilder()
        .filter(f ->
            f.filterPhrase(FilterPhrases.contains("Property1","Value1"))
                .filterPhrase(FilterPhrases.startsWith("Property1","Value1"))
                .filterPhrase(FilterPhrases.endsWith("Property1","Value1"))
                .filterPhrase(FilterPhrases.indexOf("Property1","Value1", EQ, 1))
                .filterPhrase(FilterPhrases.length("Property1", EQ, 19))
                .filterPhrase(FilterPhrases.substring("Property1", 1, 2, EQ, "ab"))
        ).toQuery();
```
Outputs: `?$filter=contains(Property1,'Value1') and startswith(Property1,'Value1') and endswith(Property1,'Value1') and indexOf(Property1,'Value1') eq 1 and length(Property1) eq 19 and substring(Property1, 1, 2) eq 'ab'`

You can also combine filter phrases in version 0.0.2 and above:

```jshelllanguage
List<String> phrases = List.of("startswith(Property1,'Value1')", "contains(Property1,'Value1')", "length(Property1) eq 19"); //you can also pass an array new String[]{"startswith(Property1,'Value1')", "length(Property1) eq 19"}
    String query = new QueryBuilder()
            .filter(f -> f.filterPhrases(phrases)).toQuery();
```
Outputs: `?$filter=startswith(Property1,'Value1') and contains(Property1,'Value1') and length(Property1) eq 19`

## Conditional Filtering Operators
By default, when you utilize `.filter` you are using the `and` operator. You can be explicit by passing your operator into the filter as a secondary parameter.
```jshelllanguage
String query = new QueryBuilder().filter(f -> f
        .filterExpression("Property1", EQ, "Value1")
        .filterExpression("Property2", EQ, "Value1"),
        AND)
    .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value1'`
```jshelllanguage
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", EQ, "Value1")
            .filterExpression("Property2", EQ, "Value2"),
            AND)
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2'`

```jshelllanguage
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", EQ, "Value1")
            .filterExpression("Property2", EQ, "Value2"),
            OR)
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' or Property2 eq 'Value2'`

## Nested Filter Chaining
Nested or [grouped](http://docs.oasis-open.org/odata/odata/v4.01/cs01/part2-url-conventions/odata-v4.01-cs01-part2-url-conventions.html#sec_Grouping) filtering is used when we need to write a more complex filter for a data set. This can be done by utilizing `.and()` or `.or()` with the filter.
```jshelllanguage
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", EQ, "Value1")
            .filterExpression("Property2", EQ, "Value2")
            .and(f1 -> f1  //can be - 'or'
                .filterExpression("Property3", EQ, "Value3")
                .filterExpression("Property4", EQ, "Value4")
            )
        )
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' and Property4 eq 'Value4')`

```jshelllanguage
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", EQ, "Value1")
            .filterExpression("Property2", EQ, "Value2")
            .or(f1 -> f1  //can be - 'and'
                .filterExpression("Property3", EQ, "Value3")
                .filterExpression("Property4", EQ, "Value4")
            )
        )
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' or Property4 eq 'Value4')`


### Reminder: We can still explicitly control the conditional operators within each of the filters by utilizing the filter's condition operator parameter which gives us even more control over the filter.
```jshelllanguage
String query6 = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", EQ, "Value1")
            .filterExpression("Property2", EQ, "Value2")
            .and(f1 -> f1  //can be - 'or'
                .filterExpression("Property3", EQ, "Value3")
                .filterExpression("Property4", EQ, "Value4")
            ),
            AND   //can be - "or"
        )
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' or Property4 eq 'Value4')`

#### Recently patched and updated! Looking For Contributors.
