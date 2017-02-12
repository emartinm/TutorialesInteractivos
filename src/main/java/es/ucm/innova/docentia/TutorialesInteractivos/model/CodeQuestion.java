package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Question de tipo Code
 * 
 * @author Carlos, Rafa
 *
 */
public class CodeQuestion extends Question<List<String>> {
    private String corrector;

	public String toString(){
		return String.format("CodeQuestion(%s,%s,%s)", this.text,
				this.solution, this.clue);
	}

	public CodeQuestion(String wording, String clue, String corrector) {
	    super(wording, clue);
	    this.corrector = corrector;
	}


	public Correction check(List<String> answer, Language lang) {
        //return lang.compileAndExecute(this.corrector, answer);
        return null;
    }

	protected void load_answer_from_object(Object o) {
	    this.lastAnswer = (List<String>)o;
		//this.lastAnswer = new ArrayList<String>(s);
	}

	protected String answer_to_string() {
		//return this.lastAnswer;
        return null;
	}

	public int getNumberGaps() {
		return 1;
	}


	

}
