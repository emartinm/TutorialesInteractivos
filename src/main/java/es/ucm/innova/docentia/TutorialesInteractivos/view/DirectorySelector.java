/* Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.view;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by kike on 8/02/17.
 */
public class DirectorySelector {

    private File f;

    public DirectorySelector(Stage stage, String prev) {
        DirectoryChooser chooser = new DirectoryChooser();
        File prevF = new File(prev);

        if ( prevF != null && prevF.exists() && prevF.isDirectory() ) {
            chooser.setInitialDirectory(new File(prev));
        }
        f = chooser.showDialog(stage);

        // Si el usuario cancela, se devuelve un archivo con ruta "prev"
        if (f == null) {
            f = prevF;
        }
    }

    public File getFile() {
        return f;
    }
}
