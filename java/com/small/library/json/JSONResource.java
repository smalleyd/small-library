package com.small.library.json;

import java.io.*;
import java.util.Date;

/** Generates an entity RESTful resource from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/26/2023
 *
 */

public class JSONResource extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "Resource";

	private final String className;
	private final String filterName;
	private final String daoName;

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONResource(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);

		className = getClassName(clazz.name);
		filterName = JSONFilter.getClassName(clazz.name);
		daoName = JSONElastic.getClassName(clazz.name);
	}

	@Override
	public void run()
	{
		writeHeader();
		writeClassDeclaration();

		writeConstructors();
		writeMethods();

		writeFooter();
	}

	private void writeHeader()
	{
		out.print("package "); out.print(appPackage); out.print(".rest"); out.println(";");

		out.println();
		out.println("import java.io.IOException;");
		out.println("import java.util.List;");
		out.println("import java.util.Map;");
		out.println("import javax.validation.Valid;");
		out.println("import javax.validation.constraints.*;");
		out.println("import javax.validation.groups.Default;");
		out.println("import javax.ws.rs.*;");
		out.println("import javax.ws.rs.core.MediaType;");
		out.println("import javax.ws.rs.core.Response;");
		out.println();
		out.println("import io.dropwizard.validation.Validated;");
		out.println("import io.swagger.v3.oas.annotations.*;");
		out.println("import io.swagger.v3.oas.annotations.media.Content;");
		out.println("import io.swagger.v3.oas.annotations.media.Schema;");
		out.println("import io.swagger.v3.oas.annotations.parameters.RequestBody;");
		out.println("import io.swagger.v3.oas.annotations.tags.Tag;");
		out.println();
		out.println("import com.codahale.metrics.annotation.Timed;");
		out.println();
		out.println("import " + domainPackage + ".common.model.Results;");
		out.println("import " + appPackage + ".constraint.MapConstraint;");
		out.println("import " + appPackage + ".constraint.OnlyAdd;");
		out.println("import " + appPackage + ".dao." + daoName + ";");
		out.println("import " + appPackage + ".domain." + clazz.name + ";");
		out.println("import " + appPackage + ".model." + filterName + ";");
		out.println();
		out.println("/** Represents the RESTful resource that provides access to the Elasticsearch " + clazz.name + " index.");
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
		out.println("@Path(\"/" + clazz.path + "\")");
		out.println("@Consumes(MediaType.APPLICATION_JSON)");
		out.println("@Produces(MediaType.APPLICATION_JSON)");
		out.println("@Tag(name=\"" + clazz.plural + "\", description=\"Handles the " + clazz.caption + ".\")");
		out.println("public class " + className);
		out.println("{");
		out.println("\tprivate final " + daoName + " dao;");
	}

	private void writeConstructors()
	{
		out.println();
		out.println("\tpublic " + className + "(final " + daoName + " dao)");
		out.println("\t{");
		out.println("\t\tthis.dao = dao;");
		out.println("\t}");
	}

	private void writeMethods()
	{
		out.println();
		out.println("\t@GET");
		out.println("\t@Path(\"/{id}\") @Timed");
		out.println("\t@Operation(summary=\"get\", description=\"Gets a single " + clazz.name + " value by ID.\")");
		out.println("\tpublic " + clazz.name + " get(@PathParam(\"id\") final String id) throws IOException, NotFoundException");
		out.println("\t{");
		out.println("\t\treturn dao.getById(id);");
		out.println("\t}");

		out.println();
		out.println("\t@GET");
		out.println("\t@Timed");
		out.println("\t@Operation(summary=\"find\", description=\"Finds " + clazz.name + " values by term match.\")");
		out.println("\tpublic List<" + clazz.name + "> find(@QueryParam(\"term\") @Parameter(name=\"term\", description=\"Represents the term on which to match.\", required=true) @NotBlank final String term,");
		out.println("\t\t@QueryParam(\"pageSize\") @Parameter(name=\"pageSize\", description=\"Represents the number of records to return.\", required=false) @DefaultValue(\"20\") @Min(1) @Max(1000) final int pageSize) throws IOException");
		out.println("\t{");
		out.println("\t\treturn dao.getByTerm(term, pageSize);");
		out.println("\t}");

		out.println();
		out.println("\t@POST");
		out.println("\t@Timed");
		out.println("\t@Operation(summary=\"add\", description=\"Adds a new single " + clazz.name + " value.\")");
		out.println("\tpublic " + clazz.name + " add(@NotNull @Valid @Validated({Default.class, OnlyAdd.class}) final " + clazz.name + " value) throws IOException");
		out.println("\t{");
		out.println("\t\treturn dao.index(value);");
		out.println("\t}");

		out.println();
		out.println("\t@PUT");
		out.println("\t@Timed");
		out.println("\t@Operation(summary=\"set\", description=\"Updates an existing single " + clazz.name + " value.\")");
		out.println("\tpublic " + clazz.name + " set(@NotNull @Valid @Validated({Default.class, OnlyAdd.class}) final " + clazz.name + " value) throws IOException");
		out.println("\t{");
		out.println("\t\treturn dao.index(value);");
		out.println("\t}");

		out.println();
		out.println("\t@PATCH");
		out.println("\t@Timed");
		out.println("\t@Operation(summary=\"patch\", description=\"Patches/merges an existing single " + clazz.name + " value.\")");
		out.println("\tpublic " + clazz.name + " patch(@NotNull @Valid final " + clazz.name + " value) throws IOException, NotFoundException");
		out.println("\t{");
		out.println("\t\treturn dao.patch(value);");
		out.println("\t}");

		out.println();
		out.println("\t@PATCH");
		out.println("\t@Path(\"/{id}\") @Timed");
		out.println("\t@Operation(summary=\"patch\",");
		out.println("\t\tdescription=\"Patches/merges an existing single " + clazz.name + " value.\",");
		out.println("\t\trequestBody=@RequestBody(content=@Content(schema=@Schema(implementation=" + clazz.name + ".class))))");
		out.println("\tpublic " + clazz.name + " patch(@PathParam(\"id\") final String id,");
		out.println("\t\t@NotNull @MapConstraint(" + clazz.name + ".class) final Map<String, Object> value) throws IOException, NotFoundException");
		out.println("\t{");
		out.println("\t\treturn dao.patch(id, value);");
		out.println("\t}");

		out.println();
		out.println("\t@DELETE");
		out.println("\t@Path(\"/{id}\") @Timed");
		out.println("\t@Operation(summary=\"remove\", description=\"Removes an existing " + clazz.name + " value.\")");
		out.println("\tpublic " + clazz.name + " remove(@PathParam(\"id\") final String id) throws IOException, NotFoundException");
		out.println("\t{");
		out.println("\t\treturn dao.remove(id);");
		out.println("\t}");

		out.println();
		out.println("\t@POST");
		out.println("\t@Path(\"/search\") @Timed");
		out.println("\t@Operation(summary=\"search\", description=\"Searches the " + clazz.name + " data set.\")");
		out.println("\tpublic Results<" + clazz.name + "> search(@NotNull final " + filterName + " request) throws IOException");
		out.println("\t{");
		out.println("\t\treturn dao.search(request);");
		out.println("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
