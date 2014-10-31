package com.small.library.data;

import java.sql.*;
import java.util.List;
import java.util.Vector;

import com.small.library.util.PoolException;

/***************************************************************************************
*
*	Parent class for application specific version of the Data Manager. The Data
*	Manager lies between the Business Services Layer and the Data Services Layer.
*	The Data Manager supplies Data Service Objects to Business Service
*	Objects upon request.
*	<BR><BR>
*	The Data Manager also coordinates transactions. Transactions can include a mix
*	of updates to a Data Service Object or straight JDBC calls.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class DataManager
{
	/***************************************************************************************
	*
	*	Static member variables
	*
	***************************************************************************************/

	/** Objects shared by the entire application. */
	private static int m_nNumberOfConnections = 2;
	private static String m_strLineBreak = "<BR>";

	/***************************************************************************************
	*
	*	Constructors/Destructor
	*
	***************************************************************************************/

	/** Constructor - Default. */
	public DataManager() {}

	/** Constructor - Accepts data source information.
		@param strDriver The JDBC class name that implements the interaction with the database.
		@param strDataSource The name of the data source.
		@param strUserName The user login to the database.
		@param strPassword the login's password to the database.
	*/
	public DataManager(String strDriver,
		String strDataSource, String strUserName, String strPassword)
			throws DataSourceException
	{ setDataSource(strDriver, strDataSource, strUserName, strPassword); }

	/** Constructor - Accepts data source information.
		@param strDriver The JDBC class name that implements the interaction with the database.
		@param strDataSource The name of the data source.
		@param strUserName The user login to the database.
		@param strPassword the login's password to the database.
		@param nNumberOfConnections Indicates the initial number of data source connections in the pool.
	*/
	public DataManager(String strDriver,
		String strDataSource, String strUserName, String strPassword, int nNumberOfConnections)
			throws DataSourceException
	{
		setDataSource(strDriver, strDataSource, strUserName, strPassword);
		setNumberOfConnections(nNumberOfConnections);
	}

	/** Constructor - Accepts a DataSource object.
		@param pDataSource The DataSource object.
	*/
	public DataManager(DataSource pDataSource) { setDataSource(pDataSource); }

	/** Constructor - Accepts a DataSource object and the initial number of connections.
		@param pDataSource The DataSource object.
		@param nNumberOfConnections Indicates the initial number of data source connections in the pool.
	*/
	public DataManager(DataSource pDataSource, int nNumberOfConnections)
	{
		setDataSource(pDataSource);
		setNumberOfConnections(nNumberOfConnections);
	}

	/***************************************************************************************
	*
	*	Connection related activity
	*
	***************************************************************************************/

	/** Accessor - retrieves a connection from the default connection pool. */
	private Connection getConnection()
		throws SQLException
	{
		// First, Does the data source object exist?
		if (null == m_DataSource)
			throw new SQLException("Data Manager: The Data Source has not been set.");

		if (isTransaction())
			return m_Connection;

		return getConnectionPool(m_DataSource).getConnection();
	}

	/** Retrieves and initializes a connection pool if necessary.
		@param pDataSource A data source object to search by.
	*/
	private ConnectionPool getConnectionPool(DataSource pDataSource)
		throws SQLException
	{ return ConnectionPool.getInstance(pDataSource, m_nNumberOfConnections); }

	private void releaseConnection(Connection pConnection)
	{
		// Make sure the connection pool object was created. If the
		// data source is invalid, the connection pool will not be created.
		if (isTransaction() || (null == m_DataSource) || (null == pConnection))
			return;

		try
		{
			ConnectionPool pPool = getConnectionPool(m_DataSource);
			pPool.release(pConnection);
		}

		catch (Exception pEx) {}
	}

	/******************************************************************************
	*
	* Methods for assisting with data access. The methods deal with the
	* connection pool and error handling. The derived class will have to
	* deal with caching since that changes from application to application.
	*
	*****************************************************************************/

	/** Loads a data collection.
		@param pData The data collection object to load.
		@param bReset Indicates whether to remove all data records from the
			data collection before loading.
	*/
	protected void loadDataCollection(DataCollection pData,
		boolean bReset) throws Errors
	{
		try { pData.load(bReset); }			
		catch (SQLException ex) { throwError(ex); }
	}

	/** Loads a data collection.
		@param pData The data collection object to load.
	*/
	protected void loadDataCollection(DataCollection pData)
		throws Errors
	{ loadDataCollection(pData, true); }

	/** Loads any data collection and returns the reference to the data
	    collection.
		@param pData The data collection object to load.
		@param bReset Indicates whether to remove all data records from the
			data collection before loading.
		@return Returns a reference to the data collection passed in.
	*/
	protected DataCollection getDataCollection(DataCollection pData,
		boolean bReset) throws Errors
	{
		loadDataCollection(pData, bReset);
		return pData;
	}

	/** Loads any data collection and returns the reference to the data
	    collection.
		@param pData The data collection object to load.
		@return Returns a reference to the data collection passed in.
	*/
	protected DataCollection getDataCollection(DataCollection pData) throws Errors
	{ return getDataCollection(pData, true); }

	/** Loads any data collection and returns the first data record. Mainly used
	    by the "gets" that only need to return a single data record object. */
	protected DataRecord getRecord(DataCollection pData) throws Errors
	{
		loadDataCollection(pData, true);

		if (0 == pData.size())
			return null;
		
		return pData.item(0);
	}

	/** Performs a <CODE>loadMore</CODE> operation on the <I>DataCollection</I>
	    and <I>DataRecord</I> objects.
		@param pData <I>DataCollection</I> object.
		@param pRecord <I>DataRecord</I> object.
	*/
	public void loadMore(DataCollection pData, DataRecord pRecord)
		throws Errors
	{
		try { pData.loadMore(pRecord); }
		catch (SQLException pEx) { throwError(pEx); }
	}

	/** Stores a data record to a data collection.
		@param pData The data collection used to the data record.
		@param pRecord The data record object to be stored through the data collection.
	*/
	public void storeRecord(DataCollection pData, DataRecord pRecord)
		throws Errors
	{
		try { pData.store(pRecord); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Stores all data records contains by the data collection.
		@param pData The data collection used to store data records. */
	public void storeDataCollection(DataCollection pData) throws Errors
	{
		try { pData.store(); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Inserts a new Data Record without actually caching the Data Record in the
	    Data Collection.
		@param pData The data collection to accept the new data record.
		@param pRecord The new data record being added to the data collection.
	*/
	public void insertRecord(DataCollection pData, DataRecord pRecord)
		throws Errors
	{
		try { pData.insertRecord(pRecord); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Adds a data record to the data collection.
		@param pData The data collection to accept the new data record.
		@param pRecord The new data record being added to the data collection.
	*/
	public void addRecord(DataCollection pData, DataRecord pRecord)
		throws Errors
	{
		try { pData.add(pRecord); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Adds a List object of data records to the data collection.
		@param pData The data collection to accept the vector of data records.
		@param pRecords The data records to add to the data collection.
	*/
	public void addRecords(DataCollection pData, List pRecords)
		throws Errors
	{
		try { pData.add(pRecords); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Adds a Vector object of data records to the data collection.
	    Mainly for backward compatibility.
		@param pData The data collection to accept the vector of data records.
		@param pRecords The data records to add to the data collection.
	*/
	public void addRecords(DataCollection pData, Vector pRecords)
		throws Errors
	{
		try { pData.add(pRecords); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Removes the supplied data record from the data collection.
		@param pData The data collection that contains the data record to remove.
		@param pRecord The data record to remove from the data collection.
	*/
	public void removeRecord(DataCollection pData, DataRecord pRecord)
		throws Errors
	{
		try { pData.remove(pRecord); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Clears the entire data collection of all data records.
		@param pData The data collection to clear.
	*/
	public void clearDataCollection(DataCollection pData) throws Errors
	{
		try { pData.clear(); }
		catch (SQLException ex) { throwError(ex); }
	}

	/** Checks the data collection for a duplicate version of the data record.
		@param pData The data collection to check.
		@param pRecord The data record to check against the data collection.
		@throw Errors Thrown when a database error occurs.
		@throw DuplicateRecordException Thrown when a duplicate record is found.
	*/
	public void isDuplicateRecord(DataCollection pData, DataRecord pRecord)
		throws Errors, DuplicateRecordException
	{
		boolean bReturn = false;

		try { bReturn = pData.isDuplicate(pRecord); }
		catch (SQLException ex) { throwError(ex); }

		if (bReturn) throw new DuplicateRecordException(pData, pRecord);
	}

	/** Checks the data collection for whether the data record can be removed.
		@param pData The data collection to check.
		@param pRecord The data record to check against the data collection.
		@throw Errors Thrown when a database error occurs.
		@throw UnremovableRecordException Thrown when an unremovable record is found.
	*/
	public void canRemoveRecord(DataCollection pData, DataRecord pRecord)
		throws Errors, UnremovableRecordException
	{
		boolean bReturn = false;

		try { bReturn = pData.canRemove(pRecord); }
		catch (SQLException ex) { throwError(ex); }

		if (!bReturn) throw new UnremovableRecordException(pData, pRecord);
	}

	/******************************************************************************
	*
	* Direct JDBC calls
	*
	*****************************************************************************/

	/** Direct JDBC methods - Retrieves a JDBC <I>ResultSet</I>.
		@param strQuery The SQL statement to execute.
		@return JDBC <I>ResultSet</I> object.
	*/
	public ResultSet createResultSet(String strQuery)
		throws Errors
	{
		Connection pConnection = null;
		ResultSet pReturn = null;

		try
		{
			pConnection = getConnection();

			pReturn = getConnection().createStatement().executeQuery(strQuery);
		}
		catch (SQLException ex) { throwError(ex); }
		finally { releaseConnection(pConnection); }

		return pReturn;
	}

	/** Direct JDBC methods - Executes a SQL statement.
		@param strQuery The SQL statement to execute.
		@return Returns the number of rows affected.
	*/
	public int executeUpdate(String strQuery)
		throws Errors
	{
		int nRowsAffected = 0;
		Connection pConnection = null;

		try
		{
			pConnection = getConnection();

			nRowsAffected = getConnection().createStatement().executeUpdate(strQuery);
		}
		catch (SQLException ex) { throwError(ex); }
		finally { releaseConnection(pConnection); }

		return nRowsAffected;
	}

	/** Direct JDBC methods - Prepares a JDBC <I>PreparedStatement</I> object.
		@param strQuery The SQL statement to prepare.
		@return JDBC <I>PreparedStatement</I> object.
	*/
	public PreparedStatement prepareStatement(String strQuery)
		throws Errors
	{
		Connection pConnection = null;
		PreparedStatement pReturn = null;

		try
		{
			pConnection = getConnection();

			pReturn = getConnection().prepareStatement(strQuery);
		}
		catch (SQLException ex) { throwError(ex); }
		finally { releaseConnection(pConnection); }

		return pReturn;
	}

	/** Direct JDBC methods - Prepares a JDBC <I>CallableStatement</I> object.
		@param strQuery The SQL statement to prepare.
		@return JDBC <I>CallableStatement</I> object.
	*/
	public CallableStatement prepareCall(String strQuery)
		throws Errors
	{
		Connection pConnection = null;
		CallableStatement pReturn = null;

		try
		{
			pConnection = getConnection();

			pReturn = getConnection().prepareCall(strQuery);
		}
		catch (SQLException ex) { throwError(ex); }
		finally { releaseConnection(pConnection); }

		return pReturn;
	}

	/******************************************************************************
	*
	* Transaction methods
	*
	*****************************************************************************/

	/** Starts a tranaction. Allocates all the connection objects necessary to
	    perform the operation of the duration of the transaction.
	*/
	public void beginTransaction() throws Errors
	{
		// Has a transaction already begun?
		if (null != m_Connection)
			return;

		try { (m_Connection = getConnection()).setAutoCommit(false); }
		catch (SQLException pEx) { throwError(pEx); }
	}

	/** Commits the active transaction, otherwise just exits. */
	public void commitTransaction() throws Errors
	{
		// Does a transaction exist?
		if (null == m_Connection)
			return;

		// Do not need to call "commit". Turning on auto commit
		// automatically commits any existing transactions.
		try { m_Connection.setAutoCommit(true); }
		catch (SQLException pEx) { if (!"01000".equals(pEx.getSQLState())) throwError(pEx); }
		finally { releaseTransactionConnection(); }
	}

	/** Rollbacks the active transaction, otherwise just exits. */
	public void rollbackTransaction() throws Errors
	{
		// Does a transaction exist?
		if (null == m_Connection)
			return;

		try { m_Connection.rollback(); }
		catch (SQLException pEx) { throwError(pEx); }
		finally { releaseTransactionConnection(); }
	}

	/** Release current transaction connection. */
	private void releaseTransactionConnection()
	{
		try { if (!m_Connection.getAutoCommit()) m_Connection.setAutoCommit(true); }
		catch (Exception pEx) {}

		releaseConnection(m_Connection);
		m_Connection = null;
	}

	/******************************************************************************
	*
	* Accessor methods
	*
	*****************************************************************************/

	/** Returns a refernce to the line break characters used by string return
	    messages. */
	public String getLineBreak() { return m_strLineBreak; }

	/** Accessor - is a transaction active? */
	public boolean isTransaction() { return (null != m_Connection) ? true : false; }

	/******************************************************************************
	*
	* Mutator methods
	*
	*****************************************************************************/

	/** Sets the line break characters used by string return messages. */
	public void setLineBreak(String strValue) { m_strLineBreak = strValue; }

	/******************************************************************************
	*
	* Exception handling methods
	*
	*****************************************************************************/

	/** Throws the Errors object based on a prepared string message.
		@param strMessage The prepared string message.
	*/
	protected void throwError(String strMessage) throws Errors
	{ throw new Errors(strMessage); }

	/** Throws the Errors object based on the SQLException object.
		@param ex The SQLException object used to build the Errors collection.
	*/
	protected void throwError(SQLException ex) throws Errors
	{
		if (isTransaction())
		{
			// Make sure to only throw the original exception.
			try { rollbackTransaction(); }
			catch (Exception pEx) {}
		}

		throw new Errors(ex, getLineBreak());
	}

	/******************************************************************************
	*
	* Accessor & Mutator methods
	*
	*****************************************************************************/

	/** Accessor - Retrieves a reference to the data source. */
	public DataSource getDataSource() { return m_DataSource; }

	/** Mutator - Sets the data source used by the data manager.
		@param pNewValue The DataSource object.
	*/
	public void setDataSource(DataSource pNewValue) { m_DataSource = pNewValue; }

	/** Mutator - Caches the application's data source information.
		@param strDriver The JDBC class name that implements the interaction with the database.
		@param strDataSource The name of the data source.
		@param strUserName The user login to the database.
		@param strPassword the login's password to the database.
	*/
	public void setDataSource(String strDriver,
		String strDataSource,
		String strUserName,
		String strPassword)
			throws DataSourceException
	{
		m_DataSource = new DataSource(strDriver,
			strDataSource,
			strUserName,
			strPassword);
	}

	/** Accessor - Gets the number of connections to kept open in the connection pool. */
	public int getNumberOfConnections() { return m_nNumberOfConnections; }

	/** Mutator - Sets the number of connections to keep open in the connection pool. */
	public void setNumberOfConnections(int nValue) { m_nNumberOfConnections = nValue; }

	/******************************************************************************
	*
	* Private member variables (non-static)
	*
	*****************************************************************************/

	/** Member variable - contains the DataSource used by this instance of the DataManager. */
	private DataSource m_DataSource = null;

	/** Member variable - contains the connection currently involved in a transaction. */
	private Connection m_Connection = null;
}
