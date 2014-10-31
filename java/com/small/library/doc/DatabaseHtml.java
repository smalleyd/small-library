package com.small.library.doc;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.small.library.data.*;
import com.small.library.html.*;
import com.small.library.metadata.*;

/***********************************************************************************
*
*	Class that outputs the simple <I>DatabaseMetaData</I> values to an
*	HTML document.
*
*	@author David Small
*	@version 1.0.0.0
*
***********************************************************************************/

public class DatabaseHtml
{
	/***************************************************************************
	*
	*	Constructors
	*
	***************************************************************************/

	/** Constructor - constructs an empty object. */
	public DatabaseHtml() { this(null, null); }

	/** Constructor - constructs a populated object.
		@param pConnectionFactory The database connection's connection factory.
		@param pWriter HTML document's output stream.
	*/
	public DatabaseHtml(ConnectionFactory pConnectionFactory,
		PrintWriter pWriter)
	{
		m_ConnectionFactory = pConnectionFactory;
		m_Writer = pWriter;
		m_Table = new Table("DatabaseMetaData",
			1, 3, 0,
			null, null, null, null);
	}

	/***************************************************************************
	*
	*	Action methods
	*
	***************************************************************************/

	/** Action method - writes the connection's metadata to an HTML
	    document. */
	public void run()
		throws SQLException, IOException
	{
		Connection pConnection = m_ConnectionFactory.getConnection();
		DatabaseMetaData pMetaData = pConnection.getMetaData();
		TableRows pRows = createTableRows(pMetaData);

		startDoc(pMetaData);

		m_Table.create(m_Writer, pRows);

		endDoc(pMetaData);

		m_Writer.flush();
		pConnection.close();
	}

	/** Action method - starts the HTML document. */
	private void startDoc(DatabaseMetaData pMetaData)
		throws SQLException, IOException
	{
		String strTitle = pMetaData.getDatabaseProductName() + " " +
			pMetaData.getDatabaseProductVersion() + " [" +
			pMetaData.getDriverName() + "]";

		m_Writer.println("<HTML>");
		m_Writer.println("<HEAD><TITLE>" + strTitle + "</TITLE></HEAD>");
		m_Writer.println("<BODY>");
		m_Writer.println("<H1>" + strTitle + "</H1>");
	}

	/** Action method - ends the HTML document. */
	private void endDoc(DatabaseMetaData pMetaData)
		throws SQLException, IOException
	{
		m_Writer.println("</BODY></HTML>");
	}

