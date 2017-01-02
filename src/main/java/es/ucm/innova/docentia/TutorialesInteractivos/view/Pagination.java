package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Clase para introducir el paginador en los tutoriales
 * 
 * Tomado y adaptado de: Manuel Montenegro (mmontene@ucm.es)
 * 
 */
public class Pagination extends StackPane {
	private static Logger log = Logger.getLogger("TutorialesInteractivos");

	/*
	 * Index of the currently selected button (from 1 to numButtons)
	 */
	private IntegerProperty currentProperty;

	/*
	 * Index of the last enabled button (from 1 to numButtons) Those from
	 * enabledProperty+1 to numButtons will be disabled
	 */
	private IntegerProperty enabledProperty;

	/*
	 * Text to show in each step button. It depends on the button index.
	 */
	private final ObjectProperty<Function<Integer, String>> stepText;

	/*
	 * Text to show at the bottom of the component.
	 */
	private final ObjectProperty<BiFunction<Integer, Integer, String>> labelText;

	private Button[] stepButtons;
	private Button previousButton, nextButton;
	private ScrollPane scrollPane;
	//private ScrollBar scrollBar;
	private Label countLabel;

	private HBox generateButtons(int n) {
		stepButtons = new Button[n];
		HBox box = new HBox(5);
		// n+1 para desplazar una casilla y que entre la intro
		for (int i = 1; i <= n; i++) {
			Button currentButton = new Button(String.valueOf(i));
			currentButton.disableProperty().bind(enabledProperty.greaterThanOrEqualTo(i).not());
			currentButton.setMinWidth(USE_PREF_SIZE);
			currentButton.setMinHeight(USE_PREF_SIZE);
			final int j = i;

			currentButton.textProperty()
					.bind(Bindings.createStringBinding(() -> stepText.getValue().apply(j), stepText));
			box.getChildren().add(currentButton);

			currentButton.setOnAction(evt -> {
				currentProperty.setValue(j);
			});

			stepButtons[i - 1] = currentButton;
		}
		box.setAlignment(Pos.CENTER);
		return box;
	}

	/**
	 * Gets the property of the current selected button.
	 * 
	 * @return Property of the selected button
	 */
	public IntegerProperty currentProperty() {
		return currentProperty;
	}

	/**
	 * Gets the property that specifies how many buttons will be enabled.
	 * 
	 * @return Enabled property
	 */
	public IntegerProperty enabledProperty() {
		return enabledProperty;
	}

	/**
	 * Returns the step text property. This is a function that specifies the
	 * text of the buttons in each of the steps.
	 * 
	 * 
	 * @return Step text property
	 */
	public ObjectProperty<Function<Integer, String>> stepTextProperty() {
		return stepText;
	}

	/**
	 * It returns the label text property. This is a function specifying how to
	 * generate the string that indicates the currently selected button. This
	 * function receives the currently selected step and the number of step in
	 * the component.
	 * 
	 * 
	 * @return Label text property
	 */
	public ObjectProperty<BiFunction<Integer, Integer, String>> labelTextProperty() {
		return labelText;
	}

	/**
	 * It returns the text property of the  Previous button in the
	 * component.
	 * @return A string property with the text of the "Previous" button.
	 */
	public StringProperty previousTextProperty() {
		return previousButton.textProperty();
	}

	/**
	 * It returns the text property of the Next  button in the component.
	 * 
	 * @return A string property with the text of the "Next" button.
	 */
	public StringProperty nextTextProperty() {
		return nextButton.textProperty();
	}

