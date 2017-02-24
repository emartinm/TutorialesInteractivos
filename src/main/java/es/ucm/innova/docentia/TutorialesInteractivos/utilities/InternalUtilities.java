package es.ucm.innova.docentia.TutorialesInteractivos.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.util.KeepType;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import es.ucm.innova.docentia.TutorialesInteractivos.controller.Controller;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Node;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;


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
    private static final MutableDataHolder OPTIONS = new MutableDataSet()
            .set(Parser.REFERENCES_KEEP, KeepType.LAST)
            .set(HtmlRenderer.INDENT_SIZE, 2)
            .set(HtmlRenderer.PERCENT_ENCODE_URLS, true)

            // for full GFM table compatibility add the following table extension options:
            .set(TablesExtension.COLUMN_SPANS, false)
            .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
            .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
            .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
            .set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
    private static Parser PARSER = Parser.builder(OPTIONS).build();
    private static HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();
    private static String javascript;
    private static String pre = inicioWebPage();
    private static String post = finWebPage();

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

	public static String resourceToString(String res) {
        InputStream is = InternalUtilities.class.getResourceAsStream(res);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
        return buffer.lines().collect(Collectors.joining("\n"));
    }

    private static String inicioWebPage() {
	    return "<html>\n" +
                "<head>\n" +
                "\t<script>" +
                resourceToString("/js/scripts.js") + "\n" +
                "\t</script>\n" +
                "</head>\n" +
                "<body onload=\"capturaEnlaces();\">\n";
    }

    private static String finWebPage() {
	    return "</body>\n" +
                "</html>";
    }

	// Permitir llamar a esta funcion y modificar el html que se le pasa o
	// modificar el webview desde donde se hae la llamada
	public static Node creaBrowser(String html, Controller c) {
        if (browser == null) {
            browser = new WebView();
            browser.setContextMenuEnabled(false);
            webEngine = browser.getEngine();
            webEngine.setUserStyleSheetLocation(InternalUtilities.class.getResource("/css/webView.css").toString() );

            // process page loading
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                @Override
                public void changed(ObservableValue ov, State oldState, State newState) {
                    // Añade al DOM el objeto "puente" entre JavaScript y Java. Se llamará bridge y está situado en "window"
                    if (newState == Worker.State.SUCCEEDED) {
                        //System.out.println("READY");
                        JSObject jsobj = (JSObject) webEngine.executeScript("window");
                        jsobj.setMember("bridge", new JavaBridge(c));
                    }
                }
            });
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

	    html = pre + html + post;
	    webEngine.loadContent(html, "text/html");

        // Para depurar el código que se muestra
        /*webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
                InternalUtilities.enableFirebug(webEngine);
            }
        });*/
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
	public String parserMarkDown(String mark, String baseDir) {
		String html;
		//Parser parser = Parser.builder().build();
        com.vladsch.flexmark.ast.Node document = PARSER.parse(mark);
		//HtmlRenderer renderer = HtmlRenderer.builder().build();
		html = RENDERER.render(document);

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
