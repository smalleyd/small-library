package com.small.library.json;

import static java.util.stream.Collectors.*;

import java.io.PrintStream;
import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;

/** Generates a Java value object from a JSON document.
 * 
 * @author smalleyd
 * @version 2.0
 * @since 9/19/2017
 *
 */

public class JSONValue extends JSONBase
{
	public static final String FORMAT_EQUALS_PRIMITIVE = "(%s == v.%s)%s";
	public static final String FORMAT_EQUALS_OBJECT = "Objects.equals(%s, v.%s)%s";

	public JSONValue(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);
	}

	public void run()
	{
		var size = clazz.fields.size();
		var index = new int[1];
		var identifiers = clazz.fields.stream().filter(v -> v.identifier).collect(toList());

		out.print("package "); out.print(conf.packageName); out.println(";");
		out.println();
		out.println("import java.io.Serializable;");
		out.println("import java.util.Objects;");
		out.println("import javax.validation.constraints.*;");
		if (CollectionUtils.isNotEmpty(conf.imports))
		{
			out.println();
			conf.imports.forEach(i -> {
				out.print("import "); out.print(i); out.println(";");
			});
		}
		out.println();
		out.println("import org.apache.commons.lang3.StringUtils;");
		out.println("import org.apache.commons.lang3.builder.ToStringBuilder;");
		out.println("import org.apache.commons.lang3.builder.ToStringStyle;");
		out.println();
		out.println("import com.fasterxml.jackson.annotation.JsonProperty;");
		out.println();
		out.print("/** Value object that represents the "); out.print(clazz.caption); out.println(".");
		out.println(" * ");
		if (null != conf.author)
		{
			out.print(" * @author "); out.println(conf.author);
		}
		if (null != conf.version)
		{
			out.print(" * @version "); out.println(conf.version);
		}
		out.print(" * @since "); out.println(new Date());
		out.println(" * ");
		out.println(" */");
		out.println();
		out.print("public class "); out.print(clazz.name); out.print(" implements Serializable");
		if (CollectionUtils.isNotEmpty(clazz.implements_))
			clazz.implements_.forEach(v -> { out.print(", "); out.print(v); });
		out.println("\n{");
		out.println("\tprivate static final long serialVersionUID = 1L;");
		out.println();
		clazz.fields.forEach(v -> {
			out.print("\t"); annotate(v); out.print("public final "); out.print(v.type()); out.print(" "); out.print(v.name); out.println(";");
		});
		out.println();
		out.print("\tpublic "); out.print(clazz.name); out.println("(");
		index[0] = 0;
		clazz.fields.forEach(v -> {
			out.print("\t\t@JsonProperty(\""); out.print(v.name); out.print("\") final "); out.print(v.type()); out.print(" "); out.print(v.name);
			out.println((size > ++index[0]) ? "," : ")");
		});
		out.println("\t{");
		clazz.fields.forEach(v -> {
			out.print("\t\tthis.");
			out.print(v.name);
			out.print(" = ");

			var wrap = v.string();

			if (wrap) out.print("StringUtils.trimToNull(");
			out.print(v.name); 
			if (wrap) out.print(")");
			out.println(";");
		});
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic boolean equals(final Object o)");
		out.println("\t{");
		out.print("\t\tif (!(o instanceof "); out.print(clazz.name); out.println(")) return false;");
		out.println();
		out.print("\t\tvar v = ("); out.print(clazz.name); out.println(") o;");
		index[0] = 0;
		clazz.fields.forEach(v -> {
			var term = (size > ++index[0]) ? " &&" : ";";
			out.print("\t\t");
			out.print((1 == index[0]) ? "return " : "\t");
			out.println(toEquals(v.name, v.type, term));
		});
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic int hashCode()");
		out.println("\t{");
		if (1 == identifiers.size())
			out.print("\t\treturn Objects.hashCode(" + identifiers.get(0).name);
		else
		{
			var vv = identifiers.isEmpty() ? clazz.fields : identifiers;
			out.print("\t\treturn Objects.hash(");
			out.print(vv.stream().map(v -> v.name).collect(joining(", ")));
		}
		out.println(");");
		out.println("\t}");
		out.println();
		out.println("\t@Override");
		out.println("\tpublic String toString() { return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE); }");
		out.println("}");
	}

	private void annotate(final JSONField v)
	{
		if (v.notBlank) out.print("@NotBlank ");
		if (v.notEmpty) out.print("@NotEmpty ");
		if (v.notNull) out.print("@NotNull ");
		if (v.email) out.print("@Email ");
		if (null != v.min) { out.print("@Min("); out.print(v.min); out.print(") "); }
		if (null != v.max) { out.print("@Max("); out.print(v.max); out.print(") "); }
		if (null != v.decimalMin) { out.print("@DecimalMin("); out.print(v.decimalMin); out.print(") "); }
		if (null != v.decimalMax) { out.print("@DecimalMax("); out.print(v.decimalMax); out.print(") "); }

		var sizeMin = (null != v.sizeMin);
		var sizeMax = (null != v.sizeMax);
		if (sizeMin || sizeMax)
		{
			out.print("@Size(");
			if (sizeMin) { out.print("min="); out.print(v.sizeMin); if (sizeMax) out.print(", "); }
			if (sizeMax) { out.print("max="); out.print(v.sizeMax); }
			out.print(") ");
		}

		if (null != v.pattern) { out.print("@Pattern(regexp=\""); out.print(v.pattern); out.print("\") "); }
	}

	private String toEquals(final String name, final String type, final String term)
	{
		return String.format(Character.isLowerCase(type.charAt(0)) ? FORMAT_EQUALS_PRIMITIVE : FORMAT_EQUALS_OBJECT, name, name, term);
	}
}
