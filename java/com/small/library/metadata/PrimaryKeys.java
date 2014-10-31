package com.small.library.metadata;

import java.sql.*;
import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the primary key of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class PrimaryKeys extends MetaDataCollection
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
		@param pParent <I>Table.Record</I> object that contains the
			foreign keys.
	*/
	public PrimaryKeys(ConnectionFactory pConnectionFactory,
		Tables.Record pParent)
	{
		super(pConnectionFactory);
		setParent(pParent);
	}

	public DataRecord newRecord() { return (DataRecord) new Record(); }
	public ResultSet getResultSet() throws SQLException
	{
		// Make sure the driver supports the retrieval of primary keys.
		try
		{
			/** NOTE: Using schema name of the table as well as the table
			    name to get the primary keys. DB2 does not return anything
			    otherwise.
			*/
			Tables.Record pParent = getParent();

			return getDatabaseMetaData().getPrimaryKeys(null, pParent.getSchema(), pParent.getName());
		}

		catch (SQLException ex)
		{
			if (ex.getSQLState().substring(0, 2).equals("IM"))
				return null;

			throw ex;
		}
	}

	public Tables.Record getParent() { return m_Parent; }
	private void setParent(Tables.Record pValue) { m_Parent = pValue; }

	public String getName() { return m_strName; }

	// Private member variables.
	private Tables.Record m_Parent = null;
	private String m_strName = null;

/***************************************************************************************
*
*	Data record that describes the primary key of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public class Record extends MetaDataRecord
	{
		public void fetch(ResultSet rs) throws SQLException
		{
			m_strKey = rs.getString(4);
			this.m_strName = rs.getString(6);
		}

		public String getName() { return m_strKey; }

		// Private member variables.
		private String m_strKey = null;
	}
}
