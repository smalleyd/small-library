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
		@param version application version number.
	*/
	public EntityBeanFilter(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName, String version)
	{
		super(pWriter, strAuthor, pTable, strPackageName, version);
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
		writeLine("*\t@version " + getVersion());
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
			// Integer and date fields filter by range so need a "lower boundary" property and an "upper boundary" property.
			if (i.isImportedKey || i.isPartOfPrimaryKey || i.isBoolean || i.isString)
				writeMethod(i);
			else
			{
				writeMethod(i, "From", "lower boundary");
				writeMethod(i, "To", "upper boundary");
			}
		}
	}

	/** Helper method: write the methods. */
	private void writeMethod(ColumnInfo i) throws IOException
	{
		writeMethod(i, "", null);
	}

	/** Helper method: write the methods. */
	private void writeMethod(ColumnInfo i, String suffix, String commentSuffix) throws IOException
	{
		writeLine();
		write("\t/** Filter option that represents the \"" + i.columnName + "\" field");
		if (null != commentSuffix)
			write(" - " + commentSuffix);
		writeLine(". */");

		// Primitives should be nullable for the filter.
		String type = i.javaType;
		if (i.isPrimitive)
			type = fromPrimitiveToObject(i.javaType);

		// Write the getter.
		write("\tpublic " + type + " " + i.accessorMethodName + suffix + "()");

		// Writer the member variable.
		writeLine(" { return " + i.memberVariableName + suffix + "; }");
		writeLine("\tprivate " + type + " " + i.memberVariableName + suffix + " = null;");

		// Write the setter.
		write("\tpublic void " + i.mutatorMethodName + suffix + "(" + type + " newValue)");
		writeLine(" { " + i.memberVariableName + suffix + " = newValue; }");

		// Write the "with" method.
		write("\tpublic " + getClassName() + " " + i.withMethodName + suffix + "(" + type + " newValue)");
		writeLine(" { " + i.memberVariableName + suffix + " = newValue; return this; }");
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
		for (ColumnInfo i : m_ColumnInfo)
		{
			// Integer and date fields filter by range so need a "lower boundary" property and an "upper boundary" property.
			if (i.isImportedKey || i.isPartOfPrimaryKey || i.isBoolean || i.isString)
				writeLine("\t\t@param " + i.memberVariableName + " represents the \"" + i.columnName + "\" field.");
			else
			{
				writeLine("\t\t@param " + i.memberVariableName + "From represents the \"" + i.columnName + "\" field - lower boundary.");
				writeLine("\t\t@param " + i.memberVariableName + "To represents the \"" + i.columnName + "\" field - upper boundary.");
			}
		}

		writeLine("\t*/");

		// Constructor signature.
		write("\tpublic " + getClassName() + "(");
		for (int i = 0, last = m_ColumnInfo.length - 1; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];

			if (0 < i)
				write("\t\t");

			// Integer and date fields filter by range so need a "lower boundary" property and an "upper boundary" property.
			if (item.isImportedKey || item.isPartOfPrimaryKey || item.isBoolean || item.isString)
				write(item.javaType + " " + item.memberVariableName);
			else
			{
				writeLine(item.javaType + " " + item.memberVariableName + "From,");
				write(item.javaType + " " + item.memberVariableName + "To", 2);
			}

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("\t{");
		for (ColumnInfo i : m_ColumnInfo)
		{
			// Integer and date fields filter by range so need a "lower boundary" property and an "upper boundary" property.
			if (i.isImportedKey || i.isPartOfPrimaryKey || i.isBoolean || i.isString)
				writeLine("this." + i.memberVariableName + " = " + i.memberVariableName + ";", 2);
			else
			{
				writeLine("this." + i.memberVariableName + "From = " + i.memberVariableName + "From;", 2);
				writeLine("this." + i.memberVariableName + "To = " + i.memberVariableName + "To;", 2);
			}
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
		@param args8 application version number.
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
			EntityBeanFilter pGenerator =
				new EntityBeanFilter((PrintWriter) null, strAuthor,
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