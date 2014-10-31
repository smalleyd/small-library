package com.small.library.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Base class for all servlets which facilitate a Remote Procedure Call.
 *  
 * @version 	1.0
 * @author	David Small
 * @date 	2/19/2004
 */

public abstract class RPCServlet extends HttpServlet
{
	/** Helper method - sets the expiration and content type headers. */
	protected void setDefaultHeaders(
		HttpServletRequest request, HttpServletResponse response)
			throws ServletException
	{
		// Set the page content.
		response.setDateHeader("Expires", 0L);
		response.setHeader("Cache-Control", "no-cache");		
		response.setContentType("text/html");
	}

	/** Helper method - outputs the header. */
	protected void outputHeader(PrintWriter out)
		throws ServletException, IOException
	{
		out.println("<HTML>");
		out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	}

	/** Helper method - outputs the footer. */
	protected void outputFooter(PrintWriter out)
		throws ServletException, IOException
	{
		out.println("</SCRIPT>");
		out.println("</HTML>");
	}
	
	/** Helper method - outputs the footer. */
	protected void outputAlert(PrintWriter out, Exception ex) 
		throws ServletException, IOException 
	{	
		out.println("window.alert('" + ex.getMessage() + "');");
	}
}
