package es.ucm.innova.docentia.TutorialesInteractivos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject con el que se trabaja
 * 
 * @author Carlos, Rafa
 *
 */
public class Subject {
	private int number; //Numero del tema
	private String correctorFile; //Nombre del archivo de correccion
	private String tittle; //Titulo del tema
	private String introduction; //Introduccion del Subject
	private List <Lesson> lessons; //Lista de las lecciones que componen el tema
	
	public Subject(int number, String tittle, String introduction, String file) {
		this.number = number;
		this.tittle = tittle;
		this.correctorFile = file;
		this.introduction = introduction;
		this.lessons = new ArrayList<Lesson>();
	}

	public String getCorrectorFile() {
		return this.correctorFile;
	}

	public void setCorrectorFile(String file) {
		this.correctorFile = file;
	}

	public int getNumber() {
		return number;
		
	}


	public String getTittle() {
		return this.tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Devuelve un array con solo el nombre de las lecciones
	 * @return ArrayList
	 */
	public ArrayList<String> getNameLessons() {
		//devuelve arraylist de nombres de lecciones
		ArrayList<String> names= new ArrayList<String>();
		for (Lesson l: this.lessons){
			names.add(l.getTittle());
		}
		return names;
	}

	public ArrayList<Element> getLeccion(String selectedItem) {
		
		int i = 0;
		while (!this.lessons.get(i).getTittle().equals(selectedItem))
			i++;
		return (ArrayList<Element>) this.lessons.get(i).getElements();
	}


	
	
	
}
