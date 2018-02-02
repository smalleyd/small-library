package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

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
		final String packageName = getPackageName();
		final String domainPackageName = getDomainPackageName();
		final String basePackageName = getBasePackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		final String name = getObjectName();
		writeLine("import java.util.*;");
		writeLine();
		writeLine("import javax.ws.rs.client.*;");
		writeLine("import javax.ws.rs.core.GenericType;");
		writeLine("import javax.ws.rs.core.Response;");
		writeLine();
		writeLine("import org.hibernate.SessionFactory;");
		writeLine("import org.junit.*;");
		writeLine("import org.junit.runners.MethodSorters;");
		writeLine();
		writeLine("import io.dropwizard.testing.junit.ResourceTestRule;");
		writeLine();
		writeLine("import " + domainPackageName + ".junit.hibernate.*;");
		writeLine("import " + domainPackageName + ".common.dao.QueryResults;");
		writeLine("import " + domainPackageName + ".common.error.ValidationExceptionMapper;");
		writeLine("import " + domainPackageName + ".common.model.Model;");
		writeLine("import " + domainPackageName + ".common.jersey.JerseyUtils;");
		writeLine("import " + domainPackageName + ".common.value.NameValue;");
		writeLine("import " + domainPackageName + ".testing.TestingUtils;");
		writeLine("import " + basePackageName + "." + getAppName() + "Application;");
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
		final String name = getClassName();
		final String objectName = getObjectName();
		final String daoName = EntityBeanDAO.getClassName(objectName);
		final String mapping = fromObjectNameToMemberName(objectName) + "s";
		final String filterName = EntityBeanFilter.getClassName(objectName);
		final String valueName = EntityBeanValueObject.getClassName(objectName);
		final String resourceName = EntityJerseyResource.getClassName(objectName);

		writeLine();
		writeLine("@FixMethodOrder(MethodSorters.NAME_ASCENDING)	// Ensure that the methods are executed in order listed.");
		writeLine("public class " + name);
		writeLine("{");
		writeLine("@ClassRule", 1);
		writeLine("public static final HibernateRule DAO_RULE = new HibernateRule(" + getAppName() + "Application.ENTITIES);", 1);
		writeLine();
		writeLine("@Rule", 1);
		writeLine("public final HibernateTransactionRule transRule = new HibernateTransactionRule(DAO_RULE);", 1);
		writeLine();
		writeLine("private static " + daoName + " dao = null;", 1);
		writeLine("private static " + valueName + " VALUE = null;", 1);
		writeLine();
		writeLine("@Rule", 1);
		writeLine("public final ResourceTestRule RULE = ResourceTestRule.builder()", 1);
		writeLine(".addResource(new ValidationExceptionMapper())", 2);
		writeLine(".addResource(new " + resourceName + "(dao)).build();", 2);
		writeLine();
		writeLine("/** Primary URI to test. */", 1);
		writeLine("private static final String TARGET = \"/" + mapping + "\";", 1);
		writeLine();
		writeLine("/** Generic types for reading values from responses. */", 1);
		writeLine("private static final GenericType<Model<Boolean>> TYPE_BOOLEAN = new GenericType<Model<Boolean>>() {};", 1);
		writeLine("private static final GenericType<List<NameValue>> TYPE_LIST_NAME_VALUE = new GenericType<List<NameValue>>() {};", 1);
		writeLine("private static final GenericType<QueryResults<" + valueName + ", " + filterName + ">> TYPE_QUERY_RESULTS =", 1);
		writeLine("new GenericType<QueryResults<" + valueName + ", " + filterName + ">>() {};", 2);
		writeLine();
		writeLine("@BeforeClass", 1);
		writeLine("public static void up()", 1);
		writeLine("{", 1);
		writeLine("final SessionFactory factory = DAO_RULE.getSessionFactory();", 2);
		writeLine("dao = new " + daoName + "(factory);", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		final String name = getObjectName();
		final String pkTypeName = getPkJavaType();
		final String filterName = EntityBeanFilter.getClassName(name);
		final String valueName = EntityBeanValueObject.getClassName(name);
		final String to_s = "String".equals(pkTypeName) ? "" : ".toString()";

		// For testing non-existence, use a value to add to the primary key.
		String invalidId = "\"INVALID\"";
		for (final ColumnInfo i : columnInfo)
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
		writeLine("final Response response = request()", 2);
		writeLine(".post(Entity.entity(VALUE = new " + valueName + "(), JerseyUtils.APPLICATION_JSON_TYPE));", 3);
		writeLine();
		writeLine("Assert.assertEquals(\"Status\", TestingUtils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("final " + valueName + " value = response.readEntity(" + valueName + ".class);", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void find()", 1);
		writeLine("{", 1);
		writeLine("final Response response = target().queryParam(\"name\", \"\")", 2);
		writeLine(".request(JerseyUtils.APPLICATION_JSON_TYPE)", 3);
		writeLine(".get();", 3);
		writeLine();
		writeLine("Assert.assertEquals(\"Status\", TestingUtils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("final List<NameValue> values = response.readEntity(TYPE_LIST_NAME_VALUE);", 2);
		writeLine("Assert.assertNotNull(\"Exists\", values);", 2);
		writeLine();
		writeLine("// TODO: do other checks.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void get()", 1);
		writeLine("{", 1);
		writeLine("final Response response = get(VALUE.id);", 2);
		writeLine();
		writeLine("Assert.assertEquals(\"Status\", TestingUtils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("final " + valueName + " value = response.readEntity(" + valueName + ".class);", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
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
		writeLine("Assert.assertEquals(\"Status\", TestingUtils.HTTP_STATUS_VALIDATION_EXCEPTION, get(VALUE.id + " + invalidId + ").getStatus());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify()", 1);
		writeLine("{", 1);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 1L);", 2);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 0L);", 2);
		writeLine();
		writeLine("// TODO: provide a change to the VALUE.", 2);
		writeLine("final Response response = request().put(Entity.entity(VALUE, JerseyUtils.APPLICATION_JSON_TYPE));", 2);
		writeLine();
		writeLine("Assert.assertEquals(\"Status\", TestingUtils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("final " + valueName + " value = response.readEntity(" + valueName + ".class);", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine();
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 0L);", 2);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 1L);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify_get()", 1);
		writeLine("{", 1);
		writeLine("final " + valueName + " value = get(VALUE.id).readEntity(" + valueName + ".class);", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
		writeLine("// TODO: check the changed property.", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void search()", 1);
		writeLine("{", 1);
		for (ColumnInfo i : columnInfo)
		{
			writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "(VALUE." + i.memberVariableName + "), 1L);", 2);
			if (i.isRange())
			{
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(VALUE." + i.memberVariableName + "), 1L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "To(VALUE." + i.memberVariableName + "), 1L);", 2);
			}
		}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the search endpoint and verifies the counts and records. */", 1);
		writeLine("private void search(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("final Response response = request(\"search\")", 2);
		writeLine(".post(Entity.entity(filter, JerseyUtils.APPLICATION_JSON_TYPE));", 3);
		writeLine();
		writeLine("final String assertId = \"SEARCH \" + filter + \": \";", 2);
		writeLine("Assert.assertEquals(assertId + \"Status\", TestingUtils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("final QueryResults<" + valueName + ", " + filterName + "> results = response.readEntity(TYPE_QUERY_RESULTS);", 2);
		writeLine("Assert.assertNotNull(assertId + \"Exists\", results);", 2);
		writeLine("Assert.assertEquals(assertId + \"Check total\", expectedTotal, results.getTotal());", 2);
		writeLine("if (0L == expectedTotal)", 2);
		writeLine("Assert.assertNull(assertId + \"Records exist\", results.getRecords());", 3);
		writeLine("else", 2);
		writeLine("{", 2);
		writeLine("Assert.assertNotNull(assertId + \"Records exist\", results.getRecords());", 3);
		writeLine("int total = (int) expectedTotal;", 3);
		writeLine("if (total > results.getPageSize())", 3);
		writeLine("{", 3);
		writeLine("if (results.getPage() == results.getPages())", 4);
		writeLine("total%= results.getPageSize();", 5);
		writeLine("else", 4);
		writeLine("total = results.getPageSize();", 5);
		writeLine("}", 3);
		writeLine("Assert.assertEquals(assertId + \"Check records.size\", total, results.getRecords().size());", 3);
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
		writeLine("final Response response = request(id" + to_s + ").delete();", 2);
		writeLine();
		writeLine("final String assertId = \"DELETE (\" + id + \", \" + success + \"): \";", 2);
		writeLine("Assert.assertEquals(assertId + \"Status\", TestingUtils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("final Model<Boolean> results = response.readEntity(TYPE_BOOLEAN);", 2);
		writeLine("Assert.assertNotNull(assertId + \"Exists\", results);", 2);
		writeLine("Assert.assertEquals(assertId + \"Check value\", success, results.getValue());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void testRemove_get()", 1);
		writeLine("{", 1);
		writeLine("Assert.assertEquals(\"Status\", TestingUtils.HTTP_STATUS_VALIDATION_EXCEPTION, get(VALUE.id).getStatus());", 2);
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
		writeLine("private Invocation.Builder request() { return target().request(JerseyUtils.APPLICATION_JSON_TYPE); }", 1);
		writeLine();
		writeLine("/** Helper method - creates the request from the WebTarget. */", 1);
		writeLine("private Invocation.Builder request(final String path) { return target().path(path).request(JerseyUtils.APPLICATION_JSON_TYPE); }", 1);

		writeLine();
		writeLine("/** Helper method - calls the DAO count call and compares the expected total value.", 1);
		writeLine(" *", 1);
		writeLine(" * @param filter", 1);
		writeLine(" * @param expectedTotal", 1);
		writeLine(" */", 1);
		writeLine("private void count(final " + filterName + " filter, long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("Assert.assertEquals(\"COUNT \" + filter + \": Check total\", expectedTotal, dao.count(filter));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(final " + valueName + " expected, final " + valueName + " value)", 1);
		writeLine("{", 1);
		writeLine("final String assertId = \"ID (\" + expected.id + \"): \";", 2);
		for (ColumnInfo i : columnInfo)
		{
			writeLine("Assert.assertEquals(assertId + \"Check " + i.memberVariableName + "\", expected." + i.memberVariableName + ", value." + i.memberVariableName + ");", 2);
			if (i.isImportedKey)
				writeLine("Assert.assertEquals(assertId + \"Check " + i.importedKeyMemberName + "Name\", expected." + i.importedKeyMemberName + "Name, value." + i.importedKeyMemberName + "Name);", 2);
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
			final File dir = extractOutputDirectory(args, 0);
			final String author = extractAuthor(args, 5);
			final String packageName = extractArgument(args, 6, null);
			final String version = extractArgument(args, 7, null);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityJerseyResourceTest(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

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
