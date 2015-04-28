package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean filter classes. The filter classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 4/28/2015
*
***************************************************************************************/

public class EntityBeanFilter extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Filter";

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
	public EntityBeanFilter() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanFilter(PrintWriter pWriter,
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
	public EntityBeanFilter(PrintWriter pWriter,
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
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMemberVariables();
		writeConstructors();

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
		String strPackageName = getPackageName();

		if (null != strPackageName)
		{
			writeLine("package " + strPackageName + ";");
			writeLine();
		}

		writeLine("import java.util.Date;");
		writeLine();
		writeLine("/********************************************************************************************************************");
		writeLine("*");
		writeLine("*\tValue object class that represents the search criteria for " + createObjectName(getTable().getName()) + " query.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.1");
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("*******************************************************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public class " + getClassName() + " extends QueryFilter");
		writeLine("{");
		writeLine("\t/** Constant - serial version UID. */");
		writeLine("\tpublic static final long serialVersionUID = 1L;");
	}

	/** Output method - writes the member variables. */
	private void writeMemberVariables() throws IOException
	{
		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write member variables.
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine();
			writeLine("\t/** Filter option that represents the \"" + i.columnName + "\" field. */");

			// Write the getter.
			write("\tpublic " + i.javaType + " " + i.accessorMethodName + "()");

			// Writer the member variable.
			writeLine(" { return " + i.memberVariableName + "; }");
			write("\tprivate " + i.javaType + " " + i.memberVariableName);
			if (!i.isPrimitive)
				write(" = null");
			writeLine(";");

			// Write the setter.
			write("\tpublic void " + i.mutatorMethodName + "(" + i.javaType + " newValue)");
			writeLine(" { " + i.memberVariableName + " = newValue; }");

			// Write the "with" method.
			write("\tpublic " + getClassName() + " " + i.withMethodName + "(" + i.javaType + " newValue)");
			writeLine(" { " + i.memberVariableName + " = newValue; return this; }");
		}
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		// Start section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tConstructors");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write the default/empty constructor. */
		writeLine();
		writeLine("\t/** Default/empty. */");
		writeLine("\tpublic " + getClassName() + "() {}");

		// Write constructor with all possible values.
		writeLine();
		writeLine("\t/** Populator.");

		// Create the parameter comments.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];
			writeLine("\t\t@param " + item.memberVariableName + " represents the \"" +
				item.columnName + "\" field.");
		}

		writeLine("\t*/");

		// Constructor signature.
		write("\tpublic " + getClassName() + "(");
		for (int i = 0, last = m_ColumnInfo.length - 1; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];

			if (0 < i)
				write("\t\t");

			write(item.javaType + " " + item.memberVariableName);

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("\t{");
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];
			writeLine("\t\tthis." + item.memberVariableName + " = " +
				item.memberVariableName + ";");
		}
		writeLine("\t}");			
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
		@param strArg1 Output directory.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 package name of the entity bean value object.
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
			EntityBeanFilter pGenerator =
				new EntityBeanFilter((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanFilter.class.getName() + " Output directory");
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
