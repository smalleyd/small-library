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
	public static final String CLASS_NAME_SUFFIX = "DAO";

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
	public EntityBeanDAO(PrintWriter writer,
		String author, Table table)
	{
		super(writer, author, table);
	}

	public EntityBeanDAO(final String author, final String packageName, final String version)
	{
		this(null, author, null, packageName, version);
	}

	/** Constructor - constructs a populated object.
		@param writer The output stream.
		@param author Name of the author.
		@param table A table record object to base the output on.
		@param packageName Package name of the wrapper class.
		@param version Represents the application version.
	*/
	public EntityBeanDAO(PrintWriter writer,
		String author, Table table, String packageName, String version)
	{
		super(writer, author, table, packageName, version);
	}

	@Override
	public void generate() throws GeneratorException, IOException
	{
		populateColumnInfo();

		writeHeader();
		writeClassDeclaration();

		writeMethods();

		writeFooter();
	}

	/** Accessor method - gets the name of the output file based on a table name.
	    Used by BaseTable.generatorTableResources.
	*/
	public String getOutputFileName(Table table)
	{
		// Name should NOT have a suffix.
		return getClassName(createObjectName(table.name)) + ".java";
	}

	/** Helper method - gets the value object name. */
	public String getValueObjectName()
	{
		return EntityBeanValueObject.getClassName(getObjectName());
	}

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
		writeLine("import static " + domainPackageName + ".dwservice.dao.OrderByBuilder.*;");
		writeLine();
		writeLine("import java.util.stream.Collectors;");
		writeLine();
		writeLine("import org.hibernate.*;");
		writeLine("import org.hibernate.criterion.*;");
		writeLine("import org.jdbi.v3.core.Jdbi;");
		writeLine("import org.jdbi.v3.sqlobject.config.RegisterRowMapper;"); 
		writeLine("import org.jdbi.v3.sqlobject.customizer.Bind;");
		writeLine("import org.jdbi.v3.sqlobject.statement.SqlQuery;"); 
		writeLine();
		writeLine("import " + domainPackageName + ".dwservice.dao.*;");
		writeLine("import " + domainPackageName + ".dwservice.errors.ValidationException;");
		writeLine("import " + domainPackageName + ".dwservice.errors.Validator;");
		writeLine("import " + domainPackageName + ".dwservice.hibernate.AbstractDAO;");
		writeLine("import " + domainPackageName + ".dwservice.hibernate.HibernateQueryBuilder;");
		writeLine("import " + domainPackageName + ".dwservice.jdbi.*;");
		writeLine("import " + basePackageName + ".entity.*;");
		writeLine("import " + basePackageName + ".filter." + EntityBeanFilter.getClassName(name) + ";");
		writeLine("import " + basePackageName + ".mapper." + JDBiMapper.getClassName(name) + ";");
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
		final String name = getClassName();
		final String objectName = getObjectName();
		final String mapperName = JDBiMapper.getClassName(objectName);

		writeLine();
		write("public class " + name + " extends AbstractDAO<");
			write(getObjectName());
			writeLine(">");
		writeLine("{");
		writeLine("private static final String SELECT = \"SELECT OBJECT(o) FROM " + objectName + " o\";", 1);
		writeLine("private static final String COUNT = \"SELECT COUNT(o." + columnInfo[0].memberVariableName + ") FROM " + objectName + " o\";", 1);
		writeLine("private static final " + mapperName + " MAPPER = new " + mapperName + "();", 1);
		writeLine("private static final OrderByBuilder ORDER = new OrderByBuilder('o', ", 1);
		int size = columnInfo.length;
		for (ColumnInfo i : columnInfo)
		{
			write("\"" + i.memberVariableName + "\", ", 2);
			if (i.isAutoIncrementing || i.isBoolean || i.isRange())
				write("DESC");
			else
				write("ASC");

			if (0 < --size)
				writeLine(",");
			else
				writeLine(");");
		}
		writeLine();
		writeLine("/** Native SQL clauses. */", 1);
		writeLine("public static final String FROM_ALIAS = \"o\";", 1);
		writeLine();
		writeLine("@RegisterRowMapper(" + mapperName + ".class)", 1);
		writeLine("private static interface READER", 1);
		writeLine("{", 1);
		writeLine("}", 1);
		writeLine();
		writeLine("private final Jdbi dbi;", 1);
		writeLine("private final READER reader;", 1);
		writeLine();
		writeLine("public " + name + "(final SessionFactory factory)", 1);
		writeLine("{", 1);
		writeLine("super(factory);", 2);
		writeLine("reader = (dbi = JDBiUtils.getReader(factory)).onDemand(READER.class);", 2);
		writeLine("");
		writeLine("}", 1);
		writeLine();
	}

	/** Output method - writes the accessor methods. */
	private void writeMethods() throws IOException
	{
		var name = getObjectName();
		var valueName = getValueObjectName();
		var primaryKeyType = this.getPkJavaType();
		var filterName = EntityBeanFilter.getClassName(name);

		writeLine("/** Adds a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return never NULL.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + valueName + " add(final " + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return value.withId(persist(toEntity(value, _validate(value.withId(null)))).getId());", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Updates a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public " + valueName + " update(final " + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("var cmrs = _validate(value);", 2);
			writeLine("var record = (" + name + ") cmrs[0];", 2);
			writeLine("if (null == record)", 2);
			writeLine("record = findWithException(value.id);", 3);
			writeLine();
			writeLine("return value.withId(toEntity(value, record, cmrs).getId());", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Validates a single " + name + " value.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public void validate(final " + valueName + " value) throws ValidationException", 1);
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
		writeLine("private Object[] _validate(final " + valueName + " value) throws ValidationException", 1);
		writeLine("{", 1);
		writeLine("value.clean();", 2);
		writeLine("var validator = new Validator();", 2);
		writeLine();
		boolean first = true;
		writeLine("// Throw exception after field existence checks and before FK checks.", 2);
		for (var i : columnInfo)
		{
			if (i.isAutoIncrementing)
				continue;

			var validator = first ? "validator" : "\t";
			if (i.isString)
			{
				final String maxLenName = valueName + ".MAX_LEN_" + i.columnName.toUpperCase();
				if (i.isNullable)
					writeLine(validator + ".ensureLength(\"" + i.memberVariableName + "\", \"" + i.name + "\", value." + i.memberVariableName + ", " + maxLenName + ")", 2);
				else
					writeLine(validator + ".ensureExistsAndLength(\"" + i.memberVariableName + "\", \"" + i.name + "\", value." + i.memberVariableName + ", " + maxLenName + ")", 2);
			}
			else if (!i.isNullable && !i.isPrimitive)
				writeLine(validator + ".ensureExists(\"" + i.memberVariableName + "\", \"" + i.name + "\", value." + i.memberVariableName + ")", 2);
			else
				continue;	// Make sure to call first = false below unless validation is written. DLS on 12/16/2019.

			first = false;
		}
		writeLine(".check();", 3);
		writeLine();
		writeLine("// Validation foreign keys.", 2);
		writeLine("var session = currentSession();", 2);
		var cmrVars = new LinkedList<String>();
		for (var i : columnInfo)
		{
			if (!i.isImportedKey)
				continue;

			int tabs = 2;
			if (i.isNullable)
			{
				writeLine("if (null != value." + i.memberVariableName + ")", tabs);
				writeLine("{", tabs++);
			}

			writeLine("var " + i.importedKeyMemberName + " = session.get(" + i.importedObjectName + ".class, value." + i.memberVariableName + ");", tabs);
			writeLine("if (null == " + i.importedKeyMemberName + ")", tabs);
			writeLine("validator.add(\"" + i.memberVariableName + "\", \"The " + i.name + ", %" + (i.isString ? "s" : "d") + ", is invalid.\", value." + i.memberVariableName + ");", tabs + 1);

			if (i.isNullable)
				writeLine("}", tabs - 1);
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
		writeLine(" * @param id", 1);
		writeLine(" * @return TRUE if the entity is found and removed.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public boolean remove(final " + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("var record = get(id);", 2);
			writeLine("if (null == record) return false;", 2);
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
		writeLine(" * @throws ValidationException if the identifier is invalid.", 1);
		writeLine(" */", 1);
		writeLine("public " + name + " findWithException(final " + primaryKeyType + " id) throws ValidationException", 1);
		writeLine("{", 1);
		writeLine("var record = get(id);", 2);
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
		writeLine("public " + valueName + " getById(final " + primaryKeyType + " id)", 1);
		writeLine("{", 1);
		writeLine("var record = get(id);", 2);
		writeLine("if (null == record) return null;", 2);
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
		writeLine("public " + valueName + " getByIdWithException(final " + primaryKeyType + " id) throws ValidationException", 1);
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
		writeLine("public QueryResults<" + valueName + ", " + filterName + "> search(final " + name + "Filter filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("var builder = createQueryBuilder(filter.clean(), SELECT);", 2);
			writeLine("var v = new QueryResults<" + valueName + ", " + filterName + ">(builder.aggregate(COUNT), filter);", 2);
			writeLine("if (v.isEmpty()) return v;", 2);
			writeLine();
			writeLine("return v.withRecords(builder.orderBy(ORDER.normalize(v)).run(v).stream().map(o -> toValue(o)).collect(Collectors.toList()));", 2);
			writeLine();
			writeLine("/*", 2);
			writeLine("final Criteria criteria = createCriteria(filter.clean());", 2);
			writeLine("final QueryResults<" + valueName + ", " + filterName + "> value = new QueryResults<>(count(criteria), filter);", 2);
			writeLine("if (value.isEmpty())", 2);
				writeLine("return value;", 3);
			writeLine();
			writeLine("criteria.setProjection(null)", 2);
			writeLine(".setResultTransformer(Criteria.ROOT_ENTITY)", 3);
			writeLine(".setFirstResult(value.retrieveFirstResult())", 3);
			writeLine(".setMaxResults(value.getPageSize());", 3);
			writeLine();
			writeLine("return value.withRecords(list(ORDER.build(criteria, value)).stream().map(o -> toValue(o)).collect(Collectors.toList()));", 2);
			writeLine("*/", 2);
		writeLine("}", 1);
		writeLine();
		writeLine("/** Counts the number of " + name + " entities based on the supplied filter.", 1);
		writeLine(" *", 1); 
		writeLine(" * @param value", 1);
		writeLine(" * @return zero if none found.", 1);
		writeLine(" * @throws ValidationException", 1);
		writeLine(" */", 1);
		writeLine("public long count(final " + filterName + " filter) throws ValidationException", 1);
		writeLine("{", 1);
			writeLine("return createQueryBuilder(filter.clean(), null).aggregate(COUNT);", 2);
			writeLine("// return count(createCriteria(filter.clean()));", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - counts the number of " + name + " entities based on the supplied criteria. */", 1);
		writeLine("private long count(final Criteria criteria)", 1);
		writeLine("{", 1);
		writeLine("return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).longValue();", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates the a standard Hibernate query builder. */", 1);
		writeLine("private QueryBuilder<" + name + "> createQueryBuilder(final " + filterName + " filter, final String select)", 1);
			writeLine("throws ValidationException", 2);
		writeLine("{", 1);
			writeLine("return new HibernateQueryBuilder<" + name + ">(currentSession(), select, " + name + ".class)", 2);
			int size = columnInfo.length;
			for (var info : columnInfo)
			{
				if (info.isString)
					write(".addContains(\"" + info.memberVariableName + "\", \"o." + info.memberVariableName + " LIKE :" + info.memberVariableName + "\", filter." + info.memberVariableName + ")", 3);
				else
					write(".add(\"" + info.memberVariableName + "\", \"o." + info.memberVariableName + " = :" + info.memberVariableName + "\", filter." + info.memberVariableName + ")", 3);

				// If NULLable, add a NULL check filter option.
				if (info.isNullable)
				{
					writeLine();
					write(".addNotNull(\"o." + info.memberVariableName + "\", filter.has" + info.name + ")", 3);
				}

				// For values that have ranges, also add greater-than (>=) and less-than (<=) searches. DLS on 6/11/2015.
				if (info.isRange())
				{
					writeLine();
					writeLine(".add(\"" + info.memberVariableName + "From\", \"o." + info.memberVariableName + " >= :" + info.memberVariableName + "From\", filter." + info.memberVariableName + "From)", 3);
					write(".add(\"" + info.memberVariableName + "To\", \"o." + info.memberVariableName + " <= :" + info.memberVariableName + "To\", filter." + info.memberVariableName + "To)", 3);
				}

				writeLine((0 == --size) ? ";" : "");
			}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates the a native SQL query. */", 1);
		writeLine("private <T> QueryBuilder<T> createNativeQuery(final " + filterName + " filter, final String select, final Class<T> entityClass, final String groupBy)", 1);
			writeLine("throws ValidationException", 2);
		writeLine("{", 1);
			writeLine("return new NativeQueryBuilder<>(currentSession(), select, entityClass, FROM_ALIAS, groupBy)", 2);
			size = columnInfo.length;
			for (ColumnInfo info : columnInfo)
			{
				if (info.isString)
					write(".addContains(\"" + info.memberVariableName + "\", \"o." + info.columnName + " LIKE :" + info.memberVariableName + "\", filter." + info.memberVariableName + ")", 3);
				else
					write(".add(\"" + info.memberVariableName + "\", \"o." + info.columnName + " = :" + info.memberVariableName + "\", filter." + info.memberVariableName + ")", 3);

				// If NULLable, add a NULL check filter option.
				if (info.isNullable)
				{
					writeLine();
					write(".addNotNull(\"o." + info.columnName + "\", filter.has" + info.name + ")", 3);
				}

				// For values that have ranges, also add greater-than (>=) and less-than (<=) searches. DLS on 6/11/2015.
				if (info.isRange())
				{
					writeLine();
					writeLine(".add(\"" + info.memberVariableName + "From\", \"o." + info.columnName + " >= :" + info.memberVariableName + "From\", filter." + info.memberVariableName + "From)", 3);
					write(".add(\"" + info.memberVariableName + "To\", \"o." + info.columnName + " <= :" + info.memberVariableName + "To\", filter." + info.memberVariableName + "To)", 3);
				}

				writeLine((0 == --size) ? ";" : "");
			}
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates the Hibernate query Criteria based on the supplied " + name + " filter. */", 1);
		writeLine("public Criteria createCriteria(final " + filterName + " filter)", 1);
			writeLine("throws ValidationException", 2);
		writeLine("{", 1);
			writeLine("Criteria criteria = criteria();", 2);
			writeLine();
			for (ColumnInfo info : columnInfo)
			{
				if (info.isString)
				{
					writeLine("if (null != filter." + info.memberVariableName + ")", 2);
					writeLine("criteria.add(Restrictions.like(\"" + info.memberVariableName + "\", filter." + info.memberVariableName + ", MatchMode.START));", 3);
				}
				else
				{
					writeLine("if (null != filter." + info.memberVariableName + ")", 2);
					writeLine("criteria.add(Restrictions.eq(\"" + info.memberVariableName + "\", filter." + info.memberVariableName + "));", 3);
				}

				// For values that have ranges, also add greater-than (>=) and less-than (<=) searches. DLS on 6/11/2015.
				if (info.isRange())
				{
					writeLine("if (null != filter." + info.memberVariableName + "From)", 2);
					writeLine("criteria.add(Restrictions.ge(\"" + info.memberVariableName + "\", filter." + info.memberVariableName + "From));", 3);
					
					writeLine("if (null != filter." + info.memberVariableName + "To)", 2);
					writeLine("criteria.add(Restrictions.le(\"" + info.memberVariableName + "\", filter." + info.memberVariableName + "To));", 3);
				}
			}
			writeLine();
			writeLine("return criteria;", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates a non-transactional value from a transactional entity. */", 1);
		writeLine("private " + valueName + " toValue(final " + name + " record)", 1);
		writeLine("{", 1);
			writeLine("var value = new " + valueName + "(", 2);
		int i = 0, last = columnInfo.length - 1;
		for (ColumnInfo info : columnInfo)
			writeLine("record." + info.accessorMethodName + "()" + ((i++ < last) ? "," : ");"), 3);
		writeLine();
		for (ColumnInfo info : columnInfo)
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
		writeLine("/** Helper method - creates a transactional entity from a non-transactional value. */", 1);
		writeLine("public " + name + " toEntity(final " + valueName + " value, final Object[] cmrs)", 1);
		writeLine("{", 1);
			writeLine("return new " + name + "(", 2);
		i = 0;
		int cmrs = 1;
		for (ColumnInfo info : columnInfo)
		{
			if (!info.isPartOfPrimaryKey)
			{
				if (info.isImportedKey)
					writeLine("(" + info.importedObjectName + ") cmrs[" + cmrs++ + "]" + ((i < last) ? "," : ");"), 3);
				else
					writeLine("value." + info.memberVariableName + ((i < last) ? "," : ");"), 3);
			}
			i++;
		}
		writeLine("}", 1);
		writeLine();
		writeLine("/** Helper method - populates the transactional entity from the non-transactional value. */", 1);
		writeLine("public " + name + " toEntity(final " + valueName + " value, final " + name + " record, final Object[] cmrs)", 1);
		writeLine("{", 1);
		cmrs = 1;
		for (ColumnInfo info : columnInfo)
		{
			if (info.isPartOfPrimaryKey)
				continue;

			if (info.isImportedKey)
				writeLine("record.put" + info.importedKeyName + "((" + info.importedObjectName + ") cmrs[" + cmrs++ + "]);", 2);
			else
				writeLine("record." + info.mutatorMethodName + "(value." + info.memberVariableName + ");", 2);
		}
		writeLine();
		writeLine("return record;", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - creates a DynamoDB Item from a non-transactional value. */", 1);
		writeLine("public Item toItem(final " + valueName + " value)", 1);
		writeLine("{", 1);
			write("var item = new Item().withPrimaryKey(", 2);

		// First do the primary keys.
		i = -1;
		for (ColumnInfo info : columnInfo)
		{
			if (info.isPartOfPrimaryKey)
			{
				if (0 < ++i)
					write(", ");

				write("\"");
				write(info.columnName);
				write("\", value.");
				write(info.memberVariableName);
			}
		}
		write(")");

		// Do remainder of non-null columns.
		for (ColumnInfo info : columnInfo)
		{
			if (info.isPartOfPrimaryKey || info.isNullable)
				continue;

			writeLine();	// Close prior line.
			write(".with" + info.dynamoDbType + "(\"" + info.columnName + "\", value." + info.memberVariableName + ")", 3);
		}
		writeLine(";");

		// Do remainder of nullable columns.
		for (ColumnInfo info : columnInfo)
		{
			if (!info.isNullable)
				continue;

			writeLine();
			writeLine("if (null != value." + info.memberVariableName + ")", 2);
			write("item.with" + info.dynamoDbType + "(\"" + info.columnName + "\", value." + info.memberVariableName + ");", 3);
		}

		writeLine();
		writeLine();
		writeLine("return item;", 2);
		writeLine("}", 1);

		writeLine();
		writeLine("/** Helper method - extracts a non-transactional value from a DynamoDB Item. */", 1);
		writeLine("public " + valueName + " fromItem(final Item item)", 1);
		writeLine("{", 1);
		writeLine("return new " + valueName + "(", 2);
		i = 0;
		for (ColumnInfo info : columnInfo)
		{
			if (!info.isNullable)
				writeLine("item.get" + info.dynamoDbType + "(\"" + info.columnName + "\")" + ((++i < columnInfo.length) ? "," : ");"), 3);
			else
				writeLine("item.isPresent(\"" + info.columnName + "\") ? item.get" + info.dynamoDbType + "(\"" + info.columnName + "\") : null" + ((++i < columnInfo.length) ? "," : ");"), 3);
		}

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
		@param args7 package name of the entity bean value object.
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
			final String version = extractArgument(args, 7, null);

			// Create and load the tables object.
			final List<Table> tables = extractTables(args, 1, 8);

			// Create the SQL Repository Item Descriptor generator.
			final EntityBeanDAO generator = new EntityBeanDAO((PrintWriter) null, author, (Table) null, packageName, version);

			// Call the BaseTable method to handle the outputting.
			generateTableResources(generator, tables, dir);
		}

		catch (final IllegalArgumentException ex)
		{
			final String message = ex.getMessage();

			if (null != message)
			{
				System.out.println(message);
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

		catch (final Exception ex) { ex.printStackTrace(); }
	}
}
