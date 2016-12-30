package es.ucm.innova.docentia.TutorialesInteractivos.controller;


import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import es.ucm.innova.docentia.TutorialesInteractivos.model.Correction;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Element;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Explanation;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Language;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Question;
import es.ucm.innova.docentia.TutorialesInteractivos.model.Subject;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.YamlReaderClass;
import es.ucm.innova.docentia.TutorialesInteractivos.view.Configuration;
import es.ucm.innova.docentia.TutorialesInteractivos.view.Content;
import es.ucm.innova.docentia.TutorialesInteractivos.view.EndLessonPane;
import es.ucm.innova.docentia.TutorialesInteractivos.view.InitialWindow;
import es.ucm.innova.docentia.TutorialesInteractivos.view.LessonsMenu;
import es.ucm.innova.docentia.TutorialesInteractivos.view.PathChooser;
import es.ucm.innova.docentia.TutorialesInteractivos.view.SubjectsMenu;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;


/**
 * Clase controlador. Ejecuta todas las variaciones de la aplicación
 * 
 * @author Carlos, Rafa, Enrique Martín
 *
 */
public class Controller {
    private static Logger log = Logger.getLogger("TutorialesInteractivos");

    public static String executable;// ejecutable del lenguaje para ejecutar código
	private Subject subject; // Subject que se está ejecutando
	private Stage primaryStage;// Vista principal de la aplicación
	private Pane root;// Panel con los elementos de la vista
	private Scene scene;
	private ArrayList<Element> elems; // Lista de elementos de un subject
	private int actualStep; // contador de el elemento del contenido en el que estamos
	private int enabledSteps; // Elementos habilitados
	private boolean[] visited; // Array con los elementos de una lección que se han visitado
	private int actualLesson; // Lección en la que se encuentra el tutorial
	private List<String> files;// temas del lenguaje
	public static String selectedLanguage; // lenguaje seleccionado
	private Correction c;
	private Preferences pref;
	public static String externalResourcesPath;
	private URLClassLoader ucl;
	private Language language;
	
	/**
	 * Constructora 
	 * @param primaryStage
	 */
	public Controller(Stage primaryStage) {
		this.subject = null;
		this.primaryStage = primaryStage;
		this.files = new ArrayList<String>();
		this.c = new Correction();
		this.pref = Preferences.userNodeForPackage(this.getClass());
	}

	/**
	 * Devuelve la clase correctora
	 * @return clase correctora
	 */
	public Correction getCorrection() {
		return c;
	}

	/**
	 * Devuelve el subject que está abierto
	 * 
	 * @return subject
	 */
	public Subject getSubject() {
		return subject;
	}

	/**
	 * Modifica el subject
	 * 
	 * @param subject
	 */
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	/**
	 * Corrige las preguntas de tipo Options
	 * 
	 * @param resp
	 * @param p
	 * @return
	 */
	public boolean check(ArrayList<Integer> resp, Question p) {
		return p.check(resp, subject);
	}

	/**
	 * Función correctora de las preguntas de tipo Code y sintaxis
	 * 
	 * @param resp
	 * @param p
	 * @return
	 */
	public boolean check(String resp, Question p) {
		return p.check(resp, subject);
	}

	/**
	 * Llama a changeview pasandole la lista de temas disponibles para un
	 * lenguaje
	 */
	public void showSubject() {

		Pane p = new SubjectsMenu();
		files.clear(); //Esto es por si se vuelve al principio que no se dupliquen los temas
		try {
			DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {

				@Override
				public boolean accept(Path entry) throws IOException {
					String fileName = entry.getFileName().toString();
					return fileName != null && fileName.endsWith(".yml");
				}

			};
			final Path path = Paths.get(externalResourcesPath + "/" + selectedLanguage);
			final DirectoryStream<Path> dirStream = Files.newDirectoryStream(path, filter);
			for (Path file : dirStream) {
				// después de aplicar el filtro de extensión .yml, por si acaso
				// comprobamos si es un directorio
				if (!Files.isDirectory(file))
					files.add(file.getFileName().toString());
			}

		} catch (Exception e) {
			log.warning(e.toString());
		}

		changeView(p, files, 0, selectedLanguage, null);
	}

	/**
	 * Vuelve a mostrar el menú de temas
	 */
	public void goSubjectsMenu() {
		showStart();
	}

