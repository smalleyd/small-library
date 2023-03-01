package com.small.library.json;

import java.io.*;
import java.util.Date;

/** Generates an Elasticsearch data access object from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/26/2023
 *
 */

public class JSONElastic extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "ES";

	private final String className;
	private final String baseClass;
	private final String filterName;

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONElastic(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);

		className = getClassName(clazz.name);
		filterName = JSONFilter.getClassName(clazz.name);
		baseClass = clazz.cacheable ? "CachedAbstractDAO" : "AbstractDAO";
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
		out.print("package "); out.print(appPackage); out.print(".dao"); out.println(";");

		out.println();
		out.println("import static " + domainPackage + ".common.model.Entity.*;");
		out.println();
		out.println("import java.io.IOException;");
		out.println("import java.util.*;");
		out.println("import javax.ws.rs.NotFoundException;");
		out.println();
		out.println("import org.elasticsearch.client.RestHighLevelClient;");
		out.println("import org.elasticsearch.index.query.QueryBuilder;");
		out.println("import org.elasticsearch.index.query.QueryBuilders;");
		out.println("import org.slf4j.*;");
		out.println();
		out.println("import com.fasterxml.jackson.core.type.TypeReference;");
		out.println();
		out.println("import " + domainPackage + ".es." + baseClass + ";");
		out.println("import " + appPackage + ".domain." + clazz.name + ";");
		out.println("import " + appPackage + ".model." + filterName + ";");
		out.println();
		out.println("/** Represents the data access component that provides access to the Elasticsearch " + clazz.name + " index.");
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
		out.println("public class " + className + " extends " + baseClass + "<" + clazz.name + ", " + filterName + ">");
		out.println("{");
		out.println("\tprivate static final Logger log = LoggerFactory.getLogger(" + className + ".class);");
		out.println();
		out.println("\tpublic static final String NAME = \"" + clazz.name + "\";");
		out.println("\tpublic static final String INDEX = \"" + clazz.name.toLowerCase() + "\";");
		out.println("\tpublic static final TypeReference<" + clazz.name + "> type = new TypeReference<>() {};");
		out.println("\tpublic static final TypeReference<List<" + clazz.name + ">> types = new TypeReference<>() {};");
	}

	private void writeConstructors()
	{
		out.println();
		out.println("\tpublic " + className + "(final RestHighLevelClient es)");
		out.println("\t{");
		out.println("\t\tthis(es, false);");
		out.println("\t}");

		out.println();
		out.println("\tpublic " + className + "(final RestHighLevelClient es, final boolean test)");
		out.println("\t{");
		out.println("\t\tsuper(es, INDEX, type, types, test);");
		out.println("\t}");
	}

	private void writeMethods()
	{
		out.println();
		out.println("\tpublic List<" + clazz.name + "> getByTerm(final String term, final int pageSize) throws IOException");
		out.println("\t{");
		out.println("\t\treturn getByQuery(QueryBuilders.multiMatchQuery(term, \"" + clazz.fields.get(1).name + "\"), pageSize);");
		out.println("\t}");

		out.println();
		out.println("\t@Override");
		out.println("\tprotected Logger log() { return log; }");

		out.println();
		out.println("\t@Override");
		out.println("\tprotected String name() { return NAME; }");

		out.println();
		out.println("\t@Override");
		out.println("\tprotected QueryBuilder buildQuery_(final " + filterName + " request)");
		out.println("\t{");
		out.println("\t\tvar o = QueryBuilders.boolQuery();");
		out.println();
		for (var v : clazz.fields)
		{
			if (v.identifier)
				out.println("\t\tidsQuery(o, request." + v.name + "s);");
			else if (v.string())
				out.println("\t\tmatchQuery(o, \"" + v.name + "\", request." + v.name + ");");
			else if (null != conf.clazz(v.type))
			{
				out.println("\t\ttermQuery(o, \"" + v.name + ".id\", request." + v.name + "_id);");
				out.println("\t\tmatchQuery(o, \"" + v.name + ".name\", request." + v.name + "_name);");
			}
			else
				out.println("\t\ttermQuery(o, \"" + v.name + "\", request." + v.name + ");");
				
			if (v.nullable())
			{
				out.println("\t\texistsQuery(o, \"" + v.name + "\", request.has_" + v.name + ");");
			}

			if (v.range)
			{
				out.println("\t\trangeQuery(o, \"" + v.name + "\", request." + v.name + "_from, request." + v.name + "_to);");
			}
		}
		out.println();
		out.println("\t\treturn o;");
		out.println("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
