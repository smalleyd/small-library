package com.small.library.jdo;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import com.small.library.taglib.ListItemImpl;

/**************************************************************************************
*
*	Data access factory.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 12/9/2004
*
**************************************************************************************/

public abstract class DataManager
{
	/******************************************************************************
	*
	*	Static members
	*
	******************************************************************************/

	/** Static member - Connection local thread instance. Stores a Connection
	    per thread for transactions.
	*/
	private static final ThreadLocal localConnection = new ThreadLocal();

	/******************************************************************************
	*
	*	RecordHandler methods
	*
	******************************************************************************/

	/** Helper method - load Collection of DataValue objects. */
	protected Collection loadDataValues(ResultSet rs, RecordHandler handler,
		Class classDataValue)
			throws JdoException
	{
		Collection values = new ArrayList();

		try
		{
			while (rs.next())
			{
				DataValue value = (DataValue) classDataValue.newInstance();
				handler.fetch(rs, value);
				values.add(value);
			}
		}

		catch (Exception ex) { throw new JdoException(ex); }

		return values;
	}

	/** Helper method - load Collection of DataValue objects. */
	protected Collection loadDataValues(StatementHandler stmtHandler,
		RecordHandler recordHandler, Class classDataValue)
			throws JdoException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			connection = getConnection();
			stmt = stmtHandler.prepare(connection);

			return loadDataValues(rs = stmt.executeQuery(), recordHandler,
				classDataValue);
		}

		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new JdoException(ex); }
		finally
		{
			closeJdbcResources(connection, stmt, rs);
		}
	}

	/** Helper method - updates by RecordHandler. */
	protected void updateRecord(String sql, RecordHandler handler, DataValue dataValue)
		throws JdoException
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = getConnection();
			stmt = connection.prepareStatement(sql);
			handler.update(stmt, dataValue);
			stmt.executeUpdate();
		}

		catch (Exception ex) { throw new JdoException(ex); }
		finally
		{
			closeJdbcResources(connection, stmt, null);
		}
	}

	/** Helper method - inserts by RecordHandler. */
	protected void insertRecord(String sql, RecordHandler handler, DataValue dataValue)
		throws JdoException
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = getConnection();
			stmt = connection.prepareStatement(sql);
			handler.insert(stmt, dataValue);
			stmt.executeUpdate();
		}

		catch (Exception ex) { throw new JdoException(ex); }
		finally
		{
			closeJdbcResources(connection, stmt, null);
		}
	}

	/** Helper method - deletes by RecordHandler. */
	protected void deleteRecord(String sql, RecordHandler handler, DataValue dataValue)
		throws JdoException
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = getConnection();
			stmt = connection.prepareStatement(sql);
			handler.insert(stmt, dataValue);
			stmt.executeUpdate();
		}

		catch (Exception ex) { throw new JdoException(ex); }
		finally
		{
			closeJdbcResources(connection, stmt, null);
		}
	}

	/*************************************************************************************
	*
	*	Connection methods
	*
	*************************************************************************************/

	/** Abstract method - gets the underlying connection. */
	protected abstract Connection getLocalConnection() throws RuntimeException;

	/** Helper method - gets the current thread's Connection. */
	protected Connection getConnection() throws RuntimeException
	{
		Connection ref = (Connection) localConnection.get();

		if (null != ref)
			return ref;

		return getLocalConnection();
	}

	/** Helper method - gets the main underlying connection. */
	protected Connection getConnection(String jndiName) throws RuntimeException
	{
		try
		{
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup(jndiName);

			return ds.getConnection();
		}

		catch (Exception ex) { throw new RuntimeException(ex); }
	}

	/**********************************************************************************
	*
	*	Transaction methods
	*
	**********************************************************************************/

	/** Helper method - starts a transaction. */
	public void beginTrans() throws RuntimeException
	{
		try
		{
			Connection ref = (Connection) localConnection.get();

			// Already started.
			if (null != ref)
				return;

			ref = getConnection();
			ref.setAutoCommit(false);
			localConnection.set(ref);
		}

		catch (SQLException ex) { throw new RuntimeException(ex); }
	}

	/** Helper method - starts a read-only transaction. Used for read-only pages to
	    speed up the load.
	*/
	public void beginReadOnlyTrans() throws RuntimeException
	{
		try
		{
			Connection ref = (Connection) localConnection.get();

			// Already started.
			if (null != ref)
				return;

			ref = getConnection();
			ref.setAutoCommit(true);
			ref.setReadOnly(true);
			localConnection.set(ref);
		}

		catch (SQLException ex) { throw new RuntimeException(ex); }
	}
			
	/** Helper method - commits a transaction. */
	public void commitTrans() throws RuntimeException
	{
		Connection ref = (Connection) localConnection.get();

		// No transaction.
		if (null == ref)
			return;

		resetTrans(ref);
	}

	/** Helper method - rollback a transaction. */
	public void rollbackTrans() throws RuntimeException
	{
		try
		{
			Connection ref = (Connection) localConnection.get();

			// No transaction.
			if (null == ref)
				return;

			ref.rollback();
			resetTrans(ref);
		}

		catch (SQLException ex) { throw new RuntimeException(ex); }
	}

	/** Helper method - is a transaction active. */
	public boolean isActiveTrans() throws RuntimeException
	{
		return (null == localConnection.get()) ? false : true;
	}

	/** Helper method - resets the transaction after a commit or rollback. */
	private void resetTrans(Connection ref) throws RuntimeException
	{
		try
		{
			ref.setAutoCommit(true);
			ref.setReadOnly(false);
			ref.close();

			localConnection.set(null);
		}

		catch (SQLException ex) { throw new RuntimeException(ex); }
	}

	/*****************************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************************/

	/** Helper method - closes JDBC resources. Mainly used in a finally clause. */
	protected void closeJdbcResources(Connection connection, Statement stmt,
		ResultSet rs)
	{
		try
		{
			if (null != rs) rs.close();
			if (null != stmt) stmt.close();
			if ((null != connection) && !isActiveTrans()) connection.close();
		}

		catch (SQLException ex) { /** No needed to throw, since just cleaning up. */ }
	}
}

