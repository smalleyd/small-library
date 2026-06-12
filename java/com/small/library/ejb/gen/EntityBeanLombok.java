package com.small.library.ejb.gen;

import com.small.library.generator.GeneratorException;
import com.small.library.metadata.Table;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/***************************************************************************************
*
*	Generates class for JPS Entity Bean that is implemented with Lombok.
*
*	The classes are annotated fully.
*
*	@author David Small
*	@version 4.0
*	@since 6/10/2026
*
***************************************************************************************/

public class EntityBeanLombok extends EntityBeanBase
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
	public EntityBeanLombok(PrintWriter writer,
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
	public EntityBeanLombok(PrintWriter writer,
                            String author, Table table, String packageName)
	{
		super(writer, author, table, packageName);
	}

	public EntityBeanLombok(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityBeanLombok(PrintWriter writer,
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
		writeLine("import java.util.List;");
		writeLine();
		writeLine("import javax.persistence.Cacheable;");
		writeLine("import javax.persistence.Column;");
		writeLine("import javax.persistence.Entity;");
		writeLine("import javax.persistence.FetchType;");
		writeLine("import javax.persistence.GeneratedValue;");
		writeLine("import javax.persistence.GenerationType;");
		writeLine("import javax.persistence.Id;");
		writeLine("import javax.persistence.JoinColumn;");
		writeLine("import javax.persistence.ManyToOne;");
		writeLine("import javax.persistence.Table;");
		writeLine();
		// writeLine("import org.apache.commons.lang3.time.DateUtils;");
		writeLine("import org.hibernate.annotations.Cache;");
		writeLine("import org.hibernate.annotations.CacheConcurrencyStrategy;");
		writeLine("import org.hibernate.annotations.DynamicUpdate;");
		writeLine();
		writeLine("import lombok.AllArgsConstructor;");
		writeLine("import lombok.Builder;");
		writeLine("import lombok.Getter;");
		writeLine("import lombok.NoArgsConstructor;");
		writeLine("import lombok.Setter;");
		// writeLine();
		// writeLine("import " + getBasePackageName() + ".value." + EntityBeanValueObject.getClassName(getObjectName()) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tJPA Entity Bean class that represents the " + getTable().name + " table.");
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
		writeLine("@Getter");
		writeLine("@Setter");
		writeLine("@NoArgsConstructor");
		writeLine("@AllArgsConstructor");
		writeLine("@Builder(setterPrefix=\"with\")");
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

			writeLine("\tprivate " + item.javaType + " " + item.memberVariableName + ";");
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

			var memberName = info.importedKeyMemberName;
			var objectName = info.importedObjectName;

			writeLine();
			writeLine("@ManyToOne(cascade={}, fetch=FetchType.LAZY)", 1);
			writeLine("@JoinColumn(name=\"" + info.columnName + "\", " +
				"nullable=" + Boolean.toString(info.isNullable) +
				", updatable=false, insertable=false)", 1);
			writeLine("private " + objectName + " " + memberName + ";", 1);
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
			generateTableResources(new EntityBeanLombok(author, packageName, version), tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
				System.out.println();
			}

			System.out.println("Usage: java " + EntityBeanLombok.class.getName() + " Output directory");
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
