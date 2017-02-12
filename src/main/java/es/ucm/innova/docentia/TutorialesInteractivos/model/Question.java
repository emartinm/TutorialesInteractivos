package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase generica del elemento Question
 * 
 * @author Carlos, Rafa
 *
 * @param <T>
 */
public abstract class Question<T> extends Element
{
	//protected int number; //number de la pregunta
	protected T solution; //solution a la pregunta
	protected T answer;
	protected final String clue; //Pista para resolver la pregunta(opcional)


	// Para recordar la respuesta del usuario
	//protected T lastAnswer;               //La última respuesta del usuario
	protected boolean lastAnswer_checked; //La respuesta 'lastAnswer' fue comprobada
	protected boolean lastAnswer_correct; //La respuesta 'lastAnswer' fue comprobada y era correcta

	public Question(){
		super(null);
		this.lastAnswer_checked = false;
		this.answer = null;
		this.lastAnswer_correct = false;
		this.clue = null;
		
	}
	
	public Question(String wording, String clue) {
		super(wording);
		this.solution = solution;
		this.clue = clue;
	}

	public T getLastAnswer() {
		return answer;
	}

	public void setLastAnswer(T lastAnswer) {
		this.answer = lastAnswer;
	}

	public boolean isLastAnswer_checked() {
		return lastAnswer_checked;
	}

	public void setLastAnswer_checked(boolean lastAnswer_checked) {
		this.lastAnswer_checked = lastAnswer_checked;
	}

	public void setLastAnswer_correct(boolean lastAnswer_correct) {
		this.lastAnswer_correct = lastAnswer_correct;
	}

	public String getHint() {
		return clue;
	}

	public abstract Correction check(Language lang);

	private void loadProgress_lastAnswer_checked(Map<String, Object> progress) {
		this.lastAnswer_checked = (Boolean) progress.getOrDefault("last_checked", Boolean.FALSE);
	}

	private void loadProgress_lastAnswer_correct(Map<String, Object> progress) {
		this.lastAnswer_correct = (Boolean) progress.getOrDefault("last_correct", Boolean.FALSE);
	}

	protected abstract void load_answer_from_object(Object o);

	private void loadProgress_lastAnswer(Map<String, Object> progress) {
	    Object o = progress.getOrDefault("last_answer", null);
	    load_answer_from_object(o);
	}

	public void loadProgress(Map<String, Object> progress) {
        loadProgress_lastAnswer_checked(progress);
        loadProgress_lastAnswer_correct(progress);
        loadProgress_lastAnswer(progress);
    }

    public Map<String, Object> get_progress() {
        Map<String, Object> p = new HashMap<String, Object>();
        p.put( "last_checked", this.lastAnswer_checked );
        p.put( "last_correct", this.lastAnswer_correct );
        p.put( "last_answer", this.answer);
        return p;
    }
	
}
