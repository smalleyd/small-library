package com.small.library.ejb.gen;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.*;
import java.util.Arrays;
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

public class EntityBeanJPA extends EntityBeanBase
{
	public static final String CLASS_NAME_SUFFIX = "";

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
	public EntityBeanJPA(PrintWriter writer,
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
	public EntityBeanJPA(PrintWriter writer,
		String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public EntityBeanJPA(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanJPA(PrintWriter writer,
		String author, Table table, String packageName,
		String version)
	{
		super(writer, author, table, packageName, version);
	}

	/** Action method - generates the Entity Bean primary key class. */
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeAccessorMethods();
		writeImportedKeysAccessorMethods();
		writeConstructors();
		writeTransients();

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
		writeLine("import java.util.*;");
		writeLine();
		writeLine("import javax.persistence.*;");
		writeLine();
		writeLine("import org.apache.commons.lang3.time.DateUtils;");
		writeLine("import org.hibernate.annotations.Cache;");
		writeLine("import org.hibernate.annotations.CacheConcurrencyStrategy;");
		writeLine("import org.hibernate.annotations.DynamicUpdate;");
		writeLine();
		writeLine("import " + getBasePackageName() + ".value." + EntityBeanValueObject.getClassName(getObjectName()) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tEntity Bean CMP class that represents the " + getTable().name + " table.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version " + getVersion());
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
		writeLine("\tprivate static final long serialVersionUID = 1L;");
	}

	/** Output method - writes the accessor methods. */
	private void writeAccessorMethods() throws IOException
	{
		// Any columns available?
		if (0 >= columnInfo.length)
			return;

		// Write accessors.
		for (var item : columnInfo)
		{
			var nullable = Boolean.toString(item.isNullable);

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
		for (var info : columnInfo)
		{
			if (!info.isImportedKey)
				continue;

			var name = info.importedKeyName;
			var memberName = info.importedKeyMemberName;
			var objectName = info.importedObjectName;

			writeLine();
			writeLine("@ManyToOne(cascade={}, fetch=FetchType.LAZY)", 1);
			writeLine("@JoinColumn(name=\"" + info.columnName + "\", " +
				"nullable=" + Boolean.toString(info.isNullable) +
				", updatable=false, insertable=false)", 1);
			write("public " + objectName + " get" + name + "()", 1);
			writeLine(" { return " + memberName + "; }");
			writeLine("public " + objectName + " " + memberName + ";", 1);
			write("public void set" + name + "(final " + objectName + " newValue)", 1);
			writeLine(" { " + memberName + " = newValue; }");
			write("public void put" + name + "(final " + objectName + " newValue)", 1);
			write(" { " + info.memberVariableName + " = (");
			if (info.isNullable)
				writeLine("null != (" + memberName + " = newValue)) ? newValue.getId() : null; }");
			else
				writeLine(memberName + " = newValue).getId(); }");
			
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
		write("public " + getClassName() + "(", 1);
		for (int i = 0, last = columnInfo.length - 1; i < columnInfo.length; i++)
		{
			ColumnInfo item = columnInfo[i];

			if (0 < i)
				write("\t\t");

			write("final " + item.javaType + " " + item.memberVariableName);

			if (last == i)
				writeLine(")");
			else
				writeLine(",");
		}

		// Write body.
		writeLine("{", 1);
		for (final ColumnInfo item : columnInfo)
			writeLine("this." + item.memberVariableName + " = " + item.memberVariableName + ";", 2);
		writeLine("}", 1);

		// Write constructor with all possible values.
		writeLine();
		var valueName = EntityBeanValueObject.getClassName(getObjectName());
		writeLine("public " + getClassName() + "(final " + valueName + " value)", 1);
		writeLine("{", 1);
		for (final ColumnInfo item : columnInfo)
			writeLine("this." + item.memberVariableName + " = value." + item.memberVariableName + ";", 2);
		writeLine("}", 1);
	}

	/** Output method - writes the transient helper methods. */
	private void writeTransients() throws IOException
	{
		var clazz = getClassName();
		var valueName = EntityBeanValueObject.getClassName(getObjectName());

		// Write the equals method. */
		writeLine();
		writeLine("@Override", 1);
		writeLine("public boolean equals(final Object o)", 1);
		writeLine("{", 1);
		writeLine("if (!(o instanceof " + clazz + ")) return false;", 2);
		writeLine();
		writeLine("var v = (" + clazz + ") o;", 2);
		ColumnInfo item = columnInfo[0];
		writeLine("return " + writeEquals(item) + " &&", 2);
		int last = columnInfo.length - 1;
		for (int i = 1; i < columnInfo.length; i++)
		{
			var term = (last > i) ? " &&" : ";";
			writeLine(writeEquals(item = columnInfo[i]) + term, 3);
		}
		writeLine("}", 1);

		// Write the equals method. */
		var pks = Arrays.stream(columnInfo).filter(c -> c.isPartOfPrimaryKey).collect(toList());

		writeLine();
		writeLine("@Override", 1);
		writeLine("public int hashCode()", 1);
		writeLine("{", 1);
		if (1 == pks.size())
			writeLine("return Objects.hashCode(" + pks.get(0).memberVariableName + ");", 2);
		else if (1 < pks.size())
			writeLine("return Objects.hash(" + pks.stream().map(c -> c.memberVariableName).collect(joining(", ")) + ");", 2);
		else
			writeLine("return 0;", 2);
		writeLine("}", 1);

		// Write the toString method. */
		writeLine();
		writeLine("@Transient", 1);
		writeLine("public " + valueName + " toValue()", 1);
		writeLine("{", 1);
			writeLine("var value = new " + valueName + "(", 2);
		int i = 0;
		last = columnInfo.length - 1;
		for (var info : columnInfo)
			writeLine(info.accessorMethodName + "()" + ((i++ < last) ? "," : ");"), 3);
		writeLine();
		for (var info : columnInfo)
		{
			if (!info.isImportedKey) continue;

			int tabs = 2;
			if (info.isNullable)
				writeLine("if (null != value." + info.memberVariableName  + ")", tabs++);
			writeLine("value." + info.importedKeyMemberName  + "Name = get" + info.importedKeyName + "().getName();", tabs);
		}
		writeLine();
		writeLine("return value;", 2);
		writeLine("}", 1);
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
			generateTableResources(new EntityBeanJPA(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
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

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
