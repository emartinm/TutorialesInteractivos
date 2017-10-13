#!/usr/bin/env bash

# Lanza la herramienta "Tutoriales Interactivos" 
# Primero prueba la version de Java en el directorio ./jre64_linux o ./jre64_mac

SCRIPT_PATH=`realpath ${0}`
TUTORIALES_PATH=`dirname ${SCRIPT_PATH}`
echo ${TUTORIALES_PATH}
OS=$(uname)
if [ ${OS} == "Linux" ]
then
    # Es un sistema Linux  
    export PATH=${TUTORIALES_PATH}/jre/jre64_linux/bin:${PATH}    
else
    # Es un sistema Darwin (Mac)
    export PATH=${TUTORIALES_PATH}/jre/jre64_mac/Contents/Home/bin:${PATH}
fi

echo ${PATH}
JAVA_COMMAND=$(which java)
echo ${JAVA_COMMAND}

${JAVA_COMMAND} -jar ${TUTORIALES_PATH}/target/TutorialesInteractivos-jar-with-dependencies.jar
