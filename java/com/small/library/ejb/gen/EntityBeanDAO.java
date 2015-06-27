package com.small.library.ejb.gen;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.small.library.generator.*;
import com.small.library.metadata.*;

/***************************************************************************************
*
*	Generates Dropwizard.io Hibernate data access objects. The metadata
*	is retrieved from the JDBC <I>DatabaseMetadata</I> interface.
*
*	@author David Small
*	@version 1.0.1
*	@since 5/20/2015
*
***************************************************************************************/

public class EntityBeanDAO extends EntityBeanBase
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - class name suffix. */
	public static final String CLASS_NAME_SUFFIX = "DAO";

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
	public EntityBeanDAO() { super(); }

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
	*/
	public EntityBeanDAO(PrintWriter writer,
		String author, Tables.Record table)
	{
		super(writer, author, table);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version.
	*/
	public EntityBeanDAO(PrintWriter writer,
		String author, Tables.Record table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
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
		String packageName = getPackageName();
		String basePackageName = getBasePackageName();
		String domainPackageName = getDomainPackageName();

		if (null != packageName)
		{
			writeLine("package " + packageName + ";");
			writeLine();
		}

		String name = getObjectName();
		writeLine("import java.util.stream.Collectors;");
		writeLine();
		writeLine("import org.hibernate.*;");
		writeLine("import org.hibernate.criterion.*;");
		writeLine();
		writeLine("import io.dropwizard.hibernate.AbstractDAO;");
		writeLine();
		writeLine("import " + basePackageName + ".entity.*;");
		writeLine("import " + domainPackageName + ".common.dao.QueryResults;");
		writeLine("import " + domainPackageName + ".common.error.ValidationException;");
		writeLine("import " + domainPackageName + ".common.error.Validator;");
		writeLine("import " + basePackageName + ".filter." + EntityBeanFilter.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".value." + EntityBeanValueObject.getClassName(name) + ";");
		writeLine();
		writeLine("/**********************************************************************************");
		writeLine("*");
		writeLine("*\tData access object that handles access to the " + getObjectName() + " entity.");
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
		String name = getClassName();

		writeLine();
		write("public class " + name + " extends AbstractDAO<");
			write(getObjectName());
			writeLine(">");
		writeLine("{");
		writeLine("public " + name + "(SessionFactory factory)", 1);
		writeLine("{", 1);
		writeLine("super(factory);", 2);
		writeLine("}", 1);
		writeLine();
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		String name = getObjectName();
		String valueName = getValueObjectName();
		String primaryKeyType = this.getPkJavaType();
		String filterName = EntityBeanFilter.getClassName(name);

		writeLine("/** Adds a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + valueName + " add(" + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return value.withId(persist(toEntity(value, _validate(value.withId(null)))).getId());", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Updates a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + valueName + " update(" + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("Object[] cmrs = _validate(value);", 2);
			writeLine(name + " record = (" + name + ") cmrs[0];", 2);
			writeLine("if (null == record)", 2);
			writeLine("record = findWithException(value.getId());", 3);
			writeLine();
			writeLine("return value.withId(toEntity(value, record, cmrs).getId());", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Validates a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void validate(" + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
		writeLine("_validate(value);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Validates a single " + name + " value and returns any CMR fields.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return array of CMRs entities.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("private Object[] _validate(" + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
		writeLine("value.clean();", 2);
		writeLine("Validator validator = new Validator();", 2);
		writeLine();
		for (ColumnInfo i : m_ColumnInfo)
		{
			if (i.isAutoIncrementing)
				continue;

			if (i.isString)
			{
				if (i.isNullable)
					writeLine("validator.ensureLength(\"" + i.memberVariableName + "\", \"" + i.name + "\", value." + i.memberVariableName + ", " + i.size + ");", 2);
				else
					writeLine("validator.ensureExistsAndLength(\"" + i.memberVariableName + "\", \"" + i.name + "\", value." + i.memberVariableName + ", " + i.size + ");", 2);
			}
			else if (!i.isNullable && !i.isPrimitive)
				writeLine("validator.ensureExists(\"" + i.memberVariableName + "\", \"" + i.name + "\", value." + i.memberVariableName + ");", 2);
		}
		writeLine();
		writeLine("// Throw exception after field existence checks and before FK checks.", 2);
		writeLine("validator.check();", 2);
		writeLine();
		writeLine("Session session = currentSession();", 2);
		List<String> cmrVars = new LinkedList<>();
		for (ColumnInfo i : m_ColumnInfo)
		{
			if (!i.isImportedKey)
				continue;

			writeLine(i.importedObjectName + " " + i.importedKeyMemberName + " = (" + i.importedObjectName +
				") session.get(" + i.importedObjectName + ".class, value." + i.memberVariableName + ");", 2);
			writeLine("if (null == " + i.importedKeyMemberName + ")", 2);
			writeLine("validator.add(\"" + i.memberVariableName + "\", \"The " + i.name + ", %s, is invalid.\", value." + i.memberVariableName + ");", 3);
		}
		writeLine();
		writeLine("// Throw exception if errors exist.", 2);
		writeLine("validator.check();", 2);
		writeLine();
		writeLine("return new Object[] { " + cmrVars.stream().collect(Collectors.joining(", ")) + " };", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Removes a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return TRUE if the entity is found and removed.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public boolean remove(" + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("" + name + " record = get(id);", 2);
			writeLine("if (null == record)", 2);
				writeLine("return false;", 3);
			writeLine();
			writeLine("currentSession().delete(record);", 2);
			writeLine();
			writeLine("return true;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Finds a single " + name + " entity by identifier.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException if the identifier is valid.", 1);
		writeLine(" */", 1);
		writeLine("public " + name + " findWithException(" + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
		writeLine(name + " record = get(id);", 2);
		writeLine("if (null == record)", 2);
			writeLine("throw new ValidationException(\"id\", \"Could not find the " + name + " because id '\" + id + \"' is invalid.\");", 3);
		writeLine();
		writeLine("return record;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Gets a single " + name + " value by identifier.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return NULL if not found.", 1);
		writeLine(" */", 1);
		writeLine("public " + valueName + " getById(" + primaryKeyType + " id)", 1);
		writeLine("{", 1);
		writeLine(name + " record = get(id);", 2);
		writeLine("if (null == record)", 2);
			writeLine("return null;", 3);
		writeLine();
		writeLine("return toValue(record);", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Gets a single " + name + " value by identifier.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param id", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException if the identifier is valid.", 1);
		writeLine(" */", 1);
		writeLine("public " + valueName + " getByIdWithException(" + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
		writeLine("return toValue(findWithException(id));", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Searches the " + name + " entity based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param filter", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public QueryResults<" + valueName + ", " + filterName + "> search(" + name + "Filter filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("Criteria criteria = createCriteria(filter);", 2);
			writeLine("QueryResults<" + valueName + ", " + filterName + "> value = new QueryResults<>(count(criteria), filter);", 2);
			writeLine("if (value.isEmpty())", 2);
				writeLine("return value;", 3);
			writeLine();
			writeLine("criteria.setProjection(null)", 2);
			writeLine(".setResultTransformer(Criteria.ROOT_ENTITY)", 3);
			writeLine(".setFirstResult(value.retrieveFirstResult())", 3);
			writeLine(".setMaxResults(value.getPageSize());", 3);
			writeLine();
			writeLine("return value.withRecords(list(criteria.addOrder(Order.desc(\"id\"))).stream().map(o -> toValue(o)).collect(Collectors.toList()));", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Counts the number of " + name + " entities based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return zero if none found.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public long count(" + filterName + " filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return count(createCriteria(filter));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - counts the number of " + name + " entities based on the supplied criteria. */", 1);
		writeLine("private long count(Criteria criteria)", 1);
		writeLine("{", 1);
		writeLine("return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).longValue();", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method - creates the Hibernate query Criteria based on the supplied " + name + " filter. */", 1);
		writeLine("public Criteria createCriteria(" + filterName + " filter)", 1);
			writeLine("throws ValidationException", 2);
		writeLine("{", 1);
			writeLine("Criteria criteria = criteria();", 2);
			writeLine();
			for (ColumnInfo info : m_ColumnInfo)
			{
				if (info.isString)
				{
					writeLine("if (null != filter." + info.accessorMethodName + "())", 2);
					writeLine("criteria.add(Restrictions.like(\"" + info.memberVariableName + "\", filter." + info.accessorMethodName + "(), MatchMode.START));", 3);
				}
				else
				{
					writeLine("if (null != filter." + info.accessorMethodName + "())", 2);
					writeLine("criteria.add(Restrictions.eq(\"" + info.memberVariableName + "\", filter." + info.accessorMethodName + "()));", 3);
				}

				// For values that have ranges, also add greater-than (>=) and less-than (<=) searches. DLS on 6/11/2015.
				if (info.isRange())
				{
					writeLine("if (null != filter." + info.accessorMethodName + "From())", 2);
					writeLine("criteria.add(Restrictions.ge(\"" + info.memberVariableName + "\", filter." + info.accessorMethodName + "From()));", 3);
					
					writeLine("if (null != filter." + info.accessorMethodName + "To())", 2);
					writeLine("criteria.add(Restrictions.le(\"" + info.memberVariableName + "\", filter." + info.accessorMethodName + "To()));", 3);
				}
			}
			writeLine();
			writeLine("return criteria;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method - creates a non-transactional value from a transactional entity. */", 1);
		writeLine("private " + valueName + " toValue(" + name + " record)", 1);
		writeLine("{", 1);
			writeLine("" + valueName + " value = new " + valueName + "(", 2);
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
				writeLine("if (null != value." + info.accessorMethodName  + "())", tabs++);
			writeLine("value." + info.importedKeyMemberName  + "Name = record.get" + info.importedKeyName + "().getName();", tabs);
		}
		writeLine();
		writeLine("return value;", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method - creates a transactional entity from a non-transactional value. */", 1);
		writeLine("public " + name + " toEntity(" + valueName + " value, Object[] cmrs)", 1);
		writeLine("{", 1);
			writeLine("return new " + name + "(", 2);
		i = 0;
		int cmrs = 1;
		for (ColumnInfo info : m_ColumnInfo)
		{
			if (!info.isPartOfPrimaryKey)
			{
				if (info.isImportedKey)
					writeLine("(" + info.importedObjectName + ") cmrs[" + cmrs++ + "]" + ((i < last) ? "," : ");"), 3);
				else
					writeLine("value." + info.accessorMethodName + "()" + ((i < last) ? "," : ");"), 3);
			}
			i++;
		}
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method - populates the transactional entity from the non-transactional value. */", 1);
		writeLine("public " + name + " toEntity(" + valueName + " value, " + name + " record, Object[] cmrs)", 1);
		writeLine("{", 1);
		cmrs = 1;
		for (ColumnInfo info : m_ColumnInfo)
		{
			if (info.isPartOfPrimaryKey)
				continue;

			writeLine("record." + info.mutatorMethodName + "(value." + info.accessorMethodName + "());", 2);
			if (info.isImportedKey)
				writeLine("record.set" + info.importedKeyName + "((" + info.importedObjectName + ") cmrs[" + cmrs++ + "]);", 2);
		}
		writeLine();
		writeLine("return record;", 2);
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
		@param args1 Output directory.
		@param args2 URL to the data source.
		@param args3 data source login name.
		@param args4 data source password.
		@param args5 optional JDBC driver class name. Will use JDBC-ODBC
			bridge if a drive is not supplied.
		@param args6 author of the generated classes. Will use the
			"user.name" system property value if not supplied.
		@param args7 package name of the entity bean value object.
		@param args8 application version.
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
			String version = extractArgument(args, 7, null);

			// Create and load the tables object.
			Tables pTables = extractTables(args, 1, 8);
			pTables.load();

			// Create the SQL Repository Item Descriptor generator.
			EntityBeanDAO pGenerator =
				new EntityBeanDAO((PrintWriter) null, strAuthor,
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

			System.out.println("Usage: java " + EntityBeanDAO.class.getName() + " Output directory");
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
