package com.small.library.rdbms;

import java.sql.*;

import com.small.library.data.*;

/***************************************************************************************
*
*	Extends the Data Collection Procedure class of the "data" package for use
*	with SQL Server stored procedures. Generates stored procedure calls of form
*	<BR><BR>
*	usp_[table name]_[service](?, ?, ?...)
*	<BR><BR>
*	The prefix is "usp" and the separator is "_".
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/4/2000
*
***************************************************************************************/

public abstract class DataCollectionSQLServer extends DataCollectionProc
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
	protected DataCollectionSQLServer(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}

	/******************************************************************************
	*
	* Implement abstract methods from parent
	*
	******************************************************************************/

	/** Returns the default SQL Server prefix for stored procedures of "usp".
		@return Returns a string.
	*/
	protected String getPrefix() { return PREFIX; }

	/** Returns the default SQL Server separator for stored procedures of "_".
		@return Returns a string.
	*/
	protected String getSeparator() { return SEPARATOR; }
}
