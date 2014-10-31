package com.small.library.rest;

import static com.small.library.rest.FetcherServlet.*;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;

import com.small.library.biz.ValidationException;

/** Filter class that serializes the contents of the fetcher.data request attribute to JSON.
 *
 * @author David Small
 * @version 3.0
 * @since 11/25/2008
 *
 */

public class JsonFilter implements Filter
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Logger. */
	public static Log log = LogFactory.getLog(JsonFilter.class);

	/** Constant - unexpected error message. */
	public String UNEXPECTED_ERROR_MESSAGE = "An unexpected error has occurred. An administration has been notified and will fix the problem as soon as possible.";

	/** Initialize the servlet filter. */
	public void init(FilterConfig config) throws ServletException {}

	/** Destroy the servlet filter. */
	public void destroy() {}

	/** HttpServlet method - handles the HTTP Get request. */
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain)
		throws ServletException, IOException
	{
		HttpServletResponse response = (HttpServletResponse) _response;
		response.setDateHeader("Expires", 0L);
		response.setHeader("Cache-Control", "no-cache");		
		response.setHeader("Pragma", "no-cache");
		response.setContentType(getMimeType());

		PrintWriter out = response.getWriter();

		try
		{
			chain.doFilter(_request, _response);
			Object data = _request.getAttribute(ATTR_FETCHER_DATA);
			if (null != data)
			{
				// MUST wrap JSON is parenthesis in order for the eval function to process it properly.
				out.print("(");
				builder.output(data, out);
				out.print(")");
			}
			else
				out.print("undefined");
		}
		catch (Exception ex)
		{
			Throwable cause = ex.getCause();
			if (null == cause)
				cause = ex;

			out.print("throw ");
			if ((cause instanceof FieldFormatException) || (cause instanceof ValidationException))
				builder.output(cause, out);
			else
			{
				log.error(cause.getMessage(), cause);
				out.print("\"");
				out.print(getUnexpectedErrorMessage());
				out.print("\"");
				doPostError(ex);
			}
		}

		out.flush();
		out.close();
	}

	/** Callback for non validation exceptions.
	 * 
	 * @param ex
	 */
	public void doPostError(Throwable ex) {}

	/** Returns the content's MIME type. Can be overridden.
	 * By default it returns <i>application/json</i>.
	 */
	public String getMimeType() { return "application/json"; }

	/** The message returned for non-validation/runtime errors. Can be overridden by subclasses. */
	public String getUnexpectedErrorMessage() { return UNEXPECTED_ERROR_MESSAGE; }

	/** JSON builder. */
	private JsonBuilder builder = new JsonBuilder();
}
