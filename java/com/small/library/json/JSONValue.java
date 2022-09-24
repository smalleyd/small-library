package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;
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

	public static final String FORMAT_EQUALS_PRIMITIVE = "(%s == v.%s)%s";
	public static final String FORMAT_EQUALS_OBJECT = "Objects.equals(%s, v.%s)%s";

	private final JSONConfig conf;
	private final JSONClass clazz;
	private final PrintStream out;

	public JSONValue(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		this.conf = conf;
		this.clazz = clazz;
		this.out = out;
	}

	public void run()
	{
		var size = clazz.fields.size();
		var index = new int[1];

		out.print("package "); out.print(conf.packageName); out.println(";");
		out.println();
		out.println("import java.io.Serializable;");
		out.println("import java.util.Objects;");
		if (CollectionUtils.isNotEmpty(conf.imports))
		{
			out.println();
			conf.imports.forEach(i -> {
				out.print("import "); out.print(i); out.println(";");
			});
		}
		out.println();
		out.println("import com.fasterxml.jackson.annotation.JsonProperty;");
		out.println("import com.jibe.dwservice.ObjectUtils;");
		out.println();
		out.print("/** Value object that represents the "); out.print(clazz.caption); out.println(".");
		out.println(" * ");
		if (null != conf.author)
		{
			out.print(" * @author "); out.println(conf.author);
		}
		if (null != conf.version)
		{
			out.print(" * @version "); out.println(conf.version);
		}
		out.print(" * @since "); out.println(new Date());
		out.println(" * ");
		out.println(" */");
		out.println();
		out.print("public class "); out.print(clazz.name); out.println(" implements Serializable");
		out.println("{");
		out.println("\tprivate static final long serialVersionUID = 1L;");
		out.println();
		clazz.fields.forEach((k, v) -> {
			out.print("\tpublic final "); out.print(v); out.print(" "); out.print(k); out.println(";");
		});
		out.println();
		out.print("\tpublic "); out.print(clazz.name); out.println("(");
		index[0] = 0;
		clazz.fields.forEach((k, v) -> {
			out.print("\t\t@JsonProperty(\""); out.print(k); out.print("\") final "); out.print(v); out.print(" "); out.print(k);
			out.println((size > ++index[0]) ? "," : ")");
		});
		out.println("\t{");
		clazz.fields.forEach((k, v) -> {
			out.print("\t\tthis."); out.print(k); out.print(" = "); out.print(k); out.println(";");
		});
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic boolean equals(final Object o)");
		out.println("\t{");
		out.print("\t\tif (!(o instanceof "); out.print(clazz.name); out.println(")) return false;");
		out.println();
		out.print("\t\tvar v = ("); out.print(clazz.name); out.println(") o;");
		index[0] = 0;
		clazz.fields.forEach((k, v) -> {
			var term = (size > ++index[0]) ? " &&" : ";";
			out.print("\t\t");
			out.print((1 == index[0]) ? "return " : "\t");
			out.println(toEquals(k, v, term));
		});
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic int hashCode()");
		out.println("\t{");
		out.print("\t\treturn Objects.hash(");
		out.print(clazz.fields.entrySet().stream().map(e -> e.getKey()).collect(joining(", ")));
		out.println(");");
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic String toString() { return ObjectUtils.toString(this); }");
		out.println("}");
	}

	private String toEquals(final String name, final String type, final String term)
	{
		return String.format(Character.isLowerCase(type.charAt(0)) ? FORMAT_EQUALS_PRIMITIVE : FORMAT_EQUALS_OBJECT, name, name, term);
	}

	public static void main(final String... args) throws Exception
	{
		if (2 > args.length)
			throw new IllegalArgumentException("Please provide at least the configuration file(s) and output directory.");

		// Get references to the configuration file(s).
		var file = new File(args[0]);
		if (!file.isFile())
			throw new IllegalArgumentException("'" + args[0] + "' is not a file.");

		// Get a reference to the output directory.
		var output = new File(args[1]);
		if (!output.isDirectory())
			throw new IllegalArgumentException("'" + args[1] + "' is not a directory.");

		// Load each configuration file and create the output.
		var mapper = new ObjectMapper();
		var conf = mapper.readValue(file, JSONConfig.class);
		if (null == conf.packageName)
		{
			throw new IllegalArgumentException("Configuration file '" + file.getAbsolutePath() + "' is missing the packageName property.");
		}

		int i = 0;
		for (var clazz : conf.classes)
		{
			i++;
			if (null == clazz.name)
			{
				log.warn("Item '{}' is missing the name property in '{}'.", i, file.getAbsolutePath());
				continue;	// Skip this file.
			}
			if (null == clazz.caption)
			{
				log.warn("Item '{}' is missing the caption property in '{}'.", i, file.getAbsolutePath());
				continue;	// Skip this file.
			}
			if (MapUtils.isEmpty(clazz.fields))
			{
				log.warn("Item '{}' has not specified any fields in '{}'.", i, file.getAbsolutePath());
				continue;	// Skip this file.
			}

			try (var out = new PrintStream(new File(output, clazz.name + ".java")))
			{
				new JSONValue(conf, clazz, out).run();
				out.flush();
			}
		}
	}
}
