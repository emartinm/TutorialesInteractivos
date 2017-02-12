package es.ucm.innova.docentia.TutorialesInteractivos;
	


import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        List<String> ls = new ArrayList<>();
        ls.add("ojete");
        ls.add("moreno");
        String s = ls.toString();
        List<String> items = Arrays.asList(s.split("\\s*,\\s*"));
        System.out.println(s);
        System.out.println(items);

		this.primaryStage=primaryStage;
		this.primaryStage.setX(50);
		this.primaryStage.setY(50);
		this.primaryStage.setTitle("Tutoriales Interactivos");
		this.primaryStage.setMinWidth(600);
		this.primaryStage.setMinHeight(400);
		Controller c = new Controller(primaryStage);
		c.start();
	}
}
