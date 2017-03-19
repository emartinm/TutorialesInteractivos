/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.view;



import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author Carlos, Rafa
 *
 */
public class EndLessonPane {
	
	public EndLessonPane(Controller c)
	{
		Stage dialog = new Stage();
		
		Scene scene = new Scene(new Group());
		Label message = new Label("ENHORABUENA!! Has completado el tutorial");
		Button b = new Button("Volver al menú de temas");
		VBox pane = new VBox(10);
		pane.getChildren().addAll(message);
		pane.getChildren().addAll(b);
		pane.setPadding(new Insets(10));
		
		message.setPadding(new Insets(10));
		b.setPadding(new Insets(10));
		
		pane.setPrefSize(300, 100);
		
		scene.setRoot(pane);
		dialog.setScene(scene);
		dialog.show();
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				c.showStart();
				dialog.close();
			}
		});
	}

}
