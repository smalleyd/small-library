package com.small.library.json;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.List;

/** Generates a test fixture for indexing and testing values.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/29/2023
 *
 */

public class JSONSearchTest extends JSONBase
{
	public static final String REQUEST = CHAR_QUOTE + "{\"%s\":%s}" + CHAR_QUOTE;
	public static final String REQUEST_EXISTS = CHAR_QUOTE + "{\"has_%s\":true}" + CHAR_QUOTE;
	public static final String REQUEST_DOES_NOT_EXIST = CHAR_QUOTE + "{\"has_%s\":false}" + CHAR_QUOTE;
	public static final String REQUEST_RANGE = CHAR_QUOTE + "{\"%s_from\":%s,\"%s_to\":%s}" + CHAR_QUOTE;
	private final List<Object[]> sampleData;	// Holds generated sample data for use in other test fixtures.

	public JSONSearchTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out, final List<Object[]> sampleData)
	{
		super(conf, clazz, out);

		this.sampleData = sampleData;
	}

	@Override
	public void run()
	{
		var i = -1;
		var oo = sampleData.get(0);
		var ids = ",1," + CHAR_QUOTE + ((String) oo[0]) + CHAR_QUOTE;
		var noIds = ",0,";
		for (var f : clazz.fields)
		{
			var v = oo[++i];
			var invalid = invalid(f, v);
			out.print(String.format(REQUEST, f.name, v));
			out.println(ids);
			out.print(String.format(REQUEST, f.name, invalid));
			out.println(noIds);

			if (f.nullable())
			{
				out.print(String.format(REQUEST_EXISTS, f.name));
				out.println(ids);
				out.print(String.format(REQUEST_DOES_NOT_EXIST, f.name));
				out.println(noIds);
			}

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (f.range)
			{
				out.print(String.format(REQUEST_RANGE, f.name, v, f.name, v));
				out.println(ids);
				out.print(String.format(REQUEST_RANGE, f.name, highend(f, v), f.name, invalid));
				out.println(noIds);
			}
		}
	}

	String invalid(final JSONField field, final Object o)
	{
		var v = (String) o;
		if (field.bool())
		{
			return "" + (!Boolean.valueOf(v).booleanValue());
		}
		else if (field.date())
		{
			return '"' + ZonedDateTime.parse(v.substring(1, v.length() - 1)).plusDays(-1L).toInstant().toString() + '"';
		}
		else if (field.integer())
		{
			return "" + (Long.valueOf(v) - 1000L);
		}
		else if (field.number())
		{
			return "" + (Double.valueOf(v) - 1000d);
		}

		return "\"invalid\"";
	}

	String highend(final JSONField field, final Object o)
	{
		var v = (String) o;
		if (field.bool())
		{
			throw new IllegalArgumentException("Booleans cannot be a Range Field.");
		}
		else if (field.date())
		{
			return '"' + ZonedDateTime.parse(v.substring(1, v.length() - 1)).plusDays(1L).toInstant().toString() + '"';
		}
		else if (field.integer())
		{
			return "" + (Long.valueOf(v) + 1000L);
		}
		else if (field.number())
		{
			return "" + (Double.valueOf(v) + 1000d);
		}

		throw new IllegalArgumentException("Strings cannot be a Range Field.");
	}
}
