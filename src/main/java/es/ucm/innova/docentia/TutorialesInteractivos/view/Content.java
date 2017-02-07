package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.ArrayList;
import java.util.List;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.*;
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
	private final ToggleGroup group = new ToggleGroup();
    private List<String> list_hints = null;

    /* Devuelve el elemento actual de la lección, o un elemento Explicación con un mensaje
    * de enhorabuena si es el último elemento de la lección */
	private Element lessonCurrentElement(Lesson le) {
		if (le.getCurrentElementPos() >= le.getElements().size() ) {
			String mensajeFinal = "# ¡Enhorabuena! \n## Has terminado la lección '" +
					le.getTitle() + "'";
			return new Explanation(mensajeFinal);
		} else {
			return le.getCurrentElement();
		}
	}

	/**
	 * @param c
	 * @param le
	 * @return
	 */

	/* Crea la vista del elemento de actual de la lección actual */
	//public Pane content(Element e, Controller c, int steps, int enabled, int selected) {
	public Pane content(Controller c, Lesson le) {
		Element e = this.lessonCurrentElement(le);
		Correction correction = null;

		int steps = le.getElements().size();
		int enabled = le.getLatestElement();
		int selected = le.getCurrentElementPos();
		//System.out.println("Total de fragmentos: " + Integer.toString(steps) );
        //System.out.println("Ultimo fragmento habilitado: " + Integer.toString(enabled) );
        //System.out.println("Fragmento a mostrar: " + Integer.toString(selected) );

		Label type = new Label(null);

		if (selected == steps) {
			type.setText("Final");
		} else if (e instanceof Question) {
			type.setText("Pregunta");
		} else if (e instanceof Explanation) {
			type.setText("Explicación");
		}
		GridPane mainPane = new GridPane();
		VBox container = new VBox(5); // Texto y campo de respuesta si es una pregunta

		SimplePagination paginator = new SimplePagination(le.getElements().size(),
				le.getLatestElement(), le.getCurrentElementPos(), c);
		String content = null;
		content = c.markToHtml(e.getText());

		// Campo donde se escribe el enunciado o la explicacion de la pregunta
		Node text = InternalUtilities.creaBrowser(content);
		container.getChildren().addAll(type);
		container.getChildren().add(text);

		type.setAlignment(Pos.CENTER);
		Label labelCode = new Label("CÓDIGO");
		TextArea taCode = new TextArea();
		taCode.setPromptText("Escriba aquí su código");

		HBox result = new HBox(10);// Contenedor donde se muestra la resolucion de la pregunta
		Label isCorrect = new Label();// Indica si la pregunta se ha respondido bien o no
		Button hints = new Button("Más pistas");

		result.getChildren().addAll(isCorrect);
		result.getChildren().addAll(hints);
		hints.setAlignment(Pos.BOTTOM_RIGHT);
		hints.setVisible(false);

		BorderPane answerBox = new BorderPane();// Contenedor con el campo de respuesta y los botones de la pregunta

		// Botones para el envio/ayuda de respuestas
		VBox buttonsCode = new VBox(5);
		Button help = new Button("Pistas");
		Button resolve = new Button("Resolver");
		buttonsCode.setAlignment(Pos.CENTER);

		VBox options = new VBox();

		// Crea las opciones o el campo de texto
		if (e instanceof OptionQuestion) {
			final OptionQuestion o = (OptionQuestion) e;
            int i = 1;
            List<Integer> lastAnswer = o.getLastAnswer();
            help.setVisible( o.getClue() != null );
            if (o.isLastAnswer_checked() ) {
                correction = c.check(lastAnswer, o);
                this.list_hints = correction.getHints();
            }
			if (!o.getMulti()) {
				List<RadioButton> l = new ArrayList<RadioButton>();
				List<String> opc = o.getOptions();
				for (Object op : opc) {
					RadioButton rb = new RadioButton();
					rb.setText(op.toString());
					rb.setToggleGroup(group);
					l.add(rb);
                    if ( lastAnswer != null ) { //Si hay respuesta anterior la carga
                        rb.setSelected( lastAnswer.contains( new Integer(i) ) );
                    }
                    ++i;
				}
				group.selectedToggleProperty().addListener( (ov, old_v, new_v) -> {
					// Cada vez que el grupo cambia, almacena la solución actual y borra
					// el mensaje de corrección
					//clearCorrectMessage(isCorrect);
					o.setLastAnswer_checked(false);
					List<Integer> currentAnswer = getAnswer_rb(l);
					o.setLastAnswer(currentAnswer);
					c.updateAndSaveCurrentLessonProgress();
					c.reloadCurrentLessonFragment();
				});
				options.getChildren().addAll(l);
			} else {
				List<CheckBox> l = new ArrayList<CheckBox>();
				List<String> opc = o.getOptions();
				for (Object op : opc) {
					CheckBox cb = new CheckBox();
					cb.setText(op.toString());
					l.add(cb);
                    if ( lastAnswer != null ) { //Si hay respuesta anterior la carga
                        cb.setSelected( lastAnswer.contains( new Integer(i) ) );
                    }
					cb.setOnAction((event) -> {
						// Cada vez que un checkbox recibe un evento, almacena la solución actual
						// y borra el mensaje de corrección
						o.setLastAnswer_checked(false);
						//clearCorrectMessage(isCorrect);
						List<Integer> current = getAnswer_cb(l);
						o.setLastAnswer(current);
						c.updateAndSaveCurrentLessonProgress();
						c.reloadCurrentLessonFragment();
					});
                    ++i;
				}
				options.getChildren().addAll(l);
			}

			//Si la pregunta fue evaluada, muestra el mensaje y posibles pistas
            showCorrectionMessages( o, correction, isCorrect, hints);

			answerBox.setCenter(options);
			answerBox.setRight(buttonsCode);
			container.getChildren().addAll(answerBox);
			container.getChildren().addAll(result);
		} else if (e instanceof CodeQuestion) {
            CodeQuestion cq = (CodeQuestion)e;
            String lastAnswer = cq.getLastAnswer();
            help.setVisible( cq.getClue() != null );
            if (cq.isLastAnswer_checked() ) {
                correction = c.check(lastAnswer, cq);
				if (correction.isCorrect()) {
					c.passCurrentElement();
					cq.setLastAnswer_correct(true);
				} else {
					cq.setLastAnswer_correct(false);
				}
                this.list_hints = correction.getHints();
            }
            container.getChildren().addAll(labelCode);
			answerBox.setCenter(taCode);
			answerBox.setRight(buttonsCode);
			container.getChildren().addAll(answerBox);
			container.getChildren().addAll(result);

            // Si la ultima accion fue una comprobación, se muestra le mensaje
            // y el botón de pistas si hay alguna

            showCorrectionMessages( cq, correction, isCorrect, hints);
            //if (cq.isLastAnswer_checked() ) {
            //    showCorrectionMessages(cq, correction, isCorrect, hints);
            //}

			// Si hay alguna respuesta anterior la reestablecemos
			if (lastAnswer != null ) {
				taCode.setText( cq.getLastAnswer() );
			}

			taCode.textProperty().addListener( (ov, old_v, new_v) -> {
				//Controller.log.info("El texto ha cambiado");
				// Cada vez que cambia el texto almacenamos su valor actual y restablecemos el
				// mensaje de corrección
				cq.setLastAnswer( new_v );
				cq.setLastAnswer_checked(false);
				clearCorrectMessage(isCorrect);
				//hints.setVisible(false);
				//showHintsButton(cq, hints);
				c.updateAndSaveCurrentLessonProgress();
			});
        }
		// Faltaria tratar el caso de SyntaxQuestion, pero las vamos a eliminar

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
				if (e instanceof OptionQuestion) {
					List<Integer> resp = new ArrayList<Integer>();
					if (!((OptionQuestion) e).getMulti()) { // Si la pregunta no es multirespuesta
						int i = 0;// Contador de la posicion de la opcion que se analiza
						for (Node o : options.getChildren()) {
							i++;
							if (((RadioButton) o).isSelected())
								resp.add(i);
						}
					} else { // La pregunta es multirespuesta
						int i = 0;
						for (Node o : options.getChildren()) {
							i++;
							if (((CheckBox) o).isSelected())
								resp.add(i);
						}
					}

					((OptionQuestion) e).setLastAnswer(resp);
					((OptionQuestion) e).setLastAnswer_checked(true);
					if (c.check(resp, (Question) e).isCorrect()) {
					    c.passCurrentElement();
						((Question) e).setLastAnswer_correct(true);
						hints.setVisible(false);
					} else {
						((Question) e).setLastAnswer_correct(false);
					}
				} else if (e instanceof CodeQuestion) {
					CodeQuestion pc = (CodeQuestion) e;
					String code = taCode.getText();
					pc.setLastAnswer_checked(true);
				}
				c.reloadCurrentLessonFragment();
			}
		});

		resolve.setMaxWidth(Double.MAX_VALUE);
		help.setMaxWidth(Double.MAX_VALUE);
		hints.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Popup popup = new Popup();
				String txt = "";
				if (hints != null) {
					for (String h : list_hints) {
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

    private void showCorrectionMessages(Question cq, Correction correction, Label isCorrect, Button hints) {
	    if (correction != null ) {
            showHintsButton(correction.getHints(), hints);
	        if (correction.isCorrect() ) {
                setCorrectMessage(isCorrect, correction);
	        } else {
	            setIncorrectMessage(isCorrect, correction);
            }
	    }
    }

    private void showHintsButton(List<String> l_hints, Button hints) {
		// Solo muestra el botón de pistas si lo ultimo que se realizo fue una corrección
		// sin exito y además si hay pistas
        this.list_hints = l_hints;
        hints.setVisible(l_hints != null && l_hints.size() > 0);
	}

	private void setCorrectMessage(Label l, Correction correction) {
        l.setText("CORRECTO");
        l.setStyle("-fx-background-color: #33cc33");
    }

    private void setIncorrectMessage(Label l, Correction correction) {
	    String msg = "RESPUESTA INCORRECTA";
	    if (correction.getMessage().length() > 0 ) {
	        msg = msg + ": " + correction.getMessage();
        }
        l.setText(msg);
        l.setStyle("-fx-background-color: #ff5400; -fx-text-fill: white");
    }

    private void clearCorrectMessage(Label l) {
		l.setText("");
	}

	private List<Integer> getAnswer_rb( List<RadioButton> l){
		int i = 0;
		List<Integer> result = new ArrayList<Integer>();
		for (RadioButton rb : l ) {
			i++;
			if ( rb.isSelected() ) {
				result.add( new Integer(i) );
			}
		}
		return result;
	}

	private List<Integer> getAnswer_cb( List<CheckBox> l){
		int i = 0;
		List<Integer> result = new ArrayList<Integer>();
		for (CheckBox cb : l ) {
			i++;
			if ( cb.isSelected() ) {
				result.add( new Integer(i) );
			}
		}
		return result;
	}

    private void logList(List<Integer> l) {
        if (l == null) {
			Controller.log.info("Lista vacia");
        } else {
            String lista = "";
            for (Integer e : l) {
                lista += " " + e.toString();
            }
			Controller.log.info(lista);
        }
    }
}
