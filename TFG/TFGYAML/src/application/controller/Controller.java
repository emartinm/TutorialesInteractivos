package application.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import application.CargaConfig;
import application.SelectedPath;
import application.model.Correction;
import application.model.Elemento;
import application.model.Explicacion;
import application.model.Pregunta;
import application.model.Tema;
import application.model.Utilities;
import application.model.YamlReaderClass;
import application.view.Contenido;
import application.view.MenuLeccion;
import application.view.MenuTema;
import application.view.Portada;
import application.view.SeleccionLenguajes;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase controlador. Ejecuta todas las variaciones de la aplicación
 * 
 * @author Carlos
 * @param <K>
 * @param <V>
 *
 */
public class Controller<K, V> {
	public static String path;// ejecutable de python
	private Tema tema;
	private Stage primaryStage;
	private Pane root;
	private VBox buttons;
	private Scene scene;
	private ArrayList<Elemento> elems;
	private int actual; // contador de el elemento del contenido en el que
						// estamos
	private ArrayList<String> files;// temas del lenguaje
	private Portada portada;
	private String len; // lenguaje seleccionado
	private Map<K, V> lenguajes; // Map con los lenguajes posibles
	private Correction c;

	
	public Controller(Stage primaryStage) {
		this.tema = null;
		this.primaryStage = primaryStage;
		this.files = new ArrayList<String>();
		this.lenguajes = YamlReaderClass.languages();
		this.c= new Correction();
		
	}

	/**
	 * Llama a la función del modelo encargada de cargar un tema
	 * 
	 * @param cargaTema
	 *            Nombre del fichero
	 */

	public Correction getCorrection() {
		return c;
	}

	/**
	 * Devuelve el tema que está abierto
	 * 
	 * @return tema
	 */
	public Tema getTema() {
		return tema;
	}

	/**
	 * Modifica el tema
	 * 
	 * @param tema
	 */
	public void setTema(Tema tema) {
		this.tema = tema;
	}

	/**
	 * Corrige las preguntas de tipo Opciones
	 * 
	 * @param resp
	 * @param p
	 * @return
	 */
	public boolean corrige(ArrayList<Integer> resp, Pregunta p) {
		
		return p.corrige(resp, tema);
	}

	public boolean corrige(String resp, Pregunta p) {
		return p.corrige(resp, tema);
	}

