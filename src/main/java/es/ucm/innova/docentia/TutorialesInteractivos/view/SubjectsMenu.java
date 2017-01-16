package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.ArrayList;
import java.util.List;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.YamlReaderClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import java.util.Collections;

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
		// Ordenamos la lista de nombres de ficheros en base a su numero de tema en el YAML
		Collections.sort( files,
                (f1, f2) -> YamlReaderClass.getNumber(c.selectedLanguage, f1).
                                compareTo( YamlReaderClass.getNumber(c.selectedLanguage, f2) )
        );
		ListView<String> subjectsList = new ListView<String>(); //Lista de los temas
		List<String> titulos = getTitles(c.selectedLanguage, files);
		//ObservableList<String> obsSubjects = FXCollections.observableArrayList(files);//permite ver la seleccion
		ObservableList<String> obsSubjects = FXCollections.observableArrayList(titulos);//permite ver la seleccion
		subjectsList.setItems(obsSubjects);
		
		Button start = new Button("Comenzar");
		Label error = new Label ();
		Button back = new Button("Volver menú anterior");
		
		language.setAlignment(Pos.TOP_CENTER);
		
		
		box.add(language, 0, 0);
		box.add(subjectsList, 0, 1);
		box.add(start, 2, 2);
		box.add(error, 1, 2);
		box.add(back, 0, 2);
		
		GridPane.setConstraints(language, 0, 0, 3,1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.NEVER, new Insets(5));
		GridPane.setConstraints(subjectsList, 0, 1, 3, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(start, 2, 2, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(error, 1, 2, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(back, 0, 2, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));

		subjectsList.setOnMouseClicked( (event) -> {
		    // TODO
            // Para detectar cuándo se ha hecho doble clic en una entrada vacía se usa el método toString() del evento
            // ya que en esos casos parece contener siempre 'null'. Realizar de manera más elegante.
			if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
					!event.getTarget().toString().contains("null")) {
				start.fire();
				// Lanza el evento de presionar el botón de "Comenzar"
			}
			Controller.log.info( event.getTarget().toString() );
		});
		
		start.setOnAction(new EventHandler<ActionEvent>(){	
			public void handle(ActionEvent event) {
				MultipleSelectionModel<String> s;
				s= subjectsList.getSelectionModel();
				if (!s.isEmpty()) {//Se comprueba que hay alguna opcion seleccionada
					int pos = s.getSelectedIndex();
					String filename = files.get(pos);
					c.selectedSubject( filename ); //Se carga el tema seleccionado
				} else {
					error.setText("Se debe seleccionar un tema");
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
		language.getStyleClass().add("tittle");
		start.getStyleClass().add("start");
		error.getStyleClass().add("error");
		back.getStyleClass().add("start");
		box.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
		
		return box;
	}

	// Dada una lista de nombre de ficheros YAML, devuelve una lista con sus títulos
	private List<String> getTitles( String language, List<String> files ) {
		List<String> r = new ArrayList<String>();
		for (String filename : files ) {
			r.add(YamlReaderClass.getTitle(language, filename) );
		}
		return r;
	}

}