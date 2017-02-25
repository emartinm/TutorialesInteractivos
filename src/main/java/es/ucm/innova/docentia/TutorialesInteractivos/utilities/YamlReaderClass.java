package es.ucm.innova.docentia.TutorialesInteractivos.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.CodeQuestion;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Element;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Explanation;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Lesson;
import es.ucm.innova.docentia.TutorialesInteractivos.model.OptionQuestion;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Subject;

/**
 * Clase que contiene las funciones de carga de un fichero yml
 * 
 * @author Carlos, Rafa
 *
 */
public final class YamlReaderClass {

	/**
	 * Carga el tema seleccionado (con el que se trabajara)
	 * 
	 * @param filename
	 *            Nombre del archivo del tema
	 * @return Subject seleccionado
	 */
	public static Subject cargaTema(String baseDir, String language, String filename) {
		Yaml yaml = new Yaml();

		File file = new File(baseDir + "/" + language + "/" + filename);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> mapObjet = (Map<String, Object>) yaml.load(input);
		// auxiliares
		ArrayList<Map> l = new ArrayList<Map>();// lista de lecciones
		ArrayList<Map> e = new ArrayList<Map>();// Lista de elementos
		l = (ArrayList<Map>) mapObjet.get("Lecciones");

		// Elementos para rellenar el objeto Subject
		Integer numberSubject = (Integer) mapObjet.get("Tema");// numero del tema
		String tittleSubject = (String) mapObjet.get("Titulo");// Nombre del tema
		String introSubject = (String) mapObjet.get("Introduccion");// introduccion del tema
		List<Element> elements = new ArrayList<Element>();// lista de elementos
		List<Lesson> lessons = new ArrayList<Lesson>();// lista de lecciones

		for (Map lesson : l) {// Recorremos la lista de lecciones y parseamos los elementos
			elements = new ArrayList<Element>();// Lista de elementos de una leccion
			e = (ArrayList<Map>) lesson.get("Elementos");// Vuelca los elementos en una Lista de Maps

			for (Map pre : e) {// Recorre los elementos de la leccion a cargar y los parsea
				Element elem = null;
				if (pre.get("Elem").equals("pregunta")) {
					String wording = (String) pre.get("Enunciado");// Enunciado
					String clue = (String) pre.get("Pista");// Pista

					if (pre.get("Tipo").equals("Codigo")) {
						// Question de tipo codigo
						String answer = (String) pre.get("Fichero");
						List<String> prompt = (List<String>) pre.get("Prompt");
						Integer numGaps = (Integer) pre.getOrDefault("Huecos", 1);
						elem = new CodeQuestion(wording, clue, answer, numGaps, prompt);

					} else if (pre.get("Tipo").equals("Sintaxis")) {
						Controller.log.info( "Preguntas de tipo Sintaxis no soportadas");
					} else if (pre.get("Tipo").equals("Opciones")) {
						Boolean isMulti = (Boolean) pre.getOrDefault("Multiple", Boolean.FALSE);
						// Cogemos el texto de las opciones correctas
						/*String correctOpc = (String) pre.get("Opcion_correcta");
						String[] corrects = correctOpc.split(",");
						ArrayList<Integer> correctsAux = StringToInt(corrects); // Cambia el tipo de respuesta a Integer
						*/
						List<Integer> correctsAux = (List<Integer>) pre.getOrDefault("Solucion", null);
						ArrayList<String> opc = (ArrayList<String>) pre.get("Opciones");
						elem = new OptionQuestion(wording, clue, opc, isMulti, correctsAux);
					}
				} else {
					String wording = (String) pre.get("Contenido");// Carga el contenido de la explicación
					elem = new Explanation(wording);
				}
				elements.add(elem);// Añade el elemento al array de elementos de Lesson

			}
			//int nLesson = (Integer) lesson.get("Leccion"); // numero de leccion
			String tLesson = (String) lesson.get("Titulo");// titulo de leccion
            Lesson lec = new Lesson(tLesson, elements);// Crea la leccion
			//lec.setElements(elements);// Modifica el array de elementos de una leccion
			lessons.add(lec);// Añade la leccion al array de lecciones
		}
		// rellenado de objetos final
		Subject t = new Subject(numberSubject, tittleSubject, introSubject);// Crea el tema con todos los elementos
		t.setLessons(lessons);// Modifica el Array de lecciones de Subject
		return t;
	}


	/**
	 * Funcion auxiliar que pasa los elementos de tipo String de un Array a tipo
	 * Int y los mete en un ArrayList
	 * 
	 * @param sol
	 * @return
	 */
	private static ArrayList<Integer> StringToInt(String[] sol) {
		ArrayList<Integer> ret = new ArrayList<Integer>();

		for (String s : sol) {
			ret.add(Integer.parseInt(s));
		}
		return ret;

	}
}
