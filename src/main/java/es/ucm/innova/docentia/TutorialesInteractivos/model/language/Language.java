package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.*;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.JSONReaderClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Clase base para los lenguajes soportados por la herramienta.
 * Contiene varias implementaciones por defecto que sirven para muchos lenguajes.
 * 
 * @author Carlos, Rafa, Enrique
 *
 */

public abstract class Language {
	private static final String marker = "<+|CODIGO|+>";
	protected String name;
	protected String path; // Ruta del lenguaje en cuestión

    public abstract boolean isConfigured();
    protected abstract ProcessBuilder getCompilationProcess(String sourcePath, String outputFilePath);
    protected abstract ProcessBuilder getExecutionProcess(String execPath, String jsonPath);
    protected abstract String getSourceExtension();
    protected abstract String getCompiledExtension();
    protected abstract boolean needCompilation();
    protected abstract boolean isLanguageName(String langName);
    protected abstract List<String> getConfigNames(String langName);

    /*
     Limite de tiempo por defecto para ejecutar un programa.
     Se puede sobreescribir en cada lenguaje.
     */
    private long getExecutionMillis(){
        return 2000;
    }


	/*
	Devuelve una lista de entradas que hay que configurar para poder usar 'language' para
	corregir ejercicios
	 */
	public static List<String> configuration(String language) {
	    PythonLanguage python = new PythonLanguage();
	    CppLanguage cpp = new CppLanguage();
	    JavaLanguage java = new JavaLanguage();
        List<String> l;

	    if (python.isLanguageName(language) ) {
	        l = python.getConfigNames(language);
        } else if (cpp.isLanguageName(language) ) {
	        l =  cpp.getConfigNames(language);
        } else if (java.isLanguageName(language)){
	        l = java.getConfigNames(language);
        } else {
	        Controller.log.warning("Lenguaje desconocido: " + language);
	        l = null;
        }
        return l;
    }

    /*
    Genera un tipo especializado de objeto Language dependiendo de la cadena
     */
    public static Language languageFactory(String language, String path, ConfigurationData config) {
        PythonLanguage python = new PythonLanguage();
        CppLanguage cpp = new CppLanguage();
        JavaLanguage java = new JavaLanguage();
        Language l = null;

        if (python.isLanguageName(language) ) {
            l =  new PythonLanguage(language, path, config);
        } else if (cpp.isLanguageName(language)) {
            l = new CppLanguage(language, path, config);
        } else if (java.isLanguageName(language)) {
            l = new JavaLanguage(language, path, config);
        } else {
            Controller.log.warning("Lenguaje desconocido: " + language);
            l = null;
        }
        // TODO: añadir aqui los demás lenguajes soportados
        return l;
    }



	public String getLanguage() {
		return name;
	}


	protected List<String> reemplazaLinea( String line, int pos, String codigo ) {
		ArrayList<String> lines = new ArrayList<String>();
		String inicio = line.substring(0, pos);
		// TODO revisar que el line.separator funciona también en Windows
		String[] cod_lines = codigo.split(System.getProperty("line.separator"));
		// Inserta todas las líneas de 'codigo' poniendo antes el mismo espaciado que tenía el hueco
		for (String l : cod_lines ) {
			lines.add( inicio + l);
		}
		return lines;
	}

