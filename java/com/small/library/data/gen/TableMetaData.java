package com.small.library.data.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean CMP classes. The CMP classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/12/2002
*
***************************************************************************************/

public class TableMetaData extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "MetaData";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param strEntityName Name of the entity.
	*/
	public static String getClassName(String strEntityName)
	{
		return strEntityName + CLASS_NAME_SUFFIX;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public TableMetaData() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public TableMetaData(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public TableMetaData(PrintWriter pWriter,
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

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		try { m_ColumnInfo = getColumnInfo(); }
		catch (SQLException ex) { throw new GeneratorException(ex); }

		writeHeader();
		writeClassDeclaration();

		writeMaxLengthConstants();
		writeIsRequiredConstants();

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
		if (null != m_strPackageName)
			writeLine("package " + m_strPackageName + ";");

		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tClass that contains constants that describe the meta data of table column");
		writeLine("*\tinformation such as maximum lengths and required fields.");
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
		writeLine("public class " + getClassName());
		writeLine("{");
	}

	/** Output method - writes the member variables. */
	private void writeMaxLengthConstants() throws IOException
	{
		// Start member variable section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tConstants - maximum lengths");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write member variables.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Constant - maximum length of field \"" + m_ColumnInfo[i].columnName + "\". */");
			writeLine("\tpublic static final int MAX_LEN_" + m_ColumnInfo[i].columnName.toUpperCase() + " = " + m_ColumnInfo[i].size + ";");
		}
	}

	/** Output method - writes the member variables. */
	private void writeIsRequiredConstants() throws IOException
	{
		// Start member variable section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tConstants - is required?");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write member variables.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			String isRequired = "true";

			if (m_ColumnInfo[i].isNullable)
				isRequired = "false";

			writeLine();
			writeLine("\t/** Constant - is field \"" + m_ColumnInfo[i].columnName + "\" required. */");
			writeLine("\tpublic static final boolean REQUIRED_" + m_ColumnInfo[i].columnName.toUpperCase() + " = " + isRequired + ";");
		}
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

	/** Member variable - contains the package name of the generated class. */
	protected String m_strPackageName = null;

	/** Member variable - array of column information objects. */
	protected ColumnInfo[] m_ColumnInfo = null;

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
		@param strArg7 package name of the entity bean CMP classes.
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
			TableMetaData pGenerator =
				new TableMetaData((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + TableMetaData.class.getName() + " Output directory");
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
