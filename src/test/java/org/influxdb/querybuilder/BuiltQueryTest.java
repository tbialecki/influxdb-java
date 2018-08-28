package org.influxdb.querybuilder;

import org.influxdb.dto.Query;
import org.junit.jupiter.api.Test;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuiltQueryTest {

	private static final String DATABASE = "testdb";

	@Test
	public void testAlias() {
		Query query = new Query("SELECT MAX(k) AS hello FROM foo;", DATABASE);
		Query select = select().max("k").as("hello").from(DATABASE,"foo");

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testRegex() {
		Query query = new Query("SELECT MAX(k) FROM foo WHERE k =~ /[0-9]/;",DATABASE);
		Query select = select().max("k").from(DATABASE,"foo").where(regex("k", "/[0-9]/"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testNegativeRegex() {
		Query query = new Query("SELECT MAX(k) FROM foo WHERE k ~! /[0-9]/;",DATABASE);
		Query select = select().max("k").from(DATABASE,"foo").where(nregex("k", "/[0-9]/"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testContains() {
		Query query = new Query("SELECT MAX(k) FROM foo WHERE k =~ /*text*/;",DATABASE);
		Query select = select().max("k").from(DATABASE,"foo").where(contains("k", "text"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testDistinct() {
		Query query = new Query("SELECT DISTINCT k FROM foo;",DATABASE);
		Query select = select().column("k").distinct().from(DATABASE ,"foo");

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testDistinctWithExpression() {
		Query query = new Query("SELECT DISTINCT COUNT(test1) FROM foo LIMIT 1 OFFSET 20;", DATABASE);
		Query select = select().count("test1").distinct().from(DATABASE ,"foo").limit(1, 20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testMultipleColumns() {
		Query query = select().column("test1").distinct().column("test2").from(DATABASE , "foo");
		assertThrows(IllegalStateException.class, () -> query.getCommand(), "Cannot mix all columns and specific columns");
	}

	@Test
	public void testOrdering() {

		Query query = new Query("SELECT * FROM foo WHERE k=4 AND c>'a' AND c<='z' ORDER BY time ASC;",DATABASE);

		Select select = select().all().from(DATABASE,"foo")
				.where(eq("k", 4))
				.and(gt("c", "a"))
				.and(lte("c", "z"))
				.orderBy(asc());

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testSelect() {
		Query query = new Query("SELECT * FROM foo WHERE k=4 AND c>'a' AND c<='z';",DATABASE);
		Query select = select().all().from(DATABASE,"foo").where(eq("k", 4)).and(gt("c", "a")).and(lte("c", "z"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testMean() {
		Query query = new Query("SELECT MEAN(k) FROM foo WHERE k=4 AND c>'a' AND c<='z';",DATABASE);
		Query select = select().mean("k")
				.from(DATABASE,"foo")
				.where(eq("k", 4))
				.and(gt("c", "a"))
				.and(lte("c", "z"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testSum() {
		Query query = new Query("SELECT SUM(k) FROM foo WHERE k=4 AND c>'a' AND c<='z';",DATABASE);
		Query select = select().sum("k")
				.from(DATABASE,"foo")
				.where(eq("k", 4))
				.and(gt("c", "a"))
				.and(lte("c", "z"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testMin() {
		Query query = new Query("SELECT MIN(k) FROM foo WHERE k=4 AND c>'a' AND c<='z';",DATABASE);
		Query select = select().min("k")
				.from(DATABASE,"foo")
				.where(eq("k", 4))
				.and(gt("c", "a"))
				.and(lte("c", "z"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testMax() {
		Query query = new Query("SELECT MAX(k) FROM foo WHERE k=4 AND c>'a' AND c<='z';",DATABASE);
		Query select = select().max("k")
				.from(DATABASE,"foo")
				.where(eq("k", 4))
				.and(gt("c", "a"))
				.and(lte("c", "z"));

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testSelectField() {
		Query query = new Query("SELECT test1,test2 FROM foo;",DATABASE);
		Query select = select().column("test1").column("test2") .from(DATABASE , "foo");
		
		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testGroupBy() {
		Query query = new Query("SELECT test1 FROM foo GROUP BY test2,test3;",DATABASE);
		Query select = select().column("test1") .from(DATABASE , "foo").groupBy("test2","test3");

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testLimit() {
		Query query = new Query("SELECT test1 FROM foo GROUP BY test2,test3 LIMIT 1;",DATABASE);
		Query select = select().column("test1").from(DATABASE , "foo").groupBy("test2","test3").limit(1);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testLimitOffset() {
		Query query = new Query("SELECT test1 FROM foo GROUP BY test2,test3 LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().column("test1").from(DATABASE , "foo").groupBy("test2","test3").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testCount() {
		Query query = new Query("SELECT COUNT(test1) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().count("test1").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testMinWithLimit() {
		Query query = new Query("SELECT MIN(test1) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().min("test1").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testMaxWithLimit() {
		Query query = new Query("SELECT MAX(test1) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().max("test1").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testSumWithLimit() {
		Query query = new Query("SELECT SUM(test1) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().sum("test1").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testAggregateCompination() {
		Query query = new Query("SELECT MAX(test1),MIN(test2),COUNT(test3),SUM(test4) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().max("test1").min("test2").count("test3").sum("test4").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testFunctionCall() {
		Query query = new Query("SELECT MEDIAN(test1) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().function("MEDIAN","test1").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testFunctionInsideFunction() {
		Query query = new Query("SELECT MEDIAN(now()) FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().function("MEDIAN",now()).from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

	@Test
	public void testRawStringOnSelection() {
		Query query = new Query("SELECT an expression on select FROM foo LIMIT 1 OFFSET 20;",DATABASE);
		Query select = select().raw("an expression on select").from(DATABASE , "foo").limit(1,20);

		assertEquals(query.getCommand(),select.getCommand());
		assertEquals(query.getDatabase(),select.getDatabase());
	}

}


