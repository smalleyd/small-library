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

	public List<TypeInfo> getTypeInfo() throws SQLException
	{
		try (final Connection connection = dataSource.getConnection();
		     final ResultSet rs = connection.getMetaData().getTypeInfo())
		{
			List<TypeInfo> values = new LinkedList<>();
			while (rs.next())
				values.add(new TypeInfo(rs));

			return values;
		}
	}
}
