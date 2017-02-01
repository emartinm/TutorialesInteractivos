package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Language que se está utilizando actualmente
 * 
 * @author Carlos, Rafa
 *
 */

public class Language {
	private static final String marker = "<+|CODIGO|+>";
	private final String language;
	private final String path;

	public Language() {
		this.language = null;
		this.path = null;
	}

	public Language(String language, String path) {
		this.language = language;
		this.path = path;
	}

	public String getLanguage() {
		return language;
	}

	public String getPath() {
		return path;
	}

	// TODO quitar static
	private static List<String> reemplazaLinea( String line, int pos, String codigo ) {
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

	// TODO quitar static, cambiar a private
	public static void reemplazaHueco( String file_in, String file_out, String codigo ) {
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
}