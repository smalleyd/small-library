package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean BMP classes. The BMP classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 7/12/2002
*
***************************************************************************************/

public class EntityBeanBMP extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "Bean";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param strEntityName Name of the entity.
	*/
	public static String getClassName(String strEntityName)
	{
		return strEntityName + CLASS_NAME_SUFFIX;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanBMP() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanBMP(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable)
	{
		super(pWriter, strAuthor, pTable);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public EntityBeanBMP(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable, strPackageName);
	}

	/******************************************************************************
	*
	*	Required methods: Base
	*
	*****************************************************************************/

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeEjbMethods();
		writeFinderMethods();
		writeHelperMethods();
		writeAccessorMethods();
		writeMutatorMethods();
		writeEntityContextMethods();
		writeMemberVariables();

		writeFooter();
	}

	/******************************************************************************
	*
	*	Required methods: BaseTable
	*
	*****************************************************************************/

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Tables.Record pTable)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(pTable.getName())) + ".java";
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - populates the key column information. */
	protected void populateColumnInfo()
		throws GeneratorException
	{
		super.populateColumnInfo();

		// Get key column information.
		PrimaryKeys primaryKeys = getTable().getPrimaryKeys();

		try { primaryKeys.load(); }
		catch (SQLException pEx) { throw new GeneratorException(pEx); }

		mapPrimaryKeys = new HashMap(primaryKeys.size());
		primaryKeyCols = new ColumnInfo[primaryKeys.size()];

		// Get relevant column values.
		for (int i = 0, key = 0; i < m_ColumnInfo.length; i++)
		{
			if (null != primaryKeys.find(m_ColumnInfo[i].columnName))
			{
				mapPrimaryKeys.put(m_ColumnInfo[i].name, m_ColumnInfo[i]);
				primaryKeyCols[key++] = m_ColumnInfo[i];
			}
		}

		// Resort the column information.
		// First set the non-primary key fields. This way the order of "sets"
		// can be used for both INSERT and UPDATE actions.
		int newKey = 0;
		resortedCols = new ColumnInfo[m_ColumnInfo.length];

		for (int i = 0; i < m_ColumnInfo.length; i++)
			if (!mapPrimaryKeys.containsKey(m_ColumnInfo[i].name))
				resortedCols[newKey++] = m_ColumnInfo[i];

		for (int i = 0; i < primaryKeyCols.length; i++)
			resortedCols[newKey++] = primaryKeyCols[i];
	}

	/******************************************************************************
	*
	*	Output methods
	*
	*****************************************************************************/

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String strPackageName = getPackageName();

		if (null != strPackageName)
		{
			writeLine("package " + strPackageName + ";");
			writeLine();
		}

		writeLine("import java.rmi.RemoteException;");
		writeLine("import java.sql.*;");
		writeLine("import java.util.*;");
		writeLine();
		writeLine("import javax.ejb.*;");
		writeLine("import javax.naming.*;");
		writeLine("import javax.sql.DataSource;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tA JDBC Entity Bean BMP class that represents the " + getTable().getName());
		writeLine("*\ttable.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.0.0");
		writeLine("*\t@date " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public class " + getClassName() + " implements EntityBean");
		writeLine("{");
	}

	/** Output method - writes the member variables. */
	private void writeMemberVariables() throws IOException
	{
		// Start member variable section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMember variables");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();

		// Write member variables.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t/** Member variable - represents the \"" + m_ColumnInfo[i].columnName + "\" field. */");
			writeLine("\tpublic " + m_ColumnInfo[i].javaType + " " + m_ColumnInfo[i].memberVariableName + ";");
			writeLine();
		}

		// Write the Entity Context member variable.
		writeLine("\t/** Member variable - represents the <I>EntityContext</I> object. */");
		writeLine("\tprivate EntityContext entityContext;");
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t/** Accessor method - gets the property that represents the");
			writeLine("\t    \"" + m_ColumnInfo[i].columnName + "\" field.");
			writeLine("\t*/");
			write("\tpublic " + m_ColumnInfo[i].javaType + " " + m_ColumnInfo[i].accessorMethodName + "()");
				writeLine(" { return " + m_ColumnInfo[i].memberVariableName + "; }");
			writeLine();
		}
	}

	/** Output method - writes the mutator methods. */
	private void writeMutatorMethods() throws IOException	
	{
		// Start the section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMutator methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		writeLine();

		// Write mutators.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t/** Mutator method - sets the property that represents the");
			writeLine("\t    \"" + m_ColumnInfo[i].columnName + "\" field.");
			writeLine("\t*/");
			write("\tpublic void " + m_ColumnInfo[i].mutatorMethodName + "(" + m_ColumnInfo[i].javaType + " newValue)");
				writeLine(" { " + m_ColumnInfo[i].memberVariableName + " = newValue; }");
			writeLine();
		}
	}

	/** Output method - writes the required methods of javax.ejb.EntityBean. */
	private void writeEjbMethods() throws IOException
	{
		// Start section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tEJB methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		writeEjbCreateMethod();
		writeEjbStoreMethod();
		writeEjbRemoveMethod();
		writeEjbLoadMethod();
		writeEmptyEjbMethods();
	}

	/** Output method - writes the Entity Context methods. */
	private void writeEntityContextMethods() throws IOException
	{
		// Start section.
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tEntity Context methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		writeLine();

		// Write accessor.
		writeLine("\t/** Accessor method - gets the <I>EntityContext</I> object. */");
		writeLine("\tpublic EntityContext getEntityContext() { return entityContext; }");

		// Write mutator
		writeLine();
		writeLine("\t/** Mutator method - sets the <I>EntityContext</I> object. */");
		writeLine("\tpublic void setEntityContext(EntityContext newValue) { entityContext = newValue; }");
		writeLine();
		writeLine("\t/** Mutator method - unsets the <I>EntityContext</I> object. */");
		writeLine("\tpublic void unsetEntityContext() { entityContext = null; }");
		writeLine();
	}

	/** Output method - writes the <CODE>ejbCreate</CODE> method. */
	private void writeEjbCreateMethod() throws IOException
	{
		// Write create method with all possible values.
		writeLine();
		writeLine("\t/** Required method - creates a populated object.");

		for (int i = 0; i < m_ColumnInfo.length; i++)
			writeLine("\t\t@param " + m_ColumnInfo[i].localVariableName +
				" represents the \"" + m_ColumnInfo[i].columnName +
				"\" field.");

		writeLine("\t*/");
		writeLine("\tpublic " + EntityBeanPrimaryKey.getClassName(getObjectName()) + " ejbCreate(");

		for (int i = 0; i < m_ColumnInfo.length - 1; i++)
			writeLine("\t\t" + m_ColumnInfo[i].javaType + " " +
				m_ColumnInfo[i].localVariableName + ",");

		writeLine("\t\t" + m_ColumnInfo[m_ColumnInfo.length - 1].javaType + " " +
			m_ColumnInfo[m_ColumnInfo.length - 1].localVariableName + ")");
		writeLine("\t\t\tthrows EJBException");

		writeLine("\t{");

		for (int i = 0; i < m_ColumnInfo.length; i++)
			writeLine("\t\t" + m_ColumnInfo[i].memberVariableName + " = " +
				m_ColumnInfo[i].localVariableName + ";");

		// Get the primary key class information.
		String primaryKeyClassName = EntityBeanPrimaryKey.getClassName(getObjectName());
		String primaryKeyConstructor = primaryKeyCols[0].memberVariableName;

		for (int i = 1; i < primaryKeyCols.length; i++)
			primaryKeyConstructor+= ", " + primaryKeyCols[i].memberVariableName;

		// Create INSERT column information.
		String insertColList = resortedCols[0].columnName;
		String insertParams = "?";

		for (int i = 1; i < resortedCols.length; i++)
		{
			insertColList+= ", " + resortedCols[i].columnName;
			insertParams+= ", ?";
		}

		writeLine();
		writeLine("\t\tConnection connection = null;");
		writeLine("\t\tPreparedStatement stmt = null;");
		writeLine();
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tconnection = getConnection();");
		writeLine("\t\t\tstmt = connection.prepareStatement(");
		writeLine("\t\t\t\t\"INSERT INTO " + getTable().getName() + " \" +");
		writeLine("\t\t\t\t\"(" + insertColList + ") \" +");
		writeLine("\t\t\t\t\"VALUES (" + insertParams + ")\");");
		writeLine();
		writeLine("\t\t\tsetParameters(stmt);");
		writeLine("\t\t\tstmt.execute();");
		writeLine();
		writeLine("\t\t\treturn new " + primaryKeyClassName + "(" + primaryKeyConstructor + ");");
		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tfinally");
		writeLine("\t\t{");
		writeLine("\t\t\ttry");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\tif (null != stmt) stmt.close();");
		writeLine("\t\t\t\tif (null != connection) connection.close();");
		writeLine("\t\t\t}");
		writeLine("\t\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\t}");
		writeLine("\t}");			

		// Write post create method with all possible values.
		writeLine();
		writeLine("\t/** Required method - handle post creation of the bean.");

		for (int i = 0; i < m_ColumnInfo.length; i++)
			writeLine("\t\t@param " + m_ColumnInfo[i].localVariableName +
				" represents the \"" + m_ColumnInfo[i].columnName +
				"\" field.");

		writeLine("\t*/");
		writeLine("\tpublic void ejbPostCreate(");

		for (int i = 0; i < m_ColumnInfo.length - 1; i++)
			writeLine("\t\t" + m_ColumnInfo[i].javaType + " " +
				m_ColumnInfo[i].localVariableName + ",");

		writeLine("\t\t" + m_ColumnInfo[m_ColumnInfo.length - 1].javaType + " " +
			m_ColumnInfo[m_ColumnInfo.length - 1].localVariableName + ")");

		writeLine("\t{}");
	}

	/** Output method - writes the <CODE>ejbStore</CODE> method. */
	private void writeEjbStoreMethod() throws IOException
	{
		// Write the storage method.
		writeLine();
		writeLine("\t/** EJB method - handles bean storage. */");
		writeLine("\tpublic void ejbStore()");
		writeLine("\t\tthrows EJBException");
		writeLine("\t{");

		// Create UPDATE column information.
		String updateColList = resortedCols[0].columnName + " = ?";
		String whereClause = primaryKeyCols[0].columnName + " = ?";

		for (int i = 1; i < resortedCols.length; i++)
		{
			ColumnInfo col = resortedCols[i];

			if (mapPrimaryKeys.containsKey(col.name))
				break;

			updateColList+= ", " + resortedCols[i].columnName +
				" = ?";
		}

		// build WHERE clause.
		for (int i = 1; i < primaryKeyCols.length; i++)
			whereClause+= " AND " + primaryKeyCols[i].columnName + " = ?";

		writeLine("\t\tConnection connection = null;");
		writeLine("\t\tPreparedStatement stmt = null;");
		writeLine();
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tconnection = getConnection();");
		writeLine("\t\t\tstmt = connection.prepareStatement(");
		writeLine("\t\t\t\t\"UPDATE " + getTable().getName() + " \" +");
		writeLine("\t\t\t\t\"SET " + updateColList + " \" +");
		writeLine("\t\t\t\t\"WHERE " + whereClause + "\");");
		writeLine();
		writeLine("\t\t\tsetParameters(stmt);");
		writeLine("\t\t\tstmt.execute();");
		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tfinally");
		writeLine("\t\t{");
		writeLine("\t\t\ttry");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\tif (null != stmt) stmt.close();");
		writeLine("\t\t\t\tif (null != connection) connection.close();");
		writeLine("\t\t\t}");
		writeLine("\t\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\t}");

		writeLine("\t}");
	}

	/** Output method - writes the <CODE>ejbRemove</CODE> method. */
	private void writeEjbRemoveMethod() throws IOException
	{
		// Write the removal method.
		writeLine();
		writeLine("\t/** EJB method - handles bean removal. */");
		writeLine("\tpublic void ejbRemove()");
		writeLine("\t\tthrows EJBException");
		writeLine("\t{");

		// Create DELETE column information.
		String whereClause = primaryKeyCols[0].columnName + " = ?";

		// build WHERE clause.
		for (int i = 1; i < primaryKeyCols.length; i++)
			whereClause+= " AND " + primaryKeyCols[i].columnName + " = ?";

		writeLine("\t\tConnection connection = null;");
		writeLine("\t\tPreparedStatement stmt = null;");
		writeLine();
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tconnection = getConnection();");
		writeLine("\t\t\tstmt = connection.prepareStatement(");
		writeLine("\t\t\t\t\"DELETE FROM " + getTable().getName() + " \" +");
		writeLine("\t\t\t\t\"WHERE " + whereClause + "\");");
		writeLine();

		for (int i = 0; i < primaryKeyCols.length; i++)
			writeLine("\t\t\tstmt.set" + primaryKeyCols[i].jdbcMethodSuffix +
				"(" + (i + 1) + ", " + primaryKeyCols[i].memberVariableName +
				");");

		writeLine();
		writeLine("\t\t\tstmt.execute();");
		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tfinally");
		writeLine("\t\t{");
		writeLine("\t\t\ttry");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\tif (null != stmt) stmt.close();");
		writeLine("\t\t\t\tif (null != connection) connection.close();");
		writeLine("\t\t\t}");
		writeLine("\t\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\t}");

		writeLine("\t}");
	}

	/** Output method - writes the <CODE>ejbLoad</CODE> method. */
	private void writeEjbLoadMethod() throws IOException
	{
		// Write the load method.
		writeLine();
		writeLine("\t/** EJB method - handles bean loading. */");
		writeLine("\tpublic void ejbLoad()");
		writeLine("\t\tthrows EJBException");
		writeLine("\t{");

		// Primary Key class name.
		String primaryKeyClassName = EntityBeanPrimaryKey.getClassName(getObjectName());

		// Create SELECT column information.
		String selectClause = m_ColumnInfo[0].columnName;

		for (int i = 1; i < m_ColumnInfo.length; i++)
			selectClause+= ", " + m_ColumnInfo[i].columnName;

		// build WHERE clause.
		String whereClause = primaryKeyCols[0].columnName + " = ?";
		for (int i = 1; i < primaryKeyCols.length; i++)
			whereClause+= " AND " + primaryKeyCols[i].columnName + " = ?";

		writeLine("\t\t" + primaryKeyClassName + " primaryKey = " +
			"(" + primaryKeyClassName + ") entityContext.getPrimaryKey();");
		writeLine("\t\tConnection connection = null;");
		writeLine("\t\tPreparedStatement stmt = null;");
		writeLine("\t\tResultSet resultSet = null;");
		writeLine();
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tconnection = getConnection();");
		writeLine("\t\t\tstmt = connection.prepareStatement(");
		writeLine("\t\t\t\t\"SELECT " + selectClause + " \" +");
		writeLine("\t\t\t\t\"FROM " + getTable().getName() + " \" +");
		writeLine("\t\t\t\t\"WHERE " + whereClause + "\");");
		writeLine();

		for (int i = 0; i < primaryKeyCols.length; i++)
			writeLine("\t\t\tstmt.set" + primaryKeyCols[i].jdbcMethodSuffix +
				"(" + (i + 1) + ", primaryKey." + primaryKeyCols[i].memberVariableName +
				");");

		writeLine();
		writeLine("\t\t\tresultSet = stmt.executeQuery();");
		writeLine();
		writeLine("\t\t\tif (!resultSet.next())");
		writeLine("\t\t\t\tthrow new EJBException(\"Cannot find \\\"" +
			getObjectName() + "\\\" with primary key - \" + primaryKey.toString() + \".\");");
		writeLine();

		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo col = m_ColumnInfo[i];

			writeLine("\t\t\t" + col.memberVariableName + " = resultSet.get" +
				col.jdbcMethodSuffix + "(" + (i + 1) + ");");
		}

		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tfinally");
		writeLine("\t\t{");
		writeLine("\t\t\ttry");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\tif (null != stmt) stmt.close();");
		writeLine("\t\t\t\tif (null != connection) connection.close();");
		writeLine("\t\t\t}");
		writeLine("\t\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\t}");

		writeLine("\t}");
	}

	/** Output method - writes the empty EJB implementation required methods. */
	private void writeEmptyEjbMethods() throws IOException
	{
		// Start section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tEmpty EJB methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write the activate method.
		writeLine();
		writeLine("\t/** EJB method - handles bean activation. */");
		writeLine("\tpublic void ejbActivate() {}");

		// Write the passivate method.
		writeLine();
		writeLine("\t/** EJB method - handles bean passivation. */");
		writeLine("\tpublic void ejbPassivate() {}");
	}

	/** Output method - writes the EJB Finder methods. */
	private void writeFinderMethods() throws IOException
	{
		// Start section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tEJB finder methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");
		
		writeEjbFindByPrimaryKeyMethod();
		writeEjbFindAllMethod();
	}

	/** Output method - writes the <CODE>ejbFindByPrimaryKey</CODE> method. */
	private void writeEjbFindByPrimaryKeyMethod() throws IOException
	{
		String strName = getObjectName();
		String strPrimaryKeyClassName = EntityBeanPrimaryKey.getClassName(strName);

		// Build the SQL Call.
		String strSQL = "SELECT " + primaryKeyCols[0].columnName +
			" FROM " + getTable().getName() +
			" WHERE " + primaryKeyCols[0].columnName + " = ?";

		for (int i = 1; i < primaryKeyCols.length; i++)
			strSQL+= " AND " + primaryKeyCols[i].columnName + " = ?";

		// Write the finder method.
		writeLine();
		writeLine("\t/**Finder method - find \"" + strName + "\" entity by primary key.");
		writeLine("\t\t@param primaryKey Primary key object.");
		writeLine("\t*/");
		writeLine("\tpublic " + strPrimaryKeyClassName + " ejbFindByPrimaryKey(" +
			strPrimaryKeyClassName + " primaryKey)");
		writeLine("\t\tthrows FinderException, EJBException");
		writeLine("\t{");
		writeLine("\t\tConnection connection = null;");
		writeLine("\t\tResultSet resultSet = null;");
		writeLine("\t\tPreparedStatement stmt = null;");
		writeLine();
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tconnection = getConnection();");
		writeLine("\t\t\tstmt = connection.prepareStatement(\"" + strSQL + "\");");
		writeLine();

		for (int i = 0; i < primaryKeyCols.length; i++)
			writeLine("\t\t\tstmt.set" + primaryKeyCols[i].jdbcMethodSuffix +
				"(" + (i + 1) + ", primaryKey." + primaryKeyCols[i].memberVariableName +
				");");

		writeLine();
		writeLine("\t\t\tresultSet = stmt.executeQuery();");
		writeLine("\t\t\tif (!resultSet.next())");
		writeLine("\t\t\t\tthrow new ObjectNotFoundException(\"Cannot find \\\"" +
			strName + "\\\" with primary key - \" + primaryKey.toString() + \".\");");
		writeLine();
		writeLine("\t\t\treturn primaryKey;");
		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tfinally");
		writeLine("\t\t{");
		writeLine("\t\t\ttry");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\tif (null != stmt) stmt.close();");
		writeLine("\t\t\t\tif (null != resultSet) resultSet.close();");
		writeLine("\t\t\t\tif (null != connection) connection.close();");
		writeLine("\t\t\t}");
		writeLine("\t\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\t}");
		writeLine("\t}");
	}

	/** Output method - writes the <CODE>ejbFindAll</CODE> method. */
	private void writeEjbFindAllMethod() throws IOException
	{
		String strName = getObjectName();
		String strPrimaryKeyClassName = EntityBeanPrimaryKey.getClassName(strName);

		// Build the SQL Call.
		String strSQL = "SELECT " + primaryKeyCols[0].columnName;

		for (int i = 1; i < primaryKeyCols.length; i++)
			strSQL+= ", " + primaryKeyCols[i].columnName;

		strSQL+= " FROM " + getTable().getName();

		// Write the finder method.
		writeLine();
		writeLine("\t/**Finder method - find all \"" + strName + "\" entities. */");
		writeLine("\tpublic Collection ejbFindAll()");
		writeLine("\t\tthrows FinderException, EJBException");
		writeLine("\t{");
		writeLine("\t\tConnection connection = null;");
		writeLine("\t\tResultSet resultSet = null;");
		writeLine();
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tconnection = getConnection();");
		writeLine("\t\t\tresultSet = connection.createStatement().executeQuery(\"" + strSQL + "\");");
		writeLine("\t\t\tArrayList primaryKeys = new ArrayList();");
		writeLine();
		writeLine("\t\t\twhile (resultSet.next())");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\t" + strPrimaryKeyClassName + " primaryKey = new " +
			strPrimaryKeyClassName + "();");
		writeLine();

		for (int i = 0; i < primaryKeyCols.length; i++)
		{
			ColumnInfo pk = primaryKeyCols[i];
			writeLine("\t\t\t\tprimaryKey." + pk.memberVariableName +
				" = resultSet.get" + pk.jdbcMethodSuffix +
				"(" + (i + 1) + ");");
		}

		writeLine();
		writeLine("\t\t\t\tprimaryKeys.add(primaryKey);");
		writeLine("\t\t\t}");
		writeLine();
		writeLine("\t\t\treturn primaryKeys;");
		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tfinally");
		writeLine("\t\t{");
		writeLine("\t\t\ttry");
		writeLine("\t\t\t{");
		writeLine("\t\t\t\tif (null != resultSet) resultSet.close();");
		writeLine("\t\t\t\tif (null != connection) connection.close();");
		writeLine("\t\t\t}");
		writeLine("\t\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\t}");
		writeLine("\t}");
	}

	/** Output method - writes the helper methods. */
	private void writeHelperMethods() throws IOException
	{
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tHelper methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		writeSetParameters();
		writeGetConnection();
	}

	/** Output method - writes the setParameters method. */
	private void writeSetParameters() throws IOException
	{
		writeLine();
		writeLine("\t/** Helper method - sets parameters of the prepared JDBC statement.");
		writeLine("\t\t@param stmt <I>PreparedStatement</I> object.");
		writeLine("\t*/");
		writeLine("\tprivate void setParameters(PreparedStatement stmt)");
		writeLine("\t\tthrows SQLException");
		writeLine("\t{");

		for (int i = 0; i < resortedCols.length; i++)
		{
			ColumnInfo col = resortedCols[i];

			writeLine("\t\tstmt.set" + col.jdbcMethodSuffix + "(" +
				(i + 1) + ", " + col.memberVariableName + ");");
		}

		writeLine("\t}");
	}

	/** Output method - writes the getConnection method. */
	private void writeGetConnection() throws IOException
	{
		writeLine();
		writeLine("\t/** Helper method - gets a JDBC connection to the underlying");
		writeLine("\t    data source.");
		writeLine("\t*/");
		writeLine("\tprivate Connection getConnection()");
		writeLine("\t\tthrows EJBException");
		writeLine("\t{");
		writeLine("\t\ttry");
		writeLine("\t\t{");
		writeLine("\t\t\tContext context = new InitialContext();");
		writeLine("\t\t\tDataSource dataSource = (DataSource)");
		writeLine("\t\t\t\tcontext.lookup(\"java:comp/env/jdbc/mainDataSource\");");
		writeLine();
		writeLine("\t\t\treturn dataSource.getConnection();");
		writeLine("\t\t}");
		writeLine();
		writeLine("\t\tcatch (NamingException pEx) { throw new EJBException(pEx); }");
		writeLine("\t\tcatch (SQLException pEx) { throw new EJBException(pEx); }");
		writeLine("\t}");
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - map of <I>ColumnInfo</I> objects that pertain to the
	    primary key.
	*/
	private Map mapPrimaryKeys = null;

	/** Member variable - array of <I>ColumnInfo</I> objects that pertain to the
	    primary key.
	*/
	private ColumnInfo[] primaryKeyCols = null;

	/** Member variable - array of <I>ColumnInfo</I> objects sorted with primary key
	    columns at the end. Used to build INSERT and UPDATE lists that can use
	    the same parameter binding logic.
	*/
	private ColumnInfo[] resortedCols = null;

	/******************************************************************************
	*
	*	Class entry point
	*
	*****************************************************************************/

	/** Command line entry point.
		@param strArg1 Output directory.
		@param strArg2 URL to the data source.
		@param strArg3 data source login name.
		@param strArg4 data source password.
		@param strArg5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param strArg6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param strArg7 package name of the entity bean BMP classes.
	*/
	public static void main(String strArgs[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > strArgs.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(strArgs, 0);
			String strAuthor = extractAuthor(strArgs, 5);
			String strPackageName = extractArgument(strArgs, 6, null);

			// Create and load the tables object.
			Tables pTables = extractTables(strArgs, 1, 7);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanBMP pGenerator =
				new EntityBeanBMP((PrintWriter) null, strAuthor,
				(Tables.Record) null, strPackageName);

			// Call the BaseTable method to handle the outputing.
			generateTableResources(pGenerator, pTables, fileOutputDir);
		}

		catch (IllegalArgumentException pEx)
		{
			String strMessage = pEx.getMessage();

			if (null != strMessage)
			{
				System.out.println(strMessage);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityBeanBMP.class.getName() + " Output directory");
			System.out.println("\tJDBC_URL");
			System.out.println("\tUser_ID");
			System.out.println("\t[Passowrd]");
			System.out.println("\t[JDBC Driver]");
			System.out.println("\t[Author]");
			System.out.println("\t[Package Name]");
			System.out.println("\t[Schema Name Pattern]");
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
