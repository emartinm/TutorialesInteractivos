package es.ucm.innova.docentia.TutorialesInteractivos.controller;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.nio.file.FileSystems;

import es.ucm.innova.docentia.TutorialesInteractivos.model.*;
import es.ucm.innova.docentia.TutorialesInteractivos.model.language.Language;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.InternalUtilities;
import es.ucm.innova.docentia.TutorialesInteractivos.utilities.JSONReaderClass;
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
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * Clase controlador. Ejecuta todas las variaciones de la aplicación
 * 
 * @author Carlos, Rafa, Enrique Martín
 *
 */
public class Controller {
    public static final Logger log = Logger.getLogger("TutorialesInteractivos"); // Logger común para toda la aplicación
    public static final String progressFileName = "progress.json";
    public static String executable;// ejecutable del lenguaje para ejecutar código
    public static String selectedLanguage; // lenguaje seleccionado
    //public static String externalResourcesPath;

	private Stage primaryStage;// Vista principal de la aplicación
	private Pane root;// Panel con los elementos de la vista
	private Scene scene;

    private Subject subject; // Subject que se está ejecutando
    private int currentLesson; // Índice de lección actual dentro del tema actual

	private List<String> files;// temas del lenguaje
	private ConfigurationData config;
	private Map<String, Object> progress;
	private Language language;

	
	/**
	 * Constructora 
	 * @param primaryStage
	 */
	public Controller(Stage primaryStage) {
		this.subject = null;
		this.primaryStage = primaryStage;
		this.files = new ArrayList<>();
		this.config = new ConfigurationData();
		//this.pref = Preferences.userNodeForPackage(this.getClass());

		// TODO
        // Para redirigir la salida del logger a un fichero
        //Handler handler = new FileHandler("test.log", LOG_SIZE, LOG_ROTATION_COUNT);
        //Handler handler = new FileHandler("test.log");
        //logger.addHandler(handler);
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
	public Correction check(List<Integer> resp, Question p) {
		return p.check(resp, null);
	}

	/**
	 * Función correctora de las preguntas de tipo Code y sintaxis
	 * 
	 * @param resp
	 * @param p
	 * @return
	 */
	public Correction check(String resp, Question p) {
		//return p.check(resp, subject);
		String langDir = getConfig().getDirTemas() + FileSystems.getDefault().getSeparator() + "python34";
		return p.check(resp, this.language );
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
			final Path path = Paths.get(getConfig().getDirTemas() + "/" + selectedLanguage);
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

		changeView(p, files, selectedLanguage);
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
		getConfig().load();

		if (getConfig().isDirTemas()) {
            progress = JSONReaderClass.loadJSON(getConfig().getDirTemas() + FileSystems.getDefault().getSeparator() + Controller.progressFileName);
			languagesList = languageNames();
			p = new InitialWindow();
		} else {
			p = new Configuration(); // hace falta configurar directorio y compiladores
		}
		Image icon = new Image(Controller.class.getResourceAsStream( "/icon/1477275626_monitor-sidebar.png" ));
		this.primaryStage.getIcons().add(icon);
		changeView(p, languagesList, selectedLanguage);

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
		return InternalUtilities.getDirectoryList(getConfig().getDirTemas());

	}

	/**
	 * Muestra la primera ventana de la aplicacion
	 */
	public void showStart() {
		//primaryStage.setTitle(selectedLanguage);
		Pane p = new SubjectsMenu();
		changeView(p, files, selectedLanguage);
	}

	/**
	 * Muestra la ventana de configuración de lenguajes
	 */
	public void showConfiguration() {
		Pane p = new Configuration();
		changeView(p, null, selectedLanguage);
	}

	/**
	 * Modifica la vista que se muestra en el momento
	 * 
	 * @param p Panel a mostrar         
	 * @param files Lista de los ficheros que componen el temario           
	 * @param lenSelect Language seleccionado
	 */
	private void changeView(Pane p, List<String> files, String lenSelect) {
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
			if (getCurrentLesson().getCurrentElementPos() == getCurrentLessonParts().size()) {
                updateAndSaveCurrentLessonProgress();
            } else {
				e = this.getCurrentLesson().getCurrentElement();
			}
            root = ((Content) p).content(this, getCurrentLesson());
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

		this.subject = YamlReaderClass.cargaTema(getConfig().getDirTemas(), selectedLanguage, selectedItem);
		// 'progress' ha sido cargado anteriormente
		this.subject.loadProgress(progress);
		changeView(new LessonsMenu(), null, selectedLanguage);
	}

	/**
	 * Carga los componenetes de la leccion, y muestra la ventana con la primera
	 * explicación, se llamará cuando elijamos una leccion
	 * 
	 * @param selectedItem es la lección seleccionada
	 * 
	 */
	public void selectedLesson(int selectedItem) {
		this.currentLesson = selectedItem;
        Lesson le = subject.getLessons().get(selectedItem);
        this.lessonPageChange(le.getCurrentElementPos() );
	}

	/**
	 * Parsea el texto en markdown y lo transforma en texto HTML
	 * 
	 * @param mark
	 * @return texto en formato HTML
	 */
	public String markToHtml(String mark) {
		return new InternalUtilities().parserMarkDown(mark, getConfig().getDirTemas() + "/" + selectedLanguage);
	}

	/**
	 * Muesta el FileChooser para seleccionar donde se encuentra el interprete en el
	 * equipo
	 */
	public String showSelection(String l) {
		// diferenciar si l tiene lenguaje o no... en funcion de eso es el path
		// de directorio o el de lenguaje
		PathChooser sp;
		if (l == null) { // si l==null queremos buscar un directorio
			sp = new PathChooser(this.primaryStage);
		} else {
			sp = new PathChooser(this.primaryStage, l);
		}
		return sp.getPath();
	}

	/**
	 * Actualiza el lenguaje seleccionado y el path del archivo de ejecucion
	 * 
	 * @param selectedItem
	 */
	public void selectedLanguage(String selectedItem) {
		selectedLanguage = selectedItem;
		language = Language.languageFactory(selectedLanguage, getConfig().getDirTemas() + "/" + selectedLanguage, getConfig());
		if (language == null || !language.isConfigured() ) {
			// El lenguaje no está configurado
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("¡Atención!");
			alert.setHeaderText("El lenguaje \"" + selectedLanguage + "\" no está configurado");
			alert.setContentText("Serás redirigido a la ventana de configuración.\nPor favor, configura el compilador/intérprete\ndel lenguaje \"" +  selectedLanguage + "\".");
			alert.showAndWait();
			this.showConfiguration();
		} else {
			showSubject();
		}
	}

	/**
	 * Devuelve el path del lenguaje seleccionado
	 * @return
	 */
	public String pathSelected() {
	    return getConfig().get(selectedLanguage);
		//return pref.get(selectedLanguage, null);
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
	public int getCurrentStep() {
	    return this.getCurrentLesson().getCurrentElementPos();
	}

	/**
	 * 
	 * @return Lista de elementos de una leccion
	 */
	public List<Element> getElems() {
		return this.getCurrentLessonParts();
	}

	/**
	 * Modifica la vista de Content
	 * 
	 * @param pos
	 */
	public void lessonPageChange(int pos) {
	    Lesson le = this.getCurrentLesson();
		le.setCurrentElementPos(pos);
		boolean esExplicacion = (pos < le.getElements().size() && le.getElements().get(pos) instanceof Explanation);
		if ( esExplicacion ) {
        	passCurrentElement();
		}
		updateAndSaveCurrentLessonProgress();
		changeView(new Content(), null, selectedLanguage);
	}

	public void passCurrentElement() {
		int pos = this.getCurrentStep();
		if ( pos + 1 > this.getCurrentLesson().getLatestElement() ) {
			this.getCurrentLesson().setLatestElement(pos + 1);
		}
	}
	
	/**
	 * Muestra la ventana de configuracion
	 */
	public void showSettings() {
		Pane p = new Configuration();
		List<String> a = languageNames();
		changeView(p, a, selectedLanguage);
	}

	/**
	 * 
	 * @return Lista de Lenguajes para la tabla
	 */
	public List<String> getLanguagesList() {
		List<String> lanL = InternalUtilities.getDirectoryList(getConfig().getDirTemas());
		List<String> l = new ArrayList<>();//Lista de lenguajes
		if (!lanL.isEmpty()) {
			for (String s : lanL) {
                l.add(s);
			}

		}
		return l;
	}
	
	/**
	 * Muestra el menu de lecciones
	 */
	public void backLessonsMenu() {
		changeView(new LessonsMenu(), null, selectedLanguage);
		
	}

	/**
	 * Muestra el mensaje de fin de lecion
	 */
	public void finishedLesson() {
		new EndLessonPane(this);
	}


	public void updateAndSaveCurrentLessonProgress() {
	    Lesson le = this.getCurrentLesson();
	    Map<String, Object> p = le.get_progress();
	    this.progress.put( le.version(), p );
	    String path = getConfig().getDirTemas() + FileSystems.getDefault().getSeparator() + Controller.progressFileName;
        JSONReaderClass.writeProgress(this.progress, path);
    }

    public Map<String, Object> getProgress() {
    	return this.progress;
	}

	List<Element> getCurrentLessonParts() {
        return getCurrentLesson().getElements();
    }

    Lesson getCurrentLesson() {
        return this.subject.getLessons().get(currentLesson);
    }

    public void reloadCurrentLessonFragment() {
        this.lessonPageChange( this.getCurrentStep() );
    }

    public ConfigurationData getConfig() {
        return config;
    }

}
