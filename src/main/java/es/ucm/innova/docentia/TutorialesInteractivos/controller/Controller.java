package es.ucm.innova.docentia.TutorialesInteractivos.controller;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
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
import es.ucm.innova.docentia.TutorialesInteractivos.view.SubjectsMenu;
import javafx.application.HostServices;
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
    private static ResourceBundle localization = ResourceBundle.getBundle("i18n.lang", Locale.getDefault());
    //private static ResourceBundle localization = ResourceBundle.getBundle("i18n.lang", Locale.ENGLISH); //Para hacer pruebas en inglés

	private Stage primaryStage;// Vista principal de la aplicación
	private HostServices hostservices;
	private Pane root;// Panel con los elementos de la vista
	private Scene scene;

    private Subject subject; // Subject que se está ejecutando
    private int currentLesson; // Índice de lección actual dentro del tema actual
	private String selectedLanguage; // lenguaje seleccionado

	private List<String> files;// temas del lenguaje
	private ConfigurationData config;
	private Map<String, Object> progress;
	private Language language;

	
	/**
	 * Constructora 
	 * @param primaryStage
	 */
	public Controller(Stage primaryStage, HostServices hs) {
		this.subject = null;
		this.primaryStage = primaryStage;
		this.hostservices = hs;
		this.files = new ArrayList<>();
		this.config = new ConfigurationData();

        this.primaryStage.setX(50);
        this.primaryStage.setY(50);
        this.primaryStage.setTitle(this.localization.getString("app_name"));
        this.primaryStage.setMinWidth(600);
        this.primaryStage.setMinHeight(400);
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

	//public Correction check(List<Integer> resp, Question p) {
	//	return p.check(resp, null);
	//}

	/**
	 * Función correctora de las preguntas de tipo Code y sintaxis
	 * 
	 * @param resp
	 * @param p
	 * @return
	 */
	//public Correction check(List<String> resp, Question p) {
		//return p.check(resp, subject);
	//	return p.check(resp, this.language );
	//}

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
            } /*else {
				e = this.getCurrentLesson().getCurrentElement();
			}*/
            root = p;
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
			alert.setTitle(Controller.getLocalizedString("controller.warning"));
			alert.setHeaderText( Controller.getLocalizedString("controller.language") + " " + selectedLanguage +
                    " " + Controller.getLocalizedString("controller.notConfig"));
			alert.setContentText(Controller.getLocalizedString("controller.redirect"));
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
		changeView(new Content(this, getCurrentLesson()), null, selectedLanguage);
	}

	public void passCurrentElement() {
		int pos = this.getCurrentStep();
		if ( pos + 1 > this.getCurrentLesson().getLatestEnabledElement() ) {
			this.getCurrentLesson().setLatestEnabledElement(pos + 1);
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

    public String getCurrentLanguage() {
		return this.selectedLanguage;
	}

	public Language getLanguage() {
		return this.language;
	}

	public void openWebPage(String url) {
		final String os_name = System.getProperty("os.name");
		if (os_name.toLowerCase().contains("windows")) {
			Desktop dt = Desktop.getDesktop();
			URI uri = null;
			try {
				uri = new URI(url);
				dt.browse(uri);
			} catch (Exception e) {
				this.log.warning("Error al abrir el navegador: " + e.getLocalizedMessage() );
			}
		} else {
			// Linux y Mac
			this.hostservices.showDocument(url);
		}
    }

    public static String getLocalizedString(String key) {
	    String val = localization.getString(key);
	    try {
            return new String(val.getBytes("ISO-8859-1"), "UTF-8");
	    } catch (java.io.UnsupportedEncodingException e) {
	        log.warning("Error al obtener cadena traducida: " + e.getLocalizedMessage());
        }
        return val;
    }

}
