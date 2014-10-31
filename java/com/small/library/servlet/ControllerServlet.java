package com.small.library.servlet;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.small.library.biz.ValidationException;
import com.small.library.taglib.Alert;

/**********************************************************************************
*
*	Base servlet that orchestrates the merging of Data and View. Subclasses
*	must implement <CODE>getModel</CODE> and <CODE>getViews</CODE>
*	methods. <CODE>getModel</CODE> returns a reference to the data
*	used by the views, supplied in <CODE>getViews</CODE>. <CODE>getViews</CODE>
*	returns an array of <I>String</I> objects that each contain an
*	URI to a local JSP or servlet to present the entire page or a section
*	of the page.
*
*	<BR><BR>
*
*	Only <CODE>doGet</CODE> is implemented. Subclasses must implement
*	<CODE>doPost</CODE> to handle posts.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/18/2002
*
***********************************************************************************/

public abstract class ControllerServlet extends HttpServlet
{
	/**************************************************************************
	*
	*	Constants
	*
	**************************************************************************/

	/** Constant - request parameter value for True. */
	public static final String REQUEST_PARAM_VALUE_TRUE = "1";

	/** Constant - true. */
	public static final String TRUE = "1";

	/** Constant - false. */
	public static final String FALSE = "0";

	/** Constant - attribute name for the <I>Model</I> object stashed
	    in the <I>HttpServletRequest</I> object.
	*/
	public static final String ATTR_NAME_MODEL = "MODEL";

	/** Constant - Session attribute name for the <I>Alert</I> object stashed in the
	    <I>HttpServletRequest</I> object.
	*/
	public static final String SESSION_ATTR_NAME_ALERT = "ALERT";

	/** Constant - attribute name for the <I>ValidationException</I> object
	    stashed in the <I>HttpServletRequest</I> object.
	*/
	public static final String ATTR_NAME_VALIDATION_EXCEPTION =
		"VALIDATION_EXCEPTION";

	/** Constant - attribute name for a validation message stashed in the
	    <I>HttpServletRequest</I> object.
	*/
	public static final String ATTR_NAME_VALIDATION_MESSAGE = "VALIDATION_MESSAGE";

	/**************************************************************************
	*
	*	Constants - HTML elements
	*
	**************************************************************************/

	/** Constant - HTML element - non-breaking space. */
	public static final String NBSP = "&nbsp;";

	/**************************************************************************
	*
	*	Static members
	*
	**************************************************************************/

	/**************************************************************************
	*
	*	Request handlers
	*
	**************************************************************************/

	/** Request handler - handles HTTP Get requests. */
	public void doGet(HttpServletRequest request,
		HttpServletResponse response)
			throws ServletException, IOException
	{
		// Use the model in the view or views.
		try { doGet(request, response, createModel(request)); }
		catch (RedirectServletException ex)
		{
			response.sendRedirect(ex.getRedirectPage());
		}
	}

	/** Request handler - handles HTTP Get requests and any validation exceptions
	    caught when handling a Post action.
		@param exception validation exception object if available.
	*/
	public void doGet(HttpServletRequest request,
		HttpServletResponse response, ValidationException exception)
			throws ServletException, IOException
	{
		try { doGet(request, response, createModel(request), exception); }
		catch (RedirectServletException ex)
		{
			response.sendRedirect(ex.getRedirectPage());
		}
	}

	/** Request handler - handles HTTP Get requests. Merges the <I>Model</I>
	    object with the servlet's list of views. Generally called by
	    a validation exception handling routines to redisplay a page.
		@param model views' data.
		@param exception validation exception object if available.
	*/
	protected void doGet(HttpServletRequest request,
		HttpServletResponse response, Model model,
		ValidationException exception)
			throws ServletException, IOException
	{
		// Stash the exception if available.
		if (null != exception)
			setValidationException(request, exception);

		doGet(request, response, model);
	}

	/** Request handler - handles HTTP Get requests. Merges the <I>Model</I>
	    object with the servlet's list of views. Generally called by
	    a validation exception handling routines to redisplay a page.
		@param model views' data.
	*/
	protected void doGet(HttpServletRequest request,
		HttpServletResponse response, Model model)
			throws ServletException, IOException
	{
		// Stash the model object in the request object.
		// Stash first so that can be used by other abstract methods.
		request.setAttribute(ATTR_NAME_MODEL, model);

		// Prepare the page.
		preparePage(request, response, model);

		// Get the pages used to present the view.
		String[] views = getViews(model);

		// Include the page header and navigation.
		outputHeader(request, response, model);

		// Loop through the views and include their output on
		// the page.
		for (int i = 0; i < views.length; i++)
			includePage(request, response, views[i]);

		// Include the page footer.
		outputFooter(request, response, model);
	}

	/**************************************************************************
	*
	*	Abstract methods
	*
	**************************************************************************/

