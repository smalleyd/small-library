package com.small.library.metadata;

import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

import com.small.library.data.*;

/***************************************************************************************
*
*	Data collection that describes the tables of a data source.
*
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Tables extends MetaDataCollection
{
	/********************************************************************************************
	*
	*	Constants
	*
	********************************************************************************************/

	/** Constant - table type for alias. */
	public static final String TYPE_ALIAS = "ALIAS";

	/** Constant - table type for user defined tables. */
	public static final String TYPE_TABLE = "TABLE";

	/** Constant - table type for views. */
	public static final String TYPE_VIEW = "VIEW";

	/** Constant - table type for system tables. */
	public static final String TYPE_SYSTEM_TABLE = "SYSTEM TABLE";

	/********************************************************************************************
	*
	*	Constructors
	*
	********************************************************************************************/

	/** Constructor - Defaults to table type of "user defined".
		@param pDataSource A reference to a connection factory.
	*/
	public Tables(DataSource pDataSource)
	{ this(pDataSource, TYPE_TABLE); }

	/** Constructor - accepts a table type.
		@param pDataSource A reference to a connection factory.
		@param strType Table type value.
	*/
	public Tables(DataSource pDataSource, String strType)
	{ this(pDataSource, strType, null); }

	/** Constructor - accepts a table type.
		@param pDataSource A reference to a connection factory.
		@param strType Table type value.
		@param tablePattern Table name pattern to filter the table
			collection by.
	*/
	public Tables(DataSource pDataSource,
		String strType, String tablePattern)
	{
		super(pDataSource);
		setType(strType);
		setTablePattern(tablePattern);
	}

	/** Constructor - accepts a table type.
		@param connectionFactory A reference to a connection factory.
		@param types Array of table type values.
		@param tablePattern Table name pattern to filter the table
			collection by.
	*/
	public Tables(DataSource connectionFactory,
		String[] types, String tablePattern)
	{
		super(connectionFactory);
		setTypes(types);
		setTablePattern(tablePattern);
	}

	/********************************************************************************************
	*
	*	Abstract implementations
	*
	********************************************************************************************/

	/** Abstract implementation - gets a ResultSet that <CODE>load</CODE> can use. */
	protected ResultSet getResultSet() throws SQLException
	{
		return getDatabaseMetaData().getTables(null, null, tablePattern, types);
	}

	/** Abstract implementation - gets a new data record object. */
	public DataRecord newRecord() { return new Record(getDataSource()); }

	/********************************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************************/

	/** Accessor method - gets the table type property. */
	public String getType()
	{
		if ((null == types) || (0 == types.length))
			return null;

		return types[0];
	}

	/** Accessor method - gets the table types property. */
	public String[] getTypes() { return types; }

	/** Accessor method - gets the table name pattern filter. */
	public String getTablePattern() { return tablePattern; }

	/********************************************************************************************
	*
	*	Mutator methods
	*
	********************************************************************************************/

	/** Mutator method - sets the table type property. */
	public void setType(String newValue)
	{
		if (null == newValue)
			types = null;
		else
			types = new String[] { newValue };
	}

	/** Mutator method - sets the table types property. */
	public void setTypes(String[] newValues) { types = newValues; }

	/** Mutator method - sets the table name pattern filter. */
	public void setTablePattern(String newValue) { tablePattern = newValue; }

	/********************************************************************************************
	*
	*	Member variables
	*
	********************************************************************************************/

	/** Member variable - contains the table type property. */
	private String[] types = null;

	/** Member variable - contains the table name pattern filter. */
	private String tablePattern = null;

/***************************************************************************************
*
*	Data record that describes the tables of data source.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record extends MetaDataRecord
	{
		/** Constructor - constructs a populated object. Each
		    table record must maintain a reference to the
		    connection factory as the child objects require
		    a reference in order to load themselves.
		*/
		private Record(DataSource pDataSource)
		{
			m_DataSource = pDataSource;
		}

		/** Abstract implementation - gets a single record from the ResultSet. */
		public void fetch(ResultSet rs) throws SQLException
		{
			m_strCatalog = rs.getString(1);
			m_strSchema = rs.getString(2);
			m_strName = rs.getString(3);
			String strType = rs.getString(4);
			m_strRemarks = rs.getString(5);

			m_bUserDefined = strType.equals(Tables.TYPE_TABLE);
			m_bView = strType.equals(Tables.TYPE_VIEW);
			m_bSystem = strType.equals(Tables.TYPE_SYSTEM_TABLE);
			m_bUnknown = (!m_bUserDefined && !m_bView && !m_bSystem);

			clean();
		}

		/** Binds the table name to a parameter. */
		public int bindID(PreparedStatement pStmt, int nParam)
			throws SQLException
		{
			pStmt.setString(nParam, m_strName);
			return (++nParam);
		}

		/** Binds the table name to a parameter. */
		public int bindDesc(PreparedStatement pStmt, int nParam)
			throws SQLException
		{ return bindID(pStmt, nParam); }

		/** Accessor method - gets the Catalog name of the table. */
		public String getCatalog() { return m_strCatalog; }

		/** Accessor method - gets the Schema name of the table. */
		public String getSchema() { return m_strSchema; }

		/** Accessor method - gets the name of the table. */
		public String getDesc() { return m_strName; }

		/** Accessor method - gets the "is user defined" property. */
		public boolean isUserDefined() { return m_bUserDefined; }

		/** Accessor method - gets the "is view" property. */
		public boolean isView() { return m_bView; }

		/** Accessor method - gets the "is system" property. */
		public boolean isSystem() { return m_bSystem; }

		/** Accessor method - gets the "is unknown" property. */
		public boolean isUnknown() { return m_bUnknown; }

		/** Accessor methods - gets a Columns object associated with this table. */
		public List<Column> getColumns() throws SQLException { return new DBMetadata(m_DataSource).getColumns(m_strSchema, m_strName); }

		/** Accessor methods - gets an Indexes object associated with this table. */
		public Index getIndexes() { return new Index(m_DataSource, this); }

		/** Accessor methods - gets a Primary Keys object associated with this table. */
		public List<PrimaryKey> getPrimaryKeys() throws SQLException { return new DBMetadata(m_DataSource).getPrimaryKeys(m_strSchema, m_strName); }

		/** Accessor methods - gets a Imported Keys object associated with this table. */
		public ImportedKeys getImportedKeys() { return new ImportedKeys(m_DataSource, this); }

		/** Accessor methods - gets a Exported Keys object associated with this table. */
		public ExportedKeys getExportedKeys() { return new ExportedKeys(m_DataSource, this); }

		/************************************************************************
		*
		*	Member variables.
		*
		************************************************************************/

		/** Member variable - reference to the connection factory used by
		    by the child objects to load additional table information.
		*/
		private DataSource m_DataSource = null;

		/** Member variable - refernece to the table catalog (database). */
		private String m_strCatalog = null;

		/** Member variable - reference to the table schema (user). */
		private String m_strSchema = null;

		/** Member variable - indicates whether this table is user defined. */
		private boolean m_bUserDefined = false;

		/** Member variable - indicates whether this table is a view. */
		private boolean m_bView = false;

		/** Member variable - indicates whether this is a system table. */
		private boolean m_bSystem = false;

		/** Member variable - indicates that the table type is unknown. */
		private boolean m_bUnknown = false;
	}
}
