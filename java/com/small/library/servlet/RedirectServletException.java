package com.small.library.servlet;

import javax.servlet.ServletException;

/***********************************************************************************
*
*	ServletException class that tells the ControllerServlet framework to perform
*	a redirection. Usually thrown from the createModel method when a redirection
*	is needed.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/27/2003
*
***********************************************************************************/

public class RedirectServletException extends ServletException
{
	/** Constructs a populated object.
		@param redirectPage Page to redirect to. Can be relative or absolute.
	*/
	public RedirectServletException(String redirectPage) { this.redirectPage = redirectPage; }

	/** Accessor method - gets the redirect page. */
	public String getRedirectPage() { return redirectPage; }

	/** Member variable - The redirect page. */
	public String redirectPage = null;
}

