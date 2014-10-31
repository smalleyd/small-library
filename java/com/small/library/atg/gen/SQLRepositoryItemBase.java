package com.small.library.atg.gen;

import java.io.*;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;

import com.small.library.atg.meta.*;
import com.small.library.generator.*;
import com.small.library.xml.XMLHelper;

/***************************************************************************************
*
*	Base generator class for Dynamo SQL Repository Item helper classes. Due
*	to limitations in the implementation of SQL Repository, helper classes are
*	required as wrappers or value objects to work more effectively with the
*	technology. An example of a limitation is the weak typing of values
*	retrieved by calling <CODE>getPropertyValue</CODE>. Each call returns
*	an <I>Object</I> that must be cast at runtime.
*
*	<BR><BR>
*
*	This base class supports generators that will derive their information from
*	an existing SQL Repository file instead of from the metadata of a database.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/3/2002
*
***************************************************************************************/

public abstract class SQLRepositoryItemBase extends Base
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - default package name of the ATG Dynamo Repository framework.
	    The default value is "com.small.library.atg.repository".
	*/
	public static final String DEFAULT_FRAMEWORK_PACKAGE =
		SQLRepositoryItemWrapper.DEFAULT_FRAMEWORK_PACKAGE;

	/******************************************************************************
	*
	*	Constants - Method suffixes
	*
	******************************************************************************/

	/** Constant - Method suffix for "long". */
	public static final String METHOD_SUFFIX_LONG = "Long";

	/** Constant - Method suffix for "int". */
	public static final String METHOD_SUFFIX_INTEGER = "Int";

	/** Constant - Method suffix for "short". */
	public static final String METHOD_SUFFIX_SMALLINT = "Short";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Static member - contains a mapping of property data type name to
	    method suffix. Used in <CODE>getPropertyMethodSuffix</CODE>.
	*/
	private static Map METHOD_SUFFIXES = null;

	/** Static member - contains a mapping of property data type name to
	    JDBC method suffix. Used by JDBC <I>ResultSet</I> "getters" and
	    <I>PreparedStatement</I> "setters".
	*/
	private static Map JDBC_METHOD_SUFFIXES = null;

	/** Static member - contains a mapping of property data type name to
	    Java data type name. Used in <CODE>getJavaType</CODE>.
	*/
	private static Map JAVA_TYPES = null;

	/** Static member - contains a mapping of property data type name to
	    Java Object data type name. Used in <CODE>getJavaObjectType</CODE>.
	*/
	private static Map JAVA_OBJECT_TYPES = null;

	/** Static member - map of SQL data types (java.sql.Types) to a Hungarian notation
	    variable prefix.
	*/
	private static Map VARIABLE_PREFIXES = null;

	/** Initializes the static members. */
	static
	{
		METHOD_SUFFIXES = new HashMap();

		METHOD_SUFFIXES.put("string", "String");
		METHOD_SUFFIXES.put("big string", "String");
		METHOD_SUFFIXES.put("enumerated", null);
		METHOD_SUFFIXES.put("boolean", "Boolean");
		METHOD_SUFFIXES.put("int", METHOD_SUFFIX_INTEGER);
		METHOD_SUFFIXES.put("short", METHOD_SUFFIX_SMALLINT);
		METHOD_SUFFIXES.put("long", METHOD_SUFFIX_LONG);
		METHOD_SUFFIXES.put("float", "Float");
		METHOD_SUFFIXES.put("double", "Double");
		METHOD_SUFFIXES.put("byte", "Byte");
		METHOD_SUFFIXES.put("binary", "Binary");
		METHOD_SUFFIXES.put("date", "Date");
		METHOD_SUFFIXES.put("timestamp", "Timestamp");
		METHOD_SUFFIXES.put("array", "Array");
		METHOD_SUFFIXES.put("set", "Set");
		METHOD_SUFFIXES.put("list", "List");
		METHOD_SUFFIXES.put("map", "Map");

		JDBC_METHOD_SUFFIXES = new HashMap();

		JDBC_METHOD_SUFFIXES.put("string", "String");
		JDBC_METHOD_SUFFIXES.put("big string", "String");
		JDBC_METHOD_SUFFIXES.put("enumerated", "Object");
		JDBC_METHOD_SUFFIXES.put("boolean", "Boolean");
		JDBC_METHOD_SUFFIXES.put("int", JDBC_METHOD_SUFFIX_INTEGER);
		JDBC_METHOD_SUFFIXES.put("short", JDBC_METHOD_SUFFIX_SMALLINT);
		JDBC_METHOD_SUFFIXES.put("long", JDBC_METHOD_SUFFIX_LONG);
		JDBC_METHOD_SUFFIXES.put("float", "Float");
		JDBC_METHOD_SUFFIXES.put("double", "Double");
		JDBC_METHOD_SUFFIXES.put("byte", "Byte");
		JDBC_METHOD_SUFFIXES.put("binary", "Bytes");
		JDBC_METHOD_SUFFIXES.put("date", "Date");
		JDBC_METHOD_SUFFIXES.put("timestamp", "Timestamp");
		JDBC_METHOD_SUFFIXES.put("array", "Array");
		JDBC_METHOD_SUFFIXES.put("set", "Object");
		JDBC_METHOD_SUFFIXES.put("list", "Object");
		JDBC_METHOD_SUFFIXES.put("map", "Object");

		JAVA_TYPES = new HashMap();

		JAVA_TYPES.put("string", "String");
		JAVA_TYPES.put("big string", "String");
		JAVA_TYPES.put("enumerated", null);
		JAVA_TYPES.put("boolean", "boolean");
		JAVA_TYPES.put("int", JAVA_TYPE_INTEGER);
		JAVA_TYPES.put("short", JAVA_TYPE_SMALLINT);
		JAVA_TYPES.put("long", JAVA_TYPE_LONG);
		JAVA_TYPES.put("float", "float");
		JAVA_TYPES.put("double", "double");
		JAVA_TYPES.put("byte", "byte");
		JAVA_TYPES.put("binary", "byte[]");
		JAVA_TYPES.put("date", "Date");
		JAVA_TYPES.put("timestamp", "Timestamp");
		JAVA_TYPES.put("array", "Object[]");
		JAVA_TYPES.put("set", "Set");
		JAVA_TYPES.put("list", "List");
		JAVA_TYPES.put("map", "Map");

		JAVA_OBJECT_TYPES = new HashMap();

		JAVA_OBJECT_TYPES.put("string", "String");
		JAVA_OBJECT_TYPES.put("big string", "String");
		JAVA_OBJECT_TYPES.put("enumerated", null);
		JAVA_OBJECT_TYPES.put("boolean", "Boolean");
		JAVA_OBJECT_TYPES.put("int", JAVA_OBJECT_TYPE_INTEGER);
		JAVA_OBJECT_TYPES.put("short", JAVA_OBJECT_TYPE_SMALLINT);
		JAVA_OBJECT_TYPES.put("long", JAVA_OBJECT_TYPE_LONG);
		JAVA_OBJECT_TYPES.put("float", "Float");
		JAVA_OBJECT_TYPES.put("double", "Double");
		JAVA_OBJECT_TYPES.put("byte", "Byte");
		JAVA_OBJECT_TYPES.put("binary", "byte[]");
		JAVA_OBJECT_TYPES.put("date", "Date");
		JAVA_OBJECT_TYPES.put("timestamp", "Timestamp");
		JAVA_OBJECT_TYPES.put("array", "Object[]");
		JAVA_OBJECT_TYPES.put("set", "Set");
		JAVA_OBJECT_TYPES.put("list", "List");
		JAVA_OBJECT_TYPES.put("map", "Map");

		VARIABLE_PREFIXES = new HashMap();

		VARIABLE_PREFIXES.put("string", PREFIX_STRING);
		VARIABLE_PREFIXES.put("big string", PREFIX_STRING);
		VARIABLE_PREFIXES.put("enumerated", PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put("boolean", PREFIX_BOOLEAN);
		VARIABLE_PREFIXES.put("int", PREFIX_INTEGER);
		VARIABLE_PREFIXES.put("short", PREFIX_SMALLINT);
		VARIABLE_PREFIXES.put("long", PREFIX_LONG);
		VARIABLE_PREFIXES.put("float", PREFIX_FLOAT);
		VARIABLE_PREFIXES.put("double", PREFIX_DOUBLE);
		VARIABLE_PREFIXES.put("byte", PREFIX_SMALLINT);
		VARIABLE_PREFIXES.put("binary", PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put("date", PREFIX_DATE);
		VARIABLE_PREFIXES.put("timestamp", PREFIX_DATE);
		VARIABLE_PREFIXES.put("array", PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put("set", PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put("list", PREFIX_OBJECT_REFERENCE);
		VARIABLE_PREFIXES.put("map", PREFIX_OBJECT_REFERENCE);
	}

	/******************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public SQLRepositoryItemBase() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pDocuemnt XML DOM document that contains the parsed
			Repository Item Descriptor.
	*/
	public SQLRepositoryItemBase(PrintWriter pWriter,
		String strAuthor, Document pDocument)
			throws DynamoMetaException
	{
		this(pWriter, strAuthor, pDocument, null);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param pDocuemnt XML DOM document that contains the parsed
			Repository Item Descriptor.
	*/
	public SQLRepositoryItemBase(PrintWriter pWriter,
		String strAuthor, Document pDocument, String strPackageName)
			throws DynamoMetaException
	{
		super(pWriter, strAuthor);

		if (null != pDocument)
			setDocument(pDocument);

		m_strPackageName = strPackageName;
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - gets method suffix used in <CODE>getPropertyMethodSuffix</CODE>.
		@param property Property object.
	*/
	public String getPropertyMethodSuffix(Property property)
	{
		String strReturn = (String) METHOD_SUFFIXES.get(property.getDataType());

		if (null == strReturn)
			return "Object";

		return strReturn;
	}

	/** Helper method - gets JDBC method suffix used by JDBC <I>ResultSet</I> "getters" and
	    <I>PreparedStatement</I> "setters".
		@param property Property object.
	*/
	public String getJdbcMethodSuffix(Property property)
	{
		String strReturn = (String) JDBC_METHOD_SUFFIXES.get(property.getDataType());

		if (null == strReturn)
			return "Object";

		return strReturn;
	}

	/** Helper method - gets the an Java data type based
	    on the property data type.
		@param property Property object.
	*/
	public String getJavaType(Property property)
	{
		String strReturn = (String) JAVA_TYPES.get(property.getDataType());

		if (null == strReturn)
			return "Object";

		return strReturn;
	}

	/** Helper method - gets the an Java object data type based
	    on the property data type.
		@param property Property object.
	*/
	public String getJavaObjectType(Property property)
	{
		String strReturn = (String) JAVA_OBJECT_TYPES.get(property.getDataType());

		if (null == strReturn)
			return "String";

		return strReturn;
	}

	/** Accessor method - gets a <I>String</I> representation of the variable
	    prefix used by the data type of the property.
		@param property Property object.
	*/
	public String getVariablePrefix(Property property)
	{
		String strReturn = (String) VARIABLE_PREFIXES.get(property.getDataType());

		if (null == strReturn)
			return PREFIX_OBJECT_REFERENCE;

		return strReturn;
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - gets the SQL Repository object that the document
	    represents.
	*/
	public SQLRepository getRepository() { return m_Repository; }

	/** Accessor method - gets the default Item Descriptor meta object. */
	public ItemDescriptor getDefaultItemDescriptor() { return m_DefaultItemDescriptor; }

	/** Accessor method - gets the package name of the wrapper class. */
	public String getPackageName() { return m_strPackageName; }

	/** Accessor method - gets the name of the repository item. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the class name of the generated output. */
	public String getClassName() { return getName(); }

	/** Accessor method - gets the ATG Dynamo Repository framework package name.
	    This accessor can be overridden to provide a different package name.
	*/
	public String getFrameworkPackage() { return DEFAULT_FRAMEWORK_PACKAGE; }

	/******************************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************************/

	/** Mutator method - sets the XML DOM document that contains the parsed
	    Repository Item Descriptor.
	*/
	public void setDocument(Document document)
		throws DynamoMetaException, NullPointerException
	{
		if (null == document)
			throw new NullPointerException("Please provide an XML Document object.");

		m_Repository = new SQLRepository(document);
		m_DefaultItemDescriptor = m_Repository.getDefaultItemDescriptor();
		m_strName = m_DefaultItemDescriptor.getName();
	}

	/** Mutator method - sets the package name of the wrapper class. */
	public void setPackageName(String newValue) { m_strPackageName = newValue; }

	/******************************************************************************
	*
	*	Helper methods - static
	*
	*****************************************************************************/

	/** Helper method - loops through an array of SQL Repository XML files
	    and generates the output.
		@param generator an instance of <I>SQLRepositoryItemBase</I> used
			to generate each resource for each SQL Repository XML file.
		@param fileItemDescriptors array of SQL Repository XML files.
		@param fileOutputDir directory to output the generated resources.
	*/
	public static void generateRepositoryResource(SQLRepositoryItemBase generator,
		File[] fileItemDescriptors, File fileOutputDir)
			throws GeneratorException, IOException
	{
		File fileInput = null;

		try
		{
			// Get DOM Parser.
			DOMParser parser = new DOMParser();

			// Loop through the Repository Item Descriptor files.
			for (int i = 0; i < fileItemDescriptors.length; i++)
			{
				// Get the current item descriptor to operate on.
				fileInput = fileItemDescriptors[i];

				// Parse the item descriptor.
				FileInputStream in = new FileInputStream(fileInput);
				InputSource inputSource = new InputSource(in);
				parser.parse(inputSource);

				// After setting the repository document
				// get the class name of java class file
				// to be generated.
				generator.setDocument(parser.getDocument());
				String strClassName = generator.getClassName();

				// Create the output file writer.
				File fileOutput = new File(fileOutputDir, strClassName + ".java");
				PrintWriter writer = new PrintWriter(new FileWriter(fileOutput));

				// Set the generator's writer.
				generator.setWriter(writer);

				// Generate the document.
				generator.generate();

				// Close the writer and flush the buffer.
				writer.close();
			}
		}

		catch (IOException pEx) { throw pEx; }
		catch (Exception pEx)
		{
			// If fileInput is valid, use in error output message.
			if (null != fileInput)
				System.err.println("While processing " + fileInput.getAbsolutePath() + " ...");

			throw new GeneratorException(pEx);
		}
	}

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - reference to the SQL Repository meta object. */
	private SQLRepository m_Repository = null;

	/** Member variable - reference to the default Item Descriptor meta object. */
	private ItemDescriptor m_DefaultItemDescriptor = null;

	/** Member variable - reference to the package name of the wrapper class. */
	private String m_strPackageName = null;

	/** Member variable - reference to the name of the repository item. */
	private String m_strName = null;
}
