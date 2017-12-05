package com.small.library.doc;

import static com.small.library.generator.Base.*;

import java.io.*;
import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

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
	private final DBMetadata metadata;
	private final PrintWriter writer;

	/** Constructor - constructs a populated object.
		@param dataSource The database connection's connection factory.
		@param writer HTML document's output stream.
	*/
	public ProceduresHtml(DataSource dataSource, PrintWriter writer)
	{
		metadata = new DBMetadata(dataSource);
		this.writer = writer;
	}

	public void write(String strValue) throws IOException { writer.print(strValue); }
	public void writeLine() throws IOException { writer.println(); }
	public void writeLine(String strValue) throws IOException { writer.println(strValue); }
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
		final List<Procedure> procedures = metadata.getProcedures();

		writeHeader();
		writeContents(procedures);
		run(procedures);
		writeFooter();
	}

	public void writeHeader() throws SQLException, IOException
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
		writeLine("\tTD { display: table-cell; padding: 3; border-style: solid; border-width: 1px; border-color: black; }");
		writeLine("\tTD.detail, TD.detail-center, TD.header { font-size: 9pt; vertical-align: text-top }");
		writeLine("\tTD.detail { text-align: left }");
		writeLine("\tTD.detail-center { text-align: center }");
		writeLine("\tTD.header { text-align: center; font-weight: bold; background-color: silver }");
		writeLine("</STYLE>");
		writeLine();		
		writeLine("</HEAD>");
		writeLine("<BODY LINK=\"Blue\" ALINK=\"Blue\" VLINK=\"Blue\">");
		writeLine("<H1>" + metadata.getCatalog() + "</H1>");
	}

	public void writeContents(final List<Procedure> procedures) throws SQLException, IOException
	{
		writeLine(createName("Contents", "<H2>Contents</H2>"));

		openTable();

		openRow();
		writeDetailHeader("No.");
		writeDetailHeader("Procedure Name");
		writeDetailHeader("Comments");
		closeRow();

		int i = 0;
		for (final Procedure o : procedures)
		{
			openRow();
			writeDetail(i++ + 1);
			writeDetail(createLink("#" + o.name, o.name));
			writeDetail(o.remarks);
			closeRow();
		}

		closeTable();
	}

	public void run(final List<Procedure> procedures) throws SQLException, IOException
	{
		int i = 0;
		for (final Procedure o : procedures)
			run(o, i++);
	}

	public void run(final Procedure procedure, final int index) throws SQLException, IOException
	{
		writeLine("<DIV STYLE=\"page-break-before:always\">");
		writeLine(createName(procedure.name, "<H2>" + procedure.name + "</H2>"));
		runParams(metadata.getParameters(procedure));

		writeBreak();
		writeLine(createLink("#Contents", "Goto Contents"));
		writeLine("</DIV>");
	}

	public void runParams(List<Parameter> parameters) throws SQLException, IOException
	{
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

		int i = 0;
		for (final Parameter o : parameters)
			run(o, i++);

		closeTable();
	}

	public void run(final Parameter parameter, final int index) throws SQLException, IOException
	{
		openRow();
		writeDetail(index + 1);
		writeDetail(parameter.name);
		writeDetail(parameter.typeName);
		writeDetail(parameter.length);
		writeDetail(parameter.scale);
		writeDetail(parameter.nullable);
		writeDetail(parameter.remarks);
		closeRow();
	}

	public void writeFooter() throws SQLException, IOException
	{
		writeLine("</BODY>");
		writeLine("</HTML>");
	}

	public static void main(final String... args)
	{
		try (final PrintWriter writer = new PrintWriter(new FileWriter(extractFile(args, 0, "output"))))
		{
			(new ProceduresHtml(extractDataSource(args, 1), writer)).run();
		}

		catch (IllegalArgumentException ex)
		{
			System.out.println("Usage: java " + ProceduresHtml.class.getName() + " HTML_File");
			System.out.println("\tJDBC_Url");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
		}

		catch (Exception ex) { ex.printStackTrace(); }
	}
}
