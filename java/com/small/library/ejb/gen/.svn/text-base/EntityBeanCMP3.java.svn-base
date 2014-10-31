package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean CMP 3.x classes. The CMP 3.x classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	The classes are annonated fully.
*
*	@author David Small
*	@version 1.1.0.0
*	@since 11/25/2005
*
***************************************************************************************/

public class EntityBeanCMP3 extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - version JavaDoc value. */
	public String version = null;

	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "";

	/** Constant - default JavaDoc version stamp. */
	public static final String VERSION_DEFAULT = "1.0.1";

	/******************************************************************************
	*
	*	Static members
	*
	*****************************************************************************/

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/******************************************************************************
	*
	*	Constructors/Destructor
	*
	*****************************************************************************/

	/** Constructor - constructs an empty object. */
	public EntityBeanCMP3() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanCMP3(PrintWriter pWriter,
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
	public EntityBeanCMP3(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName)
	{
		super(pWriter, strAuthor, pTable, strPackageName);
	}

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
		@param strPackageName Package name of the wrapper class.
	*/
	public EntityBeanCMP3(PrintWriter pWriter,
		String strAuthor, Tables.Record pTable, String strPackageName,
		String version)
	{
		super(pWriter, strAuthor, pTable, strPackageName);

		this.version = version;
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
		writeConstructors();
		writeAccessorMethods();
		writeMutatorMethods();
		writeImportedKeysAccessorMethods();

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
		return getClassName() + ".java";
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

		writeLine("import java.io.Serializable;");
		writeLine("import java.util.Date;");
		// writeLine("import java.util.List;"); // NOT needed for now.
		writeLine();
		writeLine("import javax.persistence.*;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tEntity Bean CMP class that represents the " + getTable().getName() + " table.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version " + version);
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("@Entity");
		writeLine("@Table(name=\"" + getTable().getName() + "\")");
		writeLine("public class " + getClassName() + " implements Serializable");
		writeLine("{");
		writeLine("\t/** Constant - serial version UID. */");
		writeLine("\tpublic final static long serialVersionUID = 1L;");
	}

	/** Output method - writes the member variables. */
	private void writeMemberVariables() throws IOException
	{
		// Start member variable section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tMember variables");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write member variables.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Member variable - represents the \"" + m_ColumnInfo[i].columnName + "\" field. */");
			writeLine("\tpublic " + m_ColumnInfo[i].javaType + " " + m_ColumnInfo[i].memberVariableName + ";");
		}
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		// Start section.
		writeLine();
		writeLine("\t/**************************************************************************");
		writeLine("\t*");
		writeLine("\t*\tConstructors");
		writeLine("\t*");
		writeLine("\t**************************************************************************/");

		// Write the default/empty constructor. */
		writeLine();
		writeLine("\t/** Default/empty. */");
		writeLine("\tpublic " + getClassName() + "() {}");

		// Write constructor with all possible values.
		writeLine();
		writeLine("\t/** Populator.");

		// Create the parameter comments.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];
			writeLine("\t\t@param " + item.memberVariableName + " represents the \"" +
				item.columnName + "\" field.");
		}

		writeLine("\t*/");

		// Constructor signature.
		write("\tpublic " + getClassName() + "(");
		for (int i = 0, last = m_ColumnInfo.length - 1; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];

			if (0 < i)
				write("\t\t");

			write(item.javaType + " " + item.memberVariableName);

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("\t{");
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];
			writeLine("\t\tthis." + item.memberVariableName + " = " +
				item.memberVariableName + ";");
		}
		writeLine("\t}");			
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

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];
			String nullable = Boolean.toString(item.isNullable);

			writeLine();
			writeLine("\t/** Accessor method - gets the property that represents the \"" +
				item.columnName + "\" field. */");
			write("\t@Column(name=\"" + item.columnName + "\", ");
			write("columnDefinition=\"" + item.typeDefinition + "\", ");
			writeLine("nullable=" + nullable + ")");

			if (item.isPartOfPrimaryKey)
			{
				write("\t@Id");

				if (item.isAutoIncrementing)
					writeLine(" @GeneratedValue(strategy=GenerationType.AUTO)");
				else
					writeLine();
			}

			write("\tpublic " + item.javaType + " " + item.accessorMethodName + "()");
				writeLine(" { return " + item.memberVariableName + "; }");
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

		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write mutators.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Mutator method - sets the property that represents the \"" +
				m_ColumnInfo[i].columnName + "\" field. */");
			write("\tpublic void " + m_ColumnInfo[i].mutatorMethodName + "(" + m_ColumnInfo[i].javaType + " newValue)");
				writeLine(" { " + m_ColumnInfo[i].memberVariableName + " = newValue; }");
		}
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
			String memberName = columnInfo.importedKeyMemberName;
			String objectName = columnInfo.importedObjectName;

			writeLine();
			writeLine("\t/** CMR member - the " + name + " property as a \"" +
				objectName + "\" entity. */");
			writeLine("\tpublic " + objectName + " " + memberName + ";");
			writeLine();
			writeLine("\t/** CMR accessor - gets the " + name + " property as a");
			writeLine("\t    \"" + objectName + "\" entity.");
			writeLine("\t*/");
			writeLine("\t@ManyToOne(cascade={}, fetch=FetchType.LAZY)");
			writeLine("\t@JoinColumn(name=\"" + columnInfo.columnName + "\", " +
				"nullable=" + Boolean.toString(columnInfo.isNullable) +
				", updatable=false, insertable=false)");
			write("\tpublic " + objectName + " get" + name + "()");
			writeLine(" { return " + memberName + "; }");
			writeLine();
			writeLine("\t/** CMR mutator - sets the " + name + " property as a");
			writeLine("\t    \"" + objectName + "\" entity.");
			writeLine("\t*/");
			write("\tpublic void set" + name + "(" + objectName + " newValue)");
			writeLine(" { " + memberName + " = newValue; }");
		}
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
		@param args1 Output directory.
		@param args2 URL to the data source.
		@param args3 data source login name.
		@param args4 data source password.
		@param args5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param args6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param args7 package name of the entity bean CMP classes.
		@param args8 version JavaDoc value.
	*/
	public static void main(String args[])
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			File fileOutputDir = extractOutputDirectory(args, 0);
			String strAuthor = extractAuthor(args, 5);
			String strPackageName = extractArgument(args, 6, null);
			String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			Tables pTables = extractTables(args, 1, 7);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanCMP3 pGenerator =
				new EntityBeanCMP3((PrintWriter) null, strAuthor,
				(Tables.Record) null, strPackageName, version);

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

			System.out.println("Usage: java " + EntityBeanCMP3.class.getName() + " Output directory");
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