	/**
	 * Lanza la aplicacion
	 */
	public void start() {
		// se pueden poner de usuario en vez de de sistema, pero en usuario
		// serán para el usuario concreto, de sistema funciona para todo
		Pane p = new Pane();
		List<String> languagesList = new ArrayList<String>();

		String pathResources = pref.get("ExternalResources", null);
        log.info(pathResources);

		if (pathResources != null) {
			externalResourcesPath = pathResources;
			languagesList = languageNames();
			if (loadLanguagePaths(languagesList)) { // comprobamos si los compiladores están
				p = new InitialWindow();
			} else
				p = new Configuration(); // hace falta configurar compiladores

		} else {
			p = new Configuration(); // hace falta configurar directorio y
										// compiladores
		}
		Image icon = new Image(Controller.class.getResourceAsStream( "/icon/1477275626_monitor-sidebar.png" ));
        log.info(icon.toString());
		this.primaryStage.getIcons().add(icon);
		changeView(p, languagesList, 0, selectedLanguage, null);

	}

	/**
	 * Comprueba si todos los lengujes tienen su configuracion
	 * @param languagesList Lista de lenguajes disponibles
	 * @return true si y solo si los lenguajes que hay en el directorio externo
	 *         tienen su path configurado (esté bien o mal)
	 */
	private boolean loadLanguagePaths(List<String> languagesList) {
		boolean ret = true;
		for (String s : languagesList) {
			String check = pref.get(s, null);
			if (check == null)
				ret = false;
		}
		return ret;
	}

	/**
	 * Obtiene la lista de lenguajes disponibles
	 * 
	 * @return lista de lenguajes
	 */
	public List<String> languageNames() {
		// hay que sacarlos del directorio, es decir, ir a
		// externalresources/languages y mirar las carpetas que hay, esos son
		// los lenguajes disponibles
		return InternalUtilities.getDirectoryList(externalResourcesPath);

	}

	/**
	 * Muestra la primera ventana de la aplicacion
	 */
	public void showStart() {
		primaryStage.setTitle(selectedLanguage);
		Pane p = new SubjectsMenu();
		changeView(p, files, 0, selectedLanguage, null);
	}

