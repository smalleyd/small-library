package com.small.library.generator;

import java.sql.*;
import java.io.*;

import com.small.library.data.*;
import com.small.library.metadata.*;
import com.small.library.util.StringHelper;

/***************************************************************************************
*
*	Abstract class describing dsl code generator.
*	@author Xpedior\Tomasz Piwowarski
*	@version 1.0.0.0
*	@date 4/04/2000
*
***************************************************************************************/

public abstract class DataServiceObject extends BaseTable
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - ID field name . */
	public static final String CONSTANT_ID = "id";

	/** Constant - description field name. */
	public static final String CONSTANT_DESC = "descript";

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public DataServiceObject() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public DataServiceObject(PrintWriter pWriter, String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/*************************************************************************************
	*
	*	Factory methods
	*
	*************************************************************************************/

	/** Factory method
		@return Returns DataServiceObject object
	*/
	public static DataServiceObject getJavaInstance()
		throws SQLException
	{ return new DataServiceObjectJava(); }

	/** Factory method
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@return Returns DataServiceObject object
	*/
	public static DataServiceObject getJavaInstance(PrintWriter pWriter, String strAuthor,
		Tables.Record pTable) throws SQLException
	{ return new DataServiceObjectJava(pWriter, strAuthor, pTable); }

	/******************************************************************************
	*
	*	Helper methods
	*
	******************************************************************************/

	/** Accessor method - gets a column's object version of a name. Default
	    implementation calls <I>createObjectName</I>. Subclasses can override
	    behaviour.
		@param pColumn Column record object.
	*/
	public String getColumnObjectName(Columns.Record pColumn)
	{
		String strColumnName = pColumn.getName();

		if (!StringHelper.isValid(strColumnName))
			return strColumnName;

		if (strColumnName.equalsIgnoreCase(CONSTANT_ID))
			return "ID";

		if (strColumnName.equalsIgnoreCase(CONSTANT_DESC))
			return "Desc";

		return super.getColumnObjectName(pColumn);
	}
}
