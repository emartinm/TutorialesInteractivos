/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Subject;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.YamlReaderClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Panel que muestra el menu con los temas que se compone el tutorial
 * 
 * @author Carlos, Rafa
 *
 */
public class SubjectsMenu extends Pane{

	public Pane subjectsMenu(List<String> files, String lenSelect, Controller c){
		GridPane box = new GridPane();
		
		Label language = new Label(lenSelect);
		Map<String,Subject> subjects = get_subjects(c.getConfig().getDirTemas(), c.getCurrentLanguage(), files, c.getProgress() );
		// Ordenamos la lista de nombres de ficheros en base a su numero de tema
		Collections.sort( files,
				(f1, f2) -> Integer.compare(subjects.get(f1).getNumber(), subjects.get(f2).getNumber())
		);
		List<String> titulos = this.getTitles(subjects, files);
		ListView<String> subjectsList = new ListView<String>(); //Lista de los temas

		//ObservableList<String> obsSubjects = FXCollections.observableArrayList(files);//permite ver la seleccion
		ObservableList<String> obsSubjects = FXCollections.observableArrayList(titulos);//permite ver la seleccion
		subjectsList.setItems(obsSubjects);

		/* Dibuja un tick al lado del nombre del tema si está completado */
		subjectsList.setCellFactory(param -> new ListCell<String>() {

		    @Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					setText("   " + name);
					int pos = titulos.indexOf( name );
					String filename = files.get(pos);
					Subject s = subjects.get(filename);
					ProgressIndicator pi = new ProgressIndicator(s.getProgressPercentage());
					setGraphic(pi);
				}
			}
		});
		
		Button start = new Button(Controller.getLocalizedString("subjectmenu.start"));
		Label error = new Label ();
		Button back = new Button(Controller.getLocalizedString("subjectmenu.back"));
		
		language.setAlignment(Pos.TOP_CENTER);
		
		
		box.add(language, 0, 0);
		box.add(subjectsList, 0, 1);
		box.add(start, 2, 2);
		box.add(error, 1, 2);
		box.add(back, 0, 2);
		
		GridPane.setConstraints(language, 0, 0, 3,1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.NEVER, new Insets(20,5,5,5));
		GridPane.setConstraints(subjectsList, 0, 1, 3, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(start, 2, 2, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER, new Insets(5));
		GridPane.setConstraints(error, 1, 2, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER, new Insets(5));
		GridPane.setConstraints(back, 0, 2, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER, new Insets(5));

		subjectsList.setOnMouseClicked( (event) -> {
		    // TODO
            // Para detectar cuándo se ha hecho doble clic en una entrada vacía se usa el método toString() del evento
            // ya que en esos casos parece contener siempre 'null'. Realizar de manera más elegante.
			if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
					!event.getTarget().toString().contains("null")) {
				start.fire();
				// Lanza el evento de presionar el botón de "Comenzar"
			}
			//Controller.log.info( event.getTarget().toString() );
		});
		
		start.setOnAction(new EventHandler<ActionEvent>(){	
			public void handle(ActionEvent event) {
				MultipleSelectionModel<String> s;
				s = subjectsList.getSelectionModel();
				if (!s.isEmpty()) {//Se comprueba que hay alguna opcion seleccionada
					int pos = s.getSelectedIndex();
					String filename = files.get(pos);
					c.selectedSubject( filename ); //Se carga el tema seleccionado
				} else {
					error.setText(Controller.getLocalizedString("subjectmenu.selectSubject"));
				}
			}
			
		});
		
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				c.start();
				
			}
		});
		
		//Parte estetica
		language.getStyleClass().add("title");
		start.getStyleClass().add("start");
		error.getStyleClass().add("error");
		back.getStyleClass().add("start");
		box.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
		
		return box;
	}

	// Dada una lista de nombre de ficheros YAML, devuelve una lista con sus títulos
	private List<String> getTitles( Map<String,Subject> subjects, List<String> files ) {
		List<String> r = new ArrayList<String>();
		for (String filename : files ) {
			r.add( subjects.get(filename).getTitle() );
		}
		return r;
	}

	/* Devuelve una lista de parejas (filename, subject) cargadas desde los ficheros YAML.
	 * También se carga el estado de las lecciones desde el fichero JSON para saber si están
	 * o no finalizadas */
	private Map<String, Subject> get_subjects(String baseDir, String language, List<String> files, Map<String, Object> progress) {
		Map<String, Subject> r = new HashMap<String, Subject>();
		for (String filename : files ) {
			Subject s = YamlReaderClass.cargaTema(baseDir, language, filename);
			if (s != null) {
				s.loadProgress(progress);
				r.put(filename, s);
			}
		}
		return r;
	}
}