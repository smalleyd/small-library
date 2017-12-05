package com.small.library.generator;

import java.io.*;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

/************************************************************************************************
*
*	Base class for all code generators. Supplies some basic functionality.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 12/23/2000
*
************************************************************************************************/

public abstract class Base
{
	public static final String JDBC_METHOD_SUFFIX_LONG = "Long";
	public static final String JDBC_METHOD_SUFFIX_INTEGER = "Int";
	public static final String JDBC_METHOD_SUFFIX_SMALLINT = "Short";

	public static final String JAVA_TYPE_LONG = "long";
	public static final String JAVA_TYPE_INTEGER = "int";
	public static final String JAVA_TYPE_SMALLINT = "short";

	public static final String JAVA_OBJECT_TYPE_LONG = "Long";
	public static final String JAVA_OBJECT_TYPE_INTEGER = "Integer";
	public static final String JAVA_OBJECT_TYPE_SMALLINT = "Short";

	public static final String JDBC_TYPE_LONG = "BIGINT";
	public static final String JDBC_TYPE_INTEGER = "INTEGER";
	public static final String JDBC_TYPE_SMALLINT = "SMALLINT";

	public static final String AUTHOR_DEFAULT = System.getProperty("user.name");
	public static final String PREFIX_INTEGER = "n";
	public static final String PREFIX_SMALLINT = PREFIX_INTEGER;
	public static final String PREFIX_LONG = "l";
	public static final String PREFIX_BOOLEAN = "b";
	public static final String PREFIX_FLOAT = "f";
	public static final String PREFIX_DOUBLE = "dbl";
	public static final String PREFIX_STRING = "str";
	public static final String PREFIX_DATE = "dte";
	public static final String PREFIX_OBJECT_REFERENCE = "p";

	/******************************************************************************
	*
	*	Static members
	*
	******************************************************************************/

	/** Static member - set of primitive data types. Used to determine
	    whether a java type is a primitive or an object.
	*/
	private static Map<String, String> PRIMITIVES = null;

	/** Static constructor - initializes static member variables. */
	static
	{
		PRIMITIVES = new HashMap<String, String>();

		PRIMITIVES.put(JAVA_TYPE_INTEGER, JAVA_OBJECT_TYPE_INTEGER);
		PRIMITIVES.put(JAVA_TYPE_LONG, JAVA_OBJECT_TYPE_LONG);
		PRIMITIVES.put(JAVA_TYPE_SMALLINT, JAVA_OBJECT_TYPE_SMALLINT);
		PRIMITIVES.put("byte", "Byte");
		PRIMITIVES.put("boolean", "Boolean");
		PRIMITIVES.put("float", "Float");
		PRIMITIVES.put("double", "Double");
		PRIMITIVES.put("char", "Character");
	}

	private PrintWriter writer;
	public final String author;

