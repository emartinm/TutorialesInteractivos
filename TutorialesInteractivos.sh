#!/usr/bin/env bash

# Lanza la herramienta "Tutoriales Interactivos" 
# Primero prueba la version de Java en el directorio ./jre

export PATH=./jre64_linux/bin:${PATH}
JAVA_COMMAND=$(which java)
echo `${JAVA_COMMAND} -version`

${JAVA_COMMAND} -jar target/TutorialesInteractivos-jar-with-dependencies.jar