	/**
	 * Modifica la vista que se muestra en el momento
	 * 
	 * @param p Panel a mostrar         
	 * @param files Lista de los ficheros que componen el temario           
	 * @param selected Lesson seleccionada
	 * @param lenSelect Language seleccionado
	 */
	private void changeView(Pane p, List<String> files, int selected, String lenSelect, Number newStep) {
		if (scene == null){
			scene = new Scene(new Group());
			// Arregla el problema del WebView que no muestra texto en Windows JDK 1.8.0u102
		}


		if (p instanceof Configuration) {
			root = ((Configuration) p).configuration(this);
		} else if (p instanceof InitialWindow) {
			root = ((InitialWindow) p).initialWin(files, this);
		} else if (p instanceof SubjectsMenu) {
			root = ((SubjectsMenu) p).subjectsMenu(files, lenSelect, this);
		} else if (p instanceof LessonsMenu) {
			root = ((LessonsMenu) p).lessonMenu(subject, this);
		} else if (p instanceof Content) {
			Element e;	
			if (actualStep == -1) {
				e = new Explanation(subject.getLessons().get(selected).getExplication());
			}  else if (actualStep == elems.size()) {
                String mensajeFinal = "# ¡Enhorabuena! \n## Has terminado la lección '" +
                        subject.getLessons().get(selected).getTittle() + "'";
                e = new Explanation(mensajeFinal);
            } else {
				e = elems.get(actualStep);
				stepChange(newStep, e instanceof Question);
			}
			// habilitados empezaremos con 1, y el paso actual es el 1 para la
			// vista (comienza en -1 aquí)
			// el que estás mas algo

			root = ((Content) p).content(e, this, elems.size() + 2, enabledSteps, actualStep + 2);
		}

		//root.setPrefSize(600, 600);
		scene.setRoot(root);

		//Comentado para corregir el error de tamaño minusculo al empezar en Linux y MacOSX
		//primaryStage.setWidth(scene.getWidth());
		//primaryStage.setHeight(scene.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Caraga el subject seleccionado y carga el menu de seleccion de lecciones
	 * 
	 * @param selectedItem
	 */
	public void selectedSubject(String selectedItem) {

		this.subject = YamlReaderClass.cargaTema(selectedLanguage, selectedItem);
		changeView(new LessonsMenu(), null, 0, selectedLanguage, null);
	}

	/**
	 * Carga los componenetes de la leccion, y muestra la ventana con la primera
	 * explicación, se llamará cuando elijamos una leccion
	 * 
	 * @param selectedItem es la lección seleccionada
	 * 
	 */
	public void selectedLesson(int selectedItem) {
		this.actualLesson = selectedItem;
		this.elems = (ArrayList<Element>) subject.getLessons().get(selectedItem).getElements();
		actualStep = -1;
		enabledSteps = 2;
		visited = new boolean[elems.size() + 2];
		Arrays.fill(visited, Boolean.FALSE);
		visited[0] = true;
		changeView(new Content(), null, actualLesson, selectedLanguage, 0);
	}

	/**
	 * Parsea el texto en markdown y lo transforma en texto HTML
	 * 
	 * @param mark
	 * @return texto en formato HTML
	 */
	public String markToHtml(String mark) {
		return new InternalUtilities().parserMarkDown(mark);
	}

	/**
	 * Muesta el FileChooser para seleccionar donde se encuentra el interprete en el
	 * equipo
	 */
	public void showSelection(Language l) {
		// diferenciar si l tiene lenguaje o no... en funcion de eso es el path
		// de directorio o el de lenguaje
		PathChooser sp;
		if (l == null) { // si no llega lenguaje es que hemos cambiado el path
							// del directorio
							// lo guardamos en la variable y en preferences
			sp = new PathChooser(this.primaryStage);
			externalResourcesPath = sp.getPath();
		} else {
			sp = new PathChooser(this.primaryStage, l.getLanguage());
			language = new Language(l.getLanguage(), sp.getPath());
		}
	}

	/**
	 * Actualiza el lenguaje seleccionado y el path del archivo de ejecucion
	 * 
	 * @param selectedItem
	 */
	public void selectedLanguage(String selectedItem) {
		selectedLanguage = selectedItem;
		executable = pathSelected();
		showSubject();
	}

	/**
	 * Devuelve el path del lenguaje seleccionado
	 * @return
	 */
	public String pathSelected() {

		return pref.get(selectedLanguage, null);
	}
	
	/**
	 * Devuelve la ventana principal
	 * @return
	 */
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	/**
	 * Devuelve la vista
	 */
	public Scene getScene() {

		return this.scene;
	}

	/**
	 * 
	 * @return Element actual de la leccion
	 */
	public int getActualStep() {

		return this.actualStep;
	}

	/**
	 * 
	 * @return Lista de elementos de una leccion
	 */
	public ArrayList<Element> getElems() {
		return this.elems;
	}

	/**
	 * Modifica la vista de Content
	 * 
	 * @param newStep
	 */
	public void lessonPageChange(Number newStep) {
		actualStep = (int) newStep - 2;
		changeView(new Content(), null, actualLesson, selectedLanguage, newStep);
	}

	/**
	 * Actualiza el estado del Paginator
	 * 
	 * @param newStep
	 * @param isQuestion
	 */
	public void stepChange(Number newStep, boolean isQuestion) {
		// -2 porque en nuestra indexación hay -1 que es la intro y elemento 0
		if (!isQuestion)
			enableNextStep((int) newStep);
	}

	/**
	 * Habilita el siguiente elemento del actual en el Paginator. Solo si no es
	 * pregunta
	 */
	public void enableNextStep(int actual) {

		if (!visited[actual - 1]) {
			visited[actual - 1] = true;
			enabledSteps += 1;
		}
	}

	/**
	 * 
	 * @param path externalResources path
	 * @param data lista de lenguajes disponibles en la carpeta
	 * 
	 * Este metodo guarda en preferences el path, los lenguajes y las
	 * rutas de los compiladores y pone en el classpath la ruta para
	 * usar con antlr/reflections
	 */
	public void savePrefs(String path, List<Language> data) {

		if (path != null && !data.isEmpty()) {
			pref.put("ExternalResources", path);
			for (Language l : data) {
				pref.put(l.getLanguage(), l.getPath());
			}
		
		} else {
			// TODO error que diga que no hay directorio o compiladores (aqui o en la vista?)
		}
	}
	
	/**
	 * Muestra la ventana de configuracion
	 */
	public void showSettings() {
		Pane p = new Configuration();
		List<String> a = languageNames();
		changeView(p, a, 0, selectedLanguage, null);
	}

	/**
	 * 
	 * @return Lista de Lenguajes para la tabla
	 */
	public List<Language> getLanguagesList() {
		List<String> lanL = InternalUtilities.getDirectoryList(externalResourcesPath);
		List<Language> l = new ArrayList<Language>();//Lista de lenguajes
		if (!lanL.isEmpty()) {
			for (String s : lanL) {
				Language addedL = new Language(s, pref.get(s, null));
				l.add(addedL);
			}

		}
		return l;
	}
	
	/**
	 * 
	 * @return lenguage seleccionado
	 */
	public Language getLanguageAttributes() {
		return language;
	}
	
	/**
	 * Muestra el menu de lecciones
	 */
	public void backLessonsMenu() {
		changeView(new LessonsMenu(), null, 0, selectedLanguage, null);
		
	}

	/**
	 * Muestra el mensaje de fin de lecion
	 */
	public void finishedLesson() {
		new EndLessonPane(this);
		
	}
}