	/** Constructor - constructs an empty object. */
	public Base() { this(null, AUTHOR_DEFAULT); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
	*/
	public Base(final PrintWriter writer, final String author)
	{
		this.writer = writer;
		this.author = author;
	}

	/**************************************************************************************
	*
	*	Abstract methods
	*
	**************************************************************************************/

	/** Abstract method - subclass implementation of the generation process. */
	public abstract void generate() throws GeneratorException, IOException;

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets output stream. */
	public PrintWriter getWriter() { return writer; }

	/** Accessor method - gets the author name. */
	public String getAuthor() { return author; }

	/** Accessor method - gets the current date and time. */
	public java.util.Date getNow() { return new java.util.Date(); }

	/** Accessor method - gets the current date as a <I>String</I> in short
	    format.
	*/
	public String getDateString()
	{
		return DateFormat.getDateInstance(DateFormat.LONG).format(getNow());
	}

	public void setWriter(final PrintWriter newValue) { writer = newValue; }

	/**************************************************************************************
	*
	*	Helper methods
	*
	**************************************************************************************/

	/** Helper method - Indicates whether the supplied data type is primitive.
		@param strJavaType short or long version of java type name.
	*/
	public boolean isPrimitive(String strJavaType)
	{
		return PRIMITIVES.containsKey(strJavaType);
	}

	/** Helper method - converts a primitive type to an object type. */
	public String fromPrimitiveToObject(String javaType)
	{
		return (String) PRIMITIVES.get(javaType);
	}

	/** Output the specified number of tabs into the stream. */
	public void write(int tabs) throws IOException
	{
		for (int i = 0; i < tabs; i++)
			writer.write('\t');
	}

	/** Writes a <I>String</I> to the output stream.
		@param strValue String value.
	*/
	protected void write(String strValue) throws IOException { writer.print(strValue); }

	/** Writes a <I>String</I> to the output stream.
		@param strValue String value.
		@param tabs Number of tabs to prefix the input text with.
	*/
	protected void write(String strValue, int tabs) throws IOException { write(tabs); writer.print(strValue); }

	/** Writes a new line to the output stream. */
	protected void writeLine() throws IOException { writer.println(); }

	/** Writes a <I>String</I> plus a line separator to the output stream.
		@param strValue String value.
	*/
	protected void writeLine(String strValue) throws IOException { writer.println(strValue); }

	/** Writes a <I>String</I> plus a line separator to the output stream.
		@param value String value.
		@param tabs Number of tabs to prefix the line with.
		
	*/
	protected void writeLine(String value, int tabs) throws IOException { write(tabs); writer.println(value); }

	/** Helper method - converts names with underscores (or other whitespace)
	    to proper cased names.
	    An example would be "user_group" to "UserGroup".
		@param value String value.
		@return the same string without the underscores and the beginning
			words capitalized.
	*/
	public static String createObjectName(String value)
	{
		// Validate the input value.
		if ((null == value) || (0 == value.length()))
			return value;

		// If no underscores, then assume using Java naming convention in the db.
		if (0 > value.indexOf("_"))
		{
			char[] chars = value.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);

			return new String(chars);
		}

		// Create working value that is all lower case.
		StringBuffer strWorkValue = new StringBuffer(value.toLowerCase());
		int nLength = value.length();

		// Used to upper case the first character after the removed
		// underscore. Also, used to upper case the first character of the
		// string.
		boolean bUpperCaseNextCharacter = true;

		// Loop through the characters and perform the conversion.
		for (int i = 0; i < nLength; i++)
		{
			char charCurrent = strWorkValue.charAt(i);

			// Remove the character if an underscore.
			if ((charCurrent == '_') || Character.isWhitespace(charCurrent))
			{
				strWorkValue.deleteCharAt(i);
				nLength--;
				i--;
				bUpperCaseNextCharacter = true;
			}

			else if (bUpperCaseNextCharacter && Character.isLowerCase(charCurrent))
			{
				strWorkValue.setCharAt(i, Character.toUpperCase(charCurrent));
				bUpperCaseNextCharacter = false;
			}
		}

		return strWorkValue.toString();
	}

	/******************************************************************************
	*
	*	Helper methods: Used by class entry methods (i.e. main)
	*
	*****************************************************************************/

	/** Helper method - extracts the argument from the command line
	    arguments specified by nArgument. If the argument does not exist or
	    is blank, the default is returned
		@param strArgs An array of command line arguments.
		@param nArgument Index in the array that indicates the argument.
		@param strDefault Default argument.
	*/
	protected static String extractArgument(String[] strArgs,
		int nArgument, String strDefault)
	{
		if ((strArgs.length > nArgument) && (0 < strArgs[nArgument].length()))
			return strArgs[nArgument];

		return strDefault;
	}

	/** Helper method - extracts the a file from the command line
	    arguments. Before returning the output file, it is validated
	    to be sure that it is a file. If the argument does not
	    exist, <CODE>null</CODE> is returned. It does not validate
	    the existence of the file, because most times the file is
	    being created.
		@param strArgs An array of command line arguments.
		@param nArgument Index in the array that indicates the output file.
		@param strName Name of the file used in exceptions thrown.
		@return a <I>File</I> object.
		@throws IllegalArgumentException thrown when the file does not
			exist or is not in fact a file.
	*/
	protected static File extractFile(String[] strArgs,
		int nArgument, String strName) throws IllegalArgumentException
	{
		// Does the value exist?
		if ((strArgs.length <= nArgument) || (0 == strArgs[nArgument].length()))
			return null;

		// Local variables
		File file = new File(strArgs[nArgument]);

		if (file.isDirectory())
			throw new IllegalArgumentException("The " + strName + " file supplied is not a file.");

		return file;
	}

