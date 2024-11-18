package com.small.library.json;

import static java.util.stream.Collectors.joining;

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
		out.println("import java.util.*;");
		out.println("import java.util.stream.Stream;");
		out.println("import javax.ws.rs.HttpMethod;");
		out.println("import javax.ws.rs.client.*;");
		out.println("import javax.ws.rs.core.GenericType;");
		out.println();
		out.println("import org.fest.assertions.data.MapEntry;");
		out.println("import org.junit.jupiter.api.*;");
		out.println("import org.junit.jupiter.api.extension.ExtendWith;");
		out.println("import org.junit.jupiter.params.ParameterizedTest;");
		out.println("import org.junit.jupiter.params.converter.ConvertWith;");
		out.println("import org.junit.jupiter.params.provider.*;");
		out.println();
		out.println("import io.dropwizard.jersey.validation.ValidationErrorMessage;");
		out.println("import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;");
		out.println("import io.dropwizard.testing.junit5.ResourceExtension;");
		out.println();
		out.println("import " + domainPackage + ".common.model.Results;");
		out.println("import " + domainPackage + ".es.ElasticsearchExtension;");
		out.println("import " + domainPackage + ".junit.params.DateArgumentConverter;");
		out.println("import " + domainPackage + ".junit.params.StringsArgumentConverter;");
		out.println("import " + appPackage + ".dao." + daoName + ";");
		out.println("import " + appPackage + ".domain." + clazz.name + ";");
		for (var f : clazz.fields)
		{
			var c = conf.clazz(f.type);
			if (null != c)
				out.println("import " + appPackage + ".domain." + c.name + ";");
		}
		out.println("import " + appPackage + ".model." + filterName + ";");

		if (clazz.cacheable)
			out.println("import app.fora.redis.JedisConfig;");

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
		out.println("\tprivate static final Map<String, Date> createdAt = new HashMap<>();");
		out.println("\tprivate static final Map<String, Date> updatedAt = new HashMap<>();");
		out.println("\tprivate static final String createdAtQuery = \"{\\\"created_at_from\\\":\\\"%s\\\",\\\"created_at_to\\\":\\\"%s\\\"}\";");
		out.println("\tprivate static final String updatedAtQuery = \"{\\\"updated_at_from\\\":\\\"%s\\\",\\\"updated_at_to\\\":\\\"%s\\\"}\";");
		out.println();
		out.println("\tprivate static Date createdAt(final String id) { return createdAt.get(id); }");
		out.println("\tprivate static Date updatedAt(final String id) { return updatedAt.get(id); }");
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
		if (clazz.cacheable)
			out.println("\t\tdao = new " + daoName + "(es.client(), new JedisConfig().pool(), true);");
		else
			out.println("\t\tdao = new " + daoName + "(es.client(), true);");
		out.println("\t}");
		out.println();
		out.println("\t@AfterAll");
		out.println("\tpublic static void afterAll() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.drop();");
		out.println("\t}");
	}

	public static String assertion(final JSONField f)
	{
		if ("created_at".equals(f.name))
			return "\t\tassertThat(o.created_at).as(\"Check created_at\").isNotNull().isNotEqualTo(created_at).isCloseTo(now, 60000L);";
		if ("updated_at".equals(f.name))
			return "\t\tassertThat(o.updated_at).as(\"Check updated_at\").isNotNull().isNotEqualTo(updated_at).isCloseTo(now, 60000L).isEqualTo(o.created_at);";

		return JSONElasticTest.assertion(f);
	}

	public static String assertion_(final JSONField f, final int update)
	{
		if ("created_at".equals(f.name))
		{
			switch (update)
			{
				case 1: return "\t\tAssertions.assertEquals(createdAt, o.created_at, \"Check created_at\");";
				default: return "\t\tAssertions.assertEquals(createdAt(id), o.created_at, \"Check created_at\");";
			}
		}

		if ("updated_at".equals(f.name))
		{
			switch (update)
			{
				case 0: return "\t\tassertThat(o.updated_at).as(\"Check updated_at\").isNotNull().isNotEqualTo(updated_at).isCloseTo(new Date(), 30000L).isAfter(o.created_at);";
				case 1: return "\t\tAssertions.assertEquals(createdAt, o.updated_at, \"Check updated_at\");";
				default: return "\t\tAssertions.assertEquals(updatedAt(id), o.updated_at, \"Check updated_at\");";
			}
		}

		return JSONElasticTest.assertion(f);
	}

	private void writeMethods()
	{
		var i = new int[] { 0 };
		var firstField = clazz.fields.get(0).name;	// Should be the identifier.
		var secondField = clazz.fields.get(1).name;	// Should be a unique field.
		var indexParams = "input={0}, " + clazz.fields.stream().map(f -> f.name + "={" + ++i[0] + "}").collect(joining(", "));
		var indexArgs = "final String input,\n\t\tfinal " + clazz.fields.stream().map(f -> f.typeForJunit() + " " + f.name).collect(joining(",\n\t\tfinal "));
		var indexChecks = "\t\tAssertions.assertNotNull(o, \"Exists\");\n" +
			clazz.fields.stream().map(f -> assertion(f)).collect(joining("\n"));
		var updateChecks = "\t\tAssertions.assertNotNull(o, \"Exists\");\n" +
			clazz.fields.stream().map(f -> assertion_(f, 0)).collect(joining("\n"));
		var indexChecks_ = "\t\tAssertions.assertNotNull(o, \"Exists\");\n" +
			clazz.fields.stream().map(f -> assertion_(f, 1)).collect(joining("\n"));
		var updateChecks_ = "\t\tAssertions.assertNotNull(o, \"Exists\");\n" +
			clazz.fields.stream().map(f -> assertion_(f, 2)).collect(joining("\n"));

		out.println();
		out.println("\t@Test");
		out.println("\t@Order(0)");
		out.println("\tpublic void start() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"before_post_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(0)");
		out.println("\tpublic void before_post_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"post_invalid(input={0}, message={1})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/invalid.csv\", quoteCharacter='`')");
		out.println("\t@Order(10)");
		out.println("\tpublic void post_invalid(final String input, final String message)");
		out.println("\t{");
		out.println("\t\tvar response = request().post(Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_INVALID, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeMap);");
		out.println("\t\tassertThat(o).as(\"Check results\").isNotNull().hasSize(1).contains(MapEntry.entry(\"errors\", List.of(message)));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(15)");
		out.println("\tpublic void post_invalids_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(20)");
		out.println("\tpublic void post_invalids_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\tprivate static int post = -1;");
		out.println();
		out.println("\t@ParameterizedTest(name=\"post(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(100)");
		out.println("\tpublic void post(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request().method((0 == (++post % 2)) ? HttpMethod.POST : HttpMethod.PUT, Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar now = new Date();");
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks);
		out.println();
		out.println("\t\tcreatedAt.put(id, o.created_at);");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(105)");
		out.println("\tpublic void after_post_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(" + NUM_OF_TESTS + "L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_exists(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_exists(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertTrue(dao.exists(" + firstField + "));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_exists_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_exists_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertFalse(dao.exists(" + firstField + " + \"-x\"));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar createdAt = createdAt(id);");
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks_);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_get_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_get_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar _id = " + firstField + " + \"-x\";");
		out.println("\t\tvar response = request(_id).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tassertThat(response.readEntity(typeMap)).isNotNull().hasSize(1)");
		out.println("\t\t\t.contains(MapEntry.entry(\"errors\", List.of(\"The " + clazz.name + " with ID '\" + _id + \"' cannot be found.\")));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_find(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_find(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(target().queryParam(\"term\", " + secondField + ")).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar v = response.readEntity(types);");
		out.println("\t\tassertThat(v).as(\"Check results\").isNotNull().hasSize(1).containsExactly(readEntity(input));");
		out.println();
		out.println("\t\tvar o = v.get(0);");
		out.println("\t\tvar createdAt = createdAt(id);");
		out.println(indexChecks_);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_find_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_find_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(target().queryParam(\"term\", " + "\"invalid_\" + " + secondField + ")).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(types);");
		out.println("\t\tassertThat(o).as(\"Check results\").isNotNull().isEmpty();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_post_find_invalid(term={0}, pageSize={1}, message={2})\")");
		out.println("\t@CsvSource({");
		out.println("\t\t\",,query param term must not be blank\",");
		out.println("\t\t\"'',,query param term must not be blank\",");
		out.println("\t\t\"'  ',,query param term must not be blank\",");
		out.println("\t\t\"abc,0,query param pageSize must be greater than or equal to 1\",");
		out.println("\t\t\"abc,1001,query param pageSize must be less than or equal to 1000\",");
		out.println("\t\t\"abc,-10,query param pageSize must be greater than or equal to 1\",");
		out.println("\t\t\"abc,2002,query param pageSize must be less than or equal to 1000\",");
		out.println("\t})");
		out.println("\t@Order(110)");
		out.println("\tpublic void after_post_find_invalid(final String term, final Integer pageSize, final String message) throws Exception");
		out.println("\t{");
		out.println("\t\tvar req = target();");
		out.println("\t\tif (null != term) req = req.queryParam(\"term\", term);");
		out.println("\t\tif (null != pageSize) req = req.queryParam(\"pageSize\", pageSize);");
		out.println();
		out.println("\t\tvar response = request(req).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_BAD_REQUEST, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeMap);");
		out.println("\t\tassertThat(o).as(\"Check results\").isNotNull().hasSize(1).contains(MapEntry.entry(\"errors\", List.of(message)));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"search(input={0}, size={1}, ids={2})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/search.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void search(final String input, int size, @ConvertWith(StringsArgumentConverter.class) final String... ids) throws Exception");
		out.println("\t{");
		out.println("\t\tif (input.contains(\"created_at\") || input.contains(\"updated_at\")) size = 0;	// Generated inputs will not be the same as the timestamps.");
		out.println();
		out.println("\t\tvar response = request(\"search\").post(Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals((long) size, o.total, \"Check total\");");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().hasSize(size);");
		out.println("\t\tAssertions.assertNull(o.scrollId, \"Check scollId\");");
		out.println();
		out.println("\t\tif (0 < size)");
		out.println("\t\t\tassertThat(o.data.stream().map(v -> v.id()).toArray(String[]::new))");
		out.println("\t\t\t\t.as(\"Check ids\")");
		out.println("\t\t\t\t.containsExactly(ids);");
		out.println("\t}");
		out.println();
		out.println("\tpublic static Stream<Map.Entry<String, Date>> search_by_created_at()");
		out.println("\t{");
		out.println("\t\treturn createdAt.entrySet().stream();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"search_by_created_at({0})\")");
		out.println("\t@MethodSource");
		out.println("\t@Order(110)");
		out.println("\tpublic void search_by_created_at(Map.Entry<String, Date> entry) throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(\"search\").post(Entity.json(toQuery(createdAtQuery, entry.getValue())));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals((long) 1, o.total, \"Check total\");");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().hasSize(1);");
		out.println("\t\tassertThat(o.data.stream().map(v -> v.id()).toArray(String[]::new))");
		out.println("\t\t\t.as(\"Check ids\")");
		out.println("\t\t\t.containsExactly(entry.getKey());");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"count(input={0}, size={1})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/search.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(110)");
		out.println("\tpublic void count(final String input, int size) throws Exception");
		out.println("\t{");
		out.println("\t\tif (input.contains(\"created_at\") || input.contains(\"updated_at\")) size = 0;	// Generated inputs will not be the same as the timestamps.");
		out.println();
		out.println("\t\tAssertions.assertEquals((long) size, dao.count(readFilter(input)));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"count_by_created_at({0})\")");
		out.println("\t@MethodSource(\"search_by_created_at\")");
		out.println("\t@Order(110)");
		out.println("\tpublic void count_by_created_at(Map.Entry<String, Date> entry) throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals((long) 1, dao.count(readFilter(toQuery(createdAtQuery, entry.getValue()))));");
		out.println("\t}");
		out.println();
		out.println("\tprivate static String scrollId;");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(115)");
		out.println("\tpublic void scroll() throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(\"search\").post(Entity.json(\"{\\\"pageSize\\\":1,\\\"scroll\\\":\\\"30s\\\"}\"));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals(" + NUM_OF_TESTS + "L, o.total, \"Check total\");");
		out.println("\t\tassertThat(scrollId = o.scrollId).isNotNull().hasSize(120);");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().hasSize(1);");
		out.println("\t\tassertThat(o.data.stream().map(e -> e.id).toArray(String[]::new)).as(\"Check data\").isNotNull().hasSize(1).containsExactly(\"id_1\");");
		out.println("\t}");
		out.println();
		out.println("\t@RepeatedTest(value=" + (NUM_OF_TESTS - 1) + ", name=\"scroll_next({currentRepetition})\")");
		out.println("\t@Order(120)");
		out.println("\tpublic void scroll_next(final RepetitionInfo info) throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(\"scroll/\" + scrollId).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals(" + NUM_OF_TESTS + "L, o.total, \"Check total\");");
		out.println("\t\tassertThat(scrollId = o.scrollId).isNotNull().hasSize(120);");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().hasSize(1);");
		out.println("\t\tassertThat(o.data.stream().map(e -> e.id).toArray(String[]::new)).as(\"Check IDs\").isNotNull().hasSize(1).containsExactly(\"id_\" + (info.getCurrentRepetition() + 1));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(125)");
		out.println("\tpublic void scroll_end() throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(target().path(\"scroll/\" + scrollId).queryParam(\"time\", \"30s\")).get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals(0L, o.total, \"Check total\");");
		out.println("\t\tassertThat(o.scrollId).isNull();");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().isEmpty();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(200)");
		out.println("\tpublic void patch_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + " + \"-x\").method(HttpMethod.PATCH, Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(205)");
		out.println("\tpublic void patch_fail_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch_fail_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(210)");
		out.println("\tpublic void patch_fail_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar createdAt = createdAt(id);");
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(indexChecks_);
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch_invalid(input={0}, id={1}, message={2})\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/invalid-patch.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(220)");
		out.println("\tpublic void patch_invalid(final String input,");
		out.println("\t\tfinal String id,");
		out.println("\t\tfinal String message) throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(id).method(HttpMethod.PATCH, Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_INVALID, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println("\t\tassertThat(response.readEntity(ValidationErrorMessage.class).getErrors()).as(\"Check results\").contains(message.replaceAll(\"\\\\\\\\n\", \"\\n\"));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(225)");
		out.println("\tpublic void patch_invalid_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch_invalid_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/index.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(230)");
		out.println("\tpublic void patch_invalid_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tpatch_fail_get(input, " + clazz.fields.stream().map(f -> f.name).collect(joining(", ")) + ");");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"patch(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(240)");
		out.println("\tpublic void patch(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").method(HttpMethod.PATCH, Entity.json(input));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(updateChecks);
		out.println();
		out.println("\t\tupdatedAt.put(id, o.updated_at);");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(245)");
		out.println("\tpublic void patch_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"after_patch_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(250)");
		out.println("\tpublic void after_patch_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(updateChecks_);
		out.println("\t}");
		out.println();
		out.println("\tpublic static Stream<Map.Entry<String, Date>> search_by_updated_at()");
		out.println("\t{");
		out.println("\t\treturn updatedAt.entrySet().stream();");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"search_by_updated_at({0})\")");
		out.println("\t@MethodSource");
		out.println("\t@Order(255)");
		out.println("\tpublic void search_by_updated_at(Map.Entry<String, Date> entry) throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(\"search\").post(Entity.json(toQuery(updatedAtQuery, entry.getValue())));");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println();
		out.println("\t\tvar o = response.readEntity(typeResults);");
		out.println("\t\tAssertions.assertNotNull(o, \"Exists\");");
		out.println("\t\tAssertions.assertEquals((long) 1, o.total, \"Check total\");");
		out.println("\t\tassertThat(o.data).as(\"Check data\").isNotNull().hasSize(1);");
		out.println("\t\tassertThat(o.data.stream().map(v -> v.id()).toArray(String[]::new))");
		out.println("\t\t\t.as(\"Check ids\")");
		out.println("\t\t\t.containsExactly(entry.getKey());");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"count_by_updated_at({0})\")");
		out.println("\t@MethodSource(\"search_by_updated_at\")");
		out.println("\t@Order(255)");
		out.println("\tpublic void count_by_updated_at(Map.Entry<String, Date> entry) throws Exception");
		out.println("\t{");
		out.println("\t\tAssertions.assertEquals((long) 1, dao.count(readFilter(toQuery(updatedAtQuery, entry.getValue()))));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_fail(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(300)");
		out.println("\tpublic void remove_fail(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + " + \"-x\").delete();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(305)");
		out.println("\tpublic void remove_fail_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(310)");
		out.println("\tpublic void remove_fail_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(" + NUM_OF_TESTS + "L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_success(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(315)");
		out.println("\tpublic void remove_success(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").delete();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_OK, response.getStatus());");
		out.println();
		out.println("\t\tvar o = response.readEntity(" + clazz.name + ".class);");
		out.println(updateChecks_);
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(320)");
		out.println("\tpublic void remove_success_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(325)");
		out.println("\tpublic void remove_success_count() throws Exception");
		out.println("\t{");
		out.println("\t\tcount(0L);");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_success_get(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(330)");
		out.println("\tpublic void remove_success_get(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").get();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println("\t}");
		out.println();
		out.println("\t@ParameterizedTest(name=\"remove_again(" + indexParams + ")\")");
		out.println("\t@CsvFileSource(resources=\"/" + clazz.path + "/update.csv\"" + QUOTE_CHARACTER + ")");
		out.println("\t@Order(335)");
		out.println("\tpublic void remove_again(" + indexArgs + ") throws Exception");
		out.println("\t{");
		out.println("\t\tvar response = request(" + firstField + ").delete();");
		out.println("\t\tAssertions.assertEquals(HTTP_STATUS_NOT_FOUND, response.getStatus(), () -> \"Status: \" + response.readEntity(String.class));");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(340)");
		out.println("\tpublic void remove_again_refresh() throws Exception");
		out.println("\t{");
		out.println("\t\tdao.refresh();");
		out.println("\t}");
		out.println();
		out.println("\t@Test");
		out.println("\t@Order(345)");
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
		out.println("\t@SuppressWarnings(\"unused\")");
		out.println("\tprivate " + clazz.name + " readEntity(final String input) throws Exception");
		out.println("\t{");
		out.println("\t\treturn json.readValue(input, " + clazz.name + ".class);");
		out.println("\t}");
		out.println();
		out.println("\tprivate " + filterName + " readFilter(final String input) throws Exception");
		out.println("\t{");
		out.println("\t\treturn json.readValue(input, " + filterName + ".class);");
		out.println("\t}");
		out.println();
		out.println("\tprivate String toQuery(final String template, final Date value)");
		out.println("\t{");
		out.println("\t\tvar instant = Instant.ofEpochMilli(value.getTime());");
		out.println();
		out.println("\t\treturn template.formatted(instant, instant);");
		out.println("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
