package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kike on 6/02/17.
 */
public class CSharpLanguage extends Language {
    private String compiler = null;

    protected CSharpLanguage() {}

    public CSharpLanguage(String language, String path, ConfigurationData config) {
        this.name = language;
        this.path = path;
        this.compiler = config.get(compilerName(language));
    }

    private String compilerName(String language) {
        return language + " (compilador de C#)";
    }

    public boolean isConfigured() {
        return (name != null) && (path != null) && (compiler != null);
    }

    protected String getSourceExtension(){
        return ".cs";
    }

    protected String getCompiledExtension(){
        return ".exe";
    }

    protected ProcessBuilder getCompilationProcess(String sourcePath, String outputFilePath) {
        // No es necesario realizar distinción entre Visual Studio y Mono, toman los mismo parámetros
        ProcessBuilder pb = new ProcessBuilder(this.compiler, sourcePath, "-out:" + outputFilePath);
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
        return langName.toLowerCase().contains("c sharp");
    }

    protected List<String> getConfigNames(String langName) {
        ArrayList<String> l = new ArrayList<String>();
        l.add(compilerName(langName));
        return l;
    }
}
