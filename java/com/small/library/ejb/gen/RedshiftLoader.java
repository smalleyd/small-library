package com.small.library.ejb.gen;

import java.io.*;
import java.util.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class to load Redshift another JDBC compliant data source.
*
*	@author David Small
*	@version 2.0
*	@since 7/13/2016
*
***************************************************************************************/

public class RedshiftLoader extends EntityBeanBase
{
	public static final Set<String> PRIMITIVES = new HashSet<>(Arrays.asList("Boolean", "Integer", "Long"));

	public static final String CLASS_NAME_SUFFIX = "Loader";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(final String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public RedshiftLoader(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public RedshiftLoader(PrintWriter writer,
		String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public RedshiftLoader(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public RedshiftLoader(PrintWriter writer,
		String author, Table table, String packageName,
		String version)
	{
		super(writer, author, table, packageName, version);
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeConstants();
		writeConstructors();
		writeAccessorMethods();
		writeOutputMethod();

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
		final String packageName = getPackageName();
		final String basePackageName = getBasePackageName();
		final String domainPackageName = getDomainPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		writeLine("import java.io.*;");
		writeLine("import java.sql.*;");
		writeLine();
		writeLine("import com.amazonaws.services.dynamodbv2.document.Item;");
		writeLine("import " + domainPackageName + ".dwservice.io.CSVWriter;");
		writeLine("import " + basePackageName + ".AnalyticsApplication;");
		writeLine("import " + basePackageName + ".AnalyticsConfiguration;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tClass that loads data from the " + getTable().name.toUpperCase() + " table in another JDBC compliant");
		writeLine("*\tdatabase to a similar table in Redshift.");
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
		writeLine();
		writeLine("public class " + getClassName() + " extends AbstractLoader");
		writeLine("{");
	}

	/** Output method - writes the SQS and SQL constants. */
	private void writeConstants() throws IOException
	{
		final String name = getObjectName();
		final String tableName = getTable().name;

		writeLine("/** Name of process. */", 1);
		writeLine("public static final String NAME = \"" + name + "\";", 1);
		writeLine("private static final String TABLE_NAME = \"" + tableName + "\";", 1);
		writeLine();
		writeLine("/** Size of bundle to load into S3 & subsequently Redshift. */", 1);
		writeLine("public static final int BATCH_SIZE = 10000;", 1);
		writeLine();
		writeLine("/** Creates the SELECT SQL for the specific " + tableName.toUpperCase() + " table. */", 1);
		write("public static final String SELECT = \"SELECT o." + columnInfo[0].columnName, 1);
		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(", \" +");
			write("\"o." + columnInfo[i].columnName, 2);
		}
		writeLine(" \" +");
		writeLine("\"FROM " + tableName + " o \" +", 2);
		writeLine("\"WHERE o." + columnInfo[0].columnName + " > ? AND o.id < %d \" +", 2);
		writeLine("\"ORDER BY o." + columnInfo[0].columnName + " LIMIT ? OFFSET ?\";", 2);
		writeLine();
		writeLine("/** Creates the COPY SQL for the specific " + tableName.toUpperCase() + " table. */", 1);
		write("public static final String COPY = \"COPY " + tableName + " (" + columnInfo[0].columnName, 1);
		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(", \" +");
			write("\"" + columnInfo[i].columnName, 2);
		}
		writeLine(") \" +");
		writeLine("\"FROM '%s' \" +", 2);
		writeLine("\"credentials 'aws_access_key_id=%s;aws_secret_access_key=%s' \" +", 2);
		writeLine("\"emptyasnull \" +", 2);
		writeLine("\"gzip \" +", 2);
		writeLine("\"delimiter '\\t'\";", 2);
		writeLine();
		writeLine("/** S3 resource name for the INSERT file. */", 1);
		writeLine("public static final String S3_KEY = \"loader/" + tableName + ".gz\";", 1);
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		final String name = getObjectName();
		final String className = getClassName(name);

		// Start section.
		writeLine();
		writeLine("/** Application entry point. */", 1);
		writeLine("public static void main(final String... args) throws Exception", 1);
		writeLine("{", 1);
		writeLine("final AnalyticsConfiguration conf = AnalyticsApplication.load(args);", 2);
		writeLine("final long[] minMaxIds = getMinAndMaxIds(conf, TABLE_NAME);", 2);
		writeLine();
		writeLine("new " + className + "(conf, minMaxIds[1], minMaxIds[0]).run();", 2);
		writeLine();
		writeLine("System.exit(0);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("private long lastId = 0L;", 1);
		writeLine("private final String select;", 1);
		writeLine();
		writeLine("public " + className + "(final AnalyticsConfiguration conf, final long lastId, final long minId)", 1);
		writeLine("{", 1);
		writeLine("super(conf);", 2);
		writeLine();
		writeLine("info(\"MAX_ID: {}\", this.lastId = lastId);", 2);
		writeLine("info(\"SELECT: {}\", (select = String.format(SELECT, minId)));", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getName() { return NAME; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public int getBatchSize() { return BATCH_SIZE; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getSelect() { return select; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getCopy() { return COPY; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getS3Key() { return S3_KEY; }", 1);
	}

	/** Output method - writes the mutator methods. */
	private void writeOutputMethod() throws IOException	
	{
		writeLine();
		writeLine("@Override", 1);
		writeLine("public int bindSelect(final PreparedStatement stmt) throws SQLException", 1);
		writeLine("{", 1);
		writeLine("info(\"BINDING: lastId: {}\", lastId);", 2);
		writeLine("stmt.setLong(1, lastId);", 2);
		writeLine();
		writeLine("return 1;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Writes a single line to the CSV file. */", 1);
		writeLine("@Override", 1);
		writeLine("protected Item writeLine(final ResultSet rs, final CSVWriter out) throws IOException, SQLException", 1);
		writeLine("{", 1);
		writeLine("out", 2);
		for (final ColumnInfo i : columnInfo)
		{
			if (i.isPartOfPrimaryKey)
				writeLine(".add(lastId = rs.get" + i.jdbcMethodSuffix + "(\"" + i.columnName + "\"))", 3);
			else if (PRIMITIVES.contains(i.javaType) && i.isNullable)
				writeLine(".add((" + i.javaType + ") rs.getObject(\"" + i.columnName + "\"))", 3);
			else
				writeLine(".add(rs.get" + i.jdbcMethodSuffix + "(\"" + i.columnName + "\"))", 3);
		}
		writeLine(".add();	// Line terminator", 3);
		writeLine();
		writeLine("return null;	// Do not store in DynamoDB.", 2);
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
		@param args7 package name of the entity bean CMP classes.
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
			final String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new RedshiftLoader(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + RedshiftLoader.class.getName() + " Output directory");
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
