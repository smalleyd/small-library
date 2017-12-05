package com.small.library.ejb.gen;

import java.io.*;
import java.util.List;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates class for EJB Entity Bean CMP 3.x classes. The CMP 3.x classes
*	are generated from metadata of the tables that they represent. The metadata
*	is retrieves from the JDBC <I>DatabaseMetadata</I> interface.
*
*	The classes are annotated fully.
*
*	@author David Small
*	@version 1.1.0.0
*	@since 11/25/2005
*
***************************************************************************************/

public class EntityBeanCMP3 extends EntityBeanBase
{
	public String version = null;

	public static final String CLASS_NAME_SUFFIX = "";
	public static final String VERSION_DEFAULT = "1.0.1";

	/** Helper method - gets the full class/interface name of the EJB
	    class from the entity name.
		@param name Name of the entity.
	*/
	public static String getClassName(String name)
	{
		return name + CLASS_NAME_SUFFIX;
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanCMP3(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanCMP3(PrintWriter writer,
		String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public EntityBeanCMP3(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanCMP3(PrintWriter writer,
		String author, Table table, String packageName,
		String version)
	{
		super(writer, author, table, packageName);

		this.version = version;
	}

	@Override
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

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName() + ".java";
	}

	/** Output method - writes the file header. */
	private void writeHeader() throws IOException
	{
		String packageName = getPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
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
		writeLine("*\tEntity Bean CMP class that represents the " + getTable().name + " table.");
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
		writeLine("@Table(name=\"" + getTable().name + "\")");
		writeLine("@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region=\"" + getTable().name + "\")");
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
		if (0 >= columnInfo.length)
			return;

		// Write member variables.
		for (int i = 0; i < columnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Member variable - represents the \"" + columnInfo[i].columnName + "\" field. */");
			writeLine("\tpublic " + columnInfo[i].javaType + " " + columnInfo[i].memberVariableName + ";");
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
		for (int i = 0; i < columnInfo.length; i++)
		{
			ColumnInfo item = columnInfo[i];
			writeLine("\t\t@param " + item.memberVariableName + " represents the \"" +
				item.columnName + "\" field.");
		}

		writeLine("\t*/");

		// Constructor signature.
		write("\tpublic " + getClassName() + "(");
		for (int i = 0, last = columnInfo.length - 1; i < columnInfo.length; i++)
		{
			ColumnInfo item = columnInfo[i];

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
		for (int i = 0; i < columnInfo.length; i++)
		{
			ColumnInfo item = columnInfo[i];
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
		if (0 >= columnInfo.length)
			return;

		// Write accessors.
		for (int i = 0; i < columnInfo.length; i++)
		{
			ColumnInfo item = columnInfo[i];
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
					writeLine(" @GeneratedValue(strategy=GenerationType.IDENTITY)");
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
		if (0 >= columnInfo.length)
			return;

		// Write mutators.
		for (int i = 0; i < columnInfo.length; i++)
		{
			writeLine();
			writeLine("\t/** Mutator method - sets the property that represents the \"" +
				columnInfo[i].columnName + "\" field. */");
			write("\tpublic void " + columnInfo[i].mutatorMethodName + "(" + columnInfo[i].javaType + " newValue)");
				writeLine(" { " + columnInfo[i].memberVariableName + " = newValue; }");
		}
	}

	/** Output method - writes the imported foreign key accessor methods. */
	private void writeImportedKeysAccessorMethods() throws IOException
	{
		// Start the section.
		writeLine();
		writeLine("/**************************************************************************", 1);
		writeLine("*", 1);
		writeLine("*\tAccessor methods - Imported foreign keys", 1);
		writeLine("*", 1);
		writeLine("**************************************************************************/", 1);

		// Write accessors.
		for (final ColumnInfo info : columnInfo)
		{
			if (!info.isImportedKey)
				continue;

			String name = info.importedKeyName;
			String memberName = info.importedKeyMemberName;
			String objectName = info.importedObjectName;

			writeLine();
			writeLine("/** CMR member - the " + name + " property as a \"" + objectName + "\" entity. */", 1);
			writeLine("public " + objectName + " " + memberName + ";", 1);
			writeLine();
			writeLine("/** CMR accessor - gets the " + name + " property as a " + objectName + "\" entity. */", 1);
			writeLine("@ManyToOne(cascade={}, fetch=FetchType.LAZY)", 1);
			writeLine("@JoinColumn(name=\"" + info.columnName + "\", " +
				"nullable=" + Boolean.toString(info.isNullable) +
				", updatable=false, insertable=false)", 1);
			write("public " + objectName + " get" + name + "()", 1);
			writeLine(" { return " + memberName + "; }");
			writeLine();
			writeLine("/** CMR mutator - sets the " + name + " property as a " + objectName + "\" entity. */", 1);
			write("public void set" + name + "(" + objectName + " newValue)", 1);
			writeLine(" { " + memberName + " = newValue; }");
		}
	}

	/** Output method - writes the class footer. */
	private void writeFooter() throws IOException
	{
		writeLine("}");
	}

	/** Accessor method - gets the Class Name of the resource. */
	public String getClassName() { return getClassName(getObjectName()); }

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
	public static void main(final String... args)
	{
		try
		{
			// Have enough arguments been supplied?
			if (3 > args.length)
				throw new IllegalArgumentException("Please supply at least 3 arguments.");

			// Local variables
			final File dir = extractOutputDirectory(args, 0);
			final String author = extractAuthor(args, 5);
			final String packageName = extractArgument(args, 6, null);
			final String version = extractArgument(args, 7, VERSION_DEFAULT);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(new EntityBeanCMP3(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
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

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
