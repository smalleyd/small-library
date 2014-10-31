package com.small.library.metadata;

import java.sql.*;
import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the parameters of a procedure.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Parameters extends MetaDataCollection
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
		@param pParent The parent procedure that contains the parameters.
	*/
	public Parameters(ConnectionFactory pConnectionFactory,
		Procedures.Record pParent)
	{
		super(pConnectionFactory);
		setParent(pParent);
	}

	/** Creates a new Parameter record. */
	public DataRecord newRecord() { return (DataRecord) new Record(); }

	/** Creates the result set used to populated this data collection. */
	public ResultSet getResultSet() throws SQLException
	{
		/** NOTE: Using schema name of the procedure as well as the table
		    name to get the parameters. DB2 does not return anything
		    otherwise.
		*/
		Procedures.Record pParent = getParent();

		return getDatabaseMetaData().getProcedureColumns(null, pParent.getSchema(), pParent.getName(), null);
	}

	/** Accessor method - gets the parent procedure. */
	public Procedures.Record getParent() { return m_Parent; }

	/** Mutator method - sets the parent procedure. */
	public void setParent(Procedures.Record pValue) { m_Parent = pValue; }

	/** Member variable - reference to the parent procedure. */
	private Procedures.Record m_Parent = null;

/***************************************************************************************
*
*	Data record that describes the parameters of a procedure.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record extends MetaDataRecord
	{
		public void fetch(ResultSet rs) throws SQLException
		{
			m_strName = rs.getString(4);
			m_nType = rs.getShort(5);
			m_nDataType = rs.getShort(6);
			m_strTypeName = rs.getString(7);
			m_nPrecision = rs.getInt(8);
			m_nLength = rs.getInt(9);
			m_nScale = rs.getShort(10);
			m_nRadix = rs.getShort(11);
			m_nNullable = rs.getShort(12);
			m_strRemarks = rs.getString(13);
		}

		public int bindID(PreparedStatement pStmt, int nParam) throws SQLException
		{
			pStmt.setString(nParam, getName());
			return (nParam + 1);
		}

		public int bindDesc(PreparedStatement pStmt, int nParam) throws SQLException
		{
			return bindID(pStmt, nParam);
		}

		public short getType() { return m_nType; }
		public short getDataType() { return m_nDataType; }
		public String getTypeName() { return m_strTypeName; }
		public int getPrecision() { return m_nPrecision; }
		public int getLength() { return m_nLength; }
		public short getScale() { return m_nScale; }
		public short getRadix() { return m_nRadix; }
		public short getNullable() { return m_nNullable; }
		public boolean isNullable()
		{
			return ((m_nNullable == DatabaseMetaData.procedureNullable) ?
				 true : false);
		}

		// Private member variables.
		private short m_nType = DatabaseMetaData.procedureColumnUnknown;
		private short m_nDataType = 0;
		private String m_strTypeName = null;
		private int m_nPrecision = 0;
		private int m_nLength = 0;
		private short m_nScale = 0;
		private short m_nRadix = 0;
		private short m_nNullable = DatabaseMetaData.procedureNullableUnknown;
	}
}
