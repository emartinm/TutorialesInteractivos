package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.io.*;
import java.nio.file.FileSystems;

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
        return (name != null) && (path != null) && (interpreter != null);
    }

    protected ProcessBuilder getCompilationProcess(String correctorRelativePath, String code, String sourcePath, String outputFilePath) {
        return null; // Python no necesita compilar
    }

    protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath) {
        Controller.log.info( "Ejecutando: " + execPath + " " + jsonPath);
        return new ProcessBuilder(this.interpreter, execPath, jsonPath);
    }

    /*
	* correctorRelativePath contiene la ruta al fichero correctos DESDE el directorio del lenguaje this.path
	* */
    public Correction compileAndExecute(String correctorRelativePath, String code) {
        File jsonFile = null;
        File sourceFile = null;
        Correction c = null;
        try {
            jsonFile = File.createTempFile("json", null);
            String jsonFilename = jsonFile.getAbsolutePath();
            sourceFile = File.createTempFile("program", ".py");
            String sourceFilename = sourceFile.getAbsolutePath();
            String correctorPath = this.path + FileSystems.getDefault().getSeparator() + correctorRelativePath;
            System.out.println(correctorPath);
            reemplazaHueco(correctorPath, sourceFilename, code);
            //ProcessBuilder pb = getExecutionProcess(sourceFilename, jsonFilename);
            c = execute(sourceFilename, jsonFilename);
        } catch (IOException e) {
            c = new Correction(ExecutionMessage.EXECUTION_ERROR, "Imposible crear ficheros temporales",
                    null, false);
            e.printStackTrace();
        } finally {
            //if ( jsonFile != null ) { jsonFile.delete(); }
            //if ( sourceFile != null ) { sourceFile.delete(); }
        }
        return c;
    }
}