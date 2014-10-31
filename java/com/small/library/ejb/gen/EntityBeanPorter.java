package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates a factory class that performs a narrow on remote stubs. The porter
*	factory is generated from metadata of the tables that it represents.
*	The metadata is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	Each call to <CODE>generate</CODE> simply outputs the method call
*	necessary to return the remote interface represented by the current table.
*	The framework of the class has to be provided the caller.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 1/8/2003
*
***************************************************************************************/

public class EntityBeanPorter extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanPorter() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanPorter(PrintWriter pWriter,
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
	public EntityBeanPorter(PrintWriter pWriter,
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
		String strObjectName = getObjectName();
		String strClassName = EntityBeanRemote.getClassName(strObjectName);
		String strTableName = getTable().getName();

		writeLine();
		writeLine("\t/** Narrow method - narrows a remote stub to get a reference to the " +
			strObjectName);
		writeLine("\t    remote interface.");
		writeLine("\t\t@param remoteStub Remote stub returned within a collection of a home");
		writeLine("\t\t\tinterface finder.");
		writeLine("\t*/");
		writeLine("\tpublic static " + strClassName + " narrow" + strClassName +
			"(Object remoteStub)");
		writeLine("\t{");
		writeLine("\t\treturn (" + strClassName + ") PortableRemoteObject.narrow(" +
			"remoteStub, " + strClassName + ".class);");
		writeLine("\t}");
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
		return null;
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

	/** Output method - writes the file header.
		@param out Writer to the home factory class file.
		@param strPackageName package name of the home factory class.
		@param strHomeInterfacePackageName package name of the Entity Bean's
			home interface.
		@param strAuthor Author's name to use in the comments of the class's header.
		@param strDate Date string to use in the comments of the class's header.
	*/
	public static void writeHeader(PrintWriter out,
		String strPackageName, String strAuthor, String strDate)
			throws IOException
	{
		if (null != strPackageName)
		{
			out.println("package " + strPackageName + ";");
			out.println();
		}

		out.println("import javax.rmi.PortableRemoteObject;");

		out.println();
		out.println("/**********************************************************************************");
		out.println("*");
		out.println("*\tEntity Bean porter factory. Performs the <CODE>PortableRemoteObject.narrow</CODE>");
		out.println("*\toperation on remote interface stubs.");
		out.println("*");

		if (null != strAuthor)
			out.println("*\t@author " + strAuthor);

		out.println("*\t@version 1.0.0.0");

		if (null != strDate)
			out.println("*\t@date " + strDate);

		out.println("*");
		out.println("**********************************************************************************/");
	}

	/** Output method - writes the class declaration.
		@param out Writer to the porter class file.
		@param strClassName class name of the home factory.
	*/
	public static void writeClassDeclaration(PrintWriter out,
		String strClassName) throws IOException
	{
		out.println();
		out.println("public class " + strClassName);
		out.println("{");
	}

	/** Output method - writes the narrow methods header.
		@param out Writer to the porter class file.
	*/
	public static void writeNarrowMethodsHeader(PrintWriter out)
	{
		out.println("\t/***********************************************************************************");
		out.println("\t*");
		out.println("\t*	Narrow methods");
		out.println("\t*");
		out.println("\t***********************************************************************************/");
	}

	/** Output method - writes the class footer.
		@param out Writer to the home factory class file.
	*/
	public static void writeFooter(PrintWriter out) throws IOException
	{
		out.println("}");
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
		@param strArg7 package name of the home factory class.
		@param strArg8 Home factory class name.
		@param strArg9 Optional schema name to filter by.
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
			String strClassName = extractArgument(strArgs, 7, "EntityBeanPorter");

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 8);
			pTables.load();

			// Create the writer.
			PrintWriter writer = new PrintWriter(new FileWriter(
				new File(fileOutputDir, strClassName + ".java")));

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanPorter pGenerator =
				new EntityBeanPorter(writer, strAuthor,
				(Tables.Record) null);

			// Start the java class.
			writeHeader(writer, strPackageName,
				strAuthor, pGenerator.getDateString());
			writeClassDeclaration(writer, strClassName);
			writeNarrowMethodsHeader(writer);

			// Loop through the tables and generate each factory method.
			for (int i = 0; i < pTables.size(); i++)
			{
				Tables.Record table = (Tables.Record) pTables.item(i);

				pGenerator.setTable(table);
				pGenerator.generate();

				writer.flush();
			}

			// End the java class file.
			writeFooter(writer);

			// Close the file.
			writer.close();
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityBeanPorter.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Porter Class Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
