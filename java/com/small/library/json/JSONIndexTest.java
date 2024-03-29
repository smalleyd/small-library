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
	final List<String> inputs = new ArrayList<>(NUM_OF_TESTS);
	final List<Object[]> sampleData = new ArrayList<>(NUM_OF_TESTS);	// Holds generated sample data for use in other test fixtures.

	public JSONIndexTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out, final int multiplyer)
	{
		super(conf, clazz, out);

		this.multiplyer = multiplyer;
	}

	public static String payload(final JSONField f)
	{
		return f.notContainer() ? "\"" + f.name + "\":%s" : "\"" + f.name + "\":[%s]";
	}

	@Override
	public void run()
	{
		sampleData.clear();

		var payload = clazz.fields.stream().map(v -> payload(v)).collect(joining(",", "{", "}"));
		var columns = clazz.fields.stream().map(v -> "%s").collect(joining(","));

		for (int i = 0; i < NUM_OF_TESTS; i++)
		{
			var j = i;	// Must be effectively final to be used in the lambda below.
			var values = clazz.fields.stream().map(v -> value(v, j)).toArray(Object[]::new);

			var input = String.format(payload, values);
			inputs.add(input);

			out.print(CHAR_QUOTE);
			out.print(input);
			out.print(CHAR_QUOTE);
			out.print(",");

			out.println(String.format(columns, values).replace('"', '`'));

			sampleData.add(values);
		}
	}

	public String value(final JSONField field, final int row)
	{
		var id = row + 1;
		var nano = System.nanoTime();
		var multiplied = id * multiplyer;	// Must increase 'row' to at least one otherwise the multiplier will return the same value for zero.

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
		{
			var v = field.name + "_" + (field.identifier ? id : multiplied);	// ID field should not change for updates. DLS on 2/9/2023.
			return '"' + v + (field.email ? "@test.com" : "") + '"';
		}
	}
}
