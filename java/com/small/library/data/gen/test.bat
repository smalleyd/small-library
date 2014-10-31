setlocal

set PACKAGE_NAME=com.running.journal.ejb.entity
REM set PACKAGE_NAME=""

java com.small.library.data.gen.TableMetaData output "jdbc:mysql://localhost:3306/running_journal" root password "org.gjt.mm.mysql.Driver" "David Small" %PACKAGE_NAME%

endlocal
