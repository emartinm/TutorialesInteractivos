package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kike on 4/02/17.
 */
public class CppLanguage extends Language {
    private String compiler = null;

    protected CppLanguage() {}

    public CppLanguage(String language, String path, ConfigurationData config) {
        this.name = language;
        this.path = path;
        this.compiler = config.get(language);
    }

    public boolean isConfigured() {
        return (name != null) && (path != null) && (compiler != null);
    }

    protected String getSourceExtension(){
        return ".cpp";
    }

    protected String getCompiledExtension(){
        return ".exe";
    }

    protected ProcessBuilder getCompilationProcess(String sourcePath, String outputFilePath) {
        // TODO:  hacer distinción para Windows con Visual Studio
        ProcessBuilder pb = new ProcessBuilder(this.compiler, sourcePath, "-o", outputFilePath);
        Controller.log.info("Ejecutando: " + pb.command());
        return pb;
    }

    protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath) {
        ProcessBuilder pb = new ProcessBuilder(execPath, jsonPath);
        Controller.log.info("Ejecutando: " + pb.command());
        return pb;
    }

    protected boolean needCompilation() {
        return true;
    }

    protected boolean isLanguageName(String langName) {
        return langName.toLowerCase().contains("c++");
    }

    protected List<String> getConfigNames(String langName) {
        ArrayList<String> l = new ArrayList<String>();
        l.add(langName);
        return l;
    }

}
