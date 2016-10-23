package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;

/**
 * Clase generica del elemento Question
 * 
 * @author Carlos, Rafa
 *
 * @param <T>
 */
public abstract class Question<T> extends Element
{
	protected int number; //number de la pregunta
	protected T solution; //solution a la pregunta
	protected String clue; //Pista para resolver la pregunta(opcional)
	
	public Question(){
		super(null);
		this.number=0;
		
	}
	
	public Question(int number, String wording, String clue, T solution) {
		super(wording);
		this.number = number;
		this.solution = solution;
		this.clue = clue;
	}

	public String getClue() {
		if (clue != null)
			return clue;
		else
			return "No hay pistas para esta pregunta";
	}

	public void setClue(String clue) {
		this.clue = clue;
	}

	public int getNumber() {
		return number;
	}

	public T getSolution() {
		return solution;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public abstract void setSolution(T solution);

	public abstract void setOptions(ArrayList<String> options);
	
	public abstract boolean check(T answer, Subject subject);

	public abstract void setMulti(Boolean is);
	
	
}
