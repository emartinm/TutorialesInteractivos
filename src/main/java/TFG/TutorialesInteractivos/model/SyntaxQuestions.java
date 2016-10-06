package TFG.TutorialesInteractivos.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import TFG.TutorialesInteractivos.controller.Controller;

/**
 * Question de tipo Sintax
 * 
 * @author Carlos, Rafa
 *
 */
public class SyntaxQuestions extends Question<String> {
	private String sintax;

	public SyntaxQuestions(int number, String wording, String clue, String sintax, String solution) {
		super(number, wording, clue, solution);
		this.sintax = sintax;
	}

	@Override
	public void setOptions(ArrayList<String> options) {

	}

	@Override
	public boolean check(String answer, Subject subject) {
		URL classUrl = null;
		try {
			// falta el nombre de la carpeta en concreto, que irá tambien en los
			// de abajo y en el getmethod lowercase
			String s = "file:" + Controller.externalResourcesPath + "/" + Controller.selectedLanguage + "/gramaticas/"
					+ sintax + "/";
			classUrl = new URL(s);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		URL[] classUrls = { classUrl };
		URLClassLoader ucl = new URLClassLoader(classUrls);

		try {

			Class<? extends Lexer> c = (Class<? extends Lexer>) Class.forName(sintax + "Lexer", true, ucl);
			Constructor<? extends Lexer> constructor = c.getConstructor(CharStream.class);
			
			Lexer l = constructor.newInstance(new ANTLRInputStream(answer));

			Class<? extends Parser> cParser = (Class<? extends Parser>) Class.forName(sintax + "Parser", true, ucl);
			Constructor<? extends Parser> constructorParser = cParser.getConstructor(TokenStream.class);
			Parser p = constructorParser.newInstance(new CommonTokenStream(l));

			// Aqui empieza lo bueno
			Class<? extends ParserRuleContext> inner = (Class<? extends ParserRuleContext>) Class
					.forName(sintax + "Parser$" + sintax + "Context", true, ucl);
			Constructor<? extends ParserRuleContext> constInn = inner.getConstructor(ParserRuleContext.class,
					int.class);
			ParserRuleContext prc = constInn.newInstance(new ParserRuleContext(),0);

			prc = (ParserRuleContext) cParser.getMethod(sintax.toLowerCase()).invoke(p);
			
			System.out.println(prc.exception);
			
			/*
			 * Con este bucle se mira los hijos producidos por la funcion que compruba la sintaxis
			 * Se mira si el texto tiene la palabra missing que implica que falla algo
			 */
			for(ParseTree s : prc.children){
				if (s.getText().contains("missing"))
				{
					return false;
				}
			}
			
			if (prc.exception == null) {
				return true;
			}

			else {
				return false;
			}

		} catch (Exception e1) {
			
			e1.printStackTrace();
			
		}
		return false;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	@Override
	public void setMulti(Boolean is) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void setText(String explicacion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSolution(ArrayList<Integer> correctsAux) {
		// TODO Auto-generated method stub
		
	}

}
