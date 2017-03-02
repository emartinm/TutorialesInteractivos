package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by kike on 4/02/17.
 */
public class CppLanguage extends Language {
    private String compiler = null;
    private static String visualStudioCL = null;
    private static Map<String, String> env = null;

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
        if ( this.compiler.toLowerCase().contains("vcvars") ) {
            // Para Visual Studio
            // Lanza un BAT con vcvars que extrae los nuevos valores de PATH, INCLUDE, LIB y LIBPATH
            // Usando PATH busca el primer directorio que contenga cl.exe y lo almacena en visualStudioCL, que
            // será la ruta completa del compilador
            if (env == null ) {
                env  = getVisualStudioEnvironment(this.compiler);
                visualStudioCL = binaryFullPath(env, "cl.exe");
            }
            /*
            // Chapuza para compilar usando siempre un BAT //
            bat = File.createTempFile("compilation",".bat");
            BufferedWriter bw = new BufferedWriter( new FileWriter(bat, true));
            bw.write("CALL \"" + this.compiler + "\"");
            bw.newLine();
            bw.write("CALL cl.exe \"" + sourcePath + "\" /Fe" + outputFilePath + " /Fo" + f.getParent());
            bw.newLine();
            bw.close();
            pb = new ProcessBuilder("cmd.exe", "/C\"" + bat.getAbsolutePath() + "\"" );*/
            pb = new ProcessBuilder(visualStudioCL, sourcePath, "/Fe"+outputFilePath, "/Fo"+f.getParent());
            Map<String, String> currEnv = pb.environment();
            currEnv.putAll(env);
        } else {
            // g++
            pb = new ProcessBuilder(this.compiler, sourcePath, "-std=c++11", "-o", outputFilePath);
        }
        Controller.log.info("Executing: " + pb.command());
        return pb;
    }

    protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath) {
        ProcessBuilder pb = new ProcessBuilder(execPath, jsonPath);
        Controller.log.info("Executing: " + pb.command());
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
