package com.small.library.doc;

import static com.small.library.generator.Base.*;

import java.io.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
	private final DataSource dataSource;
	private final PrintWriter out;
	private final String schemaNamePattern;

	/** Constructor - constructs a populated object.
		@param dataSource The database connection's connection factory.
		@param writer HTML document's output stream.
		@param strSchemaName Schema Name pattern to filter the tables list by.
			Use <CODE>null</CODE> for no filter.
	*/
	public TablesHtml(DataSource dataSource, PrintWriter writer,
		String schemaNamePattern)
	{
		this.dataSource = dataSource;
		out = writer;
		this.schemaNamePattern = schemaNamePattern;
	}

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

	public String createLink(String url, String strValue)
	{ return "<A HREF=\"" + url + "\">" + strValue + "</A>"; }

	public String createName(String name, String strValue)
	{ return "<A NAME=\"" + name + "\">" + strValue + "</A>"; }

	public void run()
		throws SQLException, IOException
	{
		final DBMetadata metadata = new DBMetadata(dataSource);
		final List<Table> tables = metadata.getTables(schemaNamePattern);
		
		writeHeader(metadata.getCatalog());
		writeContents(tables);
		run(tables);
		writeFooter();
	}

	private void writeHeader(final String catalog) throws SQLException, IOException
	{
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
		writeLine("<H1>" + catalog + "</H1>");
	}

	private void writeContents(final List<Table> records) throws SQLException, IOException
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
		final NumberFormat formatter = NumberFormat.getNumberInstance();

		int i = 1;
		for (final Table record : records)
		{
			Long count_ = count(record);
			if (null == count_)
				count = "N/A";
			else
				count = formatter.format(count_);

			openRow();
			writeDetail(i++);
			writeDetail(createLink("#" + record.name, record.name));
			writeDetail(record.remarks);
			writeDetail(count);
			closeRow();

			System.out.println(record.name + " - Row Count: " + count);
		}

		out.flush();

		closeTable();
	}

	private Long count(final Table table) throws SQLException
	{

		try (final Connection connection = dataSource.getConnection();
		     final Statement stmt = connection.createStatement())
		{
			stmt.setQueryTimeout(90);	// Some counts just take too long.
		    try (final ResultSet rs = stmt.executeQuery("SELECT COUNT(1) FROM " + table.name))
		    {
		    	if (rs.next()) return rs.getLong(1);
	
		    	return 0L;
		    }
			catch (SQLException ex) { /** Assume a timeout. */ return null; }
		}
	}

	private void run(final List<Table> tables) throws SQLException, IOException
	{
		int i = 0;
		for (final Table record : tables)
		{
			long time = System.currentTimeMillis();
			System.out.println("Starting output of table - " + record.name + " ...");
			run(record, i++);
			System.out.println("Finished output of table = " + record.name + " in " + (((float) (System.currentTimeMillis() - time)) / 1000f) + " seconds.");
			out.flush();
		}
	}

	private void run(final Table table, final int index) throws SQLException, IOException
	{
		writeLine("<DIV STYLE=\"page-break-before:always\">");
		writeLine(createName(table.name, "<H2>" + table.name + "</H2>"));
		runColumns(table.getColumns());
		writeBreak();
		run(table.getIndexes(), table.getPrimaryKeys());
		writeBreak();
		runImportedKeys(table.getImportedKeys());
		writeBreak();
		runExportedKeys(table.getExportedKeys());

		writeBreak();
		writeLine(createLink("#Contents", "Goto Contents"));
		writeLine("</DIV>");
	}

	private void runColumns(final List<Column> columns) throws SQLException, IOException
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

	private void run(final Column pColumn, final int index)
	{
		try
		{
			openRow();
			writeDetail(index + 1);
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

	private void run(final List<Index> indexes, final List<PrimaryKey> primaryKeys) throws SQLException, IOException
	{
		String strUnsupportedException = null;

		openTable();
		openRow();
		writeDetailHeader("Indexes", 5);
		closeRow();

		// If supported, but no indexes found, use the String variable
		// to display the message.
		if ((null == strUnsupportedException) && (0 == indexes.size()))
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

		int i = 0;
		for (final Index o : indexes)
			run(o, primaryKeys, i++);

		closeTable();
	}

	private void run(final Index index, final List<PrimaryKey> primaryKeys, final int i) throws SQLException, IOException
	{
		openRow();
		writeDetail(i + 1);
		writeDetail(index.name);
		writeDetail(index.unique);

		if ((null == primaryKeys) || (0 == primaryKeys.size()) ||
		    (null == primaryKeys.get(0).name))
			writeDetail(false);
		else
			writeDetail(primaryKeys.get(0).name.equals(index.name));

		// The run(Keys.Record) function closes the row.
		writeKeys(index.keys);
		closeRow();
	}

	private void writeKeys(final List<Key> keys) throws SQLException, IOException
	{
		writeDetail(StringUtils.join(keys, ", "));
	}

	/*
	private void run(final Key key, final int index) throws SQLException, IOException
	{
		if (0 < index)
			writeDetailBlank(4);

		writeDetail(key.name);
		closeRow();
	}
	*/

	private void runImportedKeys(final List<ForeignKey> keys) throws SQLException, IOException
	{
		openTable();
		openRow();
		writeDetailHeader("Imported Keys", 5);
		closeRow();

		if (CollectionUtils.isEmpty(keys))
		{
			writeDetailColSpan("None", 5);
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

		int i = 1;
		for (final ForeignKey o : keys)
		{
			openRow();
			writeDetail(i++);
			writeDetail(o.name);
			writeKeys(o.fks);
			writeDetail(createLink("#" + o.pkTable, o.pkTable));
			writeKeys(o.pks);
			closeRow();
		}

		closeTable();
	}

	private void runExportedKeys(final List<ForeignKey> keys) throws SQLException, IOException
	{
		openTable();
		openRow();
		writeDetailHeader("Exported Keys", 4);
		closeRow();

		if (CollectionUtils.isEmpty(keys))
		{
			writeDetailColSpan("None", 4);
			closeTable();
			return;
		}

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Name");
		writeDetailHeader("References");
		writeDetailHeader("Reference Column");
		closeRow();

		int i = 1;
		for (final ForeignKey o : keys)
		{
			openRow();
			writeDetail(i++);
			writeDetail(o.name);
			writeDetail(createLink("#" + o.fkTable, o.fkTable));
			writeKeys(o.fks);
			closeRow();
		}

		closeTable();
	}

	private void writeFooter() throws SQLException, IOException
	{
		writeLine("</BODY>");
		writeLine("</HTML>");
	}

	public static void main(final String... args)
	{
		try (final PrintWriter writer = new PrintWriter(new FileWriter(extractFile(args, 0, "output"))))
		{
			(new TablesHtml(extractDataSource(args, 1), writer,
				extractArgument(args, 5, null))).run();
		}

		catch (final IllegalArgumentException ex)
		{
			System.out.println("Usage: java " + TablesHtml.class.getName() + " HTML_File");
			System.out.println("\tJDBC_Url");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
