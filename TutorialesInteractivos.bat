@echo off

REM Lanza la herramienta "Tutoriales Interactivos" 
REM Primero prueba la version de Java en el directorio ./jre64_windows

SET PATH=%CD%\jre\jre64_win\bin\;%PATH%
REM echo %PATH%
java -version
java -jar target/TutorialesInteractivos-jar-with-dependencies.jar %*
