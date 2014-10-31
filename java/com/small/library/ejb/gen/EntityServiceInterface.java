package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates the interface for each entities' services. The metadata
*	is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 1/6/2012
*
***************************************************************************************/

public class EntityServiceInterface extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Service";

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
	public EntityServiceInterface() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityServiceInterface(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityServiceInterface(PrintWriter writer,
		String author, Tables.Record table, String packageName)
	{
		super(writer, author, table, packageName);
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

		writeMethods();

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

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
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

		writeLine("import com.fieldlens.common.exception.ValidationException;");
		writeLine("import com.fieldlens.common.value.QueryResults;");
		writeLine();
		writeLine("import com.fieldlens.platform.value.*;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tService interface that represents access to the " + getObjectName() + " entity.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.1");
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public interface " + getClassName());
		writeLine("{");
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		// Get the primary key Java type. Assume that it is NOT a composite key.
		String primaryKeyType = "Integer";
		for (ColumnInfo column : m_ColumnInfo)
		{
			if (column.isPartOfPrimaryKey)
			{
				primaryKeyType = column.javaType;
				break;
			}
		}

		writeLine("/** Adds a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + getValueObjectName() + " add(" + getValueObjectName() + " value) throws ValidationException;", 1);
		writeLine();
		writeLine("/** Updates a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void update(" + getValueObjectName() + " value) throws ValidationException;", 1);
		writeLine();
		writeLine("/** Removes a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void remove(" + getValueObjectName() + " value) throws ValidationException;", 1);
		writeLine();
		writeLine("/** Validates a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void validate(" + getValueObjectName() + " value) throws ValidationException;", 1);
		writeLine();
		writeLine("/** Gets a single " + getObjectName() + " value by identifier.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return NULL if not found.", 1);
		writeLine(" */", 1);
		writeLine("public " + getValueObjectName() + " getById(" + primaryKeyType + " id);", 1);
		writeLine();
		writeLine("/** Gets the " + getObjectName() + " entry form data.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id can be NULL for add entry forms.", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" */", 1);
		writeLine("public " + getObjectName() + "Model getModel(" + primaryKeyType + " id);", 1);
		writeLine();
		writeLine("/** Searches the " + getObjectName() + " entity based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param filter", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public QueryResults<" + getValueObjectName() + "> search(" + getObjectName() + "Filter filter) throws ValidationException;", 1);
		writeLine();
		writeLine("/** Counts the number of " + getObjectName() + " entities based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return zero if none found.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public long count(" + getObjectName() + "Filter value) throws ValidationException;", 1);
		writeLine();
		writeLine("/** Gets history of changes for a specific " + getObjectName() + ".", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" */", 1);
		writeLine("public QueryResults<" + getObjectName() + "JValue> history(" + primaryKeyType + " id);", 1);
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
			EntityServiceInterface pGenerator =
				new EntityServiceInterface((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityServiceInterface.class.getName() + " Output directory");
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
