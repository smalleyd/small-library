package com.small.library.ejb.gen;

import java.io.*;

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
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Controller";

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
	public EntityRestController() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityRestController(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityRestController(PrintWriter writer,
		String author, Tables.Record table, String packageName)
	{
		super(writer, author, table, packageName);
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
		String strPackageName = getPackageName();

		if (null != strPackageName)
		{
			writeLine("package " + strPackageName + ";");
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
		writeLine("*\t@version 1.0.1");
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
		for (ColumnInfo column : m_ColumnInfo)
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
	public static void main(String strArgs[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(strArgs, 0);
			String strAuthor = extractAuthor(strArgs, 5);
			String strPackageName = extractArgument(strArgs, 6, null);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 7);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityRestController pGenerator =
				new EntityRestController((PrintWriter) null, strAuthor,
				(Tables.Record) null, strPackageName);

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

			System.out.println("Usage: java " + EntityRestController.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
