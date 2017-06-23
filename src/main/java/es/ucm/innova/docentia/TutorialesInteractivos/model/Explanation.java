/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

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
	public String getHint() {
			return null;
	}

	public String toString(){
		return String.format("Explanation(%s)", this.text);
    }

    public  boolean isValid() {
	    boolean ok = true;
        if (this.text == null ) {
            ok = false;
            Controller.log.severe("Missing 'Content' field in 'Text' element");
        }
        return ok;
    }

	
}
