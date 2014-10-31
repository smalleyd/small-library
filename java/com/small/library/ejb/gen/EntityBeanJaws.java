package com.small.library.ejb.gen;

import java.io.*;
import java.lang.reflect.Method;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates JAWS descriptor for EJB Entity Bean. The JAWS descriptors
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/15/2002
*
***************************************************************************************/

public class EntityBeanJaws extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanJaws() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param packageName Package name where the home interfaces are located.
	*/
	public EntityBeanJaws(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String packageName)
	{
		super(pWriter, strAuthor, pTable);

		this.packageName = packageName;
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean JAWS descriptor. Each method
	    call only generates the tag information within the "entity" tag, including
	    the opening "entity" tag and the terminating "entity" tag.
	*/
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();

		writeBeanInfo();
		writeFieldInfo();
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
		// Name should NOT have a suffix.
		return createObjectName("jaws.xml");
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

	/** Output method - starts the outer tag for the entity bean descriptor. */
	private void writeHeader() throws IOException
	{
		writeLine("\t\t<entity>");
	}

	/** Output method - writes the file header. */
	private void writeBeanInfo() throws IOException
	{
		String strObjectName = getObjectName();
		String strTableName = getTable().getName();

		writeLine("\t\t\t<ejb-name>" + strObjectName + "</ejb-name>");
		writeLine("\t\t\t<table-name>" + strTableName + "</table-name>");
	}

	/** Output method - writes the class declaration. */
	private void writeFieldInfo() throws IOException
	{
		writeLine();

		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t\t\t<cmp-field>");
				writeLine("\t\t\t\t<field-name>" + m_ColumnInfo[i].memberVariableName +
					"</field-name>");
				writeLine("\t\t\t\t<column-name>" + m_ColumnInfo[i].columnName +
					"</column-name>");
				writeLine("\t\t\t\t<jdbc-type>" + m_ColumnInfo[i].jdbcTypeString +
					"</jdbc-type>");
				writeLine("\t\t\t\t<sql-type>" + m_ColumnInfo[i].typeDefinition +
					"</sql-type>");
			writeLine("\t\t\t</cmp-field>");
		}
	}

	/** Output method - writes the finder declarations. */
	private void writeFinders() throws IOException
	{
		if (null == packageName)
			return;

		String className = EntityBeanHome.getClassName(getObjectName());
		className = packageName + "." + className;
		Class value = null;

		try
		{
			value = Class.forName(className);
		}

		catch (ClassNotFoundException ex)
		{
			System.out.println("WARN: Class, \"" + className + "\", not found.");
			return;
		}
		catch (NoClassDefFoundError ex)
		{
			System.out.println("WARN: Class, \"" + className + "\", not found.");
			return;
		}

		// Get the finders
		Method[] methods = value.getMethods();
		boolean firstTimeThru = true;

		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();

			if (!methodName.startsWith("find"))
				continue;
			if (methodName.equals("findByPrimaryKey"))
				continue;

			if (firstTimeThru)
			{
				writeLine();
				firstTimeThru = false;
			}

			writeLine("\t\t\t<finder>");
			writeLine("\t\t\t\t<name>" + methodName + "</name>");
			writeLine("\t\t\t\t<query></query>");
			writeLine("\t\t\t\t<order></order>");
			writeLine("\t\t\t</finder>");
		}
	}

	/** Output method - writes the descriptor footer. */
	private void writeFooter() throws IOException
	{
		writeLine();
		writeLine("\t\t\t<read-ahead>true</read-ahead>");
		writeLine("\t\t</entity>");
	}

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - package name where the home interfaces are located. */
	private String packageName = null;

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
		@param strArg6 author of the generated file. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 optional database schema name.
		@param strArg8 optional package name where the home interfaces
			are located. Used to build finder list.
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

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 6);
			pTables.load();

			// Get the package name.
			String packageName = extractArgument(strArgs, 7, null);

			// Get the output writer.
			File fileOutput = new File(fileOutputDir, "jaws.xml");
			PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

			// Create the Deployment Descriptor generator.
			EntityBeanJaws pGenerator =
				new EntityBeanJaws(writer, strAuthor,
				(Tables.Record) null, packageName);

			// Start the deployment descriptor.
			writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			writer.println();
			writer.println("<jaws>");
			writer.println();
			writer.println("\t<datasource></datasource>");
			writer.println("\t<type-mapping></type-mapping>");
			writer.println("\t<debug>false</debug>");
			writer.println();
			writer.println("\t<default-entity>");
			writer.println("\t\t<create-table>false</create-table>");
			writer.println("\t\t<remove-table>false</remove-table>");
			writer.println("\t\t<tuned-updates>true</tuned-updates>");
			writer.println("\t\t<read-only>false</read-only>");
			writer.println("\t\t<time-out>300</time-out>");
			writer.println("\t\t<select-for-update>false</select-for-update>");
			writer.println("\t</default-entity>");
			writer.println();
			writer.println("\t<enterprise-beans>");
			writer.println();

			// Buld the body of the deployment descriptor.
			for (int i = 0; i < pTables.size(); i++)
			{
				pGenerator.setTable((Tables.Record) pTables.item(i));
				pGenerator.generate();

				writer.flush();
			}

			// End the enterprise bean section.
			writer.println();
			writer.println("\t</enterprise-beans>");

			// End the deployment descriptor.
			writer.println();
			writer.println("</jaws>");

			// Close the writer.
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

			System.out.println("Usage: java " + EntityBeanJaws.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Schema Name Pattern]");
			System.out.println("\t[Package Name]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
