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
import es.ucm.innova.docentia.TutorialesInteractivos.model.SyntaxQuestion;
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
	 * @param load
	 *            Nombre del archivo del tema
	 * @return Subject seleccionado
	 */
	public static Subject cargaTema(String language, String load) {
		Yaml yaml = new Yaml();

		File file = new File(Controller.externalResourcesPath + "/" + language + "/" + load);
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
		String introSubject = (String) mapObjet.get("Introduccion");// introduccion
																// del tema
		String fileSubject = (String) mapObjet.get("Archivo");// Nombre del archivo
															// con las funciones
															// de solucion

		// objetos a rellenar para el tema
		List<Element> elements = new ArrayList<Element>();// lista de
																// elementos
		List<Lesson> lessons = new ArrayList<Lesson>();// lista de lecciones

		for (Map lesson : l) // Recorremos la lista de lecciones y parseamos
								// los elementos
		{
			elements = new ArrayList<Element>();// Lista de elementos de una
													// leccion
			e = (ArrayList<Map>) lesson.get("Elementos");// Vuelca los
															// elementos en una
															// Lista de Maps

			for (Map pre : e)// Recorre los elementos de la leccion a cargar y
								// los parsea
			{
				Element elem = new Explanation(null); // Crea un nuevo elem

				if (pre.get("Elem").equals("pregunta"))// El elemento es de tipo
														// pregunta
				{
					// Elementos generales a todo tipo de preguntas
					int num = (Integer) pre.get("Numero"); // Numero de la
															// pregunta
					String wording = (String) pre.get("Enunciado");// Enunciado
					String clue = (String) pre.get("Pista");// Pista

					if (pre.get("Tipo").equals("Codigo")) // Question de tipo
															// codigo
					{
						String answer = (String) pre.get("Resultado");//
						elem = new CodeQuestion(num, wording, clue, answer);

					} else if (pre.get("Tipo").equals("Sintaxis")) {
						String sintax = (String) pre.get("Gramatica");
						String result = (String) pre.get("Resultado");
						elem = new SyntaxQuestion(num, wording, clue, sintax, result);
					} else if (pre.get("Tipo").equals("Opciones")) {
						Boolean is = false;
						elem = new OptionQuestion(num, wording, clue);
						if ((Boolean) pre.get("Multiple")) {
							is = true;
						}
						String correctOpc = (String) pre.get("Opcion_correcta");// Cogemos
																				// el
																				// texto
																				// de
																				// las
																				// correctas
						String[] corrects = correctOpc.split(","); // Separamos
																	// la cadena
																	// en las
																	// respuestas
						ArrayList<Integer> correctsAux = StringToInt(corrects); // Camibia
																					// el
																					// tipo
																					// de
																					// respuesta
																					// a
																					// Integer
						elem.setSolution(correctsAux); // Añade las respuestas
														// correctas a la lista
														// de respuestas del
														// objeto
						ArrayList<String> opc = new ArrayList<String>(); // Array
																			// de
																			// las
																			// opciones
						opc = (ArrayList<String>) pre.get("Opciones"); // Carga
																		// las
																		// opciones
						elem.setOptions(opc);// Las mete en el objeto
						elem.setMulti(is);// Modifica el parametro de
											// multirrespuesta
					}
				} else {
					String explication = (String) pre.get("Contenido");// Carga
																		// el
																		// contenido
																		// de la
																		// explicación
					elem.setText(explication);// Modifica el texto del elemento
				}
				elements.add(elem);// Añade el elemento al array de elementos
									// de Lesson

			}
			int nLesson = (Integer) lesson.get("Leccion"); // numero de
																// leccion
			String tLesson = (String) lesson.get("Titulo_Leccion");// titulo
																		// de
																		// leccion
			String eLesson = (String) lesson.get("Intro_leccion");// Introduccion
																	// de la
																	// leccion
			Lesson lec = new Lesson(nLesson, tLesson, eLesson);// Crea la
																	// leccion
			lec.setElements(elements);// Modifica el array de elementos de una
										// leccion
			lessons.add(lec);// Añade la leccion al array de lecciones
		}
		// rellenado de objetos final
		Subject t = new Subject(numberSubject, tittleSubject, introSubject, fileSubject);// Crea el tema con todos
														// los elementos
														// cargados
		t.setLessons(lessons);// Modifica el Array de lecciones de Subject
		return t;
	}

	/*
	 * Devuelve el título de un tema almacenado en un fichero YAML
	 */
	public static String getTitle(String language, String filename) {
		Yaml yaml = new Yaml();
		File file = new File(Controller.externalResourcesPath + "/" + language + "/" + filename);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> mapObjet = (Map<String, Object>) yaml.load(input);
		String title = (String) mapObjet.get("Titulo");// Nombre del tema

		return title;
	}

	public static Integer getNumber(String language, String filename) {
		Yaml yaml = new Yaml();
		File file = new File(Controller.externalResourcesPath + "/" + language + "/" + filename);
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> mapObjet = (Map<String, Object>) yaml.load(input);
		return (Integer) mapObjet.get("Tema");// Número del tema
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
