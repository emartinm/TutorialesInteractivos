package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;

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


	// Para recordar la respuesta del usuario
	protected T lastAnswer; //La última respuesta del usuario
	protected boolean lastAnswer_checked; //La respuesta 'lastAnswer' fue comprobada
	protected boolean lastAnswer_correct; //La respuesta 'lastAnswer' fue comprobada y era correcta
	
	public Question(){
		super(null);
		this.number=0;
		this.lastAnswer_checked = false;
		this.lastAnswer = null;
		this.lastAnswer_correct = false;
		
	}
	
	public Question(int number, String wording, String clue, T solution) {
		super(wording);
		this.number = number;
		this.solution = solution;
		this.clue = clue;
	}

	public T getLastAnswer() {
		return lastAnswer;
	}

	public void setLastAnswer(T lastAnswer) {
		this.lastAnswer = lastAnswer;
	}

	public boolean isLastAnswer_checked() {
		return lastAnswer_checked;
	}

	public void setLastAnswer_checked(boolean lastAnswer_checked) {
		this.lastAnswer_checked = lastAnswer_checked;
	}

	public boolean isLastAnswer_correct() {
		return lastAnswer_correct;
	}

	public void setLastAnswer_correct(boolean lastAnswer_correct) {
		this.lastAnswer_correct = lastAnswer_correct;
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

	public abstract void setOptions(List<String> options);
	
	public abstract boolean check(T answer, Subject subject);

	public abstract void setMulti(Boolean is);
	
	
}