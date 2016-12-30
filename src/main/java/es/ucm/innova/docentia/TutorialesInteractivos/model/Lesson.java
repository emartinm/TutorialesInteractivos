package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;

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
	

	public Lesson(int number, String title, String intro_message){
		this.number=number;
		this.title =title;
		this.elements = new ArrayList<Element>();
		this.intro_message =intro_message;
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
	
}