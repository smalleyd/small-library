package com.small.library.json;

import static java.util.stream.Collectors.joining;
import static com.small.library.json.JSONElasticTest.assertionX;

import java.io.*;
import java.util.Date;
import java.util.stream.IntStream;

/** Generates an Elasticsearch data access object from a JSON document.
 *
 * @author smalleyd
 * @version 4.0
 * @since 5/20/2026
 *
 */

public class JSONRecordTest extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "Test";

	private final String className;

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONRecordTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
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
		out.println("import " + domainPackage + ".junit.params.StringsArgumentConverter;");

		out.println();
		out.println("/** Unit test class that verifies the " + clazz.name + " domain object.");
		out.println(" *");
		out.println(" * @author " + conf.author);
		out.println(" * @version " + conf.version);
		out.println(" * @since " + new Date());
		out.println(" *");
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
		var i = new int[] { 0 };
		var indexParams = "value={0}, " + clazz.fields.stream().map(f -> f.name + "={" + ++i[0] + "}").collect(joining(", "));
		var indexArgs = "@ConvertWith(JsonArgumentConverter.class) final %s value,\n".formatted(clazz.name) +
			"\t\tfinal " + clazz.fields.stream().map(f -> f.typeForJunit() + " " + f.name).collect(joining(",\n\t\tfinal "));
		var indexChecks = "\t\tAssertions.assertNotNull(value, \"Exists\");\n" +
			clazz.fields.stream().map(f -> assertionX(f, "value")).collect(joining("\n"));

		out.println("\t@ParameterizedTest(name=\"create(" + indexParams + ")\")");
		out.println("\t@CsvSource({\n\t\t\"'{}',%s\",".formatted(IntStream.range(0, clazz.fields.size()).mapToObj(ii -> "").collect(joining(","))));
		out.println("\t\t\"'{%s}',%s\",".formatted(
			clazz.fields.stream().map(v -> "\\\"%1$s\\\":\\\"%1$s\\\"".formatted(v.name)).collect(joining(",")),
			clazz.fields.stream().map(v -> v.name).collect(joining(","))));
		out.println("\t})");
		out.println("\tpublic void create(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println(indexChecks);
		out.println("\t\tAssertions.assertTrue(value.equals(value), \"Check equals(Same)\");");
		out.println("\t\tAssertions.assertFalse(value.equals(\"\"), \"Check equals(String)\");");
		out.println("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
