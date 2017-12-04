package com.small.library.metadata;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

/** Represents a component that loads database metadata from ResultSet instances into structured objects.
 * 
 * @author smalleyd
 * @version 2.0.2
 * @since 9/21/2017.
 * 
 *
 */
public class DBMetadata
{
	private final DataSource dataSource;

	public DBMetadata(final DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getCatalog() throws SQLException
	{
		try (final Connection connection = dataSource.getConnection())
		{
			return connection.getCatalog();
		}
	}

	public List<Column> getColumns(final String schema, final String table) throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getColumns(null, schema, table, null))
		{
			return toList(r -> new Column(r), rs, Column.class);
		}
	}

	public List<Index> getIndexes(final String schema, final String table) throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getIndexInfo(null, schema, table, false, true))
		{
			final List<Index> values = new LinkedList<>();
			while (rs.next())
			{
				if (DatabaseMetaData.tableIndexStatistic == rs.getShort(7))
					continue;
			}

			return values;
		}
	}

	public List<Parameter> getParameters(final Procedure o) throws SQLException
	{
		return getParameters(o.schema, o.name);
	}

	public List<Parameter> getParameters(final String schema, final String procedure) throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getProcedureColumns(null, schema, procedure, null))
		{
			return toList(r -> new Parameter(r), rs, Parameter.class);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PrimaryKey> getPrimaryKeys(final String schema, final String table) throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getPrimaryKeys(null, schema, table))
		{
			return toList(r -> new PrimaryKey(r), rs, PrimaryKey.class);
		}
		catch (final SQLException ex)
		{
			if (ex.getSQLState().substring(0, 2).equals("IM"))
				return Collections.EMPTY_LIST;

			throw ex;
		}
	}

	public List<Procedure> getProcedures() throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getProcedures(null, null, null))
		{
			return toList(r -> new Procedure(r), rs, Procedure.class);
		}
	}

	public List<TypeInfo> getTypeInfo() throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getTypeInfo())
		{
			return toList(r -> new TypeInfo(r), rs, TypeInfo.class);
		}
	}

	private <T> List<T> toList(final SQLFunction<ResultSet, T> fx, final ResultSet rs, final Class<T> clazz)
		throws SQLException
	{
		List<T> values = new LinkedList<>();
		while (rs.next())
			values.add(fx.apply(rs));

		return values;
	}

	@FunctionalInterface
	private static interface SQLFunction<T, R>
	{
		public R apply(T value) throws SQLException;
	}
}
