package es.ucm.innova.docentia.TutorialesInteractivos.model;

import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;

import java.util.List;

/**
 * Question de tipo Code
 * 
 * @author Carlos, Rafa
 *
 */
public class CodeQuestion extends Question<String> {

	//private Correction c;// Corrector de las preguntas de tipo codigo


	public String toString(){
		return String.format("CodeQuestion(%d,%s,%s,%s)", this.number, this.text,
				this.solution, this.clue);
	}

	public CodeQuestion(int number, String wording, String clue, String solution) {
		super(number, wording, clue, solution);
		
	}


	/*
	@Override
	public Correction check(String answer, Subject subject) {
		// TODO arreglar la correccion usando Language
		//File correccion = new File(Controller.externalResourcesPath+"/"+subject.getCorrectorFile());
		File correccion = new File(Controller.externalResourcesPath+"/");
		String cor = correccion.getAbsolutePath();
		JSONParser jsonParser = new JSONParser();
		answer = answer.replace("\"", "'");
		Correction c = new Correction("", null, false);
		boolean result = false;

		try {
			File temp = File.createTempFile("json_Data", null);
			String nombre = temp.getName();
			ProcessBuilder pb = new ProcessBuilder(Controller.executable, cor, this.solution, answer, nombre);
			Controller.log.info("Ejecutando: " + Controller.executable + " " + cor + " " + this.solution + " " + answer + " " + nombre);
			Process p = pb.start();
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
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
                    //TODO usar jackson tambien aqui
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
		c.setCorrect(result);
		return c;
	}*/

    public Correction check(String answer, Subject s) { return null; }

    public Correction check(String answer, Language lang) {
        return lang.compileAndExecute(this.solution, answer);
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


	@Override
	public void setOptions(List<String> options) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setSolution(List<Integer> correctsAux) {
		// TODO Auto-generated method stub
		
	}


	protected void load_answer_from_string(String s) {
		this.lastAnswer = s;
	}

	protected String answer_to_string() {
		return this.lastAnswer;
	}


	

}
