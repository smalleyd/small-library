package com.small.library.metadata;

import java.sql.*;

import javax.sql.DataSource;

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
		@param pDataSource A reference to a connection factory.
		@param pParent <I>Table.Record</I> object that contains the
			foreign keys.
	*/
	public ImportedKeys(DataSource pDataSource,
		Tables.Record pParent)
	{
		super(pDataSource, pParent);
	}

	/** Accessor method - gets a <I>ResultSet</I> object of the imported keys. */
	public ResultSet getResultSet() throws SQLException
	{
		/** NOTE: Using schema name of the table as well as the table
		    name to get the imported keys. DB2 does not return anything
		    otherwise.
		*/
		Tables.Record pParent = getParent();

		return getDatabaseMetaData().getImportedKeys(null, pParent.getSchema(), pParent.getName());
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
