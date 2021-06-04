# small-library

### Build

- gradle shadowJar

### Documentation Generator

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.doc.TablesHtml tables.html "${URL}" $DBUSER $DBPWD $DRIVER

### Hibernate Generator

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityBeanJPA out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.entity $VERSION $TABLE_NAME

### POJO Generators

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityBeanValueObject out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.value $VERSION $TABLE_NAME

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityBeanFilter out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.filter $VERSION $TABLE_NAME

### Dropwizard Generators

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityBeanDAO out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.dao $VERSION $TABLE_NAME

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityBeanDAOTest out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.dao $VERSION $TABLE_NAME

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityJerseyResource out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.rest $VERSION $TABLE_NAME

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityJerseyResourceTest out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.rest $VERSION $TABLE_NAME

### JDBI Generators

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.JDBiMapper out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.mapper $VERSION $TABLE_NAME

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.JDBiSqlObject out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.mapper $VERSION $TABLE_NAME

### Elasticsearch Generators

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.EntityBeanES out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.es $VERSION $TABLE_NAME

### Redshift Generators

- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.RedshiftBatch out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.batch $VERSION $TABLE_NAME
- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.RedshiftBatchTest out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.batch $VERSION $TABLE_NAME
- java -cp $DB_CLASSPATH:$SMALL_LIBRARY_JAR com.small.library.ejb.gen.RedshiftLoader out "${URL}" $DBUSER $DBPWD $DRIVER "$AUTHOR" $PACKAGE.loader $VERSION $TABLE_NAME

---

*$TABLE_NAME* is optional. If omitted, all the tables are generated.