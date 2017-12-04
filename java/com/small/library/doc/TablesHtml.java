package com.small.library.doc;

import java.io.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.small.library.data.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Produces HTML documention of a data source's table structures. The documentation
*	includes columns (fields), indexes, primary keys, and foreign keys.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class TablesHtml
{
	/******************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public TablesHtml() { this(null, null, null); }

	/** Constructor - constructs a populated object.
		@param pDataSource The database connection's connection factory.
		@param pWriter HTML document's output stream.
		@param strSchemaName Schema Name pattern to filter the tables list by.
			Use <CODE>null</CODE> for no filter.
	*/
	public TablesHtml(DataSource pDataSource, PrintWriter pWriter,
		String strSchemaNamePattern)
	{
		connectionFactory = pDataSource;
		out = pWriter;
		schemaNamePattern = strSchemaNamePattern;
	}

	/******************************************************************************
	*
	*	HTML handling methods
	*
	*****************************************************************************/

	public void write(String strValue) throws IOException { out.print(strValue); }
	public void writeLine() throws IOException { out.println(); }
	public void writeLine(String strValue) throws IOException { out.println(strValue); }
	public void writeBreak() throws IOException { write("<BR>"); }

	public void writeDetail(String strValue) throws IOException
	{
		if (null == strValue)
			strValue = "&nbsp;";

		write("<TD CLASS=\"detail\">" + strValue + "</TD>");
	}

	public void writeDetail(boolean bValue) throws IOException
	{
		String strValue = null;

		if (bValue)
			strValue = "Yes";
		else
			strValue = "No";

		writeDetail(strValue);
	}

	public void writeDetail(int nValue) throws IOException
	{
		Integer pValue = new Integer(nValue);
		writeDetail(pValue.toString());
	}

	public void writeDetailColSpan(String strValue, int nColSpan) throws IOException
	{
		if (null == strValue)
			strValue = "&nbsp;";

		openRow();
		write("<TD CLASS=\"detail-center\" COLSPAN=\"" + nColSpan + "\">" + strValue + "</TD>");
		closeRow();
	}

	public void writeDetailHeader(String strValue) throws IOException
	{ writeDetailHeader(strValue, 1); }

	public void writeDetailHeader(String strValue, int nColSpan) throws IOException
	{
		if (null == strValue)
			strValue = "&nbsp;";

		write("<TD CLASS=\"header\" COLSPAN=\"" + nColSpan + "\">" + strValue + "</TD>");
	}

	public void writeDetailBlank(int nColSpan) throws IOException
	{ writeLine("<TD COLSPAN=" + nColSpan + ">&nbsp;</TD>"); }

	public void openTable() throws IOException { writeLine("<TABLE CELLSPACING=\"0\">"); }
	public void closeTable() throws IOException { writeLine("</TABLE>"); }

	public void openRow() throws IOException { writeLine("<TR>"); }
	public void closeRow() throws IOException { writeLine("</TR>"); }

	public String createLink(String strUrl, String strValue)
	{ return "<A HREF=\"" + strUrl + "\">" + strValue + "</A>"; }

	public String createName(String strName, String strValue)
	{ return "<A NAME=\"" + strName + "\">" + strValue + "</A>"; }

	/******************************************************************************
	*
	*	Action methods
	*
	*****************************************************************************/

	public void run()
		throws SQLException, IOException
	{
		// Local variables.
		Tables pTables = null;
		
		if (null == schemaNamePattern)
			pTables = new Tables(connectionFactory);
		else
			pTables = new Tables(connectionFactory, (String[]) null, schemaNamePattern);

		warnings = new ArrayList<Exception>();

		pTables.load(true);

		writeHeader();
		writeContents(pTables);
		run(pTables);
		writeFooter();
	}

	public void writeHeader() throws SQLException, IOException
	{
		// Get a title for the document.
		String strCatalog = null;

		try (final Connection pConnection = connectionFactory.getConnection())
		{
			strCatalog = pConnection.getCatalog();
		}

		writeLine("<HTML>");
		writeLine("<HEAD>");
		writeLine("<TITLE>Database Structure Document</TITLE>");
		writeLine();
		writeLine("<STYLE TYPE=\"text/css\">");
		writeLine("\tH1, H2, TD.header { font-family: verdana,arial,helvetica,sans-serif }");
		writeLine("\tH1 { font-size: 18pt }");
		writeLine("\tH2 { font-size: 14pt }");
		writeLine("\tA { font-size: 9pt }");
		writeLine("\tTABLE { display: table; padding: 0; border-style: solid; border-width: 1px; border-color: black; width: 100% }");
		writeLine("\tTD { display: table-cell; padding: 3; border-style: solid; border-width: 1px; border-color: black; empty-cells: show; }");
		writeLine("\tTD.detail, TD.detail-center, TD.header { font-size: 9pt; vertical-align: text-top }");
		writeLine("\tTD.detail { text-align: left }");
		writeLine("\tTD.detail-center { text-align: center }");
		writeLine("\tTD.header { text-align: center; font-weight: bold; background-color: silver }");
		writeLine("</STYLE>");
		writeLine();		
		writeLine("</HEAD>");
		writeLine("<BODY LINK=\"Blue\" ALINK=\"Blue\" VLINK=\"Blue\">");
		writeLine("<H1>" + strCatalog + "</H1>");
	}

	public void writeContents(Tables records) throws SQLException, IOException
	{
		writeLine(createName("Contents", "<H2>Contents</H2>"));

		openTable();

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Table Name");
		writeDetailHeader("Comments");
		writeDetailHeader("# Records");
		closeRow();

		String count = null;
		NumberFormat formatter = NumberFormat.getNumberInstance();

		try (final Connection connection = connectionFactory.getConnection())
		{
			connection.setReadOnly(true);
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			for (int i = 0; i < records.size(); i++)
			{
				Tables.Record record = (Tables.Record) records.item(i);
				Long count_ = count(connection, record);
				if (null == count_)
					count = "N/A";
				else
					count = formatter.format(count_);
	
				openRow();
				writeDetail(i + 1);
				writeDetail(createLink("#" + record.getName(), record.getName()));
				writeDetail(record.getRemarks());
				writeDetail(count);
				closeRow();
	
				System.out.println(record.getName() + " - Row Count: " + count);
			}
		}

		out.flush();

		closeTable();
	}

	public Long count(Connection connection, Tables.Record table) throws SQLException
	{
		ResultSet rs = null;
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(90);	// Some counts just take too long.

		try
		{
			rs = stmt.executeQuery("SELECT COUNT(1) FROM " + table.getName());
			if (rs.next())
				return rs.getLong(1);
	
			return 0L;
		}

		catch (SQLException ex) { /** Assume a timeout. */ return null; }
		finally
		{
			if (null != rs) rs.close();
			if (null != stmt) stmt.close();
		}
	}

	public void run(Tables pTables) throws SQLException, IOException
	{
		for (int i = 0; i < pTables.size(); i++)
		{
			Tables.Record record = (Tables.Record) pTables.item(i);
			long time = System.currentTimeMillis();
			System.out.println("Starting output of table - " + record.getName() + " ...");
			run(record, i);
			System.out.println("Finished output of table = " + record.getName() + " in " + (((float) (System.currentTimeMillis() - time)) / 1000f) + " seconds.");
			out.flush();
		}
	}

	public void run(Tables.Record pTable, int nIndex) throws SQLException, IOException
	{
		writeLine("<DIV STYLE=\"page-break-before:always\">");
		writeLine(createName(pTable.getName(), "<H2>" + pTable.getName() + "</H2>"));
		run(pTable.getColumns());
		writeBreak();
		run(pTable.getIndexes(), pTable.getPrimaryKeys());
		writeBreak();
		run(pTable.getImportedKeys());
		writeBreak();
		run(pTable.getExportedKeys());

		writeBreak();
		writeLine(createLink("#Contents", "Goto Contents"));
		writeLine("</DIV>");
	}

	public void run(final List<Column> columns) throws SQLException, IOException
	{
		openTable();
		openRow();
		writeDetailHeader("Columns", 7);
		closeRow();

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Name");
		writeDetailHeader("Data Type");
		writeDetailHeader("Size");
		writeDetailHeader("Decimal");
		writeDetailHeader("Nullable");
		writeDetailHeader("Comments");
		closeRow();

		final int[] i = new int[] { 0 };
		columns.forEach(o -> run(o, i[0]++));

		closeTable();
	}

	public void run(final Column pColumn, final int nIndex)
	{
		try
		{
			openRow();
			writeDetail(nIndex + 1);
			writeDetail(pColumn.name);
			writeDetail(pColumn.typeName);
			writeDetail(pColumn.size);
			writeDetail(pColumn.decimalDigits);
			writeDetail(pColumn.nullable);
			writeDetail(pColumn.remarks);
			closeRow();
		}
		catch (IOException ex) { throw new RuntimeException(ex); }
	}

	public void run(final Index pIndexes, final List<PrimaryKey> primaryKeys) throws SQLException, IOException
	{
		String strUnsupportedException = null;

		// DO NOT ABORT. Indexes and foreign keys errors are only warnings.
		try
		{
			pIndexes.load();
		}

		catch (SQLException pEx)
		{
			warnings.add(pEx);
			return;
		}

		openTable();
		openRow();
		writeDetailHeader("Indexes", 5);
		closeRow();

		// If supported, but no indexes found, use the String variable
		// to display the message.
		if ((null == strUnsupportedException) && (0 == pIndexes.size()))
			strUnsupportedException = "None";

		if (null != strUnsupportedException)
		{
			writeDetailColSpan(strUnsupportedException, 5);
			closeTable();
			return;
		}

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Name");
		writeDetailHeader("Unique?");
		writeDetailHeader("Primary Key?");
		writeDetailHeader("Column");
		closeRow();

		for (int i = 0; i < pIndexes.size(); i++)
			run((Index.Record) pIndexes.item(i), primaryKeys, i);

		closeTable();
	}

	public void run(final Index.Record pIndex, final List<PrimaryKey> primaryKeys, final int nIndex) throws SQLException, IOException
	{
		openRow();
		writeDetail(nIndex + 1);
		writeDetail(pIndex.getName());
		writeDetail(pIndex.isUnique());

		if ((null == primaryKeys) || (0 == primaryKeys.size()) ||
		    (null == primaryKeys.get(0).name))
			writeDetail(false);
		else
			writeDetail(primaryKeys.get(0).name.equals(pIndex.getName()));

		// The run(Keys.Record) function closes the row.
		run(pIndex.getKeys());
		closeRow();
	}

	public void run(Key pKeys) throws SQLException, IOException
	{
		StringBuffer keysText = new StringBuffer();

		for (int i = 0; i < pKeys.size(); i++)
		{
			if (0 < i)
				keysText.append(", ");

			keysText.append(((Key.Record) pKeys.item(i)).getName());
		}

		writeDetail(keysText.toString());
	}

	/*
	public void run(Keys.Record pKey, int nIndex) throws SQLException, IOException
	{
		if (0 < nIndex)
			writeDetailBlank(4);

		writeDetail(pKey.getName());
		closeRow();
	}
	*/

	public void run(ImportedKeys pImportedKeys) throws SQLException, IOException
	{
		String strUnsupportedException = null;

		// DO NOT ABORT. Indexes and foreign keys errors are only warnings.
		try { pImportedKeys.load(); }

		catch (SQLException pEx)
		{
			warnings.add(pEx);
			return;
		}

		openTable();
		openRow();
		writeDetailHeader("Imported Keys", 5);
		closeRow();

		// If supported, but no imported keys found, use the String variable
		// to display the message.
		if ((null == strUnsupportedException) && (0 == pImportedKeys.size()))
			strUnsupportedException = "None";

		if (null != strUnsupportedException)
		{
			writeDetailColSpan(strUnsupportedException, 5);
			closeTable();
			return;
		}

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Name");
		writeDetailHeader("Column");
		writeDetailHeader("References");
		writeDetailHeader("Reference Column");
		closeRow();

		for (int i = 0; i < pImportedKeys.size(); i++)
			run((ImportedKeys.Record) pImportedKeys.item(i), i);

		closeTable();
	}

	public void run(ImportedKeys.Record pImportedKey, int nIndex) throws SQLException, IOException
	{
		String strTable = pImportedKey.getTable_PK();

		openRow();
		writeDetail(nIndex + 1);
		writeDetail(pImportedKey.getName());
		run(pImportedKey.getColumns_FK());
		writeDetail(createLink("#" + strTable, strTable));
		run(pImportedKey.getColumns_PK());
		closeRow();
	}

	public void run(ExportedKeys pExportedKeys) throws SQLException, IOException
	{
		String strUnsupportedException = null;

		// DO NOT ABORT. Indexes and foreign keys errors are only warnings.
		try { pExportedKeys.load(); }

		catch (SQLException pEx)
		{
			warnings.add(pEx);
			return;
		}

		openTable();
		openRow();
		writeDetailHeader("Exported Keys", 4);
		closeRow();

		// If supported, but no exported key found, use the String variable
		// to display the message.
		if ((null == strUnsupportedException) && (0 == pExportedKeys.size()))
			strUnsupportedException = "None";

		if (null != strUnsupportedException)
		{
			writeDetailColSpan(strUnsupportedException, 4);
			closeTable();
			return;
		}

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Name");
		writeDetailHeader("References");
		writeDetailHeader("Reference Column");
		closeRow();

		for (int i = 0; i < pExportedKeys.size(); i++)
			run((ExportedKeys.Record) pExportedKeys.item(i), i);

		closeTable();
	}

	public void run(ExportedKeys.Record pExportedKey, int nIndex) throws SQLException, IOException
	{
		String strTable = pExportedKey.getTable_FK();

		openRow();
		writeDetail(nIndex + 1);
		writeDetail(pExportedKey.getName());
		writeDetail(createLink("#" + strTable, strTable));
		writeDetail(pExportedKey.getColumn_FK());
		closeRow();
	}

	public void writeFooter() throws SQLException, IOException
	{
		writeLine("</BODY>");
		writeLine("</HTML>");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	public DataSource getDataSource() { return connectionFactory; }
	public PrintWriter getWriter() { return out; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	public void setDataSource(DataSource pNewValue) { connectionFactory = pNewValue; }
	public void setWriter(OutputStream pNewValue) { setWriter(new PrintWriter(pNewValue)); }
	public void setWriter(PrintWriter pNewValue) { out = pNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	private DataSource connectionFactory = null;
	private PrintWriter out = null;
	private String schemaNamePattern = null;
	private List<Exception> warnings = null;

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

	public static void main(String strArgs[])
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
			String strSchemaNamePattern = null;

			if (3 < strArgs.length)
				strPassword = strArgs[3];

			if (4 < strArgs.length)
				strDriver = strArgs[4];
			else
				strUrl = "jdbc:odbc:" + strUrl;

			if (5 < strArgs.length)
				strSchemaNamePattern = strArgs[5];

			DataSource pDataSource = DataCollection.createDataSource(strDriver,
				strUrl, strUserName, strPassword);
			PrintWriter pWriter = new PrintWriter(new FileWriter(strFile));

			(new TablesHtml(pDataSource, pWriter,
				strSchemaNamePattern)).run();

			pWriter.close();
		}

		catch (IllegalArgumentException pEx)
		{
			System.out.println("Usage: java " + TablesHtml.class.getName() + " HTML_File");
			System.out.println("\tJDBC_Url");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
