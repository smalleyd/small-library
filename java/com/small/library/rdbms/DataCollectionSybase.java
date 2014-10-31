package com.small.library.rdbms;

import java.sql.*;

import com.small.library.data.*;

/***************************************************************************************
*
*	Extends the Data Collection SQL Server class without change.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/4/2000
*
***************************************************************************************/

public abstract class DataCollectionSybase extends DataCollectionSQLServer
{
	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	protected DataCollectionSybase(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}
}
