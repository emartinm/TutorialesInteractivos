package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;

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
	public void setOptions(ArrayList<String> opc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMulti(Boolean is) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSolution(ArrayList<Integer> correctsAux) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setText(String explication) {
		this.text = explication;
		
	}

	@Override
	public String getClue() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
