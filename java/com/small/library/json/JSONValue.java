package com.small.library.json;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/** Generates a Java value object from a JSON document.
 * 
 * @author smalleyd
 * @version 2.0
 * @since 9/19/2017
 *
 */

public class JSONValue implements Runnable
{
	private final JSONConfig conf;
	private final PrintStream out;

	public JSONValue(final JSONConfig conf, final PrintStream out)
	{
		this.conf = conf;
		this.out = out;
	}

	public void run()
	{
		out.print("package "); out.print(conf.packageName); out.println(";");
		out.println();
		out.println("import java.io.Serializable;");
		out.println();
		out.println("import com.fasterxml.jackson.annotation.JsonProperty;");
		out.println();
		out.println("/** Value object that represents a .");
		out.println(" * ");
		if (null != conf.author)
		{
			out.print(" * @author "); out.println(conf.author);
		}
		out.println(" * ");
		if (null != conf.version)
		{
			out.print(" * @version "); out.println(conf.version);
		}
		out.println(" * ");
		out.print(" * @since "); out.println(new Date());
		out.println(" * ");
		out.println(" */");
		out.println();
		out.print("public class "); out.print(conf.className); out.println(" implements Serializable");
		out.println("{");
		out.println("\tprivate static final long serialVersionUID = 1L;");
		out.println();
		conf.properties.forEach((k, v) -> {
			out.print("\tpublic final "); out.print(v); out.print(" "); out.print(k); out.println(";");
		});
		out.println();
		out.println("}");
	}

	public static void main(final String... args) throws Exception
	{
		if (1 > args.length)
			throw new IllegalArgumentException("Please provide at least the configuration file.");

		final File file = new File(args[0]);
		if (!file.isFile())
			throw new IllegalArgumentException("'" + args[0] + "' is not a file.");

		final JSONConfig conf = new ObjectMapper().readValue(file, JSONConfig.class);
		if (null == conf.packageName)
			throw new IllegalArgumentException("Please provide the 'packageName' configuration property.");
		if (null == conf.className)
			throw new IllegalArgumentException("Please provide the 'className' configuration property.");

		new JSONValue(conf, System.out).run();
	}
}
