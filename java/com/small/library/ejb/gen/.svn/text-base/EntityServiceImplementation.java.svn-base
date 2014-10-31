package com.small.library.ejb.gen;

import java.io.*;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates the implementation class for each entities' services. The metadata
*	is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 1/6/2012
*
***************************************************************************************/

public class EntityServiceImplementation extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "ServiceBean";

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
	public EntityServiceImplementation() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityServiceImplementation(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
	*/
	public EntityServiceImplementation(PrintWriter writer,
		String author, Tables.Record table, String packageName)
	{
		super(writer, author, table, packageName);
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

		writeMethods();

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

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
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

		writeLine("import static com.fieldlens.platform.value.Constants.*;");
		writeLine();
		writeLine("import java.util.ArrayList;");
		writeLine("import java.util.List;");
		writeLine();
		writeLine("import javax.persistence.*;");
		writeLine();
		writeLine("import org.springframework.beans.factory.annotation.Autowired;");
		writeLine("import org.springframework.stereotype.Service;");
		writeLine();
		writeLine("import com.fieldlens.common.dao.*;");
		writeLine("import com.fieldlens.common.exception.ValidationException;");
		writeLine("import com.fieldlens.common.service.ValidationService;");
		writeLine("import com.fieldlens.common.value.QueryResults;");
		writeLine();
		writeLine("import com.fieldlens.platform.entity.*;");
		writeLine("import com.fieldlens.platform.service.*;");
		writeLine("import com.fieldlens.platform.value.*;");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tService implementation that handles access to the " + getObjectName() + " entity.");
		writeLine("*");
		writeLine("*\t@author " + getAuthor());
		writeLine("*\t@version 1.0.1");
		writeLine("*\t@since " + getDateString());
		writeLine("*");
		writeLine("**********************************************************************************/");
	}

	/** Output method - writes the class declaration. */
	private void writeClassDeclaration() throws IOException
	{
		writeLine();
		writeLine("@Service");
		writeLine("@OrderByFields(value={");
		int i = 0, last = m_ColumnInfo.length - 1;
		for (ColumnInfo info : m_ColumnInfo)
		{
			write("@OrderByField(id=\"" + info.memberVariableName + "\", columns=\"o." + info.memberVariableName + "\")", 1);
			if (i++ < last)
				writeLine(",");
			else
				writeLine("})");
		}
		write("public class " + getClassName() + " implements ");
			write(EntityServiceInterface.getClassName(getObjectName()));
			write(", EntityMapper<");
			write(getObjectName()); write(", "); write(getValueObjectName()); writeLine(">");
		writeLine("{");
		writeLine("@PersistenceContext(unitName=\"platform\") EntityManager em;", 1);
		writeLine();
		writeLine("@Autowired AuditService auditor;", 1);
		writeLine("@Autowired CodesService codes;", 1);
		writeLine("@Autowired ValidationService validator;", 1);
		writeLine();
		writeLine("private final QueryBuilderFactory<" + getObjectName() + ", " + getValueObjectName() + "> factory =", 1);
		writeLine("new QueryBuilderFactory<" + getObjectName() + ", " + getValueObjectName() + ">(this.getClass(), this);", 2);
		writeLine();
		writeLine("private final String FROM = \"FROM " + getObjectName() + " o \";", 1);
		writeLine();
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		// Get the primary key Java type. Assume that it is NOT a composite key.
		String primaryKeyType = "Integer";
		for (ColumnInfo column : m_ColumnInfo)
		{
			if (column.isPartOfPrimaryKey)
			{
				primaryKeyType = column.javaType;
				break;
			}
		}

		writeLine("/** Adds a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + getValueObjectName() + " add(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("validate(value);", 2);
			writeLine();
			writeLine("" + getObjectName() + " record = toEntity(value);", 2);
			writeLine("em.persist(record);", 2);
			writeLine("setCMRs(record);", 2);
			writeLine();
			writeLine("// Get the surrogate key.", 2);
			writeLine("value.id = record.getId();", 2);
			writeLine();
			writeLine("// Log the addition.", 2);
			writeLine("auditor.processAdd(new " + getObjectName() + "J(record));", 2);
			writeLine();
			writeLine("return value;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Updates a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void update(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("" + getObjectName() + " record = _validate(value);", 2);
			writeLine("if (null == record)", 2);
			writeLine("{", 2);
				writeLine("if (null == value.id)", 3);
					writeLine("throw new ValidationException(\"id\", \"ID\");", 4);
				writeLine("if (null == (record = em.find(" + getObjectName() + ".class, value.id)))", 3);
					writeLine("throw new ValidationException(\"id\", \"ID\", \"ID is an invalid " + getObjectName() + " ID.\");", 4);
			writeLine("}", 2);
			writeLine();
			writeLine("toEntity(value, record);", 2);
			writeLine("setCMRs(record);", 2);
			writeLine();
			writeLine("// Log the update.", 2);
			writeLine("auditor.processUpdate(new " + getObjectName() + "J(record));", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Removes a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void remove(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("" + getObjectName() + " record = em.find(" + getObjectName() + ".class, value.id);", 2);
			writeLine("if (null == record)", 2);
				writeLine("return;", 3);
			writeLine();
			writeLine("em.remove(record);", 2);
			writeLine();
			writeLine("// Log the deletion.", 2);
			writeLine("auditor.processDelete(new " + getObjectName() + "J(record));", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Validates a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void validate(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("_validate(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Validates a single " + getObjectName() + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + getObjectName() + " _validate(" + getValueObjectName() + " value) throws ValidationException", 1);
		writeLine("{", 1);
			for (ColumnInfo info : m_ColumnInfo)
			{
				// Nothing to check on primary key or booleans.
				if (info.isPartOfPrimaryKey || "boolean".equals(info.javaType))
					continue;

				boolean isString = "String".equals(info.javaType);
				String check = "\"" + info.memberVariableName + "\", CAPTION_" + info.columnName.toUpperCase() + ", value." + info.memberVariableName;
				if (!info.isNullable)
				{
					if (info.isImportedKey)
						writeLine("validator.checkNullAndIdentifier(" + check + ", " + info.importedObjectName + ".class);", 2);
					else if (isString)
						writeLine("validator.checkNullAndLength(" + check + ", " + info.size + ");", 2);
					else
						writeLine("validator.checkNull(" + check + ");", 2);
				}
				else if (info.isImportedKey)
					writeLine("validator.checkIdentifier(" + check + ", " + info.importedObjectName + ".class);", 2);
				else if (isString)
					writeLine("validator.checkLength(" + check + ", " + info.size + ");", 2);
			}
			writeLine();
			writeLine("return null;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Gets a single " + getObjectName() + " value by identifier.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return NULL if not found.", 1);
		writeLine(" */", 1);
		writeLine("public " + getValueObjectName() + " getById(" + primaryKeyType + " id)", 1);
		writeLine("{", 1);
		writeLine(getObjectName() + " record = em.find(" + getObjectName() + ".class, id);", 2);
		writeLine("if (null == record)", 2);
			writeLine("return null;", 3);
		writeLine();
		writeLine("return toValue(record);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Gets the " + getObjectName() + " entry form data.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id can be NULL for add entry forms.", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" */", 1);
		writeLine("public " + getObjectName() + "Model getModel(" + primaryKeyType + " id)", 1);
		writeLine("{", 1);
		writeLine(getValueObjectName() + " value = null;", 2);
		writeLine("if ((null == id) || (null == (value = getById(id))))", 2);
			writeLine("value = new " + getValueObjectName() + "();", 3);
		writeLine();
		writeLine(getObjectName() + "Model model = new " + getObjectName() + "Model(value);", 2);
		for (ColumnInfo info : m_ColumnInfo)
			if (info.isImportedKey)
				writeLine("model." + info.importedKeyMemberName + "Options = codes.getActive" + info.importedObjectName + "sAsListItems();", 2);
		writeLine();
		writeLine("return model;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Searches the " + getObjectName() + " entity based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param filter", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public QueryResults<" + getValueObjectName() + "> search(" + getObjectName() + "Filter filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("QueryResults<" + getValueObjectName() + "> value = new QueryResults<" + getValueObjectName() + ">();", 2);
			writeLine("QueryBuilder<" + getObjectName() + ", " + getValueObjectName() + "> builder = createQueryBuilder(filter);", 2);
			writeLine();
			writeLine("if (0L == (value.totalRecords = count(builder)))", 2);
				writeLine("return value;", 3);
			writeLine();
			writeLine("return builder.bindAndExpand(filter, value,", 2);
				writeLine("em.createQuery(\"SELECT OBJECT(o) \" + builder.from + builder.generate(filter, value)), true);", 3);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Counts the number of " + getObjectName() + " entities based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return zero if none found.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public long count(" + getObjectName() + "Filter value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return count(createQueryBuilder(value));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method. */", 1);
		writeLine("public long count(QueryBuilder<" + getObjectName() + ", " + getValueObjectName() + "> builder)", 1);
		writeLine("{", 1);
			writeLine("return builder.bindAndGetNumber(em.createQuery(\"SELECT COUNT(o.id) \" + builder.from + builder.generate()),", 2);
				writeLine("true).longValue();", 3);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method. */", 1);
		writeLine("public QueryBuilder<" + getObjectName() + ", " + getValueObjectName() + "> createQueryBuilder(" + getObjectName() + "Filter filter)", 1);
			writeLine("throws ValidationException", 2);
		writeLine("{", 1);
			writeLine("QueryBuilder<" + getObjectName() + ", " + getValueObjectName() + "> value = factory.getInstance();", 2);
			writeLine("value.from = FROM;", 2);
			writeLine();
			writeLine();
			writeLine("return value;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public " + getValueObjectName() + " toValue(" + getObjectName() + " record)", 1);
		writeLine("{", 1);
			writeLine("" + getValueObjectName() + " value = new " + getValueObjectName() + "(", 2);
		int i = 0, last = m_ColumnInfo.length - 1;
		for (ColumnInfo info : m_ColumnInfo)
			writeLine("record." + info.accessorMethodName + "()" + ((i++ < last) ? "," : ");"), 3);
			writeLine();
			for (ColumnInfo info : m_ColumnInfo)
			{
				if (!info.isImportedKey)
					continue;

				int tabs = 2;
				if (info.isNullable)
					writeLine("if (null != value." + info.memberVariableName  + ")", tabs++);
				writeLine("value." + info.importedKeyMemberName  + "Name = record.get" + info.importedKeyName + "().getName();", tabs);
			}
			writeLine();
			writeLine("return value;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public " + getObjectName() + " toEntity(" + getValueObjectName() + " value)", 1);
		writeLine("{", 1);
			writeLine("return new " + getObjectName() + "(", 2);
		i = 0;
		for (ColumnInfo info : m_ColumnInfo)
		{
			if (!info.isPartOfPrimaryKey)
				writeLine("value." + info.memberVariableName + ((i++ < last) ? "," : ");"), 3);
			else
				i++;	// Still needs to increment to find the last one.
		}
		writeLine("}", 1);
		writeLine();
		writeLine("@Override", 1);
		writeLine("public " + getObjectName() + " toEntity(" + getValueObjectName() + " value, " + getObjectName() + " record)", 1);
		writeLine("{", 1);
		for (ColumnInfo info : m_ColumnInfo)
			if (!info.isPartOfPrimaryKey)
				writeLine("record." + info.mutatorMethodName + "(value." + info.memberVariableName + ");", 2);
			writeLine();
			writeLine("return record;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("public void setCMRs(" + getObjectName() + " record)", 1);
		writeLine("{", 1);
		for (ColumnInfo info : m_ColumnInfo)
		{
			if (info.isImportedKey)
			{
				if (info.isNullable)
					writeLine("record." + info.importedKeyMemberName + " = (null == record." + info.memberVariableName +
						") ? null : em.find(" + info.importedObjectName + ".class, record." + info.memberVariableName + ");", 2);
				else
					writeLine("record." + info.importedKeyMemberName + " = em.find(" + info.importedObjectName + ".class, record." + info.memberVariableName + ");", 2);
			}
		}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Gets history of changes for a specific " + getObjectName() + ".", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" */", 1);
		writeLine("@SuppressWarnings(\"unchecked\")", 1);
		writeLine("public QueryResults<" + getObjectName() + "JValue> history(" + primaryKeyType + " id)", 1);
		writeLine("{", 1);
			writeLine("QueryResults<" + getObjectName() + "JValue> value = new QueryResults<" + getObjectName() + "JValue>();", 2);
			writeLine();
			writeLine("List<" + getObjectName() + "J> records = em.createNamedQuery(\"find" + getObjectName() + "JByReferrer\").setParameter(1, id).getResultList();", 2);
			writeLine("value.page = value.pages = 1;", 2);
			writeLine("if (0L == (value.totalRecords = (long) (value.pageSize = records.size())))", 2);
				writeLine("return value;", 3);
			writeLine();
			writeLine("" + getObjectName() + "JValue item = null;", 2);
			writeLine("value.records = new ArrayList<" + getObjectName() + "JValue>(value.pageSize);", 2);
			writeLine("for (" + getObjectName() + "J record : records)", 2);
			writeLine("{", 2);
				writeLine("value.records.add(item = new " + getObjectName() + "JValue(record.getId(),", 3);
					for (ColumnInfo info : m_ColumnInfo)
					{
						if (info.isPartOfPrimaryKey)
							writeLine("record.get" + getObjectName() + "Id(),", 4);
						else
							writeLine("record." + info.accessorMethodName + "(),", 4);
					}
					writeLine("record.getUpdaterId(),", 4);
					writeLine("record.getActionId(),", 4);
					writeLine("record.getActionDate()));", 4);
				writeLine();
				for (ColumnInfo info : m_ColumnInfo)
				{
					if (!info.isImportedKey)
						continue;

					int tabs = 3;
					if (info.isNullable)
						writeLine("if (null != item." + info.memberVariableName  + ")", tabs++);
					writeLine("item." + info.importedKeyMemberName  + "Name = record.get" + info.importedKeyName + "().getName();", tabs);
				}
				writeLine("item.updaterName = record.getUpdater().getFullName();", 3);
				writeLine("item.actionName = record.getAction().getName();", 3);
			writeLine("}", 2);
			writeLine();
			writeLine("return value;", 2);
		writeLine("}", 1);
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
		@param strArg7 package name of the entity bean value object.
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
			EntityServiceImplementation pGenerator =
				new EntityServiceImplementation((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityServiceImplementation.class.getName() + " Output directory");
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
