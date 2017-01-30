package es.ucm.innova.docentia.TutorialesInteractivos.view;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
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
 * Created by Enrique Martín <emartinm@ucm.es> on 29/01/17.
 */
public class SimplePagination extends StackPane {

    private Button[] stepButtons;
    private Button previousButton, nextButton;
    private ScrollPane scrollPane;
    //private ScrollBar scrollBar;
    private Label countLabel;

    /*
    * Genera un HBox con n+2 botones. Cada botón, al pulsarlo, invoca a Controller.showSubjectFragment
    * 'current' es el índice el del fragmento actual: 0 .. n+1
    * 'enabled' es el índice del ultimo fragmento habilitado: 0 .. n+1
    * */
    private HBox generateButtons(int n, int enabled, int current, Controller c) {
        //stepButtons = new Button[n+2];
        HBox box = new HBox(5);

        for (int i = 0; i < n+1; i++) {
            Button currentButton = new Button(String.valueOf(i));
            if ( i == current ) {
                currentButton.getStyleClass().add("selected-button");
            }
            int pos = i;
            currentButton.setOnAction( (evt) -> {
                Controller.log.info( "pulsar " + Integer.toString(pos) );
                c.lessonPageChange(pos);
            });
            currentButton.setDisable( (i > enabled) && (i < n+1) );
            currentButton.setVisible(true);
            currentButton.setMinWidth(USE_PREF_SIZE);
            currentButton.setMinHeight(USE_PREF_SIZE);

            currentButton.setText( Integer.toString(i+1) );
            //        .bind(Bindings.createStringBinding(() -> stepText.getValue().apply(j), stepText));
            box.getChildren().add(currentButton);

            //stepButtons[i+1] = currentButton;
        }
        box.setAlignment(Pos.CENTER);
        return box;
    }



    /* numSteps es el número de fragmentos de la lección. Hay que crear numSteps+2 botones: uno
    * para la explicación y otro para la ventana final */
    public SimplePagination(int n, int enabled, int current, Controller c) {
        // Inicializamos las propiedades. Por defecto suponemos que
        // todos los botones están habilitados

        // Creamos los botones
        Pane outerGridPane = createControls(n, enabled, current, c);
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
    /*private void centerNodeInScrollPane(ScrollPane scrollPane, Node node) {
        double h = scrollPane.getContent().getBoundsInLocal().getWidth();
        double y = (node.getBoundsInParent().getMaxX() + node.getBoundsInParent().getMinX()) / 2.0;
        double v = scrollPane.getViewportBounds().getWidth();
        scrollPane.setHvalue(scrollPane.getHmax() * ((y - 0.5 * v) / (h - v)));
    }*/

    private Pane createControls(int n, int enabled, int current, Controller c) {
        GridPane outerGridPane = new GridPane();

        previousButton = new Button("<< Anterior");
        previousButton.setDisable( current == 0);
        previousButton.setOnAction( (evt) -> {
            Controller.log.info( "pulsar " + Integer.toString(current-1) );
            c.lessonPageChange(current-1);
        });
        previousButton.setMinWidth(GridPane.USE_PREF_SIZE);
        previousButton.getStyleClass().add("previous-button");

        nextButton = new Button("Siguiente >>");
        nextButton.setDisable( current == n || current == enabled );
        nextButton.setMinWidth(GridPane.USE_PREF_SIZE);
        nextButton.getStyleClass().add("next-button");
        nextButton.setOnAction( (evt) -> {
            Controller.log.info( "pulsar " + Integer.toString(current+1) );
            c.lessonPageChange(current + 1);
        });

        HBox hboxStepButtons = generateButtons(n, enabled, current, c);
        scrollPane = new ScrollPane(hboxStepButtons);
        //scrollPane.setHmax(n + 2);

        // Para mostrar algunos botones anteriores al principio de la leccion.
        // Según se llega al final, el offset se hace 0
        //int int_pos = (int) current + 1;
        //double pos = (double) int_pos;
        //double offset1 = (pos - n);
        //double offset = Math.round(offset1*3.0 / (double)n);
        scrollPane.setHmin(0.0);
        scrollPane.setHmax(n);
        scrollPane.setHvalue( current );

        scrollPane.setStyle("-fx-background-color:transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        outerGridPane.addRow(0, previousButton, scrollPane, nextButton);

        /*hboxStepButtons.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        hboxStepButtons.prefHeightProperty().bind(Bindings.createDoubleBinding(
                () -> scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));
        outerGridPane.addRow(0, previousButton, scrollPane, nextButton);
        */

		/*scrollBar = new ScrollBar();
		scrollBar.maxProperty().bind(scrollPane.vmaxProperty());
		scrollBar.minProperty().bind(scrollPane.vminProperty());
		outerGridPane.add(scrollBar, 1, 1);

		scrollPane.hvalueProperty().bindBidirectional(scrollBar.valueProperty());*/

        countLabel = new Label( Integer.toString(current + 1 ) + " / " + Integer.toString(n+1));
        //countLabel.textProperty().bind(Bindings.createStringBinding(
        //        () -> labelText.getValue().apply(currentProperty.getValue()+2, numSteps+2), labelText, currentProperty));
        countLabel.getStyleClass().add("current-page-label");
        outerGridPane.add(countLabel, 1, 2);

        //GridPane.setFillWidth(scrollPane, Boolean.TRUE);
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        //GridPane.setHalignment(scrollPane, HPos.CENTER);
        GridPane.setHgrow(previousButton, Priority.NEVER);
        GridPane.setHgrow(nextButton, Priority.NEVER);
        GridPane.setMargin(previousButton, new Insets(0, 5, 0, 5));
        GridPane.setMargin(nextButton, new Insets(0, 5, 0, 5));
        GridPane.setHalignment(countLabel, HPos.CENTER);
        GridPane.setMargin(countLabel, new Insets(2, 0, 2, 0));
        return outerGridPane;
    }

}
