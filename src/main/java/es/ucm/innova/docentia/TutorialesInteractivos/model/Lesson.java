/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.security.MessageDigest;
import java.util.*;
import java.lang.ClassCastException;

/**
 * Clase con los elementos de Lesson
 * 
 * @author Carlos, Rafa
 *
 */
public class Lesson {
	//private final int number;              //Numero de la leccion
	private final String title;            //Titulo de la leccion
	private List<Element> elements;        //Array con los elements de la leccion
	private String version = null;
	private int currentElement = 0;        // Posicion del elemento actualmente visualizado;
	private int latestEnabledElement = 0;  // Posición del elemento más avanzado habilitado
	private int latestElement = 0;          // Posición del elemento más avanzado que ha sido elemento actual

    private static MessageDigest md = null;
    private static Base64.Encoder b64enc = Base64.getEncoder();


	public String toString(){
		return String.format("Lesson(%s,%s)", this.title, this.elements.toString());
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

	public Lesson(String title, List<Element> elems){
		this.title =title;
		this.elements = elems; //new ArrayList<Element>();

        /* MessageEncoder md es un singleton */
        if (this.md == null) {
            try {
                this.md = MessageDigest.getInstance("MD5");
            } catch (java.security.NoSuchAlgorithmException e) {
                Controller.log.info("MD5 is not installed");
            }
        }
	}

	public String getTitle() {
		return this.title;
	}

	public List<Element> getElements() {
		return this.elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/* Devuelve si la leccion está terminada: si todos los fragmentos se pueden ver */
	public boolean isFinished() {
		return getProgressPercentage() == 100.0;
	}

	public double getProgressPercentage() {
	    double nelems = new Double(elements.size());
	    double progress = ((double) latestElement) / nelems;
	    return progress;
    }

	/* Carga el progreso de la lección desde 'progress', que contiene entradas
	 * para las lecciones usando la version como clave */
	public void loadProgress(Map<String, Object> progress)  {
        String version = this.version();
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
                            Controller.log.info( "There is no progress stored for question " + Integer.toString(i) );
                        }
                    }
                }

                currentElement = (Integer) lesson_prog.getOrDefault( "current", 0 );
                latestEnabledElement = (Integer) lesson_prog.getOrDefault( "enabled", 0 );
                latestElement = (Integer) lesson_prog.getOrDefault( "latest", 0 );
            } catch (ClassCastException e) {
                Controller.log.warning("Error when loading progress of lesson " + version + " -> " + e.getLocalizedMessage() );
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
	    p.put( "current", this.currentElement );
	    p.put( "enabled", this.latestEnabledElement);
        p.put( "latest", this.latestElement);
        return p;

    }

    public int getCurrentElementPos() {
        return currentElement;
    }

    public void setCurrentElementPos(int currentElement) {
        this.currentElement = currentElement;
        this.latestElement = Math.max(latestElement, currentElement );
    }

    public int getLatestEnabledElement() {
        return latestEnabledElement;
    }

    public void setLatestEnabledElement(int latestEnabledElement) {
        this.latestEnabledElement = latestEnabledElement;
    }

    public Element getCurrentElement() {
        if ( currentElement >= 0 && currentElement < this.elements.size() ){
	        return this.elements.get(currentElement);
	    } else {
            Controller.log.info( "Accesing element out of range: " + currentElement);
            return null;
        }
    }

    /* Comprueba si el tema tiene todos los campos obligatorios */
    public boolean isValid() {
        boolean ok = true;

        if (this.title == null) {
            ok = false;
            Controller.log.warning("Missing title in lesson");
        }

        if (this.elements == null || this.elements.size() == 0) {
            ok = false;
            Controller.log.warning("Missing elements in lesson (lessons must have at least one element)");
        } else {
            for (Element e : this.elements)
                ok &= e.isValid();
        }

        return ok;
    }
}