package com.small.library.metadata;

import java.sql.*;

import javax.sql.DataSource;

import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the columns (fields) of a table. The data
*	collection also accepts the schema name in case the being requested exists
*	in more than one schema.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Columns extends MetaDataCollection
{
	/** Constructor - minimal.
		@param pDataSource A reference to a connection factory.
		@param pParent Table record object.
	*/
	public Columns(DataSource pDataSource,
		Tables.Record pParent)
	{ super(pDataSource); m_Parent = pParent; }

	/** Abstract implementation - gets the ResultSet to be used by the <CODE>load</CODE> method. */
	protected ResultSet getResultSet() throws SQLException
	{
		/** NOTE: Using schema name of the table as well as the table
		    name to get the columns. DB2 does not return anything
		    otherwise.
		*/
		Tables.Record pParent = getParent();

		return getDatabaseMetaData().getColumns(null,
			pParent.getSchema(), pParent.getName(), null);
	}

	/** Abstract implementation - gets the specific data record object. */
	public DataRecord newRecord() { return (DataRecord) new Record(); }

	/** Accessor method - gets the parent table record object. */
	public Tables.Record getParent() { return m_Parent; }

	/** Member variable - reference to the parent table. */
	private Tables.Record m_Parent;

/***************************************************************************************
*
*	Data record that describes the columns (fields) of a table.
*	@author David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record extends MetaDataRecord
	{
		public void fetch(ResultSet rs) throws SQLException
		{
			m_strName = rs.getString(4);
			m_nDataType = rs.getInt(5);
			m_strTypeName = rs.getString(6);
			m_nSize = rs.getInt(7);
			m_nDecimalDigits = rs.getInt(9);
			m_nRadix = rs.getInt(10);
			m_nNullable = rs.getInt(11);
			m_strRemarks = rs.getString(12);
			isAutoIncrement = "YES".equals(rs.getString(23));
		}

		public int bindID(PreparedStatement pStmt, int nParam) throws SQLException
		{
			pStmt.setString(nParam, getName());
			return (++nParam);
		}

		public int bindDesc(PreparedStatement pStmt, int nParam) throws SQLException
		{
			return bindID(pStmt, nParam);
		}

		public String getDesc() { return getName(); }
		public int getDataType() { return m_nDataType; }
		public String getTypeName() { return m_strTypeName; }
		public int getSize() { return m_nSize; }
		public int getDecimalDigits() { return m_nDecimalDigits; }
		public int getRadix() { return m_nRadix; }
		public int getNullable() { return m_nNullable; }
		public boolean isNullable() { return ((DatabaseMetaData.columnNullable == getNullable()) ? true : false); }
		public boolean isAutoIncrement() { return isAutoIncrement; }

		/** Member variable - represents the data type ID value. */
		private int m_nDataType = 0;

		/** Member variable - represents the product specific type name. */
		private String m_strTypeName = null;

		/** Member variable - represents the size in bytes of the field. */
		private int m_nSize = 0;

		/** Member variable - represents the number of decimal places for
		    numeric data types.
		*/
		private int m_nDecimalDigits = 0;

		/** Member variable - represents the radix of numeric data types. */
		private int m_nRadix = 0;

		/** Member variable - indicates whether the column is nullable. */
		private int m_nNullable = 0;

		/** Member variable - is the field an auto-incrementing field. */
		private boolean isAutoIncrement = false;
	}
}
