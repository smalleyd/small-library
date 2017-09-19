package com.small.library.json;

import java.io.Serializable;
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
	public final String className;
	public final String author;
	public final String version;
	public final Map<String, String> properties;

	public JSONConfig(@JsonProperty("packageName") final String packageName,
		@JsonProperty("className") final String className,
		@JsonProperty("author") final String author,
		@JsonProperty("version") final String version,
		@JsonProperty("properties") final Map<String, String> properties)
	{
		this.packageName = packageName;
		this.className = className;
		this.author = author;
		this.version = version;
		this.properties = properties;
	}
}
