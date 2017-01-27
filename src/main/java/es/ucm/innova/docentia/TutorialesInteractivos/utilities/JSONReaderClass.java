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
    public static Map<String,Object> loadProgress(String filename) {
        mapper = new ObjectMapper(); // can reuse, share globally
        Map<String,Object> progress = new HashMap<String, Object>();

        try{
            File f = new File(filename);
            progress = mapper.readValue(f, Map.class);
        } catch (java.io.IOException e ) {
            //e.printStackTrace();
            Controller.log.info( "Imposible abrir el fichero de progreso: " + filename);
        }
        Controller.log.info( "Se ha cargado con éxito el fichero de progreso: " + filename);
        return progress;
    }

    public static void writeProgress(Map<String,Object> progress, String filename) {
        if (progress == null) {
            Controller.log.info( "No hay progreso que salvar: " + filename);
        }

        try{
            File f = new File(filename);
            mapper.writerWithDefaultPrettyPrinter().writeValue(f, progress);
            Controller.log.info( "Escrito en el fichero de progreso: " + filename);
        } catch (java.io.IOException e ) {
            e.printStackTrace();
            Controller.log.info( "Imposible escribire en el fichero de progreso: " + filename);
        }
    }

}
