package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.*;

/** Generates a test fixture for validation tests.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 2/14/2023
 *
 */

public class JSONInvalidPatchTest extends JSONBase
{
	public static final String OUTPUT = CHAR_QUOTE + "{\"%s_\":%s}" + CHAR_QUOTE + ",%s," + CHAR_QUOTE + "The request body field, %s_, is not a valid property - [%s]." + CHAR_QUOTE;

	@SuppressWarnings("unused")
	private final String input;
	private final Object[] sampleData;

	public JSONInvalidPatchTest(final JSONConfig conf,
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
		out.println(",`id_1`,`The request body must not be empty`");
		out.println("`{}`,`id_1`,`The request body must not be empty`");

		var i = -1;
		var id = ((String) sampleData[0]).replace('"', CHAR_QUOTE);
		var validFields = clazz.fields.stream().map(f -> f.name).sorted().collect(joining(", "));
		for (var f : clazz.fields)
		{
			out.println(String.format(OUTPUT, f.name, (String) sampleData[++i], id, f.name, validFields));
		}
	}
}