	protected void reemplazaHueco( String file_in, String file_out, String codigo ) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(file_in));
			ArrayList<String> resultado = new ArrayList<String>();
			for (String line : lines ) {
				int pos = line.indexOf(marker);
				if (pos >= 0) {
					resultado.addAll( reemplazaLinea(line, pos, codigo) );
				} else {
					resultado.add( line );
				}
			}
			Files.write(Paths.get(file_out), resultado);
		} catch (java.io.IOException e) {
			Controller.log.warning("Error al reemplazar hueco: " + e.getMessage());
		}
	}

    protected List<String> fileToListString(BufferedReader br) {
	    ArrayList<String> sa = new ArrayList<String>();
	    String line;
	    try {
	        while ( (line = br.readLine()) != null ) {
	            sa.add(line);
            }
        } catch (java.io.IOException e) {
            Controller.log.info("Imposible leer el fichero linea a linea");
        }
        return sa;
    }

    /*
    Lee el JSON del archivo y genera una corrección exitosa
     */
    protected Correction readJSONresults(File jsonFile) {
    	Correction c = null;
		Map<String, Object> json = JSONReaderClass.loadJSON(jsonFile.getAbsolutePath());
		if (json.size() > 0 ) {
			Boolean correct = (Boolean) json.getOrDefault("isCorrect", false);
			String error = (String) json.getOrDefault("typeError", "");
			List<String> hints = (List<String>) json.getOrDefault("Hints", null);
			c = new Correction(ExecutionMessage.OK, error, hints, correct);
		} else {
			c = new Correction(ExecutionMessage.OK, "Imposible obtener resultados", null, false);
		}
		return c;
	}



    /*
    Implementación por defecto de la fase de compilación. Depende del método abstracto getCompilationProcess()
     */
	protected Correction compile(String correctorPath, String outputFilePath) {
        Correction c = null;
        try {
            ProcessBuilder pb = getCompilationProcess(correctorPath, outputFilePath);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            p.waitFor(); // Confiamos en los compiladores, siempre terminan
            int exit = p.exitValue();
            // SOLO EN CASO DE ERROR DE LA FUNCION CORRECTORA DEVOLVERA UN VALOR DISTINTO DE 0,
            switch (exit) {
                case 1: {
                    c = new Correction(ExecutionMessage.COMPILATION_ERROR, "Error en compilación", fileToListString(br), false);
                    break;
                }
                case 0: { // comprobar si están vacios
                    c = new Correction(ExecutionMessage.OK, "", null, true);
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return c;
    }


    /*
    Implementación por defecto de la fase de compilación. Depende del método abstracto getExecuteProcess()
     */
    protected Correction execute(String execPath, String jsonPath) {
        Correction c = null;
        try {
            ProcessBuilder pb = getExecutionProcess(execPath, jsonPath);
            //Controller.log.info("Ejecutando: " + this.interpreter + " " + sourceFilename + " " + jsonFilename);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            boolean errCode = p.waitFor(getExecutionMillis(), TimeUnit.MILLISECONDS);
            if (!errCode) { // si ocurre esto es que el python está mal escrito, o bucle infinito
                Controller.log.info("Ejecución abortada tras " + getExecutionMillis() + " milisegundos");
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
                        File f = new File(jsonPath);
                        c = readJSONresults(f);
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            //if ( jsonFile != null ) { jsonFile.delete(); }
            //if ( sourceFile != null ) { sourceFile.delete(); }
        }
        return c;
    }

    /*
    Definición por defecto de compileAndExecute
     */
    public Correction compileAndExecute(String correctorRelativePath, String code) {
        File jsonFile = null;
        File sourceFile = null;
        File binaryFile = null;
        Correction c = null;
        try {

            jsonFile = File.createTempFile("result", ".json");
            String jsonFilename = jsonFile.getAbsolutePath();

            sourceFile = File.createTempFile("source", getSourceExtension());
            String sourceFilename = sourceFile.getAbsolutePath();

            binaryFile = File.createTempFile("program", getCompiledExtension());
            String binaryFilename = binaryFile.getAbsolutePath();

            String correctorPath = this.path + FileSystems.getDefault().getSeparator() + correctorRelativePath;
            reemplazaHueco(correctorPath, sourceFilename, code);

            if (needCompilation()) {
                c = compile(sourceFilename, binaryFilename);
                if (c.getResult() != ExecutionMessage.OK ) {
                    return c;
                }
                c = execute(binaryFilename, jsonFilename);
            } else {
                reemplazaHueco(correctorPath, sourceFilename, code);
                c = execute(sourceFilename, jsonFilename);
            }
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