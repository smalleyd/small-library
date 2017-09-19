package com.small.library.metadata;

import java.sql.*;

import javax.sql.DataSource;

import com.small.library.data.*;

/***************************************************************************************
*
*	Parent data collection class that supports retrieving a list of exported or
*	imported foreign keys from a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public abstract class ForeignKeys extends MetaDataCollection
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pDataSource A reference to a connection factory.
		@param pParent <I>Table.Record</I> object that contains the
			foreign keys.
	*/
	public ForeignKeys(DataSource pDataSource,
		Tables.Record pParent)
	{
		super(pDataSource);
		setParent(pParent);
	}

	public void load()
		throws SQLException
	{ load(true); }

	public void load(boolean bReset) throws SQLException
	{
		m_LastRecord = null;
		super.load(bReset);
	}

	protected boolean acceptRecord(DataRecord pRecord)
	{
		boolean bAcceptRecord = false;
		ForeignKey pForeignKey = (ForeignKey) pRecord;

		// Convert to object to check for null first.
		if (!pForeignKey.equals(m_LastRecord))
		{
			bAcceptRecord = true;
			pForeignKey.getColumns_PK().add(pForeignKey.getColumn_PK());
			pForeignKey.getColumns_FK().add(pForeignKey.getColumn_FK());

			// Only the last record for the first occurence of the
			// new record. Otherwise, the keys get added to the
			// wrong pRecord.
			m_LastRecord = pForeignKey;
		}

		else
		{
			bAcceptRecord = false;
			m_LastRecord.getColumns_PK().add(pForeignKey.getColumn_PK());
			m_LastRecord.getColumns_FK().add(pForeignKey.getColumn_FK());
		}

		return bAcceptRecord;
	}

	public Tables.Record getParent() { return m_Parent; }
	private void setParent(Tables.Record pValue) { m_Parent = pValue; }

	private Tables.Record m_Parent = null;
	private ForeignKey m_LastRecord = null;
}
