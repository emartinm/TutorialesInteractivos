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
	public String getClue() {
			return null;
	}

	public String toString(){
		return String.format("Explanation(%s)", this.text);
    }

	
}
