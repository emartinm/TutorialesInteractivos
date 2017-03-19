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

	private Stage primaryStage;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
		Controller c = new Controller(primaryStage, getHostServices());
		c.start();
	}
}
