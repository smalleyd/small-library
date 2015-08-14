package com.small.library.ejb.gen;

import java.io.*;

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
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "DAOTest";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanDAOTest() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanDAOTest(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version.
	*/
	public EntityBeanDAOTest(PrintWriter writer,
		String author, Tables.Record table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMethods();

		writeFooter();
	}

	/******************************************************************************
	*
	*	Required methods: BaseTable
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Tables.Record pTable)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(pTable.getName())) + ".java";
	}

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String packageName = getPackageName();
		String domainPackageName = getDomainPackageName();
		String basePackageName = getBasePackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		String name = getObjectName();
		writeLine("import org.apache.commons.lang3.StringUtils;");
		writeLine("import org.hibernate.SessionFactory;");
		writeLine("import org.junit.*;");
		writeLine("import org.junit.runners.MethodSorters;");
		writeLine();
		writeLine("import " + domainPackageName + ".junit.hibernate.*;");
		writeLine("import " + domainPackageName + ".common.dao.QueryResults;");
		writeLine("import " + domainPackageName + ".common.error.ValidationException;");
		writeLine("import " + basePackageName + "." + getAppName() + "Application;");
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
		String name = getClassName();
		String daoName = EntityBeanDAO.getClassName(getObjectName());
		String valueName = EntityBeanValueObject.getClassName(getObjectName());

		writeLine();
		writeLine("@FixMethodOrder(MethodSorters.NAME_ASCENDING)	// Ensure that the methods are executed in order listed.");
		writeLine("public class " + name);
		writeLine("{");
		writeLine("@ClassRule", 1);
		writeLine("public static final HibernateRule DAO_RULE = new HibernateRule(" + getAppName() + "Application.ENTITIES);", 1);
		writeLine();
		writeLine("@Rule", 1);
		writeLine("public final HibernateTransactionRule transRule = new HibernateTransactionRule(DAO_RULE);", 1);
		writeLine();
		writeLine("private static " + daoName + " dao = null;", 1);
		writeLine("private static " + valueName + " VALUE = null;", 1);
		writeLine();
		writeLine("@BeforeClass", 1);
		writeLine("public static void up()", 1);
		writeLine("{", 1);
		writeLine("SessionFactory factory = DAO_RULE.getSessionFactory();", 2);
		writeLine("dao = new " + daoName + "(factory);", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		String name = getObjectName();
		String entityName = EntityBeanCMP3.getClassName(name);
		String filterName = EntityBeanFilter.getClassName(name);
		String valueName = EntityBeanValueObject.getClassName(name);

		// For testing non-existence, use a value to add to the primary key.
		String invalidId = "\"INVALID\"";
		for (ColumnInfo i : m_ColumnInfo)
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
		writeLine(valueName + " value = dao.add(VALUE = new " + valueName + "());", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		// Add validation checks.
		for (ColumnInfo i : m_ColumnInfo)
		{
			if (i.isAutoIncrementing)
				continue;

			if (!i.isNullable && !i.isPrimitive)
			{
				writeLine();
				writeLine("@Test(expected=ValidationException.class)", 1);
				writeLine("public void add_missing" + i.name + "()", 1);
				writeLine("{", 1);
				writeLine("dao.add(new " + valueName + "()." + i.withMethodName + "(null));", 2);
				writeLine("}", 1);
			}
			if (i.isString)
			{
				writeLine();
				writeLine("@Test(expected=ValidationException.class)", 1);
				writeLine("public void add_long" + i.name + "()", 1);
				writeLine("{", 1);
				writeLine("dao.add(new " + valueName + "()." + i.withMethodName + "(StringUtils.repeat(\"A\", " + (i.size + 1) + ")));", 2);
				writeLine("}", 1);
			}
			if (i.isImportedKey)
			{
				String invalidId_ = i.isCharacter ? "\"INVALID\"" : (10 < i.size) ? "1000L" : "1000";
				writeLine();
				writeLine("@Test(expected=ValidationException.class)", 1);
				writeLine("public void add_invalid" + i.name + "()", 1);
				writeLine("{", 1);
				writeLine("dao.add(new " + valueName + "()." + i.withMethodName + "(VALUE.getId() + " + invalidId_ + "));", 2);
				writeLine("}", 1);
			}
		}

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void find()", 1);
		writeLine("{", 1);
		writeLine(entityName + " record = dao.findWithException(VALUE.getId());", 2);
		writeLine("Assert.assertNotNull(\"Exists\", record);", 2);
		writeLine("check(VALUE, record);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test(expected=ValidationException.class)", 1);
		writeLine("public void findWithException()", 1);
		writeLine("{", 1);
		writeLine("dao.findWithException(VALUE.getId() + " + invalidId + ");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void get()", 1);
		writeLine("{", 1);
		writeLine(valueName + " value = dao.getByIdWithException(VALUE.getId());", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test(expected=ValidationException.class)", 1);
		writeLine("public void getWithException()", 1);
		writeLine("{", 1);
		writeLine("dao.getByIdWithException(VALUE.getId() + " + invalidId + ");", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify()", 1);
		writeLine("{", 1);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 1L);", 2);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 0L);", 2);
		writeLine();
		writeLine("// TODO: provide a change to the VALUE.", 2);
		writeLine(valueName + " value = dao.update(VALUE);", 2);
		writeLine("Assert.assertNotNull(\"Exists\", value);", 2);
		writeLine("check(VALUE, value);", 2);
		writeLine();
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 0L);", 2);
		writeLine("// TODO: fill in search details // count(new " + filterName + "(), 1L);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void modify_find()", 1);
		writeLine("{", 1);
		writeLine(entityName + " record = dao.findWithException(VALUE.getId());", 2);
		writeLine("Assert.assertNotNull(\"Exists\", record);", 2);
		writeLine("// TODO: check the changed property.", 2);
		writeLine("check(VALUE, record);", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void search()", 1);
		writeLine("{", 1);
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "(VALUE." + i.accessorMethodName + "()), 1L);", 2);
			if (i.isRange())
			{
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "From(VALUE." + i.accessorMethodName + "()), 1L);", 2);
				writeLine("search(new " + filterName + "(1, 20)." + i.withMethodName + "To(VALUE." + i.accessorMethodName + "()), 1L);", 2);
			}
		}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method: performs the search and checks the counts. */", 1);
		writeLine("private void search(" + filterName + " filter, long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("QueryResults<" + valueName + ", " + filterName + "> results = dao.search(filter);", 2);
		writeLine("String assertId = \"SEARCH \" + filter + \": \";", 2);
		writeLine("Assert.assertNotNull(assertId + \"Exists\", results);", 2);
		writeLine("Assert.assertEquals(assertId + \"Check total\", expectedTotal, results.getTotal());", 2);
		writeLine("if (0L == expectedTotal)", 2);
		writeLine("Assert.assertNull(assertId + \"Records exist\", results.getRecords());", 3);
		writeLine("else", 2);
		writeLine("{", 2);
		writeLine("Assert.assertNotNull(assertId + \"Records exists\", results.getRecords());", 3);
		writeLine("int total = (int) expectedTotal;", 3);
		writeLine("if (total > results.getPageSize())", 3);
		writeLine("{", 3);
		writeLine("if (results.getPage() == results.getPages())", 4);
		writeLine("total%= results.getPageSize();", 5);
		writeLine("else", 4);
		writeLine("total = results.getPageSize();", 5);
		writeLine("}", 3);
		writeLine("Assert.assertEquals(assertId + \"Check records.size\", total, results.getRecords().size());", 3);
		writeLine("}", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("@Test", 1);
		writeLine("public void search_sort()", 1);
		writeLine("{", 1);
		int size = m_ColumnInfo.length;
		for (ColumnInfo i : m_ColumnInfo)
		{
			String defaultDir = (i.isAutoIncrementing || i.isBoolean || i.isRange()) ? "DESC" : "ASC";
			writeLine("search_sort(new " + filterName + "(\"" + i.memberVariableName + "\", null), \"" + i.memberVariableName + "\", \"" + defaultDir + "\"); // Missing sort direction is converted to the default.", 2);
			writeLine("search_sort(new " + filterName + "(\"" + i.memberVariableName + "\", \"ASC\"), \"" + i.memberVariableName + "\", \"ASC\");", 2);
			writeLine("search_sort(new " + filterName + "(\"" + i.memberVariableName + "\", \"asc\"), \"" + i.memberVariableName + "\", \"ASC\");", 2);
			writeLine("search_sort(new " + filterName + "(\"" + i.memberVariableName + "\", \"invalid\"), \"" + i.memberVariableName + "\", \"" + defaultDir + "\");	// Invalid sort direction is converted to the default.", 2);
			writeLine("search_sort(new " + filterName + "(\"" + i.memberVariableName + "\", \"DESC\"), \"" + i.memberVariableName + "\", \"DESC\");", 2);
			writeLine("search_sort(new " + filterName + "(\"" + i.memberVariableName + "\", \"desc\"), \"" + i.memberVariableName + "\", \"DESC\");", 2);

			if (i.isImportedKey)
			{
				writeLine();
				writeLine("search_sort(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", null), \"" + i.importedKeyMemberName + "Name\", \"" + defaultDir + "\"); // Missing sort direction is converted to the default.", 2);
				writeLine("search_sort(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"ASC\"), \"" + i.importedKeyMemberName + "Name\", \"ASC\");", 2);
				writeLine("search_sort(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"asc\"), \"" + i.importedKeyMemberName + "Name\", \"ASC\");", 2);
				writeLine("search_sort(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"invalid\"), \"" + i.importedKeyMemberName + "Name\", \"" + defaultDir + "\");	// Invalid sort direction is converted to the default.", 2);
				writeLine("search_sort(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"DESC\"), \"" + i.importedKeyMemberName + "Name\", \"DESC\");", 2);
				writeLine("search_sort(new " + filterName + "(\"" + i.importedKeyMemberName + "Name\", \"desc\"), \"" + i.importedKeyMemberName + "Name\", \"DESC\");", 2);
			}

			if (0 < --size)
				writeLine();
		}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method: performs the search and checks sort option fields. */", 1);
		writeLine("private void search_sort(" + filterName + " filter, String expectedSortOn, String expectedSortDir)", 1);
		writeLine("{", 1);
		writeLine("QueryResults<" + valueName + ", " + filterName + "> results = dao.search(filter);", 2);
		writeLine("String assertId = \"SEARCH_SORT (\" + filter.getSortOn() + \", \" + filter.getSortDir() + \"): \";", 2);
		writeLine("Assert.assertNotNull(assertId + \"Exists\", results);", 2);
		writeLine("Assert.assertEquals(assertId + \"Check sortOn\", expectedSortOn, results.getSortOn());", 2);
		writeLine("Assert.assertEquals(assertId + \"Check sortDir\", expectedSortDir, results.getSortDir());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("@Test", 1);
		writeLine("public void testRemove()", 1);
		writeLine("{", 1);
		writeLine("Assert.assertFalse(\"Invalid\", dao.remove(VALUE.getId() + " + invalidId + "));", 2);
		writeLine("Assert.assertTrue(\"Removed\", dao.remove(VALUE.getId()));", 2);
		writeLine("Assert.assertFalse(\"Already removed\", dao.remove(VALUE.getId()));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("@Test(expected=ValidationException.class)", 1);
		writeLine("public void testRemove_find()", 1);
		writeLine("{", 1);
		writeLine("dao.findWithException(VALUE.getId());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Test removal after the search. */", 1);
		writeLine("@Test", 1);
		writeLine("public void testRemove_search()", 1);
		writeLine("{", 1);
		writeLine("count(new " + filterName + "().withId(VALUE.getId()), 0L);", 2);
		writeLine("// TODO: provide secondary test count.", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - calls the DAO count call and compares the expected total value.", 1);
		writeLine(" *", 1);
		writeLine(" * @param filter", 1);
		writeLine(" * @param expectedTotal", 1);
		writeLine(" */", 1);
		writeLine("private void count(" + filterName + " filter, long expectedTotal)", 1);
		writeLine("{", 1);
		writeLine("Assert.assertEquals(\"COUNT \" + filter + \": Check total\", expectedTotal, dao.count(filter));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied entity record. */", 1);
		writeLine("private void check(" + valueName + " expected, " + name + " record)", 1);
		writeLine("{", 1);
		writeLine("String assertId = \"ID (\" + expected.getId() + \"): \";", 2);
		for (ColumnInfo i : m_ColumnInfo)
			writeLine("Assert.assertEquals(assertId + \"Check " + i.memberVariableName + "\", expected." + i.accessorMethodName + "(), record." + i.accessorMethodName + "());", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - checks an expected value against a supplied value object. */", 1);
		writeLine("private void check(" + valueName + " expected, " + valueName + " value)", 1);
		writeLine("{", 1);
		writeLine("String assertId = \"ID (\" + expected.getId() + \"): \";", 2);
		for (ColumnInfo i : m_ColumnInfo)
		{
			writeLine("Assert.assertEquals(assertId + \"Check " + i.memberVariableName + "\", expected." + i.accessorMethodName + "(), value." + i.accessorMethodName + "());", 2);
			if (i.isImportedKey)
				writeLine("Assert.assertEquals(assertId + \"Check " + i.importedKeyMemberName + " name\", expected.get" + i.importedKeyName + "Name(), value.get" + i.importedKeyName + "Name());", 2);
		}
		writeLine("}", 1);
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

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
		@param args8 application version.
	*/
	public static void main(String args[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(args, 0);
			String strAuthor = extractAuthor(args, 5);
			String strPackageName = extractArgument(args, 6, null);
			String version = extractArgument(args, 7, null);

			// Create and load the tables object.
			Tables pTables = extractTables(args, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanDAOTest pGenerator =
				new EntityBeanDAOTest((PrintWriter) null, strAuthor,
				(Tables.Record) null, strPackageName, version);

			// Call the BaseTable method to handle the outputing.
			generateTableResources(pGenerator, pTables, fileOutputDir);
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
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

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
