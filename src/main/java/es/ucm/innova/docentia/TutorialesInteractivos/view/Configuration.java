package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Language;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Vista para modificar la configuración de la aplicacion
 * 
 * @author Carlos, Rafa
 *
 */
public class Configuration extends Pane {

	public Pane configuration(Controller c) {

		GridPane root = new GridPane(); // Panel principal

		Label dependencies = new Label("Directorio de cursos: ");// label
																		// de
																		// dependencias
		TextField pathDep = new TextField();// Campo donde se muestra la ruta
											// del archivo dependences
        pathDep.setEditable(false);
		Button search = new Button("Buscar...");// Boton para buscar el archivo de
												// dependencias
		// Lista con los lenguajes y la ruta de su compilador
		TableView<Language> languageList = new TableView<Language>();
		ObservableList<Language> data = FXCollections.observableArrayList();
		List<Language> ls = null;
		if (Controller.externalResourcesPath != null) {
			pathDep.setText(Controller.externalResourcesPath);
			ls = c.getLanguagesList();
			data.setAll(ls);
		}
		dependencies.setMnemonicParsing(true);
		dependencies.setLabelFor(pathDep);

		Label languagesLabel = new Label("CONFIGURACIÓN");

		languageList.setEditable(true);
		languageList.setVisible(true);
		TableColumn firstNameCol = new TableColumn("Lenguaje de programación");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Language, String>("language"));
		TableColumn secondNameCol = new TableColumn("Compilador/intérprete");
		secondNameCol.setCellValueFactory(new PropertyValueFactory<Language, String>("path"));

		languageList.setItems(data);
		languageList.getColumns().addAll(firstNameCol, secondNameCol);
		languageList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		Button change = new Button("Cambiar compilador/intérprete");
		Button back = new Button("Cancelar");
		if (c.externalResourcesPath == null ) {
            back.setDisable(true);
        }
		Button accept = new Button("Aceptar");
		
		// Label warning = new Label("Para avanzar todos los lenguajes han de estar configurados");
        Label warning = new Label("");
        if (c.externalResourcesPath == null ){
            warning.setText("Debes configurar el directorio de cursos");
        }

		root.add(dependencies, 0, 0);
		root.add(pathDep, 1, 0 );
		root.add(search, 3, 0);
		//root.add(acceptPath, 3, 0);
		root.add(languagesLabel, 0, 1);
		root.add(languageList, 0, 2);
		root.add(back, 0, 3);
		root.add(change, 1, 3);
		root.add(accept, 2, 3);
		root.add(warning, 0, 4);

		languagesLabel.setAlignment(Pos.TOP_CENTER);
		warning.setAlignment(Pos.TOP_CENTER);
		
		GridPane.setConstraints(dependencies, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		GridPane.setConstraints(pathDep, 1, 0, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		GridPane.setConstraints(search, 3, 0, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		//GridPane.setConstraints(acceptPath, 3, 0, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
		//		new Insets(5));
		GridPane.setConstraints(languagesLabel, 0, 1, 4, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets(5));
		GridPane.setConstraints(languageList, 0, 2, 4, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets(5));
		GridPane.setConstraints(back, 0, 3, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets(5));
		GridPane.setConstraints(change, 1, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets(5));
		GridPane.setConstraints(accept, 3, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets(5));
		GridPane.setConstraints(warning, 0, 4, 4, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets(5));

		// Listeners
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				c.start();
			}
		});
		

		accept.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			    if (c.externalResourcesPath != null ){
				    c.savePrefs(pathDep.getText(), data);
				    c.start();
			    } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("¡Atención!");
                    alert.setHeaderText("Para poder continuar debes configurar al menos\nel directorio donde están almacenados los cursos.");
                    alert.setContentText("Por favor, elige el directorio de cursos utilizando el botón\n\"Buscar...\" de la parte superior de la ventana.");
                    alert.showAndWait();
                }
			}
		});

		change.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// este es para el lenguaje
				Language l = languageList.getSelectionModel().getSelectedItem();
				if (l != null) {

					c.showSelection(l);
					data.set(data.indexOf(l), c.getLanguageAttributes());
				} else {
					warning.setText("Selecciona un lenguaje antes");
				}
				
			}
		});

		search.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// este es para el directorio
				c.showSelection(null);
				if ( c.externalResourcesPath != null ) {
					pathDep.setText(c.externalResourcesPath);
					Controller.log.info(c.externalResourcesPath);
					File f = new File(pathDep.getText());
					if (f.exists() && f.isDirectory()) {
						List<String> lanL = InternalUtilities.getDirectoryList(pathDep.getText());
						// añadimos los lenguajes a la lista

						for (String s : lanL) {
							Language addedL = new Language(s, null);
							data.add(addedL);
						}

						data.setAll(c.getLanguagesList());
					}
				} else {
					warning.setText("Primero selecciona directorio de recursos");
				}
			}
		});

        //change.getStyleClass().add("start");
		languagesLabel.getStyleClass().add("tittle");
		accept.getStyleClass().add("conf_aceptar");
		back.getStyleClass().add("conf_cancelar");
		languageList.setId("table");
		warning.getStyleClass().add("error");
		root.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

		return root;
	}

}
