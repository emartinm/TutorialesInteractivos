package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con el elemento Explicación
 * @author Carlos, Rafa
 *
 */
public class Explanation extends Element{
	
	public Explanation(String text)
	{
		super(text);
	}

	@Override
	public void setOptions(List<String> opc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMulti(Boolean is) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSolution(List<Integer> correctsAux) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setText(String explanation) {
		this.text = explanation;
		
	}

	@Override
	public String getClue() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString(){
		return String.format("Explanation(%s)", this.text);
    }

	
}