	/** Action method - creates and populates the table rows that will make
	    up the body of the database meta data documentation.
	*/
	private TableRows createTableRows(DatabaseMetaData pMetaData)
		throws SQLException
	{
		TableRows pRows = new TableRows();

		pRows.add(new TableRow(new TableCell[] {
			new TableHeader("Meta Data Name"),
			new TableHeader("Meta Data Value") }));

		pRows.add(new TableRow(createCellData("allTablesAreSelectable", pMetaData.allTablesAreSelectable())));
		pRows.add(new TableRow(createCellData("dataDefinitionCausesTransactionCommit", pMetaData.dataDefinitionCausesTransactionCommit())));
		pRows.add(new TableRow(createCellData("dataDefinitionIgnoredInTransactions", pMetaData.dataDefinitionIgnoredInTransactions())));
		// pRows.add(new TableRow(createCellData("deletesAreDetected", pMetaData.deletesAreDetected())));
		pRows.add(new TableRow(createCellData("doesMaxRowSizeIncludeBlobs", pMetaData.doesMaxRowSizeIncludeBlobs())));
		// pRows.add(new TableRow(createCellData("getAttributes", pMetaData.getAttributes())));
		// pRows.add(new TableRow(createCellData("getBestRowIdentifier", pMetaData.getBestRowIdentifier())));
		pRows.add(new TableRow(createCellData("getCatalogSeparator", pMetaData.getCatalogSeparator())));
		pRows.add(new TableRow(createCellData("getCatalogTerm", pMetaData.getCatalogTerm())));
		// pRows.add(new TableRow(createCellData("getDatabaseMajorVersion", pMetaData.getDatabaseMajorVersion())));
		// pRows.add(new TableRow(createCellData("getDatabaseMinorVersion", pMetaData.getDatabaseMinorVersion())));
		pRows.add(new TableRow(createCellData("getDatabaseProductName", pMetaData.getDatabaseProductName())));
		pRows.add(new TableRow(createCellData("getDatabaseProductVersion", pMetaData.getDatabaseProductVersion())));
		pRows.add(new TableRow(createCellData("getDefaultTransactionIsolation", pMetaData.getDefaultTransactionIsolation())));
		pRows.add(new TableRow(createCellData("getDriverMajorVersion", pMetaData.getDriverMajorVersion())));
		pRows.add(new TableRow(createCellData("getDriverMinorVersion", pMetaData.getDriverMinorVersion())));
		pRows.add(new TableRow(createCellData("getDriverName", pMetaData.getDriverName())));
		pRows.add(new TableRow(createCellData("getDriverVersion", pMetaData.getDriverVersion())));
		pRows.add(new TableRow(createCellData("getExtraNameCharacters", pMetaData.getExtraNameCharacters())));
		pRows.add(new TableRow(createCellData("getIdentifierQuoteString", pMetaData.getIdentifierQuoteString())));
		pRows.add(new TableRow(createCellData("getJDBCMajorVersion", pMetaData.getJDBCMajorVersion())));
		pRows.add(new TableRow(createCellData("getJDBCMinorVersion", pMetaData.getJDBCMinorVersion())));
		pRows.add(new TableRow(createCellData("getMaxBinaryLiteralLength", pMetaData.getMaxBinaryLiteralLength())));
		pRows.add(new TableRow(createCellData("getMaxCatalogNameLength", pMetaData.getMaxCatalogNameLength())));
		pRows.add(new TableRow(createCellData("getMaxCharLiteralLength", pMetaData.getMaxCharLiteralLength())));
		pRows.add(new TableRow(createCellData("getMaxColumnNameLength", pMetaData.getMaxColumnNameLength())));
		pRows.add(new TableRow(createCellData("getMaxColumnsInGroupBy", pMetaData.getMaxColumnsInGroupBy())));
		pRows.add(new TableRow(createCellData("getMaxColumnsInIndex", pMetaData.getMaxColumnsInIndex())));
		pRows.add(new TableRow(createCellData("getMaxColumnsInOrderBy", pMetaData.getMaxColumnsInOrderBy())));
		pRows.add(new TableRow(createCellData("getMaxColumnsInSelect", pMetaData.getMaxColumnsInSelect())));
		pRows.add(new TableRow(createCellData("getMaxColumnsInTable", pMetaData.getMaxColumnsInTable())));
		pRows.add(new TableRow(createCellData("getMaxConnections", pMetaData.getMaxConnections())));
		pRows.add(new TableRow(createCellData("getMaxCursorNameLength", pMetaData.getMaxCursorNameLength())));
		pRows.add(new TableRow(createCellData("getMaxIndexLength", pMetaData.getMaxIndexLength())));
		pRows.add(new TableRow(createCellData("getMaxProcedureNameLength", pMetaData.getMaxProcedureNameLength())));
		pRows.add(new TableRow(createCellData("getMaxRowSize", pMetaData.getMaxRowSize())));
		pRows.add(new TableRow(createCellData("getMaxSchemaNameLength", pMetaData.getMaxSchemaNameLength())));
		pRows.add(new TableRow(createCellData("getMaxStatementLength", pMetaData.getMaxStatementLength())));
		pRows.add(new TableRow(createCellData("getMaxStatements", pMetaData.getMaxStatements())));
		pRows.add(new TableRow(createCellData("getMaxTableNameLength", pMetaData.getMaxTableNameLength())));
		pRows.add(new TableRow(createCellData("getMaxTablesInSelect", pMetaData.getMaxTablesInSelect())));
		pRows.add(new TableRow(createCellData("getMaxUserNameLength", pMetaData.getMaxUserNameLength())));
		pRows.add(new TableRow(createCellData("getNumericFunctions", pMetaData.getNumericFunctions())));
		pRows.add(new TableRow(createCellData("getProcedureTerm", pMetaData.getProcedureTerm())));
		// pRows.add(new TableRow(createCellData("getResultSetHoldability", pMetaData.getResultSetHoldability())));
		pRows.add(new TableRow(createCellData("getSQLKeywords", pMetaData.getSQLKeywords())));
		pRows.add(new TableRow(createCellData("getSQLStateType", pMetaData.getSQLStateType())));
		pRows.add(new TableRow(createCellData("getSchemaTerm", pMetaData.getSchemaTerm())));
		pRows.add(new TableRow(createCellData("getSearchStringEscape", pMetaData.getSearchStringEscape())));
		pRows.add(new TableRow(createCellData("getStringFunctions", pMetaData.getStringFunctions())));
		pRows.add(new TableRow(createCellData("getSystemFunctions", pMetaData.getSystemFunctions())));
		pRows.add(new TableRow(createCellData("getTimeDateFunctions", pMetaData.getTimeDateFunctions())));
		pRows.add(new TableRow(createCellData("getURL", pMetaData.getURL())));
		pRows.add(new TableRow(createCellData("getUserName", pMetaData.getUserName())));
		// pRows.add(new TableRow(createCellData("insertsAreDetected", pMetaData.insertsAreDetected())));
		pRows.add(new TableRow(createCellData("isCatalogAtStart", pMetaData.isCatalogAtStart())));
		pRows.add(new TableRow(createCellData("isReadOnly", pMetaData.isReadOnly())));
		// pRows.add(new TableRow(createCellData("locatorsUpdateCopy", pMetaData.locatorsUpdateCopy())));
		pRows.add(new TableRow(createCellData("nullPlusNonNullIsNull", pMetaData.nullPlusNonNullIsNull())));
		pRows.add(new TableRow(createCellData("nullsAreSortedAtEnd", pMetaData.nullsAreSortedAtEnd())));
		pRows.add(new TableRow(createCellData("nullsAreSortedAtStart", pMetaData.nullsAreSortedAtStart())));
		pRows.add(new TableRow(createCellData("nullsAreSortedHigh", pMetaData.nullsAreSortedHigh())));
		pRows.add(new TableRow(createCellData("nullsAreSortedLow", pMetaData.nullsAreSortedLow())));
		// pRows.add(new TableRow(createCellData("othersDeletesAreVisible", pMetaData.othersDeletesAreVisible())));
		// pRows.add(new TableRow(createCellData("othersInsertsAreVisible", pMetaData.othersInsertsAreVisible())));
		// pRows.add(new TableRow(createCellData("othersUpdatesAreVisible", pMetaData.othersUpdatesAreVisible())));
		// pRows.add(new TableRow(createCellData("ownDeletesAreVisible", pMetaData.ownDeletesAreVisible())));
		// pRows.add(new TableRow(createCellData("ownInsertsAreVisible", pMetaData.ownInsertsAreVisible())));
		// pRows.add(new TableRow(createCellData("ownUpdatesAreVisible", pMetaData.ownUpdatesAreVisible())));
		pRows.add(new TableRow(createCellData("storesLowerCaseIdentifiers", pMetaData.storesLowerCaseIdentifiers())));
		pRows.add(new TableRow(createCellData("storesLowerCaseQuotedIdentifiers", pMetaData.storesLowerCaseQuotedIdentifiers())));
		pRows.add(new TableRow(createCellData("storesMixedCaseIdentifiers", pMetaData.storesMixedCaseIdentifiers())));
		pRows.add(new TableRow(createCellData("storesMixedCaseQuotedIdentifiers", pMetaData.storesMixedCaseQuotedIdentifiers())));
		pRows.add(new TableRow(createCellData("storesUpperCaseIdentifiers", pMetaData.storesUpperCaseIdentifiers())));
		pRows.add(new TableRow(createCellData("storesUpperCaseQuotedIdentifiers", pMetaData.storesUpperCaseQuotedIdentifiers())));
		pRows.add(new TableRow(createCellData("supportsANSI92EntryLevelSQL", pMetaData.supportsANSI92EntryLevelSQL())));
		pRows.add(new TableRow(createCellData("supportsANSI92FullSQL", pMetaData.supportsANSI92FullSQL())));
		pRows.add(new TableRow(createCellData("supportsANSI92IntermediateSQL", pMetaData.supportsANSI92IntermediateSQL())));
		pRows.add(new TableRow(createCellData("supportsAlterTableWithAddColumn", pMetaData.supportsAlterTableWithAddColumn())));
		pRows.add(new TableRow(createCellData("supportsAlterTableWithDropColumn", pMetaData.supportsAlterTableWithDropColumn())));
		pRows.add(new TableRow(createCellData("supportsBatchUpdates", pMetaData.supportsBatchUpdates())));
		pRows.add(new TableRow(createCellData("supportsCatalogsInDataManipulation", pMetaData.supportsCatalogsInDataManipulation())));
		pRows.add(new TableRow(createCellData("supportsCatalogsInIndexDefinitions", pMetaData.supportsCatalogsInIndexDefinitions())));
		pRows.add(new TableRow(createCellData("supportsCatalogsInPrivilegeDefinitions", pMetaData.supportsCatalogsInPrivilegeDefinitions())));
		pRows.add(new TableRow(createCellData("supportsCatalogsInProcedureCalls", pMetaData.supportsCatalogsInProcedureCalls())));
		pRows.add(new TableRow(createCellData("supportsCatalogsInTableDefinitions", pMetaData.supportsCatalogsInTableDefinitions())));
		pRows.add(new TableRow(createCellData("supportsColumnAliasing", pMetaData.supportsColumnAliasing())));
		pRows.add(new TableRow(createCellData("supportsConvert", pMetaData.supportsConvert())));
		pRows.add(new TableRow(createCellData("supportsCoreSQLGrammar", pMetaData.supportsCoreSQLGrammar())));
		pRows.add(new TableRow(createCellData("supportsCorrelatedSubqueries", pMetaData.supportsCorrelatedSubqueries())));
		pRows.add(new TableRow(createCellData("supportsDataDefinitionAndDataManipulationTransactions", pMetaData.supportsDataDefinitionAndDataManipulationTransactions())));
		pRows.add(new TableRow(createCellData("supportsDataManipulationTransactionsOnly", pMetaData.supportsDataManipulationTransactionsOnly())));
		pRows.add(new TableRow(createCellData("supportsDifferentTableCorrelationNames", pMetaData.supportsDifferentTableCorrelationNames())));
		pRows.add(new TableRow(createCellData("supportsExpressionsInOrderBy", pMetaData.supportsExpressionsInOrderBy())));
		pRows.add(new TableRow(createCellData("supportsExtendedSQLGrammar", pMetaData.supportsExtendedSQLGrammar())));
		pRows.add(new TableRow(createCellData("supportsFullOuterJoins", pMetaData.supportsFullOuterJoins())));
		pRows.add(new TableRow(createCellData("supportsGetGeneratedKeys", pMetaData.supportsGetGeneratedKeys())));
		pRows.add(new TableRow(createCellData("supportsGroupBy", pMetaData.supportsGroupBy())));
		pRows.add(new TableRow(createCellData("supportsGroupByBeyondSelect", pMetaData.supportsGroupByBeyondSelect())));
		pRows.add(new TableRow(createCellData("supportsGroupByUnrelated", pMetaData.supportsGroupByUnrelated())));
		pRows.add(new TableRow(createCellData("supportsIntegrityEnhancementFacility", pMetaData.supportsIntegrityEnhancementFacility())));
		pRows.add(new TableRow(createCellData("supportsLikeEscapeClause", pMetaData.supportsLikeEscapeClause())));
		pRows.add(new TableRow(createCellData("supportsLimitedOuterJoins", pMetaData.supportsLimitedOuterJoins())));
		pRows.add(new TableRow(createCellData("supportsMinimumSQLGrammar", pMetaData.supportsMinimumSQLGrammar())));
		pRows.add(new TableRow(createCellData("supportsMixedCaseIdentifiers", pMetaData.supportsMixedCaseIdentifiers())));
		pRows.add(new TableRow(createCellData("supportsMixedCaseQuotedIdentifiers", pMetaData.supportsMixedCaseQuotedIdentifiers())));
		pRows.add(new TableRow(createCellData("supportsMultipleOpenResults", pMetaData.supportsMultipleOpenResults())));
		pRows.add(new TableRow(createCellData("supportsMultipleResultSets", pMetaData.supportsMultipleResultSets())));
		pRows.add(new TableRow(createCellData("supportsMultipleTransactions", pMetaData.supportsMultipleTransactions())));
		pRows.add(new TableRow(createCellData("supportsNamedParameters", pMetaData.supportsNamedParameters())));
		pRows.add(new TableRow(createCellData("supportsNonNullableColumns", pMetaData.supportsNonNullableColumns())));
		pRows.add(new TableRow(createCellData("supportsOpenCursorsAcrossCommit", pMetaData.supportsOpenCursorsAcrossCommit())));
		pRows.add(new TableRow(createCellData("supportsOpenCursorsAcrossRollback", pMetaData.supportsOpenCursorsAcrossRollback())));
		pRows.add(new TableRow(createCellData("supportsOpenStatementsAcrossCommit", pMetaData.supportsOpenStatementsAcrossCommit())));
		pRows.add(new TableRow(createCellData("supportsOpenStatementsAcrossRollback", pMetaData.supportsOpenStatementsAcrossRollback())));
		pRows.add(new TableRow(createCellData("supportsOrderByUnrelated", pMetaData.supportsOrderByUnrelated())));
		pRows.add(new TableRow(createCellData("supportsOuterJoins", pMetaData.supportsOuterJoins())));
		pRows.add(new TableRow(createCellData("supportsPositionedDelete", pMetaData.supportsPositionedDelete())));
		pRows.add(new TableRow(createCellData("supportsPositionedUpdate", pMetaData.supportsPositionedUpdate())));
		// pRows.add(new TableRow(createCellData("supportsResultSetConcurrency", pMetaData.supportsResultSetConcurrency())));
		// pRows.add(new TableRow(createCellData("supportsResultSetHoldability", pMetaData.supportsResultSetHoldability())));
		// pRows.add(new TableRow(createCellData("supportsResultSetType", pMetaData.supportsResultSetType())));
		pRows.add(new TableRow(createCellData("supportsSavepoints", pMetaData.supportsSavepoints())));
		pRows.add(new TableRow(createCellData("supportsSchemasInDataManipulation", pMetaData.supportsSchemasInDataManipulation())));
		pRows.add(new TableRow(createCellData("supportsSchemasInIndexDefinitions", pMetaData.supportsSchemasInIndexDefinitions())));
		pRows.add(new TableRow(createCellData("supportsSchemasInPrivilegeDefinitions", pMetaData.supportsSchemasInPrivilegeDefinitions())));
		pRows.add(new TableRow(createCellData("supportsSchemasInProcedureCalls", pMetaData.supportsSchemasInProcedureCalls())));
		pRows.add(new TableRow(createCellData("supportsSchemasInTableDefinitions", pMetaData.supportsSchemasInTableDefinitions())));
		pRows.add(new TableRow(createCellData("supportsSelectForUpdate", pMetaData.supportsSelectForUpdate())));
		// pRows.add(new TableRow(createCellData("supportsStatementPooling", pMetaData.supportsStatementPooling())));
		pRows.add(new TableRow(createCellData("supportsStoredProcedures", pMetaData.supportsStoredProcedures())));
		pRows.add(new TableRow(createCellData("supportsSubqueriesInComparisons", pMetaData.supportsSubqueriesInComparisons())));
		pRows.add(new TableRow(createCellData("supportsSubqueriesInExists", pMetaData.supportsSubqueriesInExists())));
		pRows.add(new TableRow(createCellData("supportsSubqueriesInIns", pMetaData.supportsSubqueriesInIns())));
		pRows.add(new TableRow(createCellData("supportsSubqueriesInQuantifieds", pMetaData.supportsSubqueriesInQuantifieds())));
		pRows.add(new TableRow(createCellData("supportsTableCorrelationNames", pMetaData.supportsTableCorrelationNames())));
		// pRows.add(new TableRow(createCellData("supportsTransactionIsolationLevel", pMetaData.supportsTransactionIsolationLevel())));
		pRows.add(new TableRow(createCellData("supportsTransactions", pMetaData.supportsTransactions())));
		pRows.add(new TableRow(createCellData("supportsUnion", pMetaData.supportsUnion())));
		pRows.add(new TableRow(createCellData("supportsUnionAll", pMetaData.supportsUnionAll())));
		// pRows.add(new TableRow(createCellData("updatesAreDetected", pMetaData.updatesAreDetected())));
		pRows.add(new TableRow(createCellData("usesLocalFilePerTable", pMetaData.usesLocalFilePerTable())));
		pRows.add(new TableRow(createCellData("usesLocalFiles", pMetaData.usesLocalFiles())));

		return pRows;
	}

