package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates the REST controller class for each entities' services. The metadata
*	is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 1/8/2012
*
***************************************************************************************/

public class EntityRestController extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "Controller";

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
	public EntityRestController(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public EntityRestController(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Version of application
	*/
	public EntityRestController(PrintWriter writer,
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
		String packageName = getPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		writeLine("import org.springframework.beans.factory.annotation.Autowired;");
		writeLine("import org.springframework.stereotype.Controller;");
		writeLine("import org.springframework.web.bind.annotation.*;");
		writeLine();
		writeLine("import com.fieldlens.common.exception.ValidationException;");
		writeLine("import com.fieldlens.common.spring.web.SpringRestBase;");
		writeLine("import com.fieldlens.common.value.QueryResults;");
		writeLine("import com.fieldlens.platform.service." + getObjectName() + "Service;");
		writeLine("import com.fieldlens.platform.value.*;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tRESTful Controller that provides access to the " + getObjectName() + " Service.");
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
		// Parent URI mapping.
		String mapping = fromObjectNameToMemberName(getObjectName()) + "s";
		
		writeLine();
		writeLine("@Controller");
		writeLine("@RequestMapping(value=\"/member/" + mapping + "\")");
		writeLine("public class " + getObjectName() + "Controller extends SpringRestBase");
		writeLine("{");
		writeLine("@Autowired " + getObjectName() + "Service service;", 1);
		writeLine();
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		// Get the primary key Java type. Assume that it is NOT a composite key.
		String primaryKeyType = "Integer";
		for (final ColumnInfo column : columnInfo)
		{
			if (column.isPartOfPrimaryKey)
			{
				primaryKeyType = column.javaType;
				break;
			}
		}

		// Parent URI mapping.
		String mapping = fromObjectNameToMemberName(getObjectName()) + "s";

		writeLine("/** URI: /member/" + mapping + "/{id} &mdash;", 1);
		writeLine(" *  METHOD: GET", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Gets a single " + getObjectName() + " value.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/{id}\", method=RequestMethod.GET)", 1);
		writeLine("public " + getValueObjectName() + " get(@PathVariable(value=\"id\") " + primaryKeyType + " id)", 1);
		writeLine("{", 1);
			writeLine("return service.getById(id);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** URI: /member/" + mapping + "/model &mdash;", 1);
		writeLine(" *  METHOD: POST", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Gets the data that represents the " + getObjectName() + " entry form.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/model\", method=RequestMethod.POST)", 1);
		writeLine("public " + getObjectName() + "Model model(@RequestParam(value=\"id\", required=false) " + primaryKeyType + " id)", 1);
		writeLine("{", 1);
			writeLine("return service.getModel(id);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** URI: /member/" + mapping + "/add &mdash;", 1);
		writeLine(" *  METHOD: POST", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Adds a single " + getObjectName() + " value.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/add\", method=RequestMethod.POST)", 1);
		writeLine("public " + getValueObjectName() + " add(@RequestBody " + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return service.add(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** URI: /member/" + mapping + "/set &mdash;", 1);
		writeLine(" *  METHOD: POST", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Updates a single " + getObjectName() + " value.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/set\", method=RequestMethod.POST)", 1);
		writeLine("public " + getValueObjectName() + " set(@RequestBody " + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("service.update(value);", 2);
			writeLine();
			writeLine("return value;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** URI: /member/" + mapping + "/remove &mdash;", 1);
		writeLine(" *  METHOD: POST", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Removes a single " + getObjectName() + " value.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/remove\", method=RequestMethod.POST)", 1);
		writeLine("public Boolean remove(@RequestBody " + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("service.remove(value);", 2);
			writeLine();
			writeLine("return true;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** URI: /member/" + mapping + "/search &mdash;", 1);
		writeLine(" *  METHOD: POST", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Searches the " + getObjectName() + " entity based on the supplied filter.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/search\", method=RequestMethod.POST)", 1);
		writeLine("public QueryResults<" + getValueObjectName() + "> search(@RequestBody " + getObjectName() + "Filter filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return service.search(filter);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** URI: /member/" + mapping + "/history/{id} &mdash;", 1);
		writeLine(" *  METHOD: GET", 1);
		writeLine(" *  <br />", 1);
		writeLine(" *  Gets the history of changes on the specified " + getObjectName() + " entity.", 1);
		writeLine(" */", 1);
		writeLine("@ResponseBody", 1);
		writeLine("@RequestMapping(value=\"/history/{id}\", method=RequestMethod.GET)", 1);
		writeLine("public QueryResults<" + getObjectName() + "JValue> history(@PathVariable(value=\"id\") " + primaryKeyType + " id)", 1);
		writeLine("{", 1);
			writeLine("return service.history(id);", 2);
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
		@param strArg1 Output directory.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 package name of the entity bean value object.
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
			final String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityRestController(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityRestController.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
