package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase abstracta que representa los componentes de una lección
 * 
 * @author Carlos, Rafa
 *
 */
public abstract class Element {
	
	protected String text; //Enunciado en preguntas o explicacion en explicacion
	
	public Element(String text)
	{
		this.text = text;
	}
	
	public String getText(){
		return this.text;
	}

	public abstract String getHint();

	public void loadProgress(Map<String, Object> progress) {}
}
