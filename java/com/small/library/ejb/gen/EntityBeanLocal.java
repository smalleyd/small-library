package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates interface for EJB Entity Bean local interfaces. The local interfaces
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 7/12/2002
*
***************************************************************************************/

public class EntityBeanLocal extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the local
	    interface from the entity name.
		@param strEntityName Name of the entity.
	*/
	public static String getClassName(String strEntityName)
	{
		return strEntityName;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanLocal() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanLocal(PrintWriter pWriter,
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
	public EntityBeanLocal(PrintWriter pWriter,
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

		writeAccessorMethods();
		writeImportedKeysAccessorMethods();
		writeMutatorMethods();

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
		return createObjectName(pTable.getName()) + CLASS_NAME_SUFFIX + ".java";
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

		writeLine("import javax.ejb.EJBLocalObject;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tEntity Bean local interface that represents the " + getTable().getName());
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
		writeLine("public interface " + getObjectName() + CLASS_NAME_SUFFIX + " extends EJBLocalObject");
		writeLine("{");
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Accessor method - gets the property that represents the");
			writeLine("\t    \"" + m_ColumnInfo[i].columnName + "\" field.");
			writeLine("\t*/");
			writeLine("\tpublic " + m_ColumnInfo[i].javaType + " " + m_ColumnInfo[i].accessorMethodName + "();");
		}

		// Write getAll.
		String objectName = getObjectName();
		String valueObjectName = EntityBeanValueObject.getClassName(objectName);
		
		writeLine();
		writeLine("\t/** Accessor method - gets all properties of the entity bean.");
		writeLine("\t\t@return a <I>" + valueObjectName + "</I> object.");
		writeLine("\t*/");
		writeLine("\tpublic " + valueObjectName + " getAll();");
	}

	/** Output method - writes the imported foreign key accessor methods. */
	private void writeImportedKeysAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
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
			String foreignEntity = getClassName(columnInfo.importedObjectName);

			writeLine();
			writeLine("\t/** Accessor method - gets the " + name + " property as a");
			writeLine("\t    \"" + m_ColumnInfo[i].importedObjectName + "\" entity.");
			writeLine("\t*/");
			writeLine("\tpublic " + foreignEntity + " get" + name + "();");
		}
	}

	/** Output method - writes the mutator methods. */
	private void writeMutatorMethods() throws IOException	
	{
		// Start the section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMutator methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write mutators.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Mutator method - sets the property that represents the");
			writeLine("\t    \"" + m_ColumnInfo[i].columnName + "\" field.");
			writeLine("\t*/");
			writeLine("\tpublic void " + m_ColumnInfo[i].mutatorMethodName + "(" + m_ColumnInfo[i].javaType + " newValue);");
		}

		// Write setAll.
		String objectName = getObjectName();
		String valueObjectName = EntityBeanValueObject.getClassName(objectName);
		
		writeLine();
		writeLine("\t/** Mutator method - sets all properties of the entity bean.");
		writeLine("\t\t@param newValue a <I>" + valueObjectName + "</I> object.");
		writeLine("\t*/");
		writeLine("\tpublic void setAll(" + valueObjectName + " newValue);");
	}

	/** Output method - write the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

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
		@param strArg7 package name of the local interfaces.
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
			EntityBeanLocal pGenerator =
				new EntityBeanLocal((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanLocal.class.getName() + " Output directory");
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
