package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates interface for EJB Entity Bean home interfaces. The home interfaces
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 7/12/2002
*
***************************************************************************************/

public class EntityBeanHome extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Home";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the home
	    interface from the entity name.
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
	public EntityBeanHome() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanHome(PrintWriter pWriter,
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
	public EntityBeanHome(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable, strPackageName);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean home interface. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeCreators();
		writeFinders();

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

		// Must include "java.util.*" becuase of Collection.

		writeLine("import java.util.Collection;");
		writeLine();
		writeLine("import javax.ejb.*;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tEntity Bean home interface that represents the " + getTable().getName());
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
		writeLine("public interface " + getObjectName() + CLASS_NAME_SUFFIX + " extends EJBLocalHome");
		writeLine("{");
	}

	/** Output method - writes the home interface creators. */
	private void writeCreators() throws IOException
	{
		String objectName = getObjectName();

		// Start creator section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tCreator methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		writeLine();

		// Start creator comment.
		writeLine("\t/** Creator method - creates a " + getTable().getName() +
			" entity with");
		writeLine("\t    all possible field values.");
		writeLine("\t\t@param value Value object that represents the " + objectName +
			" entity.");
		writeLine("\t*/");

		// Write creator method
		writeLine("\tpublic " + objectName + " create(" + EntityBeanValueObject.getClassName(
			objectName) + " value)");
		writeLine("\t\tthrows CreateException;");
	}

	/** Output method - writes the home interface finders. */
	private void writeFinders() throws IOException
	{
		// Start the finder section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tFinder methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");
		writeLine();

		writeFindByPrimaryKey();
			writeLine();
		writeFindAll();
	}

	/** Output method - writes the home interface "findByPrimaryKey" finder. */
	private void writeFindByPrimaryKey() throws IOException
	{
		String strName = getObjectName();
		String strPrimaryKeyClassName = EntityBeanPrimaryKey.getClassName(strName);

		// Write comments for findByPrimaryKey.
		writeLine("\t/** Finder method - finds the " + getObjectName() +
			" entity bean");
		writeLine("\t    by its primary key.");
		writeLine("\t\t@param primaryKey the " + strName + " primary key object.");
		writeLine("\t*/");

		// Write the method.
		writeLine("\tpublic " + strName + " findByPrimaryKey(" +
			strPrimaryKeyClassName + " primaryKey)");
		writeLine("\t\tthrows FinderException;");
	}

	/** Output method - writes the home interface "findAll" finder. */
	private void writeFindAll() throws IOException
	{
		// Write comments for findByPrimaryKey.
		writeLine("\t/** Finder method - finds all " + getObjectName() +
			" entity beans. */");

		// Write the method.
		writeLine("\tpublic Collection findAll()");
		writeLine("\t\tthrows FinderException;");
	}

	/** Output method - write the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

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
		@param strArg7 package name of the home interfaces.
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
			EntityBeanHome pGenerator =
				new EntityBeanHome((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanHome.class.getName() + " Output directory");
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
