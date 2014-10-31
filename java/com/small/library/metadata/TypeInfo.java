package com.small.library.metadata;

import java.sql.*;
import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the data types of a data source.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class TypeInfo extends MetaDataCollection
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	public TypeInfo(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}

	/** Creates a new TypeInfo data record. */
	public DataRecord newRecord() { return (DataRecord) new Record(); }

	/** Creates the result set used to populated this data collection. */
	public ResultSet getResultSet() throws SQLException
	{
		return getDatabaseMetaData().getTypeInfo();
	}

/***************************************************************************************
*
*	Data record that describes the data types of a data source.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public class Record extends MetaDataRecord
	{
		public void fetch(ResultSet rs) throws SQLException {
			m_strName = rs.getString(1);
			m_nType = rs.getShort(2);
			m_nPrecision = rs.getInt(3);
			m_strLiteralPrefix = rs.getString(4);
			m_strLiteralSuffix = rs.getString(5);
			m_strCreateParams = rs.getString(6);
			m_nNullable = rs.getShort(7);
			m_bCaseSensitive = rs.getBoolean(8);
			m_nSearchable = rs.getShort(9);
			m_bUnsigned = rs.getBoolean(10);
			m_bFixedPrecision = rs.getBoolean(11);
			m_bAutoIncrement = rs.getBoolean(12);
			m_strLocalName = rs.getString(13);
			m_nScaleMin = rs.getShort(14);
			m_nScaleMax = rs.getShort(15);
			m_nRadix = rs.getInt(18);
		}

		public int bindID(PreparedStatement pStmt, int nParam)
			throws SQLException
		{
			pStmt.setString(nParam, m_strName);
			return (nParam + 1);
		}

		public int bindDesc(PreparedStatement pStmt, int nParam)
			throws SQLException
		{	return bindID(pStmt, nParam);	}

		public short getType() { return m_nType; }
		public int getPrecision() { return m_nPrecision; }
		public String getLiteralPrefix() { return m_strLiteralPrefix; }
		public String getLiteralSuffix() { return m_strLiteralSuffix; }
		public String getCreateParams() { return m_strCreateParams; }
		public short getNullable() { return m_nNullable; }
		public boolean getCaseSensitive() { return m_bCaseSensitive; }
		public short getSearchable() { return m_nSearchable; }
		public boolean getUnsigned() { return m_bUnsigned; }
		public boolean getFixedPrecision() { return m_bFixedPrecision; }
		public boolean getAutoIncrement() { return m_bAutoIncrement; }
		public String getLocalName() { return m_strLocalName; }
		public short getScaleMin() { return m_nScaleMin; }
		public short getScaleMax() { return m_nScaleMax; }
		public int getRadix() { return m_nRadix; }

		private short m_nType;
		private int m_nPrecision;
		private String m_strLiteralPrefix;
		private String m_strLiteralSuffix;
		private String m_strCreateParams;
		private short m_nNullable;
		private boolean m_bCaseSensitive;
		private short m_nSearchable;
		private boolean m_bUnsigned;
		private boolean m_bFixedPrecision;
		private boolean m_bAutoIncrement;
		private String m_strLocalName;
		private short m_nScaleMin;
		private short m_nScaleMax;
		private int m_nRadix;
	}
}
