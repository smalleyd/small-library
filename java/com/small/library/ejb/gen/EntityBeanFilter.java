package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

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
		@param table A table record object to base the output on.
	*/
	public EntityBeanFilter(PrintWriter pWriter,
		String strAuthor, Table table)
	{
		super(pWriter, strAuthor, table);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param table A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
		@param version application version number.
	*/
	public EntityBeanFilter(PrintWriter pWriter,
		String strAuthor, Table table, String strPackageName, String version)
	{
		super(pWriter, strAuthor, table, strPackageName, version);
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

		writeMembers();
		writeMutators();
		writeConstructors();
		writeHelpers();
		writeToString();

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
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(table.name)) + ".java";
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
		String domainPackageName = getDomainPackageName();

		if (null != strPackageName)
		{
			writeLine("package " + strPackageName + ";");
			writeLine();
		}

		writeLine("import java.math.BigDecimal;");
		writeLine("import java.util.Date;");
		writeLine();
		writeLine("import org.apache.commons.lang3.StringUtils;");
		writeLine();
		writeLine("import " + domainPackageName + ".common.dao.QueryFilter;");
		writeLine();
		writeLine("/********************************************************************************************************************");
		writeLine("*");
		writeLine("*\tValue object class that represents the search criteria for " + createObjectName(getTable().name) + " query.");
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
		// writeLine("/** Constant - serial version UID. */", 1);
		writeLine("private static final long serialVersionUID = 1L;", 1);
	}

	/** Output method - writes the member variables. */
	private void writeMembers() throws IOException
	{
		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();
		writeLine("// Members", 1);

		// Write member variables.
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeMember(i);

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (i.isRange())
			{
				writeMember(i, "From", "lower boundary");
				writeMember(i, "To", "upper boundary");
			}
		}
	}

	/** Helper method: write the methods. */
	private void writeMember(ColumnInfo i) throws IOException
	{
		writeMember(i, "", null);
	}

	/** Helper method: write the methods. */
	private void writeMember(ColumnInfo i, String suffix, String commentSuffix) throws IOException
	{
		/* writeLine();
		write("/** Filter option that represents the \"" + i.columnName + "\" field");
		if (null != commentSuffix)
			write(" - " + commentSuffix);
		writeLine(". *", 1);	*/

		// Primitives should be nullable for the filter.
		String type = i.javaType;
		if (i.isPrimitive)
			type = fromPrimitiveToObject(i.javaType);

		/* Write the getter.
		write("public " + type + " " + i.accessorMethodName + suffix + "()", 1);
		writeLine(" { return " + i.memberVariableName + suffix + "; }"); */

		writeLine("public " + type + " " + i.memberVariableName + suffix + " = null;", 1);

		/* Write the setter.
		write("public void " + i.mutatorMethodName + suffix + "(final " + type + " newValue)", 1);
		writeLine(" { " + i.memberVariableName + suffix + " = newValue; }"); */
	}

	/** Output method - writes the member variables. */
	private void writeMutators() throws IOException
	{
		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();
		writeLine("// Mutators", 1);

		// Write member variables.
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeMutator(i);

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (i.isRange())
			{
				writeMutator(i, "From", "lower boundary");
				writeMutator(i, "To", "upper boundary");
			}
		}
	}

	/** Helper method: write the methods. */
	private void writeMutator(ColumnInfo i) throws IOException
	{
		writeMutator(i, "", null);
	}

	/** Helper method: write the methods. */
	private void writeMutator(ColumnInfo i, String suffix, String commentSuffix) throws IOException
	{
		// Primitives should be nullable for the filter.
		String type = i.javaType;
		if (i.isPrimitive)
			type = fromPrimitiveToObject(i.javaType);

		// Write the "with" method.
		write("\tpublic " + getClassName() + " " + i.withMethodName + suffix + "(final " + type + " newValue)");
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

		// Write constructor with paging values.
		writeLine();
		writeLine("/** Populator.", 1);
		writeLine("@param page", 2);
		writeLine("@param pageSize", 2);
		writeLine("*/", 1);
		writeLine("public " + getClassName() + "(final int page, final int pageSize) { super(page, pageSize); }", 1);

		// Write constructor with sorting values.
		writeLine();
		writeLine("/** Populator.", 1);
		writeLine("@param sortOn", 2);
		writeLine("@param sortDir", 2);
		writeLine("*/", 1);
		writeLine("public " + getClassName() + "(final String sortOn, final String sortDir) { super(sortOn, sortDir); }", 1);

		// Write constructor with paging & sorting values.
		writeLine();
		writeLine("/** Populator.", 1);
		writeLine("@param page", 2);
		writeLine("@param pageSize", 2);
		writeLine("@param sortOn", 2);
		writeLine("@param sortDir", 2);
		writeLine("*/", 1);
		writeLine("public " + getClassName() + "(final int page, final int pageSize, final String sortOn, final String sortDir) { super(page, pageSize, sortOn, sortDir); }", 1);

		// Write constructor with all possible values.
		writeLine();
		writeLine("\t/** Populator.");

		// Create the parameter comments.
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine("\t\t@param " + i.memberVariableName + " represents the \"" + i.columnName + "\" field.");

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (i.isRange())
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

			String type = item.javaType;
			if (item.isPrimitive)
				type = fromPrimitiveToObject(item.javaType);

			write("final "); write(type); write(" "); write(item.memberVariableName);

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (item.isRange())
			{
				writeLine(",");
				writeLine("final " + type + " " + item.memberVariableName + "From,", 2);
				write("final " + type + " " + item.memberVariableName + "To", 2);
			}

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("{", 1);
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine("this." + i.memberVariableName + " = " + i.memberVariableName + ";", 2);

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (i.isRange())
			{
				writeLine("this." + i.memberVariableName + "From = " + i.memberVariableName + "From;", 2);
				writeLine("this." + i.memberVariableName + "To = " + i.memberVariableName + "To;", 2);
			}
		}
		writeLine("}", 1);
	}

	/** Helper methods. */
	private void writeHelpers() throws IOException
	{
		// Start section.
		writeLine();
		writeLine("/**************************************************************************", 1);
		writeLine("*", 1);
		writeLine("*\tHelper methods", 1);
		writeLine("*", 1);
		writeLine("**************************************************************************/", 1);

		// Output the clean method for String cleansing.
		writeLine();
		writeLine("/** Helper method - trims all string fields and converts empty strings to NULL. */", 1);
		writeLine("public " + getClassName() + " clean()", 1);
		writeLine("{", 1);
		for (ColumnInfo i : m_ColumnInfo)
		{
			if (!i.isString)
				continue;

			writeLine(i.memberVariableName + " = StringUtils.trimToNull(" + i.memberVariableName + ");", 2);
		}
		writeLine();
		writeLine("return this;", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeToString() throws IOException
	{
		// Start section.
		writeLine();
		writeLine("/**************************************************************************", 1);
		writeLine("*", 1);
		writeLine("*\tObject methods", 1);
		writeLine("*", 1);
		writeLine("**************************************************************************/", 1);

		// Write the toString method. */
		ColumnInfo item = m_ColumnInfo[0];
		writeLine();
		writeLine("@Override", 1);
		writeLine("public String toString()", 1);
		writeLine("{", 1);
		writeLine("return new StringBuilder(\"{ " + item.memberVariableName + ": \").append(" + item.memberVariableName + ")", 2);

		// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
		if (item.isRange())
		{
			writeLine(".append(\", " + item.memberVariableName + "From: \").append(" + item.memberVariableName + "From)", 3);
			writeLine(".append(\", " + item.memberVariableName + "To: \").append(" + item.memberVariableName + "To)", 3);
		}

		for (int i = 1; i < m_ColumnInfo.length; i++)
		{
			writeLine(".append(\", " + (item = m_ColumnInfo[i]).memberVariableName + ": \").append(" + item.memberVariableName + ")", 3);

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (item.isRange())
			{
				writeLine(".append(\", " + (item = m_ColumnInfo[i]).memberVariableName + "From: \").append(" + item.memberVariableName + "From)", 3);
				writeLine(".append(\", " + (item = m_ColumnInfo[i]).memberVariableName + "To: \").append(" + item.memberVariableName + "To)", 3);
			}
		}
		writeLine(".append(\" }\").toString();", 3);
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
			final List<Table> tables = extractTables(args, 1, 8);

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanFilter pGenerator =
				new EntityBeanFilter((PrintWriter) null, strAuthor,
				(Table) null, strPackageName, version);

			// Call the BaseTable method to handle the outputing.
			generateTableResources(pGenerator, tables, fileOutputDir);
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
