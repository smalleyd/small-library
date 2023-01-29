package com.small.library.json;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Value object that represents the fields of a class definition.
 * 
 * @author smalleyd
 * @version 3.0
 * @since 1/25/2023
 *
 */

public class JSONField implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final Set<String> DATES = Set.of("Date", "ZonedDateTime");
	public static final Set<String> PRIMITIVES = Set.of("boolean", "byte", "char", "double", "float", "int", "long", "short");

	public final String name;
	public final String type;
	public final boolean email;
	public final boolean notNull;
	public final boolean notEmpty;
	public final boolean notBlank;
	public final Integer min;
	public final Integer max;
	public final String decimalMin;
	public final String decimalMax;
	public final Integer sizeMin;
	public final Integer sizeMax;
	public final String pattern;
	public final boolean range;

	public JSONField(@JsonProperty("name") final String name,
		@JsonProperty("type") final String type,
		@JsonProperty("email") final Boolean email,
		@JsonProperty("notNull") final Boolean notNull,
		@JsonProperty("notEmpty") final Boolean notEmpty,
		@JsonProperty("notBlank") final Boolean notBlank,
		@JsonProperty("min") final Integer min,
		@JsonProperty("max") final Integer max,
		@JsonProperty("decimalMin") final String decimalMin,
		@JsonProperty("decimalMax") final String decimalMax,
		@JsonProperty("sizeMin") final Integer sizeMin,
		@JsonProperty("sizeMax") final Integer sizeMax,
		@JsonProperty("pattern") final String pattern,
		@JsonProperty("range") final Boolean range)
	{
		this.name = StringUtils.trimToNull(name);
		this.type = StringUtils.trimToNull(type);
		this.email = Boolean.TRUE.equals(email);
		this.notNull = Boolean.TRUE.equals(notNull);
		this.notEmpty = Boolean.TRUE.equals(notEmpty);
		this.notBlank = Boolean.TRUE.equals(notBlank);
		this.min = min;
		this.max = max;
		this.decimalMin = decimalMin;
		this.decimalMax = decimalMax;
		this.sizeMin = sizeMin;
		this.sizeMax = sizeMax;
		this.pattern = StringUtils.trimToNull(pattern);
		this.range = Boolean.TRUE.equals(range);
	}

	public boolean date() { return DATES.contains(type); }

	public String objectify()
	{
		return primitive() ? type.substring(0, 1).toUpperCase() + type.substring(1) : type;
	}

	public boolean primitive() { return PRIMITIVES.contains(type); }
	public boolean nullable() { return !primitive(); }
	public boolean string() { return "String".equals(type); }

	@Override
	public String toString() { return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE); }
}
