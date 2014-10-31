package com.small.library.log4j;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.ThrowableInformation;

import com.small.library.data.ConnectionFactory;

/***********************************************************************************
*
*	Appender class that logs messages to a JDBC data store. Both
*	constructors accepts a <I>ConnectionFactory</I> object. The data store
*	that the <I>ConnectionFactory</I> object represents must contain a single
*	stored procedure with the name set by the StoredProcedureName mutator.
*	The stored procedure parameters are defined as follows:
*
*	<OL>
*		<LI>log_date DateTime</LI>
		<LI>application_name VarChar(128)</LI>
*		<LI>machine_name VarChar(128)</LI>
*		<LI>thread_name VarChar(64)</LI>
*		<LI>class_name VarChar(128)</LI>
*		<LI>method_name VarChar(128)</LI>
*		<LI>priority_name VarChar(16)</LI>
*		<LI>message LongVarChar (Text, CLOB, Memo, ...)</LI>
*	</OL>
*
*	The parameters must be defined in the order specified above. The
*	implementation of the stored procedure is independent of this class.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 7/11/2001
*
**********************************************************************************/

public class JDBCAppender extends AppenderSkeleton
{
	/*************************************************************************
	*
	*	Constants
	*
	*************************************************************************/

	/** Constant - default application name. */
	public static final String DEFAULT_APPLICATION_NAME = "Unknown";

	/*************************************************************************
	*
	*	Constructors
	*
	*************************************************************************/

	/** Constructor - Accepts a <I>Connection</I> object and a
	    stored procedure name.
		@param pConnectionFactory <I>ConnectionFactory</I> object used for logging.
		@param strApplicationName name of the application. Required part of the log entry.
		@param strStoredProcedureName name of the stored procedured
			used for logging.
	*/
	public JDBCAppender(ConnectionFactory pConnectionFactory,
		String strApplicationName, String strStoredProcedureName)
		throws UnknownHostException
	{
		setConnectionFactory(pConnectionFactory);
		setApplicationName(strApplicationName);
		setStoredProcedureName(strStoredProcedureName);

		m_strMachineName = InetAddress.getLocalHost().getHostName();
	}

	/** Constructor - Accepts a <I>Connection</I> object and a
	    stored procedure name.
		@param pConnection JDBC <I>Connection</I> object used for logging.
		@param strApplicationName name of the application. Required part of the log entry.
	*/
	public JDBCAppender(ConnectionFactory pConnectionFactory,
		String strApplicationName)
		throws UnknownHostException
	{ this(pConnectionFactory, strApplicationName, null); }

	/*************************************************************************
	*
	*	Action methods
	*
	*************************************************************************/

	/** Action method - Called by the parent class when a logging event
	    is generated.
		@param pLoggingEvent Reference to the <I>LoggingEvent</I>
			object that contains the event information.
	*/
	protected void append(LoggingEvent pLoggingEvent)
	{
		if (null == m_strStoredProcedureName)
			return;

		ErrorHandler pErrorHandler = getErrorHandler();
		LocationInfo pLocationInfo = pLoggingEvent.getLocationInformation();
		ThrowableInformation pThrowableInformation = pLoggingEvent.getThrowableInformation();
		String strClassName = null;
		String strMethodName = null;
		String strStackTrace = null;
		String strMessage = pLoggingEvent.getRenderedMessage();

		// Get class name and method name if available. Static methods may not
		// provide this information.
		if (pLocationInfo != null)
		{
			strClassName = pLocationInfo.getClassName();
			strMethodName = pLocationInfo.getMethodName();
		}

		// Get stack trace information if available.
		if (pThrowableInformation != null)
		{
			Throwable pThrowable = pThrowableInformation.getThrowable();

			if (null != pThrowable)
			{
				CharArrayWriter pCharArrayWriter = new CharArrayWriter();
				PrintWriter pWriter = new PrintWriter(pCharArrayWriter);
				pThrowable.printStackTrace(pWriter);
				strStackTrace = pCharArrayWriter.toString();
			}
		}

		// Create the message.
		if (strMessage == null)
			strMessage = "No Message";

		if (strStackTrace != null)
			strMessage+= "\n\nStack Trace:\n\n" + strStackTrace;

		try
		{
			PreparedStatement pStmt = getConnectionFactory().getConnection().
				prepareStatement(m_strProcedureCall);

			pStmt.setTimestamp(1, new Timestamp(pLoggingEvent.timeStamp));
			setStringParameter(pStmt, 2, m_strApplicationName);
			setStringParameter(pStmt, 3, m_strMachineName);
			setStringParameter(pStmt, 4, pLoggingEvent.getThreadName());
			setStringParameter(pStmt, 5, strClassName);
			setStringParameter(pStmt, 6, strMethodName);
			setStringParameter(pStmt, 7, pLoggingEvent.level.toString());
			setStringParameter(pStmt, 8, strMessage);

			pStmt.executeUpdate();
		}

		catch (SQLException pEx)
		{
			m_LastException = pEx;

			if (null != pErrorHandler)
				pErrorHandler.error(getFullException(pEx));
		}
	}

