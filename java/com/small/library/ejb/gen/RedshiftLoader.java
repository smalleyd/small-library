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
	private static final Set<String> PRIMITIVES = new HashSet<>(Arrays.asList("Boolean", "Integer", "Long"));

	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Loader";

	/** Constant - default JavaDoc version stamp. */
	public static final String VERSION_DEFAULT = "1.0.1";

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
	public RedshiftLoader() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public RedshiftLoader(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public RedshiftLoader(PrintWriter writer,
		String author, Tables.Record table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public RedshiftLoader(PrintWriter writer,
		String author, Tables.Record table, String packageName,
		String version)
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

		writeConstants();
		writeConstructors();
		writeAccessorMethods();
		writeOutputMethod();

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
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String packageName = getPackageName();
		String basePackageName = getBasePackageName();
		String domainPackageName = getDomainPackageName();

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
		writeLine("*\tClass that loads data from the " + getTable().getName().toUpperCase() + " table in another JDBC compliant");
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
		String name = getObjectName();
		String tableName = getTable().getName();

		writeLine("/** Name of process. */", 1);
		writeLine("public static final String NAME = \"" + name + "\";", 1);
		writeLine();
		writeLine("/** Size of bundle to load into S3 & subsequently Redshift. */", 1);
		writeLine("public static final int BATCH_SIZE = 10000;", 1);
		writeLine();
		writeLine("/** Creates the SELECT SQL for the specific " + tableName.toUpperCase() + " table. */", 1);
		write("public static String SELECT = \"SELECT o." + m_ColumnInfo[0].columnName, 1);
		for (int i = 1; i < m_ColumnInfo.length; i++)
		{
			writeLine(", \" +");
			write("\"o." + m_ColumnInfo[i].columnName, 2);
		}
		writeLine(" \" +");
		writeLine("\"FROM " + tableName + " o \" +", 2);
		writeLine("\"WHERE o." + m_ColumnInfo[0].columnName + " > ? \" +", 2);
		writeLine("\"ORDER BY o." + m_ColumnInfo[0].columnName + " LIMIT ? OFFSET ?\";", 2);
		writeLine();
		writeLine("/** Creates the COPY SQL for the specific " + tableName.toUpperCase() + " table. */", 1);
		write("public static String COPY = \"COPY " + tableName + " (" + m_ColumnInfo[0].columnName, 1);
		for (int i = 1; i < m_ColumnInfo.length; i++)
		{
			writeLine(", \" +");
			write("\"" + m_ColumnInfo[i].columnName, 2);
		}
		writeLine(") \" +");
		writeLine("\"FROM '%s' \" +", 2);
		writeLine("\"credentials 'aws_access_key_id=%s;aws_secret_access_key=%s' \" +", 2);
		writeLine("\"emptyasnull \" +", 2);
		writeLine("\"gzip \" +", 2);
		writeLine("\"delimiter '\\t'\";", 2);
		writeLine();
		writeLine("/** Gets the maximum / last ID loaded. */", 1);
		writeLine("public static final String SELECT_MAX_ID = \"SELECT MAX(id) FROM " + tableName + "\";", 1);
		writeLine();
		writeLine("/** S3 resource name for the INSERT file. */", 1);
		writeLine("public static final String S3_KEY = \"loader/" + tableName + ".gz\";", 1);
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		String name = getObjectName();
		String className = getClassName(name);

		// Start section.
		writeLine();
		writeLine("/** Application entry point. */", 1);
		writeLine("public static void main(String... args)", 1);
		writeLine("{", 1);
		writeLine("AnalyticsConfiguration conf = AnalyticsApplication.load(args);", 2);
		writeLine();
		writeLine("// Get the last ID loaded.", 2);
		writeLine("long maxId = 0L;", 2);
		writeLine("try (Connection conn = conf.getDest().getConnection())", 2);
		writeLine("{", 2);
		writeLine("try (Statement stmt = conn.createStatement())", 3);
		writeLine("{", 3);
		writeLine("ResultSet rs = stmt.executeQuery(SELECT_MAX_ID);", 4);
		writeLine("if (rs.next())", 4);
		writeLine("maxId = rs.getLong(1);", 5);
		writeLine("}", 3);
		writeLine("}", 2);
		writeLine("catch (SQLException ex) { throw new RuntimeException(ex); }", 2);
		writeLine();
		writeLine("new " + className + "(conf, maxId).run();", 2);
		writeLine();
		writeLine("System.exit(0);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Populator. */", 1);
		writeLine("public " + className + "(AnalyticsConfiguration conf, long lastId)", 1);
		writeLine("{", 1);
		writeLine("super(conf);", 2);
		writeLine();
		writeLine("info(\"MAX_ID: %d\", this.lastId = lastId);", 2);
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
		writeLine("public String getSelect() { return SELECT; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getCopy() { return COPY; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getS3Key() { return S3_KEY; }", 1);
		writeLine();
		writeLine("/** Represents the last ID field retrieved. Used as a filter on the SELECT query. */", 1);
		writeLine("private long lastId = 0L;", 1);
	}

	/** Output method - writes the mutator methods. */
	private void writeOutputMethod() throws IOException	
	{
		writeLine();
		writeLine("@Override", 1);
		writeLine("public int bindSelect(PreparedStatement stmt) throws SQLException", 1);
		writeLine("{", 1);
		writeLine("info(\"BINDING: lastId: %d\", lastId);", 2);
		writeLine("stmt.setLong(1, lastId);", 2);
		writeLine();
		writeLine("return 1;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Writes a single line to the CSV file. */", 1);
		writeLine("protected Item writeLine(ResultSet rs, CSVWriter out) throws IOException, SQLException", 1);
		writeLine("{", 1);
		writeLine("out", 2);
		for (ColumnInfo i : m_ColumnInfo)
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
		@param args7 package name of the entity bean CMP classes.
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
			String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			Tables pTables = extractTables(args, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			RedshiftLoader pGenerator =
				new RedshiftLoader((PrintWriter) null, strAuthor,
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

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
