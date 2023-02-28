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

public class JSONClass implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String name;
	public final String plural;
	public final String path;
	public final boolean cacheable;
	public final String caption;
	@JsonProperty("implements") public final List<String> implements_;
	public final boolean generateFilter;
	public final boolean generateElastic;
	public final boolean generateResource;
	public final List<JSONField> fields;

	public JSONClass(@JsonProperty("name") final String name,
		@JsonProperty("plural") final String plural,
		@JsonProperty("path") final String path,
		@JsonProperty("cacheable") final Boolean cacheable,
		@JsonProperty("caption") final String caption,
		@JsonProperty("implements") final List<String> implements_,
		@JsonProperty("generateFilter") final Boolean generateFilter,
		@JsonProperty("generateElastic") final Boolean generateElastic,
		@JsonProperty("generateResource") final Boolean generateResource,
		@JsonProperty("fields") final List<JSONField> fields)
	{
		this.name = name;
		this.plural = plural;
		this.path = path;
		this.cacheable = Boolean.TRUE.equals(cacheable);
		this.caption = caption;
		this.implements_ = implements_;
		this.generateFilter = Boolean.TRUE.equals(generateFilter);
		this.generateElastic = Boolean.TRUE.equals(generateElastic);
		this.generateResource = Boolean.TRUE.equals(generateResource);
		this.fields = fields;
	}
}
