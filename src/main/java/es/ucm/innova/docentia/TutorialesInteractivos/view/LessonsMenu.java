/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.view;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Lesson;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Subject;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.Node;

import java.util.List;

/**
 * Muestra la lista de ls lecciones del tema seleccionado
 * 
 * @author Carlos, Rafa
 *
 */
public class LessonsMenu extends Pane{

	public Pane lessonMenu(Subject t, Controller c){
		GridPane box = new GridPane();
		
		//Elementos
		
		//Creacion de webview
		// Inserta # y ## para mostrar el titulo y la introducción como cabeceras HMTL
		String content = c.markToHtml("# " + t.getTitle() + "\n## " + t.getIntroduction());
		Node title = InternalUtilities.creaBrowser(content, c);
		//WebEngine engine = title.getEngine();
		//engine.loadContent(content);
		
		Label titleList = new Label(Controller.getLocalizedString("lessons.lessons"));//TItulo de la lista de lecciones
		//Lista de lecciones
		ListView<String> leccionList = new ListView<String>();
		ObservableList<String> obsLecciones = FXCollections.observableArrayList (t.getLessonNames());
		leccionList.setItems(obsLecciones);

		/* Dibuja un tick al lado del nombre del nombre de la lección en la lista si está completada */
		leccionList.setCellFactory(param -> new ListCell<String>() {
			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					setText("   " + name);
					Lesson le = getLesson(name, t);
                    ProgressIndicator pi = new ProgressIndicator(le.getProgressPercentage());
					setGraphic(pi);
				}
			}
		});
		
		
		Button start = new Button(Controller.getLocalizedString("lessons.start"));//Boton para comenzar la leccion
		Label error = new Label();//Label de error
		Button back = new Button(Controller.getLocalizedString("lessons.back"));
		
		
		//Colocacion de elementos en el panel
		box.add(title, 0, 0);
		box.add(titleList, 0, 1);
		box.add(leccionList, 0, 2);
		box.add(start, 2, 3);
		box.add(error, 1, 3);
		box.add(back, 0, 3);
				
		
		GridPane.setConstraints(title, 0, 0, 3,1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.NEVER, new Insets(5));
		GridPane.setConstraints(titleList, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(leccionList, 0, 2, 3, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(start, 2, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(error, 1, 3, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(back, 0, 3, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		
		//Listeners
		start.setOnAction(new EventHandler<ActionEvent>(){
			MultipleSelectionModel<String> s;
				@Override
				public void handle(ActionEvent event) {
					s = leccionList.getSelectionModel();
					if (!s.isEmpty()) //Se comprueba que haya una leccion seleccionada
						c.selectedLesson(s.getSelectedIndex());
					else 
					{
						error.setText(Controller.getLocalizedString("lessons.select"));
					}
					
				}
				
			});
		
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				c.showStart();
				
			}
		});

		leccionList.setOnMouseClicked( (event) -> {
            // TODO
            // Para detectar cuándo se ha hecho doble clic en una entrada vacía se usa el método toString() del evento
            // ya que en esos casos parece contener siempre 'null'. Realizar de manera más elegante.
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
                    !event.getTarget().toString().contains("null")) {
                start.fire();
                // Lanza el evento de presionar el botón de "Comenzar"
            }
            //Controller.log.info( event.getTarget().toString() );
		} );
		
		
		//Parte estetica
		start.getStyleClass().add("start");
		error.getStyleClass().add("error");
		back.getStyleClass().add("start");
		//titleList.getStyleClass().add("title");
		box.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
		return box;
		
	}

    private Lesson getLesson(String name, Subject t) {
        List<Lesson> l = t.getLessons();
        for (Lesson le : l ) {
            if ( le.getTitle().equals(name) ) {
                return le;
            }
        }
        return null;
    }

}
