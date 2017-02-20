package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.ArrayList;
import java.util.List;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.*;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    private TextInputControl[] codes;
    private HBox result;
    private Label isCorrect;
    private Button hints;
    private BorderPane answerBox;
    private VBox buttonsCode;
    private Button help;
    private Button resolve;
    private VBox options;
    private SimplePagination paginator;
    private Correction correction;


	/* Crea la vista del elemento de actual de la lección actual */
	public Content(Controller c, Lesson le) {
	    // Calcula la corrección si es necesario
        correctIfNeeded(c, le);

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

    private void correctIfNeeded(Controller c, Lesson le) {
	    Element e = le.getCurrentElement();
	    if (e instanceof Question && ((Question)e).isLastAnswer_checked() ) {
	        Question q = (Question)e;
	        this.correction = q.check(c.getLanguage());
            if (correction.isCorrect()) {
                c.passCurrentElement();
                q.setLastAnswer_correct(true);
            } else {
                q.setLastAnswer_correct(false);
            }
            this.list_hints = correction.getHints();
        }
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

    private void showCorrectionMessages() {
	    if (correction != null ) {
            showHintsButton(correction.getHints());
            showMessage();
	    }
    }

    private void showHintsButton(List<String> l_hints) {
		// Solo muestra el botón de pistas si lo ultimo que se realizo fue una corrección
		// sin exito y además si hay pistas
        this.list_hints = l_hints;
        hints.setVisible(l_hints != null && l_hints.size() > 0);
	}

    private void showMessage() {
        if (correction.getResult() == ExecutionMessage.COMPILATION_ERROR ){
            isCorrect.setText("ERROR DE COMPILACIÓN");
            isCorrect.getStyleClass().add("msjIncorrecto");
        } else if (correction.getResult() == ExecutionMessage.EXECUTION_ERROR ) {
            isCorrect.setText("ERROR EN EJECUCIÓN");
            isCorrect.getStyleClass().add("msjIncorrecto");
        } else if (correction.getResult() == ExecutionMessage.KILLED ) {
            String msg = "EJECUCIÓN ABORTADA";
            if (correction.getMessage() != null && correction.getMessage().length() > 0){
                msg = msg + ": " + correction.getMessage();
            }
            isCorrect.setText(msg);
            isCorrect.getStyleClass().add("msjIncorrecto");
        } else if (correction.getResult() == ExecutionMessage.OK) {
            if (correction.isCorrect() ) {
                isCorrect.setText("CORRECTO");
                isCorrect.getStyleClass().add("msjCorrecto");
            } else {
                String msg = "RESPUESTA INCORRECTA";
                if (correction.getMessage() != null && correction.getMessage().length() > 0){
                    msg = msg + ": " + correction.getMessage();
                }
                isCorrect.setText(msg);
                isCorrect.getStyleClass().add("msjIncorrecto");
            }
        }
	}

    private void clearMessage() {
        isCorrect.setText("");
        isCorrect.getStyleClass().clear();
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

        // Genera la cabecera
        String cabecera = c.getCurrentLanguage() + " > " + c.getSubject().getTitle() + " > " + le.getTitle();
        type = new Label(cabecera);
        type.setAlignment(Pos.CENTER);
        type.getStyleClass().add("tipo");

        String content = c.markToHtml(e.getText());
        // Campo donde se escribe el enunciado o la explicacion de la pregunta
        text = InternalUtilities.creaBrowser(content);
        text.minHeight(100);

        result = generaResult();
        generateLabelCode(e);
        boolean neededAnswerBox = generaAnswerBox(c, e);

        container.getChildren().addAll(type);
        if (neededAnswerBox) {
            VBox answer = new VBox();
            answer.getChildren().addAll(labelCode, answerBox);
            SplitPane split = new SplitPane();
            split.setOrientation(Orientation.VERTICAL);
            split.getItems().addAll(text, answer);
            split.setDividerPositions(1.0); // Muestra answer con su tamaño minimo por defecto
            container.getChildren().add(split);
        } else {
            container.getChildren().add(text);
        }

        container.getChildren().addAll(result);

        return container;
    }

    private boolean generaAnswerBox(Controller c, Element e) {
        answerBox = new BorderPane();
        answerBox.setPadding(new Insets(2,2,2,2));
        answerBox.getStyleClass().add("respuestaBox");


        //answerBox = new BorderPane();// Contenedor con el campo de respuesta y los botones de la pregunta

        // Botones para el envio/ayuda de respuestas
        buttonsCode = new VBox(5);
        buttonsCode.setPadding(new Insets(2,2,2,2));
        help = new Button("Pistas");
        resolve = new Button("Resolver");
        buttonsCode.setAlignment(Pos.CENTER);
        buttonsCode.getChildren().addAll(resolve);
        buttonsCode.getChildren().addAll(help);

        Node left = null;
        if (e instanceof OptionQuestion) {
            left = generateOptions(c, (OptionQuestion)e);
        } else if (e instanceof CodeQuestion) {
            left = generateCode(c, (CodeQuestion)e);
        }
        // Faltaria tratar el caso de SyntaxQuestion, pero las vamos a eliminar

        answerBox.setCenter(left);
        answerBox.setRight(buttonsCode);

        help.setOnAction( (event) -> {
            final Popup popup = new Popup();
            String helpText = e.getHint();
            Label popupLabel = new Label(helpText);
            popup.setAutoHide(true);
            popupLabel.getStyleClass().add("hints");

            Node eventSource = (Node) event.getSource();
            Bounds sourceNodeBounds = eventSource.localToScreen(eventSource.getBoundsInLocal());
            popup.setX(sourceNodeBounds.getMinX() - 5.0);
            popup.setY(sourceNodeBounds.getMaxY() + 5.0);
            popup.getContent().addAll(popupLabel);
            popupLabel.getStylesheets().add(getClass().getResource("/css/content.css").toExternalForm());
            popup.show(c.getPrimaryStage());
        });

        resolve.setOnAction( (event) -> {
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

                OptionQuestion oq = (OptionQuestion)e;
                oq.setLastAnswer(resp);
                oq.setLastAnswer_checked(true);
            } else if (e instanceof CodeQuestion) {
                CodeQuestion pc = (CodeQuestion) e;
                pc.setLastAnswer_checked(true);
            }
            c.reloadCurrentLessonFragment();
        });

        //resolve.setMaxWidth(Double.MAX_VALUE);
        //help.setMaxWidth(Double.MAX_VALUE);
        hints.setOnAction( (event) -> {
            Popup popup = new Popup();
            String txt = "";
            if (hints != null) {
                for (String h : list_hints) {
                    txt += (h + "\n");
                }
                ;
                // hintsContent.setText(txt);

                Label popupLabel = new Label(txt);
                popupLabel.getStyleClass().add("hints");
                //popupLabel.setStyle("-fx-border-color: black; -fx-background-color: white");
                popup.setAutoHide(true);
                popup.setAutoFix(true);
                popup.setOpacity(1.00);
                // Calculate popup placement coordinates.
                Node eventSource = (Node) event.getSource();
                Bounds sourceNodeBounds = eventSource.localToScreen(eventSource.getBoundsInLocal());
                popup.setX(sourceNodeBounds.getMinX() + 5.0);
                popup.setY(sourceNodeBounds.getMaxY() + 1.0);
                popup.getContent().addAll(popupLabel);
                popupLabel.getStylesheets().add(getClass().getResource("/css/content.css").toExternalForm());
                popup.show(c.getPrimaryStage());
            }
        });

        answerBox.getStyleClass().add("respuestaBox");
        return (e instanceof OptionQuestion || e instanceof CodeQuestion);
    }

    private Node generateOptions(Controller c, OptionQuestion o) {
        options = new VBox();
        int i = 1;
        List<Integer> lastAnswer = o.getLastAnswer();
        help.setVisible( o.getHint() != null );
        if (o.isLastAnswer_checked() ) {
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
        showCorrectionMessages();
        return options;
    }

    private Node generateCode(Controller c, CodeQuestion cq) {
        final int nGaps = cq.getNumberGaps();
        codes = new TextInputControl[nGaps];
        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox vboxcodes = new VBox();
        if (nGaps == 1) {
            TextArea t = new TextArea();
            //t.setMaxWidth(Double.MAX_VALUE);
            codes[0] = t;
            t.setPromptText("Escriba aquí su código");
            t.setPrefRowCount(8);
            vboxcodes.getChildren().add(t);
            //VBox.setVgrow(t, Priority.ALWAYS);
        } else {
            for (int i = 0; i < nGaps; ++i) {
                TextArea t = new TextArea();
                t.setPrefRowCount(getPrefLinesFromGaps(nGaps));
                codes[i] = t;
                HBox hb = new HBox(10);
                HBox.setHgrow(t, Priority.ALWAYS);
                Label lb = new Label("Hueco #" + Integer.toString(i));
                hb.getChildren().add(lb);
                lb.setMinWidth(Region.USE_PREF_SIZE);
                hb.getChildren().add(t);
                t.setPromptText("Código del hueco #" + Integer.toString(i));
                vboxcodes.getChildren().add(hb);
                //vboxcodes.getChildren().add(t);
            }
        }
        sp.setContent(vboxcodes);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setMinHeight(150);

        // Si hay alguna respuesta anterior la reestablecemos
        restoreLastAnswer(cq.getLastAnswer());

        help.setVisible( cq.getHint() != null );

        // Si la ultima accion fue una comprobación, se corrije la pregunta
        if (cq.isLastAnswer_checked() ) {
            if (correction.isCorrect()) {
                c.passCurrentElement();
                cq.setLastAnswer_correct(true);
            } else {
                cq.setLastAnswer_correct(false);
            }
            this.list_hints = correction.getHints();
        }


        // Cuando cambie cualquier campo de texto, se actualiza la pregunta y se salva el progreso
        for (int i = 0; i < codes.length; ++i ) {
            codes[i].textProperty().addListener( (ov, old_v, new_v) -> {
                List<String> resp = getCurrentCodes();
                cq.setLastAnswer( resp );
                cq.setLastAnswer_checked(false);
                clearMessage();
                hints.setVisible(false);
                c.updateAndSaveCurrentLessonProgress();
            });
        }

        /*taCode.textProperty().addListener( (ov, old_v, new_v) -> {
            //Controller.log.info("El texto ha cambiado");
            // Cada vez que cambia el texto almacenamos su valor actual y restablecemos el
            // mensaje de corrección
            cq.setLastAnswer( new_v );
            cq.setLastAnswer_checked(false);
            clearMessage();
            hints.setVisible(false);
            //showHintsButton(cq, hints);
            c.updateAndSaveCurrentLessonProgress();
        });*/

        //return sp;
        return vboxcodes;
    }

    private void restoreLastAnswer(List<String> lastAnswer) {
        if (lastAnswer != null && this.codes.length == lastAnswer.size()) {
            for (int i = 0; i < lastAnswer.size(); ++i) {
                codes[i].setText(lastAnswer.get(i));
            }
        }
    }

    private int getPrefLinesFromGaps(int numberGaps) {
        switch (numberGaps) {
            case 1: return 10;
            case 2: return 3;
            default: return 2;
        }
    }

    private HBox generaResult() {
        HBox hb = new HBox(10);// Contenedor donde se muestra la resolucion de la pregunta
        isCorrect = new Label();// Indica si la pregunta se ha respondido bien o no
        hints = new Button("Más pistas");

        hb.getChildren().addAll(isCorrect);
        hb.getChildren().addAll(hints);
        hints.setAlignment(Pos.BOTTOM_RIGHT);
        hints.setVisible(false);

        this.showCorrectionMessages();
        return hb;
    }

    private boolean generateLabelCode(Element e) {
        labelCode = new Label();
        boolean needed = false;
        if (e instanceof OptionQuestion ) {
            labelCode.setText("Opciones");
            needed = true;
        } else if (e instanceof CodeQuestion) {
            labelCode.setText("Código");
            needed = true;
        }
        labelCode.getStyleClass().add("labcode");
        return needed;
    }

    public List<String> getCurrentCodes() {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < codes.length; ++i ) {
            l.add(codes[i].getText());
        }
        return l;
    }
}
