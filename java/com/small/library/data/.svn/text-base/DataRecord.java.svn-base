package com.small.library.data;

import java.sql.*;

/***************************************************************************************
*
*	Parent class for individual data record objects that map to the various
*	database tables and views.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public abstract class DataRecord {

	/******************************************************************************
	*
	* Constructors/Destructors
	*
	******************************************************************************/

	/** Constructor - default. */
	public DataRecord() {}

	/** Constructor - used for cloning.
		@param pRecord A reference to a <I>DataRecord</I> object for cloning.
	*/
	protected DataRecord(DataRecord pRecord)
	{
		m_bIsNewRecord = pRecord.m_bIsNewRecord;
		m_bIsDirty = pRecord.m_bIsDirty;
	}

	/******************************************************************************
	*
	* Implementation of the Object class
	*
	******************************************************************************/

	/** Default implementation of the Object.toString method. */
	public String toString() { return null; }

	/******************************************************************************
	*
	* Data access and manipulation methods
	*
	******************************************************************************/

	/** Accepts a JDBC resultset object and retrieves the current rowset's
	    values.
		@param pResultSet JDBC ResultSet object.
	*/
	public void fetch(ResultSet pResultSet) throws SQLException {}

	/** Accepts a JDBC resultset object and retrieves additional information
	    about a record that was not retrieved during the normal fetch. This
	    may include larger data fields such as long character or binary data.
		@param pResultSet JDBC ResultSet object.
	*/
	public void fetchMore(ResultSet pResultSet) throws SQLException {}

	/** Accepts a JDBC prepared statement object, sets its parameters,
	    and executes the statement.
		@param pStmt JDBC PreparedStatement object.
	*/
	public void update(PreparedStatement pStmt) throws SQLException {}

	/** Accepts a JDBC prepared statement object, sets its parameters,
	    and executes the statement.
		@param pStmt JDBC PreparedStatement object.
	*/
	public void insert(PreparedStatement pStmt) throws SQLException {}

	/** Accepts a JDCB prepared statement object, sets its parameters,
	    and executes the statement.
		@param pStmt JDBC PreparedStatement object.
	*/
	public void delete(PreparedStatement pStmt) throws SQLException
	{ bindID(pStmt, 1); pStmt.executeUpdate(); }

	/******************************************************************************
	*
	* Parameter binding methods
	*
	******************************************************************************/

	/** Binds the record's primary key value to the JDBC prepared statement object.
		@param pStmt JDBC PreparedStatement object.
		@param nParam The parameter position to bind the primary key value to.
		@return Returns the next position for the calling code to use.
	*/
	public int bindID(PreparedStatement pStmt, int nParam)
		throws SQLException
	{ return nParam; }

	/** Binds the record's unique descriptor to the JDBC prepared
	        statement object.
		@param pStmt JDBC PreparedStatement object.
		@param nParam The parameter position to bind the unique descriptor to.
		@return Returns the next position for the calling code to use.
	*/
	public int bindDesc(PreparedStatement pStmt, int nParam)
		throws SQLException
	{ return nParam; }

	/******************************************************************************
	*
	* Property editing methods
	*
	******************************************************************************/

	/** Checks current property against the new property value.
		@param bValue The property's current value.
		@param bNewValue The property's new value.
		@return Indicates whether the Dirty property was set to true.
	*/
	protected boolean editProperty(boolean bValue, boolean bNewValue)
	{
		if (bValue == bNewValue)
			return false;

		isDirty(true);
		return true;
	}

	/** Checks current property against the new property value.
		@param nValue The property's current value.
		@param nNewValue The property's new value.
		@return Indicates whether the Dirty property was set to true.
	*/
	protected boolean editProperty(int nValue, int nNewValue)
	{
		if (nValue == nNewValue)
			return false;

		isDirty(true);
		return true;
	}

	/** Checks current property against the new property value.
		@param lValue The property's current value.
		@param lNewValue The property's new value.
		@return Indicates whether the Dirty property was set to true.
	*/
	protected boolean editProperty(long lValue, long lNewValue)
	{
		if (lValue == lNewValue)
			return false;

		isDirty(true);
		return true;
	}

	/** Checks current property against the new property value.
		@param fValue The property's current value.
		@param fNewValue The property's new value.
		@return Indicates whether the Dirty property was set to true.
	*/
	protected boolean editProperty(float fValue, float fNewValue)
	{
		if (fValue == fNewValue)
			return false;

		isDirty(true);
		return true;
	}

	/** Checks current property against the new property value.
		@param dblValue The property's current value.
		@param dblNewValue The property's new value.
		@return Indicates whether the Dirty property was set to true.
	*/
	protected boolean editProperty(double dblValue, double dblNewValue)
	{
		if (dblValue == dblNewValue)
			return false;

		isDirty(true);
		return true;
	}

	/** Checks current property against the new property value.
		@param pValue The property's current value.
		@param pNewValue The property's new value.
		@return Indicates whether the Dirty property was set to true.
	*/
	protected boolean editProperty(Object pValue, Object pNewValue)
	{
		if (null == pValue)
		{
			if (null == pNewValue)
				return false;
		}

		else if (pValue.equals(pNewValue))
			return false;

		isDirty(true);
		return true;
	}

	/******************************************************************************
	*
	* Parameter setting helper methods
	*
	******************************************************************************/

	/** Sets an <I>int</I> parameter. If <I>nValue</I> is zero (0), then
	    setNull is called instead.
		@param pStmt A reference to a JDBC PreparedStatement object.
		@param nParam The order in the parameter list to bind the <I>int</I>.
		@param nValue The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement pStmt, int nParam, int nValue)
		throws SQLException
	{
		if (0 == nValue)
			pStmt.setNull(nParam, java.sql.Types.INTEGER);
		else
			pStmt.setInt(nParam, nValue);
	}

	/** Sets a <I>short</I> parameter. If <I>nValue</I> is zero (0), then
	    setNull is called instead.
		@param pStmt A reference to a JDBC PreparedStatement object.
		@param nParam The order in the parameter list to bind the <I>short</I>.
		@param nValue The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement pStmt, int nParam, short nValue)
		throws SQLException
	{
		if (0 == nValue)
			pStmt.setNull(nParam, java.sql.Types.SMALLINT);
		else
			pStmt.setShort(nParam, nValue);
	}

	/** Sets a <I>byte</I> parameter. If <I>nValue</I> is zero (0), then
	    setNull is called instead.
		@param pStmt A reference to a JDBC PreparedStatement object.
		@param nParam The order in the parameter list to bind the <I>byte</I>.
		@param nValue The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement pStmt, int nParam, byte nValue)
		throws SQLException
	{
		if (0 == nValue)
			pStmt.setNull(nParam, java.sql.Types.TINYINT);
		else
			pStmt.setByte(nParam, nValue);
	}

	/** Sets a <I>String</I> parameter. If <I>strValue</I> is NULL, then
	    setNull is called instead.
		@param pStmt A reference to a JDBC PreparedStatement object.
		@param nParam The order in the parameter list to bind the <I>String</I>.
		@param strValue The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement pStmt, int nParam, String strValue)
		throws SQLException
	{ setParam(pStmt, nParam, strValue, java.sql.Types.VARCHAR); }

	/** Sets a <I>String</I> parameter. If <I>strValue</I> is NULL, then
	    setNull is called instead.
		@param pStmt A reference to a JDBC PreparedStatement object.
		@param nParam The order in the parameter list to bind the <I>String</I>.
		@param strValue The value to bind to the parameter.
		@param nParamType The java.sql.Types that represents the parameter.
	*/
	protected void setParam(PreparedStatement pStmt, int nParam, String strValue,
		int nParamType) throws SQLException
	{
		if (null == strValue)
			pStmt.setNull(nParam, nParamType);
		else
			pStmt.setString(nParam, strValue);
	}

	/** Sets a <I>Timestamp</I> parameter. If <I>dteValue</I> is NULL, then
	    setNull is called instead.
		@param pStmt A reference to the JDBC PreparedStatement object.
		@param nParam The order in the parameter list to bind the <I>Timestamp</I>.
		@param dteValue The value to bind to the parameter.
	*/
	protected void setParam(PreparedStatement pStmt, int nParam, Timestamp dteValue)
		throws SQLException
	{
		if (null == dteValue)
			pStmt.setNull(nParam, java.sql.Types.TIMESTAMP);
		else
			pStmt.setTimestamp(nParam, dteValue);
	}

	/******************************************************************************
	*
	* Accessor methods
	*
	******************************************************************************/

	/** Returns whether the record's value is inconsistent with the database
	    table or view that it represents. */
	public boolean isDirty() { return m_bIsDirty; }

	/** Returns whether the record exists in the database table or view
	    that is represents. */
	public boolean isNewRecord() { return m_bIsNewRecord; }

	/** Returns whether the record is "Clean". The record is clean if the record
	    is both not dirty and not new. */
	public boolean isClean() { return (!isDirty() && !isNewRecord()); }

	/******************************************************************************
	*
	* Mutator methods
	*
	******************************************************************************/

	/** Mutator method for the Dirty property of the record object. */
	protected void isDirty(boolean bValue) { m_bIsDirty = bValue; }

	/** Mutator method for the "Is New Record" property of the record object. */
	protected void isNewRecord(boolean bValue) { m_bIsNewRecord = bValue; }

	/** Mutator - sets "isDirty" and "isNewRecord" properties to false. */
	protected void clean() { isDirty(false); isNewRecord(false); }

	/******************************************************************************
	*
	* Private member variables
	*
	******************************************************************************/

	/** All records are considered "new" until they are loaded. */
	private boolean m_bIsNewRecord = true;
	private boolean m_bIsDirty     = false;
}
