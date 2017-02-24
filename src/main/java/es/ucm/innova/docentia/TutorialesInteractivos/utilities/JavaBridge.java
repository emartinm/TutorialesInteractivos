package es.ucm.innova.docentia.TutorialesInteractivos.utilities;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import javafx.application.Platform;

/**
 * Created by kike on 24/02/17.
 */
public class Bridge {
    private Controller c;

    public Bridge(Controller c) {
        this.c = c;
    }

    public void exit() {
        Platform.exit();
    }
}