package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kike on 5/02/17.
 */
public class JavaLanguage extends  Language{
    private String compiler = null;
    private String interpreter = null;

    protected JavaLanguage() {}

    public JavaLanguage(String language, String path, ConfigurationData config) {
            this.name = language;
            this.path = path;
            this.compiler = config.get(compilerName(language));
            this.interpreter = config.get(interpreterName(language));
        }

    private String compilerName(String language) {
        return language + " (compilador 'javac')";
    }

    private String interpreterName(String language) {
        return language + " (intérprete 'java')";
    }

    public boolean isConfigured() {
        return (name != null) && (path != null) && (compiler != null) && (interpreter != null);
    }

    protected String getSourceExtension(){
        return ".java";
    }

    protected String getCompiledExtension(){
        return ".class";
    }

    protected ProcessBuilder getCompilationProcess(String sourcePath, String outputFilePath) {
        ProcessBuilder pb = new ProcessBuilder(this.compiler, sourcePath);
        File f = new File(sourcePath);
        Controller.log.info("Ejecutando: " + pb.command());
        return pb;
    }

    protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath) {
        File f = new File(execPath);
        ProcessBuilder pb = new ProcessBuilder(this.interpreter, "-cp", f.getParent(), "Corrector", jsonPath);
        Controller.log.info("Ejecutando: " + pb.command());
        return pb;
    }

    protected boolean needCompilation() {
        return true;
    }

    protected boolean isLanguageName(String langName) {
        return langName.toLowerCase().contains("java");
    }

    protected List<String> getConfigNames(String langName) {
        ArrayList<String> l = new ArrayList<String>();
        l.add(compilerName(langName));
        l.add(interpreterName(langName));
        return l;
    }

}
