@echo off
set JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF8"
java -Xms500M -Xmx2048M -Xmn200M -XX:+UseG1GC -jar ./dbx.war --logging.file=./dbx.log