	/**
	 * Lanza la ejecucion de las ventanas
	 */
	public void showSubject() {

		Pane p = new MenuTema();
		File folder = new File("resources/yaml/" + len);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i].getName());
			}
		}
		changeView(p, files, 0, len);
	}

	/**
	 * Vuelve a mostrar el menú de temas
	 */
	public void refresh() {
		showStart();
	}

	public void start() {
		Pane p = new SeleccionLenguajes();

		//Map<K, V> l = YamlReaderClass.languages();
		ArrayList a = languageNames();
		changeView(p, a, 0, len);
	}

	public ArrayList<String> languageNames() {
		ArrayList<Map> p = (ArrayList<Map>) this.lenguajes.get("lenguajes");
		ArrayList<String> s = new ArrayList<String>();
		for (Map o : p)
			s.add((String) o.get("nombre"));

		return s;
	}

	/**
	 * Muestra la primera ventana de la aplicación
	 */
	public void showStart() {
		primaryStage.setTitle(this.len); // el titulo se podria poner de la app,
											// o del lenguaje, pero obteniendo
											// en la primera lectura de
											// ficheros...
		// este es el encargado de hacer el setroot que tiene los contenidos
		// necesarios
		Pane p = new MenuTema();

		changeView(p, files, 0, len);

	}

	/**
	 * Modifica la vista que se muestra en el momento
	 * 
	 * @param p
	 *            Panel a mostrar
	 * @param files
	 *            Lista de los ficheros que componen el temario
	 * @param selected
	 *            Lección seleccionada
	 * @param lenSelect 
	 */
	private void changeView(Pane p, ArrayList<String> files, int selected, String lenSelect) {
		scene = new Scene(new Group());
		//root = new GridPane();
		
		if (p instanceof Portada) {
			root.getChildren().addAll(((Portada) p).portada(this, lenSelect));
		} else if (p instanceof SeleccionLenguajes) {
			//root.getChildren().addAll(((SeleccionLenguajes) p).SeleccionLenguajes(files, this));
			root = ((SeleccionLenguajes) p).SeleccionLenguajes(files, this);
		} else if (p instanceof MenuTema) {
			root=((MenuTema) p).menuTema(files, this);
		} else if (p instanceof MenuLeccion) {
			root=((MenuLeccion) p).menuLeccion(tema, this);
		} else if (p instanceof Contenido) {
			Elemento e;
			// TODO añadir preguntas de ambos tipos aquí
			if (actual == -1) {
				e = new Explicacion(tema.getLecciones().get(selected).getExplicacion());

			} else if (actual == this.elems.size())
			{
				e = new Explicacion("FIN DE LA LECCION");
			}
			else
				e = elems.get(actual);

			root=((Contenido) p).contenido(e, this, selected);
		}
		
		scene.setRoot(root);
		
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}

	/**
	 * Caraga el tema seleccionado y carga el menu de seleccion de temas
	 * 
	 * @param selectedItem
	 */
	public void selectedTema(String selectedItem) {

		this.tema = YamlReaderClass.cargaTema(len, selectedItem);
		changeView(new MenuLeccion(), null, 0, len);
	}

	/**
	 * Carga los componenetes del tema, y muestra la ventana con la primera
	 * explicación
	 * 
	 * @param selectedItem
	 */
	public void selectedLeccion(int selectedItem) {
		this.elems = (ArrayList<Elemento>) tema.getLecciones().get(selectedItem).getElementos();
		actual = -1;
		changeView(new Contenido(), null, selectedItem, len);
	}

	/**
	 * Carga el siguiente elemento(pregunta,explicacion) del Tema
	 * 
	 * @param l
	 *            Leccion seleccionada
	 */
	public void nextElem(int l) {
		// TODO ojo, hay que desactivar y activar botones para que esto no pete
		if (actual < this.elems.size())
			actual++;
		changeView(new Contenido(), null, l, len);
	}

	/**
	 * Carga el anterior elemento(pregunta,explicacion) del Tema
	 * 
	 * @param l
	 *            Leccion seleccionada
	 */
	public void prevElem(int l) {
		if (actual > -1)
			actual--;
		changeView(new Contenido(), null, l, len);
	}

	/**
	 * Parseaa el texto en markdown y lo transforma en texto HTML
	 * 
	 * @param mark
	 * @return texto en formato HTML
	 */
	public String markToHtml(String mark) {
		return Utilities.parserMarkDown(mark);
	}

	

	/**
	 * Muesta el FileChooser para seleccionar donde se encuentra python en el
	 * equipo
	 */
	public void muestraSeleccion() {
		SelectedPath sp = new SelectedPath(this.primaryStage, this.len);
		this.path = sp.getPath();
		CargaConfig.saveConfig(this.path);

	}

	/**
	 * Modifica el path del ejecutable del lenguaje
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
		modifyMap();
		YamlReaderClass.saveConfig((Map<String, Object>) this.lenguajes);
	}

	public void selectedLanguage(String selectedItem) {
		this.len = selectedItem;
		setPath(pathSelected());
		showSubject();
	}
	
	/**
	 * Modifica el path del lenguaje seleccionado
	 */
	private void modifyMap() {
		ArrayList<Map> p = (ArrayList<Map>) this.lenguajes.get("lenguajes");
		String ret = null;
	
		for (Map o : p)
		{
			if (o.get("nombre").equals(this.len))
			{
				o.put("ruta", this.path);
			}
		}
		
	}

	public String pathSelected() {
		ArrayList<Map> p = (ArrayList<Map>) this.lenguajes.get("lenguajes");
		String ret = null;
	
		for (Map o : p)
		{
			if (o.get("nombre").equals(this.len))
			{
				ret = (String)o.get("ruta");
				break;
			}
		}
		return ret;
	}
	
	public Stage getPrimaryStage()
	{
		return this.primaryStage;
	}
	
	public void showPortada(String lenguaje){
		//Map<K, V> l = YamlReaderClass.languages();
		this.len = lenguaje;
		setPath(this.pathSelected());
		this.changeView(new Portada(), null, 0, this.path);
	}

	public Scene getScene() {
		
		return this.scene;
	}

	public int getActual() {
		
		return this.actual;
	}

	public ArrayList<Elemento> getElems() {
		return this.elems;
	}

}
