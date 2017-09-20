package com.small.library.json;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Value object that represents the input parameters for the generator.
 * 
 * @author smalleyd
 * @version 2.0
 * @since 9/19/2017
 *
 */

public class JSONConfig implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String packageName;
	public final List<String> imports;
	public final String className;
	public final String caption;
	public final String author;
	public final String version;
	public final Map<String, String> fields;

	public JSONConfig(@JsonProperty("packageName") final String packageName,
		@JsonProperty("imports") final List<String> imports,
		@JsonProperty("className") final String className,
		@JsonProperty("caption") final String caption,
		@JsonProperty("author") final String author,
		@JsonProperty("version") final String version,
		@JsonProperty("fields") final Map<String, String> fields)
	{
		this.packageName = packageName;
		this.imports = imports;
		this.className = className;
		this.caption = caption;
		this.author = author;
		this.version = version;
		this.fields = fields;
	}
}
