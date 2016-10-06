package TFG.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Question de tipo Options
 * 
 * @author Carlos, Rafa
 *
 */
public class OptionQuestions extends Question<ArrayList<Integer>> {
	private List<String> options; //Lista de las opciones a la pregunta
	private Boolean multi; //La pregunta es multipocion o no
	
	
	public OptionQuestions(int number, String wording, String clue) {
		super(number, wording, clue, new ArrayList());
		this.options = new ArrayList<String>();
	}

	public ArrayList<Integer> getSolution() {
		return solution;
	}


	public List<String> getOptions() {
		return options;
	}

	@Override
	public void setOptions(ArrayList<String> options) {
		this.options=options;

	}

	public boolean check(ArrayList<Integer> answer, Subject subject) {
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
	public void setSolution(ArrayList<Integer> correctsAux) {
		this.solution = correctsAux;
		
	}

	@Override
	public void setText(String explication) {
		// TODO Auto-generated method stub
		
	}

	

		

}
