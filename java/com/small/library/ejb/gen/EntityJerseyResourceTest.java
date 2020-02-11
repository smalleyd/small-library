package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates Dropwizard.io the unit test skeletons for the Jersey RESTful resources.
*   The metadata is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 6/11/2015
*
***************************************************************************************/

public class EntityJerseyResourceTest extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "ResourceTest";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityJerseyResourceTest(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public EntityJerseyResourceTest(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version.
	*/
	public EntityJerseyResourceTest(PrintWriter writer,
		String author, Table table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMethods();

		writeFooter();
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(table.name)) + ".java";
	}

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
	}

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		var packageName = getPackageName();
		var domainPackageName = getDomainPackageName();
		var basePackageName = getBasePackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		var name = getObjectName();
		writeLine("import static org.fest.assertions.api.Assertions.assertThat;");
		writeLine("import static " + domainPackageName + ".dwtesting.TestingUtils.*;");
		writeLine();
		writeLine("import java.util.*;");
		writeLine("import javax.ws.rs.client.*;");
		writeLine("import javax.ws.rs.core.GenericType;");
		writeLine("import javax.ws.rs.core.Response;");
		writeLine();
		writeLine("import org.junit.jupiter.api.*;");
		writeLine("import org.junit.jupiter.api.extension.ExtendWith;");
		writeLine();
		writeLine("import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;");
		writeLine("import io.dropwizard.testing.junit5.ResourceExtension;");
		writeLine();
		writeLine("import " + domainPackageName + ".junit.hibernate.*;");
		writeLine("import " + domainPackageName + ".dwservice.dao.QueryResults;");
		writeLine("import " + domainPackageName + ".dwservice.errors.ValidationExceptionMapper;");
		writeLine("import " + domainPackageName + ".dwservice.mediatype.UTF8MediaType;");
		writeLine("import " + domainPackageName + ".dwservice.value.OperationResponse;");
		writeLine("import " + basePackageName + "." + getAppName() + "App;");
		writeLine("import " + basePackageName + ".dao." + EntityBeanDAO.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".filter." + EntityBeanFilter.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".value." + EntityBeanValueObject.getClassName(name) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tFunctional test for the data access object that handles access to the " + name + " entity.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version " + getVersion());
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		var name = getClassName();
		var objectName = getObjectName();
		var daoName = EntityBeanDAO.getClassName(objectName);
		var mapping = fromObjectNameToMemberName(objectName) + "s";
		var filterName = EntityBeanFilter.getClassName(objectName);
		var valueName = EntityBeanValueObject.getClassName(objectName);
		var resourceName = EntityJerseyResource.getClassName(objectName);

		writeLine();
		writeLine("@TestMethodOrder(MethodOrderer.Alphanumeric.class)	// Ensure that the methods are executed in order listed.");
		writeLine("@ExtendWith(DropwizardExtensionsSupport.class)");
		writeLine("public class " + name);
		writeLine("{");
		writeLine("public static final HibernateRule DAO_RULE = new HibernateRule(" + getAppName() + "App.ENTITIES);", 1);
		writeLine("public final HibernateTransactionRule transRule = new HibernateTransactionRule(DAO_RULE);", 1);
		writeLine();
		writeLine("private static " + daoName + " dao = null;", 1);
		writeLine("private static " + valueName + " VALUE = null;", 1);
		writeLine();
		writeLine("public final ResourceExtension RULE = ResourceExtension.builder()", 1);
		writeLine(".addResource(new ValidationExceptionMapper())", 2);
		writeLine(".addResource(new " + resourceName + "(dao)).build();", 2);
		writeLine();
		writeLine("/** Primary URI to test. */", 1);
		writeLine("private static final String TARGET = \"/" + mapping + "\";", 1);
		writeLine();
		writeLine("/** Generic types for reading values from responses. */", 1);
		writeLine("private static final GenericType<List<" + valueName + ">> TYPE_LIST_VALUE = new GenericType<List<" + valueName + ">>() {};", 1);
		writeLine("private static final GenericType<QueryResults<" + valueName + ", " + filterName + ">> TYPE_QUERY_RESULTS =", 1);
		writeLine("new GenericType<QueryResults<" + valueName + ", " + filterName + ">>() {};", 2);
		writeLine();
		writeLine("@BeforeAll", 1);
		writeLine("public static void up()", 1);
		writeLine("{", 1);
		writeLine("var factory = DAO_RULE.getSessionFactory();", 2);
		writeLine("dao = new " + daoName + "(factory);", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		var name = getObjectName();
		var pkTypeName = getPkJavaType();
		var filterName = EntityBeanFilter.getClassName(name);
		var valueName = EntityBeanValueObject.getClassName(name);
		var to_s = "String".equals(pkTypeName) ? "" : ".toString()";

		// For testing non-existence, use a value to add to the primary key.
		var invalidId = "\"INVALID\"";
		for (var i : columnInfo)
		{
			if (i.isPartOfPrimaryKey)
			{
				if (!i.isCharacter)
					invalidId = (10 < i.size) ? "1000L" : "1000";

				break;
			}
		}

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void add()", 1);
		writeLine("{", 1);
		writeLine("// TODO: populate the VALUE with data.", 2);
		writeLine("var response = request()", 2);
		writeLine(".post(Entity.entity(VALUE = new " + valueName + "(), UTF8MediaType.APPLICATION_JSON_TYPE));", 3);
		writeLine("Assertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");", 2);
		writeLine();
		writeLine("var value = response.readEntity(" + valueName + ".class);", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void find()", 1);
		writeLine("{", 1);
		writeLine("var response = request(target().queryParam(\"name\", \"\")).get();", 2);
		writeLine("Assertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");", 2);
		writeLine();
		writeLine("var values = response.readEntity(TYPE_LIST_VALUE);", 2);
		writeLine("Assertions.assertNotNull(values, \"Exists\");", 2);
		writeLine();
		writeLine("// TODO: do other checks.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void get()", 1);
		writeLine("{", 1);
		writeLine("var response = get(VALUE.id);", 2);
		writeLine("Assertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");", 2);
		writeLine();
		writeLine("var value = response.readEntity(" + valueName + ".class);", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the GET endpoint. */", 1);
		writeLine("private Response get(final " + pkTypeName + " id)", 1);
		writeLine("{", 1);
		writeLine("return request(id" + to_s + ").get();", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void getWithException()", 1);
		writeLine("{", 1);
		writeLine("Assertions.assertEquals(HTTP_STATUS_VALIDATION_EXCEPTION, get(VALUE.id + " + invalidId + ").getStatus(), \"Status\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify()", 1);
		writeLine("{", 1);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 1L);", 2);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 0L);", 2);
		writeLine();
		writeLine("// TODO: provide a change to the VALUE.", 2);
		writeLine("var response = request().put(Entity.entity(VALUE, UTF8MediaType.APPLICATION_JSON_TYPE));", 2);
		writeLine("Assertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");", 2);
		writeLine();
		writeLine("var value = response.readEntity(" + valueName + ".class);", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify_count()", 1);
		writeLine("{", 1);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 0L);", 2);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 1L);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify_get()", 1);
		writeLine("{", 1);
		writeLine("var value = get(VALUE.id).readEntity(" + valueName + ".class);", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("// TODO: check the changed property.", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void search()", 1);
		writeLine("{", 1);
		writeLine("var hourAgo = hourAgo();", 2);
		writeLine("var hourAhead = hourAhead();", 2);
		writeLine();
		for (var i : columnInfo)
		{
			writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "(VALUE." + i.memberVariableName + "), 1L);", 2);
			if (i.isTime)
			{
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAgo), 1L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "To(hourAhead), 1L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAgo)." + i.withMethodName + "To(hourAhead), 1L);", 2);
			}
			else if (i.isRange())
			{
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(VALUE." + i.memberVariableName + "), 1L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "To(VALUE." + i.memberVariableName + "), 1L);", 2);
			}
		}

		writeLine();
		writeLine("// Negative tests", 2);
		for (var i : columnInfo)
		{
			if (i.isCharacter)
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "(\"invalid\"), 0L);", 2);
			else if (i.isBoolean)
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "(!VALUE." + i.memberVariableName + "), 0L);", 2);
			else
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "(VALUE." + i.memberVariableName + " + 1000" + ((10 < i.size) ? "L" : "") + "), 0L);", 2);
				
			if (i.isTime)
			{
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAhead), 0L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "To(hourAgo), 0L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAhead)." + i.withMethodName + "To(hourAgo), 0L);", 2);
			}
			else if (i.isRange())
			{
				var l = ((10 < i.size) ? "L" : "");
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(VALUE." + i.memberVariableName + " + 1" + l + "), 0L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "To(VALUE." + i.memberVariableName + " - 1" + l + "), 0L);", 2);
			}
		}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the search endpoint and verifies the counts and records. */", 1);
		writeLine("private void search(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("var response = request(\"search\")", 2);
		writeLine(".post(Entity.entity(filter, UTF8MediaType.APPLICATION_JSON_TYPE));", 3);
		writeLine("var assertId = \"SEARCH \" + filter + \": \";", 2);
		writeLine("Assertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), assertId + \"Status\");", 2);
		writeLine();
		writeLine("var results = response.readEntity(TYPE_QUERY_RESULTS);", 2);
		writeLine("Assertions.assertNotNull(results, assertId + \"Exists\");", 2);
		writeLine("Assertions.assertEquals(expectedTotal, results.getTotal(), assertId + \"Check total\");", 2);
		writeLine("if (0L == expectedTotal)", 2);
		writeLine("Assertions.assertNull(results.getRecords(), assertId + \"Records exist\");", 3);
		writeLine("else", 2);
		writeLine("{", 2);
		writeLine("Assertions.assertNotNull(results.getRecords(), assertId + \"Records exist\");", 3);
		writeLine("int total = (int) expectedTotal;", 3);
		writeLine("if (total > results.getPageSize())", 3);
		writeLine("{", 3);
		writeLine("if (results.getPage() == results.getPages())", 4);
		writeLine("total%= results.getPageSize();", 5);
		writeLine("else", 4);
		writeLine("total = results.getPageSize();", 5);
		writeLine("}", 3);
		writeLine("Assertions.assertEquals(total, results.getRecords().size(), assertId + \"Check records.size\");", 3);
		writeLine("}", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("@Test", 1);
		writeLine("public void testRemove()", 1);
		writeLine("{", 1);
		writeLine("remove(VALUE.id + " + invalidId + ", false);", 2);
		writeLine("remove(VALUE.id, true);", 2);
		writeLine("remove(VALUE.id, false);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - call the DELETE endpoint. */", 1);
		writeLine("private void remove(final " + pkTypeName + " id, boolean success)", 1);
		writeLine("{", 1);
		writeLine("var response = request(id" + to_s + ").delete();", 2);
		writeLine("var assertId = \"DELETE (\" + id + \", \" + success + \"): \";", 2);
		writeLine("Assertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), assertId + \"Status\");", 2);
		writeLine();
		writeLine("var results = response.readEntity(OperationResponse.class);", 2);
		writeLine("Assertions.assertNotNull(results, assertId + \"Exists\");", 2);
		writeLine("Assertions.assertEquals(success, results.operation, assertId + \"Check value\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void testRemove_get()", 1);
		writeLine("{", 1);
		writeLine("Assertions.assertEquals(HTTP_STATUS_VALIDATION_EXCEPTION, get(VALUE.id).getStatus(), \"Status\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void testRemove_search()", 1);
		writeLine("{", 1);
		writeLine("count(new " + filterName + "().withId(VALUE.id), 0L);", 2);
		writeLine("// TODO: provide secondary test count.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates the base WebTarget. */", 1);
		writeLine("private WebTarget target() { return RULE.client().target(TARGET); }", 1);
		writeLine();
		writeLine("/** Helper method - creates the request from the WebTarget. */", 1);
		writeLine("private Invocation.Builder request() { return request(target()); }", 1);
		writeLine("private Invocation.Builder request(final String path) { return request(target().path(path)); }", 1);
		writeLine("private Invocation.Builder request(final WebTarget target) { return target.request(UTF8MediaType.APPLICATION_JSON_TYPE); }", 1);

		writeLine();
		writeLine("/** Helper method - calls the DAO count call and compares the expected total value.", 1);
		writeLine(" *", 1);
		writeLine(" * @param filter", 1);
		writeLine(" * @param expectedTotal", 1);
		writeLine(" */", 1);
		writeLine("private void count(final " + filterName + " filter, long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("Assertions.assertEquals(expectedTotal, dao.count(filter), \"COUNT \" + filter + \": Check total\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(final " + valueName + " expected, final " + valueName + " value)", 1);
		writeLine("{", 1);
		writeLine("var assertId = \"ID (\" + expected.id + \"): \";", 2);
		for (ColumnInfo i : columnInfo)
		{
			if (i.isTime)
			{
				writeLine("if (null == expected." + i.memberVariableName + ")", 2);
				writeLine("Assertions.assertNull(value." + i.memberVariableName + ", assertId + \"Check " + i.memberVariableName + "\");", 3);
				writeLine("else", 2);
				writeLine("assertThat(value." + i.memberVariableName + ").as(assertId + \"Check " + i.memberVariableName + "\").isCloseTo(expected." + i.memberVariableName + ", 500L);", 3);
			}
			else
			{
				writeLine("Assertions.assertEquals(expected." + i.memberVariableName + ", value." + i.memberVariableName + ", assertId + \"Check " + i.memberVariableName + "\");", 2);
				if (i.isImportedKey)
					writeLine("Assertions.assertEquals(expected." + i.importedKeyMemberName + "Name, value." + i.importedKeyMemberName + "Name, assertId + \"Check " + i.importedKeyMemberName + "Name\");", 2);
			}
		}
		writeLine("}", 1);
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/** Command line entry point.
		@param args1 Output directory.
		@param args2 URL to the data source.
		@param args3 data source login name.
		@param args4 data source password.
		@param args5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param args6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param args7 package name of the entity bean value object.
		@param args8 application version number
		@param args9 table name filter
	*/
	public static void main(final String... args)
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			var dir = extractOutputDirectory(args, 0);
			var author = extractAuthor(args, 5);
			var packageName = extractArgument(args, 6, null);
			var version = extractArgument(args, 7, null);

			// Create and load the tables object.
			var tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityJerseyResourceTest(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			var message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityJerseyResourceTest.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Version]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
