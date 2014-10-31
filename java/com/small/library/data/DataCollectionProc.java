package com.small.library.data;

import java.util.*;
import java.sql.*;

import com.small.library.data.*;

/***************************************************************************************
*
*	Extension to the Data Collection class that provides standard
*	support for stored procedures as the data access method. Stored procedure
*	calls are automatically generated through the use of getPrefix(), getTableName(),
*	and getSeparator(). Each procedures has the form of
*	<BR><BR>
*	[prefix]_[table name][separator][service](?, ?, ?...)
*	<BR><BR>
*	The prefix and separator will vary from RDBMS to RDBMS.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/4/2000
*
***************************************************************************************/

public abstract class DataCollectionProc extends DataCollection
{
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructs the data collection and supplies a JDBC connection factory.
		@param pConnectionFactory A reference to a connection factory.
	*/
	protected DataCollectionProc(ConnectionFactory pConnectionFactory)
	{
		super(pConnectionFactory);
	}

	/******************************************************************************
	*
	*	Implementation of DataCollection methods
	*
	******************************************************************************/

	/** Returns a reference to a JDBC resultset object for loading the
	    data record objects. */
	protected ResultSet getResultSet() throws SQLException
	{ return executeQuery(getQueryString()); }

	/** Returns a reference to a JDBC prepared statement object for
	    performing updates of existing database records. */
	protected PreparedStatement prepareUpdateStatement() throws SQLException
	{ return prepareStatement(getUpdateString()); }

	/** Returns a reference to a JDBC prepared statement object for
	    performing inserts of new database records. If the underlying
	    database object contains an auto incrementing field, then a CallableStatement
	    is prepared so that a the new key can be retrieved. */
	protected PreparedStatement prepareInsertStatement() throws SQLException
	{
		if (getHasAutoIncrement())
			return (PreparedStatement) prepareCall(getInsertString());
		else
			return prepareStatement(getInsertString());
	}

	/** Returns a reference to a JDBC prepared statement object for
	    performing deletes against existing database records. */
	protected PreparedStatement prepareDeleteStatement() throws SQLException
	{ return prepareStatement(getDeleteString()); }

	/** Removes all records in the database matching the records in the data collection. */
	protected void deleteAll() throws SQLException
	{ executeUpdate(getDeleteAllString()); }

	/** Returns a reference to a JDBC callable statement object for
	    determining the duplicate status of a data record object. */
	protected CallableStatement prepareDuplicateStatement() throws SQLException
	{ return prepareCall(getDuplicateString()); }

	/** Returns a reference to a JDBC callable statement object for
	    determining the removable status of a data record object. */
	protected CallableStatement prepareCanRemoveStatement() throws SQLException
	{ return prepareCall(getCanRemoveString()); }

	/******************************************************************************
	*
	* Default implementation of methods that will be overridden for supported
	* functionality of the derived class
	*
	******************************************************************************/

	/** Returns a string representing the statement used by the getResultSet method.
		@return Returns a string - defaults to null.
	*/
	protected String getQueryString() { return createProc("Load", 0, false); }

	/** Returns a string representing the statement used by the prepareUpdateStatement
	    method.
		@return Returns a string - defaults to null.
	*/
	protected String getUpdateString() { return createProc("Store", getUpdateParams(), false); }

	/** Returns a string representing the statement used by the prepareInsertStatement
	    method.
		@return Returns a string - defaults to null.
	*/
	protected String getInsertString() { return createProc("Add", getInsertParams(), getHasAutoIncrement()); }

	/** Returns a string representing the statement used by the prepareDeleteStatement
	    method.
		@return Returns a string - defaults to null.
	*/
	protected String getDeleteString() { return createProc("Remove", getDeleteParams(), false); }

	/** Returns a string representing the statement used by the deleteAll method.
		@return Returns a string - defaults to null.
	*/
	protected String getDeleteAllString() { return createProc("Clear", getDeleteAllParams(), false); }

	/** Returns a string representing the statement used by the prepareDuplicateStatement
	    method.
		@return Returns a string - defaults to null.
	*/
	protected String getDuplicateString() { return createProc("Duplicate", getDuplicateParams(), true); }

	/** Returns a string representing the statement used by the prepareCanRemoveStatement
	    method.
		@return Returns a string - defaults to null.
	*/
	protected String getCanRemoveString() { return createProc("CanRemove", getCanRemoveParams(), true); }