	/** Action method - creates and populates a table cell with an element of
	    meta data.
	*/
	private TableCell[] createCellData(String strName, boolean bValue)
	{ return createCellData(strName, "" + bValue); }

	/** Action method - creates and populates a table cell with an element of
	    meta data.
	*/
	private TableCell[] createCellData(String strName, int nValue)
	{ return createCellData(strName, "" + nValue); }

	/** Action method - creates and populates a table cell with an element of
	    meta data.
	*/
	private TableCell[] createCellData(String strName, String strValue)
	{
		return new TableCell[] { new TableCell(strName),
			new TableCell(strValue) };
	}

	/***************************************************************************
	*
	*	Accessor methods
	*
	***************************************************************************/

	/** Accessor method - gets the database connection's connection factory. */
	public ConnectionFactory getConnectionFactory() { return m_ConnectionFactory; }

	/** Accessor method - gets the HTML document's output stream. */
	public PrintWriter getWriter() { return m_Writer; }

	/***************************************************************************
	*
	*	Mutator methods
	*
	***************************************************************************/

	/** Mutator method - sets the database connection's connection factory. */
	public void setConnectionFactory(ConnectionFactory pNewValue)
	{ m_ConnectionFactory = pNewValue; }

	/** Mutator method - sets the HTML document's output stream. */
	public void setWriter(PrintWriter pNewValue) { m_Writer = pNewValue; }

