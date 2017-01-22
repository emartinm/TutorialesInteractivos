package es.ucm.innova.docentia.TutorialesInteractivos.view;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Subject;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.Node;

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
		String content = c.markToHtml("#" + t.getTitle() + "\n##" + t.getIntroduction());
		Node tittle = InternalUtilities.creaBrowser(content);
		//WebEngine engine = tittle.getEngine();
		//engine.loadContent(content);
		
		Label tittleList = new Label("Lecciones");//TItulo de la lista de lecciones
		//Lista de lecciones
		ListView<String> leccionList = new ListView<String>();
		ObservableList<String> obsLecciones = FXCollections.observableArrayList (t.getLessonNames());
		leccionList.setItems(obsLecciones);
		
		
		Button start = new Button("Comenzar leccion");//Boton para comenzar la leccion
		Label error = new Label();//Label de error
		Button back = new Button("Menú anterior");
		
		
		//Colocacion de elementos en el panel
		box.add(tittle, 0, 0);
		box.add(tittleList, 0, 1);
		box.add(leccionList, 0, 2);
		box.add(start, 2, 3);
		box.add(error, 1, 3);
		box.add(back, 0, 3);
				
		
		GridPane.setConstraints(tittle, 0, 0, 3,1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.NEVER, new Insets(5));
		GridPane.setConstraints(tittleList, 0, 1, 3, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(leccionList, 0, 2, 3, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(start, 2, 3, 1, 1, HPos.RIGHT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(error, 1, 3, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(back, 0, 3, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		
		//Listeners
		start.setOnAction(new EventHandler<ActionEvent>(){
			MultipleSelectionModel<String> s;
				@Override
				public void handle(ActionEvent event) {
					s= leccionList.getSelectionModel();
					if (!s.isEmpty()) //Se comprueba que haya una leccion seleccionada
						c.selectedLesson(s.getSelectedIndex());
					else 
					{
						error.setText("Se debe seleccionar una leccion");
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
            Controller.log.info( event.getTarget().toString() );
		} );
		
		
		//Parte estetica
		start.getStyleClass().add("start");
		error.getStyleClass().add("error");
		back.getStyleClass().add("start");
		tittleList.getStyleClass().add("tittle");
		box.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
		return box;
		
	}
}
