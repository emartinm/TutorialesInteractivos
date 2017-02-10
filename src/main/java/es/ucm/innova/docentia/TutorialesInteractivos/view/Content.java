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
public class Content extends GridPane {
	private ToggleGroup group = new ToggleGroup();
    private List<String> list_hints = null;
    private Label type;
    private VBox container;
    private Node text;
    private Label labelCode;
    private TextArea taCode;
    private HBox result;
    private Label isCorrect;
    private Button hints;
    private BorderPane answerBox;
    private VBox buttonsCode;
    private Button help;
    private Button resolve;
    private VBox options;
    private SimplePagination paginator;


	/* Crea la vista del elemento de actual de la lección actual */
	public Content(Controller c, Lesson le) {
		//GridPane mainPane = new GridPane();
        container = generateContainer(c, le);
		//container = new VBox(5); // Texto y campo de respuesta si es una pregunta

        paginator = new SimplePagination(le.getElements().size(),
				le.getLatestEnabledElement(), le.getCurrentElementPos(), c);

        HBox buttonsLabel = generateBottomButtons(c);
		
		this.add(container, 0, 0);
		this.add(paginator, 0, 1);
		this.add(buttonsLabel, 0, 2);
		
		GridPane.setConstraints(container, 0, 0, 1, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER,new Insets(5));
		GridPane.setConstraints(paginator, 0, 1, 1, 1, HPos.LEFT, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		GridPane.setConstraints(buttonsLabel, 0, 2, 1, 1, HPos.CENTER, VPos.BOTTOM, Priority.ALWAYS, Priority.ALWAYS, new Insets(5));
		this.getStylesheets().add(getClass().getResource("/css/content.css").toExternalForm());
	}

    private HBox generateBottomButtons(Controller c) {
        HBox buttonsLabel = new HBox(10);
        Button subjectButton = new Button("Elegir tema");
        Button lessonButton = new Button("Elegir lección");
        //Button menu = new Button("Menu principal");

        buttonsLabel.getChildren().addAll(subjectButton, lessonButton);
        buttonsLabel.setAlignment(Pos.BOTTOM_CENTER);

        subjectButton.setOnAction( (event) -> {
            c.showStart();
        });

        lessonButton.setOnAction( (event) -> {
            c.backLessonsMenu();
        });

        /*menu.setOnAction( (event) -> {
            c.start();
		})*/;


        return buttonsLabel;
    }

    private void showCorrectionMessages(Question cq, Correction correction, Label isCorrect, Button hints) {
	    if (correction != null ) {
            showHintsButton(correction.getHints(), hints);
            showMessage(isCorrect, correction);
	    }
    }

    private void showHintsButton(List<String> l_hints, Button hints) {
		// Solo muestra el botón de pistas si lo ultimo que se realizo fue una corrección
		// sin exito y además si hay pistas
        this.list_hints = l_hints;
        hints.setVisible(l_hints != null && l_hints.size() > 0);
	}

    private void showMessage(Label l, Correction correction) {
        if (correction.getResult() == ExecutionMessage.COMPILATION_ERROR ){
            l.setText("ERROR DE COMPILACIÓN");
            l.getStyleClass().add("msjIncorrecto");
        } else if (correction.getResult() == ExecutionMessage.EXECUTION_ERROR ) {
            l.setText("ERROR EN EJECUCIÓN");
            l.getStyleClass().add("msjIncorrecto");
        } else if (correction.getResult() == ExecutionMessage.KILLED ) {
            String msg = "EJECUCIÓN ABORTADA";
            if (correction.getMessage() != null && correction.getMessage().length() > 0){
                msg = msg + ": " + correction.getMessage();
            }
            l.setText(msg);
            l.getStyleClass().add("msjIncorrecto");
        } else if (correction.getResult() == ExecutionMessage.OK) {
            if (correction.isCorrect() ) {
                l.setText("CORRECTO");
                l.getStyleClass().add("msjCorrecto");
            } else {
                String msg = "RESPUESTA INCORRECTA";
                if (correction.getMessage() != null && correction.getMessage().length() > 0){
                    msg = msg + ": " + correction.getMessage();
                }
                l.setText(msg);
                l.getStyleClass().add("msjIncorrecto");
            }
        }
	}

    private void clearMessage(Label l) {
		l.setText("");
        l.getStyleClass().clear();
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

    private VBox generateContainer(Controller c, Lesson le) {
        VBox container = new VBox();
        Element e = this.lessonCurrentElement(le);

        String cabecera = c.getCurrentLanguage() + " > " + c.getSubject().getTitle() + " > " + le.getTitle();
        type = new Label(cabecera);
        type.setAlignment(Pos.CENTER);
        container.getChildren().addAll(type);
        type.getStyleClass().add("tipo");

        String content = c.markToHtml(e.getText());
        // Campo donde se escribe el enunciado o la explicacion de la pregunta
        text = InternalUtilities.creaBrowser(content);
        container.getChildren().add(text);

        labelCode = generateLabelCode(e);
        container.getChildren().addAll(labelCode);

        answerBox = generaAnswerBox(c, e);
        container.getChildren().addAll(answerBox);

        result = generaResult();
        container.getChildren().addAll(result);

        return container;
    }

    private BorderPane generaAnswerBox(Controller c, Element e) {
        BorderPane answerBox = new BorderPane();
        answerBox.getStyleClass().add("respuestaBox");
        Correction correction = null;

        //answerBox = new BorderPane();// Contenedor con el campo de respuesta y los botones de la pregunta

        // Botones para el envio/ayuda de respuestas
        buttonsCode = new VBox(5);
        help = new Button("Pistas");
        resolve = new Button("Resolver");
        buttonsCode.setAlignment(Pos.CENTER);
        buttonsCode.getChildren().addAll(resolve);
        buttonsCode.getChildren().addAll(help);

        Node resp;
        if (e instanceof OptionQuestion) {
            resp = generateOptions(e);
        } else if (e instanceof CodeQuestion) {
            resp = generateCode(e);
        }

        answerBox.setCenter(resp);
        answerBox.setRight(buttonsCode);

        // TODO
        // TODO
        // Seguir redistribuyendo aquí

        // Crea las opciones o el campo de texto
        if (e instanceof OptionQuestion) {
            options = new VBox();
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


        } else if (e instanceof CodeQuestion) {
            taCode = new TextArea();
            taCode.setPromptText("Escriba aquí su código");
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
            answerBox.setCenter(taCode);
            answerBox.setRight(buttonsCode);


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
                clearMessage(isCorrect);
                hints.setVisible(false);
                //showHintsButton(cq, hints);
                c.updateAndSaveCurrentLessonProgress();
            });
        }
        // Faltaria tratar el caso de SyntaxQuestion, pero las vamos a eliminar




        help.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                final Popup popup = new Popup();
                String helpText = e.getClue();
                Label popupLabel = new Label(helpText);
                popup.setAutoHide(true);
                popupLabel.getStyleClass().add("hints");
                //popupLabel.setStyle("-fx-border-color: black; -fx-background-color: white");

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

        answerBox.getStyleClass().add("respuestaBox");
        return answerBox;
    }

    private HBox generaResult() {
        HBox hb = new HBox(10);// Contenedor donde se muestra la resolucion de la pregunta
        isCorrect = new Label();// Indica si la pregunta se ha respondido bien o no
        hints = new Button("Más pistas");

        hb.getChildren().addAll(isCorrect);
        hb.getChildren().addAll(hints);
        hints.setAlignment(Pos.BOTTOM_RIGHT);
        hints.setVisible(false);
        return hb;
    }

    private Label generateLabelCode(Element e) {
        Label lb = new Label();
        if (e instanceof OptionQuestion ) {
            lb.setText("Opciones");
        } else if (e instanceof CodeQuestion) {
            lb.setText("Código");
        }
        lb.getStyleClass().add("labcode");
        return lb;
    }
}
