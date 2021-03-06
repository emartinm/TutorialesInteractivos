/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */

package es.ucm.innova.docentia.TutorialesInteractivos;


import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;


/**
 * Clase principal de la aplicación
 * @author Carlos, Rafa
 *
 */
public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        boolean reset = this.getParameters().getRaw().contains("--reset");
        boolean debug = this.getParameters().getRaw().contains("--debug");
		Controller c = new Controller(primaryStage, getHostServices(), reset, debug);
		c.start();
	}

    @Override
    public void stop(){
	    Controller.log.info("Application is closed\n\n\n\n");
    }
}
