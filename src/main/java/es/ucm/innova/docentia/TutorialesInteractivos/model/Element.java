package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;

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

	
	public abstract void setOptions(ArrayList<String> opc);

	public abstract void setMulti(Boolean is);

	public abstract void setSolution(ArrayList<Integer> correctsAux);

	public abstract void setText(String explication);

	public abstract String getClue();

	
}
