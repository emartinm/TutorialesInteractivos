package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * Created by kike on 4/02/17.
 */
public class CppLanguage extends Language {
    private String compiler = null;

    public CppLanguage(String language, String path, ConfigurationData config) {
        this.name = language;
        this.path = path;
        this.compiler = null;
        // Recorre las claves y extrae la entrada que contenga el nombre del lenguaje
        for (String k : config.keys() ) {
            if (k.toLowerCase().contains(language.toLowerCase())) {
                compiler = config.get(k);
            }
        }
    }

    public boolean isConfigured() {
        return (name != null) && (path != null) && (compiler != null);
    }

    protected ProcessBuilder getCompilationProcess(String correctorRelativePath, String code, String sourcePath, String outputFilePath) {
        // TODO:  hacer distinción para Windows con Visual Studio
        Controller.log.info("Ejecutando: " + this.compiler + " " + sourcePath + " -o " + outputFilePath);
        return new ProcessBuilder(this.compiler, sourcePath, "-o" + outputFilePath);
    }

    protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath) {
        Controller.log.info( "Ejecutando: " + execPath + " " + jsonPath);
        return new ProcessBuilder(execPath, jsonPath);
    }

    public Correction compileAndExecute(String correctorRelativePath, String code) {
        File jsonFile = null;
        File sourceFile = null;
        File binaryFile = null;
        Correction c = null;
        try {
            jsonFile = File.createTempFile("json", null);
            String jsonFilename = jsonFile.getAbsolutePath();
            sourceFile = File.createTempFile("program", ".cpp");
            String sourceFilename = sourceFile.getAbsolutePath();
            binaryFile = File.createTempFile("program", ".exe");
            String binaryFilename = binaryFile.getAbsolutePath();

            c = compile(correctorRelativePath, code, sourceFilename, binaryFilename);
            if (c.getResult() != ExecutionMessage.OK ) {
                return c;
            }
            c = execute(binaryFilename, jsonFilename);
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
