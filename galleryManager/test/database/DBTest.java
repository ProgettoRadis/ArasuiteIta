package database;

import static org.junit.Assert.assertEquals;
import junit.framework.TestSuite;

import org.junit.Test;

public class DBTest extends TestSuite {

	@Test
	public void escapeQueryWithSeveralQueries() {
		String query = "";
		String expected = "";

		// Test 1
		query = "select * from ArawordView where word=\"perro\"";
		expected = "select * from ArawordView where word=\"perro\"";
		assertEquals(expected, DB.escapeQuery(query));

		// Test 2
		query = "select * from ArawordView where word=\"*perro\"";
		expected = "select * from ArawordView where word=\"%perro\"";
		assertEquals(expected, DB.escapeQuery(query));

		// Test 3
		query = "select * from ArawordView where word=\"pe*rro\"";
		expected = "select * from ArawordView where word=\"pe%rro\"";
		assertEquals(expected, DB.escapeQuery(query));

		// Test 4
		query = "SELECT * from ArawordView where word=\"*\"";
		expected = "SELECT * from ArawordView where word=\"%\"";
		assertEquals(expected, DB.escapeQuery(query));

		// Test 4
		query = "seLECt *, fromTable, * from ArawordView where word=\"perro *\"";
		expected = "seLECt *, fromTable, * from ArawordView where word=\"perro %\"";
		assertEquals(expected, DB.escapeQuery(query));
	}
}
