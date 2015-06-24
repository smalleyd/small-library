package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean value classes. The value classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 7/18/2002
*
***************************************************************************************/

public class EntityBeanValueObject extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Value";

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
	public EntityBeanValueObject() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanValueObject(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
		@param version Represents the application version number.
	*/
	public EntityBeanValueObject(PrintWriter pWriter,
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
		writeAccessorMethods();
		writeMutatorMethods();
		writeImportedKeysAccessorMethods();
		writeHelperMethods();
		writeObjectMethods();

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

		writeLine("import java.io.Serializable;");
		writeLine("import java.math.BigDecimal;");
		writeLine("import java.util.Date;");
		writeLine();
		writeLine("import org.apache.commons.lang3.StringUtils;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tValue object class that represents the " + getTable().getName() + " table.");
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
		writeLine("public class " + getClassName() + " implements Serializable");
		writeLine("{");
		writeLine("\t/** Constant - serial version UID. */");
		writeLine("\tpublic static final long serialVersionUID = 1L;");
		writeLine();
	}

	/** Output method - writes the member variables. */
	private void writeMemberVariables() throws IOException
	{
		// Start member variable section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMember variables");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write member variables.
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine();
			writeLine("\t/** Member variable - represents the \"" + i.columnName + "\" field. */");
			write("\tpublic " + i.javaType + " " + i.memberVariableName);
			if (!i.isPrimitive)
				write(" = null");

			writeLine(";");
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

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t/** Accessor method - gets the property that represents the \"" + m_ColumnInfo[i].columnName + "\" field. */");
			write("\tpublic " + m_ColumnInfo[i].javaType + " " + m_ColumnInfo[i].accessorMethodName + "()");
				writeLine(" { return " + m_ColumnInfo[i].memberVariableName + "; }");
			writeLine();
		}
	}

	/** Output method - writes the mutator methods. */
	private void writeMutatorMethods() throws IOException	
	{
		// Start the section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMutator methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();

		// Write mutators.
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine("\t/** Mutator method - sets the property that represents the \"" + i.columnName + "\" field. */");
			write("\tpublic void " + i.mutatorMethodName + "(" + i.javaType + " newValue)");
				writeLine(" { " + i.memberVariableName + " = newValue; }");

			// Write the "with" method.
			write("\tpublic " + getClassName() + " " + i.withMethodName + "(" + i.javaType + " newValue)");
			writeLine(" { " + i.memberVariableName + " = newValue; return this; }");

			writeLine();
		}
	}

	/** Output method - writes the imported foreign key accessor methods. */
	private void writeImportedKeysAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods - Imported foreign keys");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo columnInfo = m_ColumnInfo[i];

			if (!columnInfo.isImportedKey)
				continue;

			String name = columnInfo.importedKeyName;
			String memberName = columnInfo.importedKeyMemberName;

			writeLine();
			writeLine("/** CMR member - " + name + " name. */", 1);
			writeLine("public String " + memberName + "Name = null;", 1);
			writeLine();
			writeLine("/** CMR accessor - " + name + " name. */", 1);
			write("public String get" + name + "Name()", 1);
			writeLine(" { return " + memberName + "Name; }");
			writeLine();
			writeLine("/** CMR mutator - " + name + " name. */", 1);
			write("public void set" + name + "Name(String newValue)", 1);
			writeLine(" { " + memberName + "Name = newValue; }");
			writeLine("public " + getClassName() + " with" + name + "Name(String newValue) { " + memberName + "Name = newValue; return this; }", 1);
		}
	}

	/** Output method - writes the internal helper methods. */
	private void writeHelperMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("/**************************************************************************", 1);
		writeLine("*", 1);
		writeLine("*\tHelper methods", 1);
		writeLine("*", 1);
		writeLine("**************************************************************************/", 1);

		// Output the clean method for String cleansing.
		writeLine();
		writeLine("/** Helper method - trims all string fields and converts empty strings to NULL. */", 1);
		writeLine("public void clean()", 1);
		writeLine("{", 1);
		for (ColumnInfo i : m_ColumnInfo)
		{
			if (!i.isString)
				continue;

			writeLine(i.memberVariableName + " = StringUtils.trimToNull(" + i.memberVariableName + ");", 2);
		}
		writeLine("}", 1);
	}

	/** Output method - writes Object class override methods. */
	private void writeObjectMethods() throws IOException
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

		for (int i = 1; i < m_ColumnInfo.length; i++)
			writeLine(".append(\", " + (item = m_ColumnInfo[i]).memberVariableName + ": \").append(" + item.memberVariableName + ")", 3);
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
			String version = extractArgument(strArgs, 7, null);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanValueObject pGenerator =
				new EntityBeanValueObject((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanValueObject.class.getName() + " Output directory");
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
