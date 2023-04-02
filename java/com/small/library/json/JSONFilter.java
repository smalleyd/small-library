package com.small.library.json;

import java.io.*;
import java.util.Date;

/** Generates an entity search-request/filter object from a JSON document.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/25/2023
 *
 */

public class JSONFilter extends JSONBase
{
	public static final String CLASS_NAME_SUFFIX = "SearchRequest";

	public static String getClassName(final String value)
	{
		return value + CLASS_NAME_SUFFIX;
	}

	public JSONFilter(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);
	}

	@Override
	public void run()
	{
		writeHeader();
		writeClassDeclaration();

		writeMembers();
		writeConstructors();
		writeEmpty();
		writeToString();

		writeFooter();
	}

	private void writeHeader()
	{
		out.print("package "); out.print(appPackage); out.print(".filter"); out.println(";");

		out.println();
		out.println("import java.math.BigDecimal;");
		out.println("import java.time.ZonedDateTime;");
		out.println("import java.util.Date;");
		out.println("import java.util.List;");
		out.println("import javax.validation.constraints.NotBlank;");
		out.println();
		out.println("import org.apache.commons.collections4.CollectionUtils;");
		out.println("import org.apache.commons.lang3.StringUtils;");
		out.println("import org.apache.commons.lang3.builder.ToStringBuilder;");
		out.println("import org.apache.commons.lang3.builder.ToStringStyle;");
		out.println();
		out.println("import com.fasterxml.jackson.annotation.JsonProperty;");
		out.println();
		out.println("import " + domainPackage + ".common.model.Filter;");
		out.println();
		out.println("/** Value object class that represents the search criteria for " + clazz.name + " query.");
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
		out.println("public class " + getClassName(clazz.name) + " extends Filter");
		out.println("{");
		// out.println("/** Constant - serial version UID. */", 1);
		out.println("\tprivate static final long serialVersionUID = 1L;");
	}

	/** Output method - writes the member variables. */
	private void writeMembers()
	{
		out.println();

		// Write member variables.
		for (var i : clazz.fields)
		{
			if (i.identifier)
			{
				var a = i.string() ? "@NotBlank " : "";
				out.println("\tpublic final List<" + a + i.objectify() + "> " + i.name + "s;");
			}
			else if (null != conf.clazz(i.type))
			{
				out.println("\tpublic final String " + i.name + "_id;");
				out.println("\tpublic final String " + i.name + "_name;");
			}
			else
				out.println("\tpublic final " + i.objectify() + " " + i.name + ";");

			if (i.nullable())
				out.println("\tpublic final Boolean has_" + i.name + ";");

			// Integer and date fields filter by range so need an additional "lower boundary" property and an "upper boundary" property.
			if (i.range)
			{
				out.println("\tpublic final " + i.type + " " + i.name + "_from;");
				out.println("\tpublic final " + i.type + " " + i.name + "_to;");
			}
		}
	}

	private void writeConstructors()
	{
		out.println();
		out.println("\tpublic " + getClassName(clazz.name) + "(");

		for (var v : clazz.fields)
		{
			if (v.identifier)
			{
				out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("s\") final List<"); out.print(v.objectify()); out.print("> "); out.print(v.name); out.print("s");
			}
			else if (null != conf.clazz(v.type))
			{
				out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("_id\") final String "); out.print(v.name); out.print("_id");
				out.println(",");
				out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("_name\") final String "); out.print(v.name); out.print("_name");
			}
			else
			{
				out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("\") final "); out.print(v.objectify()); out.print(" "); out.print(v.name);
			}

			if (v.nullable())
			{
				out.println(",");
				out.print("\t\t@JsonProperty(\"has_"); out.print(v.name); out.print("\") final Boolean has_"); out.print(v.name);
			}

			if (v.range)
			{
				out.println(",");
				out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("_from\") final "); out.print(v.type); out.print(" "); out.print(v.name); out.print("_from");
				out.println(",");
				out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("_to\") final "); out.print(v.type); out.print(" "); out.print(v.name); out.print("_to");
			}

			out.println(",");
		}

		out.println("\t\t@JsonProperty(\"sort\") final String sort,");
		out.println("\t\t@JsonProperty(\"asc\") final Boolean asc,");
		out.println("\t\t@JsonProperty(\"page\") final Integer page,");
		out.println("\t\t@JsonProperty(\"pageSize\") final Integer pageSize)");

		// Write body.
		out.println("\t{");
		out.println("\t\tsuper(sort, asc, page, pageSize);");
		out.println();
		for (var v : clazz.fields)
		{
			var wrap = v.string() ? "StringUtils.trimToNull(" : "";
			var wrap_ = v.string() ? ")" : "";

			if (v.identifier)
				out.println("\t\tthis." + v.name + "s = " + v.name + "s;");
			else if (null != conf.clazz(v.type))
			{
				out.println("\t\tthis." + v.name + "_id = StringUtils.trimToNull(" + v.name + "_id);");
				out.println("\t\tthis." + v.name + "_name = StringUtils.trimToNull(" + v.name + "_name);");
			}
			else
				out.println("\t\tthis." + v.name + " = " + wrap + v.name + wrap_ + ";");

			if (v.nullable())
				out.println("\t\tthis.has_" + v.name + " = has_" + v.name + ";");

			if (v.range)
			{
				out.println("\t\tthis." + v.name + "_from = " + v.name + "_from" + ";");
				out.println("\t\tthis." + v.name + "_to = " + v.name + "_to" + ";");
			}
		}
		out.println("\t}");
	}

	private void writeEmpty()
	{
		out.println();
		out.println("\t@Override");
		out.println("\tpublic boolean empty()");
		out.println("\t{");
		out.println("\t\treturn");

		var i = 0;
		var size = clazz.fields.size();
		for (var v : clazz.fields)
		{
			if (v.identifier)
				out.print("\t\t\tCollectionUtils.isEmpty(" + v.name + "s)");
			else if (null != conf.clazz(v.type))
			{
				out.print("\t\t\t(null == " + v.name + "_id)");
				out.println(" &&");
				out.print("\t\t\t(null == " + v.name + "_name)");
			}
			else
				out.print("\t\t\t(null == " + v.name + ")");

			if (v.nullable())
			{
				out.println(" &&");
				out.print("\t\t\t(null == has_" + v.name + ")");
			}

			if (v.range)
			{
				out.println(" &&");
				out.println("\t\t\t(null == " + v.name + "_from) &&");
				out.print("\t\t\t(null == " + v.name + "_to)");
			}

			out.println((++i < size) ? " &&" : ";");
		}
		out.println("\t}");
	}

	private void writeToString()
	{
		// Start section.
		out.println();
		out.println("\t@Override");
		out.println("\tpublic String toString() { return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE); }");
	}

	/** Output method - writes the class footer. */
	private void writeFooter()
	{
		out.println("}");
	}
}
