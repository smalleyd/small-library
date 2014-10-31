package com.small.library.data;

import java.sql.*;
import java.util.HashMap;

import com.small.library.util.AbstractPool;
import com.small.library.util.PoolException;

/*****************************************************************************************
*
*	A pool of JDBC Connection objects. When a client is done using a Connection,
*	it must call <I>release</I> to return the object to the pool.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 9/3/2000
*
*****************************************************************************************/

public class ConnectionPool extends AbstractPool implements ConnectionFactory
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - default initial capacity. */
	public static final int INITIAL_CAPACITY = 2;

	/*******************************************************************************
	*
	*	Static variables and methods
	*
	*******************************************************************************/

	/** Member variable - HashMap to the list of connection pools. */
	private static HashMap m_Pools = new HashMap();

	/** Creates and returns a connection pool object based on the data source.
		@param pDataSource The <I>DataSource</I> object.
		@param nInitialCapacity The initial number of <I>Connection</I> objects
			in the pool.
	*/
	public static ConnectionPool getInstance(DataSource pDataSource,
		int nInitialCapacity)
		throws SQLException
	{
		synchronized (m_Pools)
		{
			ConnectionPool pPool = (ConnectionPool) m_Pools.get(pDataSource);

			if (null == pPool)
			{
				pDataSource.validate();

				try { pPool = new ConnectionPool(pDataSource, nInitialCapacity); }
				catch (PoolException pEx) { throw new SQLException(pEx.getMessage()); }

				m_Pools.put(pDataSource, pPool);
			}

			return pPool;
		}
	}

	/** Creates and returns a connection pool object based on the data source.
		@param pDataSource The <I>DataSource</I> object.
	*/
	public static ConnectionPool getInstance(DataSource pDataSource)
		throws SQLException
	{ return getInstance(pDataSource, INITIAL_CAPACITY); }

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	******************************************************************************/

	/** Constructor - accepts a DataSource object for creating JDBC Connections.
		@param pDataSource A DataSource object.
	*/
	private ConnectionPool(DataSource pDataSource)
		throws SQLException, PoolException
	{ this(pDataSource, INITIAL_CAPACITY); }

	/** Constructor - accepts a DataSource object and a initial capacity. */
	private ConnectionPool(DataSource pDataSource, int nInitialCapacity)
		throws SQLException, PoolException
	{
		super(nInitialCapacity);

		setDataSource(pDataSource);

		try { init(); }
		catch (PoolException pEx)
		{
			Exception pRootCause = pEx.getRootCause();

			if (null == pRootCause)
				throw pEx;
			else if (pRootCause instanceof SQLException)
				throw (SQLException) pRootCause;
			else
				throw pEx;
		}
	}

	/******************************************************************************
	*
	*	Main methods
	*
	******************************************************************************/

	/** Creates the individual JDBC Connection objects in the pool. */
	public Object create() throws PoolException
	{
		try { return m_DataSource.getConnection(); }
		catch (Exception pEx) { throw new PoolException(pEx); }
	}

	/** Returns the next Connection object in the pool. Performs additional checks
	    after the next Connection object has been retrieved.
	*/
	public Connection getConnection()
		throws SQLException
	{
		try
		{
			Connection pConnection = (Connection) get();

			// Is the connection object valid?
			if ((null != pConnection) && !pConnection.isClosed())
				return pConnection;

			// If the connection is closed, restart the pool.
			init();

			return (Connection) get();
		}

		catch (PoolException pEx)
		{ throw (SQLException) pEx.getRootCause(); }
	}

	/** Mutator method - releases a JDBC <I>Connection</I> from usage by
	    the caller. This method follows a <CODE>getConnection</CODE> call
	    when the caller is finished using the <I>Connection</I>.
		@param pConnection The <I>Connection</I> object to be released.
	*/
	public void release(Connection pConnection)
	{
		try { super.release((Object) pConnection); }
		catch (PoolException pEx) { /* No exceptions should be thrown. */ }
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor - gets the DataSource object property. */
	public DataSource getDataSource() { return m_DataSource; }

	/** Accessor - returns a String identity. */
	public String toString() { return m_DataSource.toString(); }

	/** Accessor - returns the integer identity. */
	public int hashCode() { return m_DataSource.hashCode(); }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator - sets the DataSource object property. */
	public void setDataSource(DataSource pNewValue) { m_DataSource = pNewValue; }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains a reference to the DataSource object. */
	private DataSource m_DataSource = null;
}
