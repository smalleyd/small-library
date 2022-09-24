package com.small.library.json;

import java.io.Serializable;
import java.util.List;

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
	public final String author;
	public final String version;
	public final List<JSONClass> classes;

	public JSONConfig(@JsonProperty("packageName") final String packageName,
		@JsonProperty("imports") final List<String> imports,
		@JsonProperty("author") final String author,
		@JsonProperty("version") final String version,
		@JsonProperty("classes") final List<JSONClass> classes)
	{
		this.packageName = packageName;
		this.imports = imports;
		this.author = author;
		this.version = version;
		this.classes = classes;
	}
}