	/** Indicates whether the represented database object contains an
	    auto incremented field.
		@return Returns a boolean - defaults to false.
	*/
	protected boolean getHasAutoIncrement() { return false; }

	/** Returns an integer representing the number of parameters needed for the
	    UPDATE stored procedure.
		@return Returns an integer.
	*/
	protected int getUpdateParams() { return 0; }

	/** Returns an integer representing the number of parameters needed for the
	    INSERT stored procedure.
		@return Returns an integer.
	*/
	protected int getInsertParams() { return 0; }

	/** Returns an integer representing the number of parameters needed for the
	    DELETE stored procedure.
		@return Returns an integer.
	*/
	protected int getDeleteParams() { return 0; }

	/** Returns an integer representing the number of parameters needed for the
	    CLEAR (DELETE ALL) stored procedure.
		@return Returns an integer.
	*/
	protected int getDeleteAllParams() { return 0; }

	/** Returns an integer representing the number of parameters needed for the
	    "is duplicate" stored procedure.
		@return Returns an integer.
	*/
	protected int getDuplicateParams() { return 0; }

	/** Returns an integer representing the number of parameters needed for the
	    "can remove" stored procedure.
		@return Returns an integer - defaults to "getDeleteParams()".
	*/
	protected int getCanRemoveParams() { return getDeleteParams(); }

	/******************************************************************************
	*
	* Abstract methods and properties implemented by the derived class
	*
	******************************************************************************/

	/** Returns a string representing the stored procedure prefix.
		@return Returns a string.
	*/
	protected abstract String getPrefix();

	/** Returns a string representing the stored procedure body separator.
		@return Returns a string.
	*/
	protected abstract String getSeparator();

	/** Returns a string representing the underlying database object's name.
		@return Returns a string.
	*/
	protected abstract String getTableName();

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Returns a stored procedure name based on the Prefix, Table name, Separator,
	    the type of service, and number of parameters.
		@param strServiceType Represents the action the stored procedure will
			perform. Examples include Add, Store, Remove ... .
		@param nParameters Represents the number of parameters.
		@param bHasReturn Does the stored procedure return a value.
		@return Returns a string.
	*/
	private String createProc(String strServiceType, int nParameters,
		boolean bHasReturn)
	{
		// Checks the cached list of procedures first.
		if (m_pProcedures.containsKey(strServiceType))
			return (String) m_pProcedures.get(strServiceType);

		// Local variables.
		String strProc = "{";

		if (bHasReturn)
			strProc = strProc + "? = ";

		strProc = strProc + "call " + getPrefix() + "_" + getTableName() +
			getSeparator() + strServiceType;

		if (0 < nParameters)
		{
			strProc = strProc + "(";

			for (int i = 0; i < nParameters; i++)
			{
				if (0 < i) strProc = strProc + ", ";
				strProc = strProc + "?";
			}

			strProc = strProc + ")";
		}

		// Close the procedure call.
		strProc = strProc + "}";

		// Put into cache first.
		m_pProcedures.put(strServiceType, strProc);

		return strProc;
	}

	/** Returns a stored procedure name based on the Prefix, Table name, Separator,
	    the type of service, and number of parameters.
		@param strServiceType Represents the action the stored procedure will
			perform. Examples include Add, Store, Remove ... .
		@param nParameters Represents the number of parameters.
		@return Returns a string.
	*/
	private String createProc(String strServiceType, int nParameters)
	{ return createProc(strServiceType, nParameters, false); }

	/** Returns a stored procedure name based on the Prefix, Table name, Separator,
	    the type of service, and number of parameters.
		@param strServiceType Represents the action the stored procedure will
			perform. Examples include Add, Store, Remove ... .
		@param bHasReturn Does the stored procedure return a value.
		@return Returns a string.
	*/
	private String createProc(String strServiceType, boolean bHasReturn)
	{ return createProc(strServiceType, 0, bHasReturn); }

	/** Returns a stored procedure name based on the Prefix, Table name, Separator,
	    the type of service, and number of parameters.
		@param strServiceType Represents the action the stored procedure will
			perform. Examples include Add, Store, Remove ... .
		@return Returns a string.
	*/
	private String createProc(String strServiceType)
	{ return createProc(strServiceType, 0, false); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Caches the generated procedure name after the first time. */
	private Hashtable m_pProcedures = new Hashtable();
}
