package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;

/**
 * Question de tipo Code
 * 
 * @author Carlos, Rafa
 *
 */
public class CodeQuestion extends Question<String> {

	private Correction c;// Corrector de las preguntas de tipo codigo

	public CodeQuestion(int number, String wording, String clue, String solution) {
		super(number, wording, clue, solution);
		
	}


	@Override
	public boolean check(String answer, Subject subject) {
		File correccion = new File(Controller.externalResourcesPath+"/"+subject.getCorrectorFile());
		String cor = correccion.getAbsolutePath();
		JSONParser jsonParser = new JSONParser();
		answer = answer.replace("\"", "'");
		this.c = new Correction("", null);
		boolean result = false;

		try {
			File temp = File.createTempFile("json_Data", null);
			String nombre = temp.getName();
			ProcessBuilder pb = new ProcessBuilder(Controller.executable, cor, this.solution, answer, nombre);
			Process p = pb.start();
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			//String line;

			//while ((line = br.readLine()) != null) {
				// esto es para la salida de texto de python... en principio
				// no hace falta
				//System.out.println(line);
			//}
			boolean errCode = p.waitFor(2, TimeUnit.SECONDS);
			if (!errCode) { // si ocurre esto es que el python está mal escrito,
							// o bucle infinito
				//System.out.println("yo no he sido, ha sido ->python<-, o bien bucle infinito");
				// TODO añadir aviso visual
			} else {

				int exit = p.exitValue();
				// SOLO EN CASO
				// DE ERROR DE LA FUNCION CORRECTORA
				// DEVOLVER UN VALOR DISTINTO DE 0,
				// en cualquier otro caso devolver 0, en
				// caso de error de la función correctora se
				// mostrará una excepción de java
				// en caso de 0, devolver json o similar,
				// diciendo el error y demás
				switch (exit) {

				case 1: {// si devuelve 1 no es nuestro problema
					c.setMessage("No compila");
					break;
				}

				case 0: {
					// comprobar si están vacios
					FileReader fileReader = new FileReader(temp);
					JSONObject json = (JSONObject) jsonParser.parse(fileReader);
					Boolean correct = (Boolean) json.get("isCorrect");
					if (correct != null) {
						if (correct) {
							result = true;
							// solo si ocurre esto devuelve
							// true, en cualquier otro caso
							// tiene que mirar qué pasa.
							// si se pueden meter mas cosas en la
							// corrección meterlas
						} else {

							String error = (String) json.get("typeError");
							if (error != null) {
								c.setMessage(error);
							}
							@SuppressWarnings("unchecked")
							List<String> hints = (List<String>) json.get("Hints");
							if (hints != null) {
								c.setHints(hints);
							}

						}
						fileReader.close();
						break;
					}
				}

				}

			}
			temp.delete();
		} catch (IOException | InterruptedException | ParseException  e) {
			e.printStackTrace();
		
		}
		return result;
	}

	@Override
	public void setSolution(String solution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMulti(Boolean is) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void setText(String explication) {
		// TODO Auto-generated method stub

	}

	public Correction getCorrection() {
		return c;
	}



	@Override
	public void setOptions(ArrayList<String> options) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setSolution(ArrayList<Integer> correctsAux) {
		// TODO Auto-generated method stub
		
	}


	

}
