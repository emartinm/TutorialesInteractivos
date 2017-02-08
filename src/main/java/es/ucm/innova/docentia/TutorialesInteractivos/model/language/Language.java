package es.ucm.innova.docentia.TutorialesInteractivos.model.language;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.*;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.JSONReaderClass;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
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
    /*
    Para añadir un nuevo lenguaje hay que:
    1. Crear una nueva clase que extienda de Language
    2. Implementar en ella los métodos:
        * public boolean isConfigured()
        * protected String getSourceExtension()
        * protected String getCompiledExtension()
        * protected ProcessBuilder getCompilationProcess(String sourcePath, String outputFilePath)
        * protected ProcessBuilder getExecutionProcess(String execPath, String jsonPath)
        * protected boolean needCompilation()
        * protected boolean isLanguageName(String langName)
        * protected List<String> getConfigNames(String langName)
        * [OPCIONAL] protected long getExecutionMillis()
    3. Extender los dos métodos estáticos de esta clase:
       * public static List<String> configuration(String language)
       * public static Language languageFactory(String language, String path, ConfigurationData config)
    */


    /*
    Objetos estáticos para las funciones configuration() y languageFactory()
     */
    private static PythonLanguage python = new PythonLanguage();
    private static CppLanguage cpp = new CppLanguage();
    private static JavaLanguage java = new JavaLanguage();
    private static CSharpLanguage csharp = new CSharpLanguage();
    // TODO añadir nuevos lenguajes


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
    protected long getExecutionMillis(){
        return 2000;
    }


	/*
	Devuelve una lista de entradas que hay que configurar para poder usar 'language' para
	corregir ejercicios
	 */
	public static List<String> configuration(String language) {
        List<String> l;
	    if (python.isLanguageName(language) ) {
	        l = python.getConfigNames(language);
        } else if (cpp.isLanguageName(language) ) {
	        l =  cpp.getConfigNames(language);
        } else if (java.isLanguageName(language)) {
            l = java.getConfigNames(language);
        } else if (csharp.isLanguageName(language)) {
	        l = csharp.getConfigNames(language);
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
        Language l;
        if (python.isLanguageName(language) ) {
            l =  new PythonLanguage(language, path, config);
        } else if (cpp.isLanguageName(language)) {
            l = new CppLanguage(language, path, config);
        } else if (java.isLanguageName(language)) {
            l = new JavaLanguage(language, path, config);
        } else if (csharp.isLanguageName(language)) {
            l = new CSharpLanguage(language, path, config);
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

	protected List<String> reemplazaLinea( String line, int ini, int fin, String codigo ) {
		ArrayList<String> lines = new ArrayList<String>();
		String inicio = line.substring(0, ini);
        String strFinal = line.substring(fin, line.length());
		// TODO revisar que el line.separator funciona también en Windows
		String[] cod_lines = codigo.split(System.getProperty("line.separator"));
		// Inserta todas las líneas de 'codigo' poniendo antes el mismo espaciado que tenía el hueco
		for (String l : cod_lines ) {
			lines.add( inicio + l);
		}
		int last = lines.size() - 1;
        String lastLine = lines.get(last);
        lines.set(last, lastLine + strFinal);
		return lines;
	}

	/*
	* Reemplaza cada aparición de 'marker' en file_in con el código, y lo almacena en file_out */
	protected void reemplazaHueco( File file_in, File file_out, String codigo ) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(file_in.toURI()));
			ArrayList<String> resultado = new ArrayList<String>();
			for (String line : lines ) {
				int pos = line.indexOf(marker);
				if (pos >= 0) {
                    int fin = pos + marker.length();
					resultado.addAll( reemplazaLinea(line, pos, fin, codigo) );
				} else {
					resultado.add( line );
				}
			}
			Files.write(Paths.get(file_out.toURI()), resultado);
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
	protected Correction compile(File corrector, File outputFile) {
        Correction c = null;
        try {
            ProcessBuilder pb = getCompilationProcess(corrector.getAbsolutePath(), outputFile.getAbsolutePath());
            Process p = pb.start();
            p.waitFor(); // Confiamos en los compiladores, siempre terminan
            int exit = p.exitValue();
            // SOLO EN CASO DE ERROR DE LA FUNCION CORRECTORA DEVOLVERA UN VALOR DISTINTO DE 0,
            if (exit == 0){
                c = new Correction(ExecutionMessage.OK, "", null, true);
            } else {
                BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                BufferedReader brOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                List<String> salida = fileToListString(brError);
                salida.addAll(fileToListString(brOutput));
                c = new Correction(ExecutionMessage.COMPILATION_ERROR, "Error en compilación", salida, false);
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
            File correctorFile = new File(correctorPath);
            reemplazaHueco(correctorFile, sourceFile, code);

            if (needCompilation()) {
                c = compile(sourceFile, binaryFile);
                if (c.getResult() != ExecutionMessage.OK ) {
                    return c;
                }
                c = execute(binaryFilename, jsonFilename);
            } else {
                reemplazaHueco(correctorFile, sourceFile, code);
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

    protected Map<String, String> getVisualStudioEnvironment(String vcvars) {
        Map<String, String> env = new HashMap<>();

        try {
            File temp = File.createTempFile("template", null);
            File bat = File.createTempFile("vcvars", ".bat");
            InputStream batTemplate = getClass().getResourceAsStream("/getVCenvironment.bat");
            Files.copy(batTemplate, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.reemplazaHueco(temp, bat, vcvars);
            ProcessBuilder pb = new ProcessBuilder(bat.getAbsolutePath());
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ( (line = br.readLine() ) != null ) {
                if (line.startsWith("\"{{{PATH}}}->\"")) {
                    env.put( "PATH", line.substring(14));
                } else if (line.startsWith("\"{{{INCLUDE}}}->\"")) {
                    env.put( "INCLUDE", line.substring(17));
                } else if (line.startsWith("\"{{{LIB}}}->\"")) {
                    env.put( "LIB", line.substring(13));
                } else if (line.startsWith("\"{{{LIBPATH}}}->\"")) {
                    env.put( "LIBPATH", line.substring(17));
                }
            }
            br.close();
            temp.delete();
            bat.delete();
        } catch (Exception e) {
            Controller.log.warning("Imposible obtener el entorno de Visual Studio: " + e.getLocalizedMessage() );
        }
        return env;
    }

    /* Busca un determinado binario en las entradas del entorno (variable PATH) */
    protected String binaryFullPath(Map<String, String> env, String binary) {
        String path = env.getOrDefault("PATH", "");
        for ( String dir : path.split(File.pathSeparator)) {
            File f = new File(dir + FileSystems.getDefault().getSeparator() + binary);
            if (f.exists() ) {
                return f.getAbsolutePath();
            }
        }
        return "";
    }

    protected void mergeEnvs(Map<String, String> env, Map<String, String> newEntries) {
        for (String k : env.keySet() ) {
            if (newEntries.containsKey(k.toLowerCase())) {
                String old = env.get(k);
                String newE = old + newEntries.get(k.toLowerCase());
                env.put(k, newE);
            }
        }
    }
}