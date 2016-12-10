package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates Dropwizard.io the unit test skeletons for the Redshift batch copy objects.
*
*	@author David Small
*	@version 2.0
*	@since 7/13/2016
*
***************************************************************************************/

public class RedshiftBatchTest extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "BatchTest";

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
	public RedshiftBatchTest() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public RedshiftBatchTest(PrintWriter writer,
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
	public RedshiftBatchTest(PrintWriter writer,
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
		return getClassName() + ".java";
	}

	/** Helper method - gets the value object name. */
	public String valueObjectName(String name)
	{
		return EntityBeanValueObject.getClassName(name);
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
		String domainPackageName = getDomainPackageName();
		String basePackageName = getBasePackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		String name = getObjectName();
		writeLine("import java.io.File;");
		writeLine("import java.sql.*;");
		writeLine();
		writeLine("import org.junit.*;");
		writeLine("import org.junit.runners.MethodSorters;");
		writeLine();
		writeLine("import io.dropwizard.jackson.Jackson;");
		writeLine();
		writeLine("import " + domainPackageName + ".dwservice.aws.AWSManager;");
		writeLine("import " + domainPackageName + ".dwtesting.TestingUtils;");
		writeLine("import " + basePackageName + "." + getAppName() + "BatchConfig;");
		writeLine("import " + basePackageName + ".value." + valueObjectName(name) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tFunctional test class that verifies the " + name + "Batch class.");
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
		String name = getObjectName();
		String batchName = RedshiftBatch.getClassName(name);
		String valueName = valueObjectName(name);
		String tableName = getTable().getName();

		writeLine();
		writeLine("@FixMethodOrder(MethodSorters.NAME_ASCENDING)	// Ensure that the methods are executed in order listed.");
		writeLine("public class " + getClassName());
		writeLine("{");
		writeLine("private static final " + m_ColumnInfo[0].javaType + " ID = 1L;", 1);
		writeLine("private static " + getAppName() + "BatchConfig conf;", 1);
		writeLine("private static " + batchName + " batch;", 1);
		writeLine("private static AWSManager aws;", 1);
		writeLine("private static String insertQueueUrl;", 1);
		writeLine("private static String updateQueueUrl;", 1);
		writeLine();
		writeLine("private static " + valueName + " INSERT = null;", 1);
		writeLine("private static " + valueName + " UPDATE = null;", 1);
		writeLine();
		writeLine("@BeforeClass", 1);
		writeLine("public static void up() throws Exception", 1);
		writeLine("{", 1);
		writeLine("conf = Jackson.newObjectMapper().readValue(new File(\"conf/test.json\"), " + getAppName() + "BatchConfig.class);", 2);
		writeLine("conf.destPool(" + getClassName() + ".class.getName()).start();", 2);
		writeLine();
		writeLine("batch = new " + batchName + "();", 2);
		writeLine("aws = conf.awsManager();", 2);
		writeLine("aws.start();", 2);
		writeLine("insertQueueUrl = conf.getAws().insertQueueUrl(batch.getEntityName());", 2);
		writeLine("updateQueueUrl = conf.getAws().updateQueueUrl(batch.getEntityName());", 2);
		writeLine();
		writeLine("Assert.assertEquals(\"Check tableName\", \"" + tableName + "\", batch.getTableName());", 2);
		writeLine();
		writeLine("/* TODO: placeholder for the INSERT object.", 2);
		write("INSERT = new " + valueName + "(ID", 2);
		for (int i = 1; i < m_ColumnInfo.length; i++)
		{
			writeLine(",");
			write(m_ColumnInfo[i].columnName, 3);
		}
		writeLine(");");
		writeLine("*/", 2);
		writeLine();
		writeLine("/* TODO: placeholder for the UPDATE object.", 2);
		write("UPDATE = new " + valueName + "(ID", 2);
		for (int i = 1; i < m_ColumnInfo.length; i++)
		{
			writeLine(",");
			write(m_ColumnInfo[i].columnName, 3);
		}
		writeLine(");");
		writeLine("*/", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@AfterClass", 1);
		writeLine("public static void down() throws Exception", 1);
		writeLine("{", 1);
		writeLine("aws.stop();", 2);
		writeLine("conf.destPool().stop();", 2);
		writeLine();
		writeLine("try (Connection conn = conf.destPool().getConnection())", 2);
		writeLine("{", 2);
		writeLine("try (Statement stmt = conn.createStatement())", 3);
		writeLine("{", 3);
		writeLine("stmt.executeUpdate(\"TRUNCATE TABLE \" + batch.getEntityName());", 4);
		writeLine("}", 3);
		writeLine("}", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		String name = getObjectName();
		String valueName = EntityBeanValueObject.getClassName(name);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void addInsert()", 1);
		writeLine("{", 1);
		writeLine("aws.sendObject(insertQueueUrl, INSERT);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void addUpdate()", 1);
		writeLine("{", 1);
		writeLine("aws.sendObject(updateQueueUrl, UPDATE);", 2);
		writeLine("// TODO: uncomment if de-dupe is enabled. aws.sendObject(updateQueueUrl, UPDATE.withUpdatedAt(new java.util.Date(UPDATE.getUpdatedAt().getTime() + 1000L)));	// Test de-dupe.", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void process()", 1);
		writeLine("{", 1);
		writeLine("batch.run(conf);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void reindex()", 1);
		writeLine("{", 1);
		writeLine("batch.maintain(conf);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void verify() throws Exception", 1);
		writeLine("{", 1);
		writeLine("try (Connection conn = conf.destPool().getConnection())", 2);
		writeLine("{", 2);
		writeLine("try (PreparedStatement stmt = conn.prepareStatement(\"SELECT * FROM " + getTable().getName() + " WHERE id = ?\"))", 3);
		writeLine("{", 3);
		writeLine("stmt.setLong(1, ID);", 4);
		writeLine("ResultSet rs = stmt.executeQuery();", 4);
		writeLine("Assert.assertTrue(\"Exists\", rs.next());", 4);
		writeLine();
		write("check(UPDATE, new " + valueName + "(rs.get" + m_ColumnInfo[0].jdbcMethodSuffix + "(\"" + m_ColumnInfo[0].columnName + "\")", 4);
		for (int i = 1; i < m_ColumnInfo.length; i++)
		{
			writeLine(",");
			ColumnInfo col = m_ColumnInfo[i];
			if (RedshiftLoader.PRIMITIVES.contains(col.javaType) && col.isNullable)
				write("(" + col.javaType + ") rs.getObject(\"" + col.columnName + "\")", 5);
			else
				write("rs.get" + col.jdbcMethodSuffix + "(\"" + col.columnName + "\")", 5);
		}
		writeLine("));");
		writeLine("}", 3);
		writeLine("}", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(" + valueName + " expected, " + valueName + " value)", 1);
		writeLine("{", 1);
		writeLine("String assertId = \"ID (\" + expected.getId() + \"): \";", 2);
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine("Assert.assertEquals(assertId + \"Check " + i.memberVariableName + "\", expected." + i.accessorMethodName + "(), value." + i.accessorMethodName + "());", 2);
			if (i.isImportedKey)
				writeLine("Assert.assertEquals(assertId + \"Check " + i.importedKeyMemberName + " name\", expected.get" + i.importedKeyName + "Name(), value.get" + i.importedKeyName + "Name());", 2);
		}
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
		@param args8 application version number
		@param args9 table name filter
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
			RedshiftBatchTest pGenerator =
				new RedshiftBatchTest((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + RedshiftBatchTest.class.getName() + " Output directory");
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
