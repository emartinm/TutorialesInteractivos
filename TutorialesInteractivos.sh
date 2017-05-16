#!/usr/bin/env bash

# Lanza la herramienta "Tutoriales Interactivos" 

JAVA_COMMAND=$(which java)

${JAVA_COMMAND} -jar target/TutorialesInteractivos-jar-with-dependencies.jar
