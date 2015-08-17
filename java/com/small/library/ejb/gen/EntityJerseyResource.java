package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates the Jersey RESTful resource class for each entities' services. The metadata
*	is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 5/20/2015
*
***************************************************************************************/

public class EntityJerseyResource extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Resource";

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
	public EntityJerseyResource() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityJerseyResource(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version represents the version of the resource.
	*/
	public EntityJerseyResource(PrintWriter writer,
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
		String strPackageName = getPackageName();
		String domainPackageName = getDomainPackageName();
		String basePackageName = getBasePackageName();

		if (null != strPackageName)
		{
			writeLine("package " + strPackageName + ";");
			writeLine();
		}

		String name = getObjectName();

		writeLine("import java.util.*;");
		writeLine();
		writeLine("import javax.ws.rs.*;");
		writeLine();
		writeLine("import io.dropwizard.hibernate.UnitOfWork;");
		writeLine("import io.swagger.annotations.*;");
		writeLine();
		writeLine("import com.codahale.metrics.annotation.Timed;");
		writeLine("import " + domainPackageName + ".common.dao.QueryResults;");
		writeLine("import " + domainPackageName + ".common.error.ValidationException;");
		writeLine("import " + domainPackageName + ".common.jersey.JerseyUtils;");
		writeLine("import " + domainPackageName + ".common.model.Model;");
		writeLine("import " + domainPackageName + ".common.value.NameValue;");
		writeLine("import " + basePackageName + ".dao." + EntityBeanDAO.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".filter." + EntityBeanFilter.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".value." + EntityBeanValueObject.getClassName(name) + ";");

		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tJersey RESTful resource that provides access to the " + getObjectName() + "DAO.");
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
		String name = getObjectName();
		String daoName = EntityBeanDAO.getClassName(name);
		String mapping = fromObjectNameToMemberName(name) + "s";
		
		writeLine();
		writeLine("@Path(\"/" + mapping + "\")");
		writeLine("@Consumes(JerseyUtils.APPLICATION_JSON)");
		writeLine("@Produces(JerseyUtils.APPLICATION_JSON)");
		writeLine("@Api(value=\"" + name + "\")");
		writeLine("public class " + getClassName());
		writeLine("{");
		writeLine("private final " + daoName + " dao;", 1);
		writeLine();
		writeLine("/** Populator.", 1);
		writeLine(" * ", 1);
		writeLine(" * @param dao", 1);
		writeLine(" */", 1);
		writeLine("public " + getClassName() + "(" + daoName + " dao)", 1);
		writeLine("{", 1);
		writeLine("this.dao = dao;", 2);
		writeLine("}", 1);
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
		String name = getObjectName();
		String filterName = EntityBeanFilter.getClassName(name);

		writeLine();
		writeLine("@GET", 1);
		writeLine("@Path(\"/{id}\") @Timed @UnitOfWork(readOnly=true, transactional=false)", 1);
		writeLine("@ApiOperation(value=\"get\", notes=\"Gets a single " + name + " by its primary key.\", response=" + getValueObjectName() + ".class)", 1);
		writeLine("public " + getValueObjectName() + " get(@PathParam(\"id\") " + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.getByIdWithException(id);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@GET", 1);
		writeLine("@Timed @UnitOfWork(readOnly=true, transactional=false)", 1);
		writeLine("@ApiOperation(value=\"find\", notes=\"Finds " + name + "s by wildcard name search.\", response=NameValue.class, responseContainer=\"List\")", 1);
		writeLine("public List<NameValue> find(@QueryParam(\"name\") @ApiParam(name=\"name\", value=\"Value for the wildcard search\") String name)", 1);
		writeLine("{", 1);
			writeLine("return dao.getActiveByIdOrName(name);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@POST", 1);
		writeLine("@Timed @UnitOfWork", 1);
		writeLine("@ApiOperation(value=\"add\", notes=\"Adds a single " + name + ". Returns the supplied client value with the auto generated identifier populated.\", response=" + getValueObjectName() + ".class)", 1);
		writeLine("public " + getValueObjectName() + " add(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.add(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@PUT", 1);
		writeLine("@Timed @UnitOfWork", 1);
		writeLine("@ApiOperation(value=\"set\", notes=\"Updates an existing single " + name + ". Returns the supplied client value with the auto generated identifier populated.\", response=" + getValueObjectName() + ".class)", 1);
		writeLine("public " + getValueObjectName() + " set(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.update(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@DELETE", 1);
		writeLine("@Path(\"/{id}\") @Timed @UnitOfWork", 1);
		writeLine("@ApiOperation(value=\"remove\", notes=\"Removes/deactivates a single " + name + " by its primary key.\")", 1);
		writeLine("public Model<Boolean> remove(@PathParam(\"id\") " + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return new Model<Boolean>(dao.remove(id));", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@POST", 1);
		writeLine("@Path(\"/search\") @Timed @UnitOfWork(readOnly=true, transactional=false)", 1);
		writeLine("@ApiOperation(value=\"search\", notes=\"Searches the " + name + "s based on the supplied filter.\", response=QueryResults.class)", 1);
		writeLine("public QueryResults<" + getValueObjectName() + ", " + filterName + "> search(" + filterName + " filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.search(filter);", 2);
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
		@param args8 application version number
		@param args9 table name filter
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
			EntityJerseyResource pGenerator =
				new EntityJerseyResource((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityJerseyResource.class.getName() + " Output directory");
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