	/***************************************************************************
	*
	*	Member variables
	*
	***************************************************************************/

	/** Private member variable - reference to the database connection's
	    connection factory.
	*/
	private ConnectionFactory m_ConnectionFactory = null;

	/** Private member variable - reference to the HTML document's output
	    stream.
	*/
	private PrintWriter m_Writer = null;

	/** Private member variable - reference to the table that displays the output. */
	private Table m_Table = null;

	/***************************************************************************
	*
	*	Class entry point
	*
	***************************************************************************/

	/** Entry point - Command line entry point for class.
	*/
	public static void main(String[] strArgs)
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException();

			// Local variables
			String strFile = strArgs[0];
			String strUrl = strArgs[1];
			String strUserName = strArgs[2];
			String strPassword = null;
			String strDriver = "sun.jdbc.odbc.JdbcOdbcDriver";

			if (3 < strArgs.length)
				strPassword = strArgs[3];

			if (4 < strArgs.length)
				strDriver = strArgs[4];
			else
				strUrl = "jdbc:odbc:" + strUrl;

			DataSource pDataSource = new DataSource(strDriver,
				strUrl, strUserName, strPassword);
			PrintWriter pWriter = new PrintWriter(new FileWriter(strFile));

			(new DatabaseHtml(pDataSource.getConnectionPool(), pWriter)).run();

/* Used to list of meta data method calls.
			java.lang.reflect.Method[] pMethods = DatabaseMetaData.class.getMethods();
			Set setMethods = new TreeSet();

			for (int i = 0; i < pMethods.length; i++)
				setMethods.add(pMethods[i].getName());

			Iterator itMethods = setMethods.iterator();

			while (itMethods.hasNext())
			{
				String strMethod = (String) itMethods.next();

				pWriter.print("\t\tpRows.add(new TableRow(createCellData(");
				pWriter.println("\"" + strMethod + "\", pMetaData." + strMethod + "())));");
			}
*/
			pWriter.close();
		}

		catch (IllegalArgumentException pEx)
		{
			System.out.println("Usage: java " + DatabaseHtml.class.getName() + " HTML_File");
			System.out.println("\tJDBC_Url");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
