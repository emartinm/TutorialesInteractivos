package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        ProcessBuilder pb = null;
        File f = new File(outputFilePath);
        File bat;
        if ( this.compiler.toLowerCase().contains("vcvars32") ) {
            // "Chapuza" para que funcione Visual Studio
            // El problema es que no consigue compilar porque le falta el entorno adecuado para INCLUDE, LIB, etc.
            // La posible solución es crear un script BAT que invoca primero al compiler (que es un script vcvars32.bat
            // que carga el entorno) y luego invocar directamente a cl.exe con las opciones adecuadas.
            // Es bastante chapuza pero no se me ocurre otra cosa...
            try{
                bat = File.createTempFile("compilation",".bat");
                BufferedWriter bw = new BufferedWriter( new FileWriter(bat, true));
                bw.write("CALL \"" + this.compiler + "\"");
                bw.newLine();
                bw.write("CALL cl.exe \"" + sourcePath + "\" /Fe" + outputFilePath + " /Fo" + f.getParent());
                bw.newLine();
                bw.close();
                pb = new ProcessBuilder("cmd.exe", "/C\"" + bat.getAbsolutePath() + "\"" );
                //this.compiler, sourcePath, "/Fe"+outputFilePath, "/Fo"+f.getParent());
            } catch (Exception e ) {
                Controller.log.warning("Imposible construir el BAT para compilar:" + e.getLocalizedMessage() );
            }
        } else {
            // g++
            pb = new ProcessBuilder(this.compiler, sourcePath, "-std=c++11", "-o", outputFilePath);
        }
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
