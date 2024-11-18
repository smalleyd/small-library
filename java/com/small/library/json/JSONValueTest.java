package com.small.library.json;

import static java.util.stream.Collectors.joining;
import static com.small.library.json.JSONElasticTest.assertion;

import java.io.*;
import java.util.Date;
import java.util.stream.IntStream;

/** Generates an Elasticsearch data access object from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/26/2023
 *
 */

public class JSONValueTest extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "Test";

	private final String className;

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONValueTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);

		className = getClassName(clazz.name);
	}

	@Override
	public void run()
	{
		writeHeader();
		writeClassDeclaration();

		writeMethods();

		writeFooter();
	}

	private void writeHeader()
	{
		out.print("package "); out.print(appPackage); out.print(".domain"); out.println(";");

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

		out.println();
		out.println("/** Unit test class that verifies the " + clazz.name + " domain object.");
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

	private void writeMethods()
	{
		var i = new int[] { 2 };
		var indexParams = "value={0}, hashCode={1}, toString={2}, " + clazz.fields.stream().map(f -> f.name + "={" + ++i[0] + "}").collect(joining(", "));
		var indexArgs = "@ConvertWith(JsonArgumentConverter.class) final %s value,\n".formatted(clazz.name) +
			"\t\tfinal int hashCode,\n" +
			"\t\tfinal String toString,\n" +
			"\t\tfinal " + clazz.fields.stream().map(f -> f.typeForJunit() + " " + f.name).collect(joining(",\n\t\tfinal "));
		var indexChecks = "\t\tAssertions.assertNotNull(value, \"Exists\");\n" +
			clazz.fields.stream().map(f -> assertion(f, "value")).collect(joining("\n"));

		out.println("\t@ParameterizedTest(name=\"create(" + indexParams + ")\")");
		out.println("\t@CsvSource({\n\t\t\"'{}',0,'{}',%s\",".formatted(IntStream.range(0, clazz.fields.size()).mapToObj(ii -> "").collect(joining(","))));
		out.println("\t\t\"'{%s}',0,'{%s}',%s\",".formatted(
			clazz.fields.stream().map(v -> "\\\"%1$s\\\":\\\"%1$s\\\"".formatted(v.name)).collect(joining(",")),
			clazz.fields.stream().map(v -> v.name).sorted().map(v -> "\\\"%1$s\\\":\\\"%1$s\\\"".formatted(v)).collect(joining(",")),
			clazz.fields.stream().map(v -> v.name).collect(joining(","))));
		out.println("\t})");
		out.println("\tpublic void create(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println(indexChecks);
		out.println("\t\tAssertions.assertTrue(value.equals(value), \"Check equals(Same)\");");
		out.println("\t\tAssertions.assertFalse(value.equals(\"\"), \"Check equals(String)\");");
		out.println("\t\tAssertions.assertEquals(hashCode, value.hashCode(), \"Check hashCode()\");");
		out.println("\t\tAssertions.assertEquals(toString, value.toString(), \"Check toString()\");");
		out.println("\t}");
		out.println();

		out.println("\t@ParameterizedTest(name=\"equals(value={0}, input={1}, expected={2})\")");
		out.println("\t@CsvSource({\n\t\t\"'{}','{}',true\",");
		for (var field : clazz.fields)
			out.println("\t\t\"'{}','{\\\"%1$s\\\":\\\"%1$s\\\"}',false\",".formatted(field.name));
		out.println("\t})");
		out.println("\tpublic void equals(@ConvertWith(JsonArgumentConverter.class) final %1$s value,\n\t\t@ConvertWith(JsonArgumentConverter.class) final %1$s input,\n\t\tfinal boolean expected) throws Exception".formatted(clazz.name));
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals(expected, value.equals(input));");
		out.println("\t}");
		out.println();
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
