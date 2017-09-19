package com.small.library.metadata;

import java.sql.*;

import javax.sql.DataSource;

import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the stored procedures of a data source.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Procedures extends MetaDataCollection
{
	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pDataSource A reference to a connection factory.
	*/
	public Procedures(DataSource pDataSource)
	{
		super(pDataSource);
	}

	/** Creates a new Procedure data record. */
	public DataRecord newRecord() { return (DataRecord) new Record(getDataSource()); }

	/** Create a result set used to populate this data collection. */
	public ResultSet getResultSet() throws SQLException
	{
		return getDatabaseMetaData().getProcedures(null, null, null);
	}

/***************************************************************************************
*
*	Data record that describes the stored procedures of a data source.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record extends MetaDataRecord
	{
		/** Constructs a populated object.
			@param pDataSource The connection factory used
				to load the child objects.
		*/
		private Record(DataSource pDataSource)
		{
			m_DataSource = pDataSource;
		}

		public void fetch(ResultSet rs) throws SQLException
		{
			m_strCatalog = rs.getString(1);
			m_strSchema = rs.getString(2);
			m_strName = rs.getString(3);
			m_strRemarks = rs.getString(7);
			m_nType = rs.getInt(8);
		}

		public int bindID(PreparedStatement pStmt, int nParam)
			throws SQLException
		{
			pStmt.setString(nParam, m_strName);
			return (++nParam);
		}

		public int bindDesc(PreparedStatement pStmt, int nParam)
			throws SQLException
		{	return bindID(pStmt, nParam);	}

		/** Accessor method - gets the Catalog name of the table. */
		public String getCatalog() { return m_strCatalog; }

		/** Accessor method - gets the Schema name of the table. */
		public String getSchema() { return m_strSchema; }

		public String getDesc() { return m_strName; }
		public int getType() { return m_nType; }

		public Parameters getParameters() { return new Parameters(m_DataSource, this); }

		/*******************************************************************
		*
		*	Member variables
		*
		*******************************************************************/

		/** Member variable - refernece to the connection pool used to load
		    child objects.
		*/
		private DataSource m_DataSource = null;

		/** Member variable - references the database catalog that contains
		    the procedure.
		*/
		private String m_strCatalog = null;

		/** Member variable - references the database schema that contains
		    the procedure.
		*/
		private String m_strSchema = null;

		/** Member variable - contains the procedure type ID. */
		private int m_nType = DatabaseMetaData.procedureResultUnknown;
	}
}
