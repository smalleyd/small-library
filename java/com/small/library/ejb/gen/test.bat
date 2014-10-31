setlocal

set DIR_OUTPUT="output"
set JDBC_URL="jdbc:mysql://localhost:3306/running_journal"
set JDBC_USER=root
set JDBC_PASSWORD=password
set JDBC_DRIVER=com.mysql.jdbc.Driver
set AUTHOR="David Small"
set PACKAGE_NAME=com.running.journal.ejb.entity

if exist %DIR_OUTPUT goto outputDirExists
	mkdir %DIR_OUTPUT%

:outputDirExists

java com.small.library.ejb.gen.EntityBeanHome %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME%
java com.small.library.ejb.gen.EntityBeanPrimaryKey %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME%
java com.small.library.ejb.gen.EntityBeanRemote %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME%
java com.small.library.ejb.gen.EntityBeanCMP %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME%
java com.small.library.ejb.gen.EntityBeanValueObject %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME%
java com.small.library.ejb.gen.EntityBeanHomeFactory %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME% EJBHomeFactory
java com.small.library.ejb.gen.EntityBeanDescriptor %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% %PACKAGE_NAME%
java com.small.library.ejb.gen.EntityBeanJbossDescriptor %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR% "running-journal"
java com.small.library.ejb.gen.EntityBeanJaws %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR%
java com.small.library.ejb.gen.EntityBeanJbossCmp %DIR_OUTPUT% %JDBC_URL% %JDBC_USER% %JDBC_PASSWORD% %JDBC_DRIVER% %AUTHOR%

endlocal
