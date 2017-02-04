package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.JSONReaderClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Language que se está utilizando actualmente
 * 
 * @author Carlos, Rafa, Enrique
 *
 */

public abstract class Language {
	private static final String marker = "<+|CODIGO|+>";
	protected String name;
	protected String path; // Ruta del lenguaje en cuestión

	/*
	Devuelve una lista de entradas que hay que configurar para poder usar 'language' para
	corregir ejercicios
	 */
	public static List<String> configuration(String language) {
	    ArrayList<String> l = new ArrayList<>();
	    if (language.toLowerCase().contains("erlang") ) {
	        // Erlang se ejecutará con escript
	        l.add( language + " (escript)");
        } else if (language.toLowerCase().contains("java") ) {
	        // Java tiene fase de compilación y de interpretación
	        l.add(language + " (javac)");
            l.add(language + " (java)");
        } else {
	        // Python, C, C++, C#
            l.add(language);
        }
        return l;
    }

    /*
    Genera un tipo especializado de objeto Language dependiendo de la cadena
     */
    public static Language languageFactory(String language, String path, ConfigurationData config) {
        Language l = null;
        if (language.toLowerCase().contains("python") ) {
            l =  new PythonLanguage(language, path, config);
        } else if (language.toLowerCase().contains("c++") ) {
            l = new CppLanguage(language, path, config);
        }
        // TODO: añadir aqui los demás lenguajes soportados
        return l;
    }

    public abstract boolean isConfigured();

	public String getLanguage() {
		return name;
	}

	public abstract Correction compileAndExecute(String correctorRelativePath, String code);

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
		//FileReader fileReader = new FileReader(jsonFile);
		Map<String, Object> json = JSONReaderClass.loadJSON(jsonFile.getAbsolutePath());
		//JSONObject json = (JSONObject) jsonParser.parse(fileReader);
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

	protected abstract ProcessBuilder getCompilationProcess(String correctorRelativePath, String code, String sourcePath, String outputFilePath);
    protected abstract ProcessBuilder getExecutionProcess(String execPath, String jsonPath);

    private long getExecutionMillis(){
        return 2000; // Por defecto, se debe sobreescribir en cada lenguaje
    }

    /*
    Implementación por defecto de la fase de compilación. Depende del método abstracto getCompilationProcess()
     */
	protected Correction compile(String correctorRelativePath, String code, String sourcePath, String outputFilePath) {
        Correction c = null;
        try {
            ProcessBuilder pb = getCompilationProcess(correctorRelativePath, code, sourcePath, outputFilePath);
            String correctorPath = this.path + FileSystems.getDefault().getSeparator() + correctorRelativePath;
            reemplazaHueco(correctorPath, sourcePath, code);
            //Controller.log.info("Ejecutando: " + this.interpreter + " " + sourceFilename + " " + jsonFilename);
            Process p = pb.start();
            System.out.println(pb.command());
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
                        //TODO usar jackson tambien aqui
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
}