	/** Action method - Called by the parent class when the appender is
	    being shutdown.
	*/
	public void close() {}

	/** Action method - Indicates to the parent class whether a layout is
	    required.
	*/
	public boolean requiresLayout() { return false; }

	/*************************************************************************
	*
	*	Accessor methods
	*
	*************************************************************************/

	/** Accessor method - gets the <I>ConnectionFactory</I> object. */
	public ConnectionFactory getConnectionFactory() { return m_ConnectionFactory; }

	/** Accessor method - gets the Application Name property. */
	public String getApplicationName() { return m_strApplicationName; }

	/** Accessor method - gets the Stored Procedure Name property. */
	public String getStoredProcedureName() { return m_strStoredProcedureName; }

	/** Accessor method - gets the name of the machine (host). */
	public String getMachineName() { return m_strMachineName; }

	/** Accessor method - gets the Last Exception property. This is the last
	    exception during an <CODE>append</CODE> method invocation.
	*/
	public Exception getLastException() { return m_LastException; }

	/*************************************************************************
	*
	*	Mutator methods
	*
	*************************************************************************/

	/** Mutator method - sets the JDBC <I>ConnectionFactory</I> object. */
	public void setConnectionFactory(ConnectionFactory pNewValue) { m_ConnectionFactory = pNewValue; }

	/** Mutator method - sets the Application Name property. */
	public void setApplicationName(String strNewValue)
	{
		if (null == strNewValue)
			m_strApplicationName = DEFAULT_APPLICATION_NAME;
		else
			m_strApplicationName = strNewValue;
	}

	/** Mutator method - sets the Stored Procedure Name property. */
	public void setStoredProcedureName(String strNewValue)
	{
		m_strStoredProcedureName = strNewValue;
		m_strProcedureCall = "{call " + m_strStoredProcedureName +
			"(?, ?, ?, ?, ?, ?, ?, ?)}";
	}

	/*************************************************************************
	*
	*	Helper methods
	*
	*************************************************************************/

	/** Helper method - sets a <I>String</I> parameter. Uses conditional
	    logic to handle <CODE>null</CODE> values appropriately.
		@param pStmt JDBC <I>PreparedStatement</I> object.
		@param nParameter Ordinal of the parameter being set.
		@param strValue Parameter value.
	*/
	private void setStringParameter(PreparedStatement pStmt,
		int nParameter, String strValue) throws SQLException
	{
		if (null == strValue)
			pStmt.setNull(nParameter, java.sql.Types.VARCHAR);

		else
			pStmt.setString(nParameter, strValue);
	}

	/** Helper method - gets the full SQLException message. SQLException
	    objects can have nested SQLException objects.
		@param pException <I>SQLException</I> to iterate through.
	*/
	private String getFullException(SQLException pException)
	{
		String strMessage = pException.getMessage();
		SQLException pNextException = pException;

		while (null != (pNextException = pNextException.getNextException()))
			strMessage+= "\n\n" + pNextException.getMessage();

		return strMessage;
	}

	/*************************************************************************
	*
	*	Member variables
	*
	*************************************************************************/

	/** Member variable - contains the JDBC <I>ConnectionFactory</I> used for logging. */
	private ConnectionFactory m_ConnectionFactory = null;

	/** Member variable - contains the Application Name property. */
	private String m_strApplicationName = null;

	/** Member variable - contains the stored procedure name used for logging. */
	private String m_strStoredProcedureName = null;

	/** Member variable - contains the call statement made to the JDBC <I>Connection</I>
	    object.
	*/
	private String m_strProcedureCall = null;

	/** Member variable - contains the name of the machine (host). */
	private String m_strMachineName = null;

	/** Member variable - contains the last exception thrown during an
	    <CODE>append</CODE> method invocation.
	*/
	private Exception m_LastException = null;

	/*************************************************************************
	*
	*	Driver method
	*
	*************************************************************************/

	/** Driver method - main entry point for testing. */
	public static void main(String[] strArgs)
	{
		try
		{
			org.apache.log4j.Category pCategory =
				org.apache.log4j.Category.getInstance(JDBCAppender.class.getPackage().getName());

			com.small.library.data.DataSource pDataSource = new com.small.library.data.DataSource(
				strArgs[0], strArgs[1], strArgs[2], strArgs[3]);

			JDBCAppender pAppender = new JDBCAppender(pDataSource.getConnectionPool(), "JDBC Appender Test", strArgs[4]);

			pCategory.addAppender(pAppender);

			pCategory.debug("A Debug Message");
			pCategory.info("A Info Message");
			pCategory.warn("A Warning Message");
			pCategory.error("An Error Message");
			pCategory.fatal("A Fatal Message");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
