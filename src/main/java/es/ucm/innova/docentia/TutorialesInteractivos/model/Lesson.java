package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.security.MessageDigest;
import java.util.*;
import java.lang.ClassCastException;

import es.ucm.innova.docentia.TutorialesInteractivos.utilities.JSONReaderClass;

/**
 * Clase con los elementos de Lesson
 * 
 * @author Carlos, Rafa
 *
 */
public class Lesson 
{
	private int number; //Numero de la leccion
	private String title; //Titulo de la leccion
	private List<Element> elements; //Array con los elements de la leccion
	private String intro_message; //Introducción de la lección
	private boolean finished = false;
	private String version = null;

    private static MessageDigest md = null;
    private static Base64.Encoder b64enc = Base64.getEncoder();



	public String toString(){
		return String.format("Lesson(%d,%s,%s,%s)", this.number, this.title, this.elements.toString(),
				this.intro_message );
	}


	/*
	La versión es el MD5 en Base64 calculado a partir de su cadena toString
	Si no existe MD5, la versión es directacmente el toString
	 */
	public String version() {
	    // Solo se calcula una vez
	    if (this.version == null ){
            version = this.toString();
            if (this.md != null) {
                byte[] thedigest = this.md.digest(version.getBytes());
                version = b64enc.encodeToString(thedigest);
            }
        }
        return version;
	}
	

	public Lesson(int number, String title, String intro_message){
		this.number=number;
		this.title =title;
		this.elements = new ArrayList<Element>();
		this.intro_message =intro_message;

        /* MessageEncoder md es un singleton */
        if (this.md == null) {
            try {
                this.md = MessageDigest.getInstance("MD5");
            } catch (java.security.NoSuchAlgorithmException e) {
                Controller.log.info("MD5 no instalado");
            }
        }
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumero(int number) {
		this.number = number;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroMessage() {
		return intro_message;
	}

	public void setIntro_message(String intro_message) {
		this.intro_message = intro_message;
	}

	public List<Element> getElements() {
		return this.elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/* Devuelve si la leccion está terminada */
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean b){
		finished = b;
	}

	/* Carga el progreso de la lección desde 'progress', que contiene entradas
	 * para las lecciones usando la version como clave */
	public void loadProgress(Map<String, Object> progress)  {
        String version = this.version();
        //String path = Controller.externalResourcesPath + FileSystems.getDefault().getSeparator() + Controller.progressFileName;
        //Map<String, Object> progress = JSONReaderClass.loadProgress(path);
        if ((progress != null) && progress.containsKey(version)) {
            try {
                Map<String, Object> lesson_prog = (Map<String, Object>) progress.get(version);

                /* Carga el estado de las preguntas */
                for ( int i = 0; i < this.elements.size(); ++i ){
                    if ( this.elements.get(i) instanceof  Question ) {
                        Map<String, Object> question_progress = (Map<String, Object>) lesson_prog.getOrDefault(Integer.toString(i), null);
                        if (question_progress != null ) {
                            this.elements.get(i).loadProgress(question_progress);
                        } else {
                            Controller.log.info( "No existe progreso para la pregunta " + Integer.toString(i) );
                        }
                    }

                }

                finished = (Boolean) lesson_prog.getOrDefault("finished", false);
            } catch (ClassCastException e) {
                Controller.log.warning("Error al leer el progreso de la lección " + version + " -> " + e.getLocalizedMessage() );
            }

        }
    }

    public Map<String, Object> get_progress() {
	    Map<String, Object> p = new HashMap<String, Object>();
	    int i = 0;
	    for (Element e : elements ) {
	        if ( e instanceof Question ) {
	            p.put( Integer.toString(i), ((Question) e).get_progress() );
            }
            ++i;
        }
        p.put( "finished", this.finished );
        return p;

    }
	
}