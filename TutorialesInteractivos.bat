@echo off


SET "JAVA_COMMAND=%JAVA_HOME%\bin\java.exe"
IF "%JAVA_HOME%"=="" SET JAVA_COMMAND=java

"%JAVA_COMMAND%" -jar target/TutorialesInteractivos-jar-with-dependencies.jar
if %ERRORLEVEL% EQU 9009 goto ERROR_NO_JAVA
goto END

:ERROR_NO_JAVA
echo "Please set up JAVA_HOME variable"

:END
