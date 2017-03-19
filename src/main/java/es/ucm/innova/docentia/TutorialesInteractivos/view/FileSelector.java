/* Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.view;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by kike on 8/02/17.
 */
public class FileSelector {

    private File f;

    public FileSelector(Stage stage, String prev) {
        File prevF = null;
        FileChooser chooser = new FileChooser();
        if ( (prev != null) && ((prevF = new File(prev)) != null) && prevF != null && prevF.exists() ) {
            chooser.setInitialDirectory(prevF.getParentFile());
        }
        f = chooser.showOpenDialog(stage);

        // Si el usuario cancela, se devuelve un archivo con ruta "prev"
        if (f == null || !f.exists() || !f.isFile()) {
            f = prevF;
        }
    }

    public String getPath() {
        if (f != null ) {
            return f.getAbsolutePath();
        } else {
            return "";
        }

    }


}
