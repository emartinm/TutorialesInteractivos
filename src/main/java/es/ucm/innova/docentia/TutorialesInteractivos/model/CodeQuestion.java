package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.util.List;

/**
 * Question de tipo Code
 * 
 * @author Carlos, Rafa
 *
 */
public class CodeQuestion extends Question<String> {

	public String toString(){
		return String.format("CodeQuestion(%s,%s,%s)", this.text,
				this.solution, this.clue);
	}

	public CodeQuestion(String wording, String clue, String solution) {
		super(wording, clue, solution);
	}


	public Correction check(String answer, Language lang) {
        return lang.compileAndExecute(this.solution, answer);
    }

	/*@Override
	public void setSolution(String solution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMulti(Boolean is) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void setText(String explication) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setOptions(List<String> options) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setSolution(List<Integer> correctsAux) {
		// TODO Auto-generated method stub
		
	}*/


	protected void load_answer_from_string(String s) {
		this.lastAnswer = s;
	}

	protected String answer_to_string() {
		return this.lastAnswer;
	}


	

}
