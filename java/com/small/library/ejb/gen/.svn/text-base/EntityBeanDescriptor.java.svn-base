package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates deployment descriptor for EJB Entity Bean. The deployment descriptors
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/15/2002
*
***************************************************************************************/

public class EntityBeanDescriptor extends EntityBeanBase
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
	public EntityBeanDescriptor() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanDescriptor(PrintWriter pWriter,
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
	public EntityBeanDescriptor(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable, strPackageName);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean deployment descriptor. Each method
	    call only generates the tag information within the "entity" tag, including
	    the opening "entity" tag and the terminating "entity" tag.
	*/
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();

		writeBeanInfo();
		writeFieldInfo();

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
		return createObjectName(pTable.getName()) + ".xml";
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
		String strPackageName = getPackageName();

		if (null == strPackageName)
			strPackageName = "";
		else
			strPackageName+= ".";

		writeLine("\t\t\t<description>");
			writeLine("\t\t\t\tEntity Bean that represents the " + getTable().getName() +
				" table.");
		writeLine("\t\t\t</description>");
		writeLine("\t\t\t<ejb-name>" + strObjectName + "</ejb-name>");
		writeLine("\t\t\t<local-home>" + strPackageName +
			EntityBeanHome.getClassName(strObjectName) + "</local-home>");
		writeLine("\t\t\t<local>" + strPackageName +
			EntityBeanRemote.getClassName(strObjectName) + "</local>");
		writeLine("\t\t\t<ejb-class>" + strPackageName +
			EntityBeanCMP.getClassName(strObjectName) + "</ejb-class>");
		writeLine("\t\t\t<persistence-type>Container</persistence-type>");
		writeLine("\t\t\t<prim-key-class>" + strPackageName +
			EntityBeanPrimaryKey.getClassName(strObjectName) + "</prim-key-class>");
		writeLine("\t\t\t<reentrant>False</reentrant>");
		writeLine("\t\t\t<cmp-version>2.x</cmp-version>");
		writeLine("\t\t\t<abstract-schema-name>" + strObjectName + "</abstract-schema-name>");
	}

	/** Output method - writes the class declaration. */
	private void writeFieldInfo() throws IOException
	{
		writeLine();

		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t\t\t<cmp-field>");
				writeLine("\t\t\t\t<description>Represents the \"" +
					m_ColumnInfo[i].columnName + "\" field.</description>");
				writeLine("\t\t\t\t<field-name>" + m_ColumnInfo[i].memberVariableName +
					"</field-name>");
			writeLine("\t\t\t</cmp-field>");
		}

		/**
			DO NOT OUTPUT: the generated classes use a Primary Key class,
			which have fields that match the type and name of the CMP
			class.
		if (null != m_colPrimaryKeyField)
			writeLine("\t\t\t<primkey-field>" + m_colPrimaryKeyField +
				"</primkey-field>");
		**/
	}

	/** Output method - writes the descriptor footer. */
	private void writeFooter() throws IOException
	{
		writeLine("\t\t</entity>");
	}

	/******************************************************************************
	*
	*	Static generators
	*
	******************************************************************************/

	/** Generator method - generates the method permission's section of the
	    assembly descriptor.
		@param writer Writes to the the assembly descriptor section.
		@param tables Contains the list of tables used in the descriptor.
		@param roleName Role name given access to each table's entity bean.
	*/
	public static void generateMethodPermissions(PrintWriter writer,
		Tables tables, String roleName)
			throws IOException
	{
		generateMethodPermissions(writer, tables, new String[] { roleName });
	}

	/** Generator method - generates the method permission's section of the
	    assembly descriptor.
		@param writer Writes to the the assembly descriptor section.
		@param tables Contains the list of tables used in the descriptor.
		@param roleNames Role names given access to each table's entity bean.
	*/
	public static void generateMethodPermissions(PrintWriter writer,
		Tables tables, String[] roleNames)
			throws IOException
	{
		writer.println("\t\t<method-permission>");

		for (int i = 0; i < roleNames.length; i++)
			writer.println("\t\t\t<role-name>" + roleNames[i] + "</role-name>");

		generateMethodSections(writer, tables, null);

		writer.println("\t\t</method-permission>");
	}

	/** Generator method - generates the container transaction's section of the
	    assembly descriptor.
		@param writer Writes to the the assembly descriptor section.
		@param tables Contains the list of tables used in the descriptor.
		@param transAttribute The Transaction Attribute used by each table's
			entity bean.
		@param interfaceType Home or Remote. NULL removes the method-intf element.
	*/
	public static void generateContainerTransactions(PrintWriter writer,
		Tables tables, String transAttribute, String interfaceType)
			throws IOException
	{
		writer.println("\t\t<container-transaction>");

		generateMethodSections(writer, tables, interfaceType);

			writer.println("\t\t\t<trans-attribute>" + transAttribute + "</trans-attribute>");
		writer.println("\t\t</container-transaction>");
	}

	/** Generator method - generates the method sections of the an
	    assembly descriptor section for each table.
		@param writer Writes to the the assembly descriptor section.
		@param tables Contains the list of tables used in the descriptor.
		@param interfaceType Home or Remote. NULL removes the method-intf element.
	*/
	public static void generateMethodSections(PrintWriter writer,
		Tables tables, String interfaceType)
			throws IOException
	{
		for (int i = 0; i < tables.size(); i++)
		{
			Tables.Record table = (Tables.Record) tables.item(i);
			String strObjectName = createObjectName(table.getName());

			writer.println("\t\t\t<method>");
				writer.println("\t\t\t\t<ejb-name>" + strObjectName +
					"</ejb-name>");
				if (null != interfaceType)
				{
					writer.print("\t\t\t\t<method-intf>");
					writer.print(interfaceType);
					writer.println("</method-intf>");
				}
				writer.println("\t\t\t\t<method-name>*</method-name>");
			writer.println("\t\t\t</method>");
		}
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
		@param strArg6 author of the generated file. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 package name of the Entity Bean classes and interfaces.
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

			// Get the output writer.
			File fileOutput = new File(fileOutputDir, "ejb-jar.xml");
			PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

			// Create the Deployment Descriptor generator.
			EntityBeanDescriptor pGenerator =
				new EntityBeanDescriptor(writer, strAuthor,
				(Tables.Record) null, strPackageName);

			// Start the deployment descriptor.
			writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			writer.println();
			writer.println("<!DOCTYPE ejb-jar");
			writer.println("\tPUBLIC \"-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN\"");
			writer.println("\t\"http://java.sun.com/dtd/ejb-jar_2_0.dtd\">");
			writer.println();
			writer.println("<ejb-jar>");
			writer.println("\t<description>Description</description>");
			writer.println("\t<display-name>Description</display-name>");
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

			// Write the assembly descriptor section.
			writer.println();
			writer.println("\t<assembly-descriptor>");
				writer.println();
				generateMethodPermissions(writer, pTables, "Everyone");
				writer.println();
				generateContainerTransactions(writer, pTables, "Supports",
					"Remote");
				generateContainerTransactions(writer, pTables, "Required",
					"Home");
				writer.println();
			writer.println("\t</assembly-descriptor>");

			// End the deployment descriptor.
			writer.println();
			writer.println("</ejb-jar>");

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

			System.out.println("Usage: java " + EntityBeanDescriptor.class.getName() + " Output directory");
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
