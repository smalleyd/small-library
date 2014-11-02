package com.small.library.atg.gen;

import java.io.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generator class for Dynamo SQL Repository configuration files. The
*	configuration files are simple Java property files that reside in one
*	of the directories of the Dynamo CONFIGPATH. The directory structure
*	within the CONFIGPATH acts as the namespace for invoking the component
*	that a configuration file represents.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 6/21/2002
*
***************************************************************************************/

public class SQLRepositoryItemConfig extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - default SQL Repository implementation class. */
	public static final String DEFAULT_IMPLEMENTATION_CLASS = "atg.adapter.gsa.GSARepository";

	/** Constant - default XML Tools Factory. */
	public static final String DEFAULT_XML_TOOLS_FACTORY = "/atg/dynamo/service/xml/XMLToolsFactory";

	/** Constant - default Transaction Manager. */
	public static final String DEFAULT_TRANSACTION_MANAGER = "/atg/dynamo/transaction/TransactionManager";

	/** Constant - default ID Generator. */
	public static final String DEFAULT_ID_GENERATOR = "/atg/dynamo/service/IdGenerator";

	/** Constant - default Lock Manager. */
	public static final String DEFAULT_LOCK_MANAGER = "/atg/dynamo/service/ClientLockManager";

	/** Constant - default Dynamo Data Source. */
	public static final String DEFAULT_DYNAMO_DATA_SOURCE = "/atg/dynamo/service/jdbc/JTDataSource";

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public SQLRepositoryItemConfig() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strServiceDirectory Absolute path under a CONFIGPATH that
			contains the Repository Item Descriptor.
		@param strDynamoDataSource ATG Dynamo Nucleus name of the JDBC
			data source.
	*/
	public SQLRepositoryItemConfig(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable,
		String strServiceDirectory, String strDynamoDataSource)
	{
		super(pWriter, strAuthor, pTable);

		m_strServiceDirectory = strServiceDirectory;
		m_strDynamoDataSource = strDynamoDataSource;
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the SQL Repository Item Descriptor. */
	public void generate() throws GeneratorException, IOException
	{
		Tables.Record pTable = getTable();

		write("$class=");
			writeLine(getImplementationClass());
		write("$description=");
			write(pTable.getName());
			writeLine(" Entity SQL to object mapping");

		writeLine();

		write("repositoryName=");
			write(getObjectName());
			writeLine("Repository");
		write("dataSource=");
			writeLine(getDynamoDataSource());
		write("definitionFiles=");
			write(getServiceDirectory());
			write("/");
			write(pTable.getName());
			writeLine("Repository.xml");
		write("XMLToolsFactory=");
			writeLine(getXMLToolsFactory());
		write("transactionManager=");
			writeLine(getTransactionManager());
		write("idGenerator=");
			writeLine(getIDGenerator());
		write("lockManager=");
			writeLine(getLockManager());

		// Write subclass specific properties
		writeOtherProperties();
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
		return pTable.getName() + "Repository.properties";
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Helper method - writes properties not required. A subclass would override
	    this method to add subclass specific properties.
	*/
	protected void writeOtherProperties() throws GeneratorException, IOException
	{}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the absolute path under a CONFIGPATH that
	    contains the Repository Item Descriptor.
	*/
	public String getServiceDirectory()
	{ return m_strServiceDirectory; }

	/** Accessor method - gets the ATG Dynamo Nucleus name of the JDBC
	    data source.
	*/
	public String getDynamoDataSource() { return m_strDynamoDataSource; }

	/** Accessor method - gets the default Repository Implementation Class. A subclass can
	    override this method to proivde a different value.
	*/
	public String getImplementationClass() { return DEFAULT_IMPLEMENTATION_CLASS; }

	/** Accessor method - gets the default XML Tools Factory. A subclass can
	    override this method to proivde a different value.
	*/
	public String getXMLToolsFactory() { return DEFAULT_XML_TOOLS_FACTORY; }

	/** Accessor method - gets the default Transaction Manager. A subclass can
	    override this method to proivde a different value.
	*/
	public String getTransactionManager() { return DEFAULT_TRANSACTION_MANAGER; }

	/** Accessor method - gets the default ID Generator. A subclass can
	    override this method to proivde a different value.
	*/
	public String getIDGenerator() { return DEFAULT_ID_GENERATOR; }

	/** Accessor method - gets the default Lock Manager. A subclass can
	    override this method to proivde a different value.
	*/
	public String getLockManager() { return DEFAULT_LOCK_MANAGER; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the absolute path under a CONFIGPATH that
	    contains the Repository Item Descriptor.
	*/
	public void setServiceDirectory(String strNewValue)
	{ m_strServiceDirectory = strNewValue; }

	/** Mutator method - sets the ATG Dynamo Nucleus name of the JDBC
	    data source.
	*/
	public void setDynamoDataSource(String strNewValue)
	{ m_strDynamoDataSource = strNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - reference to the absolute path under a CONFIGPATH that
	    contains the Repository Item Descriptor.
	*/
	private String m_strServiceDirectory = null;

	/** Member variable - reference to the ATG Dynamo Nucleus name of the JDBC
	    data source.
	*/
	private String m_strDynamoDataSource = null;

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
		@param strArg7 repository path
		@param strArg8 Dynamo Data Source path
		@param strArg9 Schema name pattern
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
			String strServiceDirectory = extractArgument(strArgs, 6, "");
			String strDynamoDataSource = extractArgument(strArgs, 7, DEFAULT_DYNAMO_DATA_SOURCE);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			SQLRepositoryItemConfig pGenerator =
				new SQLRepositoryItemConfig((PrintWriter) null,
					strAuthor, (Tables.Record) null,
					strServiceDirectory, strDynamoDataSource);

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

			System.out.println("Usage: java " + SQLRepositoryItemConfig.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Service Directory of Repository Item Descriptor]");
			System.out.println("\t[Dynamo Data Source]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
