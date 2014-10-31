package com.small.library.rest;

import static com.small.library.rest.FetcherServlet.*;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.*;

import com.small.library.biz.ValidationException;
import com.small.library.rest.XmlBuilder;

/** Filter class that serializes the contents of the fetcher.data request attribute to XML.
 *
 * @author David Small
 * @version 1.1
 * @since 1/23/2009
 *
 */

public class XmlFilter implements Filter
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Logger. */
	public static Log log = LogFactory.getLog(XmlFilter.class);

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

		try
		{
			chain.doFilter(_request, _response);
			Object data = _request.getAttribute(ATTR_FETCHER_DATA);

			// Do NOT output anything for NULL.
			if (null == data)
				return;

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(builder.create(data)), new StreamResult(response.getWriter()));
		}

		catch (Exception ex)
		{
			Throwable cause = ex.getCause();
			if (null == cause)
				cause = ex;

			if (cause instanceof ValidationException)
			{
				try
				{
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.transform(new DOMSource(builder.create(cause)), new StreamResult(response.getWriter()));

					return;
				}
				catch (Exception e) { ex = e; }
			}

			log.error(ex.getMessage(), ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	/** Returns the content's MIME type. Can be overridden.
	 * By default it returns <i>application/json</i>.
	 */
	public String getMimeType() { return "text/xml"; }

	/** JSON builder. */
	private XmlBuilder builder = new XmlBuilder();
}
