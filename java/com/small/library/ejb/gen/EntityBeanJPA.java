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

public class EntityBeanJPA extends EntityBeanBase
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
	public EntityBeanJPA() { super(); }

	/** Constructor - constructs a populated object.
		@param pWriter The output stream.
		@param strAuthor Name of the author.
		@param pTable A table record object to base the output on.
	*/
	public EntityBeanJPA(PrintWriter pWriter,
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
	public EntityBeanJPA(PrintWriter pWriter,
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
	public EntityBeanJPA(PrintWriter pWriter,
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

		writeAccessorMethods();
		writeImportedKeysAccessorMethods();
		writeConstructors();

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
		writeLine("import java.math.BigDecimal;");
		writeLine("import java.util.Date;");
		writeLine();
		writeLine("import javax.persistence.*;");
		writeLine();
		writeLine("import org.hibernate.annotations.Cache;");
		writeLine("import org.hibernate.annotations.CacheConcurrencyStrategy;");
		writeLine("import org.hibernate.annotations.DynamicUpdate;");
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
		writeLine("@Cacheable");
		writeLine("@DynamicUpdate");
		writeLine("@Table(name=\"" + getTable().getName() + "\")");
		writeLine("@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region=\"" + getTable().getName() + "\")");
		writeLine("public class " + getClassName() + " implements Serializable");
		writeLine("{");
		writeLine("\tpublic final static long serialVersionUID = 1L;");
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Any columns available?
		if (0 >= m_ColumnInfo.length)
			return;

		// Write accessors.
		for (int i = 0; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];
			String nullable = Boolean.toString(item.isNullable);

			writeLine();
			write("\t@Column(name=\"" + item.columnName + "\", ");
			write("columnDefinition=\"" + item.typeDefinition + "\", ");
			writeLine("nullable=" + nullable + ")");

			if (item.isPartOfPrimaryKey)
			{
				write("\t@Id");

				if (item.isAutoIncrementing)
					writeLine(" @GeneratedValue(strategy=GenerationType.IDENTITY)");
				else
					writeLine();
			}

			write("\tpublic " + item.javaType + " " + item.accessorMethodName + "()");
				writeLine(" { return " + item.memberVariableName + "; }");
			writeLine("\tpublic " + item.javaType + " " + item.memberVariableName + ";");
			write("\tpublic void " + item.mutatorMethodName + "(final " + item.javaType + " newValue)");
			writeLine(" { " + item.memberVariableName + " = newValue; }");
		}
	}

	/** Output method - writes the imported foreign key accessor methods. */
	private void writeImportedKeysAccessorMethods() throws IOException
	{
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
			writeLine("@ManyToOne(cascade={}, fetch=FetchType.LAZY)", 1);
			writeLine("@JoinColumn(name=\"" + columnInfo.columnName + "\", " +
				"nullable=" + Boolean.toString(columnInfo.isNullable) +
				", updatable=false, insertable=false)", 1);
			write("public " + objectName + " get" + name + "()", 1);
			writeLine(" { return " + memberName + "; }");
			writeLine("public " + objectName + " " + memberName + ";", 1);
			write("public void set" + name + "(final " + objectName + " newValue)", 1);
			writeLine(" { " + memberName + " = newValue; }");
		}
	}

	/** Output method - writes the <CODE>constructors</CODE>. */
	private void writeConstructors() throws IOException
	{
		// Write the default/empty constructor. */
		writeLine();
		writeLine("\tpublic " + getClassName() + "() {}");

		// Write constructor with all possible values.
		writeLine();

		// Constructor signature.
		write("\tpublic " + getClassName() + "(");
		for (int i = 0, last = m_ColumnInfo.length - 1; i < m_ColumnInfo.length; i++)
		{
			ColumnInfo item = m_ColumnInfo[i];

			if (0 < i)
				write("\t\t");

			write("final " + item.javaType + " " + item.memberVariableName);

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
		@param args8 application version number
		@param args9 table name filter
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
			Tables pTables = extractTables(args, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanJPA pGenerator =
				new EntityBeanJPA((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanJPA.class.getName() + " Output directory");
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
