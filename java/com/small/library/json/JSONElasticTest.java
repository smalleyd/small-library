package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.*;
import java.util.Date;

/** Generates an Elasticsearch data access object from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/26/2023
 *
 */

public class JSONElasticTest extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "ESTest";

	private final String daoName;
	private final String className;
	private final String filterName;

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONElasticTest(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);

		className = getClassName(clazz.name);
		daoName = JSONElastic.getClassName(clazz.name);
		filterName = JSONFilter.getClassName(clazz.name);
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
		out.print("package "); out.print(appPackage); out.print(".dao"); out.println(";");

		out.println();
		out.println("import static org.fest.assertions.api.Assertions.assertThat;");
		out.println("import static app.fora.es.ElasticsearchUtils.json;");
		out.println("import static app.fora.es.ElasticsearchUtils.toMap;");
		out.println();
		out.println("import java.time.ZonedDateTime;");
		out.println("import java.util.Map;");
		out.println("import javax.ws.rs.NotFoundException;");
		out.println();
		out.println("import org.elasticsearch.index.query.QueryBuilders;");
		out.println("import org.junit.jupiter.api.*;");
		out.println("import org.junit.jupiter.params.ParameterizedTest;");
		out.println("import org.junit.jupiter.params.provider.CsvFileSource;");
		out.println();
		out.println("import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;");
		out.println();
		out.println("import " + appPackage + ".ElasticsearchExtension;");
		out.println("import " + appPackage + ".domain." + clazz.name + ";");
		out.println("import " + appPackage + ".model." + filterName + ";");
		out.println();
		out.println("/** Functional test class that verifies the Elasticsearch " + clazz.name + " data access object.");
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
		out.println("public class " + className);
		out.println("{");
		out.println("\tprivate static " + daoName + " dao;");
		out.println("\tprivate static final ElasticsearchExtension es = new ElasticsearchExtension();");
		out.println();
		out.println("\t@BeforeAll");
		out.println("\tpublic static void beforeAll() throws Exception");
		out.println("\t{");
		out.println("\t\tdao = new " + daoName + "(es.client(), true);");
		out.println("\t}");
	}

	private void writeMethods()
	{
		var i = new int[] { 0 };
		var firstField = clazz.fields.get(0).name;
		var secondField = clazz.fields.get(1).name;
		var indexParams = "input={0}, " + clazz.fields.stream().map(f -> f.name + "={" + ++i[0] + "}").collect(joining(", "));
		var indexArgs = "final String input,\n\t\tfinal " + clazz.fields.stream().map(f -> f.type + " " + f.name).collect(joining(",\n\t\tfinal "));
		var indexChecks = "\t\tAssertions.assertNotNull(o, \"Exists\");\n" +
			clazz.fields.stream().map(f -> "\t\tAssertions.assertEquals(" + f.name + ", o." + f.name + ", \"Check " + f.name + "\");").collect(joining("\n"));

		out.println();
		out.println("\t@Test");
		out.println("\t@Order(0)");
		out.println("\tpublic void start() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(0)");
		out.println("\tpublic void created()");
		out.println("\t{");
		out.println("\t\tAssertions.assertTrue(dao.created());");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"before_index_exists(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(0)");
		out.println("\tpublic void before_index_exists(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertFalse(dao.exists(" + firstField + "));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"index(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(10)");
		out.println("\tpublic void index(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.index(readEntity(input));");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(15)");
		out.println("\tpublic void after_index_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_index_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(" + NUM_OF_TESTS + "L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_index_exists(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_index_exists(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertTrue(dao.exists(" + firstField + "));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_index_exists_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void after_index_exists_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertFalse(dao.exists(" + firstField + " + \"-x\"));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"getById(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void getById(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.getById(" + firstField + ");");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"getById_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void getById_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar _id = " + firstField + " + \"-x\";");
		out.println("\t\tassertThat(Assertions.assertThrows(NotFoundException.class, () -> dao.getById(_id)))");
		out.println("\t\t\t.hasMessage(\"The \" + clazz.name + \" with ID '\" + _id + \"' cannot be found.\");");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"getFirst(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void getFirst(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.getFirst(QueryBuilders.termQuery(\"" + secondField + ".keyword\", " + secondField + "));");
		out.println("\t\tassertThat(o).as(\"Check results\").isNotNull().isEqualTo(readEntity(input));");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"getFirst_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void getFirst_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertNull(dao.getFirst(QueryBuilders.termQuery(\"" + secondField + ".keyword\", " + secondField + " + \"-x\")));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"search(input={0}, size={1}, ids={2})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/search.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void search(final String input, final int size, final String ids) throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.search(readFilter(input));");
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
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/search.json\", quoteCharacter='`')");
		out.println("\t@Order(20)");
		out.println("\tpublic void count(final String input, final int size) throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals((long) size, dao.count(readFilter(input)));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"update_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(100)");
		out.println("\tpublic void update_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertThrows(org.elasticsearch.ElasticsearchStatusException.class, () -> dao.update(" + firstField + " + \"-x\", toMap(input)));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(105)");
		out.println("\tpublic void update_fail_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"update_fail_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.json\", quoteCharacter='`')");
		out.println("\t@Order(110)");
		out.println("\tpublic void update_fail_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.getById(" + firstField + ");");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"update(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(115)");
		out.println("\tpublic void update(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.update(" + firstField + ", toMap(input));");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(120)");
		out.println("\tpublic void update_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"update_check(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(125)");
		out.println("\tpublic void update_check(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.getById(" + firstField + ");");
		out.println(indexChecks);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(200)");
		out.println("\tpublic void remove_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar _id = " + firstField + " + \"-x\";");
		out.println("\t\tassertThat(Assertions.assertThrows(NotFoundException.class, () -> dao.remove(_id)))");
		out.println("\t\t\t.hasMessage(\"The " + clazz.name + " with ID '\" + _id + \"' cannot be found.\");");
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
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(215)");
		out.println("\tpublic void remove_success(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar o = dao.remove(" + firstField + ");");
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
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(230)");
		out.println("\tpublic void remove_success_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tassertThat(Assertions.assertThrows(NotFoundException.class, () -> dao.getById(" + firstField + ")))");
		out.println("\t\t\t.hasMessage(\"The " + clazz.name + " with ID '\" + " + firstField + " + \"' cannot be found.\");");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_again(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.json\", quoteCharacter='`')");
		out.println("\t@Order(235)");
		out.println("\tpublic void remove_again(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tassertThat(Assertions.assertThrows(NotFoundException.class, () -> dao.remove(" + firstField + ")))");
		out.println("\t\t\t.hasMessage(\"The " + clazz.name + " with ID '\" + " + firstField + " + \"' cannot be found.\");");
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
		out.println("\t\tcount(9L);");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(1000)");
		out.println("\tpublic void drop() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.drop();");
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
