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
		writeLine("import java.util.stream.Collectors;");
		writeLine();
		writeLine("import org.hibernate.SessionFactory;");
		writeLine("import org.junit.*;");
		writeLine("import org.junit.runners.MethodSorters;");
		writeLine();
		writeLine("import io.dropwizard.testing.junit.ResourceTestRule;");
		writeLine();
		writeLine("import com.jibe.junit.hibernate.*;");
		writeLine("import com.jibe.dwservice.errors.ValidationException;");
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
		String daoName = EntityBeanDAO.getClassName(getObjectName());
		String resourceName = EntityJerseyResource.getClassName(getObjectName());

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
		writeLine("private final ResourceTestRule RULE = ResourceTestRule.builder().addResource(new " + resourceName + "(dao)).build();", 1);
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
		writeLine("@Test", 1);
		writeLine("public void search()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("@Test", 1);
		writeLine("public void testRemove()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void update()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide implementation.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied entity record. */", 1);
		writeLine("private void check(" + valueName + " expected, " + name + " record)", 1);
		writeLine("{", 1);
		for (ColumnInfo i : m_ColumnInfo)
			writeLine("Assert.assertEquals(\"Check " + i.memberVariableName + "\", expected." + i.accessorMethodName + "(), record." + i.accessorMethodName + "());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(" + valueName + " expected, " + name + " value)", 1);
		writeLine("{", 1);
		for (ColumnInfo i : m_ColumnInfo)
			writeLine("Assert.assertEquals(\"Check " + i.memberVariableName + "\", expected." + i.accessorMethodName + "(), value." + i.accessorMethodName + "());", 2);
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