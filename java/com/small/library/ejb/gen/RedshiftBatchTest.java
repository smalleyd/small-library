package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

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
	public static final String CLASS_NAME_SUFFIX = "BatchTest";

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
	public RedshiftBatchTest(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public RedshiftBatchTest(final String author, final String packageName, final String version)
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
	public RedshiftBatchTest(PrintWriter writer,
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
	public String getOutputFileName(final Table table)
	{
		// Name should NOT have a suffix.
		return getClassName() + ".java";
	}

	/** Helper method - gets the value object name. */
	public String valueObjectName(final String name)
	{
		return EntityBeanValueObject.getClassName(name);
	}

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
		writeLine("import java.util.*;");
		writeLine();
		writeLine("import org.junit.*;");
		writeLine("import org.junit.runners.MethodSorters;");
		writeLine("import org.skife.jdbi.v2.Handle;");
		writeLine("import org.skife.jdbi.v2.DBI;");
		writeLine();
		writeLine("import " + domainPackageName + ".junit.dropwizard.LifecycleRule;");
		writeLine("import " + domainPackageName + ".dwservice.aws.AWSManager;");
		writeLine("import " + basePackageName + "." + getAppName() + "BatchConfig;");
		writeLine("import " + basePackageName + "." + getAppName() + "BatchConfigTest;");
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
		String tableName = getTable().name;

		writeLine();
		writeLine("@FixMethodOrder(MethodSorters.NAME_ASCENDING)	// Ensure that the methods are executed in order listed.");
		writeLine("public class " + getClassName());
		writeLine("{");
		writeLine("private static final " + columnInfo[0].javaType + " ID = 1L;", 1);
		writeLine("private static " + getAppName() + "BatchConfig conf;", 1);
		writeLine("private static " + batchName + " batch;", 1);
		writeLine("private static AWSManager aws;", 1);
		writeLine("private static DBI dest;", 1);
		writeLine("private static String insertQueueUrl;", 1);
		writeLine("private static String updateQueueUrl;", 1);
		writeLine();
		writeLine("private static " + valueName + " INSERT = null;", 1);
		writeLine("private static " + valueName + " UPDATE = null;", 1);
		writeLine();
		writeLine("@ClassRule", 1);
		writeLine("public static final LifecycleRule RULE = new LifecycleRule();", 1);
		writeLine();
		writeLine("@BeforeClass", 1);
		writeLine("public static void up() throws Exception", 1);
		writeLine("{", 1);
		writeLine("conf = AnalyticsBatchConfigTest.load(\"test.json\");", 2);
		writeLine();
		writeLine("RULE.manage(aws = conf.awsManager());", 2);
		writeLine("conf.destDbi(dest = RULE.manageForDBI(conf.dest, \"dest\"));", 2);
		writeLine("batch = new " + batchName + "(conf);", 2);
		writeLine("insertQueueUrl = conf.aws.insertQueueUrl(batch.getEntityName());", 2);
		writeLine("updateQueueUrl = conf.aws.updateQueueUrl(batch.getEntityName());", 2);
		writeLine();
		writeLine("Assert.assertEquals(\"Check tableName\", \"" + tableName + "\", batch.getTableName());", 2);
		writeLine();
		writeLine("/* TODO: placeholder for the INSERT object.", 2);
		write("INSERT = new " + valueName + "(ID", 2);
		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(",");
			write(columnInfo[i].columnName, 3);
		}
		writeLine(");");
		writeLine("*/", 2);
		writeLine();
		writeLine("/* TODO: placeholder for the UPDATE object.", 2);
		write("UPDATE = new " + valueName + "(ID", 2);
		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(",");
			write(columnInfo[i].columnName, 3);
		}
		writeLine(");");
		writeLine("*/", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@AfterClass", 1);
		writeLine("public static void down() throws Exception", 1);
		writeLine("{", 1);
		writeLine("try (final Handle h = dest.open())", 2);
		writeLine("{", 2);
		writeLine("h.update(\"TRUNCATE TABLE \" + batch.getEntityName());", 3);
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
		writeLine("// TODO: uncomment if de-dupe is enabled. aws.sendObject(updateQueueUrl, UPDATE.withUpdatedAt(new Date(UPDATE.updatedAt.getTime() + 1000L)));	// Test de-dupe.", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void process()", 1);
		writeLine("{", 1);
		writeLine("batch.run();", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void reindex()", 1);
		writeLine("{", 1);
		writeLine("batch.maintain();", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Test", 1);
		writeLine("public void verify() throws Exception", 1);
		writeLine("{", 1);
		writeLine("try (final Handle h = dest.open())", 2);
		writeLine("{", 2);
		writeLine("final List<Map<String, Object>> records = h.select(\"SELECT * FROM " + getTable().name + " WHERE id = ?\", ID);", 3);
		writeLine("Assert.assertEquals(\"Check size\", 1, records.size());", 3);
		writeLine("final Map<String, Object> rs = records.get(0);", 3);
		writeLine();
		write("check(UPDATE, new " + valueName + "((" + columnInfo[0].javaType + ") rs.get(\"" + columnInfo[0].columnName + "\")", 3);
		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(",");
			ColumnInfo col = columnInfo[i];
			write("(" + col.javaType + ") rs.get(\"" + col.columnName + "\")", 4);
		}
		writeLine("));");
		writeLine("}", 2);
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
				writeLine("Assert.assertEquals(assertId + \"Check " + i.importedKeyMemberName + " name\", expected.get" + i.importedKeyName + "Name(), value.get" + i.importedKeyName + "Name());", 2);
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
			generateTableResources(new RedshiftBatchTest(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
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

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
