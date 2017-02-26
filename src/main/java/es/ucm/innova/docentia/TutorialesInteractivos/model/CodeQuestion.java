package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Question de tipo Code
 * 
 * @author Enrique, Carlos, Rafa
 *
 */
public class CodeQuestion extends Question<List<String>> {
    private static String snippetMark = "<+|SNIPPET|+>";

    private String corrector;
    private int numGaps = 1;
    private List<String> prompt;

	public String toString(){
		return String.format("CodeQuestion(%s,%s,%s,%s)", this.text,
				this.solution, this.corrector, this.clue, this.prompt);
	}

	public CodeQuestion(String wording, String clue, String corrector, int numGaps, List<String> prompt) {
	    super(wording, clue);
	    this.corrector = corrector;
	    this.numGaps = numGaps;
	    this.prompt = prompt;

	    if (this.prompt == null) {
			this.prompt = new ArrayList<>();
		}
		// Relleno los posibles prompts que falten
		for (int i = this.prompt.size(); i < this.numGaps; ++i) {
	    	this.prompt.add("Código del hueco #" + i);
		}

		// Incluye una respuesta actual vacía (numGaps cadenas vacías)
		this.answer = new ArrayList<>(this.numGaps);
		for (int i = 0; i < this.numGaps; ++i) {
			this.answer.add("");
		}
	}

	public String promptAt(int pos) {
	    return this.prompt.get(pos);
    }

	public Correction check(Language lang) {
        return lang.compileAndExecute(this.corrector, this.answer);
    }

	protected void load_answer_from_object(Object o) {
	    this.answer = (List<String>)o;
	}

	public int getNumberGaps() {
		return numGaps;
	}

	public boolean hasSnippet(Language lang){;
	    ArrayList<String> codes = new ArrayList<String>();
	    for( int i = 0;  i < this.numGaps; ++ i) {
	        codes.add("");
        }
        String codigo = lang.leeYreemplazaHuecos(this.corrector, codes);
	    int first = codigo.indexOf(this.snippetMark);
	    int second = codigo.indexOf(this.snippetMark, first + 1);
	    int third = codigo.indexOf(this.snippetMark, second+ 1);
	    return (first != -1 && second != -1 && third == -1);
	}

	public String snippet(Language lang) {
	    List<String> ans = this.answer;
	    if (ans == null) {
	        // Para rellenar el snippet con cadenas vacías
	        ans = new ArrayList<String>(this.numGaps);
	        for (int i = 0; i < this.numGaps; ++i) {
	            ans.add("");
            }
        }
        String codigo = lang.leeYreemplazaHuecos(this.corrector, ans);
        int first = codigo.indexOf(this.snippetMark);
        int second = codigo.indexOf(this.snippetMark, first + 1);

        int pos = first;
        int ini = -1;
        int fin = -1;
        while (pos < codigo.length() && ini == -1) {
            // Buscamos el siguiente salto de línea a la primera marca
            if (codigo.charAt(pos) == '\n') {
                ini = pos + 1;
            }
            pos++;
        }
        pos = second;
        while (pos > 0 && fin == -1) {
            // Buscamos el salto de linea anterior a la segunda marca
            if (codigo.charAt(pos) == '\n') {
                fin = pos; //La letra en 'fin' no se considera
            }
            pos--;
        }
        return codigo.substring(ini, fin);
	    /*return "Esto es lo que se veria al mostrar el snippet\n" +
                "\t\tojete máximo\n" +
                "\tqué más querés?";*/
    }

}