	public Pagination(int numSteps) {
		// Inicializamos las propiedades. Por defecto suponemos que
		// todos los botones están habilitados
		this.currentProperty = new SimpleIntegerProperty(1);
		this.enabledProperty = new SimpleIntegerProperty(numSteps);

		// Por defecto cada botón muestra el número de paso
		this.stepText = new SimpleObjectProperty<>(String::valueOf);

		// Por defecto el label de la parte inferior muestra el paso actual
		// y el número de pasos totales separados por un "/"
		this.labelText = new SimpleObjectProperty<>((cur, num) -> cur + " / " + num);

		// Creamos los botones
		Pane outerGridPane = createControls(numSteps);

		// Añadimos el estilo 'selected-button' al seleccionado
		//TODO añadir estilos como proceda aquí
		stepButtons[currentProperty.getValue() - 1].getStyleClass().add("selected-button");

		// Cada vez que cambie el índice seleccionado, cambiamos añadimos el
		// estilo 'selected-button' al nuevo botón seleccionado y lo quitamos
		// del antiguo
		currentProperty.addListener((obs, oldV, newV) -> {
			stepButtons[(int) oldV - 1].getStyleClass().remove("selected-button");
			stepButtons[(int) newV - 1].getStyleClass().add("selected-button");

			// Para mostrar algunos botones anteriores al principio de la leccion.
			// Según se llega al final, el offset se hace 0
			int int_pos = (int) newV - 1;
			double pos = (double) int_pos;
			double offset1 = (pos - stepButtons.length);
			double offset = Math.round(offset1*3.0 / (double)stepButtons.length);
			this.scrollPane.setHvalue( int_pos + offset);
		});

		// El botón de 'Next >>' estará deshabilitado si el paso actual es igual
		// o mayor que el máximo de los actualmente habilitados, o de todos los
		// botones del control
		nextButton.disableProperty()
				.bind(Bindings.createBooleanBinding(
						() -> currentProperty.getValue() >= Math.min(numSteps, enabledProperty.getValue()),
						currentProperty, enabledProperty));

		// El botón de '<< Previous' estará deshabilitado si el paso actual es
		// menor o igual que 1
		previousButton.disableProperty()
				.bind(Bindings.createBooleanBinding(() -> currentProperty.getValue() <= 1, currentProperty));

		// Cada vez que se pulsa el botón 'Next >>' se modifica el botón actual
		nextButton.setOnAction(evt -> {
			int newValue = currentProperty.getValue() + 1;
			currentProperty.setValue(newValue);
			centerNodeInScrollPane(scrollPane, stepButtons[(int) newValue - 1]);
		});

		// Igualmente para '<< Previous'
		previousButton.setOnAction(evt -> {
			int newValue = currentProperty.getValue() - 1;
			currentProperty.setValue(newValue);
			centerNodeInScrollPane(scrollPane, stepButtons[(int) newValue - 1]);
		});

		this.getChildren().add(outerGridPane);
		this.getStyleClass().add("new-paginator");
	}

	/*
	 * Tomado y adaptado de:
	 * 
	 * http://stackoverflow.com/questions/15840513/javafx-scrollpane-
	 * programmatically-moving-the-viewport-centering-content
	 * 
	 */
	private void centerNodeInScrollPane(ScrollPane scrollPane, Node node) {
		double h = scrollPane.getContent().getBoundsInLocal().getWidth();
		double y = (node.getBoundsInParent().getMaxX() + node.getBoundsInParent().getMinX()) / 2.0;
		double v = scrollPane.getViewportBounds().getWidth();
		scrollPane.setHvalue(scrollPane.getHmax() * ((y - 0.5 * v) / (h - v)));
	}

	private Pane createControls(int numSteps) {
		GridPane outerGridPane = new GridPane();
		previousButton = new Button("<< Anterior");
		previousButton.setMinWidth(GridPane.USE_PREF_SIZE);
		previousButton.getStyleClass().add("previous-button");
		nextButton = new Button("Siguiente >>");
		nextButton.setMinWidth(GridPane.USE_PREF_SIZE);
		nextButton.getStyleClass().add("next-button");
		HBox hboxStepButtons = generateButtons(numSteps);
		scrollPane = new ScrollPane(hboxStepButtons);
		scrollPane.setHmax(numSteps - 1);
		scrollPane.setStyle("-fx-background-color:transparent;");
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		hboxStepButtons.prefWidthProperty().bind(Bindings.createDoubleBinding(
				() -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
		hboxStepButtons.prefHeightProperty().bind(Bindings.createDoubleBinding(
				() -> scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));
		outerGridPane.addRow(0, previousButton, scrollPane, nextButton);

		/*scrollBar = new ScrollBar();
		scrollBar.maxProperty().bind(scrollPane.vmaxProperty());
		scrollBar.minProperty().bind(scrollPane.vminProperty());
		outerGridPane.add(scrollBar, 1, 1);

		scrollPane.hvalueProperty().bindBidirectional(scrollBar.valueProperty());*/


		countLabel = new Label();
		countLabel.textProperty().bind(Bindings.createStringBinding(
				() -> labelText.getValue().apply(currentProperty.getValue(), numSteps), labelText, currentProperty));
		countLabel.getStyleClass().add("current-page-label");
		outerGridPane.add(countLabel, 1, 2);

		GridPane.setFillWidth(scrollPane, Boolean.TRUE);
		GridPane.setHgrow(scrollPane, Priority.ALWAYS);
		GridPane.setHgrow(previousButton, Priority.NEVER);
		GridPane.setHgrow(nextButton, Priority.NEVER);
		GridPane.setMargin(previousButton, new Insets(0, 5, 0, 5));
		GridPane.setMargin(nextButton, new Insets(0, 5, 0, 5));
		GridPane.setHalignment(countLabel, HPos.CENTER);
		GridPane.setMargin(countLabel, new Insets(2, 0, 2, 0));
		return outerGridPane;
	}

}