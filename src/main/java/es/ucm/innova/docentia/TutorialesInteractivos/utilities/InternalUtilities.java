package es.ucm.innova.docentia.TutorialesInteractivos.utilities;

import java.io.File;
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

import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.w3c.dom.Document;

import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Node;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.html.HTMLEditorKit;

import javax.swing.*;

/**
 * Clase que contiene los metodos de modificaion de elementos dentro de la
 * aplicacion
 * 
 * @author Carlos, Rafa, Enrique Martín
 *
 */
public class InternalUtilities {
    private static WebView browser = null;
    private static WebEngine webEngine = null;

	/**
	 * Modifica el la ruta de la imagen dentro del HTML
	 * 
	 * @param html
	 * @return
	 */
	private String modifyImg(String html, String baseDir) {
		// Definimos el patrón a buscar
		String pattern = "(<img src=\"file:///)(.*?)(\".*?>)"; 
		// En el yaml el formato ha de ser "file:///"+ ruta relativa a la imagen

		// Compilar el patron ignorando si esta en mayusculas o minusculas
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(html);

		while (matcher.find()) {
			Controller.log.info(matcher.group(2));
			File f = new File(baseDir + "/" + matcher.group(2));
			String im = f.getPath();
			html = html.replace(matcher.group(), matcher.group(1) + im + matcher.group(3));
			
		}
		return html;
	}

	// Permitir llamar a esta funcion y modificar el html que se le pasa o
	// modificar el webview desde donde se hae la llamada
	public static Node creaBrowser(String html) {
        if (browser == null) {
            browser = new WebView();
            webEngine = browser.getEngine();
            webEngine.setUserStyleSheetLocation(InternalUtilities.class.getResource("/css/webView.css").toString() );
        }
        //Deshabilitado el workaround para mostrar HMTL en Windows
	    /*
	    String os_name = System.getProperty("os.name");
	    os_name = "";
        if (os_name.toLowerCase().contains("windows")) {
            JTextPane jp = new JTextPane();
            jp.setContentType( "text/html" );
            jp.setText(html);
            jp.setEditable(false);

            InputStream file;
            String aux = "";
            StyleSheet ss = new StyleSheet();
            ss.importStyleSheet(InternalUtilities.class.getClassLoader().getResource("css/webView.css"));
            HTMLEditorKit kit = (HTMLEditorKit)jp.getEditorKit();
            kit.setStyleSheet(ss);

            SwingNode browser = new SwingNode();
            browser.setContent(jp);
            return browser;
        } else {*/
            webEngine.loadContent(html, "text/html");
            //webEngine.load("http://i.imgur.com/V6aroSo.gif");
            // Para depurar el código que se muestra
            /*webEngine.documentProperty().addListener(new ChangeListener<Document>() {
                @Override public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
                    InternalUtilities.enableFirebug(webEngine);
                }
            });*/
            System.out.println(html);
            return browser;
        //}

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
	public String parserMarkDown(String mark, String baseDir) {
		String html;
		Parser parser = Parser.builder().build();
        com.vladsch.flexmark.ast.Node document = parser.parse(mark);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		html = renderer.render(document);

		html = modifyImg(html, baseDir);
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
	}

	public static void enableFirebug(final WebEngine engine) {
		engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
	}
}
