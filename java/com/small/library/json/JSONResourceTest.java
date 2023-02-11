package com.small.library.json;

import static java.util.stream.Collectors.joining;
import static com.small.library.json.JSONElasticTest.actual;
import static com.small.library.json.JSONElasticTest.expected;

import java.io.*;
import java.util.Date;

/** Generates a functional test class for the entity RESTful resource from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/29/2023
 *
 */

public class JSONResourceTest extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "ResourceTest";

	private final String daoName;
	private final String className;
	private final String filterName;
	private final String resourceName;

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONResourceTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);

		className = getClassName(clazz.name);
		daoName = JSONElastic.getClassName(clazz.name);
		filterName = JSONFilter.getClassName(clazz.name);
		resourceName = JSONResource.getClassName(clazz.name);
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
		out.print("package "); out.print(appPackage); out.print(".rest"); out.println(";");

		out.println();
		out.println("import static org.fest.assertions.api.Assertions.assertThat;");
		out.println("import static app.fora.es.ElasticsearchUtils.json;");
		out.println();
		out.println("import java.time.Instant;");
		out.println("import java.util.List;");
		out.println("import javax.ws.rs.HttpMethod;");
		out.println("import javax.ws.rs.client.*;");
		out.println("import javax.ws.rs.core.GenericType;");
		out.println();
		out.println("import org.fest.assertions.data.MapEntry;");
		out.println("import org.junit.jupiter.api.*;");
		out.println("import org.junit.jupiter.api.extension.ExtendWith;");
		out.println("import org.junit.jupiter.params.ParameterizedTest;");
		out.println("import org.junit.jupiter.params.provider.CsvFileSource;");
		out.println();
		out.println("import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;");
		out.println("import io.dropwizard.testing.junit5.ResourceExtension;");
		out.println();
		out.println("import " + domainPackage + ".common.model.Results;");
		out.println("import " + appPackage + ".ElasticsearchExtension;");
		out.println("import " + appPackage + ".dao." + daoName + ";");
		out.println("import " + appPackage + ".domain." + clazz.name + ";");
		out.println("import " + appPackage + ".model." + filterName + ";");
		out.println();
		out.println("/** Functional test class that verifies the " + clazz.name + " RESTful resource.");
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
		out.println("@TestMethodOrder(MethodOrderer.OrderAnnotation.class)");
		out.println("@ExtendWith(DropwizardExtensionsSupport.class)");
		out.println("public class " + className + " extends AbstractResourceTest");
		out.println("{");
		out.println("\tprivate static " + daoName + " dao;");
		out.println("\tprivate static final ElasticsearchExtension es = new ElasticsearchExtension();");
		out.println();
		out.println("\tprivate static final GenericType<List<" + clazz.name + ">> types = new GenericType<>() {};");
		out.println("\tprivate static final GenericType<Results<" + clazz.name + ">> typeResults = new GenericType<>() {};");
		out.println();
		out.println("\tpublic static final String TARGET = \"/" + clazz.path + "\";");
		out.println("\tprivate final ResourceExtension resource = ResourceExtension.builder()");
		out.println("\t\t.addResource(new AppExceptionMapper())");
		out.println("\t\t.addResource(new " + resourceName + "(dao))");
		out.println("\t\t.build();");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic String path() { return TARGET; }");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic ResourceExtension resource() { return resource; }");
		out.println();
		out.println("\t@BeforeAll");
		out.println("\tpublic static void beforeAll() throws Exception");
		out.println("\t{");
		out.println("\t\tdao = new " + daoName + "(es.client(), true);");
		out.println("\t}");
		out.println();
		out.println("\t@AfterAll");
		out.println("\tpublic static void afterAll() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.drop();");
		out.println("\t}");
	}

	private void writeMethods()
	{
		var i = new int[] { 0 };
		var firstField = clazz.fields.get(0).name;
		var secondField = clazz.fields.get(1).name;
		var indexParams = "input={0}, " + clazz.fields.stream().map(f -> f.name + "={" + ++i[0] + "}").collect(joining(", "));
		var indexArgs = "final String input,\n\t\tfinal " + clazz.fields.stream().map(f -> f.typeForJunit() + " " + f.name).collect(joining(",\n\t\tfinal "));
		var indexChecks = "\t\tAssertions.assertNotNull(o, \"Exists\");\n" +
			clazz.fields.stream().map(f -> "\t\tAssertions.assertEquals(" + expected(f) + ", o." + actual(f) + ", \"Check " + f.name + "\");").collect(joining("\n"));

		out.println();
		out.println("\t@Test");
		out.println("\t@Order(0)");
		out.println("\tpublic void start() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"before_post_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(0)");
		out.println("\tpublic void before_post_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, request(" + firstField + ").get().getStatus());");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"post(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(10)");
		out.println("\tpublic void post(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request().post(Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(15)");
		out.println("\tpublic void after_post_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(" + NUM_OF_TESTS + "L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_exists(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_exists(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertTrue(dao.exists(" + firstField + "));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_exists_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_exists_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertFalse(dao.exists(" + firstField + " + \"-x\"));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_get_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_get_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar _id = " + firstField + " + \"-x\";");
		out.println("\t\tvar response = request(_id).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus());");
		out.println();
		out.println("\t\tassertThat(response.readEntity(typeMap)).isNotNull().hasSize(1)");
		out.println("\t\t\t.contains(MapEntry.entry(\"errors\", List.of(\"The " + clazz.name + " with ID '\" + _id + \"' cannot be found.\")));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_find(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_find(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(target().queryParam(\"term\", " + secondField + ")).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar v = response.readEntity(types);");
		out.println("\t\tassertThat(v).as(\"Check results\").isNotNull().hasSize(1).containsExactly(readEntity(input));");
		out.println();
		out.println("\t\tvar o = v.get(0);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_find_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_post_find_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(target().queryParam(\"term\", " + secondField + " + \"-x\")).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(types);");
		out.println("\t\tassertThat(o).as(\"Check results\").isNotNull().isEmpty();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"search(input={0}, size={1}, ids={2})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/search.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void search(final String input, final int size, final String ids) throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(\"search\").post(Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals((long) size, o.total, \"Check total\");");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().hasSize(size);");
		out.println();
		out.println("\t\tif (0 < size)");
		out.println("\t\t\tassertThat(o.data.stream().map(v -> v.id()).toArray(String[]::new))");
		out.println("\t\t\t\t.as(\"Check ids\")");
		out.println("\t\t\t\t.containsExactly(ids.split(\",\"));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"count(input={0}, size={1})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/search.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(20)");
		out.println("\tpublic void count(final String input, final int size) throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals((long) size, dao.count(readFilter(input)));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(100)");
		out.println("\tpublic void patch_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + " + \"-x\").method(HttpMethod.PATCH, Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_SERVER_ERROR, response.getStatus(), \"Status\");");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(105)");
		out.println("\tpublic void patch_fail_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch_fail_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void patch_fail_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(115)");
		out.println("\tpublic void patch(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request().method(HttpMethod.PATCH, Entity.json(input));");	// " + firstField + "
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(120)");
		out.println("\tpublic void patch_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_patch_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(125)");
		out.println("\tpublic void after_patch_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), \"Status\");");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(200)");
		out.println("\tpublic void remove_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + " + \"-x\").delete();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus());");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(205)");
		out.println("\tpublic void remove_fail_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(210)");
		out.println("\tpublic void remove_fail_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(" + NUM_OF_TESTS + "L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_success(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(215)");
		out.println("\tpublic void remove_success(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").delete();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus());");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(220)");
		out.println("\tpublic void remove_success_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(225)");
		out.println("\tpublic void remove_success_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_success_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(230)");
		out.println("\tpublic void remove_success_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus());");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_again(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(235)");
		out.println("\tpublic void remove_again(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").delete();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus());");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(240)");
		out.println("\tpublic void remove_again_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(245)");
		out.println("\tpublic void remove_again_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\tprivate void count(final long expected) throws Exception");
		out.println("\t{");
		out.println("\t\tcount(\"{}\", expected);");
		out.println("\t}");
		out.println();
		out.println("\tprivate void count(final String query, final long expected) throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals(expected, dao.count(readFilter(query)), \"COUNT: \" + query);");
		out.println("\t}");
		out.println();
		out.println("\tprivate " + clazz.name + " readEntity(final String input) throws Exception");
		out.println("\t{");
		out.println("\t\treturn json.readValue(input, " + clazz.name + ".class);");
		out.println("\t}");
		out.println();
		out.println("\tprivate " + filterName + " readFilter(final String input) throws Exception");
		out.println("\t{");
		out.println("\t\treturn json.readValue(input, " + filterName + ".class);");
		out.println("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
