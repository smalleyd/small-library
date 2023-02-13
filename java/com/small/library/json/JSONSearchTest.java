package com.small.library.json;

import static java.util.stream.Collectors.joining;

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
	public static final String charQuote = "" + CHAR_QUOTE;

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
		var noIds = ",0,";
		var oo = sampleData.get(0);
		var ids = ",1," + ((String) oo[0]).replace('"', CHAR_QUOTE);
		var allIds = "," + sampleData.size() + "," + sampleData.stream()
			.map(o -> ((String) o[0]).replaceAll("\"", ""))
			.collect(joining(",", charQuote, charQuote));
		for (var f : clazz.fields)
		{
			var v = oo[++i];
			var invalid = invalid(f, v);
			out.print(String.format(REQUEST, f.name, v));
			out.println((f.bool() || f.number()) ? finish(i, v) : ids);	// Booleans and Numbers can be repeated for the same field over the span of the sample data.
			out.print(String.format(REQUEST, f.name, invalid));
			out.println((f.bool() || f.number()) ? finish(i, invalid) : noIds);	// Booleans and Numbers can be repeated for the same field over the span of the sample data.

			if (f.nullable())
			{
				out.print(String.format(REQUEST_EXISTS, f.name));
				out.println(allIds);	// All fields should be populated by default. DLS on 2/10/2023.
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

	String finish(final int i, final Object v)	// Finish the search line with the number of occurrences of a particular value and the IDs associated with it.
	{
		var count = new int[] { 0 };
		var value = sampleData
			.stream()
			.filter(o -> o[i].equals(v))
			.peek(o -> count[0]++)
			.map(o -> ((String) o[0]).replaceAll("\"", ""))
			.collect(joining(",", charQuote, charQuote));

		return "," + count[0] + "," + value;
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
