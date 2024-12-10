package com.small.library.json;

import java.io.*;
import java.util.Date;

/** Generates an entity search-request/filter object from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/25/2023
 *
 */

public class JSONFilterTest extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "SearchRequestTest";

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	private final String className;
	private final String implClassName;

	public JSONFilterTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);

		className = getClassName(clazz.name);
		implClassName = JSONFilter.getClassName(clazz.name);
	}

	@Override
	public void run()
	{
		writeHeader();
		writeClassDeclaration();

		writeEmpty();

		writeFooter();
	}

	private void writeHeader()
	{
		out.print("package "); out.print(appPackage); out.print(".filter"); out.println(";");

		out.println();
		out.println("import static org.fest.assertions.api.Assertions.assertThat;");
		out.println();
		out.println("import java.util.Date;");
		out.println();
		out.println("import org.junit.jupiter.api.*;");
		out.println("import org.junit.jupiter.params.ParameterizedTest;");
		out.println("import org.junit.jupiter.params.converter.ConvertWith;");
		out.println("import org.junit.jupiter.params.provider.CsvSource;");
		out.println();
		out.println("import " + domainPackage + ".junit.params.DateArgumentConverter;");
		out.println("import " + domainPackage + ".junit.params.JsonArgumentConverter;");
		out.println("import " + domainPackage + ".junit.params.StringsArgumentConverter;");

		out.println();
		out.println("/** Unit test class that verifies the " + implClassName + " domain object.");
		out.println(" * ");
		out.println(" * @author " + conf.author);
		out.println(" * @version " + conf.version);
		out.println(" * @since " + new Date());
		out.println(" * ");
		out.println(" */");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration()
	{
		out.println();
		out.println("public class " + className);
		out.println("{");
	}

	/** Output method - writes the member variables. */
	private void writeEmpty()
	{
		out.println("\t@ParameterizedTest(name=\"empty(value={0}, expected={1})\")");
		out.println("\t@CsvSource({");
		out.println("\t\t\"'{}',true\",");

		// Write member variables.
		for (var i : clazz.fields)
		{
			if (i.identifier)
			{
				out.println("\t\t\"'{\\\"%1$ss\\\":[]}',true\",".formatted(i.name));
				out.println("\t\t\"'{\\\"%1$ss\\\":[\\\"%1$ss\\\"]}',false\",".formatted(i.name));
			}
			else if (conf.clazz_exists(i.type))
			{
				var c = conf.clazz(i.type);
				out.println("\t\t\"'{\\\"%1$s_%2$s\\\":\\\"%1$s_%2$s\\\"}',false\",".formatted(i.name, c.fields.get(0).name));
				out.println("\t\t\"'{\\\"%1$s_%2$s\\\":\\\"%1$s_%2$s\\\"}',false\",".formatted(i.name, c.fields.get(1).name));
			}
			else
				out.println("\t\t\"'{\\\"%1$s\\\":\\\"%1$s\\\"}',false\",".formatted(i.name));

			if (i.nullable())
				out.println("\t\t\"'{\\\"has_%1$s\\\":true}',false\",".formatted(i.name));

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (i.range)
			{
				out.println("\t\t\"'{\\\"%1$s_from\\\":1000}',false\",".formatted(i.name));
				out.println("\t\t\"'{\\\"%1$s_to\\\":2000}',false\",".formatted(i.name));
			}
		}

		out.println("\t})");
		out.println("\tpublic void empty(@ConvertWith(JsonArgumentConverter.class) final %s value, final boolean expected)".formatted(implClassName));
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals(expected, value.empty());");
		out.println("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
