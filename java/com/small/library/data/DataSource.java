package com.small.library.data;

import java.sql.*;
import java.util.Properties;

/***************************************************************************************
*
*	Handles creation of JDBC <I>Connection</I> object based on specific data
*	source information.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 8/26/2000
*
***************************************************************************************/

public class DataSource
{
	/*******************************************************************************
	*
	*	Constants
	*
	*******************************************************************************/

	/** Constant - property key for the data source driver. */
	public static final String PROP_KEY_DRIVER = "jdbc.driver";

	/** Constant - property key for the data source URL. */
	public static final String PROP_KEY_URL = "jdbc.url";

	/** Constant - property key for the data source user name. */
	public static final String PROP_KEY_USERNAME = "jdbc.username";

	/** Constant - property key for the data source password. */
	public static final String PROP_KEY_PASSWORD = "jdbc.password";

	/** Constant - property key for the driver's user name. */
	public static final String PROP_KEY_DRIVER_USERNAME = "user";

	/** Constant - property key for the driver's password. */
	public static final String PROP_KEY_DRIVER_PASSWORD = "password";

	/*******************************************************************************
	*
	*	Constructors
	*
	*******************************************************************************/

	/** Constructor - accepts a <I>Properties</I> object the data source values.
		@param pProperties Property map that contains the data source values.
	*/
	public DataSource(Properties pProperties)
		throws DataSourceException
	{
		set(pProperties.getProperty(PROP_KEY_DRIVER),
			pProperties.getProperty(PROP_KEY_URL),
			pProperties.getProperty(PROP_KEY_USERNAME),
			pProperties.getProperty(PROP_KEY_PASSWORD));
	}

	/** Constructor - requires all the data source parameters.
		@param strDriver The driver class name.
		@param strURL The data source URL.
		@param strUserName Login name to the data source.
		@param strPassword Password to the data source.
	*/
	public DataSource(String strDriver,
		String strURL,
		String strUserName,
		String strPassword)
			throws DataSourceException
	{
		m_ConnectionInfo = new Properties();
		set(strDriver, strURL, strUserName, strPassword);
	}

	/*******************************************************************************
	*
	*	Main functionality
	*
	*******************************************************************************/

	/** Creates a new JDBC <I>Connection</I> object. */
	public Connection getConnection() throws SQLException
	{
		if ((null == m_strUserName) || (0 == m_strUserName.length()))
			return DriverManager.getConnection(m_strURL);
		else
			return DriverManager.getConnection(m_strURL, m_strUserName, m_strPassword);
	}

	/** Mutator method - gets a <I>ConnectionPool</I> valid for the data source.
		@return a <I>ConnectionPool</I> object.
	*/
	public ConnectionPool getConnectionPool()
		throws SQLException
	{
		return ConnectionPool.getInstance(this);
	}

	/** Mutator method - gets a <I>ConnectionPool</I> valid for the data source.
		@param nInitialCapacity The initial number of <I>Connection</I> objects
			in the pool.
		@return a <I>ConnectionPool</I> object.
	*/
	public ConnectionPool getConnectionPool(int nInitialCapacity)
		throws SQLException
	{
		return ConnectionPool.getInstance(this, nInitialCapacity);
	}

	/** Validates whether the DataSource can retrieve a valid JDBC <I>Connection</I>
	    object.
	*/
	public void validate() throws SQLException
	{
		Connection pConnection = getConnection();

		// If it was a bad connection, an exception would have been thrown.
		pConnection.close();
	}

	/** Indicates whether one DataSource object equals another. */
	public boolean equals(Object pValue)
	{
		if ((null == pValue) || (!(pValue instanceof DataSource)))
			return false;

		return m_strID.equals(((DataSource) pValue).m_strID);
	}

	/*******************************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************************/

	/** Accessor - gets a name of the Driver class. */
	public Driver getDriver() { return m_Driver; }

	/** Accessor - gets the data source URL. */
	public String getURL() { return m_strURL; }

	/** Accessor - gets the login name to the data source. */
	public String getUserName() { return m_strUserName; }

	/** Accessor - gets the password to the data source. */
	public String getPassword() { return m_strPassword; }

	/** Accessor - implementation of <I>toString</I> method. */
	public String toString() { return m_strID; }

	/** Accessor - implementation of the <I>hashCode</I> method. */
	public int hashCode() { return m_nHashCode; }

	/*******************************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************************/

	/** Mutator - sets the name of the Driver class. */
	private void setDriver(String strValue)
		throws DataSourceException
	{
		try
		{
			DriverManager.registerDriver(
				m_Driver = (Driver) Class.forName(strValue).newInstance());
		}

		catch (Exception pEx) { throw new DataSourceException(pEx); }
	}

	/** Mutator - sets the data source URL. */
	private void setURL(String strValue) { m_strURL = strValue; }

	/** Mutator - sets the login name to the data source. */
	private void setUserName(String strValue) { m_strUserName = strValue; }

	/** Mutator - sets the password to the data source. */
	private void setPassword(String strValue) { m_strPassword = strValue; }

	/** Mutator - sets all properties.
		@param strDriver The JDBC Driver class name.
		@param strURL The JDBC URL to the data source.
		@param strUserName Login name to the data source.
		@param strPassword Password of the login to the data source.
	*/
	private void set(String strDriver, String strURL, String strUserName,
		String strPassword)
			throws DataSourceException
	{
		setDriver(strDriver);
		setURL(strURL);
		setUserName(strUserName);
		setPassword(strPassword);

		m_strID = strDriver + ";" + m_strURL + ";" +
			strUserName + ";" + strPassword;

		m_nHashCode = m_strID.hashCode();
	}

	/*******************************************************************************
	*
	*	Member variables
	*
	*******************************************************************************/

	/** Member variable - reference to the JDBC <I>Driver</I> class. */
	private Driver m_Driver = null;

	/** Member variable - contains the JDBC Connection URL. */
	private String m_strURL = null;

	/** Member varaible - contains the login name to the data source. */
	private String m_strUserName = null;

	/** Member variable - contains the password to the data source. */
	private String m_strPassword = null;
	
	/** Member variable - contains the JDBC Connection information. */
	private Properties m_ConnectionInfo = null;

	/** Member variable - contains the Data Source ID. */
	private String m_strID = null;

	/** Member variable - hash code of the Data Source. */
	private int m_nHashCode = 0;
}
