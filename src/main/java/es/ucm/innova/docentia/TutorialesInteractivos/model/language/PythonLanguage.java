/* Copyright 2016 Rafael Caturla, Carlos Congosto
 * Copyright 2016-2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */
package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ConfigurationData;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Correction;
import es.ucm.innova.docentia.TutorialesInteractivos.model.ExecutionMessage;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kike on 3/02/17.
 */
public class PythonLanguage extends Language {
    private String interpreter;

    protected PythonLanguage(){}

    public PythonLanguage(String language, String path, ConfigurationData config) {
        this.name = language;
        this.path = path;
        this.interpreter = config.get(language);
    }

    public boolean isConfigured() {
        return (name != null) && (path != null) && (interpreter != null);
    }

    protected String getSourceExtension(){
        return ".py";
    }

    protected String getCompiledExtension(){
        return ".pyc";
    }

    protected ProcessBuilder getCompilationProcess(String sourcePath, String outputFilePath) {
        return null; // Python no necesita compilar
    }

    protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath) {
        ProcessBuilder pb = new ProcessBuilder(this.interpreter, execPath, jsonPath);
        // Establecemos el PYTHONPATH a la carpeta raíz del tema
        Map<String,String> env = pb.environment();
        env.put("PYTHONPATH", this.path);
        // Ejecutamos
        Controller.log.info( "Executing: " + pb.command());
        return pb;
    }

    protected boolean needCompilation() {
        return false;
    }

    protected boolean isLanguageName(String langName) {
        return langName.toLowerCase().contains("python");
    }

    protected List<String> getConfigNames(String langName) {
        ArrayList<String> l = new ArrayList<String>();
        l.add(langName);
        return l;
    }
}