	/** Abstract method - prepares the page for the response.
		@param request The current <I>HttpServletRequest</I> object.
		@param response The current <I>HttpServletResponse</I> object.
		@param model The current request's model (data).
	*/
	protected abstract void preparePage(HttpServletRequest request,
		HttpServletResponse response, Model model)
			throws ServletException, IOException;

	/** Abstract method - subclasses chance to create and populate the page's model.
		@param request The page's request object.
	*/
	protected abstract Model createModel(HttpServletRequest request)
		throws ServletException, IOException;

	/** Abstract method - gets a list of pages or sections to be included
	    in the output. The array should be indexed in the order to be
	    displayed.
		@param model The page's data.
	*/
	protected abstract String[] getViews(Model model)
		throws ServletException, IOException;

	/** Abstract method - outputs the header for the current response.
		@param request The current <I>HttpServletRequest</I> object.
		@param response The current <I>HttpServletResponse</I> object.
		@param model The current request's model (data).
	*/
	protected abstract void outputHeader(HttpServletRequest request,
		HttpServletResponse response, Model model)
			throws ServletException, IOException;

	/** Abstract method - outputs the footer for the current response.
		@param request The current <I>HttpServletRequest</I> object.
		@param response The current <I>HttpServletResponse</I> object.
		@param model The current request's model (data).
	*/
	protected abstract void outputFooter(HttpServletRequest request,
		HttpServletResponse response, Model model)
			throws ServletException, IOException;

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets the current request's model object.
		@param request The current <I>HttpServletRequest</I> object.
	*/
	protected static Model getModel(HttpServletRequest request)
		throws ServletException
	{
		return (Model) request.getAttribute(ATTR_NAME_MODEL);
	}

	/** Accessor method - gets the current request's alert object.
		@param request The current <I>HttpServletRequest</I> object.
	*/
	protected static Alert getAlert(HttpServletRequest request)
		throws ServletException
	{
		return (Alert) request.getSession(true).getAttribute(SESSION_ATTR_NAME_ALERT);
	}

	/** Accessor method - gets the current request's validation exception.
		@param request The current <I>HttpServletRequest</I> object.
	*/
	protected static ValidationException getValidationException(HttpServletRequest request)
		throws ServletException
	{
		return (ValidationException) request.getAttribute(ATTR_NAME_VALIDATION_EXCEPTION);
	}

	/** Accessor method - gets the current request's validation message.
		@param request The current <I>HttpServletRequest</I> object.
	*/
	protected static String getValidationMessage(HttpServletRequest request)
		throws ServletException
	{
		return (String) request.getAttribute(ATTR_NAME_VALIDATION_MESSAGE);
	}

	/**************************************************************************
	*
	*	Mutator methods
	*
	**************************************************************************/

	/** Mutator method - sets the current request's model object.
		@param request The current <I>HttpServletRequest</I> object.
		@param model The current request's model object.
	*/
	protected static void setModel(HttpServletRequest request, Model model)
		throws ServletException
	{
		request.setAttribute(ATTR_NAME_MODEL, model);
	}

	/** Mutator method - sets the current request's alert object.
		@param request The current <I>HttpServletRequest</I> object.
		@param alert The <I>Alert</I> object to cache in the session.
	*/
	protected static void setAlert(HttpServletRequest request, Alert alert)
		throws ServletException
	{
		request.getSession(true).setAttribute(
				SESSION_ATTR_NAME_ALERT, alert);
	}

	/** Mutator method - sets the current request's validation exception.
		@param request The current <I>HttpServletRequest</I> object.
		@param exception The current request's validation exception.
	*/
	protected static void setValidationException(HttpServletRequest request,
		ValidationException exception)
			throws ServletException
	{
		request.setAttribute(ATTR_NAME_VALIDATION_EXCEPTION, exception);
	}

	/** Mutator method - sets the current request's validation message.
		@param request The current <I>HttpServletRequest</I> object.
		@param message The current request's validation message.
	*/
	protected static void setValidationMessage(HttpServletRequest request,
		String message)
			throws ServletException
	{
		request.setAttribute(ATTR_NAME_VALIDATION_MESSAGE, message);
	}

	/**************************************************************************
	*
	*	Helper methods
	*
	**************************************************************************/

	/** Helper method - includes a servlet or JSP page within the current response.
		@param request The current <I>HttpServletRequest</I> object.
		@param response The current <I>HttpServletResponse</I> object.
		@param pageName Name of the servlet or JSP page relative to the current
			context root.
	*/
	protected void includePage(HttpServletRequest request, HttpServletResponse response,
		String pageName)
			throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher(pageName);
		dispatcher.include(request, response);
	}

	/** Helper method - forwards the current request to another servlet or JSP page.
		@param request The current <I>HttpServletRequest</I> object.
		@param response The current <I>HttpServletResponse</I> object.
		@param pageName Name of the servlet or JSP page relative to the current
			context root.
	*/
	protected void forwardPage(HttpServletRequest request, HttpServletResponse response,
		String pageName)
			throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher(pageName);
		dispatcher.forward(request, response);
	}
}
