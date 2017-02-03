package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.List;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
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


/**
 * Question de tipo Sintax
 * 
 * @author Carlos, Rafa
 *
 */
public class InitialWindow extends Pane {

	/*
	 * Vista con la portada, elige un lenguaje, y llama en controlador a showPortada
	 * 
	 */
	public Pane initialWin(List<String> files,Controller c){

		
		GridPane pane = new GridPane(); //Panel principal
		
		Label labelTittle = new Label("Selecciona un lenguaje"); //Label del titulo de la ventana

		ListView<String> languageList = new ListView<String>(); //Lista de los temas
		ObservableList<String> obsTemas = FXCollections.observableArrayList(files);//permite ver la seleccion
		languageList.setItems(obsTemas);

		Button start = new Button("Comenzar");//Boton para comenzar el tutorial con el lenguaje seleccionado
		//Label advise = new Label("Recuerda enlazar los compiladores");//Mensaje de aviso
		Label advise = new Label("");//Mensaje de aviso
		Button settings = new Button("Ajustes");//Boton de ajustes para seleccionar el compilador
		Label error = new Label(); //Label que se mostrara con el mensaje de error cuando no haya lenguaje seleccionado
		
		labelTittle.setAlignment(Pos.TOP_CENTER);

		pane.add(labelTittle, 0, 0);
		pane.add(languageList, 0, 1);
		pane.add(start, 2, 2);
		pane.add(advise, 1, 2);
		pane.add(settings, 0, 2);
		pane.add(error,1,3);
		
		
		
		GridPane.setConstraints(labelTittle, 0, 0, 3,1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.NEVER, new Insets(5));
		GridPane.setConstraints(languageList, 0, 1, 3, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(start, 2, 2, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(advise, 1, 2, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(settings, 0, 2, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(error, 1, 3, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
	
		languageList.setOnMouseClicked( (event) -> {
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
		
		start.setOnAction( (event) -> {
			MultipleSelectionModel<String> s;
			s = languageList.getSelectionModel();
			if (!s.isEmpty()){
				c.selectedLanguage(s.getSelectedItem());//Se carga el tema seleccionado
			} else {
				error.setText("Se debe seleccionar un lenguaje");
			}
		});
		
		
		settings.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				c.showSettings();
				
			}
		});
		
		//Estetica
		labelTittle.getStyleClass().add("tittle");
		start.getStyleClass().add("start");
		advise.getStyleClass().add("advise");
		settings.getStyleClass().add("setting");
		error.getStyleClass().add("error");
		pane.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
		
	
		return pane;

	}

}
