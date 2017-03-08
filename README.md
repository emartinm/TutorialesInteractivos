![build passing](https://travis-ci.org/emartinm/TutorialesInteractivos.svg?branch=master)

# Sistema de tutoriales interactivos para el aprendizaje de lenguajes de programación 

## Autores
La versión inicial de la herramienta fue producto del Trabajo Fin de grado de *Rafael Caturla* y *Carlos Congosto* del curso 2015/16, 
realizado en la Facultad de Informática en la Universidad Complutense de Madrid y dirigido por Manuel Montenegro y Enrique Martín. 
El proyecto ha sido continuado y mejorado como parte del Proyecto de Innovación número 35 de la convocatoria 2016/2017 del Programa 
Innova-Docencia titulado *Implementación de un sistema para el aprendizaje de lenguajes de programación mediante tutoriales interactivos*.

Participantes:
* Enrique Martín (Fac. Informática, UCM)
* Adrián Riesco (Fac. Informática, UCM)
* Jaime Sánchez (Fac. Informática, UCM)
* Carlos Gregorio (Fac. Matemáticas, UCM)
* Francisco J. López (Fac. Informática, UCM)
* Salvador Tamarit (Fac. Informática, UPV)
* Carlos Congosto
* Rafael Caturla

## Ejecución

Se puede ejecutar la última versión compilada que hay en el repositorio de la siguiente manera:
  1. Abrir un terminal en el directorio donde está la carpeta ```src/``` y el fichero ```pom.xml```
  1. Ejecutar ```java -jar target/TutorialesInteractivos-jar-with-dependencies.jar```

Si se desea compilar y ejecutar la herramienta a partir de las fuentes, se necesita tener *maven* instalado:
  1. Abrir un terminal en el directorio donde está la carpeta ```src/``` y el fichero ```pom.xml```
  1. Ejecutar ```mvn package``` para compilar
  2. Lanzar la aplicación con ```java -jar target/TutorialesInteractivos-jar-with-dependencies.jar```
  

## Más información
Consultar la memoria disponible en : [http://eprints.ucm.es/38408/](http://eprints.ucm.es/38408/)