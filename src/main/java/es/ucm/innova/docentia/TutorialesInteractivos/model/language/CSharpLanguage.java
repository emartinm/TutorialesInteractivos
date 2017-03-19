/* Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */

package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kike on 6/02/17.
 */
public class CSharpLanguage extends Language {
    private String compiler = null;
    private static String visualStudioCSC = null;
    private static Map<String, String> env = null;

    protected CSharpLanguage() {}

    public CSharpLanguage(String language, String path, ConfigurationData config) {
        this.name = language;
        this.path = path;
        this.compiler = config.get(compilerName(language));
    }

    private String compilerName(String language) {
        return language; // + " (compilador de C#)";
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
        ProcessBuilder pb = null;
        File f = new File(outputFilePath);
        if ( this.compiler.toLowerCase().contains("vcvars") ) {
            // Visual Studio:
            // Lanza un BAT con vcvars que extrae los nuevos valores de PATH, INCLUDE, LIB y LIBPATH
            // Usando PATH busca el primer directorio que contenga csc.exe y lo almacena en visualStudioCL, que
            // será la ruta completa del compilador
            if (env == null ) {
                env  = getVisualStudioEnvironment(this.compiler);
                visualStudioCSC = binaryFullPath(env, "csc.exe");
            }
            pb = new ProcessBuilder(visualStudioCSC, "-out:" + outputFilePath, sourcePath);
            Map<String, String> currEnv = pb.environment();
            currEnv.putAll(env);
        } else {
            // Mono
            pb = new ProcessBuilder(this.compiler, sourcePath, "-out:" + outputFilePath);
        }
        Controller.log.info("Executing: " + pb.command());
        return pb;
        // No es necesario realizar distinción entre Visual Studio y Mono, toman los mismo parámetros
        /*ProcessBuilder pb = new ProcessBuilder(this.compiler, sourcePath, "-out:" + outputFilePath);
        Controller.log.info("Ejecutando: " + pb.command());
        return pb;*/
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
        return langName.toLowerCase().contains("c sharp");
    }

    protected List<String> getConfigNames(String langName) {
        ArrayList<String> l = new ArrayList<String>();
        l.add(compilerName(langName));
        return l;
    }
}
