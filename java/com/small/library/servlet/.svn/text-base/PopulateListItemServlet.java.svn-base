package com.small.library.servlet;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.small.library.taglib.ListItem;

/**
 * Base class for all servlets which implement the dynamic list feature
 *  
 * @version 	1.5.0.0
 * @author	David Small
 * @date 	12/5/2004
 */

public abstract class PopulateListItemServlet extends RPCServlet
{
	/**
	*  Services a request to generate a javascript to load a listbox
	* 
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		setDefaultHeaders(request, response);
		
		// Start the page.
		PrintWriter out = response.getWriter();

		outputHeader(out);
		outputScript(request, out);
		outputFooter(out);
	}

	/**
	* @see com.theamc.userprofile.servlet.ListServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doGet(request,response);
	}

	/** Implement this function to retrieve required data and generate output script
			@param request The current <I>HttpServletRequest</I> object.
			@param out Print Writer.
			@return a <I>Collection</I> of <I>ListItem</I> objects.
	*/
	protected abstract Collection getListItems(HttpServletRequest request)
		throws ServletException;

	/** Accessor method - gets the name of the javascript method to callback. */
	protected abstract String getCallbackMethodName(HttpServletRequest request)
		throws ServletException;

	/** In closing, we take list values and script name and build javascript function */			
	private void outputScript(HttpServletRequest request, PrintWriter out)
		throws ServletException, IOException
	{
		try
		{
			Collection elements = getListItems(request);
			StringBuffer ids = new StringBuffer();
			StringBuffer values = new StringBuffer();
			Iterator it = elements.iterator();

			while (it.hasNext())
			{
				ListItem element = (ListItem) it.next();

				if (0 < ids.length())
				{
					ids.append(", ");
					values.append(", ");
				}

				String value = element.getListItemDesc();

				// Must escape any quotes.
				if (-1 < value.indexOf("\""))
					value = value.replace('\"', '\'');

				ids.append("\"" + element.getListItemId() + "\"");
				values.append("\"" + value + "\"");					
			}

			out.print("var ids = new Array(");
			out.print(ids);
			out.println(");");
			out.print("var values = new Array(");
			out.print(values);
			out.println(");");
			out.println();
			out.println("window.parent." + getCallbackMethodName(request) + "(ids, values)");
		}

		catch (Exception ex) { outputAlert(out, ex); }
	}
}
