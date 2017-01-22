package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
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

    private static MessageDigest md = null;
    private static Base64.Encoder b64enc = Base64.getEncoder();



	public String toString(){
		return String.format("Lesson(%d,%s,%s,%s)", this.number, this.title, this.elements.toString(),
				this.intro_message );
	}


	/*
	La versión es el MD5 calculado a partir de su cadena toString
	 */
	public String version() {
	    String v = this.toString();
	    if (this.md != null) {
            byte[] thedigest = this.md.digest(v.getBytes());
            v = b64enc.encodeToString(thedigest);
        }
        return v;
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
	
}