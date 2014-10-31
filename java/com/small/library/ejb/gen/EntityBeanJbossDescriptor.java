package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates jBoss specific deployment descriptor for EJB Entity Bean. The deployment descriptors
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/15/2002
*
***************************************************************************************/

public class EntityBeanJbossDescriptor extends EntityBeanBase
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
	public EntityBeanJbossDescriptor() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanJbossDescriptor(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strJndiContext Context container to the JNDI name for
			the entity bean.
	*/
	public EntityBeanJbossDescriptor(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strJndiContext)
	{
		super(pWriter, strAuthor, pTable, null);

		m_strJndiContext = strJndiContext;
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean jBoss descriptor. Each method
	    call only generates the tag information within the "entity" tag, including
	    the opening "entity" tag and the terminating "entity" tag.
	*/
	public void generate() throws GeneratorException, IOException
	{
		writeHeader();

		writeBeanInfo();

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
		String strJndiContext = getJndiContext();

		if (null == strJndiContext)
			strJndiContext = "";
		else
			strJndiContext+= "/";

		writeLine("\t\t\t<ejb-name>" + strObjectName + "</ejb-name>");
		writeLine("\t\t\t<jndi-name>" + strJndiContext + strObjectName +
			"</jndi-name>");
	}

	/** Output method - writes the descriptor footer. */
	private void writeFooter() throws IOException
	{
		writeLine("\t\t</entity>");
	}

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the context container for the JNDI name of an
	    entity bean.
	*/
	public void setJndiContext(String newValue) { m_strJndiContext = newValue; }

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the context container for the JNDI name of an
	    entity bean.
	*/
	public String getJndiContext() { return m_strJndiContext; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - reference to the context container for the JNDI name
	    of an entity bean.
	*/
	private String m_strJndiContext = null;

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
		@param strArg7 context container to the JNDI name.
		@param strArg8 schema name filter pattern.
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
			String strJndiContext = extractArgument(strArgs, 6, null);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 7);
			pTables.load();

			// Get the output writer.
			File fileOutput = new File(fileOutputDir, "jboss.xml");
			PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

			// Create the Deployment Descriptor generator.
			EntityBeanJbossDescriptor pGenerator =
				new EntityBeanJbossDescriptor(writer, strAuthor,
				(Tables.Record) null, strJndiContext);

			// Start the deployment descriptor.
			writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			writer.println();
			writer.println("<jboss>");
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
			writer.println("</jboss>");

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

			System.out.println("Usage: java " + EntityBeanJbossDescriptor.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[JNDI Context]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
