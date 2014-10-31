package com.small.library.generator;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;

import com.small.library.data.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates the data service object code for a particular table in a Java application.
*
*	@author Xpedior\Tomasz Piwowarski
*	@version 1.0.0.0
*	@date 4/04/2000
*
***************************************************************************************/

public class DataServiceObjectJava extends DataServiceObject
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant to map SQL datetime type to Java Timestamp type. */
	public static final int DATETIME = 11;
		
	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public DataServiceObjectJava() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public DataServiceObjectJava(PrintWriter pWriter, String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/*******************************************************************************************
	*
	*	Required methods: Base class
	*
	********************************************************************************************/

	/** Action method - generates the Java data service object. */
	public void generate()
		throws GeneratorException, IOException
	{
		try { prepareColumnInfo(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }

		// creates Data Collection portion. 
		writePackage("dsl");
		writeClassHeader("Data Collection");
		writeLine("public class " + getObjectName() + " extends DataCollectionExt implements IList");
		writeOpen();

		try { writeDataCollectionClass(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }

		// creates Data Record portion. 
		writeClassHeader("Data Record");
		writeLine("\tpublic static class Record extends DataRecord implements IListItem");
		writeLine("\t{");

		try { writeDataRecordClass(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }

		writeLine("\t}");
		writeClose();
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
		return getObjectName() + ".java";
	}

	/***************************************************************************************
	*
	*	Helper methods
	*
	***************************************************************************************/

	/** Prepares the column information. */
	public void prepareColumnInfo()
		throws SQLException
	{
		ColumnInfo[] columns = getColumnInfo();
		m_ListColumns = new ArrayList(columns.length);

		for (int i = 0; i < columns.length; i++)
		{
			ColumnInfo info = columns[i];

			if (!m_bIsAutoIncrementing)
				m_bIsAutoIncrementing = info.isAutoIncrementing;

			m_ListColumns.add(info);
		}
	}

	/** Opens a function. */	
	private void writeOpen() throws IOException { writeLine("{"); }

	/** Closes a function. */
	private void writeClose() throws IOException { writeLine("}"); }

	/** Creates start comments string. */
	private void writeStartComments() throws IOException
	{
		writeLine("/******************************************************************************");
	}

	/** Creates start comments string. */
	private void writeStartComments(String strValue) throws IOException
	{ write(strValue); writeStartComments(); }

	/** Creates end comments string. */
	private void writeEndComments() throws IOException
	{
		writeLine("******************************************************************************/");
	}

	/** Creates end comments string. */
	private void writeEndComments(String strValue) throws IOException
	{ write(strValue); writeEndComments(); }

	/** Creates a code section header. */
	private void writeSectionHeader(String strTitle) throws IOException
	{
		writeStartComments("\t");
		writeLine("\t*");
		writeLine("\t*\t" + strTitle);
		writeLine("\t*");
		writeEndComments("\t");
		writeLine();
	}

	/** Creates Package and import headers. */
	private void writePackage(String strValue) throws IOException
	{
		writeLine("package" + " " + strValue + ";");
		writeLine();
		writeLine("import" + " " + "java.sql.*;");
		writeLine();
		writeLine("import" + " " + "com.small.library.data.*;");
		writeLine("import" + " " + "com.small.library.rdbms.*;");
		writeLine("import" + " " + "com.small.library.html.*;");
	}

	/** Creates Author header. */
	private void writeClassHeader(String strType) throws IOException
	{
		java.util.Date pNow = new java.util.Date();

		writeLine();
		writeStartComments();
		writeLine("*");
		writeLine("*\t" + strType + " for the " + getTable().getName() + " table");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.0.0");
		writeLine("*\t@date " + pNow.toString());
		writeLine("*");
		writeEndComments();

		writeLine();
	}

	/** Creates Data Collection static members. */
	private void writeDataCollectionStaticMember() throws SQLException, IOException
	{	
		String strColumns = "";
		String strPrimaryKeys = "";
		Columns.Record pColumn = null;
		Columns pColumns = getColumns();
		PrimaryKeys pPrimaryKeys = getTable().getPrimaryKeys();
		PrimaryKeys.Record pPrimaryKey = null;

		pPrimaryKeys.load();

		writeSectionHeader("Constants");

		//creates TABLE_NAME static member.
		writeLine("\t/** Name of the table this data collection supports. */");
		writeLine("\tpublic static final String TABLE_NAME = \"" + getTable().getName() + "\";");
		writeLine();
		writeLine("\t/** SQL Statement. */");

		//creates SQL_QUERY static members.
		for (int i = 0; i < pColumns.size(); i++)
		{
			if (i > 0)
				strColumns+= ", ";

			pColumn = (Columns.Record) pColumns.item(i);
			strColumns += pColumn.getName();
		}

		writeLine("\tprivate static final String SQL_QUERY_SELECT = \"SELECT " + strColumns + "\";");
		writeLine("\tprivate static final String SQL_QUERY_FROM = \" FROM " + getTable().getName() + "\";");	
		writeLine("\tprivate static final String SQL_QUERY_WHERE_ID = \" WHERE " + pColumns.item(0) + " = ?\";");
		
		for (int i = 0; i < pPrimaryKeys.size(); i++)
		{
			if (i > 0)
				strPrimaryKeys+= ",";

			pPrimaryKey = (PrimaryKeys.Record) pPrimaryKeys.item(i);
			strPrimaryKeys += pPrimaryKey.getName();
		}

		writeLine("\tprivate static final String SQL_QUERY_ORDER_BY = \" ORDER BY " + strPrimaryKeys + "\";");		
	}

	/** Creates Data Collection constructors' comments and methods. */
	private void writeDataCollectionConstructor() throws IOException
	{
		// creates Constructor/Destructor comments.
		writeLine();
		writeSectionHeader("Constructors\\Destructor");

		// creates default constructor and commetns.
		writeLine("\t/** Default constructor. */");
		writeLine("\tpublic " + getObjectName() + "() { super(); }");
		writeLine();
	}

	/** Creates Data Collection main functionality comments and content. */
	private void writeDataCollectionMainFunctionality() throws SQLException, IOException
	{
		Columns.Record pColumn = null;
		Columns pColumns = getColumns();
		PrimaryKeys pPrimaryKeys = getTable().getPrimaryKeys();
		Indexes pIndexes = getTable().getIndexes();
		Indexes.Record pIndex= null;

		pPrimaryKeys.load();
		pIndexes.load();

		pColumn = (Columns.Record) pColumns.item(0);

		// creates Main functionality commments. 
		writeSectionHeader("Main functionality");

		// creates getResultSet method and comments.
		writeLine("\t/** Override the \"getResultSet\" method from DataCollection. */");
		writeLine("\tprotected ResultSet getResultSet() throws SQLException");
		writeLine("\t{");
		writeLine("\t\treturn getConnect().createStatement().executeQuery(getQueryString());");
		writeLine("\t}");
		writeLine();
		
		// creates getQueryString method and comments.
		writeLine("\t/** Override the \"getQueryString\" method from DataCollectionProc. */");
		writeLine("\tprotected String getQueryString()");
		writeLine("\t{");
		writeLine("\t\treturn SQL_QUERY_SELECT + SQL_QUERY_FROM + SQL_QUERY_ORDER_BY;");
		writeLine("\t}");
		writeLine();
		
		// creates getTableName() method and comments. 
		writeLine("\t/** Override the \"getTableName\" method from DataCollectionProc. */");
		writeLine("\tprotected String getTableName() { return TABLE_NAME; }");
		writeLine();

		String strAutoIncrement = "";

		if (m_bIsAutoIncrementing)
			strAutoIncrement = "true";
		else
			strAutoIncrement = "false";
	
		// creates getHasAutoIncrement() method and comments. 
		writeLine("\t/** Indicates whether the represented database object contains an auto incremented field.");
		writeLine("\t\t@return Returns an integer.");
		writeLine("\t*/");
		writeLine("\tprotected boolean getHasAutoIncrement() { return " + strAutoIncrement + ";	}");
		writeLine();

		// creates getUpdateParams() method and comments. 
		int nUpdateParams = 0;
		writeLine("\t/** Returns an integer representing  the number of parameters needed for UPADATE stored procedure.");
		writeLine("\t\t@return Returns and integer.");
		writeLine("\t*/");
		nUpdateParams = pColumns.size();
		writeLine("\tprotected int getUpdateParams() { return " + nUpdateParams + "; }");
		writeLine();

		// creates getInsertParams() method and comments. 
		String strInsertParams = "";
		writeLine("\t/*Returns and integer representing the number of parameters needed for the INSERT stored procedur.");
		writeLine("\t\t@return Returns an integer.");
		writeLine("\t*/");

		if (m_bIsAutoIncrementing)
			strInsertParams = "getUpdateParams() - 1";
		else
			strInsertParams = "getUpdateParams()";

		writeLine("\tprotected int getInsertParams() { return " + strInsertParams + "; }");
		writeLine();

		// creates getDeleteParams() method and comments. 
		int nDeleteParams = 0;
		writeLine("\t/**Returns and integer representing the number of parameters needed for the DELETE stored procedur.");
		writeLine("\t\t@return Returns an integer.");
		writeLine("\t*/");
		nDeleteParams = pPrimaryKeys.size();
		writeLine("\tprotected int getDeleteParams() { return " + nDeleteParams + "; }");
		writeLine();

		//	creates getDuplicateParams method and comments. 
		int nDuplicateParams = 0;
		writeLine("\t/**Returns and integer representing the number of parameters needed for the IS_DUPLICATE stored procedur.");
		writeLine("\t\t@return Returns an integer.");
		writeLine("\t*/");

		for (int i = 0; i < pIndexes.size(); i++)
		{
			pIndex = (Indexes.Record) pIndexes.item(i);
			if (pIndex.isUnique())
				nDuplicateParams++;
			
		}

		writeLine("\tprotected int getDuplicateParams() { return " + nDuplicateParams + "; }");
		writeLine();

		// creates  newRecord() method and comments. 
		writeLine("\t/** Overrides the \"new Record\" abstract method of DataColleciton.");
		writeLine("\t\t@return Returns a DataRecord object of the type this collection supports.");
		writeLine("\t*/");
		writeLine("\tpublic DataRecord newRecord() { return (DataRecord) new Record(); }");
		writeLine();

		// creates getListCount() method and comments. 
		writeLine("\t/** Overrides the \"IList\" method. */");
		writeLine("\tpublic int getListCount() { return size(); }");
		writeLine();

		//creates getListItem() method and comments.
		writeLine("\t/** Overrides the \"IList\" method. */");
		writeLine("\tpublic IListItem getListItem(int nItem) { return (IListItem) item(nItem); }");
	}

	/** creates Data Record update method. */
	private void writeDataRecordUpdate() throws IOException, SQLException
	{	
		int counter = 1;
		Columns pColumns = getColumns();

		writeLine("\t\t/** Overrides the parent \"update\" method to provide more specific functionality.");
		writeLine("\t\t\t@param pStmt Reference to a JDBC PreparedStatement object.");
		writeLine("\t\t*/");
		
		writeLine("\t\tpublic void update(PreparedStatement pStmt) throws SQLException");
		writeLine("\t\t{");
		writeLine("\t\t\t//Is new record dirty or new?");
		writeLine("\t\t\tif (isClean())");
		writeLine("\t\t\t\treturn;");
		writeLine();
		writeLine("\t\t\tif (!isNewRecord())");
		writeLine("\t\t\t\tpStmt.setInt(1, getID());");
		writeLine();

		for (int i = 1; i < pColumns.size(); i++)
		{
			ColumnInfo pColumnInfo = (ColumnInfo) m_ListColumns.get(i);
			writeLine("\t\t\tpStmt.set" + pColumnInfo.jdbcMethodSuffix + "(" + (i + 1) + ", get" + pColumnInfo.name + "());");
		}

		writeLine();
		writeLine("\t\t\tsuper.update(pStmt);");
		writeLine();
		writeLine("\t\t\t//execute the prepared statement");
		writeLine("\t\t\tpStmt.executeUpdate();");
		writeLine();
		writeLine("\t\t\tclean();");
		writeLine("\t\t}");
		writeLine();
	}
	
	/** creates Data Record fetch method. */
	private void writeDataRecordFetch() throws SQLException, IOException
	{
		Columns pColumns = getColumns();

		writeLine("\t\t/** Overrides the parent \"fetch\" method to provide more specific functionality.");
		writeLine("\t\t\t@param pStmt Reference to a JDBC PreparedStatement object.");
		writeLine("\t\t*/");

		writeLine("\t\tpublic void fetch(ResultSet pResultSet) throws SQLException");
		writeLine("\t\t{");

		for (int i = 0; i < pColumns.size(); i++)
		{
			ColumnInfo pColumnInfo = (ColumnInfo) m_ListColumns.get(i);
			writeLine("\t\t\t" + pColumnInfo.memberVariableName + " = pResultSet.get" + pColumnInfo.jdbcMethodSuffix + "(" + (i + 1) + ");");
		}

		writeLine();
		writeLine("\t\t\tsuper.fetch(pResultSet);");
		writeLine();
		writeLine("\t\t\tclean();");
		writeLine("\t\t}");
		writeLine();
	}

	/** creates Data Record insert() method */
	private void writeDataRecordInsert() throws IOException
	{
		writeLine("\t\t/** Overrides the parent \"insert\" method to provide more specific functionality.");
		writeLine("\t\t@param Reference to a JDBC Prepared Object.");
		writeLine("\t\t*/");
		writeLine("\t\tpublic void insert(PreparedStatement pStmt) throws SQLException");
		writeLine("\t\t{");
		writeLine("\t\t\tif(!isNewRecord()) return;");
		writeLine();

		if (m_bIsAutoIncrementing)
		{
			writeLine("\t\t\tCallableStatement pCall = (CallableStatement) pStmt;");
			writeLine("\t\t\tpCall.registerOutParameter(1, java.sql.Types.INTEGER);");
			writeLine("\t\t\tupdate(pCall);");
			writeLine("\t\t\tm_nID = pCall.getInt(1);");
		}

		else
			writeLine("\t\t\tupdate(pStmt);");

		writeLine("\t\t}");
		writeLine();
	}

	/** creates Data Record bindID method. */
	private void writeDataRecordBindID() throws IOException
	{
		writeLine("\t\t/** Overrides the parent \"bindID\" method to provide more specific functionality.");
		writeLine("\t\t\t@param pStmt Reference to a JDBC PreparedStatement object.");
		writeLine("\t\t\t@param nParam The paramenter position to bind the primary key value to.");
		writeLine("\t\t\t@return Returns teh next position for the calling code to use.");
		writeLine("\t\t*/");
		writeLine("\t\tpublic int bindID(PreparedStatement pStmt, int nParam) throws SQLException");
		writeLine("\t\t{ pStmt.setInt(nParam, getID()); return	(nParam + 1); }");
		writeLine();
	}

	/** creates Data Record bindDesc method. */
	private void writeDataRecordBindDesc() throws IOException
	{
		writeLine("\t\t/** Overriedes the aprent \"bindDesc\" method to provide more specific functionality.");
		writeLine("\t\t\t@paramp pStmt Reference to a JDBC PreparedStatement object.");
		writeLine("\t\t\t@param nParam The paramenter position to bind the unique descriptor to.");
		writeLine("\t\t\t@return Returns teh next position for the calling code to use.");
		writeLine("\t\t*/");
		writeLine("\t\tpublic int bindDesc(PreparedStatement pStmt, int nParam) throws SQLException");
		writeLine("\t\t{");
		writeLine("\t\t\tpStmt.setString(nParam, getDesc());");
		writeLine("\t\t\treturn	(nParam + 1);");
		writeLine("\t\t}");
		writeLine();
	}

	/** creates toString method. */
	private void writeDataRecordToString() throws IOException
	{
		writeLine("\t\t/** Converts the ID property to a String. */");
		writeLine("\t\tpublic String toString() { return \"\" + getID(); }");
		writeLine();
	}

	/** creates hashCode method. */
	private void writeDataRecordHashCode() throws IOException
	{
		writeLine("\t\t/** Returns the ID property. */");
		writeLine("\t\tpublic int hashCode()	{ return getID(); }");
		writeLine();
	}

	/** creates  Mutator for Data Record. */
	private void writeDataRecordAccessor(ColumnInfo pColumnInfo) throws IOException
	{	
		writeLine("\t\t/** Retrieves the " + pColumnInfo.name + " property */");
		writeLine("\t\tpublic " + pColumnInfo.javaType + " get" + pColumnInfo.name + "() { return " + pColumnInfo.memberVariableName + "; }");
		writeLine();                   	
	}

	/** creates Data Record getIListItemID.	 */
	private void writeDataRecordIListItemID() throws IOException
	{
		writeLine("\t\t/** Overrides the \"getListItemID\" method of IListItem. */");	   
		writeLine("\t\tpublic String getListItemID() { return toString(); }");
		writeLine();
	}

	/** creates Data Record getIListItemDesc.	 */
	private void writeDataRecordIListItemDesc() throws IOException
	{
		writeLine("\t\t/** Overrides the \"getListItemDesc\" method of IListItem. */");	   
		writeLine("\t\tpublic String getListItemDesc() { return getDesc(); }");
		writeLine();
	}

	/** creates Accessor for Data Record. */
	private void writeDataRecordMutator(ColumnInfo pColumnInfo) throws IOException
	{
		writeLine("\t\t/** Updates the " + pColumnInfo.name + " property */");
		writeLine("\t\tpublic void set" + pColumnInfo.name + "(" + pColumnInfo.javaType + " " + pColumnInfo.variablePrefix + "NewValue)");
		writeLine("\t\t{");
		writeLine("\t\t\t if (editProperty(" + pColumnInfo.memberVariableName + ", " + pColumnInfo.variablePrefix + "NewValue))");
		writeLine("\t\t\t\t" + pColumnInfo.memberVariableName + " = " + pColumnInfo.variablePrefix + "NewValue;");
		writeLine("\t\t}");
		writeLine();
	}

	/** creates a member variable for Data Record. */
	private void writeDataRecordMemberVariable(ColumnInfo pColumnInfo) throws IOException
	{
		writeLine("\t\tprivate " + pColumnInfo.javaType + " " + pColumnInfo.memberVariableName + ";");
	}

	/** creates Data Collection content for an individual table. */
	private void writeDataCollectionClass() throws SQLException, IOException
	{	
		writeDataCollectionStaticMember();
		writeDataCollectionConstructor();
		writeDataCollectionMainFunctionality();
	}

	/** creates Data Record content for an individual table. */
	private void writeDataRecordClass() throws SQLException, IOException
	{	
		Columns pColumns = getColumns();

		writeDataRecordFetch();
		writeDataRecordUpdate();
		writeDataRecordInsert();
		writeDataRecordBindID();
		writeDataRecordBindDesc();
		writeDataRecordToString();
		writeDataRecordHashCode();
			
		for (int i = 0; i < pColumns.size(); i++)
			writeDataRecordAccessor((ColumnInfo) m_ListColumns.get(i));	

		writeDataRecordIListItemID();
		writeDataRecordIListItemDesc();

		for (int i = 0; i < pColumns.size(); i++)
			writeDataRecordMutator((ColumnInfo) m_ListColumns.get(i));

		writeSectionHeader("Member variables");

		for (int i = 0; i < pColumns.size(); i++)
			writeDataRecordMemberVariable((ColumnInfo) m_ListColumns.get(i));
	}

	/************************************************************************** 
	* 
	* Member variables
	* 
	**************************************************************************/

	/** Array List of column information. */
	private ArrayList m_ListColumns = null;

	/** Is there an auto incrementing field? */
	private boolean m_bIsAutoIncrementing = false;

	/************************************************************************** 
	* 
	* Test Driver
	* 
	**************************************************************************/

	/*
	public static void main(String strArgs[])
	{
		if (3 > strArgs.length)
		{
			System.out.println("Please provide at least 3 arguments ... (Data Source, User ID, Password)");
			System.exit(1);
			return;
		}

		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			Connection pConnect = DriverManager.getConnection("jdbc:odbc:" + strArgs[0], strArgs[1], strArgs[2]);

			Tables pTables = new Tables(Tables.TYPE_TABLE);
			pTables.load(pConnect);

			if (4 <= strArgs.length)
				createClassFile((Tables.Record) pTables.find(strArgs[3]), pConnect);

			else
				for (int i = 0; i < pTables.size(); i++)
					createClassFile((Tables.Record) pTables.item(i), pConnect);
		}

		catch (Exception ex) { ex.printStackTrace(); }
	}

	private static void createClassFile(Tables.Record pTable, Connection pConnect)
		throws SQLException, IOException
	{
		if (null == pTable)
			return;

		File pFile = new File(fixVarName(pTable.getName()) + ".java");
		FileOutputStream pStream = new FileOutputStream(pFile);

		DataServiceObjectJava pData = new DataServiceObjectJava(pStream, pTable, pConnect);
		pData.run();

		System.out.println(pFile.getName() + " generated.");
	}		
	*/

/****************************************************************************************
*
*	Structure class for containing column information.
*
****************************************************************************************/
}
