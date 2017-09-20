package com.small.library.json;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.*;

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
	private static final Logger log = LoggerFactory.getLogger(JSONValue.class);

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
		conf.fields.forEach((k, v) -> {
			out.print("\tpublic final "); out.print(v); out.print(" "); out.print(k); out.println(";");
		});
		out.println();
		out.print("\tpublic "); out.print(conf.className); out.println("(");
		out.println("\t)");
		out.println("\t{");
		conf.fields.forEach((k, v) -> {
			out.print("\t\t@JsonProperty(\""); out.print(k); out.print("\") final "); out.print(v); out.print(" "); out.print(k); out.println(",");
		});
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic String toString()");
		out.println("\t{");
		out.println("\t}");
		out.println("}");
	}

	public static void main(final String... args) throws Exception
	{
		if (2 > args.length)
			throw new IllegalArgumentException("Please provide at least the configuration file(s) and output directory.");

		// Get references to the configuration file(s).
		File[] files = null;
		final File path = new File(args[0]);
		if (path.isDirectory())
		{
			files = path.listFiles((f, n) -> n.endsWith(".json"));
			if ((null == files) || (0 == files.length))
				throw new IllegalArgumentException("The directory '" + path.getAbsolutePath() + "' does not have any JSON (*.json) files.");
		}
		else if (!path.isFile())
			throw new IllegalArgumentException("'" + args[0] + "' is not a file nor a directory.");
		else
			files = new File[] { path };

		// Get a reference to the output directory.
		final File output = new File(args[1]);
		if (!output.isDirectory())
			throw new IllegalArgumentException("'" + args[0] + "' is not a directory.");

		// Load each configuration file and create the output.
		final ObjectMapper mapper = new ObjectMapper();
		for (final File file : files)
		{
			final JSONConfig conf = mapper.readValue(file, JSONConfig.class);
			if (null == conf.packageName)
			{
				log.warn("Configuration file '{}' is missing the packageName property.", file.getAbsolutePath());
				continue;	// Skip this file.
			}
			if (null == conf.className)
			{
				log.warn("Configuration file '{}' is missing the className property.", file.getAbsolutePath());
				continue;	// Skip this file.
			}
			if (MapUtils.isEmpty(conf.fields))
			{
				log.warn("Configuration file '{}' is has not specified any fields.", file.getAbsolutePath());
				continue;	// Skip this file.
			}
	
			new JSONValue(conf, new PrintStream(new File(output, conf.className + ".java"))).run();
		}
	}
}
