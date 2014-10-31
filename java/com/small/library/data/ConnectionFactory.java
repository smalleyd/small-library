package com.small.library.data;

import java.sql.Connection;
import java.sql.SQLException;

/******************************************************************************
*
*	Interface that represents classes that expose the retrieval of a
*	JDBC <I>Connection</I> object. Such classes could be pools or
*	application maintained connection factories.
*	<BR><BR>
*
*	The interface only supports one method. The <CODE>getConnection</CODE>
*	accessor is the solitary method for retrieving the connection
*	from the factory.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 7/11/2001
*
******************************************************************************/

public interface ConnectionFactory
{
	/** Accessor method - gets a JDBC <I>Connection</I> from the factory. */
	public Connection getConnection() throws SQLException;

	/** Mutator method - releases a JDBC <I>Connection</I> from usage by
	    the caller. This method follows a <CODE>getConnection</CODE> call
	    when the caller is finished using the <I>Connection</I>.
		@param pConnection The <I>Connection</I> object to be released.
	*/
	public void release(Connection pConnection) throws SQLException;
}
