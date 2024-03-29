package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates Dropwizard.io the unit test skeletons for the Hibernate data access objects.
*   The metadata is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 6/11/2015
*
***************************************************************************************/

public class EntityBeanDAOTest extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "DAOTest";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanDAOTest(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public EntityBeanDAOTest(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version.
	*/
	public EntityBeanDAOTest(PrintWriter writer,
		String author, Table table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMethods();

		writeFooter();
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(table.name)) + ".java";
	}

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
	}

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		final String packageName = getPackageName();
		final String domainPackageName = getDomainPackageName();
		final String basePackageName = getBasePackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		final String name = getObjectName();
		writeLine("import static org.junit.jupiter.api.Assertions.assertThrows;");
		writeLine("import static org.junit.jupiter.params.provider.Arguments.arguments;");
		writeLine("import static " + domainPackageName + ".dwtesting.TestingUtils.*;");
		writeLine();
		writeLine("import java.util.stream.Stream;");
		writeLine();
		writeLine("import org.apache.commons.lang3.StringUtils;");
		writeLine("import org.junit.jupiter.api.*;");
		writeLine("import org.junit.jupiter.api.extension.ExtendWith;");
		writeLine("import org.junit.jupiter.params.ParameterizedTest;");
		writeLine("import org.junit.jupiter.params.provider.Arguments;");
		writeLine("import org.junit.jupiter.params.provider.MethodSource;");
		writeLine();
		writeLine("import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;");
		writeLine();
		writeLine("import " + domainPackageName + ".junit.hibernate.*;");
		writeLine("import " + domainPackageName + ".dwservice.errors.ValidationException;");
		writeLine("import " + basePackageName + "." + getAppName() + "App;");
		writeLine("import " + basePackageName + ".entity." + name + ";");
		writeLine("import " + basePackageName + ".filter." + EntityBeanFilter.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".value." + EntityBeanValueObject.getClassName(name) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tFunctional test for the data access object that handles access to the " + name + " entity.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version " + getVersion());
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		final String name = getClassName();
		final String daoName = EntityBeanDAO.getClassName(getObjectName());
		final String valueName = EntityBeanValueObject.getClassName(getObjectName());

		writeLine();
		writeLine("@TestMethodOrder(MethodOrderer.MethodName.class)	// Ensure that the methods are executed in order listed.");
		writeLine("@ExtendWith(DropwizardExtensionsSupport.class)");
		writeLine("public class " + name);
		writeLine("{");
		writeLine("public static final HibernateRule DAO_RULE = new HibernateRule(" + getAppName() + "App.ENTITIES);", 1);
		writeLine("public final HibernateTransactionRule transRule = new HibernateTransactionRule(DAO_RULE);", 1);
		writeLine();
		writeLine("private static " + daoName + " dao = null;", 1);
		writeLine("private static " + valueName + " VALUE = null;", 1);
		writeLine();
		writeLine("@BeforeAll", 1);
		writeLine("public static void up()", 1);
		writeLine("{", 1);
		writeLine("var factory = DAO_RULE.getSessionFactory();", 2);
		writeLine("dao = new " + daoName + "(factory);", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		final String name = getObjectName();
		// final String entityName = EntityBeanCMP3.getClassName(name);
		final String filterName = EntityBeanFilter.getClassName(name);
		final String valueName = EntityBeanValueObject.getClassName(name);

		// For testing non-existence, use a value to add to the primary key.
		String invalidId = "\"INVALID\"";
		for (ColumnInfo i : columnInfo)
		{
			if (i.isPartOfPrimaryKey)
			{
				if (!i.isCharacter)
					invalidId = (10 < i.size) ? "1000L" : "1000";

				break;
			}
		}

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void add()", 1);
		writeLine("{", 1);
		writeLine("// TODO: populate the VALUE with data.", 2);
		writeLine("var value = dao.add(VALUE = new " + valueName + "());", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Creates a valid " + name + " value for the validation tests.", 1);
		writeLine(" *\t@return never NULL.", 1);
		writeLine("*/", 1);
		writeLine("private static " + valueName + " createValid()", 1);
		writeLine("{", 1);
		writeLine("// TODO: populate the valid value with data.", 2);
		writeLine("return new " + valueName + "();", 2);
		writeLine("}", 1);

		// Add validation checks.
		for (var i : columnInfo)
		{
			if (i.isAutoIncrementing)
				continue;

			if (!i.isNullable && !i.isPrimitive)
			{
				writeLine();
				writeLine("@Test", 1);
				writeLine("public void add_missing" + i.name + "()", 1);
				writeLine("{", 1);
				writeLine("assertThrows(ValidationException.class, () -> dao.add(createValid()." + i.withMethodName + "(null)));", 2);
				writeLine("}", 1);
			}
			if (i.isString)
			{
				var maxLenName = valueName + ".MAX_LEN_" + i.columnName.toUpperCase();

				writeLine();
				writeLine("@Test", 1);
				writeLine("public void add_long" + i.name + "()", 1);
				writeLine("{", 1);
				writeLine("assertThrows(ValidationException.class, () -> dao.add(createValid()." + i.withMethodName + "(StringUtils.repeat(\"A\", " + maxLenName + " + 1))));", 2);
				writeLine("}", 1);
			}
			if (i.isImportedKey)
			{
				var invalidId_ = i.isCharacter ? "\"INVALID\"" : (10 < i.size) ? "1000L" : "1000";
				writeLine();
				writeLine("@Test", 1);
				writeLine("public void add_invalid" + i.name + "()", 1);
				writeLine("{", 1);
				writeLine("assertThrows(ValidationException.class, () -> dao.add(createValid()." + i.withMethodName + "(VALUE.id + " + invalidId_ + ")));", 2);
				writeLine("}", 1);
			}
		}

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void find()", 1);
		writeLine("{", 1);
		writeLine("var record = dao.findWithException(VALUE.id);", 2);
		writeLine("Assertions.assertNotNull(record, \"Exists\");", 2);
		writeLine("check(VALUE, record);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void findWithException()", 1);
		writeLine("{", 1);
		writeLine("assertThrows(ValidationException.class, () -> dao.findWithException(VALUE.id + " + invalidId + "));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void get()", 1);
		writeLine("{", 1);
		writeLine("var value = dao.getByIdWithException(VALUE.id);", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void getWithException()", 1);
		writeLine("{", 1);
		writeLine("assertThrows(ValidationException.class, () -> dao.getByIdWithException(VALUE.id + " + invalidId + "));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("public static Stream<Arguments> modif()", 1);
		writeLine("{", 1);
		writeLine("var valid = createValid();", 2);
		writeLine();
		writeLine("return Stream.of(", 2);
		for (var i : columnInfo)
		{
			writeLine("arguments(new " + filterName + "()." + i.withMethodName + "(VALUE." + i.memberVariableName + "), 1L),", 3);
		}

		writeLine();
		writeLine("// Negative tests", 3);
		for (var i : columnInfo)
		{
			writeLine("arguments(new " + filterName + "()." + i.withMethodName + "(valid." + i.memberVariableName + "), 0L),", 3);
		}
		writeLine(");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@ParameterizedTest(name=\"modif(filter={0}, expectedTotal={1})\")", 1);
		writeLine("@MethodSource", 1);
		writeLine("public void modif(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("count(filter, expectedTotal);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify()", 1);
		writeLine("{", 1);
		writeLine("// TODO: provide a change to the VALUE.", 2);
		writeLine("var value = dao.update(VALUE);", 2);
		writeLine("Assertions.assertNotNull(value, \"Exists\");", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("public static Stream<Arguments> modify_count()", 1);
		writeLine("{", 1);
		writeLine("var valid = createValid();", 2);
		writeLine();
		writeLine("return Stream.of(", 2);
		for (var i : columnInfo)
		{
			writeLine("arguments(new " + filterName + "()." + i.withMethodName + "(VALUE." + i.memberVariableName + "), 0L),", 3);
		}

		writeLine();
		writeLine("// Negative tests", 3);
		for (var i : columnInfo)
		{
			writeLine("arguments(new " + filterName + "()." + i.withMethodName + "(valid." + i.memberVariableName + "), 1L),", 3);
		}
		writeLine(");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@ParameterizedTest(name=\"modify_count(filter={0}, expectedTotal={1})\")", 1);
		writeLine("@MethodSource", 1);
		writeLine("public void modify_count(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("count(filter, expectedTotal);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify_find()", 1);
		writeLine("{", 1);
		writeLine("var valid = createValid();", 2);
		writeLine("var record = dao.findWithException(VALUE.id);", 2);
		writeLine("Assertions.assertNotNull(record, \"Exists\");", 2);
		writeLine("// TODO: check the changed property.", 2);
		for (var i : columnInfo)
			writeLine("Assertions.assertEquals(valid." + i.memberVariableName + ", record." + i.accessorMethodName + "(), \"Check " + i.memberVariableName + "\");", 2);
		writeLine("check(VALUE, record);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("public static Stream<Arguments> search()", 1);
		writeLine("{", 1);
		writeLine("var hourAgo = hourAgo();", 2);
		writeLine("var hourAhead = hourAhead();", 2);
		writeLine();
		writeLine("return Stream.of(", 2);
		for (var i : columnInfo)
		{
			writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "(VALUE." + i.memberVariableName + "), 1L),", 3);
			if (i.isNullable)
				writeLine("arguments(new " + filterName + "(1, 20).withHas" + i.name + "(true), 1L),", 3);
			if (i.isTime)
			{
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAgo), 1L),", 3);
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "To(hourAhead), 1L),", 3);
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAgo)." + i.withMethodName + "To(hourAhead), 1L),", 3);
			}
			else if (i.isRange())
			{
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "From(VALUE." + i.memberVariableName + "), 1L),", 3);
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "To(VALUE." + i.memberVariableName + "), 1L),", 3);
			}
		}

		writeLine();
		writeLine("// Negative tests", 3);
		for (var i : columnInfo)
		{
			if (i.isCharacter)
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "(\"invalid\"), 0L),", 3);
			else if (i.isBoolean)
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "(!VALUE." + i.memberVariableName + "), 0L),", 3);
			else
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "(VALUE." + i.memberVariableName + " + 1000" + ((10 < i.size) ? "L" : "") + "), 0L),", 3);

			if (i.isNullable)
				writeLine("arguments(new " + filterName + "(1, 20).withHas" + i.name + "(false), 0L),", 3);

			if (i.isTime)
			{
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAhead), 0L),", 3);
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "To(hourAgo), 0L),", 3);
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "From(hourAhead)." + i.withMethodName + "To(hourAgo), 0L),", 3);
			}
			else if (i.isRange())
			{
				var l = ((10 < i.size) ? "L" : "");
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "From(VALUE." + i.memberVariableName + " + 1" + l + "), 0L),", 3);
				writeLine("arguments(new " + filterName + "(1, 20)." + i.withMethodName + "To(VALUE." + i.memberVariableName + " - 1" + l + "), 0L),", 3);
			}
		}
		writeLine(");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@ParameterizedTest(name=\"search(filter={0}, expectedTotal={1})\")", 1);
		writeLine("@MethodSource", 1);
		writeLine("public void search(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("var results = dao.search(filter);", 2);
		writeLine("Assertions.assertNotNull(results, \"Exists\");", 2);
		writeLine("Assertions.assertEquals(expectedTotal, results.getTotal(), \"Check total\");", 2);
		writeLine("if (0L == expectedTotal)", 2);
		writeLine("Assertions.assertNull(results.getRecords(), \"Records exist\");", 3);
		writeLine("else", 2);
		writeLine("{", 2);
		writeLine("Assertions.assertNotNull(results.getRecords(), \"Records exists\");", 3);
		writeLine("int total = (int) expectedTotal;", 3);
		writeLine("if (total > results.getPageSize())", 3);
		writeLine("{", 3);
		writeLine("if (results.getPage() == results.getPages())", 4);
		writeLine("total%= results.getPageSize();", 5);
		writeLine("else", 4);
		writeLine("total = results.getPageSize();", 5);
		writeLine("}", 3);
		writeLine("Assertions.assertEquals(total, results.getRecords().size(), \"Check records.size\");", 3);
		writeLine("}", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("public static Stream<Arguments> search_sort()", 1);
		writeLine("{", 1);
		writeLine("return Stream.of(", 2);
		int size = columnInfo.length;
		for (var i : columnInfo)
		{
			var last = (0 == --size);
			var defaultDir = (i.isAutoIncrementing || i.isBoolean || i.isRange()) ? "DESC" : "ASC";
			var lastTerm = last && !i.isImportedKey ? "" : ",";
			writeLine("arguments(new " + filterName + "(\"" + i.memberVariableName + "\", null), \"" + i.memberVariableName + "\", \"" + defaultDir + "\"), // Missing sort direction is converted to the default.", 3);
			writeLine("arguments(new " + filterName + "(\"" + i.memberVariableName + "\", \"ASC\"), \"" + i.memberVariableName + "\", \"ASC\"),", 3);
			writeLine("arguments(new " + filterName + "(\"" + i.memberVariableName + "\", \"asc\"), \"" + i.memberVariableName + "\", \"ASC\"),", 3);
			writeLine("arguments(new " + filterName + "(\"" + i.memberVariableName + "\", \"invalid\"), \"" + i.memberVariableName + "\", \"" + defaultDir + "\"),	// Invalid sort direction is converted to the default.", 3);
			writeLine("arguments(new " + filterName + "(\"" + i.memberVariableName + "\", \"DESC\"), \"" + i.memberVariableName + "\", \"DESC\"),", 3);
			writeLine("arguments(new " + filterName + "(\"" + i.memberVariableName + "\", \"desc\"), \"" + i.memberVariableName + "\", \"DESC\")" + lastTerm, 3);

			if (i.isImportedKey)
			{
				lastTerm = last ? "" : ",";
				writeLine();
				writeLine("arguments(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", null), \"" + i.importedKeyMemberName + "Name\", \"" + defaultDir + "\"), // Missing sort direction is converted to the default.", 3);
				writeLine("arguments(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"ASC\"), \"" + i.importedKeyMemberName + "Name\", \"ASC\"),", 3);
				writeLine("arguments(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"asc\"), \"" + i.importedKeyMemberName + "Name\", \"ASC\"),", 3);
				writeLine("arguments(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"invalid\"), \"" + i.importedKeyMemberName + "Name\", \"" + defaultDir + "\"),	// Invalid sort direction is converted to the default.", 3);
				writeLine("arguments(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"DESC\"), \"" + i.importedKeyMemberName + "Name\", \"DESC\"),", 3);
				writeLine("arguments(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"desc\"), \"" + i.importedKeyMemberName + "Name\", \"DESC\")" + lastTerm, 3);
			}

			if (!last) writeLine();
		}
		writeLine(");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@ParameterizedTest(name=\"search_sort(filter={0}, expectedSortOn={1}, expectedSortDir={2})\")", 1);
		writeLine("@MethodSource", 1);
		writeLine("public void search_sort(final " + filterName + " filter, final String expectedSortOn, final String expectedSortDir)", 1);
		writeLine("{", 1);
		writeLine("var results = dao.search(filter);", 2);
		writeLine("Assertions.assertNotNull(results, \"Exists\");", 2);
		writeLine("Assertions.assertEquals(expectedSortOn, results.getSortOn(), \"Check sortOn\");", 2);
		writeLine("Assertions.assertEquals(expectedSortDir, results.getSortDir(), \"Check sortDir\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("public static Stream<Arguments> testRemove()", 1);
		writeLine("{", 1);
		writeLine("return Stream.of(", 2);
		writeLine("arguments(VALUE.id + " + invalidId + ", false),", 3);
		writeLine("arguments(VALUE.id, true),", 3);
		writeLine("arguments(VALUE.id, false));	// Already removed", 3);
		writeLine("}", 1);

		writeLine();
		writeLine("@ParameterizedTest(name=\"testRemove(id={0}, success={1})\")", 1);
		writeLine("@MethodSource", 1);
		writeLine("public void testRemove(final " + getPkJavaType() + " id, final boolean success)", 1);
		writeLine("{", 1);
		writeLine("Assertions.assertEquals(success, dao.remove(id));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("@Test", 1);
		writeLine("public void testRemove_find()", 1);
		writeLine("{", 1);
		writeLine("assertThrows(ValidationException.class, () -> dao.findWithException(VALUE.id));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("public static Stream<Arguments> testRemove_search()", 1);
		writeLine("{", 1);
		writeLine("return Stream.of(", 2);
		for (var i : columnInfo)
		{
			writeLine("arguments(new " + filterName + "()." + i.withMethodName + "(VALUE." + i.memberVariableName + "), 0L),", 3);
		}
		writeLine(");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@ParameterizedTest(name=\"testRemove_search(filter={0}, expectedTotal={1})\")", 1);
		writeLine("@MethodSource", 1);
		writeLine("public void testRemove_search(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("count(filter, expectedTotal);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the DAO count call and compares the expected total value.", 1);
		writeLine(" *", 1);
		writeLine(" * @param filter", 1);
		writeLine(" * @param expectedTotal", 1);
		writeLine(" */", 1);
		writeLine("private void count(final " + filterName + " filter, final long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("Assertions.assertEquals(expectedTotal, dao.count(filter), \"COUNT \" + filter + \": Check total\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied entity record. */", 1);
		writeLine("private void check(final " + valueName + " expected, final " + name + " record)", 1);
		writeLine("{", 1);
		writeLine("var assertId = \"ID (\" + expected.id + \"): \";", 2);
		for (var i : columnInfo)
			writeLine("Assertions.assertEquals(expected." + i.memberVariableName + ", record." + i.accessorMethodName + "(), assertId + \"Check " + i.memberVariableName + "\");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(final " + valueName + " expected, final " + valueName + " value)", 1);
		writeLine("{", 1);
		writeLine("var assertId = \"ID (\" + expected.id + \"): \";", 2);
		for (var i : columnInfo)
		{
			writeLine("Assertions.assertEquals(expected." + i.memberVariableName + ", value." + i.memberVariableName + ", assertId + \"Check " + i.memberVariableName + "\");", 2);
			if (i.isImportedKey)
				writeLine("Assertions.assertEquals(expected." + i.importedKeyMemberName + "Name, value." + i.importedKeyMemberName + "Name, assertId + \"Check " + i.importedKeyMemberName + "Name\");", 2);
		}
		writeLine("}", 1);
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/** Command line entry point.
		@param args1 Output directory.
		@param args2 URL to the data source.
		@param args3 data source login name.
		@param args4 data source password.
		@param args5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param args6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param args7 package name of the entity bean value object.
		@param args8 application version number
		@param args9 table name filter
	*/
	public static void main(final String... args)
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			final File dir = extractOutputDirectory(args, 0);
			final String author = extractAuthor(args, 5);
			final String packageName = extractArgument(args, 6, null);
			final String version = extractArgument(args, 7, null);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityBeanDAOTest(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityBeanDAOTest.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Version]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
