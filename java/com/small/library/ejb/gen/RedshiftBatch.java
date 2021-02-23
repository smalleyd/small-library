package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class that batch copies data (inserts and updates) from SQS to Redshift.
*
*	@author David Small
*	@version 2.0
*	@since 7/13/2016
*
***************************************************************************************/

public class RedshiftBatch extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "Batch";

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
	public RedshiftBatch(PrintWriter writer,
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
	public RedshiftBatch(PrintWriter writer,
		String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public RedshiftBatch(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public RedshiftBatch(PrintWriter writer,
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

		String name = getObjectName();
		writeLine("import java.io.*;");
		writeLine();
		writeLine("import " + basePackageName + "." + getAppName() + "BatchConfig;");
		writeLine("import " + domainPackageName + ".dwservice.io.CSVWriter;");
		writeLine("import " + basePackageName + ".value." + valueObjectName(name) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tBatch process class that loads inserts and updates into the " + getTable().name.toUpperCase() + " table from the AWS queue.");
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
		writeLine("public class " + getClassName() + " extends AbstractBatch<" + valueObjectName(getObjectName()) + ">");
		writeLine("{");
	}

	/** Output method - writes the SQS and SQL constants. */
	private void writeConstants() throws IOException
	{
		final String name = getObjectName();
		final String tableName = getTable().name;

		writeLine("/** Name of process. */", 1);
		writeLine("public static final String NAME = \"" + name + "\";", 1);
		writeLine();
		writeLine("/** Size of bundle to load into S3 & subsequently Redshift. */", 1);
		writeLine("public static final int BATCH_SIZE = 10000;", 1);
		writeLine();
		writeLine("/** Creates the SQL for the specific " + tableName.toUpperCase() + " tables. The update uses a shadow table. */", 1);
		writeLine("public static String createCopySQL(final String entity)", 1);
		writeLine("{", 1);
		write("return new StringBuilder(\"COPY \").append(entity).append(\" (" + columnInfo[0].columnName, 2);
		for (int i = 1; i < columnInfo.length; i++)
		{
			writeLine(", \" +");
			write("\"" + columnInfo[i].columnName, 3);
		}
		writeLine(") \" +");
		writeLine("\"FROM '%s' \" +", 3);
		writeLine("\"credentials 'aws_access_key_id=%s;aws_secret_access_key=%s' \" +", 3);
		writeLine("\"emptyasnull ACCEPTINVCHARS AS ' ' TRUNCATECOLUMNS \" +", 3);
		writeLine("\"gzip \" +", 3);
		writeLine("\"delimiter '\\t'\").toString();", 3);
		writeLine("}", 1);
		writeLine();
		writeLine("/** COPY into INSERT table statement. */", 1);
		writeLine("public static final String INSERTS = createCopySQL(\"" + tableName + "\");", 1);
		writeLine();
		writeLine("/** COPY into UPDATE table statement. */", 1);
		writeLine("public static final String UPDATES = createCopySQL(\"" + tableName + "_u\");", 1);
		writeLine();
		writeLine("/** Merge the UPDATE table with target table. */", 1);
		write("public static final String MERGE = \"UPDATE " + tableName + " SET " + columnInfo[1].columnName + " = u." + columnInfo[1].columnName, 1);
		for (int i = 2; i < columnInfo.length; i++)
		{
			writeLine(", \" +");
			write("\"" + columnInfo[i].columnName + " = u." + columnInfo[i].columnName, 2);
		}
		writeLine(" \" +");
		writeLine("\"FROM " + tableName + "_u u INNER JOIN " + tableName + " v ON u." + columnInfo[0].columnName + " = v." + columnInfo[0].columnName + "\";", 2);
		writeLine();
		writeLine("/** Truncate the UPDATE staging table. Only delete the updates with a corresponding value in the primary table. */", 1);
		writeLine("public static final String TRUNCATE = \"TRUNCATE TABLE " + tableName + "_u\";", 1);
		writeLine();
		writeLine("/** S3 resource name for the INSERT file. */", 1);
		writeLine("public static final String S3_INSERT_FILE = \"" + tableName + "_insert.tsv\";", 1);
		writeLine();
		writeLine("/** S3 resource name for the INSERT file. */", 1);
		writeLine("public static final String S3_UPDATE_FILE = \"" + tableName + "_update.tsv\";", 1);
		writeLine();
		writeLine("/** SQS entity name. It is the basis for the INSERT and UPDATE queues. */", 1);
		writeLine("public static final String SQS_ENTITY_NAME = \"" + tableName + "\";", 1);
		writeLine();
		writeLine("/** Name of the database table associated with this batcher. */", 1);
		writeLine("public static final String TABLE_NAME = \"" + tableName + "\";", 1);
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		String name = getObjectName();
		String className = getClassName(name);

		// Start section.
		writeLine();
		writeLine("/** Default/empty. */", 1);
		writeLine("public " + className + "(final " + getAppName() + "BatchConfig conf)", 1);
		writeLine("{", 1);
		writeLine("super(conf, " + valueObjectName(name) + ".class);", 2);
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
		writeLine("public String getInsertsSQL() { return INSERTS; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getUpdatesSQL() { return UPDATES; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getMergeSQL() { return MERGE; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getTruncateSQL() { return TRUNCATE; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getInsertFile() { return S3_INSERT_FILE; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getUpdateFile() { return S3_UPDATE_FILE; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getEntityName() { return SQS_ENTITY_NAME; }", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String getTableName() { return TABLE_NAME; }", 1);
	}

	/** Output method - writes the mutator methods. */
	private void writeOutputMethod() throws IOException	
	{
		// Start the section.
		writeLine();
		writeLine("/** Writes a single line to the CSV file. */", 1);
		writeLine("protected void writeLine(final " + valueObjectName(getObjectName()) + " value, final CSVWriter out) throws IOException", 1);
		writeLine("{", 1);
		writeLine("out", 2);
		for (ColumnInfo i : columnInfo)
			writeLine(".add(value." + i.memberVariableName + ")", 3);
		writeLine(".add();	// Line terminator", 3);
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
			generateTableResources(new RedshiftBatch(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + RedshiftBatch.class.getName() + " Output directory");
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
