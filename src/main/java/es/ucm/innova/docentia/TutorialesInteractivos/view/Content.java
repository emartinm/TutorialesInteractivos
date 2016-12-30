package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.CodeQuestions;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Element;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Explanation;
import es.ucm.innova.docentia.TutorialesInteractivos.model.OptionQuestions;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Question;
import es.ucm.innova.docentia.TutorialesInteractivos.model.SyntaxQuestions;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;


/**
 * Vista de los elementos de la leccion
 * 
 * @author Carlos, Rafa
 *
 */
public class Content extends Pane {

	private static Logger log = Logger.getLogger("TutorialesInteractivos");
	private final ToggleGroup group = new ToggleGroup();

	/**
	 * @param e
	 * @param c
	 * @param steps
	 * @param enabled
	 * @param selected
	 * @return
	 */
	public Pane content(Element e, Controller c, int steps, int enabled, int selected) {

		Label type = new Label(null);

		if (selected == 1) {
			type.setText("Introducción");
		} else if (selected == steps) {
			type.setText("Final");
		} else  if (e instanceof Question) {
			type.setText("Pregunta");
		} else if (e instanceof Explanation) {
			type.setText("Explicación");
		}
		GridPane mainPane = new GridPane();
		VBox container = new VBox(5); // Texto y campo de respuesta si es una
										// pregunta
		Pagination paginator = new Pagination(steps); // paginador
		String content = null;
		// por defecto se habilitan 2, la intro y el siguiente
		paginator.enabledProperty().setValue(enabled);
		paginator.currentProperty().setValue(selected);
		// listener para saber cual ha seleccionado.
		paginator.currentProperty().addListener((prop, oldV, newV) -> {
			// llamamos al método que vale para cambiar de un contenido a otro
			c.lessonPageChange(newV);
		});
		content = c.markToHtml(e.getText());

		// Campo donde se escribeel enunciado o la explicacion de la pregunta
		Node text = InternalUtilities.creaBrowser(content);
		//WebView text = InternalUtilities.creaBrowser(content);


		//engine.loadContent(content);
		//engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");


		container.getChildren().addAll(type);
		container.getChildren().add(text);
		//container.getChildren().add(jpnode);

		type.setAlignment(Pos.CENTER);

		Label labelCode = new Label("CÓDIGO");
		TextArea taCode = new TextArea();
		taCode.setPromptText("Escriba aquí su código");

		HBox result = new HBox(10);// Contenedor donde se muestra la resolucion de
									// la pregunta
		Label isCorrect = new Label();// Indica si la pregunta se ha respondido bien
									// o no
		Button hints = new Button("Ver pistas");

		result.getChildren().addAll(isCorrect);
		result.getChildren().addAll(hints);
		hints.setAlignment(Pos.BOTTOM_RIGHT);
		hints.setVisible(false);

		BorderPane answerBox = new BorderPane();// Contenedor con el campo de respuesta
											// y los botones de la pregunta

		// Botones para el envio/ayuda de respuestas
		VBox buttonsCode = new VBox(5);
		Button help = new Button("Ayuda");
		Button resolve = new Button("Resolver");
		buttonsCode.setAlignment(Pos.CENTER);

		VBox options = new VBox();

		if (e instanceof OptionQuestions) {
			final OptionQuestions o = (OptionQuestions) e;

			if (!o.getMulti()) {
				List<RadioButton> l = new ArrayList<RadioButton>();
				List<String> opc = o.getOptions();

				for (Object op : opc) {
					RadioButton rb = new RadioButton();
					rb.setText(op.toString());
					rb.setToggleGroup(group);
					l.add(rb);
				}

				options.getChildren().addAll(l);

			} else {
				List<CheckBox> l = new ArrayList<CheckBox>();
				List<String> opc = o.getOptions();
				for (Object op : opc) {
					CheckBox cb = new CheckBox();
					cb.setText(op.toString());

					l.add(cb);
				}
				options.getChildren().addAll(l);
			}
			answerBox.setCenter(options);
			answerBox.setRight(buttonsCode);
			
			container.getChildren().addAll(answerBox);
			container.getChildren().addAll(result);
		} else {
			if (e instanceof Question) {
				container.getChildren().addAll(labelCode);
				answerBox.setCenter(taCode);
				answerBox.setRight(buttonsCode);
				container.getChildren().addAll(answerBox);
				container.getChildren().addAll(result);
			}
		}

		Button menu = new Button("Menu principal");

		buttonsCode.getChildren().addAll(resolve);
		buttonsCode.getChildren().addAll(help);

		menu.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				c.goSubjectsMenu();
			}

		});

		help.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				final Popup popup = new Popup();
				String helpText = e.getClue();
				Label popupLabel = new Label(helpText);
				popup.setAutoHide(true);
				popupLabel.setStyle("-fx-border-color: black; -fx-background-color: white");

				Node eventSource = (Node) event.getSource();
				Bounds sourceNodeBounds = eventSource.localToScreen(eventSource.getBoundsInLocal());
				popup.setX(sourceNodeBounds.getMinX() - 5.0);
				popup.setY(sourceNodeBounds.getMaxY() + 5.0);
				popup.getContent().addAll(popupLabel);
				popup.show(c.getPrimaryStage());

			}
		});

		resolve.setOnAction(new EventHandler<ActionEvent>() {

			
			@Override
			public void handle(ActionEvent event) {
				if (e instanceof OptionQuestions) {
					ArrayList<Integer> resp = new ArrayList<Integer>();
					if (!((OptionQuestions) e).getMulti()) // Si la pregunta no es
													// multirespuesta
					{
						int i = 0;// Contador de la posicion de la opcion que se
									// analiza
						for (Node o : options.getChildren()) // Recorre el
																// array de
																// RadioButtons
						{
							i++;
							if (((RadioButton) o).isSelected()) // Comprueba si
																// la opcion
																// está
																// seleccionada
								resp.add(i);// Se añade al array de respuestas
						}
					} else // La pregunta es multirespuesta
					{
						int i = 0;// Contador de la posicion de la opcion que se
									// analiza
						for (Node o : options.getChildren()) // Recorre array
																// de CheckBox
						{
							i++;
							if (((CheckBox) o).isSelected())
								resp.add(i);// meter respuestas elegidas en
											// array
						}
					}

					if (c.check(resp, (Question) e))// Se corrige la pregunta
					{
						isCorrect.setText("CORRECTO");
						isCorrect.setStyle("-fx-background-color: #33cc33");
						try{
							c.enableNextStep(selected);
						} catch (Exception e){
							c.finishedLesson();
						}
						paginator.enabledProperty().setValue(enabled + 1);
						hints.setVisible(false);

					} else {
						isCorrect.setText("RESPUESTA INCORRECTA");
						isCorrect.setStyle("-fx-background-color: red");
						hints.setVisible(false);
					}

				} // Fin de opciones

				else if (e instanceof CodeQuestions) // La pregunta es de type codigo
				{
					CodeQuestions pc = (CodeQuestions) e;
					String code = taCode.getText();

					if (c.check(code, pc))// Se manda el codigo al controlador
											// para que el modelo lo compruebe
					{
						isCorrect.setText("CORRECTO");
						isCorrect.setStyle("-fx-background-color: #33cc33");
						hints.setVisible(false);
						try{
							c.enableNextStep(selected);
						} catch (Exception e){
							c.finishedLesson();
						}
						paginator.enabledProperty().setValue(enabled + 1);

					} else {//
						isCorrect.setText(pc.getCorrection().getMessage());
						isCorrect.setStyle("-fx-background-color: red");
						hints.setVisible(true);
					}

				} else if (e instanceof SyntaxQuestions) {
					SyntaxQuestions ps = (SyntaxQuestions) e;
					String code = taCode.getText();
					if (c.check(code, ps)) {
						isCorrect.setText("CORRECTO");
						isCorrect.setStyle("-fx-background-color: #33cc33");
						hints.setVisible(false);
						try{
							c.enableNextStep(selected);
						} catch (Exception e){
							c.finishedLesson();
						}
						paginator.enabledProperty().setValue(enabled + 1);

					} else {//
						isCorrect.setText("HAS FALLADO");
						isCorrect.setStyle("-fx-background-color: red");
						hints.setVisible(false);
					}
				}
			}
		});

		resolve.setMaxWidth(Double.MAX_VALUE);
		help.setMaxWidth(Double.MAX_VALUE);
		hints.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Popup popup = new Popup();
				List<String> hints = ((CodeQuestions) e).getCorrection().getHints();
				String txt = "";
				if (hints != null) {
					for (String h : hints) {
						txt += (h + "\n");
					}
					;
					// hintsContent.setText(txt);

					Label popupLabel = new Label(txt);
					popupLabel.setStyle("-fx-border-color: black; -fx-background-color: white");
					popup.setAutoHide(true);
					popup.setAutoFix(true);
					popup.setOpacity(1.00);
					// Calculate popup placement coordinates.
					Node eventSource = (Node) event.getSource();
					Bounds sourceNodeBounds = eventSource.localToScreen(eventSource.getBoundsInLocal());
					popup.setX(sourceNodeBounds.getMinX() + 5.0);
					popup.setY(sourceNodeBounds.getMaxY() + 1.0);
					popup.getContent().addAll(popupLabel);
					popup.show(c.getPrimaryStage());
				}

			}
		});
		
		Button subjectButton = new Button("Elegir tema");
		Button lessonButton = new Button("Elegir lección");
		
		HBox buttonsLabel = new HBox(10);
		buttonsLabel.getChildren().addAll(subjectButton);
		buttonsLabel.getChildren().addAll(lessonButton);
		buttonsLabel.setAlignment(Pos.BOTTOM_CENTER);
		
		subjectButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				c.showStart();
				
			}
		});
		
		lessonButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				c.backLessonsMenu();
				
			}
		});

		
		mainPane.add(container, 0, 0);
		mainPane.add(paginator, 0, 1);
		mainPane.add(buttonsLabel, 0, 2);
		
		GridPane.setConstraints(container, 0, 0, 1, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER,new Insets(5));
		GridPane.setConstraints(paginator, 0, 1, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(buttonsLabel, 0, 2, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		
		labelCode.getStyleClass().add("labcode");
		type.getStyleClass().add("tipo");
		answerBox.getStyleClass().add("respuestaBox");
		mainPane.getStylesheets().add(getClass().getResource("/css/content.css").toExternalForm());

		return mainPane;
	}

}
