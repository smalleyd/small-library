package com.small.library.rest;

import static java.lang.reflect.Modifier.*;

import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.Date;
import java.util.regex.Pattern;

/** Generates JSON based on a supplied object. Uses reflection to output the 
 * public, non-static member variables.
 * 
 * @author David Small
 * @version 3.0
 * @since 11/24/2008
 *
 */

@SuppressWarnings("unchecked")
public class JsonBuilder
{
	/** Pre-compiled Regular Expression pattern - to escape backslashes. */
	private static final Pattern SLASH = Pattern.compile("\\\\");

	/** Pre-compiled Regular Expression pattern - to escape single quotes. */
	private static final Pattern QUOTE = Pattern.compile("\"");

	/** Pre-compiled Regular Expression pattern - to escape carriage returns. */
	private static final Pattern CARRIAGE_RETURN = Pattern.compile("\r\n");

	/** Pre-compiled Regular Expression pattern - to escape new lines. */
	private static final Pattern NEW_LINE = Pattern.compile("\n");

	/** Outputs the specified object to the supplied PrintWriter. */
	public void output(Object value, PrintWriter out)
	{
		Class<? extends Object> clazz = value.getClass();

		// Get all the fields, including from the super classes.
		Field[] fields = clazz.getFields();

		out.print("{");

		boolean started = false;
		for (Field field : fields)
		{
			int modifiers = field.getModifiers();

			if (!isPublic(modifiers) || isStatic(modifiers))
				continue;

			Object item = null;
			try { item = field.get(value); }
			catch (IllegalAccessException ex) { /** Just move to next field. */ continue; }

			// Keep NULL as undefined.
			if (null == item)
				continue;

			if (started)
				out.print(", ");
			else
				started = true;

			// Start the field.
			out.print("\"");
			out.print(field.getName());
			out.print("\"");
			out.print(":");

			// Recursively process items.
			outputItem(item, out);
		}

		out.print("}");
	}

	/** Output a single item. */
	public void outputItem(Object item, PrintWriter out)
	{
		// In case an array/iterable item contains a NULL value.
		if (null == item)
			out.print("undefined");
		else if (item instanceof Iterable)
		{
			Iterable<? extends Object> iterable = (Iterable<? extends Object>) item;
			boolean _started = false;
			out.print("[");
			for (Object it : iterable)
			{
				if (_started)
					out.print(", ");
				else
					_started = true;

				outputItem(it, out);
			}
			out.print("]");
		}
		else if (item.getClass().isArray())
		{
			Object[] iterable = (Object[]) item;
			boolean _started = false;
			out.print("[");
			for (Object it : iterable)
			{
				if (_started)
					out.print(", ");
				else
					_started = true;

				outputItem(it, out);
			}
			out.print("]");
		}
		else if ((item instanceof Number) || (item instanceof Boolean))
			out.print(item.toString());
		else if (item instanceof Date)
		{
			out.print("new Date(");
			out.print(((Date) item).getTime());
			out.print(")");
		}
		else if (item instanceof String)
		{
			out.print("\"");
			out.print(fix((String) item));
			out.print("\"");
		}

		// Leave open the possibility to traverse the object graph and serialize.
		else
			output(item, out);
	}

	/** This fixes a String to fit into a JavaScript literal. It can be overridden to provide alternate formatting. */
	protected String fix(String value)
	{
		return NEW_LINE.matcher(CARRIAGE_RETURN.matcher(QUOTE.matcher(SLASH.matcher(value.trim()).replaceAll("/")).replaceAll("\\\\\"")).
			replaceAll("\\\\n")).replaceAll("\\\\n");
	}
}
