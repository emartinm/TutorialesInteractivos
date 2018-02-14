![build passing](https://travis-ci.org/emartinm/TutorialesInteractivos.svg?branch=master)

# Sistema de tutoriales interactivos para el aprendizaje de lenguajes de programación 

## Autores
La versión inicial de la herramienta fue producto del Trabajo Fin de Grado del curso 2015/16 de *Rafael Caturla* y 
*Carlos Congosto*, realizado en el Dpto. de Sistemas Informáticos y Computación de la Facultad de Informática de la Universidad Complutense de Madrid y dirigido por Manuel
Montenegro y Enrique Martín ([http://eprints.ucm.es/38408/](http://eprints.ucm.es/38408/)). 
El proyecto ha sido continuado y mejorado como parte de los siguientes Proyectos de Innovación del Programa Innova-Docencia de la 
Universidad Complutense de Madrid: 
* **Convocatoria 2016/2017, proyecto #35**: *"Implementación de un sistema para el aprendizaje de lenguajes de programación mediante tutoriales interactivos"*.
* **Convocatoria 2017/18, proyecto #26**: *"Tutoriales interactivos para el estudio de la programación: impacto en el aprendizaje*  

**Participantes**:
* Jorge Carmona (Fac. Matemáticas, UCM)
* Carlos Gregorio (Fac. Matemáticas, UCM)
* Francisco J. López (Fac. Informática, UCM)
* Enrique Martín (Fac. Informática, UCM)
* Manuel Montenegro (Fac. Informática, UCM)
* Adrián Riesco (Fac. Informática, UCM)
* Jaime Sánchez (Fac. Informática, UCM)
* Salvador Tamarit (Fac. Informática, UPV)

**Antiguos participantes**:
* Carlos Congosto
* Rafael Caturla

## Ejecución
_(Ver más detalles en el [manual de usuario](doc/Manual_usuario.pdf))_

Se puede ejecutar la última versión compilada que hay en el repositorio haciendo 
doble clic sobre el fichero `TutorialesInteractivos-jar-with-dependencies.jar` 
que está dentro de la carpeta `target`. 

También se puede ejecutar desde un terminal:
  1. Abrir un terminal en el directorio donde está la carpeta `src/` y el fichero `pom.xml`
  1. Ejecutar `java -jar target/TutorialesInteractivos-jar-with-dependencies.jar`

Si se desea compilar y ejecutar la herramienta a partir de las fuentes, se necesita tener *maven* instalado:
  1. Abrir un terminal en el directorio donde está la carpeta `src/` y el fichero `pom.xml`
  1. Ejecutar `mvn package` para compilar
  2. Lanzar la aplicación con `java -jar target/TutorialesInteractivos-jar-with-dependencies.jar`
  
##  Documentación

El sistema de tutoriales interactivos se describe en dos manuales, que se pueden encontrar en la carpeta 
[doc](doc/):

 - **[Manual de usuario](doc/Manual_usuario.pdf)**: 
  donde se explica el funcionamiento del sistema desde el punto de vista del
  alumno, centrándose en la navegación y los distintos elementos de la interfaz gráfica.
 - **[Manual del profesor](doc/Manual_crear_lecciones.pdf)**:
  donde se describe con detalle el formato de los temas, los distintos elementos que componene las lecciones y cómo 
   se pueden definir nuevos temas.
   
En esa misma carpeta [doc](doc/) también podéis encontrar las transparencias utilizadas
en presentaciones y talleres:
 - **[Tutoriales Interactivos en el Aprendizaje de la Programación](doc/Taller_FDI_2017.pdf)**: Taller
 sobre la herramienta celebrado el 30 de mayo en la Fac. de Informática de la UCM.
