package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/** Generates a test fixture for validation tests.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 2/14/2023
 *
 */

public class JSONInvalidTest extends JSONBase
{
	public static final String OUTPUT = CHAR_QUOTE + "%s" + CHAR_QUOTE + "," + CHAR_QUOTE + "%s %s" + CHAR_QUOTE;

	private final String input;
	private final Object[] sampleData;

	public JSONInvalidTest(final JSONConfig conf,
		final JSONClass clazz,
		final PrintStream out,
		final String input,
		final Object[] sampleData)
	{
		super(conf, clazz, out);

		this.input = input;
		this.sampleData = sampleData;
	}

	@Override
	public void run()
	{
		var i = -1;
		for (var f : clazz.fields)
		{
			var sample = (String) sampleData[++i];
			if (f.notBlank)
			{
				out.println(String.format(OUTPUT, input.replaceAll(sample, "null"), f.name, "must not be blank"));
				out.println(String.format(OUTPUT, input.replaceAll(sample, "\"\""), f.name, "must not be blank"));
				out.println(String.format(OUTPUT, input.replaceAll(sample, "\"   \""), f.name, "must not be blank"));
			}
			else if (f.notEmpty)
			{
				out.println(String.format(OUTPUT, input.replaceAll(sample, "null"), f.name, "must not be empty"));
				out.println(String.format(OUTPUT, input.replaceAll(sample, "\"\""), f.name, "must not be empty"));
			}
			else if (f.notNull)
				out.println(String.format(OUTPUT, input.replaceAll(sample, "null"), f.name, "must not be null"));

			if (f.email)
				out.println(String.format(OUTPUT, input.replaceAll(sample, "\"not_email\""), f.name, "must be a well-formed email address"));

			if (null != f.min)
				out.println(String.format(OUTPUT, input.replaceAll(sample, "" + (f.min - 1)), f.name, "must be greater than or equal to " + f.min));
			if (null != f.max)
				out.println(String.format(OUTPUT, input.replaceAll(sample, "" + (f.max + 1)), f.name, "must be less than or equal to " + f.max));
				
			if (null != f.decimalMin)
			{
				var v = Double.valueOf(f.decimalMin);
				out.println(String.format(OUTPUT, input.replaceAll(sample, "" + (v - 1d)), f.name, "must be greater than or equal to " + f.decimalMin));
			}
			if (null != f.decimalMax)
			{
				var v = Double.valueOf(f.decimalMax);
				out.println(String.format(OUTPUT, input.replaceAll(sample, "" + (v + 1)), f.name, "must be less than or equal to " + f.decimalMax));
			}

			if ((null != f.sizeMin) || (null != f.sizeMax))
			{
				var min = (null != f.sizeMin) ? f.sizeMin : 0;
				var max = (null != f.sizeMax) ? f.sizeMax : Integer.MAX_VALUE;
				var message = "size must be between " + min + " and " + max;

				if (1 < min)
					out.println(String.format(OUTPUT, input.replaceAll(sample, '"' + StringUtils.repeat('a', min - 1) + '"'), f.name, message));
				if (100000 >= max)
					out.println(String.format(OUTPUT, input.replaceAll(sample, '"' + StringUtils.repeat('a', max + 1) + '"'), f.name, message));
			}

			if (null != f.pattern)	// TODO: need to determine a better value than the static 'invalid'. DLS on 2/14/2023.
				out.println(String.format(OUTPUT, input.replaceAll(sample, "\"invalid\""), f.name, "must match \"" + f.pattern + "\""));
		}
	}
}
