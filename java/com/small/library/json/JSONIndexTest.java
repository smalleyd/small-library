package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/** Generates a test fixture for indexing and testing values.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/29/2023
 *
 */

public class JSONIndexTest extends JSONBase
{
	private final int multiplyer;
	private final ZonedDateTime now = ZonedDateTime.now();
	final List<Object[]> sampleData = new ArrayList<>(NUM_OF_TESTS);	// Holds generated sample data for use in other test fixtures.

	public JSONIndexTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out, final int multiplyer)
	{
		super(conf, clazz, out);

		this.multiplyer = multiplyer;
	}

	@Override
	public void run()
	{
		sampleData.clear();

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

			sampleData.add(values);
		}
	}

	public String value(final JSONField field, final int row)
	{
		var nano = System.nanoTime();
		var multiplied = (row + 1) * multiplyer;	// Must increase 'row' to at least one otherwise the multiplier will return the same value for zero.

		if (field.bool())
			return (0 == (System.currentTimeMillis() % 2)) ? "true" : "false";
		else if (field.date())
		{
			var dt = now.withHour((int) (nano % 24L)).withMinute(multiplied % 60);
			return '"' + dt.toInstant().toString() + '"';
		}
		else if (field.number())
		{
			if ((null != field.min) && (null != field.max))
				return "" + (((long) field.min) + (nano % ((long) (field.max - field.min))) + 1L);
			else if ((null != field.decimalMin) && (null != field.decimalMax))
			{
				var per = ((double) (nano % 100L)) / 100d; 
				var min = Double.parseDouble(field.decimalMin);
				var max = Double.parseDouble(field.decimalMax);

				return "" + (min + ((max - min) * per));
			}
			else
				return "" + nano;
		}
		else
			return '"' + field.name + "_" + multiplied + '"';
	}
}
