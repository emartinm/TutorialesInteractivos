/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
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
		Subject t = null;
		try {
			input = new FileInputStream(file);
            Map<String, Object> mapObjet = (Map<String, Object>) yaml.load(input);
            // auxiliares
            ArrayList<Map> l; // lista de lecciones
            ArrayList<Map> e; // Lista de elementos
            l = (ArrayList<Map>) mapObjet.get("Lessons");

            // Elementos para rellenar el objeto Subject
            Integer numberSubject = (Integer) mapObjet.get("Subject");// numero del tema
            String subjectTitle = (String) mapObjet.get("Title");// Nombre del tema
            String introSubject = (String) mapObjet.get("Intro");// introduccion del tema
            List<Element> elements; // lista de elementos
            List<Lesson> lessons = new ArrayList<Lesson>();// lista de lecciones

            for (Map lesson : l) {// Recorremos la lista de lecciones y parseamos los elementos
                elements = new ArrayList<Element>();// Lista de elementos de una leccion
                e = (ArrayList<Map>) lesson.get("Elements");// Vuelca los elementos en una Lista de Maps

                for (Map pre : e) {// Recorre los elementos de la leccion a cargar y los parsea
                    Element elem = null;
                    String wording = (String) pre.get("Content");// Carga el contenido del texto, que siempre aparece
                    if (pre.get("Elem").equals("Code")) {
                        // Pregunta de tipo codigo
                        String clue = (String) pre.get("Hint");// Pista
                        String answer = (String) pre.get("File");
                        List<String> prompt = (List<String>) pre.get("Prompt");
                        Integer numGaps = (Integer) pre.getOrDefault("Gaps", 1);
                        elem = new CodeQuestion(wording, clue, answer, numGaps, prompt);
                    } else if (pre.get("Elem").equals("Options")) {
                        String clue = (String) pre.get("Hint");// Pista
                        Boolean isMulti = (Boolean) pre.getOrDefault("Multiple", Boolean.FALSE);
                        List<Integer> correctsAux = (List<Integer>) pre.getOrDefault("Solution", null);
                        ArrayList<String> opc = (ArrayList<String>) pre.get("Options");
                        elem = new OptionQuestion(wording, clue, opc, isMulti, correctsAux);
                    } else if (pre.get("Elem").equals("Text")) {
                        elem = new Explanation(wording);
                    } else if (pre.get("Elem").equals("Syntax")) {
                        Controller.log.info( "Syntax questions not supported");
                    } else {
                        Controller.log.warning( "Unsoported element in lesson " + filename + ": " + pre);
                    }
                    elements.add(elem);// Añade el elemento al array de elementos de Lesson
                }

                String tLesson = (String) lesson.get("Title");// titulo de leccion
                Lesson lec = new Lesson(tLesson, elements);// Crea la leccion
                lessons.add(lec);// Añade la leccion al array de lecciones
            }
            // rellenado de objetos final
            t = new Subject(numberSubject, subjectTitle, introSubject);// Crea el tema con todos los elementos
            t.setLessons(lessons);// Modifica el Array de lecciones de Subject
        } catch (FileNotFoundException e1) {
		    Controller.log.severe("Unable to open subject file " + file);
            //e1.printStackTrace();
            t = null;
        }

        // Si el tema no es correcto devuelve null
        //if( t != null && !t.isValid() ){
		//    t = null;
        //}

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
