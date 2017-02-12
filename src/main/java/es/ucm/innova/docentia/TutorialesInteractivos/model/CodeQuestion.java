package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Question de tipo Code
 * 
 * @author Enrique, Carlos, Rafa
 *
 */
public class CodeQuestion extends Question<List<String>> {
    private String corrector;
    private int numGaps = 1;

	public String toString(){
		return String.format("CodeQuestion(%s,%s,%s)", this.text,
				this.solution, this.corrector, this.clue);
	}

	public CodeQuestion(String wording, String clue, String corrector, int numGaps) {
	    super(wording, clue);
	    this.corrector = corrector;
	    this.numGaps = numGaps;
	}


	public Correction check(Language lang) {
        return lang.compileAndExecute(this.corrector, this.answer);
    }

	protected void load_answer_from_object(Object o) {
	    this.answer = (List<String>)o;
	}

	public int getNumberGaps() {
		return numGaps;
	}

}
