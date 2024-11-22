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
		out.println();
		out.println("import com.fasterxml.jackson.annotation.JsonProperty;");
		out.println();
		out.println("import " + domainPackage + ".common.ObjectUtils;");
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
			var a = annotate(v);
			out.print("\t");
			if (v.notContainer()) out.print(a);
			out.print("public final ");
			out.print(v.type(a));
			out.print(" ");
			out.print(v.name);
			out.println(";");
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
		out.println("\tpublic String toString() { return ObjectUtils.toString(this); }");
		out.println("}");
	}

	private String annotate(final JSONField v)
	{
		var o = new StringBuilder();

		if (v.notBlank) o.append("@NotBlank ");
		if (v.notEmpty) o.append("@NotEmpty ");
		if (v.notNull) o.append("@NotNull ");
		if (v.email) o.append("@Email ");
		if (null != v.min) { o.append("@Min("); o.append(v.min); o.append(") "); }
		if (null != v.max) { o.append("@Max("); o.append(v.max); o.append(") "); }
		if (null != v.decimalMin) { o.append("@DecimalMin(\""); o.append(v.decimalMin); o.append("\") "); }
		if (null != v.decimalMax) { o.append("@DecimalMax(\""); o.append(v.decimalMax); o.append("\") "); }

		var sizeMin = (null != v.sizeMin);
		var sizeMax = (null != v.sizeMax);
		if (sizeMin || sizeMax)
		{
			o.append("@Size(");
			if (sizeMin) { o.append("min="); o.append(v.sizeMin); if (sizeMax) o.append(", "); }
			if (sizeMax) { o.append("max="); o.append(v.sizeMax); }
			o.append(") ");
		}

		if (null != v.pattern) { o.append("@Pattern(regexp=\""); o.append(v.pattern); o.append("\") "); }

		return o.toString();
	}

	private String toEquals(final String name, final String type, final String term)
	{
		return String.format(Character.isLowerCase(type.charAt(0)) ? FORMAT_EQUALS_PRIMITIVE : FORMAT_EQUALS_OBJECT, name, name, term);
	}
}
