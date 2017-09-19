package com.small.library.doc;

import java.io.*;
import java.sql.*;

import javax.sql.DataSource;

import com.small.library.data.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Produces HTML documention of a data source's stored procedures and
*	procedure parameters.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 5/31/2002
*
***************************************************************************************/

public class ProceduresHtml
{
	/******************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public ProceduresHtml() { this(null, null); }

	/** Constructor - constructs a populated object.
		@param pDataSource The database connection's connection factory.
		@param pWriter HTML document's output stream.
	*/
	public ProceduresHtml(DataSource pDataSource, PrintWriter pWriter)
	{
		m_DataSource = pDataSource;
		m_Writer = pWriter;
	}

	/******************************************************************************
	*
	*	HTML handling methods
	*
	*****************************************************************************/

	public void write(String strValue) throws IOException { m_Writer.print(strValue); }
	public void writeLine() throws IOException { m_Writer.println(); }
	public void writeLine(String strValue) throws IOException { m_Writer.println(strValue); }
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
		Procedures pProcedures = new Procedures(m_DataSource);

		pProcedures.load(true);

		writeHeader();
		writeContents(pProcedures);
		run(pProcedures);
		writeFooter();
	}

	public void writeHeader() throws SQLException, IOException
	{
		// Get a title for the document.
		String strCatalog = null;
		Connection pConnection = m_DataSource.getConnection();

		try
		{
			strCatalog = pConnection.getCatalog();
		}

		finally
		{
			if (null != pConnection)
				pConnection.close();
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
		writeLine("\tTD { display: table-cell; padding: 3; border-style: solid; border-width: 1px; border-color: black; }");
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

	public void writeContents(Procedures pProcedures) throws SQLException, IOException
	{
		writeLine(createName("Contents", "<H2>Contents</H2>"));

		openTable();

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Procedure Name");
		writeDetailHeader("Comments");
		closeRow();

		for (int i = 0; i < pProcedures.size(); i++)
		{
			Procedures.Record pProcedure = (Procedures.Record) pProcedures.item(i);

			openRow();
			writeDetail(i + 1);
			writeDetail(createLink("#" + pProcedure.getName(), pProcedure.getName()));
			writeDetail(pProcedure.getRemarks());
			closeRow();
		}

		closeTable();
	}

	public void run(Procedures pProcedures) throws SQLException, IOException
	{
		for (int i = 0; i < pProcedures.size(); i++)
			run((Procedures.Record) pProcedures.item(i), i);
	}

	public void run(Procedures.Record pProcedure, int nIndex) throws SQLException, IOException
	{
		writeLine("<DIV STYLE=\"page-break-before:always\">");
		writeLine(createName(pProcedure.getName(), "<H2>" + pProcedure.getName() + "</H2>"));
		run(pProcedure.getParameters());

		writeBreak();
		writeLine(createLink("#Contents", "Goto Contents"));
		writeLine("</DIV>");
	}

	public void run(Parameters pParameters) throws SQLException, IOException
	{
		pParameters.load();

		openTable();
		openRow();
		writeDetailHeader("Parameters", 7);
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

		for (int i = 0; i < pParameters.size(); i++)
			run((Parameters.Record) pParameters.item(i), i);

		closeTable();
	}

	public void run(Parameters.Record pParameter, int nIndex) throws SQLException, IOException
	{
		openRow();
		writeDetail(nIndex + 1);
		writeDetail(pParameter.getName());
		writeDetail(pParameter.getTypeName());
		writeDetail(pParameter.getLength());
		writeDetail(pParameter.getScale());
		writeDetail(pParameter.isNullable());
		writeDetail(pParameter.getRemarks());
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

	public DataSource getDataSource() { return m_DataSource; }
	public PrintWriter getWriter() { return m_Writer; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	public void setDataSource(DataSource pNewValue) { m_DataSource = pNewValue; }
	public void setWriter(OutputStream pNewValue) { setWriter(new PrintWriter(pNewValue)); }
	public void setWriter(PrintWriter pNewValue) { m_Writer = pNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	private DataSource m_DataSource = null;
	private PrintWriter m_Writer = null;

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

			if (3 < strArgs.length)
				strPassword = strArgs[3];

			if (4 < strArgs.length)
				strDriver = strArgs[4];
			else
				strUrl = "jdbc:odbc:" + strUrl;

			DataSource pDataSource = DataCollection.createDataSource(strDriver,
				strUrl, strUserName, strPassword);
			PrintWriter pWriter = new PrintWriter(new FileWriter(strFile));

			(new ProceduresHtml(pDataSource, pWriter)).run();

			pWriter.close();
		}

		catch (IllegalArgumentException pEx)
		{
			System.out.println("Usage: java " + ProceduresHtml.class.getName() + " HTML_File");
			System.out.println("\tJDBC_Url");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
