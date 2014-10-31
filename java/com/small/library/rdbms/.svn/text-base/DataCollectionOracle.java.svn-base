package com.small.library.rdbms;

import java.sql.*;

import com.small.library.data.*;

/***************************************************************************************
*
*	Extends the Data Collection Procedure class of the "data" package for use
*	with Oracle stored procedures (packages). Generates stored procedure calls of form
*	<BR><BR>
*	PKG_[table name].[service](?, ?, ?...)
*	<BR><BR>
*	The prefix is "PKG" and the separator is ".".
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/4/2000
*
***************************************************************************************/

public abstract class DataCollectionOracle extends DataCollectionProc
{
	/******************************************************************************
	*
	* Static constants
	*
	******************************************************************************/

	/** Default SQL Server stored procedure prefix. */
	protected static final String PREFIX = "PKG";

	/** Default SQL Server stored procedure separator. */
	protected static final String SEPARATOR = ".";

	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	protected DataCollectionOracle(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}

	/******************************************************************************
	*
	* Implement abstract methods from parent
	*
	******************************************************************************/

	/** Returns the default Oracle prefix for stored procedures of "PKG".
		@return Returns a string.
	*/
	protected String getPrefix() { return PREFIX; }

	/** Returns the default Oracle separator for stored procedures of ".".
		@return Returns a string.
	*/
	protected String getSeparator() { return SEPARATOR; }
}
