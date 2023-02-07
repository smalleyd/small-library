package com.small.library.json;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

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

	private final Map<String, JSONClass> classes_;

	public JSONClass clazz(final String name) { return classes_.get(name); }

	public JSONConfig(@JsonProperty("packageName") final String packageName,
		@JsonProperty("imports") final List<String> imports,
		@JsonProperty("author") final String author,
		@JsonProperty("version") final String version,
		@JsonProperty("classes") final List<JSONClass> classes)
	{
		this.packageName = trimToNull(packageName);
		this.imports = imports;
		this.author = trimToNull(author);
		this.version = trimToNull(version);
		this.classes = classes;
		this.classes_ = CollectionUtils.isNotEmpty(classes) ? classes.stream().collect(toMap(v -> v.name, v -> v)) : Map.of();
	}
}
