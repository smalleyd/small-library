package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

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
	public static final String CLASS_NAME_SUFFIX = "Resource";

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
	public EntityJerseyResource(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public EntityJerseyResource(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version represents the version of the resource.
	*/
	public EntityJerseyResource(PrintWriter writer,
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

		writeLine("import java.util.List;");
		writeLine();
		writeLine("import javax.ws.rs.*;");
		writeLine();
		writeLine("import io.dropwizard.hibernate.UnitOfWork;");
		writeLine("import io.swagger.annotations.*;");
		writeLine();
		writeLine("import com.codahale.metrics.annotation.Timed;");
		writeLine("import " + domainPackageName + ".dwservice.dao.QueryResults;");
		writeLine("import " + domainPackageName + ".dwservice.errors.ValidationException;");
		writeLine("import " + domainPackageName + ".dwservice.mediatype.UTF8MediaType;");
		writeLine("import " + domainPackageName + ".dwservice.value.OperationResponse;");
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
		final String name = getObjectName();
		final String daoName = EntityBeanDAO.getClassName(name);
		final String mapping = fromObjectNameToMemberName(name) + "s";
		
		writeLine();
		writeLine("@Path(\"/" + mapping + "\")");
		writeLine("@Consumes(UTF8MediaType.APPLICATION_JSON)");
		writeLine("@Produces(UTF8MediaType.APPLICATION_JSON)");
		writeLine("@Api(value=\"" + name + "\")");
		writeLine("public class " + getClassName());
		writeLine("{");
		writeLine("private final " + daoName + " dao;", 1);
		writeLine();
		writeLine("/** Populator.", 1);
		writeLine(" * ", 1);
		writeLine(" * @param dao", 1);
		writeLine(" */", 1);
		writeLine("public " + getClassName() + "(final " + daoName + " dao)", 1);
		writeLine("{", 1);
		writeLine("this.dao = dao;", 2);
		writeLine("}", 1);
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
		var name = getObjectName();
		var valueName = getValueObjectName();
		var filterName = EntityBeanFilter.getClassName(name);

		writeLine();
		writeLine("@GET", 1);
		writeLine("@Path(\"/{id}\") @Timed @UnitOfWork(readOnly=true, transactional=false)", 1);
		writeLine("@ApiOperation(value=\"get\", notes=\"Gets a single " + name + " by its primary key.\", response=" + valueName + ".class)", 1);
		writeLine("public " + valueName + " get(@PathParam(\"id\") final " + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.getByIdWithException(id);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@GET", 1);
		writeLine("@Timed @UnitOfWork(readOnly=true, transactional=false)", 1);
		writeLine("@ApiOperation(value=\"find\", notes=\"Finds " + name + "s by wildcard name search.\", response=" + valueName + ".class, responseContainer=\"List\")", 1);
		writeLine("public List<" + valueName + "> find(@QueryParam(\"name\") @ApiParam(name=\"name\", value=\"Value for the wildcard search\") final String name)", 1);
		writeLine("{", 1);
			writeLine("return dao.getActiveByIdOrName(name);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@POST", 1);
		writeLine("@Timed @UnitOfWork", 1);
		writeLine("@ApiOperation(value=\"add\", notes=\"Adds a single " + name + ". Returns the supplied " + name + " value with the auto generated identifier populated.\", response=" + valueName + ".class)", 1);
		writeLine("public " + valueName + " add(final " + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.add(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@PUT", 1);
		writeLine("@Timed @UnitOfWork", 1);
		writeLine("@ApiOperation(value=\"set\", notes=\"Updates an existing single " + name + ". Returns the supplied " + name + " value with the auto generated identifier populated.\", response=" + valueName + ".class)", 1);
		writeLine("public " + valueName + " set(final " + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.update(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@DELETE", 1);
		writeLine("@Path(\"/{id}\") @Timed @UnitOfWork", 1);
		writeLine("@ApiOperation(value=\"remove\", notes=\"Removes/deactivates a single " + name + " by its primary key.\")", 1);
		writeLine("public OperationResponse remove(@PathParam(\"id\") final " + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return new OperationResponse(dao.remove(id));", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@POST", 1);
		writeLine("@Path(\"/search\") @Timed @UnitOfWork(readOnly=true, transactional=false)", 1);
		writeLine("@ApiOperation(value=\"search\", notes=\"Searches the " + name + "s based on the supplied filter.\", response=QueryResults.class)", 1);
		writeLine("public QueryResults<" + valueName + ", " + filterName + "> search(final " + filterName + " filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return dao.search(filter);", 2);
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
			final String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityJerseyResource(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
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

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
