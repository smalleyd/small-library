package com.small.library.json;

import static java.util.stream.Collectors.joining;

import java.io.PrintStream;
import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;

/** Generates a Java 'record' object from a JSON document.
 *
 * @author smalleyd
 * @version 4.0.1
 * @since 5/20/2026
 *
 */

public class JSONRecord extends JSONBase
{
	public static final String FORMAT_EQUALS_PRIMITIVE = "(%s == v.%s)%s";
	public static final String FORMAT_EQUALS_OBJECT = "Objects.equals(%s, v.%s)%s";

	public JSONRecord(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		super(conf, clazz, out);
	}

	public void run()
	{
		var size = clazz.fields.size();
		var index = new int[1];

		out.print("package "); out.print(conf.packageName); out.println(";");
		out.println();
		out.println("import javax.validation.constraints.*;");
		if (CollectionUtils.isNotEmpty(conf.imports))
		{
			out.println();
			conf.imports.forEach(i -> {
				out.print("import "); out.print(i); out.println(";");
			});
		}
		out.println();
		out.println("import com.fasterxml.jackson.annotation.JsonProperty;");
		out.println();
		out.print("/** Value object that represents the "); out.print(clazz.caption); out.println(".");
		out.println(" *");
		if (null != conf.author)
		{
			out.print(" * @author "); out.println(conf.author);
		}
		if (null != conf.version)
		{
			out.print(" * @version "); out.println(conf.version);
		}
		out.print(" * @since "); out.println(new Date());
		out.println(" *");
		out.println(" */");
		out.println();
		out.print("public class "); out.print(clazz.name);
		if (CollectionUtils.isNotEmpty(clazz.implements_))
			out.print(clazz.implements_.stream().collect(joining(", ", " implements ", "")));
		out.println(" (");
		index[0] = 0;
		clazz.fields.forEach(v -> {
			var a = annotate(v);
			out.print("\t@JsonProperty(\""); out.print(v.name); out.print("\") "); 
			if (v.notContainer()) out.print(a);
			out.print(v.type(a));
			out.print(" ");
			out.print(v.name);
			out.println((size > ++index[0]) ? "," : ") {}");
		});
	}

	private String annotate(final JSONField v)
	{
		var o = new StringBuilder();

		if (v.notBlank) o.append("@NotBlank ");
		if (v.notEmpty) o.append("@NotEmpty ");
		if (v.notNull) o.append("@NotNull ");
		if (v.email) o.append("@Email ");
		if (null != v.min) { o.append("@Min("); o.append(v.min); o.append("L) "); }
		if (null != v.max) { o.append("@Max("); o.append(v.max); o.append("L) "); }
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
}
