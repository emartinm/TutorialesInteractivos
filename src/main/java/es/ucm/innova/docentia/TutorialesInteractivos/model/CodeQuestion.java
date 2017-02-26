package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

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

	public boolean hasSnippet(){
		return true;
	}

	public String snippet() {
	    return "Esto es lo que se veria al mostrar el snippet\n" +
                "\t\tojete máximo\n" +
                "\tqué más querés?";
    }

}
