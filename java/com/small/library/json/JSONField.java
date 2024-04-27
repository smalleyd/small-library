package com.small.library.json;

import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.Serializable;
import java.util.Set;

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

	public static final String FORMAT_CONTAINER = "@ConvertWith(StringsArgumentConverter.class) %s<%s>";
	public static final String FORMAT_CONTAINERIZE = "%s<%s%s>";

	public static final Set<String> DATES = Set.of("Date", "ZonedDateTime");
	public static final Set<String> INTEGERS = Set.of("int", "long", "short", "Integer", "Long", "Short");
	public static final Set<String> NUMBERS = Set.of("byte", "double", "float", "int", "long", "short", "Byte", "Double", "Float", "Integer", "Long", "Short");
	public static final Set<String> PRIMITIVES = Set.of("boolean", "byte", "char", "double", "float", "int", "long", "short");

	public final String name;
	public final String type;
	public final String container;	// List, Set, ...
	public final boolean email;
	public final boolean notNull;
	public final boolean notEmpty;
	public final boolean notBlank;
	public final boolean identifier;
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
		@JsonProperty("container") final String container,
		@JsonProperty("email") final Boolean email,
		@JsonProperty("notNull") final Boolean notNull,
		@JsonProperty("notEmpty") final Boolean notEmpty,
		@JsonProperty("notBlank") final Boolean notBlank,
		@JsonProperty("identifier") final Boolean identifier,
		@JsonProperty("min") final Integer min,
		@JsonProperty("max") final Integer max,
		@JsonProperty("decimalMin") final String decimalMin,
		@JsonProperty("decimalMax") final String decimalMax,
		@JsonProperty("sizeMin") final Integer sizeMin,
		@JsonProperty("sizeMax") final Integer sizeMax,
		@JsonProperty("pattern") final String pattern,
		@JsonProperty("range") final Boolean range)
	{
		this.name = trimToNull(name);
		this.type = trimToNull(type);
		this.container = trimToNull(container);
		this.email = Boolean.TRUE.equals(email);
		this.notNull = Boolean.TRUE.equals(notNull);
		this.notEmpty = Boolean.TRUE.equals(notEmpty);
		this.notBlank = Boolean.TRUE.equals(notBlank);
		this.identifier = Boolean.TRUE.equals(identifier);
		this.min = min;
		this.max = max;
		this.decimalMin = decimalMin;
		this.decimalMax = decimalMax;
		this.sizeMin = sizeMin;
		this.sizeMax = sizeMax;
		this.pattern = trimToNull(pattern);
		this.range = Boolean.TRUE.equals(range);
	}

	public boolean bool() { return "boolean".equalsIgnoreCase(type); }
	public boolean date() { return DATES.contains(type); }

	public boolean container() { return (null != container); }
	public boolean notContainer() { return (null == container); }

	public String objectify()
	{
		return primitive() ? type.substring(0, 1).toUpperCase() + type.substring(1) : type;
	}

	public boolean integer() { return INTEGERS.contains(type); }
	public boolean nullable() { return !primitive() && !(notNull || notEmpty || notBlank); }
	public boolean number() { return NUMBERS.contains(type); }
	public boolean primitive() { return PRIMITIVES.contains(type); }
	public boolean string() { return "String".equals(type); }

	public String type() { return type(""); }
	public String type(final String annotations)
	{
		return notContainer() ? type : String.format(FORMAT_CONTAINERIZE, container, annotations, type);
	}
	public String typeForJunit()
	{
		if (container())
			return String.format(FORMAT_CONTAINER, container, type);

		return (date()) ? "Instant" : type;
	}

	@Override
	public String toString() { return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE); }
}
