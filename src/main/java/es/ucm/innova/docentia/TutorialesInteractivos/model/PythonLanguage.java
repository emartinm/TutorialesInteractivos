package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kike on 3/02/17.
 */
public class PythonLanguage extends Language {
    private String interpreter;

    public PythonLanguage(String language, String path, ConfigurationData config) {
        this.name = language;
        this.path = path;
        this.interpreter = null;
        // Recorre las claves y extrae la entrada que contenga el nombre del lenguaje
        for (String k : config.keys() ) {
            if (k.toLowerCase().contains(language.toLowerCase())) {
                interpreter = config.get(k);
            }
        }
    }

    public boolean isConfigured() {
        return (name != null) && (interpreter != null);
    }

    /*
	* correctorRelativePath contiene la ruta al fichero correctos DESDE el directorio del lenguaje this.path
	* */
    public Correction compileAndExecute(String correctorRelativePath, String code) {
        File jsonFile = null;
        File sourceFile = null;
        Correction c = null;
        try {
            //Caso concreto para Python
            jsonFile = File.createTempFile("json", null);
            String jsonFilename = jsonFile.getAbsolutePath();
            sourceFile = File.createTempFile("program", ".py");
            String sourceFilename = sourceFile.getAbsolutePath();
            String correctorPath = this.path + FileSystems.getDefault().getSeparator() + correctorRelativePath;
            System.out.println(correctorPath);
            // FIXME no obtiene el fichero corrector!!
            reemplazaHueco(correctorPath, sourceFilename, code);
            JSONParser jsonParser = new JSONParser();
            ProcessBuilder pb = new ProcessBuilder(this.interpreter, sourceFilename, jsonFilename);
            Controller.log.info("Ejecutando: " + this.interpreter + " " + sourceFilename + " " + jsonFilename);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            boolean errCode = p.waitFor(2, TimeUnit.SECONDS);
            if (!errCode) { // si ocurre esto es que el python está mal escrito, o bucle infinito
                Controller.log.info("Ejecución abortada tras 2 segundos");
                c = new Correction(ExecutionMessage.KILLED, "Tiempo excedido", fileToListString(br), false);
            } else {
                int exit = p.exitValue();
                // SOLO EN CASO DE ERROR DE LA FUNCION CORRECTORA DEVOLVERA UN VALOR DISTINTO DE 0,
                switch (exit) {
                    case 1: {
                        c = new Correction(ExecutionMessage.EXECUTION_ERROR, "Error en ejecución", fileToListString(br), false);
                        break;
                    }
                    case 0: { // comprobar si están vacios
                        //TODO usar jackson tambien aqui
                        FileReader fileReader = new FileReader(jsonFile);
                        JSONObject json = (JSONObject) jsonParser.parse(fileReader);
                        Boolean correct = (Boolean) json.getOrDefault("isCorrect", false);
                        String error = (String) json.getOrDefault("typeError", "");
                        List<String> hints = (List<String>) json.getOrDefault("Hints", null);
                        c = new Correction(ExecutionMessage.OK, error, hints, correct);
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException | ParseException e) {
            e.printStackTrace();
        } finally {
            //if ( jsonFile != null ) { jsonFile.delete(); }
            //if ( sourceFile != null ) { sourceFile.delete(); }
        }
        return c;
    }
}
