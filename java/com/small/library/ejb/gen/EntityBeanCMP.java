package com.small.library.ejb.gen;

import java.io.*;
import java.sql.*;

import com.small.library.data.*;
import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean CMP 2.x classes. The CMP 2.x classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.1.0.0
*	@since 7/12/2002
*
***************************************************************************************/

public class EntityBeanCMP extends EntityBeanBase
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
	public EntityBeanCMP() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanCMP(PrintWriter pWriter,
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
	public EntityBeanCMP(PrintWriter pWriter,
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

		writeMemberVariables();
		writeAccessorMethods();
		writeImportedKeysAccessorMethods();
		writeMutatorMethods();
		writeRequiredMethods();

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
		writeLine();
		writeLine("import javax.ejb.*;");
		writeLine("import javax.naming.NamingException;");	// For import key accessors.
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tEntity Bean CMP class that represents the " + getTable().getName());
		writeLine("*\ttable.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.0.0");
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("public abstract class " + getClassName() + " implements EntityBean");
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

		// Write the Entity Context member variable.
		writeLine();
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

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Accessor method - gets the property that represents the");
			writeLine("\t    \"" + m_ColumnInfo[i].columnName + "\" field.");
			writeLine("\t*/");
			writeLine("\tpublic abstract " + m_ColumnInfo[i].javaType + " " + m_ColumnInfo[i].accessorMethodName + "();");
		}

		// Write getAll.
		String objectName = getObjectName();
		String valueObjectName = EntityBeanValueObject.getClassName(objectName);
		
		writeLine();
		writeLine("\t/** Accessor method - gets all properties of the entity bean.");
		writeLine("\t\t@return a <I>" + valueObjectName + "</I> object.");
		writeLine("\t*/");
		writeLine("\tpublic " + valueObjectName + " getAll()");
		writeLine("\t{");
		writeLine("\t\t" + valueObjectName  + " value = new " + valueObjectName + "();");
		writeLine();

		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t\tvalue." + m_ColumnInfo[i].memberVariableName + " = " +
				m_ColumnInfo[i].accessorMethodName + "();");
		}

		writeLine();
		writeLine("\t\treturn value;");
		writeLine("\t}");
	}

	/** Output method - writes the imported foreign key accessor methods. */
	private void writeImportedKeysAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tAccessor methods - Imported foreign keys");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo columnInfo = m_ColumnInfo[i];

			if (!columnInfo.isImportedKey)
				continue;

			String name = columnInfo.importedKeyName;
			String importedObjectName = columnInfo.importedObjectName;
			String localInterface = EntityBeanLocal.getClassName(importedObjectName);

			writeLine();
			writeLine("\t/** CMR method - gets the " + name + " property as a");
			writeLine("\t    \"" + importedObjectName + "\" entity.");
			writeLine("\t*/");
			writeLine("\tpublic abstract " + localInterface + " get" + name + "();");
			writeLine();
			writeLine("\t/** CMR method - sets the " + name + " property as a");
			writeLine("\t    \"" + importedObjectName + "\" entity.");
			writeLine("\t*/");
			writeLine("\tpublic abstract void set" + name + "(" + localInterface + " newValue);");
		}
	}

	/** Output method - writes the mutator methods. */
	private void writeMutatorMethods() throws IOException	
	{
		// Start the section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMutator methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write mutators.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Mutator method - sets the property that represents the");
			writeLine("\t    \"" + m_ColumnInfo[i].columnName + "\" field.");
			writeLine("\t*/");
			writeLine("\tpublic abstract void " + m_ColumnInfo[i].mutatorMethodName + "(" +
				m_ColumnInfo[i].javaType + " newValue);");
		}

		// Write setAll.
		String objectName = getObjectName();
		String valueObjectName = EntityBeanValueObject.getClassName(objectName);
		
		writeLine();
		writeLine("\t/** Mutator method - sets all properties of the entity bean.");
		writeLine("\t\t@param newValue a <I>" + valueObjectName + "</I> object.");
		writeLine("\t*/");
		writeLine("\tpublic void setAll(" + valueObjectName + " newValue)");
		writeLine("\t{");

		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine("\t\t" + m_ColumnInfo[i].mutatorMethodName + "(newValue." +
				m_ColumnInfo[i].memberVariableName + ");");
		}

		writeLine("\t}");
	}

	/** Output method - writes the required methods of javax.ejb.EntityBean. */
	private void writeRequiredMethods() throws IOException
	{
		writeEntityContextMethods();
		writeEjbCreateMethod();
		writeEmptyEjbMethods();
	}

	/** Output method - writes the Entity Context methods. */
	private void writeEntityContextMethods() throws IOException
	{
		// Start section.
		writeLine();
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
	}

	/** Output method - writes the <CODE>ejbCreate</CODE> method. */
	private void writeEjbCreateMethod() throws IOException
	{
		String objectName = getObjectName();
		String valueObjectName = EntityBeanValueObject.getClassName(objectName);

		// Start section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tEJB Create methods");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write create method with all possible values.
		writeLine();
		writeLine("\t/** Required method - creates a populated object.");
		writeLine("\t\t@param value Value object that represents the " + objectName + " entity.");
		writeLine("\t*/");
		writeLine("\tpublic " + EntityBeanPrimaryKey.getClassName(objectName) + " ejbCreate(" +
			valueObjectName + " value)");
		writeLine("\t\tthrows CreateException");

		writeLine("\t{");
		writeLine("\t\tsetAll(value);");
		writeLine();
		writeLine("\t\treturn null;");
		writeLine("\t}");			

		// Write post create method with all possible values.
		writeLine();
		writeLine("\t/** Required method - handle post creation of the bean.");
		writeLine("\t\t@param value Value object that represents the " + objectName + " entity.");
		writeLine("\t*/");
		writeLine("\tpublic void ejbPostCreate(" + valueObjectName + " value) {}");
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

		// Write the load method.
		writeLine();
		writeLine("\t/** EJB method - handles bean loading. */");
		writeLine("\tpublic void ejbLoad() {}");

		// Write the activate method.
		writeLine();
		writeLine("\t/** EJB method - handles bean activation. */");
		writeLine("\tpublic void ejbActivate() {}");

		// Write the passivate method.
		writeLine();
		writeLine("\t/** EJB method - handles bean passivation. */");
		writeLine("\tpublic void ejbPassivate() {}");

		// Write the storage method.
		writeLine();
		writeLine("\t/** EJB method - handles bean storage. */");
		writeLine("\tpublic void ejbStore() {}");

		// Write the removal method.
		writeLine();
		writeLine("\t/** EJB method - handles bean removal. */");
		writeLine("\tpublic void ejbRemove() {}");
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
		@param strArg7 package name of the entity bean CMP classes.
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
			EntityBeanCMP pGenerator =
				new EntityBeanCMP((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanCMP.class.getName() + " Output directory");
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
