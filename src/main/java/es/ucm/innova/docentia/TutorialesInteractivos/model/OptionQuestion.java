package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Question de tipo Options
 * 
 * @author Carlos, Rafa
 *
 */
public class OptionQuestion extends Question<List<Integer>> {
	private List<String> options; //Lista de las opciones a la pregunta
	private Boolean multi; //La pregunta es multipocion o no


	public String toString(){
		return String.format("OptionQuestion(%d,%s,%s,%s,%s,%s)", this.number, this.text,
                this.solution.toString(), this.clue, this.options.toString(), this.multi.toString() );
	}

	
	public OptionQuestion(int number, String wording, String clue) {
		super(number, wording, clue, new ArrayList());
		this.options = new ArrayList<String>();
	}

	public List<Integer> getSolution() {
		return solution;
	}


	public List<String> getOptions() {
		return options;
	}

	@Override
	public void setOptions(List<String> options) {
		this.options=options;
	}

	public boolean check(List<Integer> answer, Subject subject) {
		boolean sol = true;
		int tam = answer.size();
		int i = 0;

		if (this.solution.size() == tam)// Se comprueba que haya el mismo numero
										// de opciones marcadas como opciones
										// correctas
		{
			do {
				if (!this.solution.contains(answer.get(i)))
					sol = false;
				i++;
			} while (sol && i < tam);
		} else
			sol = false;

		return sol;
	}

	@Override
	public void setMulti(Boolean is) {
		this.multi=is;
		
	}

	public Boolean getMulti() {
		return multi;
	}

	@Override
	public void setSolution(List<Integer> correctsAux) {
		this.solution = correctsAux;
		
	}

	@Override
	public void setText(String explication) {
		// TODO Auto-generated method stub
		
	}

	protected void load_answer_from_string(String s) {
		if (s != null && s.length() > 0 ){
			String[] ints = s.split(",");
			this.lastAnswer = new ArrayList<Integer>();
			for (String n : ints ) {
				this.lastAnswer.add( new Integer(n) );
			}
		}
	}

    protected String answer_to_string() {
        String ret = "";
	    if (this.lastAnswer != null ) {
	        for( int i = 0; i < lastAnswer.size(); ++i ) {
	            ret = ret + lastAnswer.get(i).toString();
	            if ( i < lastAnswer.size() - 1 ) {
	                ret = ret + ",";
                }
            }
        }
        return ret;
    }

}
