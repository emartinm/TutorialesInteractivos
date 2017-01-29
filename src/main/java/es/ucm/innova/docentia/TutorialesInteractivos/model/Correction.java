package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.List;

/**
 * Crea el mensaje y las pistas en caso de que la pregunta sea fallida
 * 
 * @author Carlos, Rafa
 *
 */
public class Correction {
	private String message;
	private List<String> hints;
	private boolean correct;
	
	public Correction(){
		
	}
	
	public Correction(String m, List<String> h, boolean correct){
		this.message = m;
		this.hints = h;
		this.correct = correct;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getHints() {
		return hints;
	}

	public void setHints(List<String> hints) {
		this.hints = hints;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
