package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Question de tipo Options
 * 
 * @author Enrique, Carlos, Rafa
 *
 */
public class OptionQuestion extends Question<List<Integer>> {
	private List<String> options; //Lista de las opciones a la pregunta
	private Boolean multi; //La pregunta es multipocion o no


	public String toString(){
		return String.format("OptionQuestion(%s,%s,%s,%s,%s)", this.text,
                this.solution.toString(), this.clue, this.options.toString(), this.multi.toString() );
	}

	public OptionQuestion(String wording, String clue, List<String> options, boolean multi, List<Integer> answer){
	    super(wording, clue);
	    this.solution = answer;
	    this.options = options;
	    this.multi = multi;
    }

	public List<String> getOptions() {
		return options;
	}

	public Correction check(Language lang) {
		boolean sol = true;
		int tam = answer.size();
		int i = 0;

		if (this.solution.size() == tam) {
		    // Se comprueba que haya el mismo numero de opciones marcadas como opciones correctas
			do {
				if (!this.solution.contains(answer.get(i)))
					sol = false;
				i++;
			} while (sol && i < tam);
		} else {
			sol = false;
		}

		return new Correction(ExecutionMessage.OK, "", null, sol);
	}

	public Boolean getMulti() {
		return multi;
	}

	protected void load_answer_from_object(Object o) {
		this.answer = (List<Integer>)o;
	}

}
