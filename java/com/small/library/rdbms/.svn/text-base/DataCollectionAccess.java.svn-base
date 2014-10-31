package com.small.library.rdbms;

import java.sql.*;

import com.small.library.data.*;

/***************************************************************************************
*
*	Extends the Data Collection Procedure class of the "data" package for use
*	with MS Access stored procedures. Generates stored procedure calls of form
*	<BR><BR>
*	usp_[table name]_[service](?, ?, ?...)
*	<BR><BR>
*	The prefix is "usp" and the separator is "_".
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/4/2000
*
***************************************************************************************/

public abstract class DataCollectionAccess extends DataCollectionProc
{
	/******************************************************************************
	*
	* Static constants
	*
	******************************************************************************/

	/** Default SQL Server stored procedure prefix. */
	protected static final String PREFIX = "usp";

	/** Default SQL Server stored procedure separator. */
	protected static final String SEPARATOR = "_";

	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	protected DataCollectionAccess(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}

	/******************************************************************************
	*
	* Implement abstract methods from parent
	*
	******************************************************************************/

	/** Returns the default Access prefix for stored procedures of "usp".
		@return Returns a string.
	*/
	protected String getPrefix() { return PREFIX; }

	/** Returns the default Access separator for stored procedures of "_".
		@return Returns a string.
	*/
	protected String getSeparator() { return SEPARATOR; }
}
