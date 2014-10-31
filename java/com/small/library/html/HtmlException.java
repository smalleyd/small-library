package com.small.library.html;

/***********************************************************************************
*
*	General exception for all HTML exceptions. Contains accessor
*	<CODE>RootCause</CODE> to get the original exception thrown.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 10/29/2001
*
***********************************************************************************/

public class HtmlException extends com.small.library.LibraryException
{
	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructor - accepts a <I>String</I> error message.
		@param strMessage Error message.
	*/
	public HtmlException(String strMessage)
	{ super(strMessage); }

	/** Constructor - accepts an <I>Exception</I> object.
		@param pRootCause Originating error message.
	*/
	public HtmlException(Exception pRootCause)
	{ super(pRootCause); }
}
