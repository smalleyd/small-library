package com.small.library.json;

import static org.apache.commons.lang3.StringUtils.repeat;

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
	public static final int MAX_LEVELS = 10;	// Use max levels as a safeguard against infinite recursion. DLS on 2/7/2023.

	private final int level;	// Zero indicates top level (default).

	public JSONElasticMapping(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		this(conf, clazz, out, 0);
	}

	public JSONElasticMapping(final JSONConfig conf, final JSONClass clazz, final PrintStream out, final int level)
	{
		super(conf, clazz, out);

		this.level = level;
	}

	@Override
	public void run()
	{
		var root = (0 == level);
		if (root) out.print("{");

		out.println("\"properties\":{");

		var i = 0;
		var size = clazz.fields.size();
		var indent = repeat("  ", 1 + (level * 2));
		for (var v : clazz.fields)
		{
			out.print(indent);

			if (0 == i)
				out.print("\"" + v.name + "\":{\"type\":\"keyword\"}");
			else if (v.string())
				out.print("\"" + v.name + "\":{\"type\":\"text\",\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}}");
			else if (v.date())
				out.print("\"" + v.name + "\":{\"type\":\"date\"}");
			else if ("int".equals(v.type))
				out.print("\"" + v.name + "\":{\"type\":\"integer\"}");
			else if (v.number() || v.bool())
				out.print("\"" + v.name + "\":{\"type\":\"" + v.type.toLowerCase() + "\"}");
			else
			{
				var c = ((MAX_LEVELS <= level) || clazz.name.equals(v.type)) ?	// Skip over types that are the same as the class name to avoid infinite recursion. DLS on 2/7/2023.
					null :
					conf.clazz(v.type);
				out.print("\"" + v.name + "\":");
				if (null != c)
				{
					out.println("{");
					out.print(repeat("  ", (level + 1) * 2));
					new JSONElasticMapping(conf, c, out, level + 1).run();
				}
				else
					out.print("{\"type\":\"" + v.type + "\"}");
			}

			if (++i < size) out.println(",");
			else out.println();
		}

		if (root)
			out.print("}}");
		else
		{
			out.print(repeat("  ", level * 2));
			out.println("}");
			out.print(repeat("  ", 1 + ((level - 1) * 2)));
			out.print("}");
		}
	}
}
