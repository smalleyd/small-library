package com.small.library.atg.repository;

/**********************************************************************************
*
*	Wrapper exception thrown from <CODE>MutableRepositoryBean.createRepositoryBeans</CODE>
*	for possible reflection exceptions.
*
*	@author AMC\David Small
*	@version 1.0.0.0
*	@date 6/25/2002
*
***********************************************************************************/

public class RepositoryBeanCreationException extends Exception
{
	/** Constructor - constructs an object with a root exception.
		@param rootException Exception wrapped by this instance.
	*/
	public RepositoryBeanCreationException(Exception rootCause)
	{
		super(rootCause.getMessage());

		m_RootCause = rootCause;
	}

	/** Accessor - gets the root cause exception. */
	public Exception getRootCause() { return m_RootCause; }

	/** Member variable - represents the root cause exception. */
	private Exception m_RootCause = null;
}
