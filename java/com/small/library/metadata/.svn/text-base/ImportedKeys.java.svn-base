package com.small.library.metadata;

import java.sql.*;
import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the imported foreign keys of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class ImportedKeys extends ForeignKeys
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
		@param pParent <I>Table.Record</I> object that contains the
			foreign keys.
	*/
	public ImportedKeys(ConnectionFactory pConnectionFactory,
		Tables.Record pParent)
	{
		super(pConnectionFactory, pParent);
	}

	/** Accessor method - gets a <I>ResultSet</I> object of the imported keys. */
	public ResultSet getResultSet() throws SQLException
	{
		// Make sure the driver supports the retrieval of foreign keys.
		try
		{
			/** NOTE: Using schema name of the table as well as the table
			    name to get the imported keys. DB2 does not return anything
			    otherwise.
			*/
			Tables.Record pParent = getParent();

			return getDatabaseMetaData().getImportedKeys(null, pParent.getSchema(), pParent.getName());
		}

		catch (SQLException pEx)
		{
			if (OperationNotSupportedException.isUnsupportedOperation(pEx))
				throw new OperationNotSupportedException(pEx);

			throw pEx;
		}
	}

	/** Accessor method - instantiates a new imported keys record object. */
	public DataRecord newRecord() { return new Record(); }

/***************************************************************************************
*
*	Data record that describes the imported foreign keys of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record extends ForeignKey
	{}
}
