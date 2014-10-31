package com.small.library.taglib;

/**********************************************************************************
*
*	Tag class that represents the Password form element. Subclasses the TextBoxTag
*	with an overridden <CODE>getType</CODE> method.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 1/1/2003
*	@see TextBoxTag
*
***********************************************************************************/

public class PasswordTag extends TextBoxTag
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** Constant - Input type property. */
	public static final String TYPE = "password";

	/** InputTag method - gets the type property. */
	public String getType() { return TYPE; }
}

