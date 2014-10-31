package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean primary key classes. The primary key classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 7/12/2002
*
***************************************************************************************/

public class EntityBeanPrimaryKey extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "PK";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the primary key
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
	public EntityBeanPrimaryKey() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanPrimaryKey(PrintWriter pWriter,
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
	public EntityBeanPrimaryKey(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable, strPackageName);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateKeyColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMemberVariables();
		writeConstructor();
		writeAccessorMethods();

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
		return getClassName(createObjectName(pTable.getName())) + ".java";
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - populates the key column information. */
	private void populateKeyColumnInfo()
		throws GeneratorException
	{
		populateColumnInfo();

		// Get key column information.
		PrimaryKeys primaryKeys = getTable().getPrimaryKeys();

		try { primaryKeys.load(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }

		m_KeyColumnInfo = new ColumnInfo[primaryKeys.size()];

		// Get relevant column values.
		for (int i = 0, key = 0; i < m_ColumnInfo.length; i++)
			if (null != primaryKeys.find(m_ColumnInfo[i].columnName))
				m_KeyColumnInfo[key++] = m_ColumnInfo[i];
	}

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String strPackageName = getPackageName();

		if (null != strPackageName)
		{
			writeLine("package " + strPackageName + ";");
			writeLine();
		}

		writeLine("import java.io.Serializable;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tEntity Bean primary key class that represents the " + getTable().getName());
		writeLine("*\ttable.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.0.0");
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public class " + getResourceName() + " implements Serializable");
		writeLine("{");
	}

	/** Output method - writes the primary key class member variables. */
	private void writeMemberVariables() throws IOException
	{
		// Start member variable section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMember variables");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any key columns available?
		if (0 >= m_KeyColumnInfo.length)
			return;

		writeLine();

		// Write member variables.
		for (int i = 0; i < m_KeyColumnInfo.length; i++)
		{
			writeLine("\t/** Member variable - represents the \"" + m_KeyColumnInfo[i].columnName + "\" field. */");
			writeLine("\tpublic " + m_KeyColumnInfo[i].javaType + " " + m_KeyColumnInfo[i].memberVariableName + ";");
			writeLine();
		}
	}

	/** Output method - writes the constructor. */
	private void writeConstructor() throws IOException	
	{
		// Start section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tConstructors");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");
		writeLine();

		// Write default constructor.
		writeLine("\t/** Constructor - constructs an empty object. */");
		writeLine("\tpublic " + getResourceName() + "() {}");

		// Any key columns available?
		if (0 >= m_KeyColumnInfo.length)
			return;

		// Write constructor with all possible values.
		writeLine();
		writeLine("\t/** Constructor - constructs a populated object.");

		for (int i = 0; i < m_KeyColumnInfo.length; i++)
			writeLine("\t\t@param " + m_KeyColumnInfo[i].localVariableName +
				" represents the \"" + m_KeyColumnInfo[i].columnName +
				"\" field.");

		writeLine("\t*/");
		writeLine("\tpublic " + getResourceName() + "(");

		for (int i = 0; i < m_KeyColumnInfo.length - 1; i++)
			writeLine("\t\t" + m_KeyColumnInfo[i].javaType + " " +
				m_KeyColumnInfo[i].localVariableName + ",");

		writeLine("\t\t" + m_KeyColumnInfo[m_KeyColumnInfo.length - 1].javaType + " " +
			m_KeyColumnInfo[m_KeyColumnInfo.length - 1].localVariableName + ")");

		writeLine("\t{");

		for (int i = 0; i < m_KeyColumnInfo.length; i++)
			writeLine("\t\t" + m_KeyColumnInfo[i].memberVariableName + " = " +
				m_KeyColumnInfo[i].localVariableName + ";");

		writeLine("\t}");			
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException	
	{
		// Start section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		writeEqualsMethod();
		writeHashCodeMethod();
		writeToStringMethod();
	}

	/** Output method - writes the <CODE>equals</CODE> method. */
	private void writeEqualsMethod() throws IOException	
	{
		writeLine();
		writeLine("\t/** Accessor method - evaluates another instance of this class");
		writeLine("\t    to determine eqality.");
		writeLine("\t*/");
		writeLine("\tpublic boolean equals(Object value)");
		writeLine("\t{");
		writeLine("\t\tif ((null == value) || (!(value instanceof " +
			getResourceName() + ")))");
		writeLine("\t\t\treturn false;");
		writeLine();

		// Loop through each field and perform an equality check.
		for (int i = 0; i < m_KeyColumnInfo.length; i++)
		{
			if (m_KeyColumnInfo[i].isPrimitive)
				writeLine("\t\tif (((" + getResourceName() + ") value)." +
					m_KeyColumnInfo[i].memberVariableName + " != " + m_KeyColumnInfo[i].memberVariableName +
					")");
			else
			{
				writeLine("\t\tif (((((" + getResourceName() + ") value)." +
					m_KeyColumnInfo[i].memberVariableName + " != null) && (" + m_KeyColumnInfo[i].memberVariableName +
					" == null))");
				writeLine("\t\t   || ((" + m_KeyColumnInfo[i].memberVariableName + " != null) && !" +
					m_KeyColumnInfo[i].memberVariableName + ".equals(((" + getResourceName() + ") value)." +
					m_KeyColumnInfo[i].memberVariableName + ")))");
			}

			writeLine("\t\t\treturn false;");
			writeLine();
		}

		writeLine("\t\treturn true;");
		writeLine("\t}");
	}

	/** Output method - writes the <CODE>hashCode</CODE> method. */
	private void writeHashCodeMethod() throws IOException	
	{
		writeLine();
		writeLine("\t/** Accessor method - implements the <I>Object.hashCode</I> method. */");
		writeLine("\tpublic int hashCode()");
		writeLine("\t{");

		if (0 == m_KeyColumnInfo.length)
			writeLine("\t\treturn 0;");

		else if (1 == m_KeyColumnInfo.length)
		{
			if (m_KeyColumnInfo[0].isPrimitive)
				writeLine("\t\treturn (int) " + m_KeyColumnInfo[0].memberVariableName + ";");
			else
				writeLine("\t\treturn " + m_KeyColumnInfo[0].memberVariableName + ".hashCode();");
		}

		else
			writeLine("\t\treturn toString().hashCode();");

		writeLine("\t}");
	}

	/** Output method - writes the <CODE>toString</CODE> method. */
	private void writeToStringMethod() throws IOException	
	{
		writeLine();
		writeLine("\t/** Accessor method - returns a <I>String</I> representation of the instance");
		writeLine("\t    of this class.");
		writeLine("\t*/");
		writeLine("\tpublic String toString()");
		writeLine("\t{");

		if (0 == m_KeyColumnInfo.length)
			writeLine("\t\treturn null;");

		else if (1 == m_KeyColumnInfo.length)
		{
			if (m_KeyColumnInfo[0].isPrimitive)
				writeLine("\t\treturn \"\" + " + m_KeyColumnInfo[0].memberVariableName + ";");
			else
				writeLine("\t\treturn " + m_KeyColumnInfo[0].memberVariableName + ".toString();");
		}

		else
		{
			writeLine("\t\treturn " + m_KeyColumnInfo[0].memberVariableName);

			for (int i = 1; i < m_KeyColumnInfo.length - 1; i++)
				writeLine("\t\t\t+ \":\" + " + m_KeyColumnInfo[i].memberVariableName);

			writeLine("\t\t\t+ \":\" + " + m_KeyColumnInfo[m_KeyColumnInfo.length - 1].memberVariableName + ";");
		}

		writeLine("\t}");
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
	******************************************************************************/

	/** Accessor method - gets the name of the resource (class, descriptor, ...). */
	public String getResourceName() { return getClassName(getObjectName()); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains column information about the primary key
	    columns.
	*/
	private ColumnInfo[] m_KeyColumnInfo = null;

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
		@param strArg7 package name of the primary key classes.
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
			EntityBeanPrimaryKey pGenerator =
				new EntityBeanPrimaryKey((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanPrimaryKey.class.getName() + " Output directory");
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
