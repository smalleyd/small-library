package com.small.library.atg.gen;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generator class for Dynamo SQL Repository Item Wrapper classes. Item Wrapper
*	classes are simple Java beans that encapsulate a <I>RepositoryItem</I> and
*	expose accessors and mutators of an entity for each property that closely
*	matches a property's type. This overcomes the overly simple <CODE>
*	getPropertyValue</CODE> and <CODE>setPropertyValue</CODE> methods of
*	<I>RepositoryItem</I> (<I>MutableRepositoryItem</I>) that do not enforce
*	strict type safety during compile.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 6/20/2002
*
***************************************************************************************/

public class SQLRepositoryItemWrapper extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - default package name of the ATG Dynamo Repository framework.
	    The default value is "com.small.library.atg.repository".
	*/
	public static final String DEFAULT_FRAMEWORK_PACKAGE =
		"com.small.library.atg.repository";

	/******************************************************************************
	*
	*	Constants - Method suffixes
	*
	******************************************************************************/

	/** Constant - Method suffix for "long". */
	public static final String METHOD_SUFFIX_LONG = SQLRepositoryItemBase.METHOD_SUFFIX_LONG;

	/** Constant - Method suffix for "int". */
	public static final String METHOD_SUFFIX_INTEGER = SQLRepositoryItemBase.METHOD_SUFFIX_INTEGER;

	/** Constant - Method suffix for "short". */
	public static final String METHOD_SUFFIX_SMALLINT = SQLRepositoryItemBase.METHOD_SUFFIX_SMALLINT;

	/******************************************************************************
	*
	*	Static members
	*
	******************************************************************************/

	/** Static member - map of SQL data types (java.sql.Types) to an ATG
	    Dynamo SQL Repository data-type attribute names.
	*/
	private static Map METHOD_SUFFIXES = null;

	/** Static constructor - initializes static member variables. */
	static
	{
		METHOD_SUFFIXES = new HashMap();

		METHOD_SUFFIXES.put(new Integer(java.sql.Types.BIGINT), METHOD_SUFFIX_LONG);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.BINARY), "Binary");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.BLOB), "Binary");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.LONGVARBINARY), "Binary");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.VARBINARY), "Binary");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.BIT), "Boolean");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.CHAR), "String");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.VARCHAR), "String");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.CLOB), "String");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.LONGVARCHAR), "String");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.DATE), "Date");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.TIME), "Timestamp");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.TIMESTAMP), "Timestamp");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.DECIMAL), "Double");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.DOUBLE), "Double");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.NUMERIC), "Double");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.REAL), "Double");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.FLOAT), "Float");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.INTEGER), METHOD_SUFFIX_INTEGER);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.SMALLINT), METHOD_SUFFIX_SMALLINT);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.TINYINT), "Short");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.ARRAY), "Array");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.DISTINCT), "Map");
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.JAVA_OBJECT), null);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.NULL), null);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.OTHER), null);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.REF), null);
		METHOD_SUFFIXES.put(new Integer(java.sql.Types.STRUCT), null);
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public SQLRepositoryItemWrapper() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public SQLRepositoryItemWrapper(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		this(pWriter, strAuthor, pTable, null);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public SQLRepositoryItemWrapper(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable);

		m_strPackageName = strPackageName;
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the SQL Repository Item Descriptor. */
	public void generate() throws GeneratorException, IOException
	{
		writeHeader();
		writeClassDeclaration();
		writeInstanceCreationHelpers();
		writeConstructors();

		try
		{
			writeAccessors();
			writeMutators();
		}

		catch (SQLException pEx) { throw new GeneratorException(pEx); }

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
		return getObjectName() + "RepositoryBean.java";
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - gets the an Item Descriptor property data-type base
	    on the column data type.
		@param pColumn A table column object.
	*/
	public String getPropertyMethodSuffix(Columns.Record pColumn)
	{
		return (String) METHOD_SUFFIXES.get(getSQLType(pColumn));
	}

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		if (null != m_strPackageName)
		{
			writeLine("package " + m_strPackageName + ";");
			writeLine();
		}

		// Must include "java.util.*" becuase of Date, Set, List, and Map.

		writeLine("import java.sql.Timestamp;");
		writeLine("import java.util.*;");
		writeLine();
		writeLine("import atg.repository.*;");
		writeLine();
		writeLine("import " + getFrameworkPackage() + ".*;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tBean class that represents the " + getTable().getName() + " repository item. Acts ");
		writeLine("*\tas a wrapper for the Repository Item that represents the " + getTable().getName());
		writeLine("*\tentity.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.0.0");
		writeLine("*\t@date " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public class " + getObjectName() + "RepositoryBean extends MutableRepositoryBean");
		writeLine("{");
	}

	/** Output method - writes the instance creation helpers. */
	private void writeInstanceCreationHelpers() throws IOException
	{
		String strObjectName = getObjectName();

		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tHelper methods - Instance creation methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");
		writeLine();
		writeLine("\t/** Helper method - constructs an array of " + strObjectName + " repository beans");
		writeLine("\t    from an array of Mutable Repository Items.");
		writeLine("\t\t@param repositoryItems Array of Mutable Repository Items.");
		writeLine("\t\t@param pClassType A <I>Class</I> object that represents the");
		writeLine("\t\t\tspecific entity repository bean to construct.");
		writeLine("\t*/");
		writeLine("\tpublic static " + strObjectName + "RepositoryBean[] createRepositoryBeans(");
		writeLine("\t\tMutableRepositoryItem[] repositoryItems)");
		writeLine("\t\t\tthrows RepositoryBeanCreationException");
		writeLine("\t{");
		writeLine("\t\treturn (" + strObjectName + "RepositoryBean[]) createRepositoryBeans(repositoryItems,");
		writeLine("\t\t\t" + strObjectName + "RepositoryBean.class);");
		writeLine("\t}");
		writeLine();
		writeLine("\t/** Helper method - constructs an array of " + strObjectName + " repository beans");
		writeLine("\t    from an array of Repository Items.");
		writeLine("\t\t@param repositoryItems Array of Repository Items.");
		writeLine("\t\t@param pClassType A <I>Class</I> object that represents the");
		writeLine("\t\t\tspecific entity repository bean to construct.");
		writeLine("\t*/");
		writeLine("\tpublic static " + strObjectName + "RepositoryBean[] createRepositoryBeans(");
		writeLine("\t\tRepositoryItem[] repositoryItems)");
		writeLine("\t\t\tthrows RepositoryBeanCreationException");
		writeLine("\t{");
		writeLine("\t\treturn createRepositoryBeans((MutableRepositoryItem[]) repositoryItems);");
		writeLine("\t}");
	}

	/** Output method - writes the constructors. */
	private void writeConstructors() throws IOException
	{
		String strObjectName = getObjectName();

		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tConstructors");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");
		writeLine();
		writeLine("\t/** Constructor - constructs a populated object.");
		writeLine("\t\t@param repositoryItem A Repository Item object that contains");
		writeLine("\t\t\t" + strObjectName + " record values.");
		writeLine("\t*/");
		writeLine("\tpublic " + strObjectName + "RepositoryBean(RepositoryItem repositoryItem)");
		writeLine("\t{");
		writeLine("\t\tthis((MutableRepositoryItem) repositoryItem);");
		writeLine("\t}");
		writeLine();
		writeLine("\t/** Constructor - constructs a populated object.");
		writeLine("\t\t@param repositoryItem A Mutable Repository Item object that contains");
		writeLine("\t\t\t" + strObjectName + " record values.");
		writeLine("\t*/");
		writeLine("\tpublic " + strObjectName + "RepositoryBean(MutableRepositoryItem repositoryItem)");
		writeLine("\t{");
		writeLine("\t\tsuper(repositoryItem);");
		writeLine("\t}");
	}

	/** Output method - writes the bean accessors. */
	private void writeAccessors() throws IOException, SQLException
	{
		Columns columns = getColumns();

		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		for (int i = 0; i < columns.size(); i++)
		{
			Columns.Record column = (Columns.Record) columns.item(i);
			String strObjectName = getColumnObjectName(column);
			String strPropertyMethodSuffix = getPropertyMethodSuffix(column);
			String strDataType = getJavaType(column);

			writeLine();
			writeLine("\t/** Accessor method - gets the " + strObjectName + " property. */");
			writeLine("\tpublic " + strDataType + " get" + strObjectName +
				"() { return getProperty" + strPropertyMethodSuffix +
				"(\"" + strObjectName + "\"); }");
		}
	}

	/** Output method - writes the bean mutators. */
	private void writeMutators() throws IOException, SQLException
	{
		Columns columns = getColumns();

		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMutator methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		for (int i = 0; i < columns.size(); i++)
		{
			Columns.Record column = (Columns.Record) columns.item(i);
			String strObjectName = getColumnObjectName(column);
			String strDataType = getJavaType(column);

			writeLine();
			writeLine("\t/** Mutator method - sets the " + strObjectName + " property. */");
			writeLine("\tpublic void set" + strObjectName + "(" +
				strDataType + " newValue) { setPropertyValue(\"" + strObjectName +
				"\", newValue); }");
		}
	}

	/** Output method - write the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets the package name of the wrapper class. */
	public String getPackageName() { return m_strPackageName; }

	/** Accessor method - gets the ATG Dynamo Repository framework package name.
	    This accessor can be overridden to provide a different package name.
	*/
	public String getFrameworkPackage() { return DEFAULT_FRAMEWORK_PACKAGE; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - sets the package name of the wrapper class. */
	public void setPackageName(String newValue) { m_strPackageName = newValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - reference to the package name of the wrapper class. */
	private String m_strPackageName = null;

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

	/** Command line entry point.
		@param strArg1 Output directory.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 package name of the wrapper class.
		@param strArg8 Schema name pattern
	*/
	public static void main(String strArgs[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(strArgs, 0);
			String strAuthor = extractAuthor(strArgs, 5);
			String strPackageName = extractArgument(strArgs, 6, null);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 7);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			SQLRepositoryItemWrapper pGenerator =
				new SQLRepositoryItemWrapper((PrintWriter) null, strAuthor,
				(Tables.Record) null, strPackageName);

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

			System.out.println("Usage: java " + SQLRepositoryItemWrapper.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
