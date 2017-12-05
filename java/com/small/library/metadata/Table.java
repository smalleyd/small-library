package com.small.library.metadata;

import java.io.Serializable;
import java.sql.*;
import java.util.List;

/***************************************************************************************
*
*	Data collection that describes the tables of a data source.
*
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Table implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String TYPE_ALIAS = "ALIAS";
	public static final String TYPE_TABLE = "TABLE";
	public static final String TYPE_VIEW = "VIEW";
	public static final String TYPE_SYSTEM_TABLE = "SYSTEM TABLE";

	public final String name;
	public final String catalog;
	public final String schema;
	public final String type;
	public final boolean userDefined;
	public final boolean view;
	public final boolean system;
	public final boolean unknown;
	public final String remarks;
	private final DBMetadata metadata;

	public Table(final ResultSet rs, final DBMetadata metadata) throws SQLException
	{
		catalog = rs.getString(1);
		schema = rs.getString(2);
		name = rs.getString(3);
		type = rs.getString(4);
		remarks = rs.getString(5);

		userDefined = type.equals(Table.TYPE_TABLE);
		view = type.equals(Table.TYPE_VIEW);
		system = type.equals(Table.TYPE_SYSTEM_TABLE);
		unknown = (!userDefined && !view && !system);

		this.metadata = metadata;
	}

	public List<Column> getColumns() throws SQLException { return metadata.getColumns(this); }
	public List<Index> getIndexes() throws SQLException { return metadata.getIndexes(this); }
	public List<PrimaryKey> getPrimaryKeys() throws SQLException { return metadata.getPrimaryKeys(this); }
	public List<ForeignKey> getImportedKeys() throws SQLException { return metadata.getImportedKeys(this); }
	public List<ForeignKey> getExportedKeys() throws SQLException { return metadata.getExportedKeys(this); }
}
