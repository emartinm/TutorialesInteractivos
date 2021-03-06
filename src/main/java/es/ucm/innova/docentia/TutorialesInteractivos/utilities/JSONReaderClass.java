/* Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enrique Martín on 24/01/17.
 */
public final class JSONReaderClass {
    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    /* Carga el progreso desde el fichero progress.json que está en el directorio de cursos y lo almacena
    *  en un atributo. Si no se puede cargar el fichero, crea un Map<String,Object> vacío */
    public static Map<String,Object> loadJSON(String filename) {
        //mapper = new ObjectMapper(); // can reuse, share globally
        Map<String,Object> progress = new HashMap<String, Object>();

        try{
            File f = new File(filename);
            progress = mapper.readValue(f, Map.class);
        } catch (java.io.IOException e ) {
            //e.printStackTrace();
            Controller.log.info( "Unable to open file: " + filename);
        }
        Controller.log.info( "Loaded file: " + filename);
        return progress;
    }

    public static void writeProgress(Map<String,Object> progress, String filename) {
        if (progress == null) {
            Controller.log.info( "There is no progress to save: " + filename);
        }

        try{
            File f = new File(filename);
            mapper.writerWithDefaultPrettyPrinter().writeValue(f, progress);
            Controller.log.info( "Saved to file: " + filename);
        } catch (java.io.IOException e ) {
            e.printStackTrace();
            Controller.log.info( "Unable to save in file: " + filename);
        }
    }

}
