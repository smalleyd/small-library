package com.small.library.rest;

import java.lang.annotation.*;

/** A simple annotation that marks a field for Long Text treatment by XML serializers. This entails
 * creating a separate element instead of an attribute.
 *
 * @author David Small
 * @version 1.1
 * @since 1/23/2009
 *
 */

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface LongText {}
