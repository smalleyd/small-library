package com.small.library.generator;

/*************************************************************************************
*
*	Class that represents exceptions specific to generation of code. The
*	accessor <CODE>getRootCause</CODE> can contain a more specific <I>Exception</I>
*	object if one exists.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/24/2000
*
*************************************************************************************/

public class GeneratorException extends Exception
{
	/*****************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor
		@param strMessage <I>String</I> message.
		@param pRootCause Root <I>Exception</I> object.
	*/
	public GeneratorException(String strMessage, Exception pRootCause)
	{ super(strMessage); setRootCause(pRootCause); }

	/** Constructor
		@param strMessage <I>String</I> message.
	*/
	public GeneratorException(String strMessage)
	{ super(strMessage); }

	/** Constructor
		@param pRootCause Root <I>Exception</I> object.
	*/
	public GeneratorException(Exception pRootCause)
	{ super(pRootCause.getMessage()); setRootCause(pRootCause); }

	/*****************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - Get the root cause exception. */
	public Exception getRootCause() { return m_RootCause; }

	/** Accessor method - Gets the root cause if not <CODE>null</CODE> or
	    the current instance.
	*/
	public Exception toException()
	{
		if (null == m_RootCause)
			return this;

		return m_RootCause;
	}

	/*****************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - Set the root cause exception. */
	protected void setRootCause(Exception pNewValue) { m_RootCause = pNewValue; }

	/*****************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	private Exception m_RootCause = null;
}
