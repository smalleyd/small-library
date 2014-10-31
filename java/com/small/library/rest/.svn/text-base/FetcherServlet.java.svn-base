package com.small.library.rest;

import java.io.*;
import java.lang.reflect.Field;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.logging.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/** Base servlet that extracts the Input type from the HTTP Request, uses the
 * input to retrieve data from the backend, and caches in a request attribute
 * to be processed by one of the serializer filters (i.e. JsonFilter). If the Input
 * type is Void, then it assumes there are no input parameters.
 *
 * @author David Small
 * @version 3.0
 * @since 11/24/2008
 *
 * @param <Input> the object extracted from the HTTP request parameters.
 * @param <Output> the object serialized to the HTTP response as JSON.
 */

public abstract class FetcherServlet<Input, Output> extends HttpServlet
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Constant - request attribute name for the fetcher data. */
	public static final String ATTR_FETCHER_DATA = "fetcher.data";

	/** Logger. */
	private final Log log = LogFactory.getLog(this.getClass());

	/** Initialize the servlet. */
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void init() throws ServletException
	{
		super.init();

		InputClass clazz = this.getClass().getAnnotation(InputClass.class);
		if ((null != clazz) && !Void.class.equals(clazz.value()))
			extractor = new HttpExtractor<Input>((Class<Input>) clazz.value());

		// Go through all public methods of this class and set the annotated
		// service beans.
		WebApplicationContext context = ContextLoaderListener.getCurrentWebApplicationContext();
		Field[] fields = this.getClass().getFields();
		for (Field field : fields)
		{
			if (null != field.getAnnotation(Autowired.class))
			{
				Object service = null;
				Class f = field.getType();
				Qualifier qualifier = field.getAnnotation(Qualifier.class);
				if (null == qualifier)
					service = context.getBean(f);
				else
					service = context.getBean(qualifier.value(), f);

				if (null == service)
					log.warn("Could not find Service Bean - " + f.getName() + ".");
				else
					log.info("Found Service Bean - " + f.getName() + ".");

				try { field.set(this, service); }
				catch (Exception ex) { throw new ServletException(ex); }
			}
		}
	}

	/** HttpServlet method - handles the HTTP Get request. */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		Input criteria = null;
		if (null != extractor)
			criteria = extractor.extract(request);

		request.setAttribute(ATTR_FETCHER_DATA, fetch(criteria, request));
	}

	/** HttpServlet method - handles the HTTP Post request. */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doGet(request, response);
	}

	/** Abstract method - Retrieves the object to be serialized. Must be implemented by the subclass. */
	public abstract Output fetch(Input criteria, HttpServletRequest request)
		throws ServletException, IOException;

	/** Extractor object. */
	private HttpExtractor<Input> extractor = null;
}