package com.small.library.doc;

import static com.small.library.generator.Base.*;

import java.io.*;
import java.sql.*;

import javax.sql.DataSource;

import com.small.library.html.*;

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
	private final DataSource dataSource;
	private final PrintWriter writer;
	private final Table table;

	/** Constructor - constructs a populated object.
		@param dataSource The database connection's connection factory.
		@param writer HTML document's output stream.
	*/
	public DatabaseHtml(final DataSource dataSource, final PrintWriter writer)
	{
		this.dataSource = dataSource;
		this.writer = writer;
		table = new Table("DatabaseMetaData",
			1, 3, 0,
			null, null, null, null);
	}

	/** Action method - writes the connection's metadata to an HTML
	    document. */
	public void run()
		throws SQLException, IOException
	{
		try (final Connection pConnection = dataSource.getConnection())
		{
			DatabaseMetaData metadata = pConnection.getMetaData();
			TableRows rows = createTableRows(metadata);
	
			startDoc(metadata);
	
			table.create(writer, rows);
	
			endDoc(metadata);
	
			writer.flush();
		}
	}

	/** Action method - starts the HTML document. */
	private void startDoc(final DatabaseMetaData metadata)
		throws SQLException, IOException
	{
		String strTitle = metadata.getDatabaseProductName() + " " +
			metadata.getDatabaseProductVersion() + " [" +
			metadata.getDriverName() + "]";

		writer.println("<HTML>");
		writer.println("<HEAD><TITLE>" + strTitle + "</TITLE></HEAD>");
		writer.println("<BODY>");
		writer.println("<H1>" + strTitle + "</H1>");
	}

	/** Action method - ends the HTML document. */
	private void endDoc(DatabaseMetaData metadata)
		throws SQLException, IOException
	{
		writer.println("</BODY></HTML>");
	}

	/** Action method - creates and populates the table rows that will make
	    up the body of the database meta data documentation.
	*/
	private TableRows createTableRows(final DatabaseMetaData metadata)
		throws SQLException
	{
		final TableRows rows = new TableRows();

		rows.add(new TableRow(new TableCell[] {
			new TableHeader("Meta Data Name"),
			new TableHeader("Meta Data Value") }));

		rows.add(new TableRow(createCellData("allTablesAreSelectable", metadata.allTablesAreSelectable())));
		rows.add(new TableRow(createCellData("dataDefinitionCausesTransactionCommit", metadata.dataDefinitionCausesTransactionCommit())));
		rows.add(new TableRow(createCellData("dataDefinitionIgnoredInTransactions", metadata.dataDefinitionIgnoredInTransactions())));
		// rows.add(new TableRow(createCellData("deletesAreDetected", metadata.deletesAreDetected())));
		rows.add(new TableRow(createCellData("doesMaxRowSizeIncludeBlobs", metadata.doesMaxRowSizeIncludeBlobs())));
		// rows.add(new TableRow(createCellData("getAttributes", metadata.getAttributes())));
		// rows.add(new TableRow(createCellData("getBestRowIdentifier", metadata.getBestRowIdentifier())));
		rows.add(new TableRow(createCellData("getCatalogSeparator", metadata.getCatalogSeparator())));
		rows.add(new TableRow(createCellData("getCatalogTerm", metadata.getCatalogTerm())));
		// rows.add(new TableRow(createCellData("getDatabaseMajorVersion", metadata.getDatabaseMajorVersion())));
		// rows.add(new TableRow(createCellData("getDatabaseMinorVersion", metadata.getDatabaseMinorVersion())));
		rows.add(new TableRow(createCellData("getDatabaseProductName", metadata.getDatabaseProductName())));
		rows.add(new TableRow(createCellData("getDatabaseProductVersion", metadata.getDatabaseProductVersion())));
		rows.add(new TableRow(createCellData("getDefaultTransactionIsolation", metadata.getDefaultTransactionIsolation())));
		rows.add(new TableRow(createCellData("getDriverMajorVersion", metadata.getDriverMajorVersion())));
		rows.add(new TableRow(createCellData("getDriverMinorVersion", metadata.getDriverMinorVersion())));
		rows.add(new TableRow(createCellData("getDriverName", metadata.getDriverName())));
		rows.add(new TableRow(createCellData("getDriverVersion", metadata.getDriverVersion())));
		rows.add(new TableRow(createCellData("getExtraNameCharacters", metadata.getExtraNameCharacters())));
		rows.add(new TableRow(createCellData("getIdentifierQuoteString", metadata.getIdentifierQuoteString())));
		rows.add(new TableRow(createCellData("getJDBCMajorVersion", metadata.getJDBCMajorVersion())));
		rows.add(new TableRow(createCellData("getJDBCMinorVersion", metadata.getJDBCMinorVersion())));
		rows.add(new TableRow(createCellData("getMaxBinaryLiteralLength", metadata.getMaxBinaryLiteralLength())));
		rows.add(new TableRow(createCellData("getMaxCatalogNameLength", metadata.getMaxCatalogNameLength())));
		rows.add(new TableRow(createCellData("getMaxCharLiteralLength", metadata.getMaxCharLiteralLength())));
		rows.add(new TableRow(createCellData("getMaxColumnNameLength", metadata.getMaxColumnNameLength())));
		rows.add(new TableRow(createCellData("getMaxColumnsInGroupBy", metadata.getMaxColumnsInGroupBy())));
		rows.add(new TableRow(createCellData("getMaxColumnsInIndex", metadata.getMaxColumnsInIndex())));
		rows.add(new TableRow(createCellData("getMaxColumnsInOrderBy", metadata.getMaxColumnsInOrderBy())));
		rows.add(new TableRow(createCellData("getMaxColumnsInSelect", metadata.getMaxColumnsInSelect())));
		rows.add(new TableRow(createCellData("getMaxColumnsInTable", metadata.getMaxColumnsInTable())));
		rows.add(new TableRow(createCellData("getMaxConnections", metadata.getMaxConnections())));
		rows.add(new TableRow(createCellData("getMaxCursorNameLength", metadata.getMaxCursorNameLength())));
		rows.add(new TableRow(createCellData("getMaxIndexLength", metadata.getMaxIndexLength())));
		rows.add(new TableRow(createCellData("getMaxProcedureNameLength", metadata.getMaxProcedureNameLength())));
		rows.add(new TableRow(createCellData("getMaxRowSize", metadata.getMaxRowSize())));
		rows.add(new TableRow(createCellData("getMaxSchemaNameLength", metadata.getMaxSchemaNameLength())));
		rows.add(new TableRow(createCellData("getMaxStatementLength", metadata.getMaxStatementLength())));
		rows.add(new TableRow(createCellData("getMaxStatements", metadata.getMaxStatements())));
		rows.add(new TableRow(createCellData("getMaxTableNameLength", metadata.getMaxTableNameLength())));
		rows.add(new TableRow(createCellData("getMaxTablesInSelect", metadata.getMaxTablesInSelect())));
		rows.add(new TableRow(createCellData("getMaxUserNameLength", metadata.getMaxUserNameLength())));
		rows.add(new TableRow(createCellData("getNumericFunctions", metadata.getNumericFunctions())));
		rows.add(new TableRow(createCellData("getProcedureTerm", metadata.getProcedureTerm())));
		// rows.add(new TableRow(createCellData("getResultSetHoldability", metadata.getResultSetHoldability())));
		rows.add(new TableRow(createCellData("getSQLKeywords", metadata.getSQLKeywords())));
		rows.add(new TableRow(createCellData("getSQLStateType", metadata.getSQLStateType())));
		rows.add(new TableRow(createCellData("getSchemaTerm", metadata.getSchemaTerm())));
		rows.add(new TableRow(createCellData("getSearchStringEscape", metadata.getSearchStringEscape())));
		rows.add(new TableRow(createCellData("getStringFunctions", metadata.getStringFunctions())));
		rows.add(new TableRow(createCellData("getSystemFunctions", metadata.getSystemFunctions())));
		rows.add(new TableRow(createCellData("getTimeDateFunctions", metadata.getTimeDateFunctions())));
		rows.add(new TableRow(createCellData("getURL", metadata.getURL())));
		rows.add(new TableRow(createCellData("getUserName", metadata.getUserName())));
		// rows.add(new TableRow(createCellData("insertsAreDetected", metadata.insertsAreDetected())));
		rows.add(new TableRow(createCellData("isCatalogAtStart", metadata.isCatalogAtStart())));
		rows.add(new TableRow(createCellData("isReadOnly", metadata.isReadOnly())));
		// rows.add(new TableRow(createCellData("locatorsUpdateCopy", metadata.locatorsUpdateCopy())));
		rows.add(new TableRow(createCellData("nullPlusNonNullIsNull", metadata.nullPlusNonNullIsNull())));
		rows.add(new TableRow(createCellData("nullsAreSortedAtEnd", metadata.nullsAreSortedAtEnd())));
		rows.add(new TableRow(createCellData("nullsAreSortedAtStart", metadata.nullsAreSortedAtStart())));
		rows.add(new TableRow(createCellData("nullsAreSortedHigh", metadata.nullsAreSortedHigh())));
		rows.add(new TableRow(createCellData("nullsAreSortedLow", metadata.nullsAreSortedLow())));
		// rows.add(new TableRow(createCellData("othersDeletesAreVisible", metadata.othersDeletesAreVisible())));
		// rows.add(new TableRow(createCellData("othersInsertsAreVisible", metadata.othersInsertsAreVisible())));
		// rows.add(new TableRow(createCellData("othersUpdatesAreVisible", metadata.othersUpdatesAreVisible())));
		// rows.add(new TableRow(createCellData("ownDeletesAreVisible", metadata.ownDeletesAreVisible())));
		// rows.add(new TableRow(createCellData("ownInsertsAreVisible", metadata.ownInsertsAreVisible())));
		// rows.add(new TableRow(createCellData("ownUpdatesAreVisible", metadata.ownUpdatesAreVisible())));
		rows.add(new TableRow(createCellData("storesLowerCaseIdentifiers", metadata.storesLowerCaseIdentifiers())));
		rows.add(new TableRow(createCellData("storesLowerCaseQuotedIdentifiers", metadata.storesLowerCaseQuotedIdentifiers())));
		rows.add(new TableRow(createCellData("storesMixedCaseIdentifiers", metadata.storesMixedCaseIdentifiers())));
		rows.add(new TableRow(createCellData("storesMixedCaseQuotedIdentifiers", metadata.storesMixedCaseQuotedIdentifiers())));
		rows.add(new TableRow(createCellData("storesUpperCaseIdentifiers", metadata.storesUpperCaseIdentifiers())));
		rows.add(new TableRow(createCellData("storesUpperCaseQuotedIdentifiers", metadata.storesUpperCaseQuotedIdentifiers())));
		rows.add(new TableRow(createCellData("supportsANSI92EntryLevelSQL", metadata.supportsANSI92EntryLevelSQL())));
		rows.add(new TableRow(createCellData("supportsANSI92FullSQL", metadata.supportsANSI92FullSQL())));
		rows.add(new TableRow(createCellData("supportsANSI92IntermediateSQL", metadata.supportsANSI92IntermediateSQL())));
		rows.add(new TableRow(createCellData("supportsAlterTableWithAddColumn", metadata.supportsAlterTableWithAddColumn())));
		rows.add(new TableRow(createCellData("supportsAlterTableWithDropColumn", metadata.supportsAlterTableWithDropColumn())));
		rows.add(new TableRow(createCellData("supportsBatchUpdates", metadata.supportsBatchUpdates())));
		rows.add(new TableRow(createCellData("supportsCatalogsInDataManipulation", metadata.supportsCatalogsInDataManipulation())));
		rows.add(new TableRow(createCellData("supportsCatalogsInIndexDefinitions", metadata.supportsCatalogsInIndexDefinitions())));
		rows.add(new TableRow(createCellData("supportsCatalogsInPrivilegeDefinitions", metadata.supportsCatalogsInPrivilegeDefinitions())));
		rows.add(new TableRow(createCellData("supportsCatalogsInProcedureCalls", metadata.supportsCatalogsInProcedureCalls())));
		rows.add(new TableRow(createCellData("supportsCatalogsInTableDefinitions", metadata.supportsCatalogsInTableDefinitions())));
		rows.add(new TableRow(createCellData("supportsColumnAliasing", metadata.supportsColumnAliasing())));
		rows.add(new TableRow(createCellData("supportsConvert", metadata.supportsConvert())));
		rows.add(new TableRow(createCellData("supportsCoreSQLGrammar", metadata.supportsCoreSQLGrammar())));
		rows.add(new TableRow(createCellData("supportsCorrelatedSubqueries", metadata.supportsCorrelatedSubqueries())));
		rows.add(new TableRow(createCellData("supportsDataDefinitionAndDataManipulationTransactions", metadata.supportsDataDefinitionAndDataManipulationTransactions())));
		rows.add(new TableRow(createCellData("supportsDataManipulationTransactionsOnly", metadata.supportsDataManipulationTransactionsOnly())));
		rows.add(new TableRow(createCellData("supportsDifferentTableCorrelationNames", metadata.supportsDifferentTableCorrelationNames())));
		rows.add(new TableRow(createCellData("supportsExpressionsInOrderBy", metadata.supportsExpressionsInOrderBy())));
		rows.add(new TableRow(createCellData("supportsExtendedSQLGrammar", metadata.supportsExtendedSQLGrammar())));
		rows.add(new TableRow(createCellData("supportsFullOuterJoins", metadata.supportsFullOuterJoins())));
		rows.add(new TableRow(createCellData("supportsGetGeneratedKeys", metadata.supportsGetGeneratedKeys())));
		rows.add(new TableRow(createCellData("supportsGroupBy", metadata.supportsGroupBy())));
		rows.add(new TableRow(createCellData("supportsGroupByBeyondSelect", metadata.supportsGroupByBeyondSelect())));
		rows.add(new TableRow(createCellData("supportsGroupByUnrelated", metadata.supportsGroupByUnrelated())));
		rows.add(new TableRow(createCellData("supportsIntegrityEnhancementFacility", metadata.supportsIntegrityEnhancementFacility())));
		rows.add(new TableRow(createCellData("supportsLikeEscapeClause", metadata.supportsLikeEscapeClause())));
		rows.add(new TableRow(createCellData("supportsLimitedOuterJoins", metadata.supportsLimitedOuterJoins())));
		rows.add(new TableRow(createCellData("supportsMinimumSQLGrammar", metadata.supportsMinimumSQLGrammar())));
		rows.add(new TableRow(createCellData("supportsMixedCaseIdentifiers", metadata.supportsMixedCaseIdentifiers())));
		rows.add(new TableRow(createCellData("supportsMixedCaseQuotedIdentifiers", metadata.supportsMixedCaseQuotedIdentifiers())));
		rows.add(new TableRow(createCellData("supportsMultipleOpenResults", metadata.supportsMultipleOpenResults())));
		rows.add(new TableRow(createCellData("supportsMultipleResultSets", metadata.supportsMultipleResultSets())));
		rows.add(new TableRow(createCellData("supportsMultipleTransactions", metadata.supportsMultipleTransactions())));
		rows.add(new TableRow(createCellData("supportsNamedParameters", metadata.supportsNamedParameters())));
		rows.add(new TableRow(createCellData("supportsNonNullableColumns", metadata.supportsNonNullableColumns())));
		rows.add(new TableRow(createCellData("supportsOpenCursorsAcrossCommit", metadata.supportsOpenCursorsAcrossCommit())));
		rows.add(new TableRow(createCellData("supportsOpenCursorsAcrossRollback", metadata.supportsOpenCursorsAcrossRollback())));
		rows.add(new TableRow(createCellData("supportsOpenStatementsAcrossCommit", metadata.supportsOpenStatementsAcrossCommit())));
		rows.add(new TableRow(createCellData("supportsOpenStatementsAcrossRollback", metadata.supportsOpenStatementsAcrossRollback())));
		rows.add(new TableRow(createCellData("supportsOrderByUnrelated", metadata.supportsOrderByUnrelated())));
		rows.add(new TableRow(createCellData("supportsOuterJoins", metadata.supportsOuterJoins())));
		rows.add(new TableRow(createCellData("supportsPositionedDelete", metadata.supportsPositionedDelete())));
		rows.add(new TableRow(createCellData("supportsPositionedUpdate", metadata.supportsPositionedUpdate())));
		// rows.add(new TableRow(createCellData("supportsResultSetConcurrency", metadata.supportsResultSetConcurrency())));
		// rows.add(new TableRow(createCellData("supportsResultSetHoldability", metadata.supportsResultSetHoldability())));
		// rows.add(new TableRow(createCellData("supportsResultSetType", metadata.supportsResultSetType())));
		rows.add(new TableRow(createCellData("supportsSavepoints", metadata.supportsSavepoints())));
		rows.add(new TableRow(createCellData("supportsSchemasInDataManipulation", metadata.supportsSchemasInDataManipulation())));
		rows.add(new TableRow(createCellData("supportsSchemasInIndexDefinitions", metadata.supportsSchemasInIndexDefinitions())));
		rows.add(new TableRow(createCellData("supportsSchemasInPrivilegeDefinitions", metadata.supportsSchemasInPrivilegeDefinitions())));
		rows.add(new TableRow(createCellData("supportsSchemasInProcedureCalls", metadata.supportsSchemasInProcedureCalls())));
		rows.add(new TableRow(createCellData("supportsSchemasInTableDefinitions", metadata.supportsSchemasInTableDefinitions())));
		rows.add(new TableRow(createCellData("supportsSelectForUpdate", metadata.supportsSelectForUpdate())));
		// rows.add(new TableRow(createCellData("supportsStatementPooling", metadata.supportsStatementPooling())));
		rows.add(new TableRow(createCellData("supportsStoredProcedures", metadata.supportsStoredProcedures())));
		rows.add(new TableRow(createCellData("supportsSubqueriesInComparisons", metadata.supportsSubqueriesInComparisons())));
		rows.add(new TableRow(createCellData("supportsSubqueriesInExists", metadata.supportsSubqueriesInExists())));
		rows.add(new TableRow(createCellData("supportsSubqueriesInIns", metadata.supportsSubqueriesInIns())));
		rows.add(new TableRow(createCellData("supportsSubqueriesInQuantifieds", metadata.supportsSubqueriesInQuantifieds())));
		rows.add(new TableRow(createCellData("supportsTableCorrelationNames", metadata.supportsTableCorrelationNames())));
		// rows.add(new TableRow(createCellData("supportsTransactionIsolationLevel", metadata.supportsTransactionIsolationLevel())));
		rows.add(new TableRow(createCellData("supportsTransactions", metadata.supportsTransactions())));
		rows.add(new TableRow(createCellData("supportsUnion", metadata.supportsUnion())));
		rows.add(new TableRow(createCellData("supportsUnionAll", metadata.supportsUnionAll())));
		// rows.add(new TableRow(createCellData("updatesAreDetected", metadata.updatesAreDetected())));
		rows.add(new TableRow(createCellData("usesLocalFilePerTable", metadata.usesLocalFilePerTable())));
		rows.add(new TableRow(createCellData("usesLocalFiles", metadata.usesLocalFiles())));

		return rows;
	}

	/** Action method - creates and populates a table cell with an element of
	    meta data.
	*/
	private TableCell[] createCellData(String name, boolean bValue)
	{ return createCellData(name, "" + bValue); }

	/** Action method - creates and populates a table cell with an element of
	    meta data.
	*/
	private TableCell[] createCellData(String name, int nValue)
	{ return createCellData(name, "" + nValue); }

	/** Action method - creates and populates a table cell with an element of
	    meta data.
	*/
	private TableCell[] createCellData(String name, String strValue)
	{
		return new TableCell[] { new TableCell(name),
			new TableCell(strValue) };
	}

	/** Entry point - Command line entry point for class.
	*/
	public static void main(final String... args)
	{
		try (final PrintWriter writer = new PrintWriter(new FileWriter(extractFile(args, 0, "output"))))
		{
			(new DatabaseHtml(extractDataSource(args, 1), writer)).run();
		}

		catch (final IllegalArgumentException ex)
		{
			System.out.println("Usage: java " + DatabaseHtml.class.getName() + " HTML_File");
			System.out.println("\tJDBC_Url");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
