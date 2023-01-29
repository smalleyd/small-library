package com.small.library.json;

import java.io.*;

/** Generates an Elasticsearch data access object from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/26/2023
 *
 */

public class JSONElasticMapping extends JSONBase
{
	public JSONElasticMapping(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);
	}

	@Override
	public void run()
	{
		out.println("{\"properties\":{");

		var i = 0;
		var size = clazz.fields.size();
		for (var v : clazz.fields)
		{
			if (0 == i)
				out.print("  \"" + v.name + "\":{\"type\":\"keyword\"}");
			else if (v.string())
				out.print("  \"" + v.name + "\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}}");
			else if (v.date())
				out.print("  \"" + v.name + "\":{\"type\":\"date\"}");
			else
				out.print("  \"" + v.name + "\":{\"type\":\"" + v.type + "\"}");

			if (++i < size) out.println(",");
			else out.println();
		}

		out.println("}}");
	}
}
