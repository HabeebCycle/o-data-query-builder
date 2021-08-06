## Recently patched and updated! Looking For Contributors.

# odata-query-builder
OData Query Builder in Java for Java Applications

Inspired by [Jared Mahan](https://github.com/jaredmahan/odata-query-builder) An eloquently fluent OData query builder for NPM

[![Build Status](https://travis-ci.org/jaredmahan/angular-searchFilter.svg?branch=master)](https://travis-ci.org/jaredmahan/odata-query-builder)
[![js-standard-style](https://img.shields.io/badge/code%20style-standard-brightgreen.svg)](http://standardjs.com/)

## Installation
#### Java 1.8 and later is required

Use Maven
```
<dependencies>
  <dependency>
    <groupId>com.habeebcycle.framework</groupId>
    <artifactId>o-data-query-builder</artifactId>
    <version>0.0.1</version>
  </dependency>
  ...
</dependencies>
```
or Gradle
```
compile 'com.habeebcycle.framework:o-data-query-builder:0.0.1'
```

### Usage
```
String query = new QueryBuilder()
        .count()
        .top(5)
        .skip(5)
        .expand("MyProps")
        .orderBy("MyProp2")
        .filter(f -> f.filterExpression("Property", "eq", "MyValue"))
        .select("Field1,Field2")
        .toQuery();
```

Outputs:
`?$orderby=MyProp2&$top=5&$skip=5&$count=true&$expand=MyProps&$select=Field1,Field2&$filter=Property eq 'MyValue'`

# Filtering

## Filter Expressions
Filter expressions utilize [logical operators](http://docs.oasis-open.org/odata/odata/v4.01/cs01/part2-url-conventions/odata-v4.01-cs01-part2-url-conventions.html#sec_LogicalOperatorExamples) to filter data on a specific property.

Operator Options:
- Equal: `eq`
- Not Eqaul: `ne`
- Greater Than: `gt`
- Greater Than or Equal: `ge`
- Less Than: `lt`
- Less Than or Equal: `le`

```
String query = new QueryBuilder()
        .filter(f -> f.filterExpression("Property1", "eq", "Value1"))
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1'`

## Filter Phrases
Filter phrases are to be used with [canonical functions](http://docs.oasis-open.org/odata/odata/v4.01/cs01/part2-url-conventions/odata-v4.01-cs01-part2-url-conventions.html#sec_CanonicalFunctions). Filter Phrasing exposes the filter as a string which allows you to inject any of the various filtering mechanisms available in `OData v4`.

Below are a few examples:

```
String query = new QueryBuilder()
        .filter(f ->
            f.filterPhrase("contains(Property1,'Value1')")
                .filterPhrase("startswith(Property1,'Value1')")
                .filterPhrase("endswith(Property1,'Value1')")
                .filterPhrase("indexOf(Property1,'Value1') eq 1")
                .filterPhrase("length(Property1) eq 19")
                .filterPhrase("substring(Property1, 1, 2) eq 'ab'")
        ).toQuery();
```
Outputs: `?$filter=contains(Property1,'Value1') and startswith(Property1,'Value1') and endswith(Property1,'Value1') and indexOf(Property1,'Value1') eq 1 and length(Property1) eq 19 and substring(Property1, 1, 2) eq 'ab'`

## Conditional Filtering Operators
By default, when you utilize `.filter` you are using the `and` operator. You can be explicit by passing your operator into the filter as a secondary parameter.
```
const query = new QueryBuilder().filter(f => f
      .filterExpression('Property1', 'eq', 'Value1')
      .filterExpression('Property2', 'eq', 'Value1'),
      'and'
    ).toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value1'`
```
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", "eq", "Value1")
            .filterExpression("Property2", "eq", "Value2"),
            "and")
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2'`

```
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", "eq", "Value1")
            .filterExpression("Property2", "eq", "Value2"),
            "or")
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' or Property2 eq 'Value2'`

## Nested Filter Chaining
Nested or [grouped](http://docs.oasis-open.org/odata/odata/v4.01/cs01/part2-url-conventions/odata-v4.01-cs01-part2-url-conventions.html#sec_Grouping) filtering is used when we need to write a more complex filter for a data set. This can be done by utilizing `.and()` or `.or()` with the filter.
```
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", "eq", "Value1")
            .filterExpression("Property2", "eq", "Value2")
            .and(f1 -> f1  //can be - 'or'
                .filterExpression("Property3", "eq", "Value3")
                .filterExpression("Property4", "eq", "Value4")
            )
        )
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' and Property4 eq 'Value4')`

```
String query = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", "eq", "Value1")
            .filterExpression("Property2", "eq", "Value2")
            .or(f1 -> f1  //can be - 'and'
                .filterExpression("Property3", "eq", "Value3")
                .filterExpression("Property4", "eq", "Value4")
            )
        )
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' or Property4 eq 'Value4')`


### Reminder: We can still explicitly control the conditional operators within each of the filters by utilizing the filter's condition operator parameter which gives us even more control over the filter.
```
String query6 = new QueryBuilder()
        .filter(f -> f
            .filterExpression("Property1", "eq", "Value1")
            .filterExpression("Property2", "eq", "Value2")
            .and(f1 -> f1  //can be - 'or'
                .filterExpression("Property3", "eq", "Value3")
                .filterExpression("Property4", "eq", "Value4")
            ),
            "and"   //can be - 'or'
        )
        .toQuery();
```
Outputs: `?$filter=Property1 eq 'Value1' and Property2 eq 'Value2' and (Property3 eq 'Value3' or Property4 eq 'Value4')`

