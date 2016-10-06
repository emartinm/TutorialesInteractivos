package TFG.TutorialesInteractivos.utilities;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.w3c.dom.Document;

import TFG.TutorialesInteractivos.controller.Controller;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Clase que contiene los metodos de modificaion de elementos dentro de la
 * aplicacion
 * 
 * @author Carlos, Rafa
 *
 */
public class InternalUtilities {
	/**
	 * Modifica el la ruta de la imagen dentro del HTML
	 * 
	 * @param html
	 * @return
	 */
	private String modifyImg(String html) {
		// Definimos el patrón a buscar
		String pattern = "(<img src=\"file:///)(.*?)(\".*?>)"; 
		// En el yaml el formato ha de ser "file:///"+ ruta relativa a la imagen

		// Compilar el patron ignorando si esta en mayusculas o minusculas
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(html);

		while (matcher.find()) {
			System.out.println(matcher.group(2));
			File f = new File(Controller.externalResourcesPath + "/" + matcher.group(2));
			String im = f.getPath();
			html = html.replace(matcher.group(), matcher.group(1) + im + matcher.group(3));
			
		}
		return html;
	}

	// Permitir llamar a esta funcion y modificar el html que se le pasa o
	// modificar el webview desde donde se hae la llamada
	public static WebView creaBrowser(String html) {
		InputStream file;
		String aux = "";
		file = InternalUtilities.class.getClassLoader().getResourceAsStream("css/webView.css");

		aux = fileToString(file);

		final String CSS = aux;
		WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();

		webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				Document doc = webEngine.getDocument();
				org.w3c.dom.Element styleNode = doc.createElement("style");
				org.w3c.dom.Text styleContent = doc.createTextNode(CSS);
				styleNode.appendChild(styleContent);
				doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);
			}
		});
		webEngine.loadContent(html);

		return browser;
	}

	/**
	 * Pasa todo el contenido de un fichero a una cadena
	 * 
	 * @param file
	 *            Nomobre del fichero
	 * @return
	 */
	private static String fileToString(InputStream file) {
		String style = "";
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			style += sc.nextLine() + "\n";
		}
		sc.close();

		return style;
	}

	/**
	 * Parsea el texto en markdown y lo pasa a texto HTML
	 * 
	 * @param mark
	 * @return
	 */
	public String parserMarkDown(String mark) {
		String html;
		PegDownProcessor pro = new PegDownProcessor(Extensions.ALL - Extensions.EXTANCHORLINKS);
		html = pro.markdownToHtml(mark);
		
		html = modifyImg(html);
		return html;
	}

	public static List<String> getDirectoryList(String path) {
        Path dir = Paths.get(path);
        List<String> foldersInDirectory = new ArrayList<String>();
        try {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir);
            for (Path p : dirStream) {
                if (Files.isDirectory(p)) {
                    foldersInDirectory.add(p.getName(p.getNameCount()-1).toString());
                }

            }
        } catch (java.io.IOException e){
            e.printStackTrace();
        }

        return foldersInDirectory;
		/*File directory = new File(path);

		FileFilter directoryFileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        System.out.println("Cosa" + directoryListAsFile);
		List<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
		for (File directoryAsFile : directoryListAsFile) {
			foldersInDirectory.add(directoryAsFile.getName());
		}

		return foldersInDirectory;
		*/
	}
}
