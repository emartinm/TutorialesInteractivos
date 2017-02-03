package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        if (language.toLowerCase().contains("python") ) {
            return new PythonLanguage(language, path, config);
        } else  {
            // Insertar aqui los nuevos lenguajes soportados
            return null;
        }
    }

	/*public Language() {
		this.language = null;
		this.path = null;
	}

	public Language(String language, String path) {
		this.language = language;
		this.path = path;
	}*/

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
}