package com.small.library.metadata;

import java.sql.*;

import javax.sql.DataSource;

import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the indexes and statistics of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Indexes extends MetaDataCollection
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pDataSource A reference to a connection factory.
		@param pParent <I>Table.Record</I> object that contains the
			foreign keys.
	*/
	public Indexes(DataSource pDataSource,
		Tables.Record pParent)
	{
		super(pDataSource);
		setParent(pParent);
	}

	/** Action method - Wraps the parent <CODE>load</CODE> method. */
	public void load() throws SQLException
	{
		load(true);
	}

	/** Action method - Wraps the parent <CODE>load</CODE> method. */
	public void load(boolean bReset) throws SQLException
	{
		m_LastRecord = null;
		super.load(bReset);
	}

	/** Indicates whether the data record should be accepted into the
	    data collection. The index result set includes more than just
	    index information. It also includes statistics information that
	    is not pertainent to this collection.
	*/
	protected boolean acceptRecord(DataRecord pRecord)
	{
		boolean bAcceptRecord = false;
		Indexes.Record pIndex = (Indexes.Record) pRecord;

		if ((null == pIndex.getColumnName()) ||
		    (DatabaseMetaData.tableIndexStatistic == pIndex.getType()))
			return false;

		// Convert to object to check for null first.
		if (!pIndex.equals((Object) m_LastRecord))
		{
			bAcceptRecord = true;
			pIndex.getKeys().add(pIndex.getColumnName(), pIndex.getSort());

			// Only the last record for the first occurence of the
			// new record. Otherwise, the keys get added to the
			// wrong pRecord.
			m_LastRecord = pIndex;
		}

		else
		{
			bAcceptRecord = false;
			m_LastRecord.getKeys().add(pIndex.getColumnName(), pIndex.getSort());
		}

		return bAcceptRecord;
	}

	/** Gets the result set used to populate this data collection. */
	protected ResultSet getResultSet() throws SQLException
	{
		/** NOTE: Using schema name of the table as well as the table
		    name to get the indexes. DB2 does not return anything
		    otherwise.
		*/
		Tables.Record pParent = getParent();

		return getDatabaseMetaData().getIndexInfo(null, pParent.getSchema(),
			pParent.getName(), false, true);
	}

	public DataRecord newRecord() { return new Record(); }
	public Tables.Record getParent() { return m_Parent; }
	private void setParent(Tables.Record pValue) { m_Parent = pValue; }

	private Tables.Record m_Parent = null;
	private Record m_LastRecord = null;

/***************************************************************************************
*
*	Data record that describes the indexes and statistics of a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record extends MetaDataRecord
	{
		public void fetch(ResultSet rs) throws SQLException
		{
			m_bIsUnique = (!rs.getBoolean(4));
			m_strName = rs.getString(6);
			m_nType = rs.getShort(7);
			m_strColumnName = rs.getString(9);
			m_strSort = rs.getString(10);
			m_nCardinality = rs.getInt(11);
			m_nPages = rs.getInt(12);
			m_strFilterCondition = rs.getString(13);
		}

		public boolean isUnique() { return m_bIsUnique; }
		public short getType() { return m_nType; }
		public int getCardinality() { return m_nCardinality; }
		public int getPages() { return m_nPages; }
		public String getFilterCondition() { return m_strFilterCondition; }

		// These accessor methods are used to build the keys.
		public String getColumnName() { return m_strColumnName; }
		public String getSort() { return m_strSort; }

		// Set by the parent.
		public boolean isPrimaryKey() { return m_bPrimaryKey; }

		public Keys getKeys() { return Data; }

		public boolean equals(Object pValue)
		{
			if ((null == pValue) || (!(pValue instanceof Record)))
				return false;

			return equals((Record) pValue);
		}

		public boolean equals(Record pRecord)
		{
			return getName().equals(pRecord.getName());
		}

		// Field data members.
		private boolean m_bIsUnique = false;
		private short m_nType = DatabaseMetaData.tableIndexOther;
		private int m_nCardinality = 0;
		private int m_nPages = 0;
		private String m_strFilterCondition = null;

		// Retrieves Index Column informaton.
		private String m_strColumnName = null;
		private String m_strSort = null;

		// Set by the parent.
		private boolean m_bPrimaryKey = false;

		private Keys Data = new Keys();
	}
}
