# Cambios

### 30/05/2017: v1.3.5

**Ampliación de características**

- Soporte para fórmulas LaTeX usando [MathJax](https://www.mathjax.org).
- Soporte para enlaces en navegador externo.
- Soporte para múltiples idiomas.
- Lanzadores directos para Windows, Mac y Linux.
- Opción para lanzar la herramienta eliminando cualquier configuración previa.
- Generación de un fichero _log_ con información de ejecución para depuración.
- Corrección de errores.


### 26/02/2017: v1.3.0

**Soporte para preguntas de código con varios huecos**

- Las preguntas de tipo código admiten varios huecos para rellenar.
- Mejoradas las ventanas para seleccionar archivos y carpetas.
- Corrección de errores.

### 06/02/2017: v1.2.0

**Soporte para varios lenguajes de programación**

- Además de lenguajes interpretados como Python, ahora se soportan lenguajes compilados (C++) y compilados 
y ejecutados (Java, C#). El diseño de la herramienta facilita la incorporación de nuevos
lenguajes (Erlang, Haskell, Go...) y distintos compiladores/intérpretes para soportar.
 distintos sistemas operativos.
- Cambios en el estilo de las explicaciones.
- Corrección de errores. 

### 01/02/2017: v1.1.0

**Superación de lecciones y temas**

- Las lecciones se marcan como superadas al llegar a la pantalla final. 
- Los temas se marcan como superados al completar todas sus lecciones.
- Simplificación del código: rediseño, eliminación de métodos no necesarios, eliminación de comentarios desactualizados.
- Corrección de errores.


### 14/01/2017: v1.0.0
**Corrección de errores y mejoras sobre la versión inicial en [https://github.com/Kherdu/TFG](https://github.com/Kherdu/TFG)**

- Corregido el problema al acceder a los ficheros en Linux.
- Corregido el problema con el tamaño inicial de la ventana en Linux.
- Arreglada visualización de contenidos HMTL en Windows.
- Sincronizada la barra de progreso con la posición actual dentro de la lección.
- Arreglado el error *IndexOutOfBounds* al terminar una lección, ahora muestra una pantalla de enhorabuena.
- Incorporada memoria para recordar las respuestas del alumno en cada fragmento de la lección, para que pueda navegar 
sin perder sus respuestas.
- Configurado un sistema de integración continua: Travis.
- Soporte para doble clic al seleccionar elementos de la listas.
- Uso del nombre del tema en lugar del nombre de fichero al elegir temas.
- Añadido un icono de aplicación provisional.
- Reorganizado el inicio de la aplicación y las ventanas de configuración del sistema.
- Mejorado el campo de texto en el que introducir código.

