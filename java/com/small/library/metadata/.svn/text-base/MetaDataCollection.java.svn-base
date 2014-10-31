package com.small.library.metadata;

import java.sql.*;

import com.small.library.data.*;

/***************************************************************************************
*
*	Base class of data collection classes for meta data.
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 4/15/2002
*
***************************************************************************************/

public abstract class MetaDataCollection extends DataCollection
{
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	protected MetaDataCollection(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}

	/*******************************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************************/

	/** Accessor method - Retrieves the JDBC <I>DatabaseMetaData</I>. */
	public DatabaseMetaData getDatabaseMetaData() throws SQLException
	{ return getConnection().getMetaData(); }

	/** Accesspr method - Retrieves a reference to the Meta Data Record. */
	public MetaDataRecord getMetaDataRecord(int nIndex)
	{ return (MetaDataRecord) item(nIndex); }
}
