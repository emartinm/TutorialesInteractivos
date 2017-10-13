@echo off

SET PATH=%CD%\jre64_win\bin\;%PATH%
REM echo %PATH%
java -version
java -jar target/TutorialesInteractivos-jar-with-dependencies.jar
