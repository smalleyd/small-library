package com.small.library.rest;

import java.lang.annotation.*;

/** Explicit declaration of the input class for a FetcherServlet.
 * 
 * @author David Small
 * @version 1
 * @since 11/13/2010
 *
 */

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface InputClass
{
	/** Required. */
	public abstract Class<? extends Object> value();
}
