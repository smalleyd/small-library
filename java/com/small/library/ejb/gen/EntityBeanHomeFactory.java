package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates a home factory for the EJB Entity Bean home interfaces. The home
*	interface factory is generated from metadata of the tables that it represents.
*	The metadata is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	Each call to <CODE>generate</CODE> simply outputs the method call
*	necessary to return the home interface represented by the current table.
*	The framework of the class has to be provided the caller.
*
*	@author David Small
*	@version 1.0.0.0
*	@since 7/18/2002
*
***************************************************************************************/

public class EntityBeanHomeFactory extends EntityBeanBase
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
	public EntityBeanHomeFactory() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanHomeFactory(PrintWriter pWriter,
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
	public EntityBeanHomeFactory(PrintWriter pWriter,
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
		String strClassName = EntityBeanHome.getClassName(strObjectName);
		String strTableName = getTable().getName();

		writeLine();
		writeLine("\t/** Factory method - gets a reference to the " +
			strObjectName);
		writeLine("\t    home interface.");
		writeLine("\t*/");
		writeLine("\tpublic " + strClassName + " get" + strClassName +
			"()");
		writeLine("\t\tthrows NamingException");
		writeLine("\t{");
		writeLine("\t\treturn (" + strClassName + ") getHomeInterface(JNDI_NAME_" +
			strTableName.toUpperCase() + ", " + strClassName + ".class);");
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
		String strPackageName, String strHomeInterfacePackageName,
		String strAuthor, String strDate)
			throws IOException
	{
		if (null != strPackageName)
		{
			out.println("package " + strPackageName + ";");
			out.println();
		}

		out.println("import javax.naming.*;");
		out.println();
		out.println("import com.small.library.ejb.util.AbstractHomeFactory;");

		if (null != strHomeInterfacePackageName)
		{
			out.println();
			out.println("import " + strHomeInterfacePackageName + ";");
		}

		out.println();
		out.println("/**********************************************************************************");
		out.println("*");
		out.println("*\tEntity Bean home interface factory.");
		out.println("*");

		if (null != strAuthor)
			out.println("*\t@author " + strAuthor);

		out.println("*\t@version 1.0.0.0");

		if (null != strDate)
			out.println("*\t@since " + strDate);

		out.println("*");
		out.println("**********************************************************************************/");
	}

	/** Output method - writes the class declaration.
		@param out Writer to the home factory class file.
		@param strClassName class name of the home factory.
	*/
	public static void writeClassDeclaration(PrintWriter out,
		String strClassName) throws IOException
	{
		out.println();
		out.println("public class " + strClassName + " extends AbstractHomeFactory");
		out.println("{");
	}

	/** Output method - writes the JNDI constants.
		@param out Writer to the home factory class file.
		@param tables Collection of tables represented by entity beans.
	*/
	public static void writeJndiConstants(PrintWriter out,
		Tables tables)
	{
		out.println("\t/***********************************************************************************");
		out.println("\t*");
		out.println("\t*	Constants - JNDI names");
		out.println("\t*");
		out.println("\t***********************************************************************************/");
		out.println();
		out.println("\t/** Constant - JNDI name prefix for entity beans. */");
		out.println("\tpublic static final String JNDI_PREFIX_ENTITY = \"ejb/entities\";");

		for (int i = 0; i < tables.size(); i++)
		{
			Tables.Record table = (Tables.Record) tables.item(i);
			String tableName = table.getName();
			String objectName = createObjectName(tableName);
			
			out.println();
			out.println("\t/** Constant - JNDI name for the entity, " + tableName + " */");
			out.println("\tpublic static final String JNDI_NAME_" +
				tableName.toUpperCase() + " = JNDI_PREFIX_ENTITY + \"/" +
				objectName + "Home\";");
		}
	}

	/** Output method - writes the constructor.
		@param out Writer to the home factory class file.
		@param strClassName class name of the home factory.
	*/
	public static void writeConstructors(PrintWriter out, String strClassName)
	{
		// Write the constructor.
		out.println();
		out.println("\t/***********************************************************************************");
		out.println("\t*");
		out.println("\t*	Constructors");
		out.println("\t*");
		out.println("\t***********************************************************************************/");
		out.println();
		out.println("\t/** Constructor - constructs a populated object.");
		out.println("\t\t@param context JNDI <I>Context</I> object used to located");
		out.println("\t\t\tthe EJB Home interfaces. Should be direct parent");
		out.println("\t\t\tto the EJB home interfaces.");
		out.println("\t*/");
		out.println("\tpublic " + strClassName + "(Context context)");
		out.println("\t{");
		out.println("\t\tsuper(context);");
		out.println("\t}");
	}

	/** Output method - writes the factory methods header.
		@param out Writer to the home factory class file.
	*/
	public static void writeFactoryMethodsHeader(PrintWriter out)
	{
		out.println();
		out.println("\t/***********************************************************************************");
		out.println("\t*");
		out.println("\t*	Factory methods");
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
			String strClassName = extractArgument(strArgs, 7, "EJBHomeFactory");

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 8);
			pTables.load();

			// Create the writer.
			PrintWriter writer = new PrintWriter(new FileWriter(
				new File(fileOutputDir, strClassName + ".java")));

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanHomeFactory pGenerator =
				new EntityBeanHomeFactory(writer, strAuthor,
				(Tables.Record) null);

			// Start the java class.
			writeHeader(writer, strPackageName, null,
				strAuthor, pGenerator.getDateString());
			writeClassDeclaration(writer, strClassName);
			writeJndiConstants(writer, pTables);
			writeConstructors(writer, strClassName);
			writeFactoryMethodsHeader(writer);

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

			System.out.println("Usage: java " + EntityBeanHomeFactory.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Home Factory Class Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
