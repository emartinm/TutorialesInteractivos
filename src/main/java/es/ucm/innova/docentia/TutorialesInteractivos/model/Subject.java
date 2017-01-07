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
	private String title; //Titulo del tema
	private String introduction; //Introduccion del Subject
	private List <Lesson> lessons; //Lista de las lecciones que componen el tema
	
	public Subject(int number, String title, String introduction, String file) {
		this.number = number;
		this.title = title;
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


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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
			names.add(l.getTitle());
		}
		return names;
	}

	public ArrayList<Element> getLeccion(String selectedItem) {
		
		int i = 0;
		while (!this.lessons.get(i).getTitle().equals(selectedItem))
			i++;
		return (ArrayList<Element>) this.lessons.get(i).getElements();
	}


	
	
	
}
