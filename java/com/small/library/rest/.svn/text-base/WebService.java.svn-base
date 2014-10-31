package com.small.library.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation that describes a web service. Used primarily for documentation.
 * 
 * @author David Small
 * @version 3.5
 * @since 9/9/2010
 *
 */

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface WebService
{
	public abstract String[] urls();
}
