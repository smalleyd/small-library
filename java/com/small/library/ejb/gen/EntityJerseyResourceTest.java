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
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "ResourceTest";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityJerseyResourceTest() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityJerseyResourceTest(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version.
	*/
	public EntityJerseyResourceTest(PrintWriter writer,
		String author, Tables.Record table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMethods();

		writeFooter();
	}

	/******************************************************************************
	*
	*	Required methods: BaseTable
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Tables.Record pTable)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(pTable.getName())) + ".java";
	}

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String packageName = getPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		String name = getObjectName();
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
		writeLine("import com.jibe.junit.hibernate.*;");
		writeLine("import com.jibe.dwservice.errors.ValidationExceptionMapper;");
		writeLine("import com.jibe.question.entity." + name + ";");
		writeLine("import com.jibe.question.model." + EntityBeanFilter.getClassName(name) + ";");
		writeLine("import com.jibe.question.model.QueryResults;");
		writeLine("import com.jibe.question.value." + EntityBeanValueObject.getClassName(name) + ";");
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
		String name = getClassName();
		String objectName = getObjectName();
		String mapping = fromObjectNameToMemberName(name) + "s";
		String daoName = EntityBeanDAO.getClassName(objectName);
		String filterName = EntityBeanFilter.getClassName(objectName);
		String valueName = EntityBeanValueObject.getClassName(objectName);
		String resourceName = EntityJerseyResource.getClassName(objectName);

		writeLine();
		writeLine("@FixMethodOrder(MethodSorters.NAME_ASCENDING)	// Ensure that the methods are executed in order listed.");
		writeLine("public class " + name);
		writeLine("{");
		writeLine("@ClassRule", 1);
		writeLine("public static final HibernateRule DAO_RULE = new HibernateRule(QuestionServiceApplication.ENTITIES);", 1);
		writeLine();
		writeLine("@Rule", 1);
		writeLine("public final HibernateTransactionRule transRule = new HibernateTransactionRule(DAO_RULE);", 1);
		writeLine();
		writeLine("private static " + daoName + " dao = null;", 1);
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
		writeLine("private static final GenericType<Model<Boolean>> BOOLEAN_TYPE = new GenericType<Model<Boolean>>() {};", 1);
		writeLine("private static final GenericType<List<NameValue>> LIST_NAME_VALUE_TYPE = new GenericType<List<NameValue>>() {};", 1);
		writeLine("private static final GenericType<QueryResults<" + valueName + ", " + filterName + ">> QUERY_RESULTS_TYPE =", 1);
		writeLine("new GenericType<QueryResults<" + valueName + ", " + filterName + ">>() {};", 2);
		writeLine();
		writeLine("@BeforeClass", 1);
		writeLine("public static void up()", 1);
		writeLine("{", 1);
		writeLine("SessionFactory factory = DAO_RULE.getSessionFactory();", 2);
		writeLine("dao = new " + daoName + "(factory);", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		String name = getObjectName();
		String pkTypeName = getPkJavaType();
		String filterName = EntityBeanFilter.getClassName(name);
		String valueName = EntityBeanValueObject.getClassName(name);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void add()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void find()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void get()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the GET endpoint. */", 1);
		writeLine("private void get(" + pkTypeName + " id)", 1);
		writeLine("{", 1);
		writeLine("return request(id).get();", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void getWithException()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify_get()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void search()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the search endpoint and verifies the counts and records. */", 1);
		writeLine("private void search(" + filterName + " filter, long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("Response response = request(\"search\")", 2);
		writeLine(".post(Entity.entity(filter, JerseyUtils.APPLICATION_JSON_TYPE));", 3);
		writeLine();
		writeLine("String assertId = \"SEARCH \" + filter + \": \";", 2);
		writeLine("Assert.assertEquals(assertId + \"Status\", Utils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("QueryResults<" + valueName + ", " + filterName + "> results = response.readEntity(QUERY_RESULTS_TYPE);", 2);
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
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - call the DELETE endpoint. */", 1);
		writeLine("private void remove(" + pkTypeName + " id, boolean success)", 1);
		writeLine("{", 1);
		writeLine("Response response = request(id).delete();", 2);
		writeLine();
		writeLine("String assertId = \"DELETE (\" + id + \", \" + success + \"): \";", 2);
		writeLine("Assert.assertEquals(assertId + \"Status\", Utils.HTTP_STATUS_OK, response.getStatus());", 2);
		writeLine("Model<Boolean> results = response.readEntity(BOOLEAN_TYPE);", 2);
		writeLine("Assert.assertNotNull(assertId + \"Exists\", results);", 2);
		writeLine("Assert.assertEquals(assertId + \"Check value\", success, results.getValue());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void testRemove_get()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void testRemove_search()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates the base WebTarget. */", 1);
		writeLine("private WebTarget target() { return RULE.client().target(TARGET); }", 1);
		writeLine();
		writeLine("/** Helper method - creates the request from the WebTarget. */", 1);
		writeLine("private Invocation.Builder request() { return target().request(JerseyUtils.APPLICATION_JSON_TYPE); }", 1);
		writeLine();
		writeLine("/** Helper method - creates the request from the WebTarget. */", 1);
		writeLine("private Invocation.Builder request(String path) { return target().path(path).request(JerseyUtils.APPLICATION_JSON_TYPE); }", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(" + valueName + " expected, " + valueName + " value)", 1);
		writeLine("{", 1);
		writeLine("String assertId = \"ID (\" + expected.getId() + \"): \";", 2);
		for (ColumnInfo i : m_ColumnInfo)
			writeLine("Assert.assertEquals(assertId + \"Check " + i.memberVariableName + "\", expected." + i.accessorMethodName + "(), value." + i.accessorMethodName + "());", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

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
		@param args8 application version.
	*/
	public static void main(String args[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(args, 0);
			String strAuthor = extractAuthor(args, 5);
			String strPackageName = extractArgument(args, 6, null);
			String version = extractArgument(args, 7, null);

			// Create and load the tables object.
			Tables pTables = extractTables(args, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityJerseyResourceTest pGenerator =
				new EntityJerseyResourceTest((PrintWriter) null, strAuthor,
				(Tables.Record) null, strPackageName, version);

			// Call the BaseTable method to handle the outputing.
			generateTableResources(pGenerator, pTables, fileOutputDir);
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
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

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
