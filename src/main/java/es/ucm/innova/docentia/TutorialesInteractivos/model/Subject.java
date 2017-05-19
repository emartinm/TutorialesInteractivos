/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Subject con el que se trabaja
 * 
 * @author Carlos, Rafa
 *
 */
public class Subject {
	private int number = -1; //Numero del tema
	private String title; //Titulo del tema
	private String introduction; //Introduccion del Subject
	private List <Lesson> lessons; //Lista de las lecciones que componen el tema


	public Subject(int number, String title, String introduction) {
		this.number = number;
		this.title = title;
		//this.correctorFile = file;
		this.introduction = introduction;
		this.lessons = new ArrayList<Lesson>();
	}

	public int getNumber() {
		return number;
	}


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Devuelve un array con solo el nombre de las lecciones
	 * @return ArrayList
	 */
	public ArrayList<String> getLessonNames() {
		//devuelve arraylist de nombres de lecciones
		ArrayList<String> names= new ArrayList<String>();
		for (Lesson l: this.lessons){
			names.add(l.getTitle());
		}
		return names;
	}

	public ArrayList<Element> getLeccion(String selectedItem) {
		
		int i = 0;
		while (!this.lessons.get(i).getTitle().equals(selectedItem))
			i++;
		return (ArrayList<Element>) this.lessons.get(i).getElements();
	}

	/* Comprueba si el tema completo esta terminado, mirando si todas sus lecciones están completadas */
	public boolean isFinished() {
		boolean ret = true;
		for( Lesson l : this.lessons) {
			ret = ret && l.isFinished();
		}
		return ret;
	}

	/* Carga el progreso de todas sus lecciones desde 'progress', que contiene entradas
	 * para las lecciones usando la version como clave */
	public void loadProgress(Map<String, Object> progress)  {
		for (Lesson le : lessons ) {
			le.loadProgress(progress);
		}
	}

	public double getProgressPercentage() {
		double acc = 0.0;
		double num = 0.0;
		for (Lesson l : this.getLessons()) {
			num += 1.0;
			acc += l.getProgressPercentage();
		}
		return acc/num;
	}

	/* Comprueba si el tema tiene todos los campos obligatorios */
	public boolean isValid(){
	    boolean ok = true;

	    if (this.number == -1) {
	        ok = false;
	        Controller.log.severe("Missing subject number");
	    }

	    if (this.title == null) {
            ok = false;
            Controller.log.severe("Missing subject title");
	    }

        if (this.introduction == null) {
            ok = false;
            Controller.log.severe("Missing subject introduction message");
	    }

        if( this.lessons == null || this.lessons.size() == 0) {
            ok = false;
            Controller.log.severe("Missing lessons in subject (subjects must have at least one lesson)");
        } else {
		    for (Lesson l : this.lessons)
		        ok &= l.isValid();
        }

        return ok;
	}

}
