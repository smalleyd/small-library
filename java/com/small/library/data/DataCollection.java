package com.small.library.data;

import java.util.*;
import java.sql.*;

import com.small.library.util.MapList;

/***************************************************************************************
*
*	Parent class for collection objects that contain zero to many DataRecord
*	object.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public abstract class DataCollection
{
	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	protected DataCollection(ConnectionFactory pConnectionFactory)
	{
		setConnectionFactory(pConnectionFactory);
	}

	/******************************************************************************
	*
	* Main functionality
	*
	******************************************************************************/

	/** Loads the data collection with data records based on the subclasses
	    getResultSet functionality.
		@param bReset Indicates whether to reset the data collection first.
	*/
	public void load(boolean bReset) throws SQLException
	{
		// Should the collection be reset (cleared)?
		if (bReset)
			reset();

		try
		{
			ResultSet pResultSet = getResultSet();

			// If the result set is null, then do perform the load.
			if (null == pResultSet)
				return;

			while (pResultSet.next())
			{
				DataRecord pRecord = newRecord();
				pRecord.fetch(pResultSet);

				// Base class can implement "acceptRecord" to throw out
				// records it doesn't want.
				if (acceptRecord(pRecord))
					addItem(pRecord);
			}

			pResultSet.close();
		}

		finally { releaseConnection(); }
	}

	/** Loads the data collection with data records based on the subclasses
	    getResultSet functionality.
	*/
	public void load() throws SQLException
	{ load(true); }

	/** Loads a <I>DataRecord</I> object with additional information
	    about a record that was not retrieved during the normal fetch. This
	    may include larger data fields such as long character or binary data.
		@param pRecord A <I>DataRecord</I> object.
	*/
	public void loadMore(DataRecord pRecord) throws SQLException
	{
		try { pRecord.fetchMore(getResultSet(pRecord)); }
		finally { releaseConnection(); }
	}

	/** Stores an indivdual data record in the data collection.
		@param pRecord A reference to a data record to store.
	*/
	public void store(DataRecord pRecord) throws SQLException
	{
		try
		{
			pRecord.update(prepareUpdateStatement());

			// Also, store the record in the same location in case
			// the record is a cloned version.
			setItem(pRecord);
		}

		finally { releaseConnection(); }
	}

	/** Stores all the dirty data record objects in the collection. */
	public void store() throws SQLException
	{
		try
		{
			PreparedStatement pStmt = prepareUpdateStatement();
 
			int nSize = m_Data.size();

			for (int i = 0; i < nSize; i++)
				item(i).update(pStmt);
		}

		finally { releaseConnection(); }
	}

	/** Only persists a new data record. It does not add the data record to the collection.
		@param pRecord The data record to add to the persistent storage.
	*/
	public void insertRecord(DataRecord pRecord) throws SQLException
	{
		insertRecord(prepareInsertStatement(), pRecord);
	}

	/** Only persists a new data record. It does not add the data record to the collection.
		@param pStmt A Prepared Statement object for inserting the record.
		@param pRecord The data record to add to the persistent storage.
	*/
	public void insertRecord(PreparedStatement pStmt,
		DataRecord pRecord) throws SQLException
	{
		pRecord.insert(pStmt);
	}

	/** Adds a new data record object to the data collection.
		@param pRecord A reference to a data record to add to the data collection.
	*/
	public DataRecord add(DataRecord pRecord) throws SQLException
	{
		try
		{
			insertRecord(pRecord);
			addItem(pRecord);

			return pRecord;
		}

		finally { releaseConnection(); }
	}

	/** Adds all the data records in the List object to the data collection.
		@param pRecords A reference to the List interface of data record objects to add
		                to the data collection.
		@return Returns the number of records added.
	*/
	public int add(DataRecord[] pRecords) throws SQLException
	{
		try
		{
			PreparedStatement pStmt = prepareInsertStatement();

			for (int i = 0; i < pRecords.length; i++)
			{
				DataRecord pRecord = pRecords[i];
				insertRecord(pStmt, pRecord);
				addItem(pRecord);
			}

			return pRecords.length;
		}

		finally { releaseConnection(); }
	}

	/** Adds all the data records in the List object to the data collection.
		@param pRecords A reference to the List interface of data record objects to add
		                to the data collection.
		@return Returns the number of records added.
	*/
	public int add(List pRecords) throws SQLException
	{
		int nSize = pRecords.size();

		if (0 == nSize)
			return nSize;

		return add((DataRecord[]) pRecords.toArray(new DataRecord[nSize]));
	}

	/** Removes a data record object from the data collection.
		@param pRecord A reference to a data record object to remove from the data collection.
	*/
	public void remove(DataRecord pRecord) throws SQLException
	{
		try
		{
			pRecord.delete(prepareDeleteStatement());
			removeItem(pRecord);
		}

		finally { releaseConnection(); }
	}

	/** Removes all the data record objects from the data collection and physical database. */
	public void clear() throws SQLException
	{
		try
		{
			deleteAll();
			reset();
		}

		finally { releaseConnection(); }
	}

	/** Determines if a data record already exists in the data collection.
		@param pRecord A reference to a data record object to check for duplication.
		@return Returns true if the data record already exists in the data collection.
	*/
	public boolean isDuplicate(DataRecord pRecord) throws SQLException
	{
		try
		{
			CallableStatement pStmt = prepareDuplicateStatement();

			pStmt.registerOutParameter(1, Types.INTEGER);
			pRecord.bindDesc(pStmt, pRecord.bindID(pStmt, 2));

			// Execute the statement.
			pStmt.executeUpdate();

			// Test for zero (0), because the return value can be
			// any number greater than zero, if true.
			return ((0 == pStmt.getInt(1)) ? false : true);
		}

		finally { releaseConnection(); }
	}

	/** Determines if a data record can be removed from the database.
		@param pRecord A reference to a data record object to for removal.
		@return Returns true if the data record can be removed from the data collection.
	*/
	public boolean canRemove(DataRecord pRecord) throws SQLException
	{
		try
		{
			CallableStatement pStmt = prepareCanRemoveStatement();

			pStmt.registerOutParameter(1, Types.INTEGER);
			pRecord.bindID(pStmt, 2);

			// Execute the statement.
			pStmt.executeUpdate();

			// Test for zero (0), because the return value can be
			// any number greater than zero, if true.
			return ((0 == pStmt.getInt(1)) ? false : true);
		}

		finally { releaseConnection(); }
	}

	/** Loops through the data record objects in the collection and polls for
	    a dirty record. A data record object is dirty if the data it contains
	    is inconsistent with the physical database.
		@return Returns true if a single data record object is dirty.
	*/
	public boolean isDirty()
	{
		int nSize = m_Data.size();

		for (int i = 0; i < nSize; i++)
			if (item(i).isDirty())
				return true;

		return false;
	}

	/** Returns the number of data record objects in the data collection. */
	public int size() { return m_Data.size(); }

	/** Returns a reference to a data record object at the index value in the
	    data collection.
		@param nItem The index value of the data record object in the data collection.
	*/
	public DataRecord item(int nItem) { return (DataRecord) m_Data.get(nItem);	}

	/** Returns a reference to a data record object that matches the key parameter.
		@param strKey A reference to a string key value.
	*/
	public DataRecord find(String strKey) { return (DataRecord) m_Data.get(strKey); }

	/** Returns whether a data record object matching the key parameter exists
	    in the data collection.
		@param strKey A reference to a string key value.
	*/
	public boolean exists(String strKey) { return m_Data.containsKey(strKey); }

	/******************************************************************************
	*
	* Functionality to be implemented by the subclass
	*
	******************************************************************************/

	/** Returns a reference to a new data record object appropriate the subclassed
	    data collection object. */
	public abstract DataRecord newRecord();

	/** Returns <CODE>true</CODE> to indicate that the record should be accepted into
	    the data collection during a load. The default implementation returns <CODE>true</CODE>.
	*/
	protected boolean acceptRecord(DataRecord pRecord) { return true; }

	/** Returns a reference to a JDBC resultset object for loading the
	    data record objects. */
	protected ResultSet getResultSet() throws SQLException { return null; }

	/** Returns a JDBC resultset object that retrieves additional information
	    about a record that was not retrieved during the normal fetch. This
	    may include larger data fields such as long character or binary data.
		@param pRecord A <I>DataRecord</I> object.
	*/
	protected ResultSet getResultSet(DataRecord pRecord) throws SQLException { return null; }

	/** Returns a reference to a JDBC prepared statement object for
	    performing updates of existing database records. */
	protected PreparedStatement prepareUpdateStatement() throws SQLException { return null; }

	/** Returns a reference to a JDBC prepared statement object for
	    performing inserts of new database records. */
	protected PreparedStatement prepareInsertStatement() throws SQLException { return null; }

	/** Returns a reference to a JDBC prepared statement object for
	    performing deletes against existing database records. */
	protected PreparedStatement prepareDeleteStatement() throws SQLException { return null; }

	/** Removes all records in the database matching the records in the data collection. */
	protected void deleteAll() throws SQLException {}

	/** Returns a reference to a JDBC callable statement object for
	    determining the duplicate status of a data record object. */
	protected CallableStatement prepareDuplicateStatement() throws SQLException { return null; }

	/** Returns a reference to a JDBC callable statement object for
	    determining the removable status of a data record object. */
	protected CallableStatement prepareCanRemoveStatement() throws SQLException { return null; }

	/******************************************************************************
	*
	*	Accessor/Mutator methods
	*
	******************************************************************************/

	/** Accessor method - gets a reference to the JDBC connection object in use. */
	public Connection getConnection() throws SQLException
	{
		if (null == m_Connection)
			initConnection();

		return m_Connection;
	}

	/** Accessor method - gets a reference to the connection factory in use. */
	public ConnectionFactory getConnectionFactory()
	{ return m_ConnectionFactory; }

	/** Mutator method - Sets the connection factory for use in the data collection.
		@param pConnectionFactory A reference to a connection factory for use.
	*/
	public void setConnectionFactory(ConnectionFactory pNewValue)
	{ m_ConnectionFactory = pNewValue; }

	/** Mutator method - initializes the connection object for the current
	    operation.
	*/
	private void initConnection() throws SQLException
	{
		m_Connection = m_ConnectionFactory.getConnection();
	}

	/** Mutator method - releases the connection object used for the current
	    operation.
	*/
	private void releaseConnection() throws SQLException
	{
		if (null != m_Connection)
			m_ConnectionFactory.release(m_Connection);

		m_Connection = null;
	}

	/******************************************************************************
	*
	*	Protected Helper Methods
	*
	******************************************************************************/

	/** Creates a JDBC ResultSet object based on the supplied statement.
		@param strStatement A SQL statement or stored procedure to prepare.
	*/
	protected ResultSet executeQuery(String strStatement) throws SQLException
	{
		return getConnection().createStatement().executeQuery(strStatement);
	}

	/** Creates a JDBC ResultSet object based on the supplied statement.
		@param strStatement A SQL statement or stored procedure to prepare.
	*/
	protected int executeUpdate(String strStatement) throws SQLException
	{
		return getConnection().createStatement().executeUpdate(strStatement);
	}

	/** Creates a JDBC PreparedStatement object based on the supplied statement.
		@param strStatement A SQL statement or stored procedure to prepare.
	*/
	protected PreparedStatement prepareStatement(String strStatement)
		throws SQLException
	{
		return getConnection().prepareStatement(strStatement);
	}

	/** Creates a JDBC CallableStatement object based on the supplied statement.
		@param strStatement A SQL statement or stored procedure to prepare.
	*/
	protected CallableStatement prepareCall(String strStatement)
		throws SQLException
	{
		return getConnection().prepareCall(strStatement);
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Adds a new data record object to the in-memory collection. */
	private void addItem(DataRecord pRecord)
	{
		m_Data.put(pRecord.toString(), pRecord);
	}

	/** Replaces the data record with a cloned version. Mainly used for stores. */
	private DataRecord setItem(DataRecord pRecord)
	{
		return (DataRecord) m_Data.put(pRecord.toString(), pRecord);
	}

	/** Removes all data record objects from the in-memory collection. */
	private void reset()
	{
		m_Data.clear();
	}

	/** Removes the item from both collections and compacts their order. */
	private void removeItem(DataRecord pRecord)
	{
		m_Data.remove(pRecord.toString());
	}

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - reference to the connection factory used by the
	    data collection.
	*/
	private ConnectionFactory m_ConnectionFactory = null;

	/** Member variable - reference to the connection object being used by the
	    current data collection operation.
	*/
	private Connection m_Connection = null;

	/** Member variable - reference to the container for the collection of
	    data records.
	*/
	private MapList m_Data = new MapList();
}
