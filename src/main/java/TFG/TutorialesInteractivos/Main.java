package TFG.TutorialesInteractivos;
	


import javafx.application.Application;
import javafx.stage.Stage;
import TFG.TutorialesInteractivos.controller.Controller;


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
		this.primaryStage.setX(50);
		this.primaryStage.setY(50);
		this.primaryStage.setTitle("Tutoriales Interactivos");
		Controller c = new Controller(primaryStage);
		c.start();
				
	}
}
