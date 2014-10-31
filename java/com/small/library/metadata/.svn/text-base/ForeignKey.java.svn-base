package com.small.library.metadata;

import java.sql.*;
import com.small.library.data.*;

/***************************************************************************************
*
*	Parent data record class that supports caching of the exported or imported
*	foreign key information for a table.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public abstract class ForeignKey extends MetaDataRecord
{
	public void fetch(ResultSet rs) throws SQLException
	{
		m_strTable_PK = rs.getString(3);
		m_strColumn_PK = rs.getString(4);
		m_strTable_FK = rs.getString(7);
		m_strColumn_FK = rs.getString(8);
		m_nUpdateRule = rs.getShort(10);
		m_nDeleteRule = rs.getShort(11);
		m_strName = rs.getString(12);
		m_strName_PK = rs.getString(13);
		m_nDeferrability = rs.getShort(14);
	}

	public String getName_PK() { return m_strName_PK; }
	public String getTable_PK() { return m_strTable_PK; }
	public String getTable_FK() { return m_strTable_FK; }
	public short getUpdateRule() { return m_nUpdateRule; }
	public short getDeleteRule() { return m_nDeleteRule; }
	public short getDeferrability() { return m_nDeferrability; }

	// These accessor methods are used to build the keys.
	public String getColumn_PK() { return m_strColumn_PK; }
	public String getColumn_FK() { return m_strColumn_FK; }

	// Key column collections.
	public Keys getColumns_PK() { return m_Columns_PK; }
	public Keys getColumns_FK() { return m_Columns_FK; }

	public boolean equals(Object pValue)
	{
		if ((null == pValue) || (!(pValue instanceof ForeignKey)))
			return false;

		return getName().equals(((ForeignKey) pValue).getName());
	}

	// Field data members.
	private String m_strName_PK = null;
	private String m_strTable_PK = null;
	private String m_strColumn_PK = null;
	private String m_strTable_FK = null;
	private String m_strColumn_FK = null;
	private short m_nUpdateRule = DatabaseMetaData.importedKeyNoAction;
	private short m_nDeleteRule = DatabaseMetaData.importedKeyNoAction;
	private short m_nDeferrability = DatabaseMetaData.importedKeyInitiallyDeferred;

	private Keys m_Columns_PK = new Keys();
	private Keys m_Columns_FK = new Keys();
}
