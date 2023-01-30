package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.*;
import java.time.Instant;

/** Generates a test fixture for indexing and testing values.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/29/2023
 *
 */

public class JSONIndexTest extends JSONBase
{
	public JSONIndexTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);
	}

	@Override
	public void run()
	{
		var payload = clazz.fields.stream().map(v -> "\"" + v.name + "\":%s").collect(joining(",", "{", "}"));
		var columns = clazz.fields.stream().map(v -> "%s").collect(joining(","));

		for (int i = 0; i < NUM_OF_TESTS; i++)
		{
			var j = i;	// Must be effectively final to be used in the lambda below.
			var values = clazz.fields.stream().map(v -> value(v, j)).toArray(Object[]::new);

			out.print(CHAR_QUOTE);
			out.print(String.format(payload, values));
			out.print(CHAR_QUOTE);
			out.print(",");

			out.println(String.format(columns, values).replace('"', '`'));
		}
	}

	public String value(final JSONField field, final int row)
	{
		if (field.bool())
			return (0 == (System.currentTimeMillis() % 2)) ? "true" : "false";
		else if (field.date())
			return '"' + Instant.now().toString() + '"';
		else if (field.number())
		{
			if ((null != field.min) && (null != field.max))
				return "" + (((long) field.min) + (System.nanoTime() % ((long) (field.max - field.min))) + 1L);
			else if ((null != field.decimalMin) && (null != field.decimalMax))
			{
				var per = ((double) (System.nanoTime() % 100L)) / 100d; 
				var min = Double.parseDouble(field.decimalMin);
				var max = Double.parseDouble(field.decimalMax);

				return "" + (min + ((max - min) * per));
			}
			else
				return "" + System.nanoTime();
		}
		else
			return '"' + field.name + "_" + row + '"';
	}
}
