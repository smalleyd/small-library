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

public class JSONClass implements Serializable
{
	private static final long serialVersionUID = 1L;

	public final String name;
	public final String caption;
	public final Map<String, String> fields;

	public JSONClass(@JsonProperty("name") final String name,
		@JsonProperty("caption") final String caption,
		@JsonProperty("fields") final Map<String, String> fields)
	{
		this.name = name;
		this.caption = caption;
		this.fields = fields;
	}
}
