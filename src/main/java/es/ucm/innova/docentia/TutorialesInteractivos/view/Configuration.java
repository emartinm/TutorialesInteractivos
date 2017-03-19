/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
		//root.setGridLinesVisible(true); // Para depurar

		Label dependencies = new Label(Controller.getLocalizedString("config.dir"));// label de dependencias
		TextField pathDep = new TextField();// Campo donde se muestra la ruta del archivo dependences
        pathDep.setEditable(false);
		Button search = new Button(Controller.getLocalizedString("config.find"));// Boton para buscar el archivo de dependencias
		// Lista con las entradas de configuración y su ruta
		TableView<ConfigEntry> languageList = new TableView<>();
		languageList.setPlaceholder(new Label("")); // Evita mostrar el mensaje "No content in table" si está vacío
		ObservableList<ConfigEntry> data = FXCollections.observableArrayList();
		if (c.getConfig().getDirTemas() != null) {
			pathDep.setText(c.getConfig().getDirTemas());
			List<String> ls = c.getLanguagesList();
			// Carga el contenido inicial desde la configuración
			data.removeAll();
            List<String> lentrynames = configEntries(ls);
			List<ConfigEntry> lentries = new ArrayList<>();
			for (String entryname : lentrynames) {
				lentries.add( new ConfigEntry(entryname, c.getConfig().get(entryname) ) );
			}
			data.setAll(lentries);
		}
		dependencies.setMnemonicParsing(true);
		dependencies.setLabelFor(pathDep);

		Label languagesLabel = new Label(Controller.getLocalizedString("config.config"));

		languageList.setEditable(true);
		languageList.setVisible(true);
		TableColumn firstNameCol = new TableColumn(Controller.getLocalizedString("config.lang"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<ConfigEntry, String>("key"));
		TableColumn secondNameCol = new TableColumn(Controller.getLocalizedString("config.comp"));
		secondNameCol.setCellValueFactory(new PropertyValueFactory<ConfigEntry, String>("value"));

		languageList.setItems(data);
		languageList.getColumns().addAll(firstNameCol, secondNameCol);
		languageList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		Button change = new Button(Controller.getLocalizedString("config.change_compiler"));
		Button back = new Button(Controller.getLocalizedString("config.cancel"));
		if (!c.getConfig().isDirTemas()) {
            back.setDisable(true);
        }
		Button accept = new Button(Controller.getLocalizedString("config.accept"));
		
		// Label warning = new Label("Para avanzar todos los lenguajes han de estar configurados");
        /*Label warning = new Label("");
        if (!c.getConfig().isDirTemas() ){
            warning.setText("Debes configurar el directorio de cursos");
        }*/

		root.add(dependencies, 0, 0);
		root.add(pathDep, 1, 0 );
		root.add(search, 3, 0);
		//root.add(acceptPath, 3, 0);
		root.add(languagesLabel, 0, 1);
		root.add(languageList, 0, 2);
		root.add(back, 0, 3);
		root.add(change, 1, 3);
		root.add(accept, 2, 3);
		//root.add(warning, 0, 4);

		languagesLabel.setAlignment(Pos.TOP_CENTER);
		//warning.setAlignment(Pos.TOP_CENTER);
		
		GridPane.setConstraints(dependencies, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		GridPane.setConstraints(pathDep, 1, 0, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		GridPane.setConstraints(search, 3, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		//GridPane.setConstraints(acceptPath, 3, 0, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
		//		new Insets(5));
		GridPane.setConstraints(languagesLabel, 0, 1, 4, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
				new Insets(20,5,5,5));
		GridPane.setConstraints(languageList, 0, 2, 4, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
				new Insets( 5));
		GridPane.setConstraints(back, 0, 3, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		GridPane.setConstraints(change, 1, 3, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		GridPane.setConstraints(accept, 3, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.NEVER,
				new Insets(5));
		//GridPane.setConstraints(warning, 0, 4, 4, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS,
		//		new Insets(5));

		// Listeners
		back.setOnAction( (event) -> {
			c.start();
		});

		accept.setOnAction( (event) -> {
		    if ( c.getConfig().isDirTemas() ) {
			//if (c.externalResourcesPath != null ){
			    c.getConfig().save();
			    //c.savePrefs(pathDep.getText(), data); // Esto sobrará
			    c.start();
			} else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(Controller.getLocalizedString("config.warning"));
                alert.setHeaderText(Controller.getLocalizedString("config.msg_select1"));
                alert.setContentText(Controller.getLocalizedString("config.msg_select2"));
                alert.showAndWait();
            }
		});

		change.setOnAction( (event) -> {
			// este es para el lenguaje
			ConfigEntry entry = languageList.getSelectionModel().getSelectedItem();
			if (entry != null) {
				String path = new FileSelector(c.getPrimaryStage(), entry.getValue()).getPath();
				//c.showSelection(entry.getKey());
				c.getConfig().set(entry.getKey(), path); // Almacena la configuración
				entry.setValue(path);
				data.set(data.indexOf(entry), entry); // Actualiza la vista
			} else {
				//warning.setText("Selecciona un lenguaje antes");
			}
		});

		search.setOnAction( (event) -> {
			// este es para el directorio
			DirectorySelector ds = new DirectorySelector(c.getPrimaryStage(), pathDep.getText());
            File newDir = ds.getFile();
            //String externalResourcesPath = c.selectDirectory(pathDep.getText());
			if ( newDir != null && newDir.exists() && newDir.isDirectory() &&
                    !newDir.getAbsolutePath().equals(pathDep.getText())) {
				pathDep.setText(newDir.getAbsolutePath());
				//warning.setText("");
				Controller.log.info(newDir.getAbsolutePath());
				c.getConfig().clear(); // Borro todas las configuraciones antiguas
				c.getConfig().setDirTemas(newDir.getAbsolutePath());
				List<String> lanL = InternalUtilities.getDirectoryList(newDir.getAbsolutePath());
				// añadimos los lenguajes a la lista
				List<String> lentrynames = configEntries(lanL);
				List<ConfigEntry> lentries = new ArrayList<>();
				for (String entryname : lentrynames) {
					lentries.add( new ConfigEntry(entryname, c.getConfig().get(entryname) ) );
				}
				data.removeAll();
				data.setAll(lentries);
			} else {
				//warning.setText("Directorio de temas inválido");
			}
		});

        //change.getStyleClass().add("start");
		languagesLabel.getStyleClass().add("title");
		accept.getStyleClass().add("conf_aceptar");
		back.getStyleClass().add("conf_cancelar");
		languageList.setId("table");
		//warning.getStyleClass().add("error");
		root.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

		return root;
	}

	private List<String> configEntries( List<String> directoryNames ) {
        List<String> lentrynames = new ArrayList<>();
        for (String k : directoryNames) {
            lentrynames.addAll(Language.configuration(k));
        }
        return lentrynames;
    }

}