	/** Helper method - extracts the a directory from the command line
	    arguments. Before returning the output directory, it is validated
	    to be sure that it exists and is a directory. If the argument does not
	    exist, <CODE>null</CODE> is returned.
		@param strArgs An array of command line arguments.
		@param nArgument Index in the array that indicates the output directory.
		@param strName Name of the directory used in exceptions thrown.
		@return a <I>File</I> object.
		@throws IllegalArgumentException thrown when the directory does not
			exist or is not in fact a directory.
	*/
	protected static File extractDirectory(String[] strArgs,
		int nArgument, String strName) throws IllegalArgumentException
	{
		// Does the value exist?
		if ((strArgs.length <= nArgument) || (0 == strArgs[nArgument].length()))
			return null;

		// Local variables
		File fileDir = new File(strArgs[nArgument]);

		// Make sure the directory exists.
		if (!fileDir.exists())
			throw new IllegalArgumentException("The " + strName + " directory does not exist.");

		if (!fileDir.isDirectory())
			throw new IllegalArgumentException("The " + strName + " directory supplied is not a directory.");

		return fileDir;
	}

	/** Helper method - extracts the output directory from the command line
	    arguments. Before returning the output directory, it is validated
	    to be sure that it exists and is a directory. If the argument does not
	    exist, <CODE>null</CODE> is returned.
		@param strArgs An array of command line arguments.
		@param nArgument Index in the array that indicates the output directory.
		@return a <I>File</I> object.
		@throws IllegalArgumentException thrown when the directory does not
			exist or is not in fact a directory.
	*/
	protected static File extractOutputDirectory(String[] strArgs,
		int nArgument) throws IllegalArgumentException
	{
		return extractDirectory(strArgs, nArgument, "output");
	}

	/** Helper method - gets a Data Source object based on the command line
	    arguments.
		@param strArgs An array of command line arguments.
		@param nFirstArgument Index in the array that indicates the first
			data source argument. The argument order should be
			URL, User ID, Password, & Driver.
	*/
	protected static DataSource extractDataSource(String[] strArgs,
		int nFirstArgument) throws IllegalArgumentException
	{
		if (strArgs.length < nFirstArgument + 2)
			throw new IllegalArgumentException("Missing JDBC_URL or User_ID argument.");

		String strUrl = strArgs[nFirstArgument];
		String strUserName = strArgs[++nFirstArgument];
		String strPassword = extractArgument(strArgs, ++nFirstArgument, null);
		String strDriver = "sun.jdbc.odbc.JdbcOdbcDriver";

		if ((++nFirstArgument < strArgs.length) && (0 < strArgs[nFirstArgument].length()))
			strDriver = strArgs[nFirstArgument];
		else
			strUrl = "jdbc:odbc:" + strUrl;

		// Create the data source.
		return createDataSource(strDriver, strUrl, strUserName, strPassword);
	}

	/** Helper method - creates JDBC DataSource object. */
	public static DataSource createDataSource(final String driver, final String url, final String userName, final String password)
	{
		final BasicDataSource value = new BasicDataSource();
		value.setDriverClassName(driver);
		value.setUrl(url);
		value.setUsername(userName);
		value.setPassword(password);

		return value;
	}

	/** Helper method - extracts the author name from the command line
	    arguments. If it is not found, the default is returned.
		@param strArgs An array of command line arguments.
		@param nArgument Index in the array that indicates the author.
	*/
	protected static String extractAuthor(String[] strArgs,
		int nArgument)
	{
		return extractArgument(strArgs, nArgument, AUTHOR_DEFAULT);
	}
}
