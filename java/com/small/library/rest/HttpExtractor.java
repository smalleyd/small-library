package com.small.library.rest;

import static java.lang.reflect.Modifier.*;

import java.lang.reflect.*;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/** Generically processes an HTTP request and populates the supplied class with the values.
 * Uses reflection to populate the public, non-static member variables.
 * 
 * @author David Small
 * @version 3.0
 * @since 11/24/2008
 *
 */

public class HttpExtractor<T>
{
	/** Populates with the class to extract on each request. */
	public HttpExtractor(Class<T> clazz) { this.clazz = clazz; }

	/** Extracts the HTTP request parameters based on the class definition.
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws FieldFormatException
	 */
	public T extract(HttpServletRequest request) throws ServletException
	{
		T value = newInstance();

		// Get all the fields, including from the super classes.
		Field[] fields = clazz.getFields();

		for (Field field : fields)
		{
			int modifiers = field.getModifiers();
			if (!isPublic(modifiers) || isStatic(modifiers))
				continue;

			String name = field.getName();
			String param = request.getParameter(name);

			// If NULL, assume that the POJO is constructed with default values.
			if ((null == param) || (0 == param.length()))
				continue;

			Class<? extends Object> c = field.getType();

			// Get the values.
			try
			{
				if (c.isArray())
				{
					Class<? extends Object> componentType = c.getComponentType();
					String[] params = request.getParameterValues(name);
					Object items = Array.newInstance(componentType, params.length);

					for (int i = 0; i < params.length; i++)
					{
						param = params[i];

						if (boolean.class.equals(componentType))
							Array.setBoolean(items, i, Boolean.valueOf(param));
						else if (short.class.equals(componentType))
							Array.setShort(items, i, Short.valueOf(param));
						else if (int.class.equals(componentType))
							Array.setInt(items, i, Integer.valueOf(param));
						else if (long.class.equals(componentType))
							Array.setLong(items, i, Long.valueOf(param));
						else if (float.class.equals(componentType))
							Array.setFloat(items, i, Float.valueOf(param));
						else if (double.class.equals(componentType))
							Array.setDouble(items, i, Double.valueOf(param));
						else if (String.class.equals(componentType))
							Array.set(items, i, param);
						else
							Array.set(items, i, getObject(componentType, param));
					}

					field.set(value, items);
				}
				else if (boolean.class.equals(c))
					field.setBoolean(value, Boolean.valueOf(param));
				else if (short.class.equals(c))
					field.setShort(value, Short.valueOf(param));
				else if (int.class.equals(c))
					field.setInt(value, Integer.valueOf(param));
				else if (long.class.equals(c))
					field.setLong(value, Long.valueOf(param));
				else if (float.class.equals(c))
					field.setFloat(value, Float.valueOf(param));
				else if (double.class.equals(c))
					field.setDouble(value, Double.valueOf(param));
				else if (String.class.equals(c))
					field.set(value, param);
				else
					field.set(value, getObject(c, param));
			}

			catch (IllegalAccessException ex) { continue; /** Move to the next field. */ }
			catch (NumberFormatException ex)
			{
				throw new FieldFormatException(name, c, param);
			}
		}

		return value;
	}

	/** Create a new instance of the POJO. Can be overridden to provide special construction. */
	public T newInstance() throws ServletException
	{
		try { return clazz.newInstance(); }
		catch (IllegalAccessException ex) { throw new ServletException(ex); }
		catch (InstantiationException ex) { throw new ServletException(ex); }
	}

	/** Converts the String parameter to the appropriate class. It assumes that the class
	 * has a String constructor. In the case of a Date field, it assumes that the parameter
	 * is a long.
	 */
	protected Object getObject(Class<? extends Object> clazz, String param)
		throws ServletException
	{
		if (Date.class.equals(clazz))
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(Long.parseLong(param)));

			// This is necessary because of day-light-savings overlapping periods. For example,
			// if it's March 10, 2009 and your adding a run for March 3, 2009 - it'll lose an hour.
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			if (23 == hour)
				cal.add(Calendar.HOUR_OF_DAY, 1);
			else if (1 == hour)
				cal.add(Calendar.HOUR_OF_DAY, -1);

			return cal.getTime();
		}

		try
		{
			Constructor<? extends Object> constructor = clazz.getConstructor(String.class);

			return constructor.newInstance(param);
		}

		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex)
		{
			Throwable cause = ex.getCause();
			if (null == cause)
				cause = ex;

			if (cause instanceof NumberFormatException)
				throw (NumberFormatException) cause;

			throw new ServletException(cause);
		}
	}

	/** Definition of output. */
	private Class<T> clazz = null;
}